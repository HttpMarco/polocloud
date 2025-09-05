import { NextRequest, NextResponse } from 'next/server';

interface WebSocketMessage {
  type: 'message' | 'disconnect';
  data?: string;
  code?: number;
  timestamp: number;
}

const wsConnections = new Map<string, WebSocket>();
const messageQueues = new Map<string, WebSocketMessage[]>();

export async function POST(request: NextRequest) {
  try {
    const { backendIp, path } = await request.json();

    const token = request.cookies.get('token')?.value;
    const cookieBackendIp = request.cookies.get('backend_ip')?.value;

    const finalBackendIp = backendIp || (cookieBackendIp ? decodeURIComponent(cookieBackendIp) : null);
    
    if (!finalBackendIp || !path || !token) {
      return NextResponse.json({ 
        success: false, 
        error: 'Missing required parameters (backendIp, path, or token)' 
      }, { status: 400 });
    }

    const connectionKey = `${finalBackendIp}${path}`;

    if (wsConnections.has(connectionKey)) {
      const existingWs = wsConnections.get(connectionKey);
      if (existingWs && existingWs.readyState === WebSocket.OPEN) {
        return NextResponse.json({
          success: true,
          message: 'Already connected',
          method: 'websocket'
        });
      } else {
        existingWs?.close();
        wsConnections.delete(connectionKey);
        messageQueues.delete(connectionKey);
      }
    }

    try {
      const protocol = determineBackendProtocol(finalBackendIp);
      const wsUrl = `${protocol}://${finalBackendIp}/polocloud/api/v3${path}?token=${token}`;

      const ws = new WebSocket(wsUrl);
      
      return new Promise<NextResponse>((resolve) => {
        const timeout = setTimeout(() => {
          ws.close();
          resolve(NextResponse.json({
            success: false,
            error: 'Connection timeout'
          }, { status: 408 }));
        }, 10000);

        ws.onopen = () => {
          clearTimeout(timeout);

          wsConnections.set(connectionKey, ws);
          messageQueues.set(connectionKey, []);
          
          resolve(NextResponse.json({
            success: true,
            message: 'Connected via WebSocket',
            method: 'websocket',
            protocol
          }));
        };

        ws.onmessage = (event) => {
          if (!messageQueues.has(connectionKey)) {
            messageQueues.set(connectionKey, []);
          }
          
          const queue = messageQueues.get(connectionKey)!;
          queue.push({
            type: 'message',
            data: event.data,
            timestamp: Date.now()
          });

          if (queue.length > 500) {
            queue.splice(0, queue.length - 500);
          }
        };

        ws.onclose = (event) => {
          wsConnections.delete(connectionKey);

          const queue = messageQueues.get(connectionKey);
          if (queue) {
            queue.push({
              type: 'disconnect',
              code: event.code,
              timestamp: Date.now()
            });
          }
        };

        ws.onerror = () => {
          clearTimeout(timeout);
          
          wsConnections.delete(connectionKey);
          
          resolve(NextResponse.json({
            success: false,
            error: 'WebSocket connection failed'
          }, { status: 500 }));
        };
      });

    } catch {
      return NextResponse.json({
        success: false,
        error: 'Failed to create WebSocket connection'
      }, { status: 500 });
    }

  } catch {
    return NextResponse.json({
      success: false,
      error: 'Internal server error'
    }, { status: 500 });
  }
}

function determineBackendProtocol(backendIp: string): 'ws' | 'wss' {
  if (backendIp.includes('localhost') || 
      backendIp.includes('127.0.0.1') || 
      backendIp.startsWith('192.168.') ||
      backendIp.startsWith('10.')) {
    return 'ws';
  }

  if (backendIp.includes('https') || backendIp.includes(':443')) {
    return 'wss';
  }

  return 'ws';
}

export { wsConnections, messageQueues };

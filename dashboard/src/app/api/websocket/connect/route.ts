import { NextRequest, NextResponse } from 'next/server';
import { logError } from '@/lib/error-handling';
import { wsConnections, messageQueues, cleanupConnection } from '@/lib/websocket-connections';


let cleanupInterval: NodeJS.Timeout | null = null;

const startCleanupInterval = () => {
  if (cleanupInterval) return;
  
  cleanupInterval = setInterval(() => {
    const now = Date.now();
    const maxAge = 5 * 60 * 1000;
    const maxQueueSize = 1000;
    
    wsConnections.forEach((ws, key) => {
      if (ws.readyState !== WebSocket.OPEN) {
        cleanupConnection(key);
        return;
      }

      const queue = messageQueues.get(key);
      if (queue) {
        if (queue.length > maxQueueSize) {
          cleanupConnection(key);
          return;
        }

        if (queue.length > 0) {
          const lastMessage = queue[queue.length - 1];
          if (now - lastMessage.timestamp > maxAge) {
            cleanupConnection(key);
            return;
          }
        }
      }
    });
  }, 60000);
};
startCleanupInterval();

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

        ws.onclose = () => {
          cleanupConnection(connectionKey);
        };

        ws.onerror = () => {
          clearTimeout(timeout);

          cleanupConnection(connectionKey);
          
          resolve(NextResponse.json({
            success: false,
            error: 'WebSocket connection failed'
          }, { status: 500 }));
        };
      });

    } catch (error) {
      logError(error, { 
        component: 'WebSocketConnect', 
        action: 'createWebSocketConnection' 
      });
      return NextResponse.json({
        success: false,
        error: 'Failed to create WebSocket connection'
      }, { status: 500 });
    }

  } catch (error) {
    logError(error, { 
      component: 'WebSocketConnect', 
      action: 'mainHandler' 
    });
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

import { NextRequest, NextResponse } from 'next/server';
import { wsConnections } from '../connect/route';

export async function POST(request: NextRequest) {
  try {
    const { backendIp, path, message } = await request.json();

    const token = request.cookies.get('token')?.value;
    const cookieBackendIp = request.cookies.get('backend_ip')?.value;

    const finalBackendIp = backendIp || (cookieBackendIp ? decodeURIComponent(cookieBackendIp) : null);
    
    if (!finalBackendIp || !path || !token || !message) {
      return NextResponse.json({ 
        success: false, 
        error: 'Missing required parameters (backendIp, path, token, or message)' 
      }, { status: 400 });
    }

    const connectionKey = `${finalBackendIp}${path}`;

    const ws = wsConnections.get(connectionKey);
    
    if (!ws || ws.readyState !== WebSocket.OPEN) {
      return NextResponse.json({
        success: false,
        error: 'No active WebSocket connection found'
      }, { status: 404 });
    }

    try {
      const messageData = JSON.stringify(message);
      ws.send(messageData);

      
      return NextResponse.json({
        success: true,
        message: 'Message sent successfully'
      });
      
    } catch {
      
      return NextResponse.json({
        success: false,
        error: 'Failed to send message'
      }, { status: 500 });
    }

  } catch {
    return NextResponse.json({
      success: false,
      error: 'Internal server error'
    }, { status: 500 });
  }
}

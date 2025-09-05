import { NextRequest, NextResponse } from 'next/server';
import { wsConnections, messageQueues } from '../connect/route';

export async function POST(request: NextRequest) {
  try {
    const { backendIp, path } = await request.json();

    const cookieBackendIp = request.cookies.get('backend_ip')?.value;
    const finalBackendIp = backendIp || (cookieBackendIp ? decodeURIComponent(cookieBackendIp) : null);
    
    if (!finalBackendIp || !path) {
      return NextResponse.json({ 
        success: false, 
        error: 'Missing required parameters (backendIp or path)' 
      }, { status: 400 });
    }

    const connectionKey = `${finalBackendIp}${path}`;

    // ✅ VERBESSERT: Manuelles Cleanup
    const ws = wsConnections.get(connectionKey);
    if (ws) {
      try {
        ws.close();
      } catch {
        // WebSocket bereits geschlossen
      }
    }
    wsConnections.delete(connectionKey);
    messageQueues.delete(connectionKey);

    return NextResponse.json({
      success: true,
      message: 'Disconnected successfully'
    });

  } catch {
    return NextResponse.json({
      success: false,
      error: 'Internal server error'
    }, { status: 500 });
  }
}

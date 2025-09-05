import { NextRequest, NextResponse } from 'next/server';
import { messageQueues } from '../connect/route';

const clientPollingState = new Map<string, {
  lastPolledIndex: number;
  lastPollTime: number;
}>();

export async function POST(request: NextRequest) {
  try {
    const { backendIp, path, clientId } = await request.json();

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
    const pollingClientId = clientId || `${connectionKey}_${Date.now()}`;

    const queue = messageQueues.get(connectionKey);
    if (!queue) {
      return NextResponse.json({
        success: true,
        messages: [],
        hasMore: false
      });
    }

    let clientState = clientPollingState.get(pollingClientId);
    if (!clientState) {
      clientState = {
        lastPolledIndex: 0,
        lastPollTime: Date.now()
      };
      clientPollingState.set(pollingClientId, clientState);
    }

    const newMessages = queue.slice(clientState.lastPolledIndex);

    clientState.lastPolledIndex = queue.length;
    clientState.lastPollTime = Date.now();

    return NextResponse.json({
      success: true,
      messages: newMessages,
      hasMore: newMessages.length > 0,
      clientId: pollingClientId,
      totalMessages: queue.length
    });

  } catch {
    return NextResponse.json({
      success: false,
      error: 'Internal server error'
    }, { status: 500 });
  }
}

setInterval(() => {
  const now = Date.now();
  const fiveMinutes = 5 * 60 * 1000;
  
  for (const [clientId, state] of clientPollingState.entries()) {
    if (now - state.lastPollTime > fiveMinutes) {
      clientPollingState.delete(clientId);
    }
  }
}, 5 * 60 * 1000);

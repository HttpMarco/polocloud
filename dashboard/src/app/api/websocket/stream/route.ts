import { NextRequest, NextResponse } from 'next/server';

import { messageQueues } from '../connect/route';

const sseClients = new Map<string, ReadableStreamDefaultController>();

export async function GET(request: NextRequest) {
  const { searchParams } = new URL(request.url);
  const backendIp = searchParams.get('backendIp');
  const path = searchParams.get('path');

  const token = request.cookies.get('token')?.value;
  const cookieBackendIp = request.cookies.get('backend_ip')?.value;

  const finalBackendIp = backendIp || (cookieBackendIp ? decodeURIComponent(cookieBackendIp) : null);

  if (!finalBackendIp || !path || !token) {
    return NextResponse.json({ 
      error: 'Missing required parameters (backendIp, path, or token)' 
    }, { status: 400 });
  }

  const connectionKey = `${finalBackendIp}${path}`;
  const clientKey = `${connectionKey}_${Date.now()}_${Math.random()}`;


  const stream = new ReadableStream({
    start(controller) {
      sseClients.set(clientKey, controller);

      controller.enqueue(
        new TextEncoder().encode(
          `data: ${JSON.stringify({ 
            type: 'connected', 
            timestamp: Date.now(),
            clientKey 
          })}\n\n`
        )
      );

      const queue = messageQueues.get(connectionKey);
      if (queue && queue.length > 0) {

        queue.forEach(message => {
          try {
            controller.enqueue(
              new TextEncoder().encode(
                `data: ${JSON.stringify(message)}\n\n`
              )
            );
          } catch (error) {
        console.warn('API error in route:', error);
      }});
      }

      request.signal.addEventListener('abort', () => {
        sseClients.delete(clientKey);
        try {
          controller.close();
        } catch (error) {
        console.warn('API error in route:', error);
      }});
    }
  });

  startMessageForwarding(connectionKey);

  return new Response(stream, {
    headers: {
      'Content-Type': 'text/event-stream',
      'Cache-Control': 'no-cache, no-store, must-revalidate',
      'Connection': 'keep-alive',
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Headers': 'Cache-Control, Content-Type',
      'X-Accel-Buffering': 'no'
    }
  });
}

const forwardingIntervals = new Map<string, NodeJS.Timeout>();

function startMessageForwarding(connectionKey: string) {
  if (forwardingIntervals.has(connectionKey)) {
    return;
  }

  let lastProcessedIndex = 0;
  
  const interval = setInterval(() => {
    const queue = messageQueues.get(connectionKey);
    if (!queue || queue.length === 0) {
      return;
    }

    const newMessages = queue.slice(lastProcessedIndex);
    if (newMessages.length === 0) {
      return;
    }

    lastProcessedIndex = queue.length;

    const relevantClients = Array.from(sseClients.entries())
      .filter(([key]) => key.startsWith(connectionKey));

    if (relevantClients.length === 0) {
      clearInterval(interval);
      forwardingIntervals.delete(connectionKey);
      return;
    }

    newMessages.forEach(message => {
      const sseData = `data: ${JSON.stringify(message)}\n\n`;
      const encodedData = new TextEncoder().encode(sseData);

      relevantClients.forEach(([clientKey, controller]) => {
        try {
          controller.enqueue(encodedData);
        } catch {
          sseClients.delete(clientKey);
        }
      });
    });

  }, 100);

  forwardingIntervals.set(connectionKey, interval);

}

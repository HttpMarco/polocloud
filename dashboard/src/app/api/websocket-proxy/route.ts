import { NextRequest, NextResponse } from 'next/server';

export async function GET(request: NextRequest) {
  try {
    const backendIp = request.cookies.get('backend_ip')?.value;
    if (!backendIp) {
      return NextResponse.json({ error: 'No backend connection found' }, { status: 400 });
    }

    const token = request.cookies.get('token')?.value;
    if (!token) {
      return NextResponse.json({ error: 'No authentication token found' }, { status: 401 });
    }

         const { searchParams } = new URL(request.url);
     const path = searchParams.get('path') || '/logs';
     const service = searchParams.get('service');
     
     // ✅ VERBESSERT: Service-spezifischer Pfad für Service-Screen
     const finalPath = service ? `/service/${service}/screen` : path;

    const decodedBackendIp = decodeURIComponent(backendIp);
    
    // ✅ HTTP-Polling statt WebSocket für Production
    const isHttpsBackend = decodedBackendIp.includes('https') || 
                          decodedBackendIp.includes(':443');
    
    const protocol = isHttpsBackend ? 'https' : 'http';
    const backendUrl = `${protocol}://${decodedBackendIp}/polocloud/api/v3/websocket/stream`;

    // ✅ Server-Sent Events für Frontend
    const encoder = new TextEncoder();
    
    const stream = new ReadableStream({
      start(controller) {
        let isActive = true;
        
        const pollBackend = async () => {
          if (!isActive) return;
          
          try {
            const response = await fetch(backendUrl, {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
                'Cookie': `token=${token}`
              },
                             body: JSON.stringify({
                 path: finalPath,
                 service: service || null
               })
            });

            if (response.ok) {
              const data = await response.json();
              
              if (data.messages && Array.isArray(data.messages)) {
                data.messages.forEach((message: { data?: string; [key: string]: unknown }) => {
                  const sseData = `data: ${JSON.stringify({
                    type: 'message',
                    data: message.data || message,
                    service: service || null
                  })}\n\n`;
                  
                  controller.enqueue(encoder.encode(sseData));
                });
              }
            }
          } catch {
            const sseData = `data: ${JSON.stringify({
              type: 'error',
              error: 'Connection failed'
            })}\n\n`;
            
            controller.enqueue(encoder.encode(sseData));
          }
          
          // Poll alle 2 Sekunden
          if (isActive) {
            setTimeout(pollBackend, 2000);
          }
        };

        // Start polling
        pollBackend();

        // Cleanup function
        return () => {
          isActive = false;
        };
      },
      
      cancel() {
        // Cleanup
      }
    });

    return new Response(stream, {
      headers: {
        'Content-Type': 'text/event-stream',
        'Cache-Control': 'no-cache',
        'Connection': 'keep-alive',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'GET',
        'Access-Control-Allow-Headers': 'Cache-Control'
      }
    });

  } catch (error) {
    console.error('WebSocket proxy error:', error);
    return NextResponse.json({ 
      error: 'WebSocket proxy failed' 
    }, { status: 500 });
  }
}

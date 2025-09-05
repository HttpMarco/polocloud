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
     
     const decodedBackendIp = decodeURIComponent(backendIp);
     
     // ✅ VERBESSERT: Service-spezifischer Pfad für Service-Screen
     const finalPath = service ? `/service/${service}/screen` : path;
     
     console.log('WebSocket Proxy Debug:', {
       path,
       service,
       finalPath,
       backendIp: decodedBackendIp
     });
    
         // ✅ VERBESSERT: Echter WebSocket-Endpoint für /logs
     const isHttpsBackend = decodedBackendIp.includes('https') || 
                           decodedBackendIp.includes(':443');
     
     const wsProtocol = isHttpsBackend ? 'wss' : 'ws';
     const backendWsUrl = `${wsProtocol}://${decodedBackendIp}/polocloud/api/v3${finalPath}`;

         // ✅ VERBESSERT: Echte WebSocket-Verbindung zum Backend
     const encoder = new TextEncoder();
     
     const stream = new ReadableStream({
       start(controller) {
         let isActive = true;
         let ws: WebSocket | null = null;
         
         try {
           // ✅ WebSocket-Verbindung zum Backend herstellen
           ws = new WebSocket(backendWsUrl);
           
           ws.onopen = () => {
             console.log('WebSocket connected to backend:', backendWsUrl);
             
             const sseData = `data: ${JSON.stringify({
               type: 'connected',
               message: 'Connected to backend'
             })}\n\n`;
             
             controller.enqueue(encoder.encode(sseData));
           };
           
           ws.onmessage = (event) => {
             if (!isActive) return;
             
             const message = event.data;
             console.log('WebSocket message from backend:', message);
             
             const sseData = `data: ${JSON.stringify({
               type: 'message',
               data: message,
               service: service || null
             })}\n\n`;
             
             controller.enqueue(encoder.encode(sseData));
           };
           
           ws.onclose = (event) => {
             console.log('WebSocket closed:', event.code, event.reason);
             
             const sseData = `data: ${JSON.stringify({
               type: 'disconnect',
               code: event.code,
               reason: event.reason
             })}\n\n`;
             
             controller.enqueue(encoder.encode(sseData));
             controller.close();
           };
           
           ws.onerror = (error) => {
             console.error('WebSocket error:', error);
             
             const sseData = `data: ${JSON.stringify({
               type: 'error',
               error: 'WebSocket connection failed'
             })}\n\n`;
             
             controller.enqueue(encoder.encode(sseData));
             controller.close();
           };
           
         } catch (error) {
           console.error('Failed to create WebSocket:', error);
           
           const sseData = `data: ${JSON.stringify({
             type: 'error',
             error: 'Failed to create WebSocket connection'
           })}\n\n`;
           
           controller.enqueue(encoder.encode(sseData));
           controller.close();
         }

         // Cleanup function
         return () => {
           isActive = false;
           if (ws) {
             ws.close();
           }
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

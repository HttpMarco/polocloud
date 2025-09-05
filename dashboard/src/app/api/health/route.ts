import { NextRequest, NextResponse } from 'next/server';

export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url);
    const backendUrl = searchParams.get('backend_ip');

    if (!backendUrl) {
      return NextResponse.json({ error: 'Backend URL fehlt' }, { status: 400 });
    }

    const endpoints = [
      `/polocloud/api/v3/health`,
      `/polocloud/api/v3/health/`,
    ];

    for (const endpoint of endpoints) {
      const testUrl = `http://${backendUrl.trim()}${endpoint}`;
      
      try {
        const response = await fetch(testUrl, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json'
          }
        });

        
        if (response.ok) {
          return NextResponse.json({ 
            success: true, 
            message: 'Backend connection successful',
            backendUrl: backendUrl.trim(),
            workingEndpoint: endpoint
          });
        }
      } catch (error) {
        console.log(`Endpoint ${endpoint} error:`, error);
      }
    }

    return NextResponse.json({ 
      success: false, 
      error: 'No working endpoint found. Backend might require authentication.' 
    }, { status: 401 });

  } catch (error) {
    console.error('Health check error:', error);
    return NextResponse.json({
      success: false,
      error: error instanceof Error ? error.message : 'Connection test failed'
    }, { status: 500 });
  }
}
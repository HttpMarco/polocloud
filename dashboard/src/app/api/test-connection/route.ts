import { NextRequest, NextResponse } from 'next/server';
import { buildBackendUrl } from '@/lib/api/utils';

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();
    const { backendUrl } = body;

    if (!backendUrl) {
      return NextResponse.json({ error: 'Backend URL fehlt' }, { status: 400 });
    }

    const testUrl = buildBackendUrl(backendUrl.trim(), '/polocloud/api/v3/alive');

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
        backendUrl: backendUrl.trim()
      });
    } else {
      return NextResponse.json({ 
        success: false, 
        error: `Backend responded with status: ${response.status}` 
      }, { status: response.status });
    }

  } catch (error) {
    return NextResponse.json({
      success: false,
      error: error instanceof Error ? error.message : 'Connection test failed'
    }, { status: 500 });
  }
}

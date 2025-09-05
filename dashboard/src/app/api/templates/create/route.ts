import { NextRequest, NextResponse } from 'next/server';
import { buildBackendUrl } from '@/lib/api/utils';

export async function POST(request: NextRequest) {
  try {
    const backendIp = request.cookies.get('backend_ip')?.value;
    if (!backendIp) {
      return NextResponse.json({ error: 'No backend connection found' }, { status: 400 });
    }

    const token = request.cookies.get('token')?.value;
    if (!token) {
      return NextResponse.json({ error: 'No authentication token found' }, { status: 401 });
    }

    const body = await request.json();
    const { name } = body;

    if (!name || typeof name !== 'string' || name.trim() === '') {
      return NextResponse.json({ error: 'Template name is required' }, { status: 400 });
    }

    const decodedBackendIp = decodeURIComponent(backendIp);

    const response = await fetch(buildBackendUrl(decodedBackendIp, '/polocloud/api/v3/template/'), {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Cookie': `token=${token}`
      },
      body: JSON.stringify({ name: name.trim() })
    });

    if (response.ok) {
      const data = await response.json();
      return NextResponse.json({
        success: true,
        message: data.message || 'Template created successfully'
      });
    } else {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json({ 
        error: errorData.message || 'Failed to create template' 
      }, { status: response.status });
    }
  } catch {
    return NextResponse.json({ error: 'Internal server error' }, { status: 500 });
  }
}




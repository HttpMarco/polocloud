import { NextRequest, NextResponse } from 'next/server';
import { buildBackendUrl } from '@/lib/api/utils';

export async function GET(request: NextRequest) {
  try {

    const backendIp = request.cookies.get('backend_ip')?.value;
    const token = request.cookies.get('token')?.value;

    if (!backendIp) {
      return NextResponse.json({ error: 'Backend IP not configured' }, { status: 400 });
    }

    if (!token) {
      return NextResponse.json({ error: 'Authentication token not found' }, { status: 401 });
    }

    const backendResponse = await fetch(buildBackendUrl(backendIp, '/polocloud/api/v3/system/information'), {
      headers: {
        'Cookie': `token=${token}`,
        'Content-Type': 'application/json'
      }
    });

    if (!backendResponse.ok) {
      const errorData = await backendResponse.json().catch(() => ({}));
      return NextResponse.json({ 
        error: errorData.message || 'Failed to fetch system information' 
      }, { status: backendResponse.status });
    }

    const data = await backendResponse.json();
    return NextResponse.json(data.data || data);

  } catch {
    return NextResponse.json({ error: 'Internal server error' }, { status: 500 });
  }
}

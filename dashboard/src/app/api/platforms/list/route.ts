import { NextRequest, NextResponse } from 'next/server';
import { buildBackendUrl } from '@/lib/api/utils';

export async function GET(request: NextRequest) {
  try {
    const backendIp = request.cookies.get('backend_ip')?.value;
    if (!backendIp) {
      return NextResponse.json({ error: 'Backend IP not found' }, { status: 400 });
    }
    
    const token = request.cookies.get('token')?.value;
    if (!token) {
      return NextResponse.json({ error: 'Token not found' }, { status: 401 });
    }

    const decodedBackendIp = decodeURIComponent(backendIp);

    const response = await fetch(buildBackendUrl(decodedBackendIp, '/polocloud/api/v3/platforms/list'), {
      headers: {
        'Cookie': `token=${token}`
      }
    });
    
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json({ 
        error: errorData.message || 'Failed to fetch platforms' 
      }, { status: response.status });
    }
    
    const data = await response.json();

    if (data.status === 200 && Array.isArray(data.data)) {
      return NextResponse.json(data.data);
    }

    return NextResponse.json(data.data || data);
    
  } catch {
    return NextResponse.json({ error: 'Internal server error' }, { status: 500 });
  }
}

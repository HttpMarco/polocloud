import { NextRequest, NextResponse } from 'next/server';
import { buildBackendUrl } from '@/lib/api/utils';

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

    const decodedBackendIp = decodeURIComponent(backendIp);


    const apiUrl = buildBackendUrl(decodedBackendIp, '/polocloud/api/v3/services/list');

    const response = await fetch(apiUrl, {
      method: 'GET',
      headers: {
        'Cookie': `token=${token}`,
        'User-Agent': 'Polocloud-Dashboard/1.0'
      }
    });


    if (response.ok) {
      const data = await response.json();
      return NextResponse.json(data.data || data);
    } else {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json({ 
        error: errorData.message || 'Failed to fetch services' 
      }, { status: response.status });
    }
  } catch {
    return NextResponse.json({ error: 'Internal server error' }, { status: 500 });
  }
}

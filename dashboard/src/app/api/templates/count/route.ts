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

    const response = await fetch(buildBackendUrl(decodedBackendIp, '/polocloud/api/v3/templates/list'), {
      method: 'GET',
      headers: {
        'Cookie': `token=${token}`
      }
    });

    if (response.ok) {
      const data = await response.json();
      const templates = data.data || data;
      const templateCount = Array.isArray(templates) ? templates.length : 0;
      
      return NextResponse.json({
        templateCount,
        percentage: 0
      });
    } else {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json({ 
        error: errorData.message || 'Failed to load template count' 
      }, { status: response.status });
    }
  } catch {
    return NextResponse.json({ error: 'Internal server error' }, { status: 500 });
  }
}

import { NextRequest, NextResponse } from 'next/server';
import { buildBackendUrl } from '@/lib/api/utils';

export async function PATCH(
  request: NextRequest,
  { params }: { params: { serviceName: string } }
) {
  try {
    const backendIp = request.cookies.get('backend_ip')?.value;
    if (!backendIp) {
      return NextResponse.json({ error: 'No backend connection found' }, { status: 400 });
    }

    const token = request.cookies.get('token')?.value;
    if (!token) {
      return NextResponse.json({ error: 'No authentication token found' }, { status: 401 });
    }

    const { serviceName } = params;

    const decodedBackendIp = decodeURIComponent(backendIp);

    const response = await fetch(buildBackendUrl(decodedBackendIp, `/polocloud/api/v3/service/${serviceName}/restart`), {
      method: 'PATCH',
      headers: {
        'Cookie': `token=${token}`
      }
    });

    if (response.ok) {
      const data = await response.json();
      return NextResponse.json({
        success: true,
        message: data.message || 'Service is restarting'
      });
    } else {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json({ 
        error: errorData.message || 'Failed to restart service' 
      }, { status: response.status });
    }
  } catch {
    return NextResponse.json({ error: 'Internal server error' }, { status: 500 });
  }
}

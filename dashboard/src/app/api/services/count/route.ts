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

    const { searchParams } = new URL(request.url);
    const from = searchParams.get('from');
    const to = searchParams.get('to');

    const listUrl = buildBackendUrl(backendIp, '/polocloud/api/v3/services/list');
    const listResponse = await fetch(listUrl, {
      method: 'GET',
      headers: {
        'Cookie': `token=${token}`
      }
    });

    if (listResponse.ok) {
      const response = await listResponse.json();

      const services = response.data || [];

      const totalServices = Array.isArray(services) ? services.length : 0;
      const onlineServices = Array.isArray(services) ? services.filter((service: any) => service.state === 'ONLINE').length : 0;

      const percentage = totalServices > 0 ? Math.round((onlineServices / totalServices) * 100) : 0;

      const data = {
        serviceCount: totalServices,
        percentage: percentage
      };
      
      return NextResponse.json(data);
    } else {
      const errorData = await listResponse.json();
      return NextResponse.json({ error: errorData.message || 'Failed to fetch service count' }, { status: listResponse.status });
    }
  } catch {
    return NextResponse.json({ error: 'Internal server error' }, { status: 500 });
  }
}

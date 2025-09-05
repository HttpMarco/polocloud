import { NextRequest, NextResponse } from 'next/server';
import { logError } from '@/lib/error-handling';
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

      const data = {
        serviceCount: totalServices,
        percentage: 0
      };
      
      return NextResponse.json(data);
    } else {
      const errorData = await listResponse.json();
      return NextResponse.json({ error: errorData.message || 'Failed to fetch service count' }, { status: listResponse.status });
    }
  } catch (error) {
    logError(error, { 
      component: 'ServicesCount', 
      action: 'getServiceCount' 
    });
    return NextResponse.json({ error: 'Internal server error' }, { status: 500 });
  }
}

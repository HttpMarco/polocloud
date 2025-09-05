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

    const { searchParams } = new URL(request.url);
    const from = searchParams.get('from');
    const to = searchParams.get('to');

    if (!from || !to) {
      return NextResponse.json({ error: 'Missing from or to parameters' }, { status: 400 });
    }

    const decodedBackendIp = decodeURIComponent(backendIp);
    const apiUrl = buildBackendUrl(decodedBackendIp, `/polocloud/api/v3/system/information/average?from=${from}&to=${to}`);

    const response = await fetch(apiUrl, {
      method: 'GET',
      headers: {
        'Cookie': `token=${token}`
      },
    });

    if (response.ok) {
      const data = await response.json();
      return NextResponse.json(data.data || data);
    } else {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json({ 
        error: errorData.message || 'Failed to fetch system average' 
      }, { status: response.status });
    }
  } catch (error) {
    logError(error, { 
      component: 'SystemAverage', 
      action: 'getSystemAverage' 
    });
    return NextResponse.json({ error: 'Internal server error' }, { status: 500 });
  }
}

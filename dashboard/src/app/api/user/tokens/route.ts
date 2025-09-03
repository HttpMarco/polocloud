import { NextRequest, NextResponse } from 'next/server';
import { buildBackendUrl } from '@/lib/api/utils';

export async function GET(request: NextRequest) {
  try {

    const backendIp = request.cookies.get('backend_ip')?.value;
    if (!backendIp) {
      return NextResponse.json({ success: false, message: 'No backend connection found' }, { status: 400 });
    }

    const token = request.cookies.get('token')?.value;
    if (!token) {
      return NextResponse.json({ success: false, message: 'No authentication token' }, { status: 401 });
    }

    const decodedBackendIp = decodeURIComponent(backendIp);
    const apiUrl = buildBackendUrl(decodedBackendIp, '/polocloud/api/v3/user/tokens');
    
    const response = await fetch(apiUrl, {
      method: 'GET',
      headers: {
        'Cookie': `token=${token}`
      },
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      if (response.status === 401) {
        return NextResponse.json({ success: false, message: 'Unauthorized' }, { status: 401 });
      }
      if (response.status === 403) {
        return NextResponse.json({ success: false, message: 'Forbidden - No permission to view tokens' }, { status: 403 });
      }
      return NextResponse.json({ 
        success: false, 
        message: errorData.message || 'Failed to fetch tokens' 
      }, { status: response.status });
    }

    const data = await response.json();

    return NextResponse.json({ 
      success: true, 
      data: data.data || data 
    });
  } catch {
    return NextResponse.json({ success: false, message: 'Internal server error' }, { status: 500 });
  }
}

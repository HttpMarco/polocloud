import { NextRequest, NextResponse } from 'next/server';
import { buildBackendUrl } from '@/lib/api/utils';

export async function POST(request: NextRequest) {
  try {
    const { newPassword } = await request.json();

    const backendIp = request.cookies.get('backend_ip')?.value;
    if (!backendIp) {
      return NextResponse.json({ success: false, message: 'No backend connection found' }, { status: 400 });
    }

    const token = request.cookies.get('token')?.value;
    if (!token) {
      return NextResponse.json({ success: false, message: 'No authentication token found' }, { status: 401 });
    }

    const decodedBackendIp = decodeURIComponent(backendIp);
    const apiUrl = buildBackendUrl(decodedBackendIp, '/polocloud/api/v3/user/self/change-password');
    

    const response = await fetch(apiUrl, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        'Cookie': `token=${token}`
      },
      body: JSON.stringify({
        password: newPassword
      })
    });

    if (response.ok) {
      const data = await response.json();
      return NextResponse.json({
        success: true, 
        message: data.message || 'Password changed successfully' 
      });
    } else {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json({ 
        success: false, 
        message: errorData.message || 'Failed to change password' 
      }, { status: response.status });
    }
  } catch {
    return NextResponse.json({ success: false, message: 'Internal server error' }, { status: 500 });
  }
}

import { NextRequest, NextResponse } from 'next/server';
import { buildBackendUrl } from '@/lib/api/utils';

export async function GET(request: NextRequest) {
  try {

    const token = request.cookies.get('token')?.value;
    const backendIp = request.cookies.get('backend_ip')?.value;

    if (!token) {
      return NextResponse.json(
        { error: 'No token found' },
        { status: 500 }
      );
    }

    if (!backendIp) {
      return NextResponse.json(
        { error: 'No backend IP found' },
        { status: 500 }
      );
    }

    const response = await fetch(buildBackendUrl(backendIp, '/polocloud/api/v3/auth/token'), {
      method: 'GET',
      headers: {
        'Cookie': `token=${token}`,
        'Accept': 'application/json',
      }
    });

    if (response.ok) {
      const data = await response.json();
      const tokenData = data.data || data;
      
      return NextResponse.json({
        success: true,
        token: tokenData.token,
        message: data.message || 'Token is valid'
      });
    } else {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json(
        { error: errorData.message || 'Token is invalid' },
        { status: 401 }
      );
    }
  } catch {
    return NextResponse.json(
      { error: 'Internal server error' },
      { status: 500 }
    );
  }
}

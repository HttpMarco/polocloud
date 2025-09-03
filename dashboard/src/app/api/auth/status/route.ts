import { NextRequest, NextResponse } from 'next/server';
import { buildBackendUrl } from '@/lib/api/utils';

export async function GET(request: NextRequest) {
  try {
    const backendIp = request.cookies.get('backend_ip')?.value;
    const token = request.cookies.get('token')?.value;

    if (!backendIp || !token) {
      return NextResponse.json({ 
        authenticated: false, 
        backendIp: null, 
        token: null 
      });
    }

    const decodedBackendIp = decodeURIComponent(backendIp);

    const response = await fetch(buildBackendUrl(decodedBackendIp, '/polocloud/api/v3/system/information'), {
      headers: {
        'Cookie': `token=${token}`
      }
    });

    if (response.ok) {
      return NextResponse.json({ 
        authenticated: true, 
        backendIp: decodedBackendIp, 
        token: token 
      });
    } else {
      return NextResponse.json({ 
        authenticated: false, 
        backendIp: decodedBackendIp, 
        token: token,
        error: 'Token invalid'
      });
    }

  } catch {
    return NextResponse.json({ 
      authenticated: false, 
      backendIp: null, 
      token: null,
      error: 'Internal error'
    });
  }
}

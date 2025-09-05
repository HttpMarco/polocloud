import { NextRequest, NextResponse } from 'next/server';
import { buildBackendUrl } from '@/lib/api/utils'

export async function GET(request: NextRequest) {
  try {
    const backendIp = request.cookies.get('backend_ip')?.value;
    const token = request.cookies.get('token')?.value;

    if (!backendIp || !token) {
      return NextResponse.json({ 
        authenticated: false, 
        user: null,
        error: 'No authentication data'
      });
    }

    const response = await fetch(buildBackendUrl(backendIp, `/polocloud/api/v3/user/self`), {
      headers: {
        'Cookie': `token=${token}`
      }
    });

    if (response.ok) {
      const data = await response.json();
      const userData = data.data || data;

      return NextResponse.json({ 
        authenticated: true, 
        user: {
          username: userData.username || null,
          uuid: userData.uuid || userData.userUUID || '',
          role: userData.role || null,
          hasChangedPassword: userData.hasChangedPassword || userData.hasChangedPassword === false ? userData.hasChangedPassword : false
        }
      });
    } else {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json({ 
        authenticated: false, 
        user: null,
        error: errorData.message || 'Failed to fetch user data'
      });
    }

  } catch {
    return NextResponse.json({ 
      authenticated: false, 
      user: null,
      error: 'Internal error'
    });
  }
}

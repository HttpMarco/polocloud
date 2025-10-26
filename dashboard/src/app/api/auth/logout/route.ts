import { NextRequest, NextResponse } from 'next/server';
import { buildBackendUrl } from '@/lib/api/utils'

export async function POST(request: NextRequest) {
  try {

    const token = request.cookies.get('token')?.value;
    const backendIp = request.cookies.get('backend_ip')?.value;

    if (!token) {
      return NextResponse.json(
        { error: 'Kein Token gefunden' },
        { status: 401 }
      );
    }

    if (!backendIp) {
      return NextResponse.json(
        { error: 'Keine Backend-IP gefunden' },
        { status: 500 }
      );
    }

    const response = await fetch(buildBackendUrl(backendIp, '/polocloud/api/v3/auth/logout'), {
      method: 'POST',
      headers: {
        'Cookie': `token=${token}`,
        'Accept': 'application/json',
      }
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json({ 
        error: errorData.message || 'Logout fehlgeschlagen' 
      }, { status: response.status });
    }

    const data = await response.json();
    
    const nextResponse = NextResponse.json({ 
      success: true, 
      message: data.message || 'Logout erfolgreich' 
    });

    nextResponse.cookies.set('token', '', {
      httpOnly: true,
      secure: process.env.NODE_ENV === 'production',
      sameSite: 'lax',
      maxAge: 0,
      path: '/'
    });

    nextResponse.cookies.set('polocloud_credentials', '', {
      httpOnly: false,
      secure: process.env.NODE_ENV === 'production',
      sameSite: 'lax',
      maxAge: 0,
      path: '/'
    });

    return nextResponse;
  } catch {
    return NextResponse.json(
      { error: 'Interner Server-Fehler' },
      { status: 500 }
    );
  }
}

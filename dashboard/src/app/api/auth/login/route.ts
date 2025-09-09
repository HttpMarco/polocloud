import { NextRequest, NextResponse } from 'next/server';


export async function POST(request: NextRequest) {
  try {
    const { username, password, backendIp } = await request.json();

    if (!username || !password || !backendIp) {
      return NextResponse.json(
        { error: 'Username, password und backendIp sind erforderlich' },
        { status: 400 }
      );
    }

    const protocol = backendIp.includes('.') && !backendIp.includes('localhost') && !backendIp.includes('127.0.0.1') && !backendIp.includes(':') ? 'https' : 'http';
    const loginUrl = `${protocol}://${backendIp}/polocloud/api/v3/auth/login`;

    const response = await fetch(loginUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
      },
      body: JSON.stringify({
        username: username,
        password: password
      })
    });

    if (response.ok) {

      const setCookieHeader = response.headers.get('Set-Cookie');
      
      if (!setCookieHeader) {
        return NextResponse.json(
          { error: 'Kein Token-Cookie vom Backend erhalten' },
          { status: 500 }
        );
      }

      const tokenMatch = setCookieHeader.match(/token=([^;]+)/);
      if (!tokenMatch) {
        return NextResponse.json(
          { error: 'Token konnte nicht aus dem Cookie extrahiert werden' },
          { status: 500 }
        );
      }

      const token = tokenMatch[1];

      const nextResponse = NextResponse.json({ 
        success: true, 
        message: 'Login erfolgreich' 
      });

      nextResponse.cookies.set('token', token, {
        httpOnly: false,
        secure: process.env.NODE_ENV === 'production',
        sameSite: 'lax',
        maxAge: 7 * 24 * 60 * 60,
        path: '/'
      });


      nextResponse.cookies.set('backend_ip', backendIp, {
        httpOnly: false,
        secure: process.env.NODE_ENV === 'production',
        sameSite: 'lax',
        maxAge: 7 * 24 * 60 * 60,
        path: '/'
      });


      return nextResponse;
    } else {
      const errorData = await response.json().catch(() => ({}));
      let errorMessage = errorData.message || 'Login failed';
      
      if (response.status === 401) {
        errorMessage = 'Invalid credentials';
      } else if (response.status === 400) {
        errorMessage = 'Invalid input data';
      }
      
      return NextResponse.json(
        { error: errorMessage },
        { status: response.status }
      );
    }
  } catch {
    return NextResponse.json(
      { error: 'Interner Server-Fehler' },
      { status: 500 }
    );
  }
}

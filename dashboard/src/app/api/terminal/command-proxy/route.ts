import { NextRequest, NextResponse } from 'next/server';

export async function POST(request: NextRequest) {
  try {
    const backendIp = request.cookies.get('backend_ip')?.value;
    if (!backendIp) {
      return NextResponse.json({ error: 'No backend connection found' }, { status: 400 });
    }

    const token = request.cookies.get('token')?.value;
    if (!token) {
      return NextResponse.json({ error: 'No authentication token found' }, { status: 401 });
    }

    const body = await request.json();
    const { command } = body;

    if (!command || command.trim() === '') {
      return NextResponse.json({ error: 'Command cannot be empty' }, { status: 400 });
    }

    const decodedBackendIp = decodeURIComponent(backendIp);

    const isHttpsBackend = decodedBackendIp.includes('https') || 
                          decodedBackendIp.includes(':443') ||
                          (!decodedBackendIp.includes(':') && !decodedBackendIp.includes('http'));
    
    const protocol = isHttpsBackend ? 'https' : 'http';
    const backendUrl = `${protocol}://${decodedBackendIp}/polocloud/api/v3/terminal/command`;

    const backendResponse = await fetch(backendUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Cookie': `token=${token}`
      },
      body: JSON.stringify({ command: command.trim() })
    });

    if (backendResponse.ok) {
      const data = await backendResponse.json();
      return NextResponse.json({
        success: true,
        message: data.message || 'Command executed successfully'
      });
    } else {
      const errorData = await backendResponse.json().catch(() => ({}));
      return NextResponse.json({ 
        error: errorData.message || 'Failed to execute command' 
      }, { status: backendResponse.status });
    }
  } catch {
    return NextResponse.json({ error: 'Internal server error' }, { status: 500 });
  }
}

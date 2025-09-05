import { NextRequest, NextResponse } from 'next/server';
import { buildBackendUrl } from '@/lib/api/utils';

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


    const response = await fetch(buildBackendUrl(decodedBackendIp, '/polocloud/api/v3/terminal/command'), {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Cookie': `token=${token}`
      },
      body: JSON.stringify({ command: command.trim() })
    });

    if (response.ok) {
      const data = await response.json();
      return NextResponse.json({
        success: true,
        message: data.message || 'Command executed successfully'
      });
    } else {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json({ 
        error: errorData.message || 'Failed to execute command' 
      }, { status: response.status });
    }
  } catch {
    return NextResponse.json({ error: 'Internal server error' }, { status: 500 });
  }
}

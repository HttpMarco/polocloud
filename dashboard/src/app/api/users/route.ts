import { NextRequest, NextResponse } from 'next/server'
import { buildBackendUrl } from '@/lib/api/utils'

export async function GET(request: NextRequest) {
  try {
    const token = request.cookies.get('token')?.value
    if (!token) {
      return NextResponse.json({ message: 'Unauthorized' }, { status: 401 })
    }

    const backendIp = request.cookies.get('backend_ip')?.value
    if (!backendIp) {
      return NextResponse.json({ message: 'Backend IP nicht gefunden' }, { status: 400 })
    }

    const response = await fetch(buildBackendUrl(backendIp, '/polocloud/api/v3/users/'), {
      headers: {
        'Cookie': `token=${token}`,
      },
    })

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json({ 
        error: errorData.message || 'Fehler beim Abrufen der Users' 
      }, { status: response.status });
    }

    const data = await response.json();
    return NextResponse.json(data.data || data);
  } catch {
    return NextResponse.json({ message: 'Internal server error' }, { status: 500 })
  }
}

export async function POST(request: NextRequest) {
  try {
    const token = request.cookies.get('token')?.value
    if (!token) {
      return NextResponse.json({ message: 'Unauthorized' }, { status: 401 })
    }

    const body = await request.json()
    const { username, roleId, backendIp } = body

    if (!username || !backendIp) {
      return NextResponse.json({ message: 'Username und Backend IP sind erforderlich' }, { status: 400 })
    }

    const response = await fetch(buildBackendUrl(backendIp, '/polocloud/api/v3/user/'), {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Cookie': `token=${token}`,
      },
      body: JSON.stringify({
        username: username,
        roleId: roleId || 0,
      }),
    })

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json({ 
        error: errorData.message || 'Fehler beim Erstellen des Users im Backend' 
      }, { status: response.status });
    }

    const result = await response.json();
    
    return NextResponse.json({
      success: true,
      message: result.message || 'User erfolgreich erstellt',
      password: result.data?.password || result.password,
      username: username
    });

  } catch {
    return NextResponse.json({ message: 'Internal server error' }, { status: 500 })
  }
}

import { NextRequest, NextResponse } from 'next/server'
import { buildBackendUrl } from '@/lib/api/utils'

export async function GET(request: NextRequest) {
  try {

    const backendIp = request.cookies.get('backend_ip')?.value
    
    if (!backendIp) {
      return NextResponse.json({ error: 'Backend IP not found' }, { status: 400 })
    }

    const token = request.cookies.get('token')?.value
    
    if (!token) {
      return NextResponse.json({ error: 'No token found' }, { status: 401 })
    }

    const response = await fetch(buildBackendUrl(backendIp, '/polocloud/api/v3/roles/'), {
      method: 'GET',
      headers: {
        'Cookie': `token=${token}`,
        'Content-Type': 'application/json'
      }
    })
    
    if (response.ok) {
      const data = await response.json();
      return NextResponse.json(data.data || data);
    } else {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json({ 
        error: errorData.message || `HTTP ${response.status}: Failed to fetch roles` 
      }, { status: response.status });
    }

  } catch {
    return NextResponse.json({ error: 'Failed to fetch roles' }, { status: 500 })
  }
}

export async function POST(request: NextRequest) {
  try {
    const token = request.cookies.get('token')?.value
    if (!token) {
      return NextResponse.json({ message: 'Unauthorized' }, { status: 401 })
    }

    const body = await request.json()
    const { label, hexColor, permissions, backendIp } = body

    if (!label || !hexColor || !backendIp) {
      return NextResponse.json({ message: 'Label und Hex-Color sind erforderlich' }, { status: 400 })
    }

    const requestBody = {
      label,
      hexColor,
      permissions: permissions || []
    }

    const response = await fetch(buildBackendUrl(backendIp, '/polocloud/api/v3/role/'), {
      method: 'POST',
      headers: { 
        'Content-Type': 'application/json', 
        'Cookie': `token=${token}` 
      },
      body: JSON.stringify(requestBody),
    })

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json({ 
        error: errorData.message || 'Fehler beim Erstellen der Rolle im Backend' 
      }, { status: response.status });
    }

    const result = await response.json();

    return NextResponse.json({ 
      success: true,
      message: 'Rolle erfolgreich erstellt', 
      role: result.data || result
    });
  } catch {
    return NextResponse.json({ message: 'Internal server error' }, { status: 500 })
  }
}

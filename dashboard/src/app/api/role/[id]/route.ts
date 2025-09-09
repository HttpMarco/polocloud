import { NextRequest, NextResponse } from 'next/server'
import { buildBackendUrl } from '@/lib/api/utils'

export async function GET(
  request: NextRequest,
  { params }: { params: { id: string } }
) {
  try {
    const roleId = params.id

    const backendIp = request.cookies.get('backend_ip')?.value
    
    if (!backendIp) {
      return NextResponse.json({ error: 'Backend IP not found' }, { status: 400 })
    }

    const token = request.cookies.get('token')?.value
    
    if (!token) {
      return NextResponse.json({ error: 'No token found' }, { status: 401 })
    }

    const response = await fetch(buildBackendUrl(backendIp, `/polocloud/api/v3/role/${roleId}`), {
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
        error: errorData.message || `HTTP ${response.status}: Failed to fetch role` 
      }, { status: response.status });
    }

  } catch {
    return NextResponse.json({ error: 'Failed to fetch role' }, { status: 500 })
  }
}

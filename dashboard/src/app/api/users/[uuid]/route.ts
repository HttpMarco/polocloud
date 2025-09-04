import { NextRequest, NextResponse } from 'next/server'
import { buildBackendUrl } from '@/lib/api/utils'

export async function DELETE(
  request: NextRequest,
  { params }: { params: { uuid: string } }
) {
  try {
    const token = request.cookies.get('token')?.value
    if (!token) {
      return NextResponse.json({ message: 'Unauthorized' }, { status: 401 })
    }

    const backendIp = request.cookies.get('backend_ip')?.value
    if (!backendIp) {
      return NextResponse.json({ message: 'Backend IP not found' }, { status: 400 })
    }

    const response = await fetch(buildBackendUrl(backendIp, `/polocloud/api/v3/user/${params.uuid}`), {
      method: 'DELETE',
      headers: {
        'Cookie': `token=${token}`,
      },
    })

    if (response.status === 204) {
      return NextResponse.json({ message: 'User successfully deleted' });
    } else if (response.ok) {
      const result = await response.json();
      return NextResponse.json({ message: result.message || 'User successfully deleted' });
    } else {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json({ 
        error: errorData.message || 'Error deleting user in backend' 
      }, { status: response.status });
    }
  } catch {
    return NextResponse.json({ message: 'Internal server error' }, { status: 500 })
  }
}

export async function PATCH(
  request: NextRequest,
  { params }: { params: { uuid: string } }
) {
  try {
    const token = request.cookies.get('token')?.value
    if (!token) {
      return NextResponse.json({ message: 'Unauthorized' }, { status: 401 })
    }

    const backendIp = request.cookies.get('backend_ip')?.value
    if (!backendIp) {
      return NextResponse.json({ message: 'Backend IP not found' }, { status: 400 })
    }

    const body = await request.json()
    const { roleId } = body

    if (roleId === undefined) {
      return NextResponse.json({ message: 'Role ID is required' }, { status: 400 })
    }

    const response = await fetch(buildBackendUrl(backendIp, '/polocloud/api/v3/user/edit'), {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        'Cookie': `token=${token}`,
      },
      body: JSON.stringify({
        uuid: params.uuid,
        roleId: roleId
      }),
    })

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json({ 
        error: errorData.message || 'Error editing user in backend' 
      }, { status: response.status });
    }

    const result = await response.json();
    
    return NextResponse.json({ 
      success: true,
      message: result.message || 'User successfully edited'
    });
  } catch {
    return NextResponse.json({ message: 'Internal server error' }, { status: 500 })
  }
}

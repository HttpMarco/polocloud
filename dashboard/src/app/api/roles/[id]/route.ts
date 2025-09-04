import { NextRequest, NextResponse } from 'next/server'
import { buildBackendUrl } from '@/lib/api/utils'

export async function DELETE(
  request: NextRequest,
  { params }: { params: { id: string } }
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

    const response = await fetch(buildBackendUrl(backendIp, `/polocloud/api/v3/role/${params.id}`), {
      method: 'DELETE',
      headers: {
        'Cookie': `token=${token}`,
      },
    })

    if (response.status === 204) {
      return NextResponse.json({ message: 'Role successfully deleted' });
    } else if (response.ok) {
      const result = await response.json();
      return NextResponse.json({ message: result.message || 'Role successfully deleted' });
    } else {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json({ 
        error: errorData.message || 'Error deleting role in backend' 
      }, { status: response.status });
    }
  } catch {
    return NextResponse.json({ message: 'Internal server error' }, { status: 500 })
  }
}

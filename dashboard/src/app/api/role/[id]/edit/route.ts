import { NextRequest, NextResponse } from 'next/server';
import { buildBackendUrl } from '@/lib/api/utils';

export async function PATCH(
  request: NextRequest,
  { params }: { params: { id: string } }
) {
  try {
    const { id } = params;
    const backendIp = request.cookies.get('backend_ip')?.value;
    if (!backendIp) {
      return NextResponse.json({ error: 'No backend connection found' }, { status: 400 });
    }

    const token = request.cookies.get('token')?.value;
    if (!token) {
      return NextResponse.json({ error: 'No authentication token found' }, { status: 401 });
    }

    const body = await request.json();
    const { label, hexColor, permissions } = body;

    if (!label || typeof label !== 'string' || label.trim() === '') {
      return NextResponse.json({ error: 'Role label is required' }, { status: 400 });
    }

    if (!hexColor || typeof hexColor !== 'string' || hexColor.trim() === '') {
      return NextResponse.json({ error: 'Role hex color is required' }, { status: 400 });
    }

    const decodedBackendIp = decodeURIComponent(backendIp);

    const response = await fetch(buildBackendUrl(decodedBackendIp, `/polocloud/api/v3/role/${id}`), {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        'Cookie': `token=${token}`
      },
      body: JSON.stringify({
        label: label.trim(),
        hexColor: hexColor.trim(),
        permissions: permissions || []
      })
    });

    if (response.ok) {
      const data = await response.json();
      return NextResponse.json({
        success: true,
        message: 'Role updated successfully',
        role: data.data || data
      });
    } else {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json({ 
        error: errorData.message || 'Failed to update role' 
      }, { status: response.status });
    }
  } catch {
    return NextResponse.json({ error: 'Internal server error' }, { status: 500 });
  }
}




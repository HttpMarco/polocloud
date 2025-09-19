import { NextRequest, NextResponse } from 'next/server';
import { buildBackendUrl } from '@/lib/api/utils';

export async function DELETE(
  request: NextRequest,
  { params }: { params: { token: string } }
) {
  try {

    const backendIp = request.cookies.get('backend_ip')?.value;
    if (!backendIp) {
      return NextResponse.json({ success: false, message: 'No backend connection found' }, { status: 400 });
    }

    const authToken = request.cookies.get('token')?.value;
    if (!authToken) {
      return NextResponse.json({ success: false, message: 'No authentication token' }, { status: 401 });
    }

    const tokenToDelete = params.token;

    const decodedBackendIp = decodeURIComponent(backendIp);
    const apiUrl = buildBackendUrl(decodedBackendIp, `/polocloud/api/v3/user/token/${tokenToDelete}`);
    
    const response = await fetch(apiUrl, {
      method: 'DELETE',
      headers: {
        'Cookie': `token=${authToken}`
      },
    });

    if (!response.ok) {
      if (response.status === 401) {
        return NextResponse.json({ success: false, message: 'Unauthorized' }, { status: 401 });
      }
      if (response.status === 403) {
        return NextResponse.json({ success: false, message: 'Forbidden - No permission to delete tokens' }, { status: 403 });
      }
      if (response.status === 404) {
        return NextResponse.json({ success: false, message: 'Token not found' }, { status: 404 });
      }
      return NextResponse.json({ success: false, message: 'Failed to delete token' }, { status: response.status });
    }

    return NextResponse.json({ success: true, message: 'Token deleted successfully' });
  } catch {
    return NextResponse.json({ success: false, message: 'Internal server error' }, { status: 500 });
  }
}

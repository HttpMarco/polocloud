import { NextRequest, NextResponse } from 'next/server';
import { buildBackendUrl } from '@/lib/api/utils';

export async function GET(
  request: NextRequest,
  { params }: { params: { playerName: string } }
) {
  try {
    const backendIp = request.cookies.get('backend_ip')?.value;
    if (!backendIp) {
      return NextResponse.json({ error: 'No backend connection found' }, { status: 400 });
    }

    const token = request.cookies.get('token')?.value;
    if (!token) {
      return NextResponse.json({ error: 'No authentication token found' }, { status: 401 });
    }

    const { playerName } = params;

    if (!playerName || playerName.trim() === '') {
      return NextResponse.json({ error: 'Player name cannot be null or empty' }, { status: 400 });
    }

    const decodedBackendIp = decodeURIComponent(backendIp);

    const response = await fetch(buildBackendUrl(decodedBackendIp, `/polocloud/api/v3/player/${playerName}`), {
      method: 'GET',
      headers: {
        'Cookie': `token=${token}`
      }
    });

    if (response.ok) {
      const data = await response.json();
      return NextResponse.json(data.data || data);
    } else if (response.status === 404) {
      return NextResponse.json({ error: 'Player not found' }, { status: 404 });
    } else {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json({ 
        error: errorData.message || 'Failed to load player' 
      }, { status: response.status });
    }
  } catch {
    return NextResponse.json({ error: 'Internal server error' }, { status: 500 });
  }
}

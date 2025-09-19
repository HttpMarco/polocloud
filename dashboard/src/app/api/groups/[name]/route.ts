import { NextRequest, NextResponse } from 'next/server';
import { buildBackendUrl } from '@/lib/api/utils';

export async function GET(
  request: NextRequest,
  { params }: { params: { name: string } }
) {
  try {
    const { name } = params;
    const backendIp = request.cookies.get('backend_ip')?.value;
    if (!backendIp) {
      return NextResponse.json({ error: 'No backend connection found' }, { status: 400 });
    }

    const token = request.cookies.get('token')?.value;
    if (!token) {
      return NextResponse.json({ error: 'No authentication token found' }, { status: 401 });
    }

    const response = await fetch(buildBackendUrl(backendIp, `/polocloud/api/v3/group/${name}`), {
      method: 'GET',
      headers: {
        'Cookie': `token=${token}`
      }
    });

    if (response.ok) {
      const data = await response.json();
      return NextResponse.json(data.data || data);
    } else {
      const errorData = await response.json();
      return NextResponse.json({ error: errorData.message || 'Failed to fetch group' }, { status: response.status });
    }
  } catch {
    return NextResponse.json({ error: 'Internal server error' }, { status: 500 });
  }
}

export async function PATCH(
  request: NextRequest,
  { params }: { params: { name: string } }
) {
  try {
    const groupName = params.name;
    const body = await request.json();

    const {
        minMemory,
        maxMemory,
        minOnlineService,
        maxOnlineService,
        percentageToStartNewService
    } = body;

    if (minMemory < 0 || maxMemory < 0) {
        return NextResponse.json(
            { error: 'Memory values must be greater than or equal to 0' },
            { status: 400 }
        );
    }

    if (minMemory > maxMemory) {
        return NextResponse.json(
            { error: 'Minimum memory cannot be greater than maximum memory'},
            { status: 400 }
        );
    }

    if (minOnlineService < 0 || maxOnlineService < 0) {
        return NextResponse.json(
            { error: 'Online service values must be greater than or equal to 0' },
            { status: 400 }
        );
    }

    if (minOnlineService > maxOnlineService) {
        return NextResponse.json(
            { error: 'Minimum online service cannot be greater than maximum online service'},
            { status: 400 }
        );
    }

    if (percentageToStartNewService < 0.0 || percentageToStartNewService > 100.0) {
        return NextResponse.json(
            { error: 'Percentage must be between 0.0 and 100.0' },
            { status: 400 }
        );
    }

    const backendIp = request.cookies.get('backend_ip')?.value;
      if (!backendIp) {
          return NextResponse.json({ error: 'No backend connection found' }, { status: 400 });
      }

      const token = request.cookies.get('token')?.value;
      if (!token) {
          return NextResponse.json({ error: 'No authentication token found' }, { status: 401 });
      }

      const response = await fetch(buildBackendUrl(backendIp, `/polocloud/api/v3/group/${groupName}`), {
          method: 'PATCH',
          headers: {
              'Content-Type': 'application/json',
              'Cookie': `token=${token}`
          },
          body: JSON.stringify({
              minMemory: parseInt(minMemory),
              maxMemory: parseInt(maxMemory),
              minOnlineService: parseInt(minOnlineService),
              maxOnlineService: parseInt(maxOnlineService),
              percentageToStartNewService: parseFloat(percentageToStartNewService)
          })
      });

      if (!response.ok) {
          const errorData = await response.json().catch(() => ({}));
          return NextResponse.json(
              {
                  error: errorData.message || 'Failed to update group',
                  status: response.status
              },
              { status: response.status }
          );
      }

      const result = await response.json();

      return NextResponse.json({
          success: true,
          message: result.message || 'Group updated successfully',
          data: {
              name: groupName,
              minMemory,
              maxMemory,
              minOnlineService,
              maxOnlineService,
              percentageToStartNewService
          }
      });
  } catch {
    return NextResponse.json({ error: 'Internal server error' }, { status: 500 });
  }
}

export async function DELETE(
  request: NextRequest,
  { params }: { params: { name: string } }
) {
  try {
    const { name } = params;
    const backendIp = request.cookies.get('backend_ip')?.value;
    if (!backendIp) {
      return NextResponse.json({ error: 'No backend connection found' }, { status: 400 });
    }

    const token = request.cookies.get('token')?.value;
    if (!token) {
      return NextResponse.json({ error: 'No authentication token found' }, { status: 401 });
    }

    const response = await fetch(buildBackendUrl(backendIp, `/polocloud/api/v3/group/${name}`), {
      method: 'DELETE',
      headers: {
        'Cookie': `token=${token}`
      }
    });

    if (response.status === 204) {
      return NextResponse.json({ message: 'Group deleted successfully' });
    } else if (response.ok) {
      const result = await response.json();
      return NextResponse.json({ message: result.message || 'Group deleted successfully' });
    } else {
      try {
        const errorData = await response.json();
        return NextResponse.json({ error: errorData.message || 'Failed to delete group' }, { status: response.status });
      } catch {
        return NextResponse.json({ error: response.statusText || 'Failed to delete group' }, { status: response.status });
      }
    }
  } catch {
    return NextResponse.json({ error: 'Internal server error' }, { status: 500 });
  }
}

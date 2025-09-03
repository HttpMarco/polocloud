import { NextRequest, NextResponse } from 'next/server';
import { buildBackendUrl } from '@/lib/api/utils';

export async function GET(request: NextRequest) {
    try {
        const backendIp = request.cookies.get('backend_ip')?.value;
        if (!backendIp) {
            return NextResponse.json({ error: 'Backend IP not found' }, { status: 400 });
        }

        const token = request.cookies.get('token')?.value;
        if (!token) {
            return NextResponse.json({ error: 'Token not found' }, { status: 401 });
        }

        const decodedBackendIp = decodeURIComponent(backendIp);

        const { searchParams } = new URL(request.url);
        const from = searchParams.get('from');
        const to = searchParams.get('to');

        let apiUrl = buildBackendUrl(decodedBackendIp, '/polocloud/api/v3/services/count');
        if (from && to) {
            apiUrl += `?from=${from}&to=${to}`;
        }

        const response = await fetch(apiUrl, {
            method: 'GET',
            headers: {
                'Cookie': `token=${token}`
            },
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            return NextResponse.json({ 
                error: errorData.message || 'Backend request failed' 
            }, { status: response.status });
        }

        const data = await response.json();
        return NextResponse.json(data.data || data);

    } catch {
        return NextResponse.json({ error: 'Internal server error' }, { status: 500 });
    }
}

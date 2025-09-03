import { NextRequest, NextResponse } from 'next/server';
import { buildBackendUrl } from '@/lib/api/utils';
import { CreateTemplateModel } from '@/types/templates';

export async function POST(request: NextRequest) {
    try {
        const backendIp = request.cookies.get('backend_ip')?.value;
        if (!backendIp) {
            return NextResponse.json({ error: 'No backend connection found' }, { status: 400 });
        }

        const token = request.cookies.get('token')?.value;
        if (!token) {
            return NextResponse.json({ error: 'No authentication token found' }, { status: 401 });
        }

        const body: CreateTemplateModel = await request.json();

        if (!body.name || body.name.trim() === '') {
            return NextResponse.json({ error: 'Template name cannot be empty' }, { status: 400 });
        }

        const decodedBackendIp = decodeURIComponent(backendIp);
        const backendUrl = buildBackendUrl(decodedBackendIp, '/polocloud/api/v3/template/');

        const response = await fetch(backendUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Cookie': `token=${token}`
            },
            body: JSON.stringify(body)
        });

        let responseContent: string;

        try {
            responseContent = await response.text();
        } catch {
            responseContent = '';
        }

        if (response.ok || response.status === 202) {
            try {
                const jsonData = JSON.parse(responseContent);
                return NextResponse.json(jsonData);
            } catch {
                return NextResponse.json({ message: responseContent });
            }
        } else {
            try {
                const jsonData = JSON.parse(responseContent);
                return NextResponse.json({ error: jsonData.message || 'Failed to create template' }, { status: response.status });
            } catch {
                return NextResponse.json({ error: responseContent || 'Failed to create template' }, { status: response.status });
            }
        }
    } catch {
        return NextResponse.json({ error: 'Internal server error' }, { status: 500 });
    }
}

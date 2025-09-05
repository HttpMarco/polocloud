import { NextRequest, NextResponse } from 'next/server';
import { logError } from '@/lib/error-handling';
import { buildBackendUrl } from '@/lib/api/utils';

interface GroupCreateRequest {
  name: string;
  minMemory: number;
  maxMemory: number;
  minOnlineService: number;
  maxOnlineService: number;
  platform: {
    name: string;
    version: string;
  };
  percentageToStartNewService: number;
  createdAt: number;
  templates: string[];
  properties: Record<string, boolean>;
}

export async function POST(request: NextRequest) {
  try {
    const body: GroupCreateRequest = await request.json();

    const token = request.cookies.get('token')?.value;
    const backendIp = request.cookies.get('backend_ip')?.value;
    
    if (!token) {
      return NextResponse.json({ error: 'Token not found' }, { status: 401 });
    }
    
    if (!backendIp) {
      return NextResponse.json({ error: 'Backend IP not found' }, { status: 400 });
    }
    
    const apiUrl = buildBackendUrl(backendIp, '/polocloud/api/v3/group/create');

    const minMemoryMB = parseInt(body.minMemory.toString().replace(/\D/g, ''));
    const maxMemoryMB = parseInt(body.maxMemory.toString().replace(/\D/g, ''));

    const percentage = parseFloat(body.percentageToStartNewService.toString());

    const requestBody = {
      name: body.name,
      minMemory: minMemoryMB,
      maxMemory: maxMemoryMB,
      minOnlineService: body.minOnlineService,
      maxOnlineService: body.maxOnlineService,
      platform: {
        name: body.platform.name,
        version: body.platform.version
      },
      percentageToStartNewService: percentage,
      createdAt: body.createdAt,
      templates: body.templates,
      properties: {
        fallback: body.properties.fallback,
        static: body.properties.static
      }
    };
    
    const response = await fetch(apiUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Cookie': `token=${token}`,
      },
      body: JSON.stringify(requestBody),
    });
    
    if (!response.ok) {
      const errorData = await response.json();
      return NextResponse.json(
        { error: errorData.message || 'Failed to create group' },
        { status: response.status }
      );
    }
    
    const result = await response.json();
    return NextResponse.json({
      success: true,
      message: result.message || 'Group created successfully'
    }, { status: 201 });
    
  } catch (error) {
    logError(error, { 
      component: 'GroupsCreate', 
      action: 'createGroup' 
    });
    return NextResponse.json(
      { error: 'Internal server error' },
      { status: 500 }
    );
  }
}


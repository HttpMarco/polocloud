import { NextRequest, NextResponse } from 'next/server';
import { getCachedModrinthOrganization, clearModrinthCache } from '@/lib/modrinth';

export async function GET(request: NextRequest) {
    try {
        console.log('🔄 Modrinth API: Fetching organization data...');
        const organization = await getCachedModrinthOrganization();
        
        console.log('✅ Modrinth API: Data fetched successfully');
        console.log('📊 Projects count:', organization.projects?.length || 0);
        console.log('📊 Organization:', organization.name);

        const response = NextResponse.json(organization);
        response.headers.set('Access-Control-Allow-Origin', '*');
        response.headers.set('Access-Control-Allow-Methods', 'GET');
        response.headers.set('Access-Control-Allow-Headers', 'Content-Type');
        response.headers.set('Cache-Control', 'public, s-maxage=1800, stale-while-revalidate=3600');

        return response;
    } catch (error) {
        console.error('❌ Modrinth API Error:', error);
        console.error('Error details:', {
            message: error instanceof Error ? error.message : 'Unknown error',
            stack: error instanceof Error ? error.stack : 'No stack trace',
            name: error instanceof Error ? error.name : 'Unknown error type'
        });
        
        const fallbackData = {
            id: 'polocloud',
            name: 'Polocloud',
            description: 'Simple and easy minecraft cloud',
            projects: [
                {
                    project_id: 'polocloud-hub',
                    project_type: 'plugin',
                    slug: 'polocloud-hub',
                    title: 'polocloud-hub',
                    description: 'This plugin is an addon for PoloCloud that adds a /hub command, allowing players to quickly return to the fallback server.',
                    author: 'HttpMarco',
                    display_categories: ['utility'],
                    download_count: 3,
                    follower_count: 5,
                    date_created: '2024-01-01T00:00:00Z',
                    date_modified: '2024-01-01T00:00:00Z',
                    latest_version: '1.0.0',
                    gallery: [],
                    client_side: 'unsupported',
                    server_side: 'required',
                    donation_urls: [],
                    license: {
                        id: 'MIT',
                        name: 'MIT License'
                    },
                    team: 'polocloud',
                    published: '2024-01-01T00:00:00Z',
                    updated: '2024-01-01T00:00:00Z',
                    followers: 5,
                    additional_categories: [],
                    loaders: ['bungeecord', 'velocity'],
                    game_versions: ['1.20'],
                    versions: ['1.0.0'],
                    follows: 5,
                    rating: {
                        count: 0,
                        average: 0
                    },
                    status: 'approved'
                }
            ],
            members: [
                {
                    user: {
                        username: 'HttpMarco',
                        avatar_url: 'https://github.com/HttpMarco.png'
                    },
                    role: 'Member'
                },
                {
                    user: {
                        username: 'RECHERGG',
                        avatar_url: undefined
                    },
                    role: 'Member'
                }
            ],
            created: '2024-01-01T00:00:00Z',
            updated: '2024-01-01T00:00:00Z'
        };

        console.log('🔄 Modrinth API: Returning fallback data');
        const response = NextResponse.json(fallbackData);
        response.headers.set('Cache-Control', 'public, s-maxage=60');
        return response;
    }
}

export async function POST(request: NextRequest) {
    try {
        clearModrinthCache();
        const organization = await getCachedModrinthOrganization();

        const response = NextResponse.json({
            success: true,
            message: 'Cache cleared and fresh data fetched',
            organization
        });
        return response;
    } catch (error) {
        console.error('POST API Error:', error);
        return NextResponse.json({
            success: false,
            error: error instanceof Error ? error.message : 'Unknown error'
        }, { status: 500 });
    }
}
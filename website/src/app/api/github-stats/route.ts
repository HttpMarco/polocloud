import { NextRequest, NextResponse } from 'next/server';
import { getCachedGitHubStats, getCacheStatus } from '@/lib/github';

export async function GET(request: NextRequest) {
    try {
        const stats = await getCachedGitHubStats();

        const response = NextResponse.json(stats);
        response.headers.set('Access-Control-Allow-Origin', '*');
        response.headers.set('Access-Control-Allow-Methods', 'GET');
        response.headers.set('Access-Control-Allow-Headers', 'Content-Type');
        response.headers.set('Cache-Control', 'public, s-maxage=300, stale-while-revalidate=600');

        return response;
    } catch (error) {
        console.error('API Error fetching GitHub stats:', error);
        const fallbackData = {
            stars: 61,
            forks: 22,
            releases: 4,
            downloads: 0,
            lastUpdated: new Date().toISOString(),
        };

        const response = NextResponse.json(fallbackData);
        response.headers.set('Cache-Control', 'public, s-maxage=60');
        return response;
    }
}

export async function POST(request: NextRequest) {
    if (process.env.NODE_ENV === 'development') {
        const cacheStatus = getCacheStatus();
        return NextResponse.json(cacheStatus);
    }

    return NextResponse.json({ error: 'Not available in production' }, { status: 403 });
} 
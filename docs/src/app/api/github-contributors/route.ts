import { NextRequest, NextResponse } from 'next/server';
import { getCachedGitHubContributors } from '@/lib/github';

export async function GET(request: NextRequest) {
    try {
        const contributors = await getCachedGitHubContributors();

        const response = NextResponse.json(contributors);
        response.headers.set('Access-Control-Allow-Origin', '*');
        response.headers.set('Access-Control-Allow-Methods', 'GET');
        response.headers.set('Access-Control-Allow-Headers', 'Content-Type');
        response.headers.set('Cache-Control', 'public, s-maxage=1800, stale-while-revalidate=3600');

        return response;
    } catch (error) {
        console.error('API Error fetching GitHub contributors:', error);
        const fallbackData = [
            {
                login: 'HttpMarco',
                id: 1,
                avatar_url: 'https://github.com/HttpMarco.png',
                contributions: 156,
                html_url: 'https://github.com/HttpMarco',
                type: 'User'
            }
        ];

        const response = NextResponse.json(fallbackData);
        response.headers.set('Cache-Control', 'public, s-maxage=60');
        return response;
    }
}
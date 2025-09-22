import { NextResponse } from 'next/server';
import { getPlatformsFromGitHub } from '@/lib/github';
import { Platform } from '@/components/admin/types';

let cachedPlatforms: Platform[] = [];
let lastFetch = 0;
const CACHE_DURATION = 30 * 1000;

export async function GET() {
  try {
    const now = Date.now();

    if (cachedPlatforms.length > 0 && (now - lastFetch) < CACHE_DURATION) {
      console.log('📦 Returning cached platforms data');
      return NextResponse.json({ platforms: cachedPlatforms });
    }

    console.log('🔄 Fetching fresh platforms data from GitHub');
    const platforms = await getPlatformsFromGitHub();

    cachedPlatforms = platforms;
    lastFetch = now;
    
    console.log('Platforms data fetched successfully:', platforms.length, 'platforms');
    return NextResponse.json({ platforms });
  } catch (error) {
    console.error('Error fetching platforms:', error);
    return NextResponse.json({ error: 'Failed to fetch platforms' }, { status: 500 });
  }
}

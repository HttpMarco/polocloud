import { NextResponse } from 'next/server';
import { getPartnersFromGitHub } from '@/lib/github';
import { Partner } from '@/components/admin/types';

let cachedPartners: Partner[] = [];
let lastFetch = 0;
const CACHE_DURATION = 30 * 1000;

export async function GET() {
  try {
    const now = Date.now();

    if (cachedPartners.length > 0 && now - lastFetch < CACHE_DURATION) {
      return NextResponse.json({ partners: cachedPartners }, {
        headers: {
          'Cache-Control': 'public, s-maxage=300, stale-while-revalidate=600',
        },
      });
    }

    const partners: Partner[] = await getPartnersFromGitHub();

    const validPartners = partners
      .filter((partner: Partner) => partner.name && partner.logo)
      .sort((a: Partner, b: Partner) => new Date(b.addedAt || 0).getTime() - new Date(a.addedAt || 0).getTime());

    cachedPartners = validPartners;
    lastFetch = now;

    return NextResponse.json({ partners: validPartners }, {
      headers: {
        'Cache-Control': 'public, s-maxage=300, stale-while-revalidate=600',
      },
    });
  } catch (error) {
    console.error('Error fetching partners:', error);

    return NextResponse.json({ partners: cachedPartners }, {
      headers: {
        'Cache-Control': 'public, s-maxage=60, stale-while-revalidate=120',
      },
    });
  }
}

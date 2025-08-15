import { NextResponse } from 'next/server';
import { getChangelogFromGitHub } from '@/lib/github';

export async function GET() {
  try {
    const changelogData = await getChangelogFromGitHub();

    const sortedChangelog = changelogData.sort((a, b) => {
      const dateA = new Date(a.releaseDate).getTime();
      const dateB = new Date(b.releaseDate).getTime();
      return dateB - dateA;
    });

    return NextResponse.json({ changelog: sortedChangelog });
  } catch (error) {
    console.error('Error fetching changelog:', error);
    return NextResponse.json({ changelog: [] });
  }
}

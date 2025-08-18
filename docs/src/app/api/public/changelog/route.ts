import { NextResponse } from 'next/server';
import { getAllChangelogFiles } from '@/lib/github';

export async function GET() {
  try {
    console.log('Fetching changelogs from GitHub...');
    const changelogData = await getAllChangelogFiles();
    console.log('Changelog data:', changelogData);

    if (!changelogData || !Array.isArray(changelogData)) {
      console.log('No changelog data or invalid format, returning empty array');
      return NextResponse.json({ changelog: [] });
    }

    return NextResponse.json({ changelog: changelogData });

  } catch (error) {
    console.error('Error listing changelog entries:', error);
    console.error('Error details:', {
      message: error instanceof Error ? error.message : 'Unknown error',
      stack: error instanceof Error ? error.stack : 'No stack trace',
      name: error instanceof Error ? error.name : 'Unknown error type'
    });

    return NextResponse.json({ changelog: [] });
  }
}

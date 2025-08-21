import { NextResponse } from 'next/server';
import { getAllChangelogFiles } from '@/lib/github';

export async function GET() {
  try {
    console.log('Fetching changelogs from GitHub...');
    console.log('Environment variables:', {
      GITHUB_REPO_OWNER: process.env.GITHUB_REPO_OWNER,
      GITHUB_REPO_NAME: process.env.GITHUB_REPO_NAME,
      GITHUB_BRANCH: process.env.GITHUB_BRANCH,
      GITHUB_TOKEN: process.env.GITHUB_TOKEN ? 'SET' : 'NOT SET'
    });
    
    const changelogData = await getAllChangelogFiles();
    console.log('Changelog data:', changelogData);
    console.log('Changelog data length:', changelogData?.length || 0);

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

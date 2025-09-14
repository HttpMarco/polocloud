import { NextRequest, NextResponse } from 'next/server';

export async function GET(request: NextRequest) {
  try {
    const githubToken = process.env.GITHUB_TOKEN;
    
    if (!githubToken) {
      return NextResponse.json({ error: 'GitHub token not configured' }, { status: 500 });
    }

    const response = await fetch('https://api.github.com/repos/HttpMarco/polocloud/releases/latest', {
      headers: {
        'Authorization': `token ${githubToken}`,
        'Accept': 'application/vnd.github.v3+json',
        'User-Agent': 'PoloCloud-Website'
      }
    });

    if (!response.ok) {
      throw new Error(`GitHub API responded with status: ${response.status}`);
    }

    const releaseData = await response.json();
    
    return NextResponse.json({
      latestVersion: releaseData.tag_name,
      releaseName: releaseData.name,
      releaseDate: releaseData.published_at,
      releaseUrl: releaseData.html_url,
      isPrerelease: releaseData.prerelease,
      isDraft: releaseData.draft
    });

  } catch (error) {
    console.error('Error fetching GitHub releases:', error);
    return NextResponse.json({ 
      error: 'Failed to fetch latest release',
      fallbackVersion: 'v3.0.0-pre.5-SNAPSHOT'
    }, { status: 500 });
  }
}

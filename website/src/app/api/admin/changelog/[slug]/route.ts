import { NextRequest, NextResponse } from 'next/server';
import { cookies } from 'next/headers';
import { readFile } from 'fs/promises';
import path from 'path';
import {
  createOrUpdateChangelogFile,
  generateChangelogMDXContent,
  GITHUB_REPO_CONFIG,
  ChangelogMetadata,
  deleteChangelogFromGitHub
} from '@/lib/github';

let adminUsers: string[] = [];

async function loadAdminUsers() {
  try {
    const filePath = path.join(process.cwd(), 'data', 'admin-users.json');
    const data = await readFile(filePath, 'utf8');
    adminUsers = JSON.parse(data);
  } catch {
    adminUsers = [];
  }
}

export async function PUT(
  req: NextRequest,
  { params }: { params: Promise<{ slug: string }> }
) {
  try {
    await loadAdminUsers();

    const cookieStore = await cookies();
    const githubAdminCookie = cookieStore.get('github_admin_auth');
    const userCookie = cookieStore.get('user');
    const adminCookie = cookieStore.get('admin_auth');

    let isAuthenticated = false;
    let userData: { username?: string; id?: string } | null = null;

    if (githubAdminCookie) {
      try {
        userData = JSON.parse(githubAdminCookie.value);
        isAuthenticated = true;
      } catch {
        // Invalid cookie
      }
    }

    if (!isAuthenticated && userCookie) {
      try {
        userData = JSON.parse(userCookie.value);
        if (userData && userData.id && adminUsers.includes(userData.id)) {
          isAuthenticated = true;
        }
      } catch {
        // Invalid cookie
      }
    }

    if (!isAuthenticated && adminCookie) {
      try {
        const adminData = JSON.parse(adminCookie.value);
        isAuthenticated = true;
        userData = { username: adminData.username, id: 'admin' };
      } catch {
        // Invalid cookie
      }
    }

    if (!isAuthenticated) {
      return NextResponse.json({ error: 'Not authenticated' }, { status: 401 });
    }

    const { slug } = await params;
    const { version, type, content, author, description } = await req.json();

    if (!version || !type || !content) {
      return NextResponse.json({
        error: 'Version, type, and content are required'
      }, { status: 400 });
    }

    const metadata: ChangelogMetadata = {
      version: version.trim(),
      title: `Version ${version.trim()}`,
      description: description?.trim() || '',
      type: type as 'major' | 'minor' | 'patch' | 'hotfix',
      releaseDate: new Date().toISOString().split('T')[0],
      author: author?.trim() || userData?.username || 'Unknown',
      slug
    };

    const mdxContent = generateChangelogMDXContent(metadata, content.trim());
    const filePath = `${GITHUB_REPO_CONFIG.changelogPath}/${slug}.mdx`;

    await createOrUpdateChangelogFile(
      filePath,
      mdxContent,
      `Update changelog: Version ${version}`
    );

    return NextResponse.json({
      success: true,
      slug,
      message: 'Changelog updated successfully'
    });

  } catch (error) {
    console.error('Error updating changelog:', error);
    return NextResponse.json({
      error: 'Failed to update changelog',
      details: error instanceof Error ? error.message : 'Unknown error'
    }, { status: 500 });
  }
}

export async function DELETE(
  req: NextRequest,
  { params }: { params: Promise<{ slug: string }> }
) {
  try {
    await loadAdminUsers();

    const cookieStore = await cookies();
    const githubAdminCookie = cookieStore.get('github_admin_auth');
    const userCookie = cookieStore.get('user');
    const adminCookie = cookieStore.get('admin_auth');

    let isAuthenticated = false;
    let userData: { username?: string; id?: string } | null = null;

    if (githubAdminCookie) {
      try {
        userData = JSON.parse(githubAdminCookie.value);
        isAuthenticated = true;
      } catch {
        // Invalid cookie
      }
    }

    if (!isAuthenticated && userCookie) {
      try {
        userData = JSON.parse(userCookie.value);
        if (userData && userData.id && adminUsers.includes(userData.id)) {
          isAuthenticated = true;
        }
      } catch {
        // Invalid cookie
      }
    }

    if (!isAuthenticated && adminCookie) {
      try {
        const adminData = JSON.parse(adminCookie.value);
        isAuthenticated = true;
        userData = { username: adminData.username, id: 'admin' };
      } catch {
        // Invalid cookie
      }
    }

    if (!isAuthenticated) {
      return NextResponse.json({ error: 'Not authenticated' }, { status: 401 });
    }

    const { slug } = await params;
    
    await deleteChangelogFromGitHub(
      slug,
      userData?.username || 'admin'
    );

    return NextResponse.json({
      success: true,
      message: 'Changelog deleted successfully'
    });

  } catch (error) {
    console.error('Error deleting changelog:', error);
    return NextResponse.json({
      error: 'Failed to delete changelog',
      details: error instanceof Error ? error.message : 'Unknown error'
    }, { status: 500 });
  }
}


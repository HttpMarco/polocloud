import { NextRequest, NextResponse } from 'next/server';
import { cookies } from 'next/headers';
import { readFile } from 'fs/promises';
import path from 'path';
import {
  addChangelogToGitHub,
  GITHUB_REPO_CONFIG
} from '@/lib/github';

let adminUsers: string[] = [];

async function loadAdminUsers() {
  try {
    const filePath = path.join(process.cwd(), 'data', 'admin-users.json');
    const data = await readFile(filePath, 'utf8');
    adminUsers = JSON.parse(data);
  } catch (error) {
    adminUsers = [];
  }
}

export async function POST(req: NextRequest) {
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

    const { version, title, description, changes, type, releaseDate } = await req.json();

    if (!version || !title || !description || !changes || !type || !releaseDate) {
      return NextResponse.json({
        error: 'Version, title, description, changes, type, and releaseDate are required'
      }, { status: 400 });
    }

    if (!Array.isArray(changes) || changes.length === 0) {
      return NextResponse.json({
        error: 'Changes must be a non-empty array'
      }, { status: 400 });
    }

    if (!['major', 'minor', 'patch', 'hotfix'].includes(type)) {
      return NextResponse.json({
        error: 'Type must be one of: major, minor, patch, hotfix'
      }, { status: 400 });
    }

    const changelogData = {
      version: version.trim(),
      title: title.trim(),
      description: description.trim(),
      changes: changes.map((change: string) => change.trim()).filter(Boolean),
      type,
      releaseDate: releaseDate.trim(),
      author: userData?.username || 'Unknown'
    };

    const changelog = await addChangelogToGitHub(changelogData);

    return NextResponse.json({
      success: true,
      changelog,
      message: 'Changelog entry created successfully'
    });

  } catch (error) {
    console.error('Error creating changelog entry:', error);
    return NextResponse.json({
      error: 'Failed to create changelog entry',
      details: error instanceof Error ? error.message : 'Unknown error'
    }, { status: 500 });
  }
}

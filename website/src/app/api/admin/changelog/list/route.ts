import { NextRequest, NextResponse } from 'next/server';
import { cookies } from 'next/headers';
import { readFile } from 'fs/promises';
import path from 'path';
import { getAllChangelogFiles } from '@/lib/github';

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

export async function GET(req: NextRequest) {
  try {
    await loadAdminUsers();

    const cookieStore = await cookies();
    const githubAdminCookie = cookieStore.get('github_admin_auth');
    const userCookie = cookieStore.get('user');
    const adminCookie = cookieStore.get('admin_auth');

    let isAuthenticated = false;

    if (githubAdminCookie) {
      try {
        JSON.parse(githubAdminCookie.value);
        isAuthenticated = true;
      } catch {
        // Invalid cookie
      }
    }

    if (!isAuthenticated && userCookie) {
      try {
        const userData = JSON.parse(userCookie.value);
        if (userData && userData.id && adminUsers.includes(userData.id)) {
          isAuthenticated = true;
        }
      } catch {
        // Invalid cookie
      }
    }

    if (!isAuthenticated && adminCookie) {
      isAuthenticated = true;
    }

    if (!isAuthenticated) {
      return NextResponse.json({ error: 'Not authenticated' }, { status: 401 });
    }

    const changelogs = await getAllChangelogFiles();

    return NextResponse.json({
      success: true,
      changelogs
    });

  } catch (error) {
    console.error('Error fetching changelogs:', error);
    return NextResponse.json({
      error: 'Failed to fetch changelogs',
      details: error instanceof Error ? error.message : 'Unknown error'
    }, { status: 500 });
  }
}


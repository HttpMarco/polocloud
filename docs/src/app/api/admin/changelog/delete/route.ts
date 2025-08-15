import { NextRequest, NextResponse } from 'next/server';
import { cookies } from 'next/headers';
import { readFile } from 'fs/promises';
import path from 'path';
import { deleteChangelogFromGitHub } from '@/lib/github';

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

export async function DELETE(req: NextRequest) {
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

    const { slug } = await req.json();

    if (!slug) {
      return NextResponse.json({
        error: 'Changelog entry slug is required'
      }, { status: 400 });
    }

    await deleteChangelogFromGitHub(slug, userData?.username || 'Unknown');

    return NextResponse.json({
      success: true,
      message: 'Changelog entry deleted successfully'
    });

  } catch (error) {
    console.error('Error deleting changelog entry:', error);
    return NextResponse.json({
      error: 'Failed to delete changelog entry',
      details: error instanceof Error ? error.message : 'Unknown error'
    }, { status: 500 });
  }
}

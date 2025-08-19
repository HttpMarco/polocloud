import { NextRequest, NextResponse } from 'next/server';
import { cookies } from 'next/headers';
import { readFile } from 'fs/promises';
import path from 'path';
import { getAllBlogFiles } from '@/lib/github';

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

export async function GET(req: NextRequest) {
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

      }
    }

    if (!isAuthenticated && userCookie) {
      try {
        userData = JSON.parse(userCookie.value);


        if (userData && userData.id && adminUsers.includes(userData.id)) {
          isAuthenticated = true;
        }
      } catch {

      }
    }

    if (!isAuthenticated && adminCookie) {
      try {
        const adminData = JSON.parse(adminCookie.value);

        isAuthenticated = true;
        userData = { username: adminData.username, id: 'admin' };
      } catch {

      }
    }

    if (!isAuthenticated) {
      return NextResponse.json({ error: 'Not authenticated' }, { status: 401 });
    }

    try {
      const posts = await getAllBlogFiles();

      const adminPosts = posts.map(post => ({
        slug: post.slug,
        title: post.title,
        description: post.description,
        date: post.date,
        author: post.author,
        tags: post.tags,
        pinned: post.pinned,
        contentPreview: post.content ? post.content.substring(0, 200) + (post.content.length > 200 ? '...' : '') : '',
        wordCount: post.content ? post.content.split(/\s+/).length : 0,
      }));

      return NextResponse.json({ posts: adminPosts });
    } catch (error) {
      console.error('Error fetching blog posts:', error);
      return NextResponse.json({ posts: [] });
    }

  } catch (error) {
    return NextResponse.json({
      error: 'Failed to list blog posts',
      details: error instanceof Error ? error.message : 'Unknown error'
    }, { status: 500 });
  }
}

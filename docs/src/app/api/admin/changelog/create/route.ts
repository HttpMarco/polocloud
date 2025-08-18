import { NextRequest, NextResponse } from 'next/server';
import { cookies } from 'next/headers';
import { Octokit } from '@octokit/rest';
import { GITHUB_REPO_CONFIG } from '@/lib/github';
import { readFile } from 'fs/promises';
import path from 'path';

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

    const { version, title, description, type, releaseDate, content } = await req.json();

    if (!version || !title || !description || !type || !releaseDate || !content) {
      return NextResponse.json({
        error: 'Version, title, description, type, releaseDate, and content are required'
      }, { status: 400 });
    }

    if (!['major', 'minor', 'patch', 'hotfix'].includes(type)) {
      return NextResponse.json({
        error: 'Type must be one of: major, minor, patch, hotfix'
      }, { status: 400 });
    }

    const slug = title
      .toLowerCase()
      .replace(/[^a-z0-9\s-]/g, '')
      .replace(/\s+/g, '-')
      .replace(/-+/g, '-')
      .trim();

    const mdxContent = `---
version: "${version}"
title: "${title}"
description: "${description}"
type: "${type}"
releaseDate: "${releaseDate}"
author: "${userData?.username || 'Unknown'}"
---

${content}`;

    const githubToken = process.env.GITHUB_TOKEN;
    if (!githubToken) {
      return NextResponse.json({
        error: 'GitHub token not configured'
      }, { status: 500 });
    }

    const octokit = new Octokit({
      auth: githubToken,
    });

    const response = await octokit.rest.repos.createOrUpdateFileContents({
      owner: GITHUB_REPO_CONFIG.owner,
      repo: GITHUB_REPO_CONFIG.repo,
      path: `${GITHUB_REPO_CONFIG.changelogPath}/${slug}.mdx`,
      message: `Add changelog: ${title}`,
      content: Buffer.from(mdxContent).toString('base64'),
      branch: GITHUB_REPO_CONFIG.branch,
    });

    const status = response.status;
    if (status !== 200 && status !== 201) {
      throw new Error(`GitHub API error: ${status}`);
    }

    return NextResponse.json({
      success: true,
      slug,
      message: 'Changelog entry created successfully on GitHub',
      githubUrl: response.data.content?.html_url
    });

  } catch (error) {
    console.error('Error creating changelog entry:', error);
    return NextResponse.json({
      error: 'Failed to create changelog entry',
      details: error instanceof Error ? error.message : 'Unknown error'
    }, { status: 500 });
  }
}

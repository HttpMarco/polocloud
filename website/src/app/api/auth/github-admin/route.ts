import { NextRequest, NextResponse } from 'next/server';
import { getFileFromGitHub, createOrUpdateBlogFile } from '@/lib/github';

async function loadAllowedAdminUsers(): Promise<string[]> {
  try {
    console.log('Loading allowed admin users...');
    const adminFile = await getFileFromGitHub('website/data/github-admin-users.json');
    console.log('Admin file found:', !!adminFile);
    
    if (adminFile && adminFile.content) {
      console.log('Admin file content length:', adminFile.content.length);
      try {
        const users = JSON.parse(adminFile.content);
        console.log('Parsed users:', users);
        
        if (Array.isArray(users) && users.length > 0) {
          const usernames = users.map((user: { username: string }) => user.username);
          console.log('👥 Returning usernames from file:', usernames);
          return usernames;
        } else {
          console.log('Users array is empty or invalid, using fallback');
        }
      } catch (parseError) {
        console.error('Error parsing admin users JSON:', parseError);
      }
    } else {
      console.log('No admin file or content, using fallback');
    }

    console.log('🔄 Using fallback: HttpMarco');
    return ['HttpMarco'];
  } catch (error) {
    console.error('Error loading allowed admin users:', error);
    console.log('🔄 Using fallback due to error: HttpMarco');
    return ['HttpMarco'];
  }
}

async function addUserToAdminList(username: string, userId: string): Promise<void> {
  try {
    console.log('➕ Adding user to admin list:', username);

    let currentUsers: Array<{
      username: string;
      id: string;
      role: string;
      addedBy: string;
      addedAt: string;
      isFounder: boolean;
    }> = [];
    
    try {
      const adminFile = await getFileFromGitHub('website/data/github-admin-users.json');
      if (adminFile && adminFile.content) {
        currentUsers = JSON.parse(adminFile.content);
        if (!Array.isArray(currentUsers)) {
          currentUsers = [];
        }
      }
    } catch {
      console.log('Could not load existing admin users, starting fresh');
      currentUsers = [];
    }

    const userExists = currentUsers.find((user) => user.username === username);
    if (userExists) {
      console.log('ℹ️ User already exists in admin list:', username);
      return;
    }

    const newUser = {
      username,
      id: userId,
      role: 'ADMIN',
      addedBy: 'system',
      addedAt: new Date().toISOString(),
      isFounder: username === 'HttpMarco'
    };

    currentUsers.push(newUser);

    const content = JSON.stringify(currentUsers, null, 2);
    await createOrUpdateBlogFile(
      'website/data/github-admin-users.json',
      content,
      `Add admin user: ${username}`,
      undefined
    );

    console.log('User successfully added to admin list:', username);
  } catch (error) {
    console.error('Error adding user to admin list:', error);
  }
}

export async function GET(request: NextRequest) {
  const { searchParams } = new URL(request.url);
  const code = searchParams.get('code');
  const state = searchParams.get('state');

  if (!code) {

    const githubClientId = process.env.NEXT_PUBLIC_GITHUB_CLIENT_ID;

    if (!githubClientId) {
      return NextResponse.json(
        { error: 'GitHub OAuth not configured' },
        { status: 500 }
      );
    }

    const params = new URLSearchParams({
      client_id: githubClientId,
      redirect_uri: `${process.env.NEXT_PUBLIC_BASE_URL || 'https://polocloud.de'}/api/auth/github-admin`,
      scope: 'user:email',
      state: 'admin-login'
    });

    return NextResponse.redirect(`https://github.com/login/oauth/authorize?${params}`);
  }

  try {
    const githubClientId = process.env.NEXT_PUBLIC_GITHUB_CLIENT_ID;
    const githubClientSecret = process.env.GITHUB_CLIENT_SECRET;

    if (!githubClientId || !githubClientSecret) {
      return NextResponse.json(
        { error: 'GitHub OAuth not configured' },
        { status: 500 }
      );
    }

    const tokenResponse = await fetch('https://github.com/login/oauth/access_token', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        client_id: githubClientId,
        client_secret: githubClientSecret,
        code,
      }),
    });

    const tokenData = await tokenResponse.json();

    if (!tokenData.access_token) {
      console.error('Failed to get access token:', tokenData);
      console.error('Full token response:', JSON.stringify(tokenData, null, 2));
      const baseUrl = process.env.NEXT_PUBLIC_BASE_URL || 'https://polocloud.de';
      return NextResponse.redirect(`${baseUrl}/admin?error=oauth_failed`);
    }

    const userResponse = await fetch('https://api.github.com/user', {
      headers: {
        'Authorization': `Bearer ${tokenData.access_token}`,
        'Accept': 'application/vnd.github.v3+json',
      },
    });

    const userData = await userResponse.json();

    const allowedUsers = await loadAllowedAdminUsers();
    console.log(`GitHub login attempt by: ${userData.login}`);
    console.log(`Allowed users:`, allowedUsers);

    if (!allowedUsers.includes(userData.login)) {
      console.log('User not authorized:', userData.login);
      const baseUrl = process.env.NEXT_PUBLIC_BASE_URL || 'https://polocloud.de';
      return NextResponse.redirect(`${baseUrl}/admin?error=unauthorized`);
    }

    await addUserToAdminList(userData.login, userData.id.toString());

    const adminSession = {
      id: userData.id,
      username: userData.login,
      name: userData.name || userData.login,
      avatar: userData.avatar_url,
      isGitHubAdmin: true,
      loginTime: new Date().toISOString()
    };

    const baseUrl = process.env.NEXT_PUBLIC_BASE_URL || 'https://polocloud.de';
    const response = NextResponse.redirect(`${baseUrl}/admin?success=github_login`);

    response.cookies.set('github_admin_auth', JSON.stringify(adminSession), {
      httpOnly: true,
      secure: process.env.NODE_ENV === 'production',
      sameSite: 'lax',
      maxAge: 60 * 60 * 24 * 7,
      path: '/'
    });

    return response;

  } catch (error) {
    console.error('GitHub admin OAuth error:', error);
    const baseUrl = process.env.NEXT_PUBLIC_BASE_URL || 'https://polocloud.de';
    return NextResponse.redirect(`${baseUrl}/admin?error=oauth_error`);
  }
}

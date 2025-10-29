import { NextRequest, NextResponse } from 'next/server';
import { getFileFromGitHub } from '@/lib/github';

async function loadAllowedAdminUsers(): Promise<string[]> {
  try {
    console.log('[ME] Loading allowed admin users...');
    const adminFile = await getFileFromGitHub('website/data/github-admin-users.json');
    console.log('[ME] Admin file found:', !!adminFile);
    
    if (adminFile && adminFile.content) {
      console.log('[ME] Admin file content length:', adminFile.content.length);
      try {
        const users = JSON.parse(adminFile.content);
        console.log('[ME] Parsed users:', users);
        
        if (Array.isArray(users) && users.length > 0) {
          const usernames = users.map((user: { username: string }) => user.username);
          console.log('👥 [ME] Returning usernames from file:', usernames);
          return usernames;
        } else {
          console.log('[ME] Users array is empty or invalid, using fallback');
        }
      } catch (parseError) {
        console.error('[ME] Error parsing admin users JSON:', parseError);
      }
    } else {
      console.log('[ME] No admin file or content, using fallback');
    }

    console.log('🔄 [ME] Using fallback: HttpMarco');
    return ['HttpMarco'];
  } catch (error) {
    console.error('[ME] Error loading allowed admin users:', error);
    console.log('🔄 [ME] Using fallback due to error: HttpMarco');
    return ['HttpMarco'];
  }
}

export async function GET(request: NextRequest) {
  try {
    const githubAdminCookie = request.cookies.get('github_admin_auth');

    if (!githubAdminCookie) {
      return NextResponse.json({ authenticated: false });
    }

    let session;
    try {
      session = JSON.parse(githubAdminCookie.value);
    } catch {
      return NextResponse.json({ authenticated: false });
    }


    if (!session.username || !session.id) {
      return NextResponse.json({ authenticated: false });
    }


    const allowedUsers = await loadAllowedAdminUsers();
    if (!allowedUsers.includes(session.username)) {

      const response = NextResponse.json({
        authenticated: false,
        error: 'Access revoked',
        message: 'Your admin access has been revoked'
      }, { status: 403 });

      response.cookies.set('github_admin_auth', '', {
        maxAge: 0,
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        sameSite: 'lax'
      });

      return response;
    }

    return NextResponse.json({
      authenticated: true,
      user: {
        id: session.id,
        username: session.username,
        name: session.name,
        avatar: session.avatar,
        isGitHubAdmin: true
      }
    });
  } catch (error) {
    console.error('Error validating GitHub admin session:', error);
    return NextResponse.json({ authenticated: false }, { status: 500 });
  }
}
import { NextRequest, NextResponse } from 'next/server';
import { getPlatformsFromGitHub, savePlatformsToGitHub } from '@/lib/github';

export async function GET(req: NextRequest) {
  try {

    const adminAuth = req.cookies.get('admin_auth')?.value;
    const discordUser = req.cookies.get('discord_user')?.value;
    const githubAdminAuth = req.cookies.get('github_admin_auth')?.value;

    if (!adminAuth && !discordUser && !githubAdminAuth) {
      return NextResponse.json({ error: 'Unauthorized' }, { status: 401 });
    }

    const platforms = await getPlatformsFromGitHub();
    return NextResponse.json({ platforms });
  } catch (error) {
    console.error('Error fetching platforms:', error);
    return NextResponse.json({ error: 'Failed to fetch platforms' }, { status: 500 });
  }
}

export async function POST(req: NextRequest) {
  try {

    const githubAdminAuth = req.cookies.get('github_admin_auth')?.value;
    const adminAuth = req.cookies.get('admin_auth')?.value;
    const discordUser = req.cookies.get('discord_user')?.value;

    if (!githubAdminAuth && !adminAuth && !discordUser) {
      return NextResponse.json({ error: 'Unauthorized' }, { status: 401 });
    }


    if (githubAdminAuth) {
      const adminAuthData = JSON.parse(githubAdminAuth);
      if (adminAuthData.username !== 'jakubbbdev') {
        return NextResponse.json({
          error: `Only jakubbbdev can manage platforms. Your username: ${adminAuthData.username}`
        }, { status: 403 });
      }
    } else if (adminAuth) {
      const adminData = JSON.parse(adminAuth);
      if (adminData.username !== 'jakubbbdev' && adminData.username !== 'admin') {
        return NextResponse.json({
          error: `Only admin can manage platforms. Your username: ${adminData.username}`
        }, { status: 403 });
      }
    } else {
      return NextResponse.json({ error: 'Admin authentication required' }, { status: 401 });
    }

    const { name, icon, versions, addons } = await req.json();

    if (!name || !icon) {
      return NextResponse.json({ error: 'Name and icon are required' }, { status: 400 });
    }

    if (!icon.startsWith('https://') || !icon.includes('vercel-storage.com')) {
      return NextResponse.json({ 
        error: 'Invalid icon URL. Please upload an image first.' 
      }, { status: 400 });
    }

    const currentPlatforms = await getPlatformsFromGitHub();

    const newPlatform = {
      id: Date.now().toString(),
      name: name.trim(),
      icon: icon.trim(),
      versions: versions || {
        '1.7-1.12': 'not-supported',
        '1.12-1.16': 'not-supported',
        '1.18-1.19': 'not-supported',
        '1.20+': 'not-supported'
      },
      addons: addons || {
        'Severmobs': 'not-supported',
        'Signs': 'not-supported'
      },
      addedAt: new Date().toISOString(),
      addedBy: 'jakubbbdev'
    };

    const updatedPlatforms = [...currentPlatforms, newPlatform];

    await savePlatformsToGitHub(updatedPlatforms, `Add new platform: ${newPlatform.name}`);

    console.log('✅ Platform added successfully, cache will refresh automatically');

    return NextResponse.json({
      success: true,
      platform: newPlatform,
      message: 'Platform added successfully'
    });
  } catch (error) {
    console.error('Error adding platform:', error);
    return NextResponse.json({ error: 'Failed to add platform' }, { status: 500 });
  }
}

export async function DELETE(req: NextRequest) {
  try {

    const githubAdminAuth = req.cookies.get('github_admin_auth')?.value;
    const adminAuth = req.cookies.get('admin_auth')?.value;
    const discordUser = req.cookies.get('discord_user')?.value;

    if (!githubAdminAuth && !adminAuth && !discordUser) {
      return NextResponse.json({ error: 'Unauthorized' }, { status: 401 });
    }

    if (githubAdminAuth) {
      const adminAuthData = JSON.parse(githubAdminAuth);
      if (adminAuthData.username !== 'jakubbbdev') {
        return NextResponse.json({
          error: `Only jakubbbdev can manage platforms. Your username: ${adminAuthData.username}`
        }, { status: 403 });
      }
    } else if (adminAuth) {
      const adminData = JSON.parse(adminAuth);
      if (adminData.username !== 'jakubbbdev' && adminData.username !== 'admin') {
        return NextResponse.json({
          error: `Only admin can manage platforms. Your username: ${adminData.username}`
        }, { status: 403 });
      }
    } else {
      return NextResponse.json({ error: 'Admin authentication required' }, { status: 401 });
    }

    const { platformId } = await req.json();

    if (!platformId) {
      return NextResponse.json({ error: 'Platform ID is required' }, { status: 400 });
    }

    const currentPlatforms = await getPlatformsFromGitHub();
    const platformToRemove = currentPlatforms.find(p => p.id === platformId);

    if (!platformToRemove) {
      return NextResponse.json({ error: 'Platform not found' }, { status: 404 });
    }

    const updatedPlatforms = currentPlatforms.filter(p => p.id !== platformId);
    await savePlatformsToGitHub(updatedPlatforms, `Remove platform: ${platformToRemove.name}`);

    if (platformToRemove.icon && platformToRemove.icon.includes('vercel-storage.com')) {
      try {

        const urlParts = platformToRemove.icon.split('/');
        const blobId = `/polocloud/storage/images/platforms/${urlParts[urlParts.length - 1]}`;

        const { del } = await import('@vercel/blob');
        await del(blobId);
        
        console.log('✅ Platform icon deleted from Vercel Blob:', blobId);
      } catch (error) {
        console.warn('⚠️ Error deleting platform icon from Vercel Blob:', error);
      }
    }

    console.log('✅ Platform removed successfully, cache will refresh automatically');

    return NextResponse.json({
      success: true,
      message: 'Platform removed successfully'
    });
  } catch (error) {
    console.error('Error removing platform:', error);
    return NextResponse.json({ error: 'Failed to remove platform' }, { status: 500 });
  }
}

export async function PUT(req: NextRequest) {
  try {

    const githubAdminAuth = req.cookies.get('github_admin_auth')?.value;
    const adminAuth = req.cookies.get('admin_auth')?.value;
    const discordUser = req.cookies.get('discord_user')?.value;

    if (!githubAdminAuth && !adminAuth && !discordUser) {
      return NextResponse.json({ error: 'Unauthorized' }, { status: 401 });
    }

    if (githubAdminAuth) {
      const adminAuthData = JSON.parse(githubAdminAuth);
      if (adminAuthData.username !== 'jakubbbdev') {
        return NextResponse.json({
          error: `Only jakubbbdev can manage platforms. Your username: ${adminAuthData.username}`
        }, { status: 403 });
      }
    } else if (adminAuth) {
      const adminData = JSON.parse(adminAuth);
      if (adminData.username !== 'jakubbbdev' && adminData.username !== 'admin') {
        return NextResponse.json({
          error: `Only admin can manage platforms. Your username: ${adminData.username}`
        }, { status: 403 });
      }
    } else {
      return NextResponse.json({ error: 'Admin authentication required' }, { status: 401 });
    }

    const { id, name, icon, versions, addons } = await req.json();

    if (!id || !name || !icon) {
      return NextResponse.json({ error: 'ID, name, and icon are required' }, { status: 400 });
    }

    if (!icon.startsWith('https://') || !icon.includes('vercel-storage.com')) {
      return NextResponse.json({ 
        error: 'Invalid icon URL. Please upload an image first.' 
      }, { status: 400 });
    }

    const currentPlatforms = await getPlatformsFromGitHub();
    const platformIndex = currentPlatforms.findIndex(p => p.id === id);

    if (platformIndex === -1) {
      return NextResponse.json({ error: 'Platform not found' }, { status: 404 });
    }

    const updatedPlatform = {
      ...currentPlatforms[platformIndex],
      name: name.trim(),
      icon: icon.trim(),
      versions: versions || {
        '1.7-1.12': 'not-supported',
        '1.12-1.16': 'not-supported',
        '1.18-1.19': 'not-supported',
        '1.20+': 'not-supported'
      },
      addons: addons || {
        'Severmobs': 'not-supported',
        'Signs': 'not-supported'
      }
    };

    currentPlatforms[platformIndex] = updatedPlatform;
    await savePlatformsToGitHub(currentPlatforms, `Update platform: ${updatedPlatform.name}`);

    console.log('✅ Platform updated successfully, cache will refresh automatically');

    return NextResponse.json({
      success: true,
      platform: updatedPlatform,
      message: 'Platform updated successfully'
    });
  } catch (error) {
    console.error('Error updating platform:', error);
    return NextResponse.json({ error: 'Failed to update platform' }, { status: 500 });
  }
}

import { NextRequest, NextResponse } from 'next/server';
import { put } from '@vercel/blob';

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
    } else if (adminAuth) {
      const adminData = JSON.parse(adminAuth);
    } else {
      return NextResponse.json({ error: 'Admin authentication required' }, { status: 401 });
    }

    const formData = await req.formData();
    const file = formData.get('file') as File;
    const partnerName = formData.get('partnerName') as string;
    const platformName = formData.get('platformName') as string;

    if (!file) {
      return NextResponse.json({ error: 'No file provided' }, { status: 400 });
    }

    if (!partnerName && !platformName) {
      return NextResponse.json({ error: 'Either partnerName or platformName is required' }, { status: 400 });
    }

    const allowedTypes = ['image/png', 'image/jpeg', 'image/svg+xml', 'image/webp'];
    if (!allowedTypes.includes(file.type)) {
      return NextResponse.json({ 
        error: 'Invalid file type. Only PNG, JPG, SVG, and WebP are allowed.' 
      }, { status: 400 });
    }

    const maxSize = 5 * 1024 * 1024;
    if (file.size > maxSize) {
      return NextResponse.json({ 
        error: 'File too large. Maximum size is 5MB.' 
      }, { status: 400 });
    }

    const timestamp = Date.now();
    const fileExtension = file.name.split('.').pop();
    
    let fileName: string;
    let blobPath: string;
    
    if (partnerName) {

      const sanitizedName = partnerName.replace(/[^a-zA-Z0-9]/g, '-').toLowerCase();
      fileName = `partner-${sanitizedName}-${timestamp}.${fileExtension}`;
      blobPath = `/polocloud/storage/images/partners/${fileName}`;
    } else {

      const sanitizedName = platformName.replace(/[^a-zA-Z0-9]/g, '-').toLowerCase();
      fileName = `platform-${sanitizedName}-${timestamp}.${fileExtension}`;
      blobPath = `/polocloud/storage/images/platforms/${fileName}`;
    }

    const blob = await put(blobPath, file, {
      access: 'public',
      addRandomSuffix: false
    });

    console.log('Image uploaded successfully to Vercel Blob:', blob.url);

    return NextResponse.json({
      success: true,
      url: blob.url,
      blobId: blob.pathname,
      message: 'Image uploaded successfully'
    });

  } catch (error) {
    console.error('Error uploading image:', error);
    return NextResponse.json({ 
      error: 'Failed to upload image' 
    }, { status: 500 });
  }
}

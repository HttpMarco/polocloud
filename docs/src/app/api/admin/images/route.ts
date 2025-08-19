import { NextRequest, NextResponse } from 'next/server';
import { put, del, list } from '@vercel/blob';

export async function GET(req: NextRequest) {
  try {
    const { searchParams } = new URL(req.url);
    const category = searchParams.get('category');
    
    console.log('GET /api/admin/images - Category filter:', category);

    const githubAdminAuth = req.cookies.get('github_admin_auth')?.value;
    const adminAuth = req.cookies.get('admin_auth')?.value;
    
    if (!githubAdminAuth && !adminAuth) {
      console.log('No admin authentication found');
      return NextResponse.json({ error: 'Admin authentication required' }, { status: 401 });
    }

    console.log('Admin authentication verified');

    const { blobs } = await list();
    console.log('📦 Total blobs found:', blobs.length);

    console.log('All blob pathnames:', blobs.map(b => b.pathname));

    const imageBlobs = blobs.filter(blob => {
      const isImage = blob.pathname.includes('polocloud/storage/images/');
      console.log(`Blob ${blob.pathname}: isImage = ${isImage}`);
      return isImage;
    });
    console.log('🖼️ Image blobs found:', imageBlobs.length);

    const filteredBlobs = category && category !== 'all'
      ? imageBlobs.filter(blob => blob.pathname.includes(`/${category}/`))
      : imageBlobs;
    console.log('Filtered blobs:', filteredBlobs.length);

    const images = filteredBlobs.map(blob => {
      const pathParts = blob.pathname.split('/');
      const fileName = pathParts[pathParts.length - 1];
      const category = pathParts[pathParts.length - 2] || 'general';

      const fileExtension = fileName.split('.').pop()?.toLowerCase();
      let mimeType = 'image/unknown';
      if (fileExtension === 'png') mimeType = 'image/png';
      else if (fileExtension === 'jpg' || fileExtension === 'jpeg') mimeType = 'image/jpeg';
      else if (fileExtension === 'svg') mimeType = 'image/svg+xml';
      else if (fileExtension === 'webp') mimeType = 'image/webp';

      const validCategory = ['general', 'partners', 'platforms', 'blog', 'changelog'].includes(category) 
        ? category as 'general' | 'partners' | 'platforms' | 'blog' | 'changelog'
        : 'general';
      
      return {
        id: blob.pathname,
        name: fileName,
        url: blob.url,
        blobId: blob.pathname,
        size: blob.size || 0,
        type: mimeType,
        uploadedAt: blob.uploadedAt || new Date().toISOString(),
        uploadedBy: 'admin',
        category: validCategory
      };
    });

    console.log('Returning images:', images.length);
    console.log('📋 Image details:', images.map(img => ({ name: img.name, category: img.category, url: img.url })));

    return NextResponse.json({ images });
  } catch (error) {
    console.error('Error fetching images:', error);
    return NextResponse.json({ error: 'Failed to fetch images' }, { status: 500 });
  }
}

export async function POST(req: NextRequest) {
  try {
    console.log('📤 POST /api/admin/images - Starting upload');

    const githubAdminAuth = req.cookies.get('github_admin_auth')?.value;
    const adminAuth = req.cookies.get('admin_auth')?.value;
    
    if (!githubAdminAuth && !adminAuth) {
      console.log('No admin authentication found for upload');
      return NextResponse.json({ error: 'Admin authentication required' }, { status: 401 });
    }

    console.log('Admin authentication verified for upload');

    const formData = await req.formData();
    const file = formData.get('file') as File;
    const category = formData.get('category') as string || 'general';

    console.log('Upload details:', {
      fileName: file?.name,
      fileSize: file?.size,
      fileType: file?.type,
      category: category
    });

    if (!file) {
      console.log('No file provided');
      return NextResponse.json({ error: 'No file provided' }, { status: 400 });
    }
    const validCategories = ['general', 'partners', 'platforms', 'blog', 'changelog'];
    if (!validCategories.includes(category)) {
      console.log('Invalid category:', category);
      return NextResponse.json({ 
        error: 'Invalid category. Allowed categories: general, partners, platforms, blog, changelog' 
      }, { status: 400 });
    }

    const allowedTypes = ['image/png', 'image/jpeg', 'image/svg+xml', 'image/webp'];
    if (!allowedTypes.includes(file.type)) {
      console.log('Invalid file type:', file.type);
      return NextResponse.json({ 
        error: 'Invalid file type. Only PNG, JPG, SVG, and WebP are allowed.' 
      }, { status: 400 });
    }

    const maxSize = 5 * 1024 * 1024;
    if (file.size > maxSize) {
      console.log('File too large:', file.size, 'bytes');
      return NextResponse.json({ 
        error: 'File too large. Maximum size is 5MB.' 
      }, { status: 400 });
    }

    const timestamp = Date.now();
    const fileExtension = file.name.split('.').pop();
    const sanitizedName = file.name.replace(/[^a-zA-Z0-9]/g, '-').toLowerCase();
    const fileName = `${category}-${sanitizedName}-${timestamp}.${fileExtension}`;
    const blobPath = `/polocloud/storage/images/${category}/${fileName}`;

    console.log('📂 Uploading to blob path:', blobPath);

    const blob = await put(blobPath, file, {
      access: 'public',
      addRandomSuffix: false
    });

    console.log('Image uploaded successfully to Vercel Blob:', blob.url);

    const imageData = {
      id: blob.pathname,
      name: fileName,
      url: blob.url,
      blobId: blob.pathname,
      size: file.size,
      type: file.type,
      uploadedAt: new Date().toISOString(),
      uploadedBy: 'admin',
      category: category as 'general' | 'partners' | 'platforms' | 'blog' | 'changelog'
    };

    console.log('📋 Returning image data:', imageData);

    return NextResponse.json({
      success: true,
      image: imageData,
      message: 'Image uploaded successfully'
    });

  } catch (error) {
    console.error('Error uploading image:', error);
    return NextResponse.json({ 
      error: 'Failed to upload image' 
    }, { status: 500 });
  }
}

export async function DELETE(req: NextRequest) {
  try {
    const githubAdminAuth = req.cookies.get('github_admin_auth')?.value;
    const adminAuth = req.cookies.get('admin_auth')?.value;
    
    if (!githubAdminAuth && !adminAuth) {
      return NextResponse.json({ error: 'Admin authentication required' }, { status: 401 });
    }

    const { blobId } = await req.json();

    if (!blobId) {
      return NextResponse.json({ error: 'Blob ID is required' }, { status: 400 });
    }

    await del(blobId);
    
    console.log('Image deleted from Vercel Blob:', blobId);

    return NextResponse.json({
      success: true,
      message: 'Image deleted successfully'
    });

  } catch (error) {
    console.error('Error deleting image:', error);
    return NextResponse.json({ 
      error: 'Failed to delete image' 
    }, { status: 500 });
  }
}

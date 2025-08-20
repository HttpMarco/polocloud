import { NextRequest, NextResponse } from 'next/server';
import { getPartnersFromGitHub, savePartnersToGitHub } from '@/lib/github';
import { Partner } from '@/components/admin/types';

export async function GET(req: NextRequest) {
  try {

    const adminAuth = req.cookies.get('admin_auth')?.value;
    const discordUser = req.cookies.get('discord_user')?.value;
    const githubAdminAuth = req.cookies.get('github_admin_auth')?.value;

    if (!adminAuth && !discordUser && !githubAdminAuth) {
      return NextResponse.json({ error: 'Unauthorized' }, { status: 401 });
    }

    const partners = await getPartnersFromGitHub();
    return NextResponse.json({ partners });
  } catch (error) {
    console.error('Error fetching partners:', error);
    return NextResponse.json({ error: 'Failed to fetch partners' }, { status: 500 });
  }
}

export async function POST(req: NextRequest) {
  try {

    const githubAdminAuth = req.cookies.get('github_admin_auth')?.value;
    const adminAuth = req.cookies.get('admin_auth')?.value;
    const discordUser = req.cookies.get('discord_user')?.value;

    console.log('- github_admin_auth:', githubAdminAuth ? 'exists' : 'missing');
    console.log('- admin_auth:', adminAuth ? 'exists' : 'missing');
    console.log('- discord_user:', discordUser ? 'exists' : 'missing');

    let userLogin: string | undefined;
    let adminData: { username?: string } | undefined;

    if (githubAdminAuth) {
      const adminAuthData = JSON.parse(githubAdminAuth);
      userLogin = adminAuthData.username;
    }
    else if (adminAuth) {
      adminData = JSON.parse(adminAuth);
    }
    else {
      return NextResponse.json({ error: 'Admin authentication required' }, { status: 401 });
    }

    const { name, logo, website, description } = await req.json();

    if (!name || !logo) {
      return NextResponse.json({ error: 'Name and logo are required' }, { status: 400 });
    }

    if (!logo.startsWith('https://') || !logo.includes('vercel-storage.com')) {
      return NextResponse.json({ 
        error: 'Invalid logo URL. Please upload an image first.' 
      }, { status: 400 });
    }

    const currentPartners = await getPartnersFromGitHub();

    const newPartner = {
      id: Date.now().toString(),
      name: name.trim(),
      logo: logo.trim(),
      website: website?.trim() || '',
      description: description?.trim() || '',
      addedAt: new Date().toISOString(),
      addedBy: userLogin || adminData?.username || 'Unknown'
    };

    const updatedPartners = [...currentPartners, newPartner];

    await savePartnersToGitHub(updatedPartners, `Add new partner: ${newPartner.name}`);

    console.log('Partner added successfully, cache will refresh automatically');

    return NextResponse.json({
      success: true,
      partner: newPartner,
      message: 'Partner added successfully'
    });
  } catch (error) {
    console.error('Error adding partner:', error);
    return NextResponse.json({ error: 'Failed to add partner' }, { status: 500 });
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
    } else if (adminAuth) {
      const adminData = JSON.parse(adminAuth);
    } else {
      return NextResponse.json({ error: 'Admin authentication required' }, { status: 401 });
    }

    const { partnerId } = await req.json();

    if (!partnerId) {
      return NextResponse.json({ error: 'Partner ID is required' }, { status: 400 });
    }

    const currentPartners = await getPartnersFromGitHub();
    const partnerToRemove = currentPartners.find((p: Partner) => p.id === partnerId);

    if (!partnerToRemove) {
      return NextResponse.json({ error: 'Partner not found' }, { status: 404 });
    }

    const updatedPartners = currentPartners.filter((p: Partner) => p.id !== partnerId);
    await savePartnersToGitHub(updatedPartners, `Remove partner: ${partnerToRemove.name}`);

    if (partnerToRemove.logo && partnerToRemove.logo.includes('vercel-storage.com')) {
      try {

        const urlParts = partnerToRemove.logo.split('/');
        const fileName = urlParts[urlParts.length - 1];

        const blobId = `/polocloud/storage/images/partners/${fileName}`;
        
        console.log('🗑️ Attempting to delete blob:', blobId);
        console.log('🗑️ Original logo URL:', partnerToRemove.logo);
        console.log('🗑️ Extracted filename:', fileName);

        const { del } = await import('@vercel/blob');
        await del(blobId);
        
        console.log('Partner logo deleted from Vercel Blob:', blobId);
      } catch (error) {
        console.error('Error deleting partner logo from Vercel Blob:', error);
        console.error('Error details:', error);
      }
    }

    console.log('Partner removed successfully, cache will refresh automatically');

    return NextResponse.json({
      success: true,
      message: 'Partner removed successfully'
    });
  } catch (error) {
    console.error('Error removing partner:', error);
    return NextResponse.json({ error: 'Failed to remove partner' }, { status: 500 });
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
    } else if (adminAuth) {
      const adminData = JSON.parse(adminAuth);
    } else {
      return NextResponse.json({ error: 'Admin authentication required' }, { status: 401 });
    }

    const { id, name, logo, website, description } = await req.json();

    if (!id || !name || !logo) {
      return NextResponse.json({ error: 'ID, name, and logo are required' }, { status: 400 });
    }
    if (!logo.startsWith('https://') || !logo.includes('vercel-storage.com')) {
      return NextResponse.json({ 
        error: 'Invalid logo URL. Please upload an image first.' 
      }, { status: 400 });
    }

    const currentPartners = await getPartnersFromGitHub();
    const partnerIndex = currentPartners.findIndex((p: Partner) => p.id === id);

    if (partnerIndex === -1) {
      return NextResponse.json({ error: 'Partner not found' }, { status: 404 });
    }

    const updatedPartner = {
      ...currentPartners[partnerIndex],
      name: name.trim(),
      logo: logo.trim(),
      website: website.trim(),
      description: description.trim()
    };

    currentPartners[partnerIndex] = updatedPartner;
    await savePartnersToGitHub(currentPartners, `Update partner: ${updatedPartner.name}`);

    console.log('Partner updated successfully, cache will refresh automatically');

    return NextResponse.json({
      success: true,
      partner: updatedPartner,
      message: 'Partner updated successfully'
    });
  } catch (error) {
    console.error('Error updating partner:', error);
    return NextResponse.json({ error: 'Failed to update partner' }, { status: 500 });
  }
}

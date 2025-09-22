"use client";

import { useEffect, useMemo, useState } from 'react';
import { ToastContainer, showToast } from '@/components/ui/toast';
import { 
  AdminHeader, 
  TabNavigation, 
  FeedbackTab, 
  UsersTab, 
  PartnersTab, 
  PlatformsTab, 
  ChangelogTab,
  BlogTab,
  ImagesTab,
  LoginForm
} from '@/components/admin';
import { 
  MeResp, 
  Feedback, 
  AdminUser, 
  Partner, 
  NewPartner, 
  Platform, 
  NewPlatform, 
  ActiveTab,
  EditPlatform,
  EditPartner,
  ChangelogEntry,
  ImageFile
} from '@/components/admin/types';
import { BlogPostMetadata } from '@/lib/github';

export default function AdminPage() {
  const [loading, setLoading] = useState(true);
  const [auth, setAuth] = useState<MeResp | null>(null);

  const [feedbacks, setFeedbacks] = useState<Feedback[]>([]);
  const [loadingFeedbacks, setLoadingFeedbacks] = useState(false);

  const [query, setQuery] = useState('');
  const [ratingFilter, setRatingFilter] = useState<number | null>(null);
  const [statusFilter, setStatusFilter] = useState<'ALL' | 'PENDING' | 'APPROVED' | 'REJECTED'>('ALL');

  const [adminUsers, setAdminUsers] = useState<AdminUser[]>([]);
  const [loadingUsers, setLoadingUsers] = useState(false);
  const [isSuperAdmin, setIsSuperAdmin] = useState(false);
  const [newUserGitHubUsername, setNewUserGitHubUsername] = useState('');
  const [addingUser, setAddingUser] = useState(false);

  const [partners, setPartners] = useState<Partner[]>([]);
  const [loadingPartners, setLoadingPartners] = useState(false);

  const [platforms, setPlatforms] = useState<Platform[]>([]);
  const [loadingPlatforms, setLoadingPlatforms] = useState(false);

  const [changelog, setChangelog] = useState<ChangelogEntry[]>([]);
  const [loadingChangelog, setLoadingChangelog] = useState(false);

  const [blogPosts, setBlogPosts] = useState<BlogPostMetadata[]>([]);
  const [loadingBlog, setLoadingBlog] = useState(false);

  const [images, setImages] = useState<ImageFile[]>([]);
  const [loadingImages, setLoadingImages] = useState(false);

  const [activeTab, setActiveTab] = useState<ActiveTab>('feedback');
  const [newPartner, setNewPartner] = useState<NewPartner>({
    name: '',
    logo: '',
    website: '',
    description: ''
  });
  const [addingPartner, setAddingPartner] = useState(false);
  const [editingPartner, setEditingPartner] = useState<EditPartner | null>(null);
  const [editingPartnerId, setEditingPartnerId] = useState<string | null>(null);

  const [newPlatform, setNewPlatform] = useState<NewPlatform>({
    name: '',
    icon: '',
    versions: {
      '1.7-1.12': 'not-supported',
      '1.12-1.16': 'not-supported',
      '1.18-1.19': 'not-supported',
      '1.20+': 'not-supported'
    },
    addons: {
      'Severmobs': 'not-supported',
      'Signs': 'not-supported'
    }
  });
  const [addingPlatform, setAddingPlatform] = useState(false);
  const [editingPlatform, setEditingPlatform] = useState<EditPlatform | null>(null);
  const [editingPlatformId, setEditingPlatformId] = useState<string | null>(null);

  const filteredFeedbacks = useMemo(() => {
    const filtered = feedbacks.filter(fb => {
      const q = query.toLowerCase().trim();
      const matchesQuery = !q || fb.username.toLowerCase().includes(q) || fb.description.toLowerCase().includes(q);
      const matchesRating = ratingFilter == null || fb.rating === ratingFilter;
      const matchesStatus = statusFilter === 'ALL' || fb.status === statusFilter;
      return matchesQuery && matchesRating && matchesStatus;
    });
    
    console.log('Filtered Feedbacks:', {
      total: feedbacks.length,
      filtered: filtered.length,
      query,
      ratingFilter,
      statusFilter,
      feedbacks: feedbacks.map(f => ({ id: f.id, username: f.username, status: f.status }))
    });
    
    return filtered;
  }, [feedbacks, query, ratingFilter, statusFilter]);

  const feedbackStatusCounts = useMemo(() => {
    return feedbacks.reduce((acc, fb) => {
      acc[fb.status] = (acc[fb.status] || 0) + 1;
      return acc;
    }, {} as Record<string, number>);
  }, [feedbacks]);

  const fetchMe = async () => {
    setLoading(true);
    try {
      const githubRes = await fetch('/api/auth/github-admin/me', { credentials: 'include' });
      if (githubRes.ok) {
        const githubData = await githubRes.json();
        if (githubData.authenticated) {
          setAuth(githubData);
          setLoading(false);
          return;
        }
      }

      const res = await fetch('/api/auth/me', { credentials: 'include' });
      if (res.ok) {
        const data = await res.json();
        setAuth(data);
      }
    } catch (error) {
      console.error('Error fetching auth:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchFeedbacks = async () => {
    setLoadingFeedbacks(true);
    try {
      const res = await fetch('/api/admin/feedback', { credentials: 'include' });
      if (res.ok) {
        const data = await res.json();
        setFeedbacks(data.feedbacks || []);
      }
    } catch (error) {
      console.error('Error fetching feedbacks:', error);
      showToast('Error loading feedbacks', 'error');
    } finally {
      setLoadingFeedbacks(false);
    }
  };

  const fetchAdminUsers = async () => {
    setLoadingUsers(true);
    try {
      const res = await fetch('/api/admin/github-users', { credentials: 'include' });
      if (res.ok) {
        const data = await res.json();
        setAdminUsers(data.users || []);
        setIsSuperAdmin(data.users?.some((user: AdminUser) => user.username === 'HttpMarco') || false);
      }
    } catch (error) {
      console.error('Error fetching admin users:', error);
      showToast('Error loading admin users', 'error');
    } finally {
      setLoadingUsers(false);
    }
  };

  const fetchPartners = async () => {
    setLoadingPartners(true);
    try {
      const res = await fetch('/api/admin/partners', { credentials: 'include' });
      if (res.ok) {
        const data = await res.json();
        setPartners(data.partners || []);
      }
    } catch (error) {
      console.error('Error fetching partners:', error);
      showToast('Error loading partners', 'error');
    } finally {
      setLoadingPartners(false);
    }
  };

  const handleApproveFeedback = async (feedbackId: string) => {
    try {
      const res = await fetch('/api/admin/feedback/approve', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ feedbackId })
      });
      
      if (res.ok) {
        showToast('Feedback approved successfully', 'success');
        fetchFeedbacks();
      } else {
        const error = await res.json();
        showToast(`Error approving feedback: ${error.error}`, 'error');
      }
    } catch (error) {
      console.error('Error approving feedback:', error);
      showToast('Error approving feedback', 'error');
    }
  };

  const handleRejectFeedback = async (feedbackId: string) => {
    try {
      const res = await fetch('/api/admin/feedback/reject', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ feedbackId })
      });
      
      if (res.ok) {
        showToast('Feedback rejected successfully', 'success');
        fetchFeedbacks();
      } else {
        const error = await res.json();
        showToast(`Error rejecting feedback: ${error.error}`, 'error');
      }
    } catch (error) {
      console.error('Error rejecting feedback:', error);
      showToast('Error rejecting feedback', 'error');
    }
  };

  const handleAddUser = async () => {
    if (!newUserGitHubUsername.trim()) return;
    
    setAddingUser(true);
    try {
      const res = await fetch('/api/admin/github-users', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ username: newUserGitHubUsername })
      });
      
      if (res.ok) {
        showToast('User added successfully', 'success');
        setNewUserGitHubUsername('');
        fetchAdminUsers();
      } else {
        const error = await res.json();
        showToast(`Error adding user: ${error.error}`, 'error');
      }
    } catch (error) {
      console.error('Error adding user:', error);
      showToast('Error adding user', 'error');
    } finally {
      setAddingUser(false);
    }
  };

  const handleRemoveUser = async (username: string) => {
    try {
      const res = await fetch('/api/admin/github-users', {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ username })
      });
      
      if (res.ok) {
        showToast('User removed successfully', 'success');
        fetchAdminUsers();
      } else {
        const error = await res.json();
        showToast(`Error removing user: ${error.error}`, 'error');
      }
    } catch (error) {
      console.error('Error removing user:', error);
      showToast('Error removing user', 'error');
    }
  };

  const handleAddPartner = async () => {
    if (!newPartner.name.trim() || (!newPartner.logo.trim() && !newPartner.logoFile)) return;
    
    setAddingPartner(true);
    try {
      let logoUrl = newPartner.logo;

      if (newPartner.logoFile) {
        const formData = new FormData();
        formData.append('file', newPartner.logoFile);
        formData.append('partnerName', newPartner.name);
        
        const uploadRes = await fetch('/api/admin/upload', {
          method: 'POST',
          credentials: 'include',
          body: formData
        });
        
        if (!uploadRes.ok) {
          const error = await uploadRes.json();
          throw new Error(`Upload failed: ${error.error}`);
        }
        
        const uploadData = await uploadRes.json();
        logoUrl = uploadData.url;
      }

      const res = await fetch('/api/admin/partners', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({
          ...newPartner,
          logo: logoUrl
        })
      });
      
      if (res.ok) {
        showToast('Partner added successfully', 'success');
        setNewPartner({ name: '', logo: '', website: '', description: '' });
        fetchPartners();
      } else {
        const error = await res.json();
        showToast(`Error adding partner: ${error.error}`, 'error');
      }
    } catch (error) {
      console.error('Error adding partner:', error);
      const errorMessage = error instanceof Error ? error.message : 'Unknown error';
      showToast(`Error adding partner: ${errorMessage}`, 'error');
    } finally {
      setAddingPartner(false);
    }
  };

  const handleRemovePartner = async (partnerId: string) => {
    try {
      const res = await fetch('/api/admin/partners', {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ partnerId })
      });
      
      if (res.ok) {
        showToast('Partner removed successfully', 'success');
        fetchPartners();
      } else {
        const error = await res.json();
        showToast(`Error removing partner: ${error.error}`, 'error');
      }
    } catch (error) {
      console.error('Error removing partner:', error);
      showToast('Error removing partner', 'error');
    }
  };

  const handleEditPartner = (partner: Partner) => {
    setEditingPartner({
      id: partner.id,
      name: partner.name,
      logo: partner.logo,
      website: partner.website,
      description: partner.description
    });
    setEditingPartnerId(partner.id);
  };

  const handleUpdatePartner = async () => {
    if (!editingPartner || !editingPartnerId) return;
    
    setAddingPartner(true);
    try {
      let logoUrl = editingPartner.logo;

      if (editingPartner.logoFile) {
        const formData = new FormData();
        formData.append('file', editingPartner.logoFile);
        formData.append('partnerName', editingPartner.name);
        
        const uploadRes = await fetch('/api/admin/upload', {
          method: 'POST',
          credentials: 'include',
          body: formData
        });
        
        if (!uploadRes.ok) {
          const error = await uploadRes.json();
          throw new Error(`Upload failed: ${error.error}`);
        }
        
        const uploadData = await uploadRes.json();
        logoUrl = uploadData.url;
      }

      const res = await fetch('/api/admin/partners', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({
          id: editingPartnerId,
          name: editingPartner.name,
          logo: logoUrl,
          website: editingPartner.website,
          description: editingPartner.description
        })
      });
      
      if (res.ok) {
        showToast('Partner updated successfully', 'success');
        setEditingPartner(null);
        setEditingPartnerId(null);
        fetchPartners();
      } else {
        const error = await res.json();
        showToast(`Error updating partner: ${error.error}`, 'error');
      }
    } catch (error) {
      console.error('Error updating partner:', error);
      const errorMessage = error instanceof Error ? error.message : 'Unknown error';
      showToast(`Error updating partner: ${errorMessage}`, 'error');
    } finally {
      setAddingPartner(false);
    }
  };

  const handleCancelEditPartner = () => {
    setEditingPartner(null);
    setEditingPartnerId(null);
  };

  const fetchPlatforms = async () => {
    setLoadingPlatforms(true);
    try {
      const res = await fetch('/api/admin/platforms', { credentials: 'include' });
      if (res.ok) {
        const data = await res.json();
        setPlatforms(data.platforms || []);
      } else {
        console.error('Failed to fetch platforms');
      }
    } catch (error) {
      console.error('Error fetching platforms:', error);
    } finally {
      setLoadingPlatforms(false);
    }
  };

  const handleAddPlatform = async () => {
    if (!newPlatform.name.trim() || !newPlatform.iconFile) return;
    
    setAddingPlatform(true);
    try {
      let iconUrl = newPlatform.icon;

      if (newPlatform.iconFile) {
        const formData = new FormData();
        formData.append('file', newPlatform.iconFile);
        formData.append('platformName', newPlatform.name);
        
        const uploadRes = await fetch('/api/admin/upload', {
          method: 'POST',
          credentials: 'include',
          body: formData
        });
        
        if (!uploadRes.ok) {
          const error = await uploadRes.json();
          throw new Error(`Upload failed: ${error.error}`);
        }
        
        const uploadData = await uploadRes.json();
        iconUrl = uploadData.url;
      }

      const res = await fetch('/api/admin/platforms', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({
          ...newPlatform,
          icon: iconUrl
        })
      });
      
      if (res.ok) {
        showToast('Platform added successfully', 'success');
        setNewPlatform({
          name: '',
          icon: '',
          versions: {
            '1.7-1.12': 'not-supported',
            '1.12-1.16': 'not-supported',
            '1.18-1.19': 'not-supported',
            '1.20+': 'not-supported'
          },
          addons: {
            'Severmobs': 'not-supported',
            'Signs': 'not-supported'
          }
        });
        fetchPlatforms();
      } else {
        const error = await res.json();
        showToast(`Error adding platform: ${error.error}`, 'error');
      }
    } catch (error) {
      console.error('Error adding platform:', error);
      const errorMessage = error instanceof Error ? error.message : 'Unknown error';
      showToast(`Error adding platform: ${errorMessage}`, 'error');
    } finally {
      setAddingPlatform(false);
    }
  };

  const handleRemovePlatform = async (platformId: string) => {
    try {
      const res = await fetch('/api/admin/platforms', {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ platformId })
      });
      
      if (res.ok) {
        showToast('Platform removed successfully', 'success');
        fetchPlatforms();
      } else {
        const error = await res.json();
        showToast(`Error removing platform: ${error.error}`, 'error');
      }
    } catch (error) {
      console.error('Error removing platform:', error);
      showToast('Error removing platform', 'error');
    }
  };

  const handleEditPlatform = (platform: Platform) => {
    setEditingPlatform({
      id: platform.id,
      name: platform.name,
      icon: platform.icon,
      versions: { ...platform.versions },
      addons: { ...platform.addons }
    });
    setEditingPlatformId(platform.id);
  };

  const handleUpdatePlatform = async () => {
    if (!editingPlatform || !editingPlatformId) return;
    
    setAddingPlatform(true);
    try {
      let iconUrl = editingPlatform.icon;

      if (editingPlatform.iconFile) {
        const formData = new FormData();
        formData.append('file', editingPlatform.iconFile);
        formData.append('platformName', editingPlatform.name);
        
        const uploadRes = await fetch('/api/admin/upload', {
          method: 'POST',
          credentials: 'include',
          body: formData
        });
        
        if (!uploadRes.ok) {
          const error = await uploadRes.json();
          throw new Error(`Upload failed: ${error.error}`);
        }
        
        const uploadData = await uploadRes.json();
        iconUrl = uploadData.url;
      }

      const res = await fetch('/api/admin/platforms', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({
          id: editingPlatformId,
          name: editingPlatform.name,
          icon: iconUrl,
          versions: editingPlatform.versions,
          addons: editingPlatform.addons
        })
      });
      
      if (res.ok) {
        showToast('Platform updated successfully', 'success');
        setEditingPlatform(null);
        setEditingPlatformId(null);
        fetchPlatforms();
      } else {
        const error = await res.json();
        showToast(`Error updating platform: ${error.error}`, 'error');
      }
    } catch (error) {
      console.error('Error updating platform:', error);
      const errorMessage = error instanceof Error ? error.message : 'Unknown error';
      showToast(`Error updating platform: ${errorMessage}`, 'error');
    } finally {
      setAddingPlatform(false);
    }
  };

  const handleCancelEdit = () => {
    setEditingPlatform(null);
    setEditingPlatformId(null);
  };

  const handleLogout = () => {
    document.cookie = 'github_admin_auth=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
    document.cookie = 'admin_auth=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
    setAuth(null);
    setFeedbacks([]);
    setAdminUsers([]);
    setPartners([]);
    setPlatforms([]);
    setChangelog([]);
    setBlogPosts([]);
    setImages([]);
    setEditingPlatform(null);
    setEditingPlatformId(null);
    setEditingPartner(null);
    setEditingPartnerId(null);
  };

  const fetchChangelog = async () => {
    setLoadingChangelog(true);
    try {
      console.log('🔄 Fetching changelog from /api/public/changelog...');
      const res = await fetch('/api/public/changelog', { credentials: 'include' });
      console.log('Changelog API response status:', res.status);
      
      if (res.ok) {
        const data = await res.json();
        console.log('Changelog API response data:', data);
        console.log('Changelog count:', data.changelog?.length || 0);
        setChangelog(data.changelog || []);
      } else {
        console.error('Changelog API error:', res.status, res.statusText);
      }
    } catch (error) {
      console.error('Error fetching changelog:', error);
      showToast('Error loading changelog', 'error');
    } finally {
      setLoadingChangelog(false);
    }
  };

  const fetchBlogPosts = async () => {
    setLoadingBlog(true);
    try {
      const res = await fetch('/api/public/blog', { credentials: 'include' });
      if (res.ok) {
        const data = await res.json();
        setBlogPosts(data.posts || []);
      }
    } catch (error) {
      console.error('Error fetching blog posts:', error);
      showToast('Error loading blog posts', 'error');
    } finally {
      setLoadingBlog(false);
    }
  };

  const fetchImages = async () => {
    setLoadingImages(true);
    try {
      console.log('🔄 Fetching images...');
      const res = await fetch('/api/admin/images', { credentials: 'include' });
      if (res.ok) {
        const data = await res.json();
        console.log('Images fetched:', data.images?.length || 0, 'images');
        setImages(data.images || []);
      } else {
        console.error('Failed to fetch images:', res.status, res.statusText);
      }
    } catch (error) {
      console.error('Error fetching images:', error);
    } finally {
      setLoadingImages(false);
    }
  };

  useEffect(() => {
    fetchMe();

    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');
    const success = urlParams.get('success');
    
    if (error === 'unauthorized') {
      showToast('Access denied. You are not authorized to access the admin dashboard.', 'error');

      window.history.replaceState({}, document.title, window.location.pathname);
    } else if (error === 'oauth_failed') {
      showToast('GitHub OAuth failed. Please try again.', 'error');
      window.history.replaceState({}, document.title, window.location.pathname);
    } else if (error === 'oauth_error') {
      showToast('GitHub OAuth error occurred. Please try again.', 'error');
      window.history.replaceState({}, document.title, window.location.pathname);
    } else if (success === 'github_login') {
      showToast('Successfully logged in with GitHub!', 'success');
      window.history.replaceState({}, document.title, window.location.pathname);
    }
  }, []);

  useEffect(() => {
    if (auth?.authenticated) {
      fetchFeedbacks();
      fetchAdminUsers();
      fetchPartners();
      fetchPlatforms();
      fetchChangelog();
      fetchBlogPosts();
      fetchImages();
    }
  }, [auth?.authenticated]);

  useEffect(() => {
    if (auth?.authenticated) {
      const interval = setInterval(async () => {
        try {
          const res = await fetch('/api/auth/github-admin/me', { credentials: 'include' });
          if (!res.ok) {
            showToast('Session expired. Please log in again.', 'warning');
            handleLogout();
          }
        } catch (error) {
          console.error('Session validation error:', error);
        }
      }, 60000);

      return () => clearInterval(interval);
    }
  }, [auth?.authenticated]);

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-10 w-10 sm:h-12 sm:w-12 border-b-2 border-primary mx-auto mb-3 sm:mb-4"></div>
          <p className="text-sm sm:text-base text-muted-foreground">Loading...</p>
        </div>
      </div>
    );
  }

  if (!auth?.authenticated) {
    return <LoginForm />;
  }

  return (
    <div className="min-h-screen bg-background relative scroll-optimized">
      <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)] pointer-events-none"></div>
      
      <div className="container mx-auto py-6 sm:py-8 px-4 sm:px-6 relative z-10 dashboard-container">
        <AdminHeader 
          auth={auth} 
          onLogout={handleLogout} 
          onRefresh={fetchFeedbacks} 
        />

        <TabNavigation
          activeTab={activeTab}
          onTabChange={setActiveTab}
          feedbacksCount={feedbacks.length}
          adminUsersCount={adminUsers.length}
          partnersCount={partners.length}
          platformsCount={platforms.length}
          changelogCount={changelog.length}
          blogCount={blogPosts.length}
          imagesCount={images.length}
          isSuperAdmin={isSuperAdmin}
        />

        {activeTab === 'feedback' && (
          <FeedbackTab
            feedbacks={feedbacks}
            query={query}
            setQuery={setQuery}
            ratingFilter={ratingFilter}
            setRatingFilter={setRatingFilter}
            statusFilter={statusFilter}
            setStatusFilter={setStatusFilter}
            filteredFeedbacks={filteredFeedbacks}
            feedbackStatusCounts={feedbackStatusCounts}
            onApprove={handleApproveFeedback}
            onReject={handleRejectFeedback}
            onRefresh={fetchFeedbacks}
          />
        )}

        {activeTab === 'users' && isSuperAdmin && (
          <UsersTab
            adminUsers={adminUsers}
            loadingUsers={loadingUsers}
            newUserGitHubUsername={newUserGitHubUsername}
            setNewUserGitHubUsername={setNewUserGitHubUsername}
            addingUser={addingUser}
            onAddUser={handleAddUser}
            onRemoveUser={handleRemoveUser}
            onRefresh={fetchAdminUsers}
          />
        )}

        {activeTab === 'partners' && isSuperAdmin && (
          <PartnersTab
            partners={partners}
            loadingPartners={loadingPartners}
            newPartner={newPartner}
            setNewPartner={setNewPartner}
            addingPartner={addingPartner}
            onAddPartner={handleAddPartner}
            onRemovePartner={handleRemovePartner}
            onRefresh={fetchPartners}
            editingPartner={editingPartner}
            setEditingPartner={setEditingPartner}
            onEditPartner={handleEditPartner}
            onUpdatePartner={handleUpdatePartner}
            onCancelEditPartner={handleCancelEditPartner}
          />
        )}

        {activeTab === 'platforms' && isSuperAdmin && (
          <PlatformsTab
            platforms={platforms}
            loadingPlatforms={loadingPlatforms}
            newPlatform={newPlatform}
            setNewPlatform={setNewPlatform}
            addingPlatform={addingPlatform}
            onAddPlatform={handleAddPlatform}
            onRemovePlatform={handleRemovePlatform}
            onRefresh={fetchPlatforms}
            editingPlatform={editingPlatform}
            setEditingPlatform={setEditingPlatform}
            onEditPlatform={handleEditPlatform}
            onUpdatePlatform={handleUpdatePlatform}
            onCancelEdit={handleCancelEdit}
          />
        )}

        {activeTab === 'changelog' && (
          <ChangelogTab />
        )}

        {activeTab === 'blog' && (
          <BlogTab />
        )}

        {activeTab === 'images' && (
          <ImagesTab
            images={images}
            loadingImages={loadingImages}
            onRefresh={fetchImages}
          />
        )}
      </div>
      
      <ToastContainer />
    </div>
  );
}

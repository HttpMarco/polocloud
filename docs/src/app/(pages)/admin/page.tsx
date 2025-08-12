"use client";

import { useEffect, useMemo, useState } from 'react';
import { ToastContainer, showToast } from '@/components/ui/toast';
import { 
  AdminHeader, 
  TabNavigation, 
  FeedbackTab, 
  UsersTab, 
  PartnersTab, 
  LoginForm,
  MeResp, 
  Feedback, 
  AdminUser, 
  Partner, 
  NewPartner, 
  ActiveTab 
} from '@/components/admin';

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

  const [activeTab, setActiveTab] = useState<ActiveTab>('feedback');
  const [newPartner, setNewPartner] = useState<NewPartner>({
    name: '',
    logo: '',
    website: '',
    description: ''
  });
  const [addingPartner, setAddingPartner] = useState(false);

  const filteredFeedbacks = useMemo(() => {
    const filtered = feedbacks.filter(fb => {
      const q = query.toLowerCase().trim();
      const matchesQuery = !q || fb.username.toLowerCase().includes(q) || fb.description.toLowerCase().includes(q);
      const matchesRating = ratingFilter == null || fb.rating === ratingFilter;
      const matchesStatus = statusFilter === 'ALL' || fb.status === statusFilter;
      return matchesQuery && matchesRating && matchesStatus;
    });
    
    console.log('🔍 Filtered Feedbacks:', {
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
        setIsSuperAdmin(data.users?.some((user: AdminUser) => user.username === 'jakubbbdev') || false);
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
    if (!newPartner.name.trim() || !newPartner.logo.trim()) return;
    
    setAddingPartner(true);
    try {
      const res = await fetch('/api/admin/partners', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(newPartner)
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
      showToast('Error adding partner', 'error');
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

  const handleLogout = () => {
    document.cookie = 'github_admin_auth=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
    document.cookie = 'admin_auth=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
    setAuth(null);
    setFeedbacks([]);
    setAdminUsers([]);
    setPartners([]);
  };

  useEffect(() => {
    fetchMe();
  }, []);

  useEffect(() => {
    if (auth?.authenticated) {
      fetchFeedbacks();
      fetchAdminUsers();
      fetchPartners();
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
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4"></div>
          <p className="text-muted-foreground">Loading...</p>
        </div>
      </div>
    );
  }

  if (!auth?.authenticated) {
    return <LoginForm />;
  }

  return (
    <div className="min-h-screen bg-background">
      <div className="container mx-auto py-8">
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
          />
        )}
      </div>
      
      <ToastContainer />
    </div>
  );
}

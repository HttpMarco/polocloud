'use client'

import { useState, useEffect } from "react";
import { Sidebar, SidebarContent, SidebarFooter } from "@/components/ui/sidebar";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { toast } from "sonner";
import { 
  Settings, 
  Lock
} from "lucide-react";
import { API_ENDPOINTS } from "@/lib/api";
import { getUsernameFromCookie, clearCredentialsCookie } from "@/lib/auth-credentials";
import { useAuth } from '@/hooks/useAuth';
import { SidebarHeaderComponent } from "@/components/sidebar/sidebar-header";
import { CloudNavigation } from "@/components/sidebar/cloud-navigation";
import { TeamNavigation } from "@/components/sidebar/team-navigation";
import { SettingsDialog } from "@/components/sidebar/settings-dialog";
import { SidebarFooterComponent } from "@/components/sidebar/sidebar-footer";

interface UserData {
  username: string;
  userUUID: string;
  role: { id: number; label: string; hexColor: string } | null;
}

interface ChangePasswordData {
  currentPassword: string;
  newPassword: string;
  confirmPassword: string;
}



let userDataCache: UserData | null = null;

const resetUserDataCache = () => {
  userDataCache = null;
};

const loadUserData = async (): Promise<UserData> => {
  if (userDataCache) return userDataCache;
  
  try {
    const adminUsername = getUsernameFromCookie() || localStorage.getItem('adminUsername');
    const isLoggedIn = localStorage.getItem('isLoggedIn');


    if (adminUsername && isLoggedIn === 'true') {
      try {
        const userResponse = await fetch(API_ENDPOINTS.AUTH.ME);
        if (userResponse.ok) {
          const responseData = await userResponse.json();

          if (responseData.authenticated && responseData.user) {
            let userData = responseData.user;
            if (userData.role === null || userData.role === undefined) {

              try {

                let backendIp = localStorage.getItem('backend_ip');
                let token = localStorage.getItem('token');

                if (!backendIp || !token) {
                  const cookies = document.cookie.split(';');
                  for (const cookie of cookies) {
                    const [name, value] = cookie.trim().split('=');
                    if (name === 'backend_ip') backendIp = value;
                    if (name === 'token') token = value;
                  }
                }

                
                if (backendIp && token) {
                  const directUserResponse = await fetch(`http://${backendIp}/polocloud/api/v3/user/${userData.uuid}`, {
                    headers: {
                      'Cookie': `token=${token}`
                    }
                  });
                  
                  if (directUserResponse.ok) {
                    const directUserData = await directUserResponse.json();

                    if (directUserData.data) {
                      userData = directUserData.data;
                    }
                  } else {
                    console.log('Direct backend user endpoint failed:', directUserResponse.status, directUserResponse.statusText);
                  }
                } else {

                  if (adminUsername === 'admin') {
                    userData.role = -1;

                  } else {
                    userData.role = 0;

                  }
                }
              } catch (directError) {
                console.log('Error fetching direct backend user data:', directError);
                if (adminUsername === 'admin') {
                  userData.role = -1;

                } else {
                  userData.role = 0;

                }
              }
            }

            const username = userData.username || adminUsername;
            const userUUID = userData.uuid || 'admin-' + Date.now();

            let role = null;
            if (userData.role !== undefined && userData.role !== null) {
              try {

                const roleResponse = await fetch(API_ENDPOINTS.ROLE.GET(userData.role));
                if (roleResponse.ok) {
                  const roleData = await roleResponse.json();

                  role = {
                    id: roleData.id || userData.role,
                    label: roleData.label || roleData.name || 'Unknown',
                    hexColor: roleData.hexColor || roleData.color || '#6b7280'
                  };
                } else {
                  role = {
                    id: userData.role,
                    label: userData.role === 1 ? 'Team' : userData.role === 0 ? 'User' : `Role ${userData.role}`,
                    hexColor: userData.role === 1 ? '#3b82f6' : userData.role === 0 ? '#6b7280' : '#8b5cf6'
                  };
                }
              } catch (roleError) {
                console.log('Error loading specific role:', roleError);
                role = {
                  id: userData.role,
                  label: userData.role === 1 ? 'Team' : userData.role === 0 ? 'User' : `Role ${userData.role}`,
                  hexColor: userData.role === 1 ? '#3b82f6' : userData.role === 0 ? '#6b7280' : '#8b5cf6'
                };
                console.log('Using fallback role after error:', role);
              }
            } else {
              role = {
                id: -1,
                label: 'Admin',
                hexColor: '#dc2626'
              };
            }

              userDataCache = { username, userUUID, role };
            return userDataCache;
          } else {
            const role = {
              id: -1,
              label: 'Admin',
              hexColor: '#dc2626'
            };
            
            userDataCache = { 
              username: adminUsername, 
              userUUID: 'admin-' + Date.now(),
              role 
            };
              return userDataCache;
            }
        } else {
          const role = {
            id: -1,
            label: 'Admin',
            hexColor: '#dc2626'
          };
          
          userDataCache = { 
            username: adminUsername, 
            userUUID: 'admin-' + Date.now(),
            role 
          };
          return userDataCache;
        }
      } catch (error) {
        console.log('Error loading user data:', error);
      const role = {
        id: -1,
        label: 'Admin',
        hexColor: '#dc2626'
      };
      
      userDataCache = { 
        username: adminUsername, 
        userUUID: 'admin-' + Date.now(),
        role 
      };
      return userDataCache;
      }
    }

    userDataCache = { username: 'Guest', userUUID: '', role: null };
    return userDataCache;
    
  } catch (error) {
    console.log('Error in loadUserData:', error);
    userDataCache = { username: 'Guest', userUUID: '', role: null };
    return userDataCache;
  }
};





const ChangePasswordDialog = ({ 
  isOpen, 
  onClose, 
  onSubmit, 
  data, 
  onChange, 
  isLoading 
}: {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: () => void;
  data: ChangePasswordData;
  onChange: (field: keyof ChangePasswordData, value: string) => void;
  isLoading: boolean;
}) => (
  <Dialog open={isOpen} onOpenChange={onClose}>
    <DialogContent className="max-w-md bg-sidebar border border-sidebar-border">
      <DialogHeader>
        <DialogTitle className="text-xl font-semibold text-sidebar-foreground flex items-center space-x-2">
          <Lock className="w-5 h-5" />
          <span>Change Password</span>
        </DialogTitle>
      </DialogHeader>
      
      <div className="space-y-4 mt-6">
        <div className="space-y-2">
          <Label htmlFor="current-password" className="text-sm font-medium text-sidebar-foreground">
            Current Password
          </Label>
          <Input 
            id="current-password" 
            type="password" 
            placeholder="Enter current password" 
            className="bg-sidebar-accent border-sidebar-border"
            value={data.currentPassword}
            onChange={(e) => onChange('currentPassword', e.target.value)}
          />
        </div>
        
        <div className="space-y-2">
          <Label htmlFor="new-password" className="text-sm font-medium text-sidebar-foreground">
            New Password
          </Label>
          <Input 
            id="new-password" 
            type="password" 
            placeholder="Enter new password" 
            className="bg-sidebar-accent border-sidebar-border"
            value={data.newPassword}
            onChange={(e) => onChange('newPassword', e.target.value)}
          />
        </div>
        
        <div className="space-y-2">
          <Label htmlFor="confirm-password" className="text-sm font-medium text-sidebar-foreground">
            Confirm New Password
          </Label>
          <Input 
            id="confirm-password" 
            type="password" 
            placeholder="Confirm new password" 
            className="bg-sidebar-accent border-sidebar-border"
            value={data.confirmPassword}
            onChange={(e) => onChange('confirmPassword', e.target.value)}
          />
        </div>
        
        <div className="flex justify-end space-x-2">
          <Button 
            variant="outline" 
            onClick={onClose}
            className="border-sidebar-border text-sidebar-foreground hover:bg-sidebar-accent"
          >
            Cancel
          </Button>
          <Button 
            onClick={onSubmit}
            disabled={isLoading || !data.newPassword || !data.confirmPassword}
            className="bg-primary hover:bg-primary/90"
          >
            {isLoading ? 'Changing...' : 'Save Changes'}
          </Button>
        </div>
      </div>
    </DialogContent>
  </Dialog>
);






export function AppSidebar() {
  const [userData, setUserData] = useState<UserData | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isSettingsOpen, setIsSettingsOpen] = useState(false);
  const [isChangePasswordOpen, setIsChangePasswordOpen] = useState(false);
  const { logout } = useAuth();

  const [changePasswordData, setChangePasswordData] = useState<ChangePasswordData>({
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  });
  const [isChangingPassword, setIsChangingPassword] = useState(false);

  useEffect(() => {
    const loadDataAsync = async () => {
      try {
        resetUserDataCache();
        const data = await loadUserData();
        setUserData(data);
        setIsLoading(false);
      } catch (error) {
        console.log('Error loading user data:', error);
        const adminUsername = getUsernameFromCookie() || localStorage.getItem('adminUsername');
        const isLoggedIn = localStorage.getItem('isLoggedIn');
        
        if (adminUsername && isLoggedIn === 'true') {
          setUserData({
            username: adminUsername,
            userUUID: 'admin-' + Date.now(),
            role: {
              id: -1,
              label: 'Admin',
              hexColor: '#dc2626'
            }
          });
        } else {
          setUserData({ username: 'Guest', userUUID: '', role: null });
        }
        setIsLoading(false);
      }
    };

    loadDataAsync();

    const interval = setInterval(async () => {
      try {
        const response = await fetch(API_ENDPOINTS.AUTH.ME);
        if (!response.ok) {
          resetUserDataCache();

          const data = await loadUserData();
          setUserData(data);
        }
      } catch (error) {
        console.log('Error in interval check:', error);
      }
    }, 5 * 60 * 1000);

    const handleUserLogin = () => {

      resetUserDataCache();

      loadDataAsync();
    };

    window.addEventListener('user-logged-in', handleUserLogin as EventListener);

    return () => {
      clearInterval(interval);
      window.removeEventListener('user-logged-in', handleUserLogin as EventListener);
    };
  }, []);

  useEffect(() => {
    if (isSettingsOpen) {
      loadSessions();
    }
  }, [isSettingsOpen]);

  const loadSessions = async () => {
    try {
      const response = await fetch(API_ENDPOINTS.USER.TOKENS);
      if (response.ok) {

      } else {

      }
    } catch  {}
  };

  const handleLogout = async () => {
    try {
      const response = await fetch(API_ENDPOINTS.AUTH.LOGOUT, { method: 'POST' });
      if (response.ok) {
        resetUserDataCache();
        setUserData({ username: 'Guest', userUUID: '', role: null });
        clearCredentialsCookie();
        logout();
      }
    } catch {
      clearCredentialsCookie();
      logout();
    }
  };

  const handleChangePassword = async () => {
    if (changePasswordData.newPassword !== changePasswordData.confirmPassword) {
      toast.error('New passwords do not match');
      return;
    }

    if (changePasswordData.newPassword.length < 6) {
      toast.error('New password must be at least 6 characters long');
      return;
    }

    setIsChangingPassword(true);
    try {
      const response = await fetch(API_ENDPOINTS.USER.CHANGE_PASSWORD, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ newPassword: changePasswordData.newPassword })
      });

      if (response.ok) {
        toast.success('Password changed successfully');
        setIsChangePasswordOpen(false);
        setChangePasswordData({ currentPassword: '', newPassword: '', confirmPassword: '' });
      } else {
        const errorData = await response.json();
        toast.error(errorData.error || 'Failed to change password');
      }
    } catch {
      toast.error('Failed to change password');
    } finally {
      setIsChangingPassword(false);
    }
  };


  const handlePasswordDataChange = (field: keyof ChangePasswordData, value: string) => {
    setChangePasswordData(prev => ({ ...prev, [field]: value }));
  };

  if (isLoading || !userData) {
    return (
      <Sidebar className="border-r border-sidebar-border bg-sidebar">
        <SidebarHeaderComponent />
        <SidebarContent className="px-2 py-3">
          <CloudNavigation />
          <TeamNavigation />
        </SidebarContent>
        <SidebarFooter className="border-t border-sidebar-border p-3">
          <div className="flex items-center space-x-3">
            <div className="w-8 h-8 rounded-lg overflow-hidden">
              <div className="w-8 h-8 bg-sidebar-accent animate-pulse rounded-lg"></div>
            </div>
            <div className="flex-1 min-w-0">
              <div className="w-16 h-4 bg-sidebar-accent animate-pulse rounded"></div>
              <div className="w-12 h-3 bg-sidebar-accent animate-pulse rounded mt-1"></div>
            </div>
            <div className="flex-shrink-0">
              <Settings className="w-4 h-4 text-sidebar-foreground/60" />
            </div>
          </div>
        </SidebarFooter>
      </Sidebar>
    );
  }

  return (
    <>
      <Sidebar className="border-r border-sidebar-border bg-sidebar">
        <SidebarHeaderComponent />
        <SidebarContent className="px-2 py-3">
          <CloudNavigation />
          <TeamNavigation />
        </SidebarContent>
        <SidebarFooterComponent 
          userData={userData}
          onSettingsClick={() => setIsSettingsOpen(true)}
          onLogout={handleLogout}
        />
      </Sidebar>

      <SettingsDialog 
        isOpen={isSettingsOpen}
        onClose={() => setIsSettingsOpen(false)}
        userData={userData}
      />

      <ChangePasswordDialog 
        isOpen={isChangePasswordOpen}
        onClose={() => setIsChangePasswordOpen(false)}
        onSubmit={handleChangePassword}
        data={changePasswordData}
        onChange={handlePasswordDataChange}
        isLoading={isChangingPassword}
      />
    </>
  );
}

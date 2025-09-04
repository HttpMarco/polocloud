'use client'

import { useState, useEffect } from "react";
import { Sidebar, SidebarContent, SidebarFooter } from "@/components/ui/sidebar";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useRouter } from "next/navigation";
import { toast } from "sonner";
import { 
  Settings, 
  Lock
} from "lucide-react";
import { API_ENDPOINTS } from "@/lib/api";
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

    const adminUsername = localStorage.getItem('adminUsername');
    const isLoggedIn = localStorage.getItem('isLoggedIn');
    
    if (adminUsername && isLoggedIn === 'true') {

      try {
        const userResponse = await fetch(API_ENDPOINTS.AUTH.ME);
        if (userResponse.ok) {
          const responseData = await userResponse.json();

          if (responseData.authenticated && responseData.user) {
            const username = responseData.user.username || adminUsername;
            const userUUID = responseData.user.uuid || 'admin-' + Date.now();

            let role = null;
            if (responseData.user.role !== undefined && responseData.user.role !== null) {
              try {
                const roleResponse = await fetch(API_ENDPOINTS.ROLE.GET(responseData.user.role));
                if (roleResponse.ok) {
                  const roleData = await roleResponse.json();
                  role = roleData;
                }
              } catch {}
            }

            if (role) {
              userDataCache = { username, userUUID, role };
              return userDataCache;
            }
          }
        }
      } catch  {}
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

    userDataCache = { username: 'Guest', userUUID: '', role: null };
    return userDataCache;
    
  } catch {
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

  const [changePasswordData, setChangePasswordData] = useState<ChangePasswordData>({
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  });
  const [isChangingPassword, setIsChangingPassword] = useState(false);
  const router = useRouter();

  useEffect(() => {

    const loadDataImmediately = () => {
      try {
        const adminUsername = localStorage.getItem('adminUsername');
        const isLoggedIn = localStorage.getItem('isLoggedIn');
        
        if (adminUsername && isLoggedIn === 'true') {

          const immediateData = {
            username: adminUsername,
            userUUID: 'admin-' + Date.now(),
            role: {
              id: -1,
              label: 'Admin',
              hexColor: '#dc2626'
            }
          };
          setUserData(immediateData);
          setIsLoading(false);
        } else {
          setUserData({ username: 'Guest', userUUID: '', role: null });
          setIsLoading(false);
        }
      } catch {
        setUserData({ username: 'Guest', userUUID: '', role: null });
        setIsLoading(false);
      }
    };

    loadDataImmediately();

    const loadDataAsync = async () => {
      try {
        const data = await loadUserData();
        setUserData(data);
      } catch {}
    };

    setTimeout(loadDataAsync, 100);

    const interval = setInterval(async () => {
      try {
        const response = await fetch(API_ENDPOINTS.AUTH.ME);
        if (!response.ok) {
          resetUserDataCache();

        }
      } catch  {}
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
        router.push('/login');
      }
    } catch {}
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

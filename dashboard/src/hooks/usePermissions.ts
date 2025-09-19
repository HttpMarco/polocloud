'use client'

import { useState, useEffect, useCallback } from 'react';

interface UserData {
  username: string;
  userUUID: string;
  role: {
    id: number;
    label: string;
    hexColor: string;
    permissions?: string[];
  } | null;
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
        const userResponse = await fetch('/api/auth/me');
        if (userResponse.ok) {
          const responseData = await userResponse.json();

          if (responseData.authenticated && responseData.user) {
            const username = responseData.user.username || adminUsername;
            const userUUID = responseData.user.uuid || 'admin-' + Date.now();

            let role = null;
            if (responseData.user.role !== undefined && responseData.user.role !== null) {
              try {
                const roleResponse = await fetch(`/api/role/${responseData.user.role}`);
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
      } catch {}

      if (adminUsername === 'admin') {
        const role = {
          id: -1,
          label: 'Admin',
          hexColor: '#dc2626',
          permissions: ['*']
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
    
  } catch {
    userDataCache = { username: 'Guest', userUUID: '', role: null };
    return userDataCache;
  }
};

export function usePermissions() {
  const [userData, setUserData] = useState<UserData | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  const loadData = useCallback(async () => {
    try {
      const data = await loadUserData();
      setUserData(data);
    } catch {
      setUserData({ username: 'Guest', userUUID: '', role: null });
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    loadData();

    const interval = setInterval(async () => {
      try {
        const response = await fetch('/api/auth/me');
        if (!response.ok) {
          resetUserDataCache();
          loadData();
        }
      } catch {
        loadData();
      }
    }, 5 * 60 * 1000);

    return () => clearInterval(interval);
  }, [loadData]);

  const hasPermission = useCallback((permission: string): boolean => {
    if (!userData?.role?.permissions) return false;

    if (userData.role.permissions.includes('*')) return true;

    return userData.role.permissions.includes(permission);
  }, [userData]);

  const hasAnyPermission = useCallback((permissions: string[]): boolean => {
    if (!userData?.role?.permissions) return false;

    if (userData.role.permissions.includes('*')) return true;

    return permissions.some(permission => userData.role?.permissions?.includes(permission));
  }, [userData]);

  const hasAllPermissions = useCallback((permissions: string[]): boolean => {
    if (!userData?.role?.permissions) return false;

    if (userData.role.permissions.includes('*')) return true;

    return permissions.every(permission => userData.role?.permissions?.includes(permission));
  }, [userData]);

  return {
    userData,
    isLoading,
    hasPermission,
    hasAnyPermission,
    hasAllPermissions,
    refreshUserData: loadData
  };
}

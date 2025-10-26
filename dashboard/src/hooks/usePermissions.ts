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

const createAdminRole = () => ({
  id: -1,
  label: 'Admin',
  hexColor: '#dc2626',
  permissions: ['*']
});

const createAdminUserData = (username: string) => ({
  username,
  userUUID: 'admin-' + Date.now(),
  role: createAdminRole()
});

const createGuestUserData = () => ({
  username: 'Guest',
  userUUID: '',
  role: null
});

const loadUserData = async (): Promise<UserData> => {
  if (userDataCache) return userDataCache;
  
  try {
    let adminUsername = localStorage.getItem('adminUsername');
    const isLoggedIn = localStorage.getItem('isLoggedIn');

    if (isLoggedIn === 'true' && !adminUsername) {
      const cookies = document.cookie.split(';');
      for (const cookie of cookies) {
        const [name, value] = cookie.trim().split('=');
        if (name === 'adminUsername') {
          adminUsername = value;
          break;
        }
      }

      if (!adminUsername) {
        try {
          const userResponse = await fetch('/api/auth/me');
          if (userResponse.ok) {
            const responseData = await userResponse.json();
            if (responseData.authenticated && responseData.user) {
              const role = responseData.user.role;
              if (role === -1 || (role && role.id === -1) || (role && role.label === 'Admin') || (role && Array.isArray(role.permissions) && role.permissions.includes('*'))) {
                adminUsername = responseData.user.username || 'admin';
              }
            }
          }
        } catch {}
      }
    }

    if (adminUsername && isLoggedIn === 'true') {
      try {
        const userResponse = await fetch('/api/auth/me');
        if (userResponse.ok) {
          const responseData = await userResponse.json();

          if (responseData.authenticated && responseData.user) {
            const username = responseData.user.username || adminUsername;
            const userUUID = responseData.user.uuid || 'admin-' + Date.now();
            let role = null;
            const incomingRole = responseData.user.role;
            if (incomingRole !== undefined && incomingRole !== null) {
              if (typeof incomingRole === 'object' && (Array.isArray(incomingRole.permissions) || incomingRole.permissions === undefined)) {
                role = incomingRole;
              } else {
                try {
                  const roleId = String(incomingRole);
                  const roleResponse = await fetch(`/api/role/${roleId}`);
                  if (roleResponse.ok) {
                    const roleData = await roleResponse.json();
                    role = roleData;
                  }
                } catch {}
              }
            }

            if (role) {
              userDataCache = { username, userUUID, role };
              return userDataCache;
            }
          }
        }
      } catch {}
      if (adminUsername === 'admin') {
        userDataCache = createAdminUserData(adminUsername);
        return userDataCache;
      }
    }

    if (isLoggedIn === 'true') {
      try {
        const userResponse = await fetch('/api/auth/me');
        if (userResponse.ok) {
          const responseData = await userResponse.json();
          if (responseData.authenticated && responseData.user) {
            const role = responseData.user.role;
            if (role === -1 || (role && role.id === -1) || (role && role.label === 'Admin') || (role && Array.isArray(role.permissions) && role.permissions.includes('*'))) {
              userDataCache = createAdminUserData(responseData.user.username || 'admin');
              return userDataCache;
            }
          }
        }
      } catch {}
    }

    userDataCache = createGuestUserData();
    return userDataCache;

  } catch {
    const adminUsername = localStorage.getItem('adminUsername');
    if (adminUsername === 'admin') {
      userDataCache = createAdminUserData(adminUsername);
      return userDataCache;
    }

    userDataCache = createGuestUserData();
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
      setUserData(createGuestUserData());
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

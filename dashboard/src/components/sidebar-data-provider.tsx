'use client';

import { createContext, useContext, useEffect, useState, ReactNode, useCallback } from 'react';
import { usePathname } from 'next/navigation';
import { Group } from '@/types/groups';
import { Service } from '@/types/services';
import { useWebSocketSystem } from '@/hooks/useWebSocketSystem';

interface SidebarDataContextType {
  groups: Group[];
  services: Service[];
  isLoading: boolean;
  refreshData: () => void;
  updateServiceState: (serviceName: string, state: string, additionalData?: Record<string, unknown>) => void;
}

const SidebarDataContext = createContext<SidebarDataContextType | undefined>(undefined);

export function SidebarDataProvider({ children }: { children: ReactNode }) {
  const pathname = usePathname();
  const isOnboardingPage = pathname === '/onboarding';
  const isLoginPage = pathname === '/login';
  const shouldHideSidebar = isOnboardingPage || isLoginPage;

  const [sidebarData, setSidebarData] = useState<{
    groups: Group[];
    services: Service[];
    isLoading: boolean;
  }>({
    groups: [],
    services: [],
    isLoading: true
  });

  const [isClient, setIsClient] = useState(false);

  const [backendCredentials, setBackendCredentials] = useState<{backendIp: string | null, token: string | null}>({
    backendIp: null,
    token: null
  });

  useEffect(() => {
    if (isClient) {
      const backendIp = localStorage.getItem('backendIp');

      const fetchToken = async () => {
        try {
          const response = await fetch('/api/auth/token');
          if (response.ok) {
            const data = await response.json();
            if (data.success && data.token) {
              setBackendCredentials({ backendIp, token: data.token });
              return;
            }
          }
        } catch {
        }

        let token = localStorage.getItem('token');
        if (!token) {
          const cookies = document.cookie.split(';');
          for (const cookie of cookies) {
            const [name, value] = cookie.trim().split('=');
            if (name === 'token') {
              token = value;
              break;
            }
          }
        }

        if (!token) {
          const tokenMatch = document.cookie.match(/token=([^;]+)/);
          if (tokenMatch) {
            token = tokenMatch[1];
          }
        }
        
        setBackendCredentials({ backendIp, token: token || null });
      };
      
      fetchToken();
    }
  }, [isClient]);

  const shouldConnect = !shouldHideSidebar && isClient && !!backendCredentials.backendIp && !!backendCredentials.token;
  
  const { connect } = useWebSocketSystem({
    backendIp: backendCredentials.backendIp || undefined,
    token: backendCredentials.token || undefined,
    path: '/services/update',
    autoConnect: false,
    onConnect: () => {
      window.dispatchEvent(new CustomEvent('websocketConnect'));
    },
    onDisconnect: () => {
      window.dispatchEvent(new CustomEvent('websocketDisconnect'));
    },
    onError: (error) => {
      window.dispatchEvent(new CustomEvent('websocketError', {
        detail: { message: error.message }
      }));
    },
    onMessage: (message) => {
      try {
        let updateData;
        if (typeof message.data === 'string') {
          try {
            updateData = JSON.parse(message.data);
          } catch {
            return;
          }
        } else if (message.data && typeof message.data === 'object') {
          updateData = message.data;
        } else {
          updateData = message;
        }
        
        if (updateData && updateData.serviceName && updateData.state) {
          updateServiceState(updateData.serviceName, updateData.state, updateData);

          window.dispatchEvent(new CustomEvent('serviceStateUpdate', {
            detail: { serviceName: updateData.serviceName, state: updateData.state, updateData }
          }));
        }
      } catch {
      }
    }
  });

  useEffect(() => {
    if (shouldConnect) {
      connect().catch(() => {
      });
    }
  }, [shouldConnect, connect]);

  const loadSidebarData = useCallback(async () => {
    try {
      const [groupsResponse, servicesResponse] = await Promise.all([
        fetch('/api/groups/list'),
        fetch('/api/services/list')
      ]);

      const groups = groupsResponse.ok ? await groupsResponse.json() : [];
      const services = servicesResponse.ok ? await servicesResponse.json() : [];

      const newData = {
        groups: Array.isArray(groups) ? groups : [],
        services: Array.isArray(services) ? services : [],
        isLoading: false
      };

      setSidebarData(newData);

      if (isClient) {
        localStorage.setItem('sidebarGroups', JSON.stringify(newData.groups));
        localStorage.setItem('sidebarServices', JSON.stringify(newData.services));
      }
    } catch {
      setSidebarData(prev => ({ ...prev, isLoading: false }));
    }
  }, [isClient]);

  useEffect(() => {
    setIsClient(true);

    try {
      const savedGroups = localStorage.getItem('sidebarGroups');
      const savedServices = localStorage.getItem('sidebarServices');
      
      if (savedGroups && savedServices) {
        setSidebarData({
          groups: JSON.parse(savedGroups),
          services: JSON.parse(savedServices),
          isLoading: false
        });
      } else {
        loadSidebarData();
      }
    } catch {
      loadSidebarData();
    }
  }, [loadSidebarData]);

  useEffect(() => {
    if (!shouldHideSidebar && isClient) {
      const backendIp = localStorage.getItem('backendIp');
      if (backendIp) {
        const hasBackendIpCookie = document.cookie.includes('backend_ip=');
        if (!hasBackendIpCookie) {
          document.cookie = `backend_ip=${backendIp}; path=/; max-age=${7 * 24 * 60 * 60}; secure; samesite=lax`;
        }
        loadSidebarData();
      }
    }
  }, [shouldHideSidebar, isClient, loadSidebarData]);



  const updateServiceState = useCallback((serviceName: string, state: string, additionalData?: Record<string, unknown>) => {
    setSidebarData(prev => ({
      ...prev,
      services: prev.services.map(service => 
        service.name === serviceName 
          ? { 
              ...service, 
              state: state,
              ...(state === 'STARTING' || state === 'PREPARING' ? {
                playerCount: -1,
                maxPlayerCount: -1,
                cpuUsage: -1,
                memoryUsage: -1,
                maxMemory: -1
              } : {}),
              ...(state === 'STOPPING' || state === 'STOPPED' ? {
                playerCount: 0,
                maxPlayerCount: 0,
                cpuUsage: 0,
                memoryUsage: 0,
                maxMemory: 0
              } : {}),
              ...(additionalData || {})
            }
          : service
      )
    }));
  }, []);

  const refreshData = useCallback(async () => {
    try {
      const [groupsResponse, servicesResponse] = await Promise.all([
        fetch('/api/groups/list'),
        fetch('/api/services/list')
      ]);

      const groups = groupsResponse.ok ? await groupsResponse.json() : [];
      const services = servicesResponse.ok ? await servicesResponse.json() : [];

      const newData = {
        groups: Array.isArray(groups) ? groups : [],
        services: Array.isArray(services) ? services : [],
        isLoading: false
      };

      setSidebarData(newData);

      if (isClient) {
        localStorage.setItem('sidebarGroups', JSON.stringify(newData.groups));
        localStorage.setItem('sidebarServices', JSON.stringify(newData.services));
      }
    } catch (error) {
      console.warn('Sidebar error in sidebar-data-provider:', error);
    }
  }, [isClient]);

  if (shouldHideSidebar) {
    return <>{children}</>;
  }

  return (
    <SidebarDataContext.Provider value={{
      groups: sidebarData.groups,
      services: sidebarData.services,
      isLoading: sidebarData.isLoading,
      refreshData,
      updateServiceState
    }}>
      {children}
    </SidebarDataContext.Provider>
  );
}

export function useSidebarData() {
  const context = useContext(SidebarDataContext);
  if (context === undefined) {
    throw new Error('useSidebarData must be used within a SidebarDataProvider');
  }
  return context;
}

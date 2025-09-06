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

  // Get backend credentials
  const [backendCredentials, setBackendCredentials] = useState<{backendIp: string | null, token: string | null}>({
    backendIp: null,
    token: null
  });

  useEffect(() => {
    if (isClient) {
      const backendIp = localStorage.getItem('backendIp');
      
      // Try to get token from API instead of cookies
      const fetchToken = async () => {
        try {
          const response = await fetch('/api/auth/token');
          if (response.ok) {
            const data = await response.json();
            if (data.success && data.token) {
              console.log('SidebarDataProvider: Token fetched from API', { 
                backendIp, 
                token: 'present',
                tokenValue: data.token.substring(0, 20) + '...'
              });
              setBackendCredentials({ backendIp, token: data.token });
              return;
            }
          }
        } catch (error) {
          console.log('SidebarDataProvider: Failed to fetch token from API', error);
        }
        
        // Fallback to cookie parsing
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
        
        // Alternative parsing method if the above doesn't work
        if (!token) {
          const tokenMatch = document.cookie.match(/token=([^;]+)/);
          if (tokenMatch) {
            token = tokenMatch[1];
          }
        }
        
        console.log('SidebarDataProvider: Loading credentials from cookies', { 
          backendIp, 
          token: token ? 'present' : 'missing',
          allCookies: document.cookie,
          cookieArray: document.cookie.split(';'),
          tokenFromCookie: document.cookie.split(';').find(c => c.trim().startsWith('token='))
        });
        setBackendCredentials({ backendIp, token: token || null });
      };
      
      fetchToken();
    }
  }, [isClient]);

  // Shared WebSocket connection for service updates - manual connection like services page
  const shouldConnect = !shouldHideSidebar && isClient && !!backendCredentials.backendIp && !!backendCredentials.token;
  
  const { connect, disconnect, isConnected } = useWebSocketSystem({
    backendIp: backendCredentials.backendIp || undefined,
    token: backendCredentials.token || undefined,
    path: '/services/update',
    autoConnect: false, // Don't auto-connect, we'll do it manually
    onConnect: () => {
      console.log('SidebarDataProvider: WebSocket connected');
      // Dispatch connect event for debug info
      window.dispatchEvent(new CustomEvent('websocketConnect'));
    },
    onDisconnect: () => {
      console.log('SidebarDataProvider: WebSocket disconnected');
      // Dispatch disconnect event for debug info
      window.dispatchEvent(new CustomEvent('websocketDisconnect'));
    },
    onError: (error) => {
      console.log('SidebarDataProvider: WebSocket error:', error);
      // Dispatch error event for debug info
      window.dispatchEvent(new CustomEvent('websocketError', {
        detail: { message: error.message }
      }));
    },
    onMessage: (message) => {
      console.log('SidebarDataProvider: WebSocket message received:', message);
      try {
        let updateData;
        if (typeof message.data === 'string') {
          try {
            updateData = JSON.parse(message.data);
          } catch (parseError) {
            console.error('Failed to parse service update:', parseError);
            return;
          }
        } else if (message.data && typeof message.data === 'object') {
          updateData = message.data;
        } else {
          updateData = message;
        }
        
        if (updateData && updateData.serviceName && updateData.state) {
          updateServiceState(updateData.serviceName, updateData.state, updateData);
          
          // Dispatch custom event for other components to listen to
          window.dispatchEvent(new CustomEvent('serviceStateUpdate', {
            detail: { serviceName: updateData.serviceName, state: updateData.state, updateData }
          }));
        }
      } catch (error) {
        console.warn('Error processing service update:', error);
      }
    }
  });

  // Manually connect when credentials are ready
  useEffect(() => {
    if (shouldConnect) {
      console.log('SidebarDataProvider: Attempting manual WebSocket connection', {
        backendIp: backendCredentials.backendIp,
        token: backendCredentials.token ? 'present' : 'missing',
        shouldHideSidebar,
        isClient
      });
      connect().catch(error => {
        console.log('SidebarDataProvider: Manual connection failed:', error);
      });
    }
  }, [shouldConnect, connect, backendCredentials.backendIp, backendCredentials.token, shouldHideSidebar, isClient]);

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
              // Apply additional state-specific updates
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
              // Apply any additional data from the update
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

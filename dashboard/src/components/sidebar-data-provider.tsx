'use client';

import { createContext, useContext, useEffect, useState, ReactNode, useCallback } from 'react';
import { usePathname } from 'next/navigation';
import { Group } from '@/types/groups';
import { Service } from '@/types/services';

interface SidebarDataContextType {
  groups: Group[];
  services: Service[];
  isLoading: boolean;
  refreshData: () => void;
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



  const refreshData = async () => {
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
      }};

  if (shouldHideSidebar) {
    return <>{children}</>;
  }

  return (
    <SidebarDataContext.Provider value={{
      groups: sidebarData.groups,
      services: sidebarData.services,
      isLoading: sidebarData.isLoading,
      refreshData
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

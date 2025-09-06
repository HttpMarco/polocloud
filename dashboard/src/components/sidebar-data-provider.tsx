'use client';

import { createContext, useContext, useEffect, useState, ReactNode, useCallback } from 'react';
import { usePathname } from 'next/navigation';
import { Group } from '@/types/groups';
import { Service } from '@/types/services';
import { useServices } from '@/contexts/ServicesContext';

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

  // Use services from the global ServicesContext
  const { services, isLoading: servicesLoading } = useServices();

  const [sidebarData, setSidebarData] = useState<{
    groups: Group[];
    isLoading: boolean;
  }>({
    groups: [],
    isLoading: true
  });

  const [isClient, setIsClient] = useState(false);

  const loadSidebarData = useCallback(async () => {
    try {
      const groupsResponse = await fetch('/api/groups/list');
      const groups = groupsResponse.ok ? await groupsResponse.json() : [];

      const newData = {
        groups: Array.isArray(groups) ? groups : [],
        isLoading: false
      };

      setSidebarData(newData);

      if (isClient) {
        localStorage.setItem('sidebarGroups', JSON.stringify(newData.groups));
      }
    } catch {
      setSidebarData(prev => ({ ...prev, isLoading: false }));
    }
  }, [isClient]);

  useEffect(() => {
    setIsClient(true);

    try {
      const savedGroups = localStorage.getItem('sidebarGroups');
      
      if (savedGroups) {
        setSidebarData({
          groups: JSON.parse(savedGroups),
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
      const groupsResponse = await fetch('/api/groups/list');
      const groups = groupsResponse.ok ? await groupsResponse.json() : [];

      const newData = {
        groups: Array.isArray(groups) ? groups : [],
        isLoading: false
      };

      setSidebarData(newData);

      if (isClient) {
        localStorage.setItem('sidebarGroups', JSON.stringify(newData.groups));
      }
    } catch (error) {
      console.warn('Sidebar error in sidebar-data-provider:', error);
    }
  };

  if (shouldHideSidebar) {
    return <>{children}</>;
  }

  return (
    <SidebarDataContext.Provider value={{
      groups: sidebarData.groups,
      services: services, // Use services from ServicesContext
      isLoading: sidebarData.isLoading || servicesLoading,
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

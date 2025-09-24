'use client';

import { usePathname } from 'next/navigation';
import { SidebarProvider } from "@/components/ui/sidebar";
import ScrollIndicator from "@/components/scroll-indicator";
import { SidebarDataProvider } from '@/components/sidebar-data-provider';
import { AppSidebar } from '@/components/app-sidebar';
import { useAuth } from '@/hooks/useAuth';
import { useEffect } from 'react';

interface LayoutWrapperProps {
  children: React.ReactNode;
}

export function LayoutWrapper({ children }: LayoutWrapperProps) {
  const pathname = usePathname();
  const isAuthPage = pathname === '/login' || pathname === '/onboarding';
  const { isAuthenticated, isLoading } = useAuth();

  useEffect(() => {
    const isLoggedInFromStorage = localStorage.getItem('isLoggedIn') === 'true';
    
    if (!isLoading && !isAuthenticated && !isAuthPage && !isLoggedInFromStorage) {
      window.location.href = '/login';
    }
  }, [isAuthenticated, isLoading, isAuthPage]);

  if (isLoading && !isAuthPage) {
    return (
      <div className="flex items-center justify-center h-screen bg-background">
        <div className="flex flex-col items-center space-y-4">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
          <p className="text-muted-foreground">Authentifizierung wird überprüft...</p>
        </div>
      </div>
    );
  }

  if (isAuthPage) {
    return <>{children}</>;
  }

  if (!isAuthenticated) {
    return null;
  }
  
  return (
    <SidebarDataProvider>
      <ScrollIndicator />
      <SidebarProvider>
        <div className="flex h-screen w-full">
          <AppSidebar />
          <main className="flex-1 min-w-0 w-full h-full overflow-auto modern-scrollbar">
            {children}
          </main>
        </div>
      </SidebarProvider>
    </SidebarDataProvider>
  );
}

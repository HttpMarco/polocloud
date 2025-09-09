'use client';

import { usePathname } from 'next/navigation';
import { SidebarProvider } from "@/components/ui/sidebar";
import ScrollIndicator from "@/components/scroll-indicator";
import { SidebarDataProvider } from '@/components/sidebar-data-provider';
import { AppSidebar } from '@/components/app-sidebar';

interface LayoutWrapperProps {
  children: React.ReactNode;
}

export function LayoutWrapper({ children }: LayoutWrapperProps) {
  const pathname = usePathname();
  const isAuthPage = pathname === '/login' || pathname === '/onboarding';
  
  if (isAuthPage) {
    return <>{children}</>;
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

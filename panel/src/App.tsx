import { AppSidebar } from '@/components/app-sidebar';
import AppBreadcrumb from '@/components/system/breadcrumb/AppBreadcrumb';
import LoadingSpinner from '@/components/system/LoadingSpinner';

import { Separator } from '@/components/ui/separator';
import {
  SidebarInset,
  SidebarProvider,
  SidebarTrigger,
} from '@/components/ui/sidebar';
import { Toaster } from '@/components/ui/sonner';
import { InstanceProvider } from '@/lib/providers/InstanceProvider';
import { lazy, Suspense } from 'react';
import { Route, Switch } from 'wouter';

const Dashboard = lazy(() => import('@/layout/dashboard/DashboardRoute'));
const CreateGroup = lazy(() => import('@/layout/groups/create/CreateGroupLayout'));
const Groups = lazy(() => import('@/layout/groups/GroupLayout'));
const ConnectInstance = lazy(() => import('@/layout/connect/ConnectInstancePage'));
const GroupDetail = lazy(() => import('@/layout/groups/detail/GroupDetailLayout'));
const ServiceDetail = lazy(
  () => import('@/layout/service/detail/ServiceDetailLayout')
);

function App() {
  return (
    <InstanceProvider>
      <div className="min-h-screen bg-background w-full flex flex-col relative">
        <SidebarProvider>
          <AppSidebar />
          <SidebarInset>
            <header className="flex h-16 shrink-0 items-center gap-2 transition-[width,height] ease-linear group-has-[[data-collapsible=icon]]/sidebar-wrapper:h-12 top-0 sticky z-30 bg-background border-b rounded-t-xl">
              <div className="flex items-center gap-2 px-4">
                <SidebarTrigger className="-ml-1" />
                <Separator orientation="vertical" className="mr-2 h-4" />
                <AppBreadcrumb />
              </div>
            </header>
            <div className="flex flex-1 flex-col gap-4 pt-0">
              <Suspense
                fallback={
                  <div className="flex flex-1 items-center justify-center">
                    <LoadingSpinner />
                  </div>
                }
              >
                <Switch>
                  <Route path="/" component={Dashboard} />
                  <Route path={'/instance/connect'} component={ConnectInstance} />
                  <Route path={'/groups/create'} component={CreateGroup} />
                  <Route path="/groups/" component={Groups} />
                  <Route path={'/groups/:name'} component={GroupDetail} />
                  <Route path={'/services/:name/*?'} component={ServiceDetail} />
                </Switch>
              </Suspense>
            </div>
          </SidebarInset>
        </SidebarProvider>
        <Toaster />
      </div>
    </InstanceProvider>
  );
}

export default App;

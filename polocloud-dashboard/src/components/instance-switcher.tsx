import { ChevronsUpDown, Plus } from 'lucide-react';

import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuShortcut,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import {
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  useSidebar,
} from '@/components/ui/sidebar';
import { useInstance } from '@/lib/providers/InstanceProvider';
import { cn } from '@/lib/utils';
import { Link, useLocation } from 'wouter';

export function InstanceSwitcher() {
  const { isMobile } = useSidebar();
  const [, setLocation] = useLocation();
  const { currentInstance, availableInstances, switchInstance } = useInstance();
  return (
    <SidebarMenu className="border-b pb-2">
      <SidebarMenuItem>
        {!currentInstance ? (
          <Link href="/instance/connect">
            <div className="flex flex-row items-center space-x-2">
              <Plus className="text-xl" />
              <div>
                <p className="text-sm">No instance connected</p>
                <p className="text-xs text-muted-foreground">
                  Click to connect an instance
                </p>
              </div>
            </div>
          </Link>
        ) : (
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <SidebarMenuButton
                size="lg"
                className="data-[state=closed]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground"
              >
                <div
                  className={cn(
                    'flex aspect-square size-6 items-center group-data-[collapsible=icon]:size-8 rounded-lg justify-center animate-pulse text-sidebar-primary-foreground',
                    currentInstance.state === 'ONLINE' && 'bg-green-500',
                    currentInstance.state === 'MAINTENANCE' && 'bg-yellow-500',
                    currentInstance.state === 'OFFLINE' && 'bg-red-500'
                  )}
                >
                  <p className="font-bold text-lg">
                    {currentInstance.name.substring(0, 1)}
                  </p>
                </div>
                <div className="grid flex-1 text-left text-sm leading-tight">
                  <div className="flex flex-row items-center space-x-2">
                    <span className="truncate font-semibold">
                      {currentInstance.name}{' '}
                      <span className="text-xs text-muted-foreground">
                        ({currentInstance.state})
                      </span>
                    </span>
                  </div>
                  <span className="truncate text-xs text-muted-foreground">
                    {currentInstance.description}
                  </span>
                </div>
                <ChevronsUpDown className="ml-auto" />
              </SidebarMenuButton>
            </DropdownMenuTrigger>
            <DropdownMenuContent
              className="w-[--radix-dropdown-menu-trigger-width] min-w-56 rounded-lg"
              align="start"
              side={isMobile ? 'bottom' : 'right'}
              sideOffset={4}
            >
              <DropdownMenuLabel className="text-xs text-muted-foreground">
                Instances
              </DropdownMenuLabel>
              {availableInstances.map((team, index) => (
                <DropdownMenuItem
                  disabled
                  key={team.name}
                  onClick={() => switchInstance(team)}
                  className="gap-2 p-2"
                >
                  <div
                    className={cn(
                      'flex aspect-square size-3 items-center justify-center rounded-full  animate-pulse text-sidebar-primary-foreground',
                      team.state === 'ONLINE' && 'bg-green-500',
                      team.state === 'MAINTENANCE' && 'bg-yellow-500',
                      team.state === 'OFFLINE' && 'bg-red-500'
                    )}
                  />
                  {team.name}
                  <DropdownMenuShortcut>⌘{index + 1}</DropdownMenuShortcut>
                </DropdownMenuItem>
              ))}
              <DropdownMenuSeparator />
              <DropdownMenuItem
                disabled
                onClick={() => setLocation('/instance/connect')}
                className="gap-2 p-2"
              >
                <div className="flex size-6 items-center justify-center rounded-md border bg-background">
                  <Plus className="size-4" />
                </div>
                <div className="font-medium text-muted-foreground">
                  Connect Cloud instance
                </div>
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        )}
      </SidebarMenuItem>
    </SidebarMenu>
  );
}

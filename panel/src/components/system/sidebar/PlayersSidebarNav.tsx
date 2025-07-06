'use client';

import { ChevronRight, Users } from 'lucide-react';

import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from '@/components/ui/collapsible';
import {
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarMenuSub,
  SidebarMenuSubButton,
  SidebarMenuSubItem,
} from '@/components/ui/sidebar';
import { Skeleton } from '@/components/ui/skeleton';
import useClusterPlayers from '@/lib/hooks/api/useClusterPlayers';
import { Link } from 'wouter';

export function PlayersSidebarNav() {
  const { data: players, isLoading } = useClusterPlayers();

  return (
    <SidebarMenu>
      <Collapsible defaultOpen asChild className="group/collapsible">
        <SidebarMenuItem>
          <CollapsibleTrigger asChild>
            <Link href={'/groups'}>
              <SidebarMenuButton tooltip={'Players'}>
                <Users />
                <span>Players</span>
                <ChevronRight className="ml-auto transition-transform duration-200 group-data-[state=open]/collapsible:rotate-90" />
              </SidebarMenuButton>
            </Link>
          </CollapsibleTrigger>
          <CollapsibleContent>
            <SidebarMenuSub>
              {!players || isLoading
                ? Array.from({ length: 4 }).map((_, i) => (
                    <SidebarMenuSubItem key={i}>
                      <SidebarMenuSubButton asChild>
                        <Skeleton className="w-full h-5 rounded-md" />
                      </SidebarMenuSubButton>
                    </SidebarMenuSubItem>
                  ))
                : players.players.map((player) => (
                    <SidebarMenuSubItem key={player}>
                      <SidebarMenuSubButton asChild>
                        <Link
                          href={'/players/' + player}
                          className="flex flex-row items-center"
                        >
                          <img
                            src={`https://mineskin.eu/helm/${player}/64`}
                            className="size-3 object-contain"
                          />
                          <span>{player}</span>
                        </Link>
                      </SidebarMenuSubButton>
                    </SidebarMenuSubItem>
                  ))}
            </SidebarMenuSub>
          </CollapsibleContent>
        </SidebarMenuItem>
      </Collapsible>
    </SidebarMenu>
  );
}

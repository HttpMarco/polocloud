'use client';

import { Boxes, ChevronRight } from 'lucide-react';

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
import useClusterGroups from '@/lib/hooks/api/useClusterGroups';
import { Link } from 'wouter';
import { generateGradientStyle } from '@/lib/color/colorHelper';

export function GroupsSidebarNav() {
  const { data: groups, isLoading } = useClusterGroups();

  return (
    <SidebarMenu>
      <Collapsible defaultOpen asChild className="group/collapsible">
        <SidebarMenuItem>
          <CollapsibleTrigger asChild>
            <Link href={'/groups'}>
              <SidebarMenuButton tooltip={'Groups'}>
                <Boxes />
                <span>Groups</span>
                <ChevronRight className="ml-auto transition-transform duration-200 group-data-[state=open]/collapsible:rotate-90" />
              </SidebarMenuButton>
            </Link>
          </CollapsibleTrigger>
          <CollapsibleContent>
            <SidebarMenuSub>
              {!groups || isLoading
                ? Array.from({ length: 4 }).map((_, i) => (
                    <SidebarMenuSubItem key={i}>
                      <SidebarMenuSubButton asChild>
                        <Skeleton className="w-full h-5 rounded-md" />
                      </SidebarMenuSubButton>
                    </SidebarMenuSubItem>
                  ))
                : groups.map((group) => (
                    <SidebarMenuSubItem key={group.name}>
                      <SidebarMenuSubButton asChild>
                        <Link href={'/groups/' + group.name}>
                          <div
                            className="size-3 rounded-sm"
                            style={generateGradientStyle(group.name)}
                          />
                          <span>{group.name}</span>
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

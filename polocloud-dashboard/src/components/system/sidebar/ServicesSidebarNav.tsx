'use client';

import { ChevronRight, Server } from 'lucide-react';

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
import { generateGradientStyle } from '@/lib/color/colorHelper';
import useClusterServices from '@/lib/hooks/api/useClusterServices';
import { Link } from 'wouter';

export function ServicesSidebarNav() {
  const { data: services, isLoading } = useClusterServices();

  return (
    <SidebarMenu>
      <Collapsible defaultOpen asChild className="group/collapsible">
        <SidebarMenuItem>
          <CollapsibleTrigger asChild>
            <Link href={'/groups'}>
              <SidebarMenuButton tooltip={'Services'}>
                <Server />
                <span>Services</span>
                <ChevronRight className="ml-auto transition-transform duration-200 group-data-[state=open]/collapsible:rotate-90" />
              </SidebarMenuButton>
            </Link>
          </CollapsibleTrigger>
          <CollapsibleContent>
            <SidebarMenuSub>
              {!services || isLoading
                ? Array.from({ length: 4 }).map((_, i) => (
                    <SidebarMenuSubItem key={i}>
                      <SidebarMenuSubButton asChild>
                        <Skeleton className="w-full h-5 rounded-md" />
                      </SidebarMenuSubButton>
                    </SidebarMenuSubItem>
                  ))
                : services.map((service) => (
                    <SidebarMenuSubItem key={service.id}>
                      <SidebarMenuSubButton asChild>
                        <Link href={'/services/' + service.name}>
                          <div
                            className="size-3 rounded-sm"
                            style={generateGradientStyle(service.name.split('-')[0])}
                          />
                          <span>{service.name}</span>
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

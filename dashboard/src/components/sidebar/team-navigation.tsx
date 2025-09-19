'use client'

import { SidebarGroup, SidebarGroupContent, SidebarGroupLabel, SidebarMenu, SidebarMenuButton, SidebarMenuItem } from "@/components/ui/sidebar";
import { Users, Shield } from "lucide-react";

export function TeamNavigation() {
  return (
    <SidebarGroup className="mt-6">
      <SidebarGroupLabel className="px-2 py-1 text-xs font-semibold text-sidebar-foreground/60 uppercase tracking-wider">
        Team
      </SidebarGroupLabel>
      <SidebarGroupContent>
        <SidebarMenu>
          <SidebarMenuItem>
            <SidebarMenuButton asChild>
              <a href="/users" className="w-full">
                <Users className="h-4 w-4" />
                <span>Users</span>
              </a>
            </SidebarMenuButton>
          </SidebarMenuItem>
          <SidebarMenuItem>
            <SidebarMenuButton asChild>
              <a href="/roles">
                <Shield className="h-4 w-4" />
                <span>Roles</span>
              </a>
            </SidebarMenuButton>
          </SidebarMenuItem>
        </SidebarMenu>
      </SidebarGroupContent>
    </SidebarGroup>
  );
}

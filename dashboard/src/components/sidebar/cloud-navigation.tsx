'use client'

import { useState, useEffect } from "react";
import { SidebarGroup, SidebarMenu, SidebarMenuItem, SidebarMenuButton, SidebarMenuSub, SidebarMenuSubButton, SidebarMenuSubItem } from "@/components/ui/sidebar";
import { 
  Terminal, 
  Activity,
  Users,
  Building2,
  ChevronDown,
  ChevronRight
} from "lucide-react";
import { useRouter, usePathname } from "next/navigation";

export function CloudNavigation() {
  const [services, setServices] = useState<any[]>([]);
  const [groups, setGroups] = useState<any[]>([]);
  const [players, setPlayers] = useState<any[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [expandedSections, setExpandedSections] = useState<Set<string>>(new Set());
  
  const router = useRouter();
  const pathname = usePathname();

  useEffect(() => {
    const loadData = async () => {
      try {
        // Load services
        const servicesResponse = await fetch('/api/services/list');
        if (servicesResponse.ok) {
          const servicesData = await servicesResponse.json();
          if (servicesData.success && servicesData.services) {
            setServices(servicesData.services);
          }
        }

        // Load groups
        const groupsResponse = await fetch('/api/groups/list');
        if (groupsResponse.ok) {
          const groupsData = await groupsResponse.json();
          if (groupsData.success && groupsData.groups) {
            setGroups(groupsData.groups);
          }
        }

        // Load players - using a placeholder for now
        // You can implement this when you have a players API endpoint
        setPlayers([]);
      } catch (error) {
        console.error('Failed to load data:', error);
      } finally {
        setIsLoading(false);
      }
    };

    loadData();
  }, []);

  const toggleSection = (sectionName: string) => {
    setExpandedSections(prev => {
      const newSet = new Set(prev);
      if (newSet.has(sectionName)) {
        newSet.delete(sectionName);
      } else {
        newSet.add(sectionName);
      }
      return newSet;
    });
  };

  const isActive = (href: string) => {
    if (href === '/') {
      return pathname === '/';
    }
    return pathname.startsWith(href);
  };

  return (
    <SidebarGroup>
      <SidebarMenu>

        {/* Groups */}
        <SidebarMenuItem>
          <SidebarMenuButton 
            onClick={() => toggleSection('groups')}
            className="w-full justify-start"
          >
            <Building2 className="w-4 h-4" />
            <span>Groups</span>
            {expandedSections.has('groups') ? (
              <ChevronDown className="w-4 h-4 ml-1" />
            ) : (
              <ChevronRight className="w-4 h-4 ml-1" />
            )}
          </SidebarMenuButton>
          
          {expandedSections.has('groups') && (
            <SidebarMenuSub>
              {groups.map((group) => (
                <SidebarMenuSubItem key={group.name}>
                  <SidebarMenuSubButton 
                    onClick={() => router.push(`/groups/${group.name}`)}
                    isActive={pathname === `/groups/${group.name}`}
                    className="w-full justify-start"
                  >
                    <span>{group.name}</span>
                  </SidebarMenuSubButton>
                </SidebarMenuSubItem>
              ))}
            </SidebarMenuSub>
          )}
        </SidebarMenuItem>

        {/* Services */}
        <SidebarMenuItem>
          <SidebarMenuButton 
            onClick={() => toggleSection('services')}
            className="w-full justify-start"
          >
            <Activity className="w-4 h-4" />
            <span>Services</span>
            {expandedSections.has('services') ? (
              <ChevronDown className="w-4 h-4 ml-1" />
            ) : (
              <ChevronRight className="w-4 h-4 ml-1" />
            )}
          </SidebarMenuButton>
          
          {expandedSections.has('services') && (
            <SidebarMenuSub>
              {services.map((service) => (
                <SidebarMenuSubItem key={service.name}>
                  <SidebarMenuSubButton 
                    onClick={() => router.push(`/services/${service.name}/screen`)}
                    isActive={pathname === `/services/${service.name}/screen`}
                    className="w-full justify-start"
                  >
                    <span>{service.name}</span>
                  </SidebarMenuSubButton>
                </SidebarMenuSubItem>
              ))}
            </SidebarMenuSub>
          )}
        </SidebarMenuItem>

        {/* Players */}
        <SidebarMenuItem>
          <SidebarMenuButton 
            onClick={() => router.push('/players')}
            isActive={isActive('/players')}
            className="w-full justify-start"
          >
            <Users className="w-4 h-4" />
            <span>Players</span>
          </SidebarMenuButton>
        </SidebarMenuItem>

        {/* Terminal */}
        <SidebarMenuItem>
          <SidebarMenuButton 
            onClick={() => router.push('/terminal')}
            isActive={isActive('/terminal')}
            className="w-full justify-start"
          >
            <Terminal className="w-4 h-4" />
            <span>Terminal</span>
          </SidebarMenuButton>
        </SidebarMenuItem>
      </SidebarMenu>
    </SidebarGroup>
  );
}

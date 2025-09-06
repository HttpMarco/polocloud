'use client'

import { useState, useEffect } from "react";
import { SidebarGroup, SidebarGroupLabel, SidebarMenu, SidebarMenuItem, SidebarMenuButton, SidebarMenuSub, SidebarMenuSubButton, SidebarMenuSubItem } from "@/components/ui/sidebar";
import { Badge } from "@/components/ui/badge";
import { 
  Server, 
  Users, 
  Terminal, 
  Settings, 
  Building2, 
  ChevronDown, 
  ChevronRight,
  Activity,
  WifiOff
} from "lucide-react";
import { useRouter, usePathname } from "next/navigation";
import { motion, AnimatePresence } from "framer-motion";

interface Service {
  name: string;
  state: string;
  group: string;
}

interface Group {
  name: string;
  services: Service[];
}

export function CloudNavigation() {
  const [services, setServices] = useState<Service[]>([]);
  const [groups, setGroups] = useState<Group[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [expandedGroups, setExpandedGroups] = useState<Set<string>>(new Set());
  const [expandedSections, setExpandedSections] = useState<Set<string>>(new Set(['cloud']));
  
  const router = useRouter();
  const pathname = usePathname();

  useEffect(() => {
    const loadServices = async () => {
      try {
        const response = await fetch('/api/services/list');
        if (response.ok) {
          const data = await response.json();
          if (data.success && data.services) {
            setServices(data.services);
            
            // Group services by group name
            const groupedServices = data.services.reduce((acc: { [key: string]: Service[] }, service: Service) => {
              const groupName = service.group || 'default';
              if (!acc[groupName]) {
                acc[groupName] = [];
              }
              acc[groupName].push(service);
              return acc;
            }, {});
            
            const groupArray = Object.entries(groupedServices).map(([name, services]) => ({
              name,
              services: services as Service[]
            }));
            
            setGroups(groupArray);
          }
        }
      } catch (error) {
        console.error('Failed to load services:', error);
      } finally {
        setIsLoading(false);
      }
    };

    loadServices();
  }, []);

  const toggleGroup = (groupName: string) => {
    setExpandedGroups(prev => {
      const newSet = new Set(prev);
      if (newSet.has(groupName)) {
        newSet.delete(groupName);
      } else {
        newSet.add(groupName);
      }
      return newSet;
    });
  };

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

  const getServiceStateColor = (state: string) => {
    switch (state) {
      case 'ONLINE':
        return 'bg-green-500';
      case 'STARTING':
      case 'PREPARING':
        return 'bg-yellow-500';
      case 'STOPPING':
      case 'STOPPED':
        return 'bg-red-500';
      default:
        return 'bg-gray-500';
    }
  };

  const getServiceStateText = (state: string) => {
    switch (state) {
      case 'ONLINE':
        return 'Online';
      case 'STARTING':
        return 'Starting';
      case 'PREPARING':
        return 'Preparing';
      case 'STOPPING':
        return 'Stopping';
      case 'STOPPED':
        return 'Stopped';
      default:
        return 'Unknown';
    }
  };

  const isActive = (href: string) => {
    if (href === '/') {
      return pathname === '/';
    }
    return pathname.startsWith(href);
  };

  if (isLoading) {
    return (
      <SidebarGroup>
        <SidebarGroupLabel>Cloud</SidebarGroupLabel>
        <SidebarMenu>
          <SidebarMenuItem>
            <div className="flex items-center justify-center py-2">
              <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-sidebar-foreground"></div>
            </div>
          </SidebarMenuItem>
        </SidebarMenu>
      </SidebarGroup>
    );
  }

  return (
    <SidebarGroup>
      <SidebarGroupLabel 
        className="flex items-center justify-between cursor-pointer hover:bg-sidebar-accent/50 rounded-md px-2 py-1"
        onClick={() => toggleSection('cloud')}
      >
        <span>Cloud</span>
        {expandedSections.has('cloud') ? (
          <ChevronDown className="w-4 h-4" />
        ) : (
          <ChevronRight className="w-4 h-4" />
        )}
      </SidebarGroupLabel>
      
      <AnimatePresence>
        {expandedSections.has('cloud') && (
          <motion.div
            initial={{ opacity: 0, height: 0 }}
            animate={{ opacity: 1, height: 'auto' }}
            exit={{ opacity: 0, height: 0 }}
            transition={{ duration: 0.2 }}
          >
            <SidebarMenu>
              <SidebarMenuItem>
                <SidebarMenuButton 
                  onClick={() => router.push('/')}
                  isActive={isActive('/')}
                  className="w-full justify-start"
                >
                  <Server className="w-4 h-4" />
                  <span>Dashboard</span>
                </SidebarMenuButton>
              </SidebarMenuItem>

              <SidebarMenuItem>
                <SidebarMenuButton 
                  onClick={() => router.push('/services')}
                  isActive={isActive('/services')}
                  className="w-full justify-start"
                >
                  <Activity className="w-4 h-4" />
                  <span>Services</span>
                  <Badge variant="secondary" className="ml-auto">
                    {services.length}
                  </Badge>
                </SidebarMenuButton>
              </SidebarMenuItem>

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

              {groups.map((group) => (
                <SidebarMenuItem key={group.name}>
                  <SidebarMenuButton 
                    onClick={() => toggleGroup(group.name)}
                    className="w-full justify-start"
                  >
                    <Building2 className="w-4 h-4" />
                    <span className="truncate">{group.name}</span>
                    <Badge variant="outline" className="ml-auto">
                      {group.services.length}
                    </Badge>
                    {expandedGroups.has(group.name) ? (
                      <ChevronDown className="w-4 h-4 ml-1" />
                    ) : (
                      <ChevronRight className="w-4 h-4 ml-1" />
                    )}
                  </SidebarMenuButton>
                  
                  <AnimatePresence>
                    {expandedGroups.has(group.name) && (
                      <motion.div
                        initial={{ opacity: 0, height: 0 }}
                        animate={{ opacity: 1, height: 'auto' }}
                        exit={{ opacity: 0, height: 0 }}
                        transition={{ duration: 0.2 }}
                      >
                        <SidebarMenuSub>
                          {group.services.map((service) => (
                            <SidebarMenuSubItem key={service.name}>
                              <SidebarMenuSubButton 
                                onClick={() => router.push(`/services/${service.name}/screen`)}
                                isActive={pathname === `/services/${service.name}/screen`}
                                className="w-full justify-start"
                              >
                                <div className="flex items-center space-x-2 w-full">
                                  <div className={`w-2 h-2 rounded-full ${getServiceStateColor(service.state)}`} />
                                  <span className="truncate flex-1">{service.name}</span>
                                  <Badge 
                                    variant="outline" 
                                    className={`text-xs ${
                                      service.state === 'ONLINE' ? 'text-green-600 border-green-600' :
                                      service.state === 'STARTING' || service.state === 'PREPARING' ? 'text-yellow-600 border-yellow-600' :
                                      'text-red-600 border-red-600'
                                    }`}
                                  >
                                    {getServiceStateText(service.state)}
                                  </Badge>
                                </div>
                              </SidebarMenuSubButton>
                            </SidebarMenuSubItem>
                          ))}
                        </SidebarMenuSub>
                      </motion.div>
                    )}
                  </AnimatePresence>
                </SidebarMenuItem>
              ))}
            </SidebarMenu>
          </motion.div>
        )}
      </AnimatePresence>
    </SidebarGroup>
  );
}

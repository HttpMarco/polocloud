'use client'

import { useState, useEffect } from "react";
import { SidebarGroup, SidebarGroupContent, SidebarGroupLabel, SidebarMenu, SidebarMenuButton, SidebarMenuItem } from "@/components/ui/sidebar";
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from "@/components/ui/collapsible";
import { Button } from "@/components/ui/button";
import { Package, Cloud, FileText, Users, Terminal, ChevronRight, Play, Square, Loader2, RotateCcw } from "lucide-react";
import Image from "next/image";
import { getPlatformIcon } from "@/lib/platform-icons";
import { useSidebarData } from "@/components/sidebar-data-provider";
import { Group } from "@/types/groups";
import { Service } from "@/types/services";
import { API_ENDPOINTS } from "@/lib/api";

const cloudItems = [
  {
    title: "Groups",
    icon: Package,
    items: 0,
    collapsible: false,
    href: "/groups"
  },
  {
    title: "Services",
    icon: Cloud,
    items: 0,
    collapsible: true,
  },
  {
    title: "Templates",
    icon: FileText,
    items: 0,
    collapsible: false,
    href: "/templates"
  },
  {
    title: "Players",
    icon: Users,
    items: 0,
    collapsible: false,
    href: "/players"
  },
  {
    title: "Terminal",
    icon: Terminal,
    items: 0,
    collapsible: false,
    href: "/terminal"
  },
];

export function CloudNavigation() {
  const { groups: initialGroups, services: initialServices, isLoading: sidebarDataLoading } = useSidebarData();
  const [groups, setGroups] = useState<Group[]>(initialGroups);
  const [services, setServices] = useState<Service[]>(initialServices);
  const [isGroupsLoading, setIsGroupsLoading] = useState(sidebarDataLoading);
  const [isServicesLoading, setIsServicesLoading] = useState(sidebarDataLoading);
  const [restartingServices, setRestartingServices] = useState<string[]>([]);

  useEffect(() => {
    setGroups(initialGroups);
    setServices(initialServices);
    setIsGroupsLoading(sidebarDataLoading);
    setIsServicesLoading(sidebarDataLoading);
  }, [initialGroups, initialServices, sidebarDataLoading]);

  useEffect(() => {
    const handleServiceStateUpdate = (event: CustomEvent) => {
      const { serviceName, state, updateData } = event.detail;

      setServices(prev => prev.map(service => 
        service.name === serviceName 
          ? { 
              ...service, 
              state: state,
              ...(state === 'STARTING' || state === 'PREPARING' ? {
                playerCount: -1,
                maxPlayerCount: -1,
                cpuUsage: -1,
                memoryUsage: -1,
                maxMemory: -1
              } : {}),
              ...(state === 'STOPPING' || state === 'STOPPED' ? {
                playerCount: 0,
                maxPlayerCount: 0,
                cpuUsage: 0,
                memoryUsage: 0,
                maxMemory: 0
              } : {}),
              ...(updateData || {})
            }
            : service
      ));
    };

    window.addEventListener('serviceStateUpdate', handleServiceStateUpdate as EventListener);
    
    return () => {
      window.removeEventListener('serviceStateUpdate', handleServiceStateUpdate as EventListener);
    };
  }, []);

  const typedGroups = (groups || []) as Group[];
  const typedServices = (services || []) as Service[];

  const getPlatformName = (group: Group): string => {
    if (typeof group.platform === 'string') {
      return group.platform
    } else if (group.platform && typeof group.platform === 'object' && 'name' in group.platform) {
      return group.platform.name
    }
    return 'default'
  }

  const getServiceStatusIcon = (state: string | undefined) => {
    if (state === 'ONLINE') {
      return <Play className="w-3 h-3 text-green-500" />
    } else if (state === 'OFFLINE' || state === 'STOPPING' || state === 'STOPPED') {
      return <Square className="w-3 h-3 text-red-500" />
    } else if (state === 'STARTING' || state === 'PREPARING') {
      return <Loader2 className="w-3 h-3 text-yellow-500 animate-spin" />
    } else {
      return <Square className="w-3 h-3 text-gray-500" />
    }
  }

  const handleRestartService = async (serviceName: string, event: React.MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();
    
    if (restartingServices.includes(serviceName)) return;

    setRestartingServices(prev => [...prev, serviceName]);

    try {
      const response = await fetch(API_ENDPOINTS.SERVICES.RESTART(serviceName), {
        method: 'PATCH'
      });

      if (response.ok) {
      } else {
      }
    } catch {
    } finally {
      setRestartingServices(prev => prev.filter(name => name !== serviceName));
    }
  };

  return (
    <SidebarGroup>
      <SidebarGroupLabel className="px-2 py-1 text-xs font-semibold text-sidebar-foreground/60 uppercase tracking-wider">
        Cloud
      </SidebarGroupLabel>
      <SidebarGroupContent>
        <SidebarMenu>
          {cloudItems.map((item) => (
            <SidebarMenuItem key={item.title}>
              {item.title === "Groups" ? (
                <Collapsible className="group/collapsible">
                  <div className="flex items-center">
                    <SidebarMenuButton 
                      className="flex-1 px-2 py-2 rounded-md hover:bg-sidebar-accent hover:text-sidebar-accent-foreground transition-all duration-200"
                      onClick={() => window.location.href = '/groups'}
                    >
                      <span className="flex items-center space-x-2">
                        <item.icon className="w-4 h-4" />
                        <span className="text-sm font-medium">
                          {item.title}
                        </span>
                      </span>
                    </SidebarMenuButton>
                    <CollapsibleTrigger asChild>
                      <Button 
                        variant="ghost" 
                        size="sm" 
                        className="h-8 w-8 p-0 hover:bg-sidebar-accent"
                      >
                        <ChevronRight className="w-4 h-4 transition-transform group-data-[state=open]/collapsible:rotate-90" />
                      </Button>
                    </CollapsibleTrigger>
                  </div>
                  
                  <CollapsibleContent>
                    <div className="ml-4 mt-1 space-y-1">
                      {isGroupsLoading ? (
                        <div className="px-2 py-1.5">
                          <div className="w-full h-3 bg-sidebar-accent animate-pulse rounded"></div>
                        </div>
                      ) : !Array.isArray(typedGroups) || typedGroups.length === 0 ? (
                        <div className="px-2 py-1.5 text-xs text-sidebar-foreground/40">
                          No groups found
                        </div>
                      ) : (
                        typedGroups.map((group, index) => (
                          <a 
                            key={index} 
                            href={`/groups/${group.name}`}
                            className="block px-3 py-2 rounded-md transition-colors"
                          >
                            <div className="flex items-center space-x-3">
                              <Image 
                                src={getPlatformIcon(getPlatformName(group))} 
                                alt={`${getPlatformName(group)} icon`}
                                width={16}
                                height={16}
                                className="w-4 h-4 object-contain"
                                onError={(e) => {
                                  e.currentTarget.src = '/placeholder.png'
                                }}
                              />
                              <span className="text-sm text-sidebar-foreground/80 truncate">
                                {group.name}
                              </span>
                            </div>
                          </a>
                        ))
                      )}
                    </div>
                  </CollapsibleContent>
                </Collapsible>
              ) : item.title === "Services" ? (
                <Collapsible className="group/collapsible">
                  <div className="flex items-center">
                    <SidebarMenuButton 
                      className="flex-1 px-2 py-2 rounded-md hover:bg-sidebar-accent hover:text-sidebar-accent-foreground transition-all duration-200"
                      onClick={() => window.location.href = '/services'}
                    >
                      <span className="flex items-center space-x-2">
                        <item.icon className="w-4 h-4" />
                        <span className="text-sm font-medium">
                          {item.title}
                        </span>
                      </span>
                    </SidebarMenuButton>
                    <CollapsibleTrigger asChild>
                      <Button 
                        variant="ghost" 
                        size="sm" 
                        className="h-8 w-8 p-0 hover:bg-sidebar-accent"
                      >
                        <ChevronRight className="w-4 h-4 transition-transform group-data-[state=open]/collapsible:rotate-90" />
                      </Button>
                    </CollapsibleTrigger>
                  </div>
                  
                  <CollapsibleContent>
                    <div className="ml-4 mt-1 space-y-1">
                      {isServicesLoading ? (
                        <div className="px-2 py-1.5">
                          <div className="w-full h-3 bg-sidebar-accent animate-pulse rounded"></div>
                        </div>
                      ) : !Array.isArray(typedServices) || typedServices.length === 0 ? (
                        <div className="px-2 py-1.5 text-xs text-sidebar-foreground/40">
                          No services found
                        </div>
                      ) : (
                        typedServices.map((service, index) => (
                          <div 
                            key={index} 
                            className="group flex items-center justify-between px-3 py-2 rounded-md hover:bg-sidebar-accent transition-colors"
                          >
                            <a 
                              href={`/services/${service.name}/screen`}
                              className="flex items-center space-x-3 flex-1 min-w-0"
                            >
                              {getServiceStatusIcon(service.state)}
                              <span className="text-sm text-sidebar-foreground/80 truncate">
                                {service.name}
                              </span>
                            </a>
                            <Button
                              size="sm"
                              variant="ghost"
                              className="opacity-0 group-hover:opacity-100 transition-opacity h-6 w-6 p-0 hover:bg-sidebar-accent-foreground/10"
                              onClick={(e) => handleRestartService(service.name, e)}
                              disabled={restartingServices.includes(service.name)}
                            >
                              {restartingServices.includes(service.name) ? (
                                <Loader2 className="w-3 h-3 animate-spin" />
                              ) : (
                                <RotateCcw className="w-3 h-3" />
                              )}
                            </Button>
                          </div>
                        ))
                      )}
                    </div>
                  </CollapsibleContent>
                </Collapsible>

              ) : item.collapsible ? (
                <Collapsible className="group/collapsible">
                  <CollapsibleTrigger asChild>
                    <SidebarMenuButton className="w-full px-2 py-2 rounded-md hover:bg-accent hover:text-accent-foreground transition-all duration-200">
                      <span className="flex items-center justify-between w-full">
                        <span className="flex items-center justify-between w-full">
                          <span className="flex items-center space-x-2">
                            <item.icon className="w-4 h-4" />
                            <span className="text-sm font-medium">{item.title}</span>
                          </span>
                          <ChevronRight className="w-4 h-4 transition-transform group-data-[state=open]/collapsible:rotate-90" />
                        </span>
                      </span>
                    </SidebarMenuButton>
                  </CollapsibleTrigger>
                  <CollapsibleContent>
                    <div className="ml-4 mt-1 space-y-1">
                      {Array.from({ length: item.items }).map((_, index) => (
                        <div key={index} className="px-2 py-1.5 rounded-md hover:bg-sidebar-accent/50 transition-colors">
                          <div className="w-full h-3 bg-sidebar-accent animate-pulse rounded"></div>
                        </div>
                      ))}
                    </div>
                  </CollapsibleContent>
                </Collapsible>
              ) : (
                <SidebarMenuButton asChild>
                  <a href={item.href || "#"} className="w-full px-2 py-2 rounded-md hover:bg-sidebar-accent hover:text-sidebar-accent-foreground transition-all duration-200">
                    <span className="flex items-center space-x-2">
                      <item.icon className="w-4 h-4" />
                      <span className="text-sm font-medium">{item.title}</span>
                    </span>
                  </a>
                </SidebarMenuButton>
              )}
            </SidebarMenuItem>
          ))}
        </SidebarMenu>
      </SidebarGroupContent>
    </SidebarGroup>
  )
}
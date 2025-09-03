'use client'

import { useState, useEffect, useCallback } from "react";
import { Sidebar, SidebarContent, SidebarFooter, SidebarGroup, SidebarGroupContent, SidebarGroupLabel, SidebarHeader, SidebarMenu, SidebarMenuButton, SidebarMenuItem } from "@/components/ui/sidebar";
import { Button } from "@/components/ui/button";
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from "@/components/ui/collapsible";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from "@/components/ui/dialog";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useRouter } from "next/navigation";
import Image from "next/image";
import { 
  Package, 
  Cloud, 
  FileText, 
  Users, 
  Terminal, 
  Settings, 
  LogOut, 
  ChevronRight, 
  Moon, 
  Sparkles, 
  Globe, 
  Palette, 
  TreePine, 
  Flame,
  Play,
  Square,
  Loader2,
  Monitor,
  Shield,
  User,
  Lock,
  Edit,
  Save,
  X,
  Eye,
  EyeOff,
  Trash2,
  Laptop,
  Smartphone,
  Tablet
} from "lucide-react";
import { getPlatformIcon } from "@/lib/platform-icons";
import { API_ENDPOINTS } from "@/lib/api";
import { Group } from "@/types/groups";
import { Service } from "@/types/services";
import { useWebSocketSystem } from "@/hooks/useWebSocketSystem";
import { useSidebarData } from "@/components/sidebar-data-provider";

interface UserData {
  username: string;
  userUUID: string;
  role: { id: number; label: string; hexColor: string } | null;
}

interface ChangePasswordData {
  currentPassword: string;
  newPassword: string;
  confirmPassword: string;
}

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


let userDataCache: UserData | null = null;

const resetUserDataCache = () => {
  userDataCache = null;
};

const loadUserData = async (): Promise<UserData> => {
  if (userDataCache) return userDataCache;
  
  try {

    const adminUsername = localStorage.getItem('adminUsername');
    const isLoggedIn = localStorage.getItem('isLoggedIn');
    
    if (adminUsername && isLoggedIn === 'true') {

      try {
        const userResponse = await fetch(API_ENDPOINTS.AUTH.ME);
        if (userResponse.ok) {
          const responseData = await userResponse.json();

          if (responseData.authenticated && responseData.user) {
            const username = responseData.user.username || adminUsername;
            const userUUID = responseData.user.uuid || 'admin-' + Date.now();

            let role = null;
            if (responseData.user.role !== undefined && responseData.user.role !== null) {
              try {
                const roleResponse = await fetch(API_ENDPOINTS.ROLE.GET(responseData.user.role));
                if (roleResponse.ok) {
                  const roleData = await roleResponse.json();
                  role = roleData;
                }
              } catch {}
            }

            if (role) {
              userDataCache = { username, userUUID, role };
              return userDataCache;
            }
          }
        }
      } catch  {}
      const role = {
        id: -1,
        label: 'Admin',
        hexColor: '#dc2626'
      };
      
      userDataCache = { 
        username: adminUsername, 
        userUUID: 'admin-' + Date.now(),
        role 
      };
      return userDataCache;
    }

    userDataCache = { username: 'Guest', userUUID: '', role: null };
    return userDataCache;
    
  } catch {
    userDataCache = { username: 'Guest', userUUID: '', role: null };
    return userDataCache;
  }
};

const SidebarHeaderComponent = () => (
  <SidebarHeader className="border-b border-sidebar-border p-4">
    <a href="/" className="flex items-center space-x-3 hover:opacity-80 transition-opacity cursor-pointer">
      <div className="w-8 h-8 flex items-center justify-center">
        <Image src="/logo.png" alt="Polocloud Logo" width={32} height={32} className="w-8 h-8" />
      </div>
      <div>
        <h2 className="text-sm font-semibold text-sidebar-foreground">Polocloud</h2>
        <p className="text-xs text-sidebar-foreground/60">The simplest cloudsystem</p>
      </div>
    </a>
  </SidebarHeader>
);

const CloudNavigation = () => {

  const { groups: initialGroups, services: initialServices, isLoading: sidebarDataLoading } = useSidebarData();
  const [groups, setGroups] = useState<Group[]>(initialGroups);
  const [services, setServices] = useState<Service[]>(initialServices);
  const [isGroupsLoading, setIsGroupsLoading] = useState(sidebarDataLoading);
  const [isServicesLoading, setIsServicesLoading] = useState(sidebarDataLoading);

  const { isConnected: wsConnected } = useWebSocketSystem({
    path: '/services/update',
    autoConnect: true,
    onMessage: (message) => {
      try {

        let updateData;
        if (typeof message.data === 'string') {
          try {
            updateData = JSON.parse(message.data);
          } catch (parseError) {
            return;
          }
        } else if (message.data && typeof message.data === 'object') {
          updateData = message.data;
        } else {
          updateData = message;
        }
        
        if (updateData && updateData.serviceName && updateData.state) {

          setServices(prev => prev.map(service => 
            service.name === updateData.serviceName 
              ? { ...service, state: updateData.state }
              : service
          ));
        }
      } catch {}
    }
  });

  useEffect(() => {
    setGroups(initialGroups);
    setServices(initialServices);
    setIsGroupsLoading(sidebarDataLoading);
    setIsServicesLoading(sidebarDataLoading);
  }, [initialGroups, initialServices, sidebarDataLoading]);



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
                <Collapsible defaultOpen className="group/collapsible">
                  <CollapsibleTrigger asChild>
                    <SidebarMenuButton className="w-full px-2 py-2 rounded-md hover:bg-sidebar-accent hover:text-sidebar-accent-foreground transition-all duration-200">
                      <span className="flex items-center justify-between w-full">
                        <span className="flex items-center space-x-2">
                          <item.icon className="w-4 h-4" />
                          <span 
                            className="text-sm font-medium cursor-pointer hover:text-sidebar-accent-foreground"
                            onClick={(e) => {
                              e.stopPropagation()
                              window.location.href = '/groups'
                            }}
                          >
                            {item.title}
                          </span>
                        </span>
                        <ChevronRight className="w-4 h-4 transition-transform group-data-[state=open]/collapsible:rotate-90" />
                      </span>
                    </SidebarMenuButton>
                  </CollapsibleTrigger>
                  
                  {}
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
                <Collapsible defaultOpen className="group/collapsible">
                  <CollapsibleTrigger asChild>
                    <SidebarMenuButton className="w-full px-2 py-2 rounded-md hover:bg-sidebar-accent hover:text-sidebar-accent-foreground transition-all duration-200">
                      <span className="flex items-center justify-between w-full">
                        <span className="flex items-center space-x-2">
                          <item.icon className="w-4 h-4" />
                          <span 
                            className="text-sm font-medium cursor-pointer hover:text-sidebar-accent-foreground"
                            onClick={(e) => {
                              e.stopPropagation()
                              window.location.href = '/services'
                            }}
                          >
                            {item.title}
                          </span>
                        </span>
                        <ChevronRight className="w-4 h-4 transition-transform group-data-[state=open]/collapsible:rotate-90" />
                      </span>
                    </SidebarMenuButton>
                  </CollapsibleTrigger>
                  
                  {}
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
                          <a 
                            key={index} 
                            href={`/services/${service.name}/screen`}
                            className="block px-3 py-2 rounded-md transition-colors"
                          >
                            <div className="flex items-center space-x-3">
                              {getServiceStatusIcon(service.state)}
                              <span className="text-sm text-sidebar-foreground/80 truncate">
                                {service.name}
                              </span>
                            </div>
                          </a>
                        ))
                      )}
                    </div>
                  </CollapsibleContent>
                </Collapsible>

              ) : item.collapsible ? (
                <Collapsible defaultOpen className="group/collapsible">
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

const TeamNavigation = () => (
  <>
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


  </>
);

const SettingsDialog = ({ 
  isOpen, 
  onClose, 
  userData
}: {
  isOpen: boolean;
  onClose: () => void;
  userData: UserData;
}) => (
  <Dialog open={isOpen} onOpenChange={onClose}>
    <DialogContent className="max-w-4xl bg-background border border-border">
      <DialogHeader>
        <DialogTitle className="text-xl font-semibold text-foreground flex items-center space-x-2">
          <Settings className="w-5 h-5" />
          <span>Settings</span>
        </DialogTitle>
      </DialogHeader>
      
      <Tabs defaultValue="account" className="w-full">
        <TabsList className="grid w-full grid-cols-3 bg-muted border border-border">
          <TabsTrigger value="account" className="flex items-center space-x-2">
            <User className="w-4 h-4" />
            <span>Account</span>
          </TabsTrigger>
          <TabsTrigger value="security" className="flex items-center space-x-2">
            <Lock className="w-4 h-4" />
            <span>Security</span>
          </TabsTrigger>
          <TabsTrigger value="appearance" className="flex items-center space-x-2">
            <Palette className="w-4 h-4" />
            <span>Appearance</span>
          </TabsTrigger>
        </TabsList>
        
        <TabsContent value="account" className="space-y-4 mt-6">
          <GeneralTab user={userData} skinHeadUrl={`https://mineskin.eu/helm/${userData.username}/64`} />
        </TabsContent>
        
        <TabsContent value="security" className="space-y-6 mt-6">
          <SecurityTab />
        </TabsContent>
        
        <TabsContent value="appearance" className="space-y-4 mt-6">
          <AppearanceTab />
        </TabsContent>
      </Tabs>
    </DialogContent>
  </Dialog>
);

const ChangePasswordDialog = ({ 
  isOpen, 
  onClose, 
  onSubmit, 
  data, 
  onChange, 
  isLoading 
}: {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: () => void;
  data: ChangePasswordData;
  onChange: (field: keyof ChangePasswordData, value: string) => void;
  isLoading: boolean;
}) => (
  <Dialog open={isOpen} onOpenChange={onClose}>
    <DialogContent className="max-w-md bg-sidebar border border-sidebar-border">
      <DialogHeader>
        <DialogTitle className="text-xl font-semibold text-sidebar-foreground flex items-center space-x-2">
          <Lock className="w-5 h-5" />
          <span>Change Password</span>
        </DialogTitle>
      </DialogHeader>
      
      <div className="space-y-4 mt-6">
        <div className="space-y-2">
          <Label htmlFor="current-password" className="text-sm font-medium text-sidebar-foreground">
            Current Password
          </Label>
          <Input 
            id="current-password" 
            type="password" 
            placeholder="Enter current password" 
            className="bg-sidebar-accent border-sidebar-border"
            value={data.currentPassword}
            onChange={(e) => onChange('currentPassword', e.target.value)}
          />
        </div>
        
        <div className="space-y-2">
          <Label htmlFor="new-password" className="text-sm font-medium text-sidebar-foreground">
            New Password
          </Label>
          <Input 
            id="new-password" 
            type="password" 
            placeholder="Enter new password" 
            className="bg-sidebar-accent border-sidebar-border"
            value={data.newPassword}
            onChange={(e) => onChange('newPassword', e.target.value)}
          />
        </div>
        
        <div className="space-y-2">
          <Label htmlFor="confirm-password" className="text-sm font-medium text-sidebar-foreground">
            Confirm New Password
          </Label>
          <Input 
            id="confirm-password" 
            type="password" 
            placeholder="Confirm new password" 
            className="bg-sidebar-accent border-sidebar-border"
            value={data.confirmPassword}
            onChange={(e) => onChange('confirmPassword', e.target.value)}
          />
        </div>
        
        <div className="flex justify-end space-x-2">
          <Button 
            variant="outline" 
            onClick={onClose}
            className="border-sidebar-border text-sidebar-foreground hover:bg-sidebar-accent"
          >
            Cancel
          </Button>
          <Button 
            onClick={onSubmit}
            disabled={isLoading || !data.newPassword || !data.confirmPassword}
            className="bg-primary hover:bg-primary/90"
          >
            {isLoading ? 'Changing...' : 'Save Changes'}
          </Button>
        </div>
      </div>
    </DialogContent>
  </Dialog>
);


const SecurityTab = () => {
  const [tokens, setTokens] = useState<any[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [revealedIPs, setRevealedIPs] = useState<Set<string>>(new Set());

  const [isPasswordModalOpen, setIsPasswordModalOpen] = useState(false);
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [showNewPassword, setShowNewPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [isChangingPassword, setIsChangingPassword] = useState(false);

  useEffect(() => {
    fetchTokens();
  }, []);

  const toggleIPVisibility = (ip: string) => {
    setRevealedIPs(prev => {
      const newSet = new Set(prev);
      if (newSet.has(ip)) {
        newSet.delete(ip);
      } else {
        newSet.add(ip);
      }
      return newSet;
    });
  };

  const fetchTokens = useCallback(async () => {
    setIsLoading(true);
    try {

      const backendIP = localStorage.getItem('backendIp');
      if (!backendIP) {

        setTokens([]);
        return;
      }

      const response = await fetch(API_ENDPOINTS.USER.TOKENS);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const result = await response.json();
      
      if (result.success && result.data) {

        const parsedTokens = await parseTokensData(result.data);
        setTokens(parsedTokens);
      } else {
        setTokens([]);
      }
    } catch {
      setTokens([]);
    } finally {
      setIsLoading(false);
    }
  }, []);

  const parseTokensData = useCallback(async (rawTokens: any[]): Promise<any[]> => {
    const parsedTokens: any[] = [];

    for (const token of rawTokens) {
      try {
        if (!token.ip || !token.userAgent || !token.userUUID) {
          continue;
        }

        const { device, browser } = parseUserAgent(token.userAgent);
        const location = await getLocationFromIP(token.ip);
        const isCurrent = false;
        const lastActivity = formatLastActivity(token.lastActivity);

        const parsedToken = {
          ip: token.ip,
          userUUID: token.userUUID,
          userAgent: token.userAgent,
          lastActivity: token.lastActivity,
          device,
          browser,
          location,
          isCurrent,
          formattedLastActivity: lastActivity
        };

        parsedTokens.push(parsedToken);
      } catch {
        parsedTokens.push({
          ip: token.ip || 'Unknown',
          userUUID: token.userUUID || 'Unknown',
          userAgent: token.userAgent || 'Unknown',
          lastActivity: token.lastActivity || 0,
          device: 'Unknown',
          browser: 'Unknown',
          location: 'Unknown',
          isCurrent: false,
          formattedLastActivity: 'Unknown'
        });
      }
    }

    return parsedTokens;
  }, []);

  const parseUserAgent = (userAgent: string): { device: string; browser: string } => {
    const ua = userAgent.toLowerCase();

    let device = 'Unknown';
    if (ua.includes('mobile') || ua.includes('android') || ua.includes('iphone')) {
      device = 'Mobile';
    } else if (ua.includes('tablet') || ua.includes('ipad')) {
      device = 'Tablet';
    } else if (ua.includes('windows') || ua.includes('macintosh') || ua.includes('linux')) {
      device = 'Desktop';
    } else if (ua.includes('laptop')) {
      device = 'Laptop';
    }

    let browser = 'Unknown';
    if (ua.includes('chrome')) {
      browser = 'Chrome';
    } else if (ua.includes('firefox')) {
      browser = 'Firefox';
    } else if (ua.includes('safari') && !ua.includes('chrome')) {
      browser = 'Safari';
    } else if (ua.includes('edge')) {
      browser = 'Edge';
    } else if (ua.includes('opera')) {
      browser = 'Opera';
    }

    return { device, browser };
  };

  const getLocationFromIP = async (ip: string): Promise<string> => {
    try {
      if (ip.startsWith('192.168.') || ip.startsWith('10.') || ip.startsWith('172.') || ip === 'localhost' || ip === '127.0.0.1') {
        return 'Local Network';
      }

      const response = await fetch(`https://ipapi.co/${ip}/json/`);
      if (!response.ok) {
        return 'Unknown Location';
      }

      const data = await response.json();
      if (data.city && data.country_name) {
        return `${data.city}, ${data.country_name}`;
      } else if (data.country_name) {
        return data.country_name;
      } else if (data.region) {
        return data.region;
      } else {
        return 'Unknown Location';
      }
    } catch {
      return 'Unknown Location';
    }
  };

  const formatLastActivity = (timestamp: number): string => {
    if (!timestamp) return 'Never';

    const now = Date.now();
    const diff = now - timestamp;
    const minutes = Math.floor(diff / (1000 * 60));
    const hours = Math.floor(diff / (1000 * 60 * 60));
    const days = Math.floor(diff / (1000 * 60 * 60 * 24));

    if (minutes < 1) return 'Just now';
    if (minutes < 60) return `${minutes}m ago`;
    if (hours < 24) return `${hours}h ago`;
    if (days < 7) return `${days}d ago`;

    return new Date(timestamp).toLocaleDateString();
  };

  const handleRevokeSession = async (tokenValue: string) => {
    try {
      const response = await fetch(API_ENDPOINTS.USER.TOKEN_DELETE(tokenValue), {
        method: 'DELETE',
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      setTokens(prev => prev.filter(token => token.value !== tokenValue));

      await fetchTokens();
    } catch {}
  };

  const handleChangePassword = () => {
    setIsPasswordModalOpen(true);
  };

  const handlePasswordChangeSubmit = async () => {
    if (!newPassword.trim() || !confirmPassword.trim()) {
      return;
    }

    if (newPassword !== confirmPassword) {
      return;
    }

    if (newPassword.length < 8) {
      return;
    }

    setIsChangingPassword(true);
    try {

      const response = await fetch(API_ENDPOINTS.USER.CHANGE_PASSWORD, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          newPassword: newPassword.trim()
        })
      });

      const result = await response.json();

      if (result.success) {

        setIsPasswordModalOpen(false);
        setNewPassword('');
        setConfirmPassword('');


        alert('Password changed successfully! You will be logged out in 3 seconds...');

        setTimeout(() => {

          document.cookie = 'token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';

          window.location.href = '/login';
        }, 3000);
      } else {

        alert(`Failed to change password: ${result.message}`);
      }
    } catch {
      alert('Failed to change password. Please try again.');
    } finally {
      setIsChangingPassword(false);
    }
  };

  const handlePasswordModalClose = () => {
    setIsPasswordModalOpen(false);
    setNewPassword('');
    setConfirmPassword('');
    setShowNewPassword(false);
    setShowConfirmPassword(false);
  };

  const getDeviceIcon = (device: string) => {
    switch (device.toLowerCase()) {
      case 'desktop':
        return <Monitor className="h-4 w-4"/>;
      case 'laptop':
        return <Laptop className="h-4 w-4"/>;
      case 'mobile':
        return <Smartphone className="h-4 w-4"/>;
      case 'tablet':
        return <Tablet className="h-4 w-4"/>;
      default:
        return <Globe className="h-4 w-4"/>;
    }
  };

  return (
    <div className="space-y-6">
      <div>
        <h3 className="text-2xl font-bold mb-2">Security Settings</h3>
        <p className="text-muted-foreground">
          Manage your account security and active sessions
        </p>
      </div>

      <div className="bg-card border border-border rounded-lg p-6">
        <h4 className="text-lg font-semibold mb-4 flex items-center gap-2">
          <Lock className="h-5 w-5"/>
          Change Password
        </h4>
        <div className="space-y-4">
          <p className="text-sm text-muted-foreground">
            Update your account password to keep your account secure. Your new password must be at least 8
            characters long.
          </p>
          <Button
            onClick={handleChangePassword}
            className="w-full"
          >
            <Lock className="h-4 w-4 mr-2"/>
            Change Password
          </Button>
        </div>
      </div>

      <div className="bg-card border border-border rounded-lg p-6">
        <div className="flex items-center justify-between mb-4">
          <h4 className="text-lg font-semibold">Active Sessions</h4>
          <Button
            variant="outline"
            size="sm"
            onClick={fetchTokens}
            disabled={isLoading}
          >
            {isLoading ? 'Refreshing...' : 'Refresh'}
          </Button>
        </div>

        {isLoading ? (
          <div className="flex items-center justify-center py-8">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
          </div>
        ) : tokens.length === 0 ? (
          <div className="text-center py-8 text-muted-foreground">
            <p>No active sessions found</p>
          </div>
        ) : (
          <div className={`space-y-3 ${tokens.length > 2 ? 'max-h-96 overflow-y-auto pr-2 scrollbar-thin scrollbar-track-muted/30 scrollbar-thumb-muted-foreground/40 hover:scrollbar-thumb-muted-foreground/60' : ''}`}>
            {tokens.map((token) => (
              <div key={token.ip}
                   className="bg-card border border-border rounded-lg p-4 hover:bg-muted/20 transition-colors">
                <div className="flex items-start justify-between">
                  <div className="flex items-center space-x-3">
                    <div className="flex items-center space-x-2">
                      <div className="p-2 bg-primary/10 rounded-lg">
                        {getDeviceIcon(token.device)}
                      </div>
                      <div className="flex flex-col">
                        <span className="text-sm font-semibold">{token.device}</span>
                        <span className="text-xs text-muted-foreground">{token.browser}</span>
                      </div>
                    </div>

                    {token.isCurrent && (
                      <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800 border border-green-200">
                        Current Session
                      </span>
                    )}
                  </div>

                  {!token.isCurrent && (
                    <Button
                      variant="ghost"
                      size="sm"
                      className="p-2 h-8 w-8 text-red-500 hover:text-red-600 hover:bg-red-50/50"
                      onClick={() => handleRevokeSession(token.userUUID)}
                      title="Revoke this session"
                    >
                      <Trash2 className="h-4 w-4"/>
                    </Button>
                  )}
                </div>

                <div className="mt-3 pt-3 border-t border-border/50">
                  <div className="grid grid-cols-2 gap-4 text-sm">
                    <div>
                      <span className="text-muted-foreground">Location:</span>
                      <div className="font-medium">{token.location}</div>
                    </div>
                    <div>
                      <span className="text-muted-foreground">Last Online:</span>
                      <div className="font-medium">{token.formattedLastActivity}</div>
                    </div>
                    <div className="col-span-2">
                      <span className="text-muted-foreground">IP Address:</span>
                      <div
                        className="font-mono text-xs cursor-pointer hover:text-foreground transition-colors mt-1"
                        onClick={() => toggleIPVisibility(token.ip)}
                        title="Click to reveal/hide IP"
                      >
                        <span
                          className={`transition-all duration-200 ${
                            revealedIPs.has(token.ip)
                              ? 'blur-none'
                              : 'blur-sm'
                          }`}
                        >
                          {token.ip}
                        </span>
                      </div>
                    </div>
                    <div className="col-span-2">
                      <span className="text-muted-foreground">User UUID:</span>
                      <div className="font-mono text-xs mt-1">{token.userUUID}</div>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      <Dialog open={isPasswordModalOpen} onOpenChange={handlePasswordModalClose}>
        <DialogContent className="sm:max-w-md">
          <DialogHeader>
            <DialogTitle className="flex items-center gap-2">
              <Lock className="h-5 w-5"/>
              Change Password
            </DialogTitle>
            <DialogDescription>
              Update your account password to keep your account secure. You will be automatically logged
              out after changing your password.
            </DialogDescription>
          </DialogHeader>
          <div className="space-y-4 py-4">
            <div className="space-y-2">
              <Label htmlFor="new-password" className="text-sm font-medium">
                New Password <span className="text-red-500">*</span>
              </Label>
              <div className="relative">
                <Input
                  id="new-password"
                  type={showNewPassword ? "text" : "password"}
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  placeholder="Enter new password"
                  required
                  className="pr-10"
                />
                <Button
                  type="button"
                  variant="ghost"
                  size="sm"
                  className="absolute right-0 top-0 h-full px-3 hover:bg-transparent"
                  onClick={() => setShowNewPassword(!showNewPassword)}
                  title={showNewPassword ? "Hide password" : "Show password"}
                >
                  {showNewPassword ? <EyeOff className="h-4 w-4"/> : <Eye className="h-4 w-4"/>}
                </Button>
              </div>
              <p className="text-xs text-muted-foreground">
                Password must be at least 8 characters long
              </p>
            </div>

            <div className="space-y-2">
              <Label htmlFor="confirm-password" className="text-sm font-medium">
                Confirm New Password <span className="text-red-500">*</span>
              </Label>
              <div className="relative">
                <Input
                  id="confirm-password"
                  type={showConfirmPassword ? "text" : "password"}
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  placeholder="Confirm new password"
                  required
                  className="pr-10"
                />
                <Button
                  type="button"
                  variant="ghost"
                  size="sm"
                  className="absolute right-0 top-0 h-full px-3 hover:bg-transparent"
                  onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                  title={showConfirmPassword ? "Hide password" : "Show password"}
                >
                  {showConfirmPassword ? <EyeOff className="h-4 w-4"/> : <Eye className="h-4 w-4"/>}
                </Button>
              </div>
              {confirmPassword && newPassword !== confirmPassword && (
                <p className="text-xs text-red-500">
                  Passwords do not match
                </p>
              )}
            </div>
          </div>
          <div className="flex gap-3">
            <Button
              variant="outline"
              onClick={handlePasswordModalClose}
              className="flex-1"
              disabled={isChangingPassword}
            >
              Cancel
            </Button>
            <Button
              onClick={handlePasswordChangeSubmit}
              className="flex-1"
              disabled={isChangingPassword || !newPassword.trim() || !confirmPassword.trim() || newPassword !== confirmPassword || newPassword.length < 8}
            >
              {isChangingPassword ? (
                <>
                  <div
                    className="w-4 h-4 border-2 border-current border-t-transparent rounded-full animate-spin mr-2"/>
                  Changing...
                </>
              ) : (
                'Change Password'
              )}
            </Button>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  );
};

const GeneralTab = ({ user, skinHeadUrl, onUserUpdate }: {
  user: UserData;
  skinHeadUrl: string;
  onUserUpdate?: (newUsername: string) => void;
}) => {
  const [isEditingUsername, setIsEditingUsername] = useState(false);
  const [newUsername, setNewUsername] = useState(user.username);
  const [isSaving, setIsSaving] = useState(false);

  const handleSaveUsername = async () => {
    if (!newUsername.trim() || newUsername.trim() === user.username) {
      setIsEditingUsername(false);
      setNewUsername(user.username);
      return;
    }

    if (newUsername.trim().length < 3) {
      return;
    }

    setIsSaving(true);
    try {

      await new Promise(resolve => setTimeout(resolve, 2000));
      setIsEditingUsername(false);
      if (onUserUpdate) {
        onUserUpdate(newUsername.trim());
      }
    } catch {
    } finally {
      setIsSaving(false);
    }
  };

  const handleCancelEdit = () => {
    setIsEditingUsername(false);
    setNewUsername(user.username);
  };

  return (
    <div className="space-y-6">
      <div>
        <h3 className="text-2xl font-bold mb-2">Account Information</h3>
        <p className="text-muted-foreground">
          Manage your basic account details and profile information
        </p>
      </div>

      <div className="bg-card border border-border rounded-lg p-6">
        <div className="flex items-center space-x-4 mb-6">
          <div className="relative">
            <Image
              src={skinHeadUrl}
              alt={`${user.username}'s Minecraft skin`}
              width={80}
              height={80}
              className="h-20 w-20 rounded-xl border-2 border-border/50 shadow-lg"
              onError={(e) => {
                e.currentTarget.src = 'https://mineskin.eu/helm/Steve/32.png';
              }}
            />
            <div
              className="absolute -bottom-1 -right-1 bg-green-500 w-4 h-4 rounded-full border-2 border-background"></div>
          </div>
          <div>
            <h4 className="text-lg font-semibold">{user.username}</h4>
            <p className="text-muted-foreground font-mono text-sm">{user.userUUID}</p>
            <span
              className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800 mt-2">
              Online
            </span>
          </div>
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div className="space-y-2">
            <Label className="text-sm font-medium text-muted-foreground">Username</Label>
            <div className="flex items-center space-x-2">
              {isEditingUsername ? (
                <>
                  <Input
                    type="text"
                    value={newUsername}
                    onChange={(e) => setNewUsername(e.target.value)}
                    className="flex-1"
                    placeholder="Enter new username"
                    disabled={isSaving}
                  />
                  <Button
                    size="sm"
                    onClick={handleSaveUsername}
                    disabled={isSaving || !newUsername.trim() || newUsername.trim() === user.username}
                    className="px-3"
                  >
                    {isSaving ? (
                      <div
                        className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
                    ) : (
                      <Save className="h-4 w-4"/>
                    )}
                  </Button>
                  <Button
                    size="sm"
                    variant="outline"
                    onClick={handleCancelEdit}
                    disabled={isSaving}
                    className="px-3"
                  >
                    <X className="h-4 w-4"/>
                  </Button>
                </>
              ) : (
                <>
                  <Input
                    type="text"
                    value={user.username}
                    className="flex-1 disabled:opacity-50"
                    disabled
                  />
                  <Button
                    size="sm"
                    variant="outline"
                    onClick={() => setIsEditingUsername(true)}
                    className="px-3"
                  >
                    <Edit className="h-4 w-4"/>
                  </Button>
                </>
              )}
            </div>
            {isEditingUsername && (
              <p className="text-xs text-muted-foreground">
                Username must be at least 3 characters long
              </p>
            )}
          </div>
          <div className="space-y-2">
            <Label className="text-sm font-medium text-muted-foreground">Role</Label>
            <div className="flex items-center space-x-2">
              {user.role ? (
                <Badge
                  variant="secondary"
                  className="px-3 py-2 text-sm font-medium"
                  style={{
                    backgroundColor: user.role.hexColor + '20',
                    color: user.role.hexColor,
                    border: `1px solid ${user.role.hexColor}40`
                  }}
                >
                  {user.role.label}
                </Badge>
              ) : (
                <span
                  className="px-3 py-2 text-sm text-muted-foreground bg-muted/30 border border-border rounded-md">
                  Admin
                </span>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

const AppearanceTab = () => {
  const [selectedTheme, setSelectedTheme] = useState('dark');

  const themes = [
    { 
      id: 'dark', 
      name: 'Dark', 
      icon: Moon,
      description: 'Standard dark theme',
      colors: ['#0f0f0f', '#1f1f1f', '#ffffff']
    },
    { 
      id: 'darker', 
      name: 'Darker', 
      icon: Sparkles,
      description: 'Ultra dark theme',
      colors: ['#000000', '#080808', '#ffffff']
    },
    { 
      id: 'ocean', 
      name: 'Ocean', 
      icon: Globe,
      description: 'Dark blue ocean theme',
      colors: ['#0a1a2a', '#1a2a3a', '#00d4ff']
    },
    { 
      id: 'purple', 
      name: 'Purple', 
      icon: Palette,
      description: 'Elegant purple theme',
      colors: ['#1a0a2a', '#2a1a3a', '#a855f7']
    },
    { 
      id: 'forest', 
      name: 'Forest', 
      icon: TreePine,
      description: 'Dark green nature theme',
      colors: ['#0a2a0a', '#1a3a1a', '#22c55e']
    },
    { 
      id: 'sunset', 
      name: 'Sunset', 
      icon: Flame,
      description: 'Warm orange-red theme',
      colors: ['#2a0a0a', '#3a1a1a', '#f97316']
    }
  ];

  const applyTheme = (themeId: string) => {

    document.documentElement.classList.remove('dark', 'darker', 'ocean', 'purple', 'forest', 'sunset', 'light');

    if (themeId !== 'light' && themeId !== 'custom') {
      document.documentElement.classList.add(themeId);
    }

    localStorage.setItem('theme', themeId);

    setSelectedTheme(themeId);

    window.dispatchEvent(new CustomEvent('theme-changed', { detail: { theme: themeId } }));
  };

  useEffect(() => {
    const savedTheme = localStorage.getItem('theme') || 'dark';
    applyTheme(savedTheme);
  }, []);

  return (
    <div className="space-y-6">
      <div>
        <h3 className="text-2xl font-bold mb-2">Theme Selection</h3>
        <p className="text-muted-foreground">
          Choose your preferred theme. Dark and Darker themes are available.
        </p>
      </div>

      {}
      <div className="grid grid-cols-2 gap-4">
        {themes.map((theme) => (
          <div
            key={theme.id}
            className={`relative p-4 rounded-xl border-2 transition-all duration-300 cursor-pointer group ${
              selectedTheme === theme.id
                ? 'border-primary bg-primary/10 shadow-lg shadow-primary/20'
                : 'border-border bg-muted/20 hover:border-primary/50 hover:bg-primary/5 hover:shadow-md'
            }`}
            onClick={() => {
              if (theme.id === 'dark' || theme.id === 'darker' || theme.id === 'ocean' || theme.id === 'purple' || theme.id === 'forest' || theme.id === 'sunset') {
                applyTheme(theme.id);
              }
            }}
          >
            {}
            <div className="flex h-2 mb-3 rounded-full overflow-hidden">
              {theme.colors.map((color, index) => (
                <div 
                  key={index} 
                  className="flex-1" 
                  style={{ backgroundColor: color }}
                />
              ))}
            </div>

            {}
            <div className="flex items-center gap-3">
              <div className={`p-3 rounded-lg transition-all duration-200 ${
                selectedTheme === theme.id
                  ? 'bg-primary/20 border border-primary/30'
                  : 'bg-muted/30 border border-border/30 group-hover:bg-primary/10 group-hover:border-primary/20'
              }`}>
                <theme.icon className={`w-5 h-5 transition-colors duration-200 ${
                  selectedTheme === theme.id
                    ? 'text-primary'
                    : 'text-muted-foreground group-hover:text-primary'
                }`} />
              </div>
              
              <div className="flex-1 min-w-0">
                <h4 className={`font-semibold transition-colors duration-200 ${
                  selectedTheme === theme.id
                    ? 'text-primary'
                    : 'text-foreground group-hover:text-primary'
                }`}>
                  {theme.name}
                </h4>
                <p className="text-sm text-muted-foreground truncate">
                  {theme.description}
                </p>
              </div>

              {}
              {selectedTheme === theme.id && (
                <div className="w-3 h-3 bg-primary rounded-full animate-pulse" />
              )}
            </div>

            {}
            <div className={`absolute inset-0 rounded-xl transition-all duration-300 ${
              selectedTheme === theme.id
                ? 'ring-2 ring-primary/20'
                : 'group-hover:ring-2 group-hover:ring-primary/10'
            }`} />
          </div>
        ))}
      </div>

      {}
      <div className="p-4 rounded-lg border border-border bg-muted/20">
        <h4 className="text-lg font-semibold mb-2 flex items-center gap-2">
          <Monitor className="h-5 w-5" />
          Current Theme: {selectedTheme === 'dark' ? 'Dark' : selectedTheme === 'darker' ? 'Darker' : selectedTheme === 'ocean' ? 'Ocean' : selectedTheme === 'purple' ? 'Purple' : selectedTheme === 'forest' ? 'Forest' : selectedTheme === 'sunset' ? 'Sunset' : 'Dark'}
        </h4>
        <p className="text-sm text-muted-foreground">
          {selectedTheme === 'dark' 
            ? 'The Dark theme is currently active.' 
            : selectedTheme === 'darker'
            ? 'The Darker theme is currently active.'
            : selectedTheme === 'ocean'
            ? 'The Ocean theme is currently active.'
            : selectedTheme === 'purple'
            ? 'The Purple theme is currently active.'
            : selectedTheme === 'forest'
            ? 'The Forest theme is currently active.'
            : selectedTheme === 'sunset'
            ? 'The Sunset theme is currently active.'
            : 'The Dark theme is currently active.'
          } Other themes will be available in future updates.
        </p>
      </div>
    </div>
  );
};

const SidebarFooterComponent = ({ userData, onSettingsClick, onLogout }: { 
  userData: UserData; 
  onSettingsClick: () => void; 
  onLogout: () => void; 
}) => (
  <SidebarFooter className="border-t border-sidebar-border p-3">
    <div className="flex items-center space-x-3">
      <div className="w-8 h-8 rounded-lg overflow-hidden">
        <Image 
          src={`https://mineskin.eu/helm/${userData.username}/64`}
          alt={`${userData.username} Minecraft Head`}
          width={32}
          height={32}
          className="w-8 h-8"
          onError={(e) => {
            e.currentTarget.src = 'https://mineskin.eu/helm/MHF_Question/64';
          }}
        />
      </div>
      <div className="flex-1 min-w-0">
        <p className="text-sm font-medium text-sidebar-foreground truncate">
          {userData.username}
        </p>
        <div 
          className="px-1.5 py-0.5 text-xs rounded-full mt-1 inline-block"
          style={{
            backgroundColor: userData.role ? `${userData.role.hexColor}20` : 'rgba(239, 68, 68, 0.2)',
            color: userData.role ? userData.role.hexColor : '#f87171'
          }}
        >
          {userData.role ? userData.role.label : 'Admin'}
        </div>
      </div>
      <div className="flex-shrink-0 flex gap-1">
        <button 
          onClick={onSettingsClick}
          className="w-7 h-7 text-sidebar-foreground/60 hover:text-sidebar-foreground/80 hover:bg-sidebar-accent/50 rounded-md transition-all duration-200 p-1"
          title="Settings"
        >
          <Settings className="w-4 h-4" />
        </button>
        <button 
          onClick={onLogout}
          className="w-7 h-7 text-sidebar-foreground/60 hover:text-red-400 hover:bg-red-500/10 rounded-md transition-all duration-200 p-1"
          title="Sign Out"
        >
          <LogOut className="w-4 h-4" />
        </button>
      </div>
    </div>
  </SidebarFooter>
);

export function AppSidebar() {
  const [userData, setUserData] = useState<UserData | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isSettingsOpen, setIsSettingsOpen] = useState(false);
  const [isChangePasswordOpen, setIsChangePasswordOpen] = useState(false);

  const [changePasswordData, setChangePasswordData] = useState<ChangePasswordData>({
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  });
  const [isChangingPassword, setIsChangingPassword] = useState(false);
  const router = useRouter();

  useEffect(() => {

    const loadDataImmediately = () => {
      try {
        const adminUsername = localStorage.getItem('adminUsername');
        const isLoggedIn = localStorage.getItem('isLoggedIn');
        
        if (adminUsername && isLoggedIn === 'true') {

          const immediateData = {
            username: adminUsername,
            userUUID: 'admin-' + Date.now(),
            role: {
              id: -1,
              label: 'Admin',
              hexColor: '#dc2626'
            }
          };
          setUserData(immediateData);
          setIsLoading(false);
        } else {
          setUserData({ username: 'Guest', userUUID: '', role: null });
          setIsLoading(false);
        }
      } catch {
        setUserData({ username: 'Guest', userUUID: '', role: null });
        setIsLoading(false);
      }
    };

    loadDataImmediately();

    const loadDataAsync = async () => {
      try {
        const data = await loadUserData();
        setUserData(data);
      } catch {}
    };

    setTimeout(loadDataAsync, 100);

    const interval = setInterval(async () => {
      try {
        const response = await fetch(API_ENDPOINTS.AUTH.ME);
        if (!response.ok) {
          resetUserDataCache();

        }
      } catch  {}
    }, 5 * 60 * 1000);

    const handleUserLogin = () => {

      resetUserDataCache();

      loadDataAsync();
    };

    window.addEventListener('user-logged-in', handleUserLogin as EventListener);

    return () => {
      clearInterval(interval);
      window.removeEventListener('user-logged-in', handleUserLogin as EventListener);
    };
  }, []);

  useEffect(() => {
    if (isSettingsOpen) {
      loadSessions();
    }
  }, [isSettingsOpen]);

  const loadSessions = async () => {
    try {
      const response = await fetch(API_ENDPOINTS.USER.TOKENS);
      if (response.ok) {

      } else {

      }
    } catch  {}
  };

  const handleLogout = async () => {
    try {
      const response = await fetch(API_ENDPOINTS.AUTH.LOGOUT, { method: 'POST' });
      if (response.ok) {
        resetUserDataCache();
        setUserData({ username: 'Guest', userUUID: '', role: null });
        router.push('/login');
      }
    } catch {}
  };

  const handleChangePassword = async () => {
    if (changePasswordData.newPassword !== changePasswordData.confirmPassword) {
      alert('New passwords do not match');
      return;
    }

    if (changePasswordData.newPassword.length < 6) {
      alert('New password must be at least 6 characters long');
      return;
    }

    setIsChangingPassword(true);
    try {
      const response = await fetch(API_ENDPOINTS.USER.CHANGE_PASSWORD, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ newPassword: changePasswordData.newPassword })
      });

      if (response.ok) {
        alert('Password changed successfully');
        setIsChangePasswordOpen(false);
        setChangePasswordData({ currentPassword: '', newPassword: '', confirmPassword: '' });
      } else {
        const errorData = await response.json();
        alert(errorData.error || 'Failed to change password');
      }
    } catch {
      alert('Failed to change password');
    } finally {
      setIsChangingPassword(false);
    }
  };


  const handlePasswordDataChange = (field: keyof ChangePasswordData, value: string) => {
    setChangePasswordData(prev => ({ ...prev, [field]: value }));
  };

  if (isLoading || !userData) {
    return (
      <Sidebar className="border-r border-sidebar-border bg-sidebar">
        <SidebarHeaderComponent />
        <SidebarContent className="px-2 py-3">
          <CloudNavigation />
          <TeamNavigation />
        </SidebarContent>
        <SidebarFooter className="border-t border-sidebar-border p-3">
          <div className="flex items-center space-x-3">
            <div className="w-8 h-8 rounded-lg overflow-hidden">
              <div className="w-8 h-8 bg-sidebar-accent animate-pulse rounded-lg"></div>
            </div>
            <div className="flex-1 min-w-0">
              <div className="w-16 h-4 bg-sidebar-accent animate-pulse rounded"></div>
              <div className="w-12 h-3 bg-sidebar-accent animate-pulse rounded mt-1"></div>
            </div>
            <div className="flex-shrink-0">
              <Settings className="w-4 h-4 text-sidebar-foreground/60" />
            </div>
          </div>
        </SidebarFooter>
      </Sidebar>
    );
  }

  return (
    <>
      <Sidebar className="border-r border-sidebar-border bg-sidebar">
        <SidebarHeaderComponent />
        <SidebarContent className="px-2 py-3">
          <CloudNavigation />
          <TeamNavigation />
        </SidebarContent>
        <SidebarFooterComponent 
          userData={userData}
          onSettingsClick={() => setIsSettingsOpen(true)}
          onLogout={handleLogout}
        />
      </Sidebar>

      <SettingsDialog 
        isOpen={isSettingsOpen}
        onClose={() => setIsSettingsOpen(false)}
        userData={userData}
      />

      <ChangePasswordDialog 
        isOpen={isChangePasswordOpen}
        onClose={() => setIsChangePasswordOpen(false)}
        onSubmit={handleChangePassword}
        data={changePasswordData}
        onChange={handlePasswordDataChange}
        isLoading={isChangingPassword}
      />
    </>
  );
}

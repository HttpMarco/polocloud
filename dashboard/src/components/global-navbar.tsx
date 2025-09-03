'use client';

import { usePathname } from 'next/navigation';
import { 
  Breadcrumb, 
  BreadcrumbItem, 
  BreadcrumbLink, 
  BreadcrumbList, 
  BreadcrumbPage, 
  BreadcrumbSeparator 
} from '@/components/ui/breadcrumb';
import SystemInfoBadges from '@/components/system-info-badges';
import { Home, Server, Users, User, Shield, FileText, Terminal, Settings, Building2, ChevronRightIcon } from 'lucide-react';
import { motion } from 'framer-motion';

export default function GlobalNavbar() {
  const pathname = usePathname();

  const getBreadcrumbs = () => {
    const segments = pathname.split('/').filter(Boolean);
    
    if (segments.length === 0) return null;
    
    const breadcrumbs = [];

    breadcrumbs.push({ 
      label: 'Dashboard', 
      href: '/', 
      isPage: false, 
      icon: <Home className="w-4 h-4" />
    });

    if (segments[0] === 'services') {
      breadcrumbs.push({ 
        label: 'Services', 
        href: '/services', 
        isPage: false, 
        icon: <Server className="w-4 h-4" />
      });
      
      if (segments[1]) {
        if (segments[2] === 'screen') {
          breadcrumbs.push({ 
            label: segments[1], 
            href: `/services/${segments[1]}`, 
            isPage: false 
          });
          breadcrumbs.push({ 
            label: 'Screen', 
            href: pathname, 
            isPage: true 
          });
        } else {
          breadcrumbs.push({ 
            label: segments[1], 
            href: pathname, 
            isPage: true 
          });
        }
      }
    }

    if (segments[0] === 'groups') {
      breadcrumbs.push({ 
        label: 'Groups', 
        href: '/groups', 
        isPage: true, 
        icon: <Building2 className="w-4 h-4" />
      });
      
      if (segments[1] === 'create') {
        breadcrumbs.push({ 
          label: 'Create Group', 
          href: pathname, 
          isPage: true 
        });
      } else if (segments[1] && segments[1] !== 'create') {
        if (segments[2] === 'edit') {
          breadcrumbs.push({ 
            label: segments[1], 
            href: `/groups/${segments[1]}`, 
            isPage: false 
          });
          breadcrumbs.push({ 
            label: 'Edit', 
            href: pathname, 
            isPage: true 
          });
        } else {
          breadcrumbs.push({ 
            label: segments[1], 
            href: pathname, 
            isPage: true 
          });
        }
      }
    }

    if (segments[0] === 'users') {
      breadcrumbs.push({ 
        label: 'Users', 
        href: '/users', 
        isPage: true, 
        icon: <Users className="w-4 h-4" />
      });
    }

    if (segments[0] === 'players') {
      breadcrumbs.push({ 
        label: 'Players', 
        href: '/players', 
        isPage: false, 
        icon: <User className="w-4 h-4" />
      });
      
      if (segments[1]) {
        breadcrumbs.push({ 
          label: segments[1], 
          href: pathname, 
          isPage: true 
        });
      }
    }

    if (segments[0] === 'roles') {
      breadcrumbs.push({ 
        label: 'Roles', 
        href: '/roles', 
        isPage: true, 
        icon: <Shield className="w-4 h-4" />
      });
    }

    if (segments[0] === 'templates') {
      breadcrumbs.push({ 
        label: 'Templates', 
        href: '/templates', 
        isPage: true, 
        icon: <FileText className="w-4 h-4" />
      });
    }

    if (segments[0] === 'terminal') {
      breadcrumbs.push({ 
        label: 'Terminal', 
        href: '/terminal', 
        isPage: true, 
        icon: <Terminal className="w-4 h-4" />
      });
    }

    if (segments[0] === 'settings') {
      breadcrumbs.push({ 
        label: 'Settings', 
        href: '/settings', 
        isPage: true, 
        icon: <Settings className="w-4 h-4" />
      });
    }
    
    return breadcrumbs;
  };

  const breadcrumbs = getBreadcrumbs();

  if (pathname === '/onboarding' || pathname === '/login') {
    return null;
  }

  return (
    <motion.div 
      className="border-b border-border/50 bg-background/80 backdrop-blur-sm sticky top-0 z-50"
      initial={{ opacity: 0, y: -20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.3 }}
    >
      <div className="w-full px-6 py-3">
        <div className="flex items-center justify-between w-full">
          <div className="flex-shrink-0">
            {breadcrumbs && breadcrumbs.length > 0 ? (
              <Breadcrumb>
                <BreadcrumbList className="bg-card/50 rounded-lg border border-border/30 px-4 py-2 shadow-sm backdrop-blur-sm">
                  {breadcrumbs.map((crumb, index) => (
                    <div key={index} className="flex items-center">
                      {index > 0 && (
                        <BreadcrumbSeparator className="mx-2 text-muted-foreground/50">
                          <ChevronRightIcon className="w-3 h-3" />
                        </BreadcrumbSeparator>
                      )}
                      {crumb.isPage ? (
                        <BreadcrumbItem>
                          <BreadcrumbPage className="flex items-center gap-2 text-foreground font-medium">
                            {crumb.icon && (
                              <span className="text-white">
                                {crumb.icon}
                              </span>
                            )}
                            {crumb.label}
                          </BreadcrumbPage>
                        </BreadcrumbItem>
                      ) : (
                        <BreadcrumbItem>
                          <BreadcrumbLink 
                            href={crumb.href}
                            className="flex items-center gap-2 text-white hover:text-foreground transition-colors duration-200 font-medium"
                          >
                            {crumb.icon && (
                              <span className="text-white">
                                {crumb.icon}
                              </span>
                            )}
                            {crumb.label}
                          </BreadcrumbLink>
                        </BreadcrumbItem>
                      )}
                    </div>
                  ))}
                </BreadcrumbList>
              </Breadcrumb>
            ) : (
              <div className="flex items-center gap-2 text-white bg-card/50 rounded-lg border border-border/30 px-4 py-2">
                <Home className="w-4 h-4 text-white" />
                <span className="text-sm">Dashboard</span>
              </div>
            )}
          </div>
          
          <motion.div
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.3, delay: 0.1 }}
            className="flex-shrink-0"
          >
            <SystemInfoBadges />
          </motion.div>
        </div>
      </div>
    </motion.div>
  );
}

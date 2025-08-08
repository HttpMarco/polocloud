'use client';

import { ChevronDown, Home, Info, Server, Shield, Users } from 'lucide-react';
import { useRouter, usePathname } from 'next/navigation';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';

interface HomeSection {
  label: string;
  href: string;
  icon?: React.ReactNode;
  description: string;
}

const homeSections: HomeSection[] = [
  {
    label: 'About',
    href: '#about',
    icon: <Info className="w-4 h-4" />,
    description: 'Learn about PoloCloud and its mission',
  },
  {
    label: 'Platforms',
    href: '#platforms',
    icon: <Server className="w-4 h-4" />,
    description: 'Supported platforms and environments',
  },
  {
    label: 'Addons',
    href: '#addons',
    icon: <Shield className="w-4 h-4" />,
    description: 'Additional plugins and extensions',
  },
  {
    label: 'Partners',
    href: '#partners',
    icon: <Users className="w-4 h-4" />,
    description: 'Our trusted partners and integrations',
  },
];

export function HomeDropdown() {
  const router = useRouter();
  const pathname = usePathname();

  const scrollToSection = (href: string) => {
    const id = href.replace('#', '');

    const isOnHomePage = pathname === '/';
    
    if (!isOnHomePage) {
      router.push(`/${href}`);
      return;
    }

    setTimeout(() => {
      const element = document.getElementById(id);
      if (element) {
        const navbarHeight = 64;
        const elementPosition = element.offsetTop - navbarHeight;
        
        window.scrollTo({
          top: elementPosition,
          behavior: 'smooth',
        });
        
        console.log(`Scrolling to section: ${id}`);
      } else {
        console.error(`Section with id "${id}" not found`);
        const fallbackElement = document.querySelector(href);
        if (fallbackElement) {
          const navbarHeight = 64;
          const elementPosition = fallbackElement.getBoundingClientRect().top + window.pageYOffset - navbarHeight;
          window.scrollTo({
            top: elementPosition,
            behavior: 'smooth',
          });
        }
      }
    }, 100);
  };

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <button className="flex items-center gap-2 px-4 py-2 text-sm font-medium text-muted-foreground hover:text-foreground transition-all duration-300 hover:scale-105 rounded-lg hover:bg-card/50 backdrop-blur-sm border border-transparent hover:border-border/30">
          <Home className="w-4 h-4" />
          <span>Home</span>
          <ChevronDown className="w-4 h-4 transition-transform duration-300" />
        </button>
      </DropdownMenuTrigger>
      <DropdownMenuContent 
        align="start" 
        className="w-[500px] bg-card/95 backdrop-blur-md border border-border/50 rounded-xl shadow-xl p-4"
        sideOffset={8}
      >
        <div className="mb-4">
          <h3 className="text-lg font-bold text-foreground mb-2">PoloCloud</h3>
          <p className="text-sm text-muted-foreground">
            Deploy and manage your Minecraft servers with ease.
          </p>
        </div>

        <div className="grid grid-cols-2 gap-3">
          {homeSections.map((section, index) => (
            <DropdownMenuItem key={index} asChild className="p-0">
              <button
                onClick={() => scrollToSection(section.href)}
                className="flex flex-col items-start gap-2 p-3 rounded-lg hover:bg-primary/10 transition-all duration-300 cursor-pointer group text-left w-full"
              >
                <div className="flex items-center gap-2">
                  <div className="text-primary group-hover:scale-110 transition-transform duration-300">
                    {section.icon}
                  </div>
                  <span className="font-medium text-foreground">{section.label}</span>
                </div>
                <p className="text-sm text-muted-foreground leading-relaxed">
                  {section.description}
                </p>
              </button>
            </DropdownMenuItem>
          ))}
        </div>
      </DropdownMenuContent>
    </DropdownMenu>
  );
} 
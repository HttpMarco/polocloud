'use client'

import { SidebarFooter } from "@/components/ui/sidebar";
import { Settings, LogOut } from "lucide-react";
import Image from "next/image";
import { useState, useEffect } from "react";
import { API_ENDPOINTS } from "@/lib/api";

interface UserData {
  username: string;
  userUUID: string;
  role: { id: number; label: string; hexColor: string } | null;
}

interface SidebarFooterComponentProps {
  userData: UserData;
  onSettingsClick: () => void;
  onLogout: () => void;
}

export function SidebarFooterComponent({ userData, onSettingsClick, onLogout }: SidebarFooterComponentProps) {
  const [version, setVersion] = useState<string>('');

  useEffect(() => {
    const fetchVersion = async () => {
      try {
        const response = await fetch(API_ENDPOINTS.SYSTEM.VERSION);
        if (response.ok) {
          const data = await response.json();
          setVersion(data.version || '');
        }
      } catch {
      }
    };

    fetchVersion();
  }, []);

  return (
    <SidebarFooter className="border-t border-sidebar-border p-3">
      {version && (
        <div className="text-center mb-4">
          <p className="text-xs text-sidebar-foreground/60">
            v{version}
          </p>
        </div>
      )}
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
}

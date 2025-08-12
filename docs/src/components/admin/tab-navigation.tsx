"use client";

import { Star, Users, Handshake, FileText } from 'lucide-react';
import { ActiveTab } from './types';

interface TabNavigationProps {
  activeTab: ActiveTab;
  onTabChange: (tab: ActiveTab) => void;
  feedbacksCount: number;
  adminUsersCount: number;
  partnersCount: number;
  isSuperAdmin: boolean;
}

export function TabNavigation({ 
  activeTab, 
  onTabChange, 
  feedbacksCount, 
  adminUsersCount, 
  partnersCount, 
  isSuperAdmin 
}: TabNavigationProps) {
  return (
    <div className="flex flex-wrap gap-2 mb-8 p-1 bg-muted/30 rounded-lg border border-border/30">
      <button
        onClick={() => onTabChange('feedback')}
        className={`flex items-center gap-2 px-4 py-2 rounded-lg font-medium transition-all duration-200 ${
          activeTab === 'feedback'
            ? 'bg-primary text-primary-foreground shadow-lg'
            : 'text-muted-foreground hover:text-foreground hover:bg-background/50'
        }`}
      >
        <Star className="w-4 h-4" />
        Feedback ({feedbacksCount})
      </button>
      {isSuperAdmin && (
        <button
          onClick={() => onTabChange('users')}
          className={`flex items-center gap-2 px-4 py-2 rounded-lg font-medium transition-all duration-200 ${
            activeTab === 'users'
              ? 'bg-primary text-primary-foreground shadow-lg'
              : 'text-muted-foreground hover:text-foreground hover:bg-background/50'
          }`}
        >
          <Users className="w-4 h-4" />
          Users ({adminUsersCount})
        </button>
      )}
      {isSuperAdmin && (
        <button
          onClick={() => onTabChange('partners')}
          className={`flex items-center gap-2 px-4 py-2 rounded-lg font-medium transition-all duration-200 ${
            activeTab === 'partners'
              ? 'bg-primary text-primary-foreground shadow-lg'
              : 'text-muted-foreground hover:text-foreground hover:bg-background/50'
          }`}
        >
          <Handshake className="w-4 h-4" />
          Partners ({partnersCount})
        </button>
      )}
      <button
        onClick={() => window.open('/admin/blog', '_blank')}
        className="flex items-center gap-2 px-4 py-2 rounded-lg font-medium text-muted-foreground hover:text-foreground hover:bg-background/50 transition-all duration-200"
      >
        <FileText className="w-4 h-4" />
        Blog Management
      </button>
    </div>
  );
}

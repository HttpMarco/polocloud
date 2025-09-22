"use client";

import { Star, Users, Handshake, Server, FileText, GitBranch, Image } from 'lucide-react';
import { ActiveTab } from './types';

interface TabNavigationProps {
  activeTab: ActiveTab;
  onTabChange: (tab: ActiveTab) => void;
  feedbacksCount: number;
  adminUsersCount: number;
  partnersCount: number;
  platformsCount: number;
  changelogCount: number;
  blogCount: number;
  imagesCount: number;
  isSuperAdmin: boolean;
}

export function TabNavigation({ 
  activeTab, 
  onTabChange, 
  feedbacksCount, 
  adminUsersCount, 
  partnersCount, 
  platformsCount, 
  changelogCount,
  blogCount,
  imagesCount,
  isSuperAdmin 
}: TabNavigationProps) {
  return (
    <div className="flex flex-wrap gap-1.5 sm:gap-2 mb-6 sm:mb-8 p-1 bg-muted/30 rounded-lg border border-border/30">
      <button
        onClick={() => onTabChange('feedback')}
        className={`flex items-center gap-1.5 sm:gap-2 px-3 sm:px-4 py-2 rounded-lg font-medium transition-all duration-200 text-xs sm:text-sm ${
          activeTab === 'feedback'
            ? 'bg-primary text-primary-foreground shadow-lg'
            : 'text-muted-foreground hover:text-foreground hover:bg-background/50'
        }`}
      >
        <Star className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
        <span className="hidden sm:inline">Feedback ({feedbacksCount})</span>
        <span className="sm:hidden">FB ({feedbacksCount})</span>
      </button>
      {isSuperAdmin && (
        <button
          onClick={() => onTabChange('users')}
          className={`flex items-center gap-1.5 sm:gap-2 px-3 sm:px-4 py-2 rounded-lg font-medium transition-all duration-200 text-xs sm:text-sm ${
            activeTab === 'users'
              ? 'bg-primary text-primary-foreground shadow-lg'
              : 'text-muted-foreground hover:text-foreground hover:bg-background/50'
          }`}
        >
          <Users className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
          <span className="hidden sm:inline">Users ({adminUsersCount})</span>
          <span className="sm:hidden">Users ({adminUsersCount})</span>
        </button>
      )}
      {isSuperAdmin && (
        <button
          onClick={() => onTabChange('partners')}
          className={`flex items-center gap-1.5 sm:gap-2 px-3 sm:px-4 py-2 rounded-lg font-medium transition-all duration-200 text-xs sm:text-sm ${
            activeTab === 'partners'
              ? 'bg-primary text-primary-foreground shadow-lg'
              : 'text-muted-foreground hover:text-foreground hover:bg-background/50'
          }`}
        >
          <Handshake className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
          <span className="hidden sm:inline">Partners ({partnersCount})</span>
          <span className="sm:hidden">Part ({partnersCount})</span>
        </button>
      )}
      {isSuperAdmin && (
        <button
          onClick={() => onTabChange('platforms')}
          className={`flex items-center gap-1.5 sm:gap-2 px-3 sm:px-4 py-2 rounded-lg font-medium transition-all duration-200 text-xs sm:text-sm ${
            activeTab === 'platforms'
              ? 'bg-primary text-primary-foreground shadow-lg'
              : 'text-muted-foreground hover:text-foreground hover:bg-background/50'
          }`}
        >
          <Server className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
          <span className="hidden sm:inline">Platforms ({platformsCount})</span>
          <span className="sm:hidden">Plat ({platformsCount})</span>
        </button>
      )}
      <button
        onClick={() => onTabChange('changelog')}
        className={`flex items-center gap-1.5 sm:gap-2 px-3 sm:px-4 py-2 rounded-lg font-medium transition-all duration-200 text-xs sm:text-sm ${
          activeTab === 'changelog'
            ? 'bg-primary text-primary-foreground shadow-lg'
            : 'text-muted-foreground hover:text-foreground hover:bg-background/50'
        }`}
      >
        <GitBranch className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
        <span className="hidden sm:inline">Changelog ({changelogCount})</span>
        <span className="sm:hidden">Chg ({changelogCount})</span>
      </button>
      <button
        onClick={() => onTabChange('blog')}
        className={`flex items-center gap-1.5 sm:gap-2 px-3 sm:px-4 py-2 rounded-lg font-medium transition-all duration-200 text-xs sm:text-sm ${
          activeTab === 'blog'
            ? 'bg-primary text-primary-foreground shadow-lg'
            : 'text-muted-foreground hover:text-foreground hover:bg-background/50'
        }`}
      >
        <FileText className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
        <span className="hidden sm:inline">Blog ({blogCount})</span>
        <span className="sm:hidden">Blog ({blogCount})</span>
      </button>
      <button
        onClick={() => onTabChange('images')}
        className={`flex items-center gap-1.5 sm:gap-2 px-3 sm:px-4 py-2 rounded-lg font-medium transition-all duration-200 text-xs sm:text-sm ${
          activeTab === 'images'
            ? 'bg-primary text-primary-foreground shadow-lg'
            : 'text-muted-foreground hover:text-foreground hover:bg-background/50'
        }`}
      >
        <Image className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
        <span className="hidden sm:inline">Images ({imagesCount})</span>
        <span className="sm:hidden">Img ({imagesCount})</span>
      </button>
    </div>
  );
}

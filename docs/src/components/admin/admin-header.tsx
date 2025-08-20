"use client";

import { LogOut, Shield, RefreshCcw, Github } from 'lucide-react';
import { MeResp } from './types';

interface AdminHeaderProps {
  auth: MeResp;
  onLogout: () => void;
  onRefresh: () => void;
}

export function AdminHeader({ auth, onLogout, onRefresh }: AdminHeaderProps) {
  return (
    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 sm:gap-0 mb-6 sm:mb-8 p-4 sm:p-6 bg-gradient-to-br from-blue-500/5 via-transparent to-blue-500/5 border border-blue-500/20 rounded-xl">
      <div className="flex items-center gap-3 sm:gap-4">
        {auth.user?.avatar ? (
          <img
            src={auth.user.avatar}
            alt={auth.user.username}
            className="w-10 h-10 sm:w-12 sm:h-12 rounded-full border-2 border-primary/20"
          />
        ) : (
          <div className="w-10 h-10 sm:w-12 sm:h-12 rounded-full bg-primary/10 flex items-center justify-center">
            <Shield className="w-5 h-5 sm:w-6 sm:h-6 text-primary" />
          </div>
        )}
        <div>
          <div className="text-xs sm:text-sm text-muted-foreground">
            Signed in as {auth.user?.isGitHubAdmin && (
              <span className="inline-flex items-center gap-1 text-primary">
                <Github className="w-2.5 h-2.5 sm:w-3 sm:h-3" />
                GitHub Admin
              </span>
            )}
          </div>
          <div className="text-sm sm:text-base text-foreground font-semibold">
            {auth.user?.name || auth.user?.username}
            {auth.user?.isGitHubAdmin && (
              <span className="text-xs text-muted-foreground ml-2">
                (@{auth.user.username})
              </span>
            )}
          </div>
        </div>
      </div>
      <div className="flex items-center gap-2">
        <button onClick={onRefresh} className="inline-flex items-center gap-2 px-2.5 sm:px-3 py-2 rounded-lg border border-border/50 bg-background hover:bg-background/70 text-xs sm:text-sm">
          <RefreshCcw className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
          <span className="hidden sm:inline">Refresh</span>
          <span className="sm:hidden">Ref</span>
        </button>
        <button onClick={onLogout} className="inline-flex items-center gap-2 px-2.5 sm:px-3 py-2 bg-destructive text-destructive-foreground rounded-lg hover:bg-destructive/90 text-xs sm:text-sm">
          <LogOut className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
          <span className="hidden sm:inline">Logout</span>
          <span className="sm:hidden">Out</span>
        </button>
      </div>
    </div>
  );
}

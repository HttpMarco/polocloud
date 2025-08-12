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
    <div className="flex items-center justify-between mb-8 p-6 bg-gradient-to-br from-blue-500/5 via-transparent to-blue-500/5 border border-blue-500/20 rounded-xl">
      <div className="flex items-center gap-4">
        {auth.user?.avatar ? (
          <img
            src={auth.user.avatar}
            alt={auth.user.username}
            className="w-12 h-12 rounded-full border-2 border-primary/20"
          />
        ) : (
          <div className="w-12 h-12 rounded-full bg-primary/10 flex items-center justify-center">
            <Shield className="w-6 h-6 text-primary" />
          </div>
        )}
        <div>
          <div className="text-sm text-muted-foreground">
            Signed in as {auth.user?.isGitHubAdmin && (
              <span className="inline-flex items-center gap-1 text-primary">
                <Github className="w-3 h-3" />
                GitHub Admin
              </span>
            )}
          </div>
          <div className="text-foreground font-semibold">
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
        <button onClick={onRefresh} className="inline-flex items-center gap-2 px-3 py-2 rounded-lg border border-border/50 bg-background hover:bg-background/70 text-sm">
          <RefreshCcw className="w-4 h-4" /> Refresh
        </button>
        <button onClick={onLogout} className="inline-flex items-center gap-2 px-3 py-2 bg-destructive text-destructive-foreground rounded-lg hover:bg-destructive/90 text-sm">
          <LogOut className="w-4 h-4" /> Logout
        </button>
      </div>
    </div>
  );
}

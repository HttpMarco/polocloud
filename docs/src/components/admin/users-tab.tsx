"use client";

import { Users, Plus, Trash2, Crown, RefreshCcw } from 'lucide-react';
import { AdminUser } from './types';

interface UsersTabProps {
  adminUsers: AdminUser[];
  loadingUsers: boolean;
  newUserGitHubUsername: string;
  setNewUserGitHubUsername: (username: string) => void;
  addingUser: boolean;
  onAddUser: () => void;
  onRemoveUser: (username: string) => void;
  onRefresh: () => void;
}

export function UsersTab({
  adminUsers,
  loadingUsers,
  newUserGitHubUsername,
  setNewUserGitHubUsername,
  addingUser,
  onAddUser,
  onRemoveUser,
  onRefresh
}: UsersTabProps) {
  return (
    <div className="mb-6 p-6 bg-gradient-to-br from-purple-500/5 via-transparent to-purple-500/5 border border-purple-500/20 rounded-xl">
      <div className="flex items-center gap-3 mb-4">
        <Users className="w-5 h-5 text-purple-500" />
        <h3 className="text-lg font-semibold text-foreground">GitHub Admin Users</h3>
        <button
          onClick={onRefresh}
          className="ml-auto inline-flex items-center gap-2 px-3 py-1 bg-purple-600/10 text-purple-600 rounded-lg hover:bg-purple-600/20 text-sm transition-all duration-300"
        >
          <RefreshCcw className="w-4 h-4" />
          Refresh
        </button>
      </div>

      <div className="mb-6 p-4 bg-background/30 rounded-lg border border-border/30">
        <h4 className="font-medium text-foreground mb-3">Add New GitHub User</h4>
        <div className="flex gap-3">
          <input
            type="text"
            value={newUserGitHubUsername}
            onChange={(e) => setNewUserGitHubUsername(e.target.value)}
            placeholder="GitHub username (e.g., johndoe)"
            className="flex-1 px-3 py-2 bg-background border border-border/50 rounded-lg text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-purple-500/50"
          />
          <button
            onClick={onAddUser}
            disabled={addingUser || !newUserGitHubUsername.trim()}
            className="inline-flex items-center gap-2 px-4 py-2 bg-purple-600 text-white rounded-lg hover:bg-purple-700 disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-300"
          >
            {addingUser ? (
              <>
                <RefreshCcw className="w-4 h-4 animate-spin" />
                Adding...
              </>
            ) : (
              <>
                <Plus className="w-4 h-4" />
                Add User
              </>
            )}
          </button>
        </div>
      </div>

      {loadingUsers ? (
        <div className="text-center py-8 text-muted-foreground">
          <RefreshCcw className="w-8 h-8 animate-spin mx-auto mb-2" />
          Loading users...
        </div>
      ) : adminUsers.length === 0 ? (
        <div className="text-center py-8 text-muted-foreground">
          No admin users found.
        </div>
      ) : (
        <div className="space-y-3">
          {adminUsers.map((user) => (
            <div
              key={user.username}
              className="flex items-center justify-between p-4 bg-background/30 rounded-lg border border-border/30"
            >
              <div className="flex items-center gap-3">
                {user.avatar ? (
                  <img
                    src={user.avatar}
                    alt={user.username}
                    className="w-10 h-10 rounded-full border border-border/50"
                  />
                ) : (
                  <div className="w-10 h-10 rounded-full bg-muted flex items-center justify-center">
                    <Users className="w-5 h-5 text-muted-foreground" />
                  </div>
                )}
                <div>
                  <div className="flex items-center gap-2">
                    <span className="font-medium text-foreground">{user.name || user.username}</span>
                    {user.role === 'SUPER_ADMIN' && (
                      <span className="inline-flex items-center gap-1 text-xs bg-yellow-500/10 text-yellow-600 px-2 py-0.5 rounded-full">
                        <Crown className="w-3 h-3" />
                        Admin
                      </span>
                    )}
                    {user.role !== 'SUPER_ADMIN' && (
                      <span className="text-xs bg-blue-500/10 text-blue-600 px-2 py-0.5 rounded-full">Team</span>
                    )}
                  </div>
                  <div className="text-xs text-muted-foreground">
                    @{user.username} • Added by {user.addedBy}
                  </div>
                </div>
              </div>
              {user.role !== 'SUPER_ADMIN' && (
                <button
                  onClick={() => onRemoveUser(user.username)}
                  className="inline-flex items-center gap-1 px-2 py-1 bg-red-600/10 text-red-600 rounded-lg hover:bg-red-600/20 text-xs transition-all duration-300"
                >
                  <Trash2 className="w-3 h-3" />
                  Remove
                </button>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

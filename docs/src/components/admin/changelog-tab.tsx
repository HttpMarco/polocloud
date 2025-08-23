"use client";

import { useState, useEffect } from 'react';
import { Plus, Calendar, User, GitBranch } from 'lucide-react';
import { ChangelogEntry } from './types';
import { useRouter } from 'next/navigation';

export function ChangelogTab() {
  const router = useRouter();
  const [changelog, setChangelog] = useState<ChangelogEntry[]>([]);
  const [loading, setLoading] = useState(true);




  useEffect(() => {
    fetchChangelog();
  }, []);

  const fetchChangelog = async () => {
    try {
      setLoading(true);
      const response = await fetch('/api/public/changelog');
      if (response.ok) {
        const data = await response.json();
        setChangelog(data.changelog || []);
      }
    } catch (error) {
      console.error('Error fetching changelog:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    try {
      const url = '/api/admin/changelog/create';
      const method = 'POST';
      const body = {};

      const response = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
      });

      if (response.ok) {
        await fetchChangelog();

      } else {
        const error = await response.json();
        alert(`Error: ${error.error}`);
      }
    } catch (error) {
      console.error('Error saving changelog entry:', error);
      alert('Failed to save changelog entry');
    }
  };

  const handleDelete = async (slug: string) => {
    console.log('Delete functionality not implemented for changelog entries');
    alert('Delete functionality is not yet implemented');
  };




  const getTypeColor = (type: string) => {
    switch (type) {
      case 'major': return 'bg-red-100 text-red-800 dark:bg-red-900/20 dark:text-red-400';
      case 'minor': return 'bg-blue-100 text-blue-800 dark:bg-blue-900/20 dark:text-blue-400';
      case 'patch': return 'bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-400';
      case 'hotfix': return 'bg-orange-100 text-orange-800 dark:bg-orange-900/20 dark:text-orange-400';
      default: return 'bg-gray-100 text-gray-800 dark:bg-gray-900/20 dark:text-gray-400';
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center p-6 sm:p-8">
        <div className="animate-spin rounded-full h-6 w-6 sm:h-8 sm:w-8 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="space-y-4 sm:space-y-6">
      <div className="flex flex-col sm:flex-row sm:justify-between sm:items-center gap-4 sm:gap-0">
        <h2 className="text-xl sm:text-2xl font-bold">Changelog Management</h2>
        <div className="flex flex-col sm:flex-row gap-2 sm:gap-3">
          <button
            onClick={() => router.push('/admin/changelog/create')}
            className="flex items-center justify-center gap-2 px-3 sm:px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:bg-primary/90 transition-colors text-sm"
          >
            <Plus className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
            <span className="hidden sm:inline">Create New Entry</span>
            <span className="sm:hidden">Create Entry</span>
          </button>

          <button
            onClick={fetchChangelog}
            className="flex items-center justify-center gap-2 px-3 sm:px-4 py-2 border border-border text-foreground rounded-lg hover:bg-muted transition-colors text-sm"
          >
            <GitBranch className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
            <span className="hidden sm:inline">Refresh</span>
            <span className="sm:hidden">Ref</span>
          </button>
        </div>
      </div>

      <div className="space-y-3 sm:space-y-4">
        {changelog.map((entry) => (
          <div key={entry.slug} className="bg-card border rounded-lg p-4 sm:p-6">
            <div className="flex flex-col sm:flex-row sm:justify-between sm:items-start gap-3 sm:gap-0 mb-3 sm:mb-4">
              <div className="flex flex-col sm:flex-row sm:items-center gap-2 sm:gap-3">
                <span className={`px-2 sm:px-3 py-1 rounded-full text-xs font-medium ${getTypeColor(entry.type)}`}>
                  {entry.type.toUpperCase()}
                </span>
                <h3 className="text-lg sm:text-xl font-semibold">{entry.version}</h3>
                <h4 className="text-base sm:text-lg font-medium text-muted-foreground">{entry.title}</h4>
              </div>
              
              <div className="flex gap-2">
              </div>
            </div>

            <p className="text-sm sm:text-base text-muted-foreground mb-3 sm:mb-4">{entry.description}</p>

            {entry.content && (
              <div className="mb-3 sm:mb-4">
                <div className="bg-muted/50 rounded-lg p-3 border">
                  <h5 className="text-sm font-medium mb-2">Content Preview:</h5>
                  <div className="text-xs text-muted-foreground line-clamp-3">
                    {entry.content
                      .replace(/---[\s\S]*?---/, '')
                      .replace(/[#*`]/g, '')
                      .trim()
                      .substring(0, 200)}
                    {entry.content.length > 200 && '...'}
                  </div>
                </div>
              </div>
            )}

            <div className="flex flex-col sm:flex-row sm:items-center gap-2 sm:gap-4 text-xs sm:text-sm text-muted-foreground">
              <div className="flex items-center gap-1">
                <Calendar className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
                {new Date(entry.releaseDate).toLocaleDateString()}
              </div>
              <div className="flex items-center gap-1">
                <User className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
                {entry.author}
              </div>
              <div className="flex items-center gap-1">
                <GitBranch className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
                {entry.slug}
              </div>
            </div>
          </div>
        ))}

        {changelog.length === 0 && (
          <div className="text-center py-8 sm:py-12 text-muted-foreground">
            <GitBranch className="w-8 h-8 sm:w-12 sm:h-12 mx-auto mb-3 sm:mb-4 opacity-50" />
            <p className="text-base sm:text-lg">No changelog entries yet</p>
            <p className="text-sm mb-3 sm:mb-4">Create your first changelog entry to get started</p>
            <button
              onClick={() => router.push('/admin/changelog/create')}
              className="inline-flex items-center justify-center gap-2 px-3 sm:px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:bg-primary/90 transition-colors text-sm"
            >
              <Plus className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
              <span className="hidden sm:inline">Create First Entry</span>
              <span className="sm:hidden">Create Entry</span>
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

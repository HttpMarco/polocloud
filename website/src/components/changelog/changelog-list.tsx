'use client';

import { useState } from 'react';
import { GitBranch } from 'lucide-react';
import { ChangelogCard } from './changelog-card';
import { ChangelogFilters } from './changelog-filters';

interface ChangelogPost {
  title: string;
  description?: string;
  date?: string;
  version?: string;
  type?: 'major' | 'minor' | 'patch' | 'hotfix';
  author?: string;
  slug: string;
}

interface ChangelogListProps {
  posts: ChangelogPost[];
}

export function ChangelogList({ posts }: ChangelogListProps) {
  const [activeFilter, setActiveFilter] = useState('all');
  const [searchQuery, setSearchQuery] = useState('');

  const filteredPosts = posts.filter((post) => {
    if (activeFilter !== 'all' && post.type !== activeFilter) {
      return false;
    }
    if (searchQuery) {
      const query = searchQuery.toLowerCase();
      return (
        post.title.toLowerCase().includes(query) ||
        post.description?.toLowerCase().includes(query) ||
        post.author?.toLowerCase().includes(query) ||
        post.version?.toLowerCase().includes(query)
      );
    }

    return true;
  });

  const filterCounts = {
    all: posts.length,
    major: posts.filter(p => p.type === 'major').length,
    minor: posts.filter(p => p.type === 'minor').length,
    patch: posts.filter(p => p.type === 'patch').length,
  };

  const handleFilterChange = (filter: string) => {
    setActiveFilter(filter);
  };

  const handleSearchChange = (query: string) => {
    setSearchQuery(query);
  };

  return (
    <>
      <ChangelogFilters 
        totalCount={posts.length}
        onFilterChange={handleFilterChange}
        onSearchChange={handleSearchChange}
        filterCounts={filterCounts}
      />

      <div className="space-y-3 sm:space-y-4">
        {filteredPosts.length === 0 ? (
          <div className="text-center py-8 sm:py-12">
            <GitBranch className="w-12 h-12 sm:w-16 sm:h-16 text-muted-foreground mx-auto mb-3 sm:mb-4" />
            <h3 className="text-lg sm:text-xl font-semibold text-foreground mb-2">
              {searchQuery ? 'No matching releases found' : 'No changelog entries yet'}
            </h3>
            <p className="text-sm sm:text-base text-muted-foreground">
              {searchQuery 
                ? `No releases match "${searchQuery}". Try adjusting your search.`
                : "We'll post release notes here as we ship new versions."
              }
            </p>
          </div>
        ) : (
          filteredPosts.map((post, index) => (
            <ChangelogCard
              key={post.slug}
              post={post}
              index={index}
              totalPosts={filteredPosts.length}
            />
          ))
        )}
      </div>
    </>
  );
}

'use client';

import { useState } from 'react';
import { BlogCard } from './blog-card';
import { BlogFilters } from './blog-filters';

interface BlogPost {
  title: string;
  description?: string;
  date?: string;
  author?: string;
  tags?: string[];
  slug: string;
  pinned?: boolean;
}

interface BlogListProps {
  pinnedPosts: BlogPost[];
  unpinnedPosts: BlogPost[];
}

export function BlogList({ pinnedPosts, unpinnedPosts }: BlogListProps) {
  const [activeFilter, setActiveFilter] = useState('all');
  const [searchQuery, setSearchQuery] = useState('');

  const allPosts = [...pinnedPosts, ...unpinnedPosts];

  const filterPosts = (posts: BlogPost[]) => {
    return posts.filter((post) => {
      if (activeFilter === 'pinned' && !post.pinned) return false;
      if (activeFilter === 'recent' && post.date) {
        const postDate = new Date(post.date);
        const thirtyDaysAgo = new Date();
        thirtyDaysAgo.setDate(thirtyDaysAgo.getDate() - 30);
        if (postDate < thirtyDaysAgo) return false;
      }
      if (activeFilter === 'tutorial' && post.tags) {
        const hasTutorialTag = post.tags.some(tag => 
          tag.toLowerCase().includes('tutorial') || 
          tag.toLowerCase().includes('guide') ||
          tag.toLowerCase().includes('how-to')
        );
        if (!hasTutorialTag) return false;
      }

      if (searchQuery) {
        const query = searchQuery.toLowerCase();
        return (
          post.title.toLowerCase().includes(query) ||
          post.description?.toLowerCase().includes(query) ||
          post.author?.toLowerCase().includes(query) ||
          post.tags?.some(tag => tag.toLowerCase().includes(query))
        );
      }

      return true;
    });
  };

  const filteredPinnedPosts = filterPosts(pinnedPosts);
  const filteredUnpinnedPosts = filterPosts(unpinnedPosts);

  const filterCounts = {
    all: allPosts.length,
    pinned: pinnedPosts.length,
    recent: allPosts.filter(p => {
      if (!p.date) return false;
      const postDate = new Date(p.date);
      const thirtyDaysAgo = new Date();
      thirtyDaysAgo.setDate(thirtyDaysAgo.getDate() - 30);
      return postDate >= thirtyDaysAgo;
    }).length,
    tutorial: allPosts.filter(p => 
      p.tags?.some(tag => 
        tag.toLowerCase().includes('tutorial') || 
        tag.toLowerCase().includes('guide') ||
        tag.toLowerCase().includes('how-to')
      )
    ).length,
  };

  const handleFilterChange = (filter: string) => {
    setActiveFilter(filter);
  };

  const handleSearchChange = (query: string) => {
    setSearchQuery(query);
  };

  return (
    <>
      <BlogFilters 
        totalCount={allPosts.length}
        onFilterChange={handleFilterChange}
        onSearchChange={handleSearchChange}
        filterCounts={filterCounts}
      />

      {filteredPinnedPosts.length > 0 && (
        <div className="mb-8 sm:mb-12">
          <div className="flex items-center gap-2 mb-4 sm:mb-6">
            <div className="w-1 h-6 bg-primary rounded-full"></div>
            <h2 className="text-xl sm:text-2xl font-bold text-foreground dark:text-white">
              Pinned Posts
            </h2>
          </div>
          <div className="grid gap-6 sm:gap-8 md:grid-cols-2 lg:grid-cols-3">
            {filteredPinnedPosts.map((post, index) => (
              <BlogCard
                key={post.slug}
                post={post}
                index={index}
                totalPosts={filteredPinnedPosts.length}
                isPinned={true}
              />
            ))}
          </div>
        </div>
      )}

      {filteredUnpinnedPosts.length > 0 && (
        <div>
          <div className="flex items-center gap-2 mb-4 sm:mb-6">
            <div className="w-1 h-6 bg-primary rounded-full"></div>
            <h2 className="text-xl sm:text-2xl font-bold text-foreground dark:text-white">
              All Posts
            </h2>
          </div>
          <div className="grid gap-6 sm:gap-8 md:grid-cols-2 lg:grid-cols-3">
            {filteredUnpinnedPosts.map((post, index) => (
              <BlogCard
                key={post.slug}
                post={post}
                index={index}
                totalPosts={filteredUnpinnedPosts.length}
                isPinned={false}
              />
            ))}
          </div>
        </div>
      )}

      {filteredPinnedPosts.length === 0 && filteredUnpinnedPosts.length === 0 && (
        <div className="text-center py-12 sm:py-16">
          <div className="w-16 h-16 sm:w-20 sm:h-20 bg-gradient-to-br from-primary/20 to-primary/10 rounded-full flex items-center justify-center mx-auto mb-4 sm:mb-6">
            <span className="text-2xl sm:text-3xl">📝</span>
          </div>
          <h2 className="text-xl sm:text-2xl font-bold text-foreground dark:text-white mb-3 sm:mb-4">
            {searchQuery ? 'No matching posts found' : 'No blog posts yet'}
          </h2>
          <p className="text-sm sm:text-base text-muted-foreground max-w-md mx-auto px-4 sm:px-0">
            {searchQuery 
              ? `No posts match "${searchQuery}". Try adjusting your search.`
              : "We're working on creating amazing content for you. Check back soon for updates!"
            }
          </p>
        </div>
      )}
    </>
  );
}

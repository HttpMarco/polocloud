'use client';

import { useState } from 'react';
import { Search, Filter } from 'lucide-react';

interface BlogFiltersProps {
  totalCount: number;
  onFilterChange?: (filter: string) => void;
  onSearchChange?: (query: string) => void;
  filterCounts?: {
    all: number;
    pinned: number;
    recent: number;
    tutorial: number;
  };
}

export function BlogFilters({ totalCount, onFilterChange, onSearchChange, filterCounts }: BlogFiltersProps) {
  const [activeFilter, setActiveFilter] = useState('all');
  const [searchQuery, setSearchQuery] = useState('');

  const filters = [
    { id: 'all', label: 'All Posts', count: filterCounts?.all || totalCount },
    { id: 'pinned', label: 'Pinned', count: filterCounts?.pinned || 0 },
    { id: 'recent', label: 'Recent', count: filterCounts?.recent || 0 },
    { id: 'tutorial', label: 'Tutorials', count: filterCounts?.tutorial || 0 },
  ];

  const handleFilterClick = (filterId: string) => {
    setActiveFilter(filterId);
    onFilterChange?.(filterId);
  };

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const query = e.target.value;
    setSearchQuery(query);
    onSearchChange?.(query);
  };

  return (
    <div className="mb-8 sm:mb-12">
      <div className="flex flex-col sm:flex-row gap-4 sm:gap-6 items-start sm:items-center justify-between">
        <div className="flex flex-wrap gap-2 sm:gap-3">
          {filters.map((filter) => (
            <button
              key={filter.id}
              onClick={() => handleFilterClick(filter.id)}
              className={`px-3 py-1.5 text-xs sm:text-sm font-medium rounded-full border transition-colors ${
                activeFilter === filter.id
                  ? 'bg-primary/10 text-primary border-primary/20'
                  : 'bg-muted/50 text-muted-foreground border-border/50 hover:bg-muted hover:text-foreground'
              }`}
            >
              <span className="flex items-center gap-1.5">
                <Filter className="w-3 h-3" />
                {filter.label}
                {filter.count > 0 && (
                  <span className="bg-muted text-muted-foreground px-1.5 py-0.5 rounded-full text-xs">
                    {filter.count}
                  </span>
                )}
              </span>
            </button>
          ))}
        </div>

        <div className="relative flex-1 max-w-md">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
          <input
            type="text"
            placeholder="Search blog posts..."
            value={searchQuery}
            onChange={handleSearchChange}
            className="w-full pl-10 pr-4 py-2 bg-muted/50 border border-border/50 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary/50 transition-colors"
          />
        </div>
      </div>
    </div>
  );
}

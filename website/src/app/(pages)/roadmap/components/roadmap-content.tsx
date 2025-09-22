'use client';
import { useEffect, useState, useRef } from 'react';
import Link from 'next/link';
import { ArrowLeft, Calendar, Star, Package, Code, Server, Filter, X } from 'lucide-react';

interface RoadmapItem {
  id: string;
  title: string;
  description: string;
  category: 'ui' | 'platforms' | 'bot' | 'addons';
  estimatedDate?: string;
  tags?: string[];
  assignees?: Array<{
    login: string;
    avatarUrl?: string;
  }>;
}

interface RoadmapColumn {
  id: string;
  title: string;
  color: string;
  items: RoadmapItem[];
}

const fallbackData: RoadmapColumn[] = [
  {
    id: 'no-status',
    title: 'No Status',
    color: 'border-red-500',
    items: []
  },
  {
    id: 'todo',
    title: 'Todo',
    color: 'border-gray-500',
    items: []
  },
  {
    id: 'in-progress',
    title: 'In Progress',
    color: 'border-amber-600',
    items: []
  },
  {
    id: 'quality-testing',
    title: 'Quality-Testing',
    color: 'border-purple-500',
    items: []
  },
  {
    id: 'done',
    title: 'Done',
    color: 'border-green-600',
    items: []
  }
];

const getTagStyle = (tag: string) => {
  const tagLower = tag.toLowerCase();

  if (tagLower.includes('improvement') || tagLower.includes('improvment')) {
    return {
      backgroundColor: 'rgba(234, 187, 44, 0.15)',
      color: 'rgb(234, 187, 44)',
      borderColor: 'rgba(234, 187, 44, 0.3)'
    };
  }
  if (tagLower.includes('bug')) {
    return {
      backgroundColor: 'rgba(236, 161, 168, 0.15)',
      color: 'rgb(236, 161, 168)',
      borderColor: 'rgba(236, 161, 168, 0.3)'
    };
  }
  if (tagLower.includes('feature')) {
    return {
      backgroundColor: 'rgba(40, 217, 43, 0.15)',
      color: 'rgb(40, 217, 43)',
      borderColor: 'rgba(40, 217, 43, 0.3)'
    };
  }
  if (tagLower.includes('prototype-6')) {
    return {
      backgroundColor: 'rgba(147, 51, 234, 0.15)',
      color: 'rgb(147, 51, 234)',
      borderColor: 'rgba(147, 51, 234, 0.3)'
    };
  }
  if (tagLower.includes('prototype-5')) {
    return {
      backgroundColor: 'rgba(136, 252, 202, 0.15)',
      color: 'rgb(136, 252, 202)',
      borderColor: 'rgba(136, 252, 202, 0.3)'
    };
  }
  if (tagLower.includes('new') || tagLower.includes('requirement')) {
    return {
      backgroundColor: 'rgba(130, 247, 29, 0.15)',
      color: 'rgb(130, 247, 29)',
      borderColor: 'rgba(130, 247, 29, 0.3)'
    };
  }

  const hash = tag.split('').reduce((a, b) => {
    a = ((a << 5) - a) + b.charCodeAt(0);
    return a & a;
  }, 0);
  
  const hue = Math.abs(hash) % 360;
  const saturation = 70;
  const lightness = 60;
  
  return {
    backgroundColor: `hsla(${hue}, ${saturation}%, ${lightness}%, 0.15)`,
    color: `hsl(${hue}, ${saturation}%, ${lightness}%)`,
    borderColor: `hsla(${hue}, ${saturation}%, ${lightness}%, 0.3)`
  };
};

const getCategoryIcon = (category: string) => {
  switch (category) {
    case 'ui': return <Star className="w-4 h-4" />;
    case 'platforms': return <Server className="w-4 h-4" />;
    case 'bot': return <Code className="w-4 h-4" />;
    case 'addons': return <Package className="w-4 h-4" />;
    default: return <Code className="w-4 h-4" />;
  }
};

const getCategoryColor = (category: string) => {
  switch (category) {
    case 'ui': return 'bg-blue-500/20 border-blue-500/30 text-blue-400';
    case 'platforms': return 'bg-purple-500/20 border-purple-500/30 text-purple-400';
    case 'bot': return 'bg-green-500/20 border-green-500/30 text-green-400';
    case 'addons': return 'bg-orange-500/20 border-orange-500/30 text-orange-400';
    default: return 'bg-gray-500/20 border-gray-500/30 text-gray-400';
  }
};

const RoadmapCard = ({ item, index }: { item: RoadmapItem; index: number }) => {
  return (
    <div 
      className="bg-card/40 backdrop-blur-sm border border-border/40 rounded-xl p-3 sm:p-4 mb-3 hover:bg-card/60 transition-all duration-500 hover:shadow-lg hover:border-border/60 hover:scale-105 group"
      style={{
        animationDelay: `${index * 100}ms`
      }}
    >
      {item.tags && (
        <div className="flex flex-wrap gap-1.5 sm:gap-2 mb-2">
          {item.tags.map((tag, tagIndex) => {
            const tagStyle = getTagStyle(tag);
            return (
              <span 
                key={tag} 
                className="px-2 py-0.5 rounded-full text-xs font-medium border transition-all duration-300 hover:scale-110 hover:shadow-md"
                style={{
                  ...tagStyle,
                  animationDelay: `${(index * 100) + (tagIndex * 50)}ms`
                }}
              >
                {tag}
              </span>
            );
          })}
        </div>
      )}
      <h3 className="font-bold text-foreground dark:text-white mb-2 text-sm leading-tight group-hover:text-primary transition-colors duration-300">
        {item.title}
      </h3>
      <p className="text-xs text-muted-foreground dark:text-white/70 mb-3 leading-relaxed">
        {item.description}
      </p>

      <div className="flex items-center justify-between">
        {item.estimatedDate && (
          <div className="flex items-center gap-1.5 text-xs text-muted-foreground dark:text-white/60 bg-muted/30 px-2 py-1 rounded-md transition-all duration-300 hover:bg-muted/50">
            <Calendar className="w-3 h-3" />
            <span className="font-medium">{item.estimatedDate}</span>
          </div>
        )}

        {item.assignees && item.assignees.length > 0 && (
          <div className="flex items-center gap-1">
            {item.assignees.slice(0, 3).map((assignee, assigneeIndex) => (
              <div key={assignee.login} className="flex items-center gap-1">
                <div className="relative">
                  <img 
                    src={assignee.avatarUrl || `https://github.com/${assignee.login}.png`}
                    alt={assignee.login}
                    className="w-5 h-5 rounded-full border border-border/50 transition-all duration-300 hover:scale-110 hover:border-primary/50"
                    title={assignee.login}
                    onError={(e) => {
                      const target = e.target as HTMLImageElement;
                      target.style.display = 'none';
                      const fallback = target.nextElementSibling as HTMLElement;
                      if (fallback) fallback.style.display = 'flex';
                    }}
                  />
                  <div className="w-5 h-5 rounded-full bg-muted flex items-center justify-center absolute top-0 left-0" style={{ display: 'none' }}>
                    <span className="text-xs font-medium text-muted-foreground">
                      {assignee.login.charAt(0).toUpperCase()}
                    </span>
                  </div>
                </div>
              </div>
            ))}
            <div className="flex items-center gap-1">
              <span className="text-xs text-muted-foreground font-medium transition-colors duration-300 group-hover:text-foreground">
                {item.assignees.slice(0, 2).map(assignee => assignee.login).join(', ')}
                {item.assignees.length > 2 && ` +${item.assignees.length - 2}`}
              </span>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

const RoadmapColumn = ({ column, columnIndex }: { column: RoadmapColumn; columnIndex: number }) => {
  const getBorderColor = (columnId: string) => {
    switch (columnId) {
      case 'no-status':
        return '#dc2626';
      case 'todo':
        return '#9198a1';
      case 'in-progress':
        return '#9e6a03';
      case 'quality-testing':
        return '#8957e5';
      case 'done':
        return '#238636';
      default:
        return '#6b7280';
    }
  };

  return (
    <div 
      className="flex flex-col h-full"
      style={{
        animationDelay: `${columnIndex * 200}ms`,
        animation: 'slideInLeft 0.8s ease-out forwards'
      }}
    >
      <div
        className="mb-4 sm:mb-6 p-3 sm:p-4 bg-card/50 backdrop-blur-sm border rounded-xl shadow-lg transition-all duration-500 hover:shadow-xl hover:scale-105 relative"
        style={{
          borderColor: getBorderColor(column.id),
          boxShadow: `inset 0 0 30px ${getBorderColor(column.id)}25, 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)`
        }}
      >
        <div
            className="absolute inset-0 rounded-xl opacity-20"
          style={{
            background: `radial-gradient(circle at center, ${getBorderColor(column.id)}30, transparent 60%)`
          }}
        />

        <div className="relative z-10">
          <h2 className="font-bold text-foreground dark:text-white text-sm uppercase tracking-wider mb-1">
              {column.title}
            </h2>
          <div className="text-xs text-muted-foreground dark:text-white/60 font-medium">
            {column.items.length} items
          </div>
        </div>
      </div>

      <div className="flex-1 space-y-3 max-h-[500px] sm:max-h-[600px] overflow-y-auto pr-2 scrollbar-thin scrollbar-thumb-border/50 scrollbar-track-transparent">
        {column.items.map((item, index) => (
          <RoadmapCard key={item.id} item={item} index={index} />
        ))}
      </div>
    </div>
  );
};

export function RoadmapContent() {
  const [isVisible, setIsVisible] = useState(false);
  const [roadmapData, setRoadmapData] = useState<RoadmapColumn[]>(fallbackData);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [activeFilters, setActiveFilters] = useState<string[]>([]);
  const [isFilterModalOpen, setIsFilterModalOpen] = useState(false);
  const [allTags, setAllTags] = useState<string[]>([]);
  const contentRef = useRef<HTMLElement>(null);

  useEffect(() => {
    const style = document.createElement('style');
    style.textContent = `
      @keyframes slideInUp {
        from {
          opacity: 0;
          transform: translateY(30px);
        }
        to {
          opacity: 1;
          transform: translateY(0);
        }
      }
      
      @keyframes slideInLeft {
        from {
          opacity: 0;
          transform: translateX(-30px);
        }
        to {
          opacity: 1;
          transform: translateX(0);
        }
      }
      
      @keyframes fadeInScale {
        from {
          opacity: 0;
          transform: scale(0.8);
        }
        to {
          opacity: 1;
          transform: scale(1);
        }
      }
      
      @keyframes pulse {
        0%, 100% {
          opacity: 1;
        }
        50% {
          opacity: 0.5;
        }
      }
      
      @keyframes shimmer {
        0% {
          background-position: -200px 0;
        }
        100% {
          background-position: calc(200px + 100%) 0;
        }
      }
      
      .loading-shimmer {
        background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
        background-size: 200px 100%;
        animation: shimmer 1.5s infinite;
      }
      
      @keyframes modalFadeIn {
        from {
          opacity: 0;
        }
        to {
          opacity: 1;
        }
      }
      
      @keyframes modalSlideIn {
        from {
          opacity: 0;
          transform: scale(0.9) translateY(-20px);
        }
        to {
          opacity: 1;
          transform: scale(1) translateY(0);
        }
      }
    `;
    document.head.appendChild(style);
    return () => {
      if (document.head.contains(style)) {
        document.head.removeChild(style);
      }
    };
  }, []);

  useEffect(() => {
    const observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) {
          setIsVisible(true);
        }
      },
      { threshold: 0.1, rootMargin: '0px 0px -100px 0px' }
    );

    if (contentRef.current) {
      observer.observe(contentRef.current);
    }

    return () => observer.disconnect();
  }, []);

  useEffect(() => {
    const fetchRoadmapData = async () => {
    try {
      setIsLoading(true);
      setError(null);
      
        const response = await fetch('/api/github-projects?refresh=true');
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const data = await response.json();
      
      if (data.error) {
        throw new Error(data.error);
      }
      
      setRoadmapData(data);

      const tags = new Set<string>();
      data.forEach((column: RoadmapColumn) => {
        column.items.forEach((item: RoadmapItem) => {
          if (item.tags) {
            item.tags.forEach(tag => tags.add(tag));
          }
        });
      });
      
      const sortedTags = Array.from(tags).sort();
      setAllTags(sortedTags);

      console.log('Found tags:', sortedTags);
      sortedTags.forEach(tag => {
        const style = getTagStyle(tag);
        console.log(`Tag: "${tag}" -> Style:`, style);
      });
      console.log('Roadmap data:', data);
    } catch (err) {
      console.error('Failed to fetch roadmap data:', err);
      setError(err instanceof Error ? err.message : 'Failed to load roadmap data');
      setRoadmapData(fallbackData);
    } finally {
      setIsLoading(false);
    }
  };

    fetchRoadmapData();
  }, []);

  const toggleFilter = (tag: string) => {
    setActiveFilters(prev => 
      prev.includes(tag) 
        ? prev.filter(t => t !== tag)
        : [...prev, tag]
    );
  };

  const clearAllFilters = () => {
    setActiveFilters([]);
  };

  const filteredData = roadmapData.map(column => ({
    ...column,
    items: column.items.filter(item => {
      if (activeFilters.length === 0) return true;
      return item.tags?.some(tag => activeFilters.includes(tag));
    })
  }));

  return (
    <>
      <section ref={contentRef} className="relative py-8 sm:py-12 overflow-hidden">
        <div className="absolute inset-0 bg-gradient-to-b from-background via-muted/5 to-muted/5" />

        <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]" />

        <div className="relative container mx-auto px-4 sm:px-6">
          <div className={`mb-6 sm:mb-8 transition-all duration-1000 ease-out ${
            isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
          }`}>
            <div className="flex justify-between items-center">
              <Link 
                href="/"
                className="inline-flex items-center gap-2 px-3 sm:px-4 py-2 bg-card/50 hover:bg-card border border-border/50 rounded-lg text-sm font-medium transition-all duration-300 hover:scale-105 backdrop-blur-sm"
              >
                <ArrowLeft className="w-4 h-4" />
                <span className="hidden sm:inline">Back to Home</span>
                <span className="sm:hidden">Back</span>
              </Link>
            </div>
          </div>

          <div className={`text-center mb-12 sm:mb-16 transition-all duration-1000 ease-out ${
            isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
          }`}>
            <h1 className={`text-3xl sm:text-4xl md:text-5xl font-bold mb-4 sm:mb-6 bg-gradient-to-r from-foreground to-muted-foreground bg-clip-text text-transparent transition-all duration-1000 delay-200 ${
              isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
            }`}>
              Development Roadmap
            </h1>
            <p className={`text-lg sm:text-xl text-muted-foreground max-w-3xl mx-auto leading-relaxed px-4 sm:px-0 transition-all duration-1000 delay-400 ${
              isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
            }`}>
              Track our progress and see what&apos;s coming next for PoloCloud
            </p>
          </div>

          {isLoading && (
            <div className={`text-center mb-8 sm:mb-12 transition-all duration-1000 delay-500 ${
              isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
            }`}>
              <div className="inline-flex items-center gap-3 px-4 sm:px-6 py-3 bg-card/40 backdrop-blur-sm border border-border/40 rounded-xl">
                <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-foreground"></div>
                <span className="text-sm font-medium">Loading roadmap data...</span>
              </div>
            </div>
          )}

          {error && (
            <div className={`text-center mb-8 sm:mb-12 transition-all duration-1000 delay-500 ${
              isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
            }`}>
              <div className="inline-flex items-center gap-3 px-4 sm:px-6 py-3 bg-red-500/10 backdrop-blur-sm border border-red-500/20 rounded-xl">
                <span className="text-sm font-medium text-red-400">Error: {error}</span>
              </div>
            </div>
          )}

          <div className={`mb-8 sm:mb-12 transition-all duration-1000 delay-500 ${
            isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
          }`}>
            <div className="bg-card/60 backdrop-blur-sm border border-border/50 rounded-2xl p-4 sm:p-8 shadow-lg relative overflow-hidden">
              <div className="absolute inset-0 rounded-2xl bg-gradient-to-br from-muted/20 via-background/50 to-muted/20"></div>

              <div className="relative z-10">
                <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 sm:gap-0 mb-6 sm:mb-8">
                  <div className="text-center sm:text-left">
                    <h3 className="font-bold text-foreground dark:text-white text-lg sm:text-xl mb-2">
                      Categories
                    </h3>
                    <p className="text-sm text-muted-foreground">
                      Filter and organize roadmap items by tags
                    </p>
                  </div>
                  <button
                    onClick={() => setIsFilterModalOpen(true)}
                    className="inline-flex items-center justify-center gap-2 sm:gap-3 px-4 sm:px-6 py-3 bg-primary/10 hover:bg-primary/20 border border-primary/30 hover:border-primary/50 rounded-xl text-sm font-medium transition-all duration-300 hover:shadow-lg hover:scale-105 backdrop-blur-sm"
                  >
                    <Filter className="w-4 h-4" />
                    <span className="hidden sm:inline">Filter Tags</span>
                    <span className="sm:hidden">Filter</span>
                    {activeFilters.length > 0 && (
                      <span className="bg-primary text-primary-foreground text-xs rounded-full px-2 py-1 min-w-[24px] flex items-center justify-center font-bold">
                        {activeFilters.length}
                      </span>
                    )}
                  </button>
                </div>
                
                <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-3 sm:gap-4">
                  {allTags.slice(0, 5).map((tag, index) => {
                    const tagStyle = getTagStyle(tag);
                    const tagLower = tag.toLowerCase();
                    let description = 'Custom tag';

                    if (tagLower.includes('improvement') || tagLower.includes('improvment')) {
                      description = 'Enhancements & improvements';
                    } else if (tagLower.includes('new') || tagLower.includes('requirement')) {
                      description = 'New features & requirements';
                    } else if (tagLower.includes('prototype-6')) {
                      description = 'Current prototype';
                    } else if (tagLower.includes('prototype-5')) {
                      description = 'Previous prototype';
                    } else if (tagLower.includes('bug')) {
                      description = 'Bug fixes & issues';
                    } else if (tagLower.includes('feature')) {
                      description = 'New features & capabilities';
                    }
                    
                    return (
                      <div key={tag} className="flex flex-col items-center p-3 sm:p-4 rounded-xl bg-card/40 border border-border/40 hover:border-border/60 transition-all duration-300 hover:scale-105 group">
                        <span className="px-3 sm:px-4 py-2 rounded-full text-xs sm:text-sm font-medium border mb-2" style={tagStyle}>
                          {tag}
                        </span>
                        <span className="text-xs text-muted-foreground text-center">
                          {description}
                        </span>
                      </div>
                    );
                  })}
                </div>
              </div>
            </div>
          </div>

          <div className={`transition-all duration-1000 delay-600 ${
            isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
          }`}>
            <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 2xl:grid-cols-5 gap-6 sm:gap-8">
              {filteredData.map((column, index) => (
                <div
                  key={column.id}
                  className={`transition-all duration-1000 delay-${(index + 1) * 100} ${
                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                  }`}
                >
                  <RoadmapColumn column={column} columnIndex={index} />
                </div>
              ))}
            </div>
          </div>
        </div>
      </section>

      {isFilterModalOpen && (
        <div className="fixed inset-0 bg-black/50 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-card/95 backdrop-blur-sm border border-border/40 rounded-2xl p-4 sm:p-6 shadow-2xl max-w-md w-full max-h-[80vh] overflow-y-auto">
              <div className="flex items-center justify-between mb-6">
                  <h3 className="text-lg font-bold text-foreground">Filter by Tags</h3>
                <button
                  onClick={() => setIsFilterModalOpen(false)}
                className="text-muted-foreground hover:text-foreground transition-colors duration-200"
                >
                <X className="w-5 h-5" />
                </button>
              </div>

              {activeFilters.length > 0 && (
              <div className="mb-6 p-4 bg-muted/30 rounded-lg">
                <div className="text-sm text-muted-foreground mb-3">Active filters:</div>
                  <div className="flex flex-wrap gap-2">
                  {activeFilters.map((tag) => {
                    const tagStyle = getTagStyle(tag);
                    return (
                      <span
                        key={tag}
                        className="inline-flex items-center gap-1 px-3 py-1.5 rounded-full text-sm font-medium border"
                        style={tagStyle}
                      >
                        {tag}
                        <button
                          onClick={() => toggleFilter(tag)}
                        className="hover:bg-black/10 rounded-full p-0.5 transition-colors duration-200"
                        >
                          <X className="w-3 h-3" />
                        </button>
                      </span>
                    );
                  })}
                  </div>
                  <button
                    onClick={clearAllFilters}
                  className="mt-3 text-xs text-muted-foreground hover:text-foreground transition-colors duration-200 flex items-center gap-1"
                  >
                    <X className="w-3 h-3" />
                    Clear all filters
                  </button>
              </div>
            )}

            <div className="space-y-3 mb-6">
              {allTags.map((tag) => {
                const tagStyle = getTagStyle(tag);
                return (
                  <label
                    key={tag}
                    className="flex items-center gap-3 p-4 rounded-lg hover:bg-muted/30 transition-colors duration-200 cursor-pointer"
                  >
                    <input
                      type="checkbox"
                      checked={activeFilters.includes(tag)}
                      onChange={() => toggleFilter(tag)}
                      className="w-5 h-5 rounded border-border/50 text-blue-500 focus:ring-blue-500 focus:ring-2 focus:ring-offset-2"
                    />
                    <span
                      className="flex-1 text-sm font-medium"
                      style={{ color: tagStyle.color }}
                    >
                      {tag}
                    </span>
                    <div
                      className="w-4 h-4 rounded-full"
                      style={{ backgroundColor: tagStyle.color }}
                    />
                  </label>
                );
              })}
              </div>

              <div className="pt-4 border-t border-border/30">
              <div className="text-sm text-muted-foreground mb-4">
                  Showing {filteredData.reduce((total, column) => total + column.items.length, 0)} items
                  {activeFilters.length > 0 && ` (filtered by ${activeFilters.length} tag${activeFilters.length > 1 ? 's' : ''})`}
              </div>
              <button
                  onClick={() => setIsFilterModalOpen(false)}
                className="w-full px-4 py-2 bg-blue-500 hover:bg-blue-600 text-white rounded-lg font-medium transition-colors duration-200"
                >
                  Apply Filters
              </button>
            </div>
              </div>
            </div>
      )}
    </>
  );
}
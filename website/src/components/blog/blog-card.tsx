'use client';

import Link from 'next/link';
import { Calendar, User, Tag, ArrowRight, Pin } from 'lucide-react';

interface BlogCardProps {
  post: {
    title: string;
    description?: string;
    date?: string;
    author?: string;
    tags?: string[];
    slug: string;
    pinned?: boolean;
  };
  index: number;
  totalPosts: number;
  isPinned?: boolean;
}

export function BlogCard({ post, index, totalPosts, isPinned = false }: BlogCardProps) {
  return (
    <article
      className={`group bg-card/30 backdrop-blur-sm border border-border/50 rounded-xl p-4 sm:p-6 hover:bg-card/40 hover:border-border/70 transition-all duration-300 relative overflow-hidden ${
        isPinned ? 'bg-primary/5 border-primary/20 hover:border-primary/30' : ''
      }`}
    >
      <div className="absolute inset-0 bg-gradient-to-r from-primary/5 via-transparent to-primary/5 opacity-0 group-hover:opacity-100 transition-opacity duration-300" />

      {isPinned && (
        <div className="absolute top-3 sm:top-4 right-3 sm:right-4 flex flex-col gap-1.5 sm:gap-2 items-end z-10">
          <span className="inline-flex items-center gap-1 px-2 sm:px-3 py-1 rounded-full text-xs font-medium bg-primary/20 text-primary border border-primary/30 backdrop-blur-sm">
            <Pin className="w-3 h-3" />
            PINNED
          </span>
        </div>
      )}

      <div className="absolute top-3 sm:top-4 left-3 sm:left-4 flex flex-col gap-1.5 sm:gap-2 items-start z-10">
        <span className="inline-flex items-center justify-center w-6 h-6 sm:w-8 sm:h-8 bg-muted/50 text-muted-foreground rounded-full text-xs sm:text-sm font-bold backdrop-blur-sm">
          {totalPosts - index}
        </span>
      </div>

      <div className="flex flex-wrap items-center gap-2 sm:gap-3 text-xs sm:text-sm text-muted-foreground mb-3 sm:mb-4 pt-6 sm:pt-8">
        {post.date && (
          <div className="flex items-center gap-2 bg-muted/50 px-2 sm:px-3 py-1 rounded-full">
            <Calendar className="w-3 h-3 sm:w-4 sm:h-4" />
            <span className="hidden sm:inline">{new Date(post.date).toLocaleDateString('de-DE', {
              year: 'numeric',
              month: 'long',
              day: 'numeric'
            })}</span>
            <span className="sm:hidden">{new Date(post.date).toLocaleDateString('de-DE', {
              year: 'numeric',
              month: 'short',
              day: 'numeric'
            })}</span>
          </div>
        )}
        {post.author && (
          <div className="flex items-center gap-2 bg-muted/50 px-2 sm:px-3 py-1 rounded-full">
            <User className="w-3 h-3 sm:w-4 sm:h-4" />
            <span>{post.author}</span>
          </div>
        )}
      </div>

      <h2 className="text-lg sm:text-xl md:text-2xl font-bold text-foreground dark:text-white mb-2 sm:mb-3 group-hover:text-primary transition-colors line-clamp-2 leading-tight">
        {post.title}
      </h2>

      {post.description && (
        <p className="text-sm sm:text-base text-muted-foreground mb-3 sm:mb-4 leading-relaxed line-clamp-3">
          {post.description}
        </p>
      )}

      {post.tags && post.tags.length > 0 && (
        <div className="flex flex-wrap gap-1.5 sm:gap-2 mb-4 sm:mb-6">
          {post.tags.slice(0, 3).map((tag) => (
            <span
              key={tag}
              className={`inline-flex items-center gap-1 px-2 sm:px-3 py-1 rounded-full text-xs font-medium border transition-colors ${
                isPinned 
                  ? 'bg-primary/20 text-primary border-primary/30 hover:bg-primary/30' 
                  : 'bg-primary/10 text-primary border-primary/20 hover:bg-primary/20'
              }`}
            >
              <Tag className="w-3 h-3" />
              {tag}
            </span>
          ))}
          {post.tags.length > 3 && (
            <span className="text-xs text-muted-foreground bg-muted/50 px-2 sm:px-3 py-1 rounded-full">
              +{post.tags.length - 3} more
            </span>
          )}
        </div>
      )}

      <div className="relative z-10">
        <Link
          href={`/blog/${post.slug}`}
          className="inline-flex items-center gap-2 text-primary hover:text-primary/80 font-medium transition-colors group/link text-sm"
        >
          Read More
          <ArrowRight className="w-3 h-3 sm:w-4 sm:h-4 group-hover/link:translate-x-1 transition-transform" />
        </Link>
      </div>
    </article>
  );
}

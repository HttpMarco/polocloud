'use client';

import Link from 'next/link';
import { Calendar, User, ArrowRight } from 'lucide-react';

interface ChangelogCardProps {
  post: {
    title: string;
    description?: string;
    date?: string;
    version?: string;
    type?: 'major' | 'minor' | 'patch' | 'hotfix';
    author?: string;
    slug: string;
  };
  index: number;
  totalPosts: number;
}

function getTypeColor(type: string) {
  switch (type) {
    case 'major':
      return 'bg-red-500/10 text-red-500 border-red-500/20';
    case 'minor':
      return 'bg-blue-500/10 text-blue-500 border-blue-500/20';
    case 'patch':
      return 'bg-green-500/10 text-green-500 border-green-500/20';
    case 'hotfix':
      return 'bg-orange-500/10 text-orange-500 border-orange-500/20';
    default:
      return 'bg-gray-500/10 text-gray-500 border-gray-500/20';
  }
}

function getTypeIcon(type: string) {
  switch (type) {
    case 'major':
      return '🚀';
    case 'minor':
      return '✨';
    case 'patch':
      return '🐛';
    case 'hotfix':
      return '🚨';
    default:
      return '📝';
  }
}

export function ChangelogCard({ post, index, totalPosts }: ChangelogCardProps) {
  return (
    <article className="group bg-card/30 backdrop-blur-sm border border-border/50 rounded-xl p-4 sm:p-6 hover:bg-card/40 hover:border-border/70 transition-all duration-300 relative overflow-hidden">
      <div className="absolute inset-0 bg-gradient-to-r from-primary/5 via-transparent to-primary/5 opacity-0 group-hover:opacity-100 transition-opacity duration-300" />

      <div className="absolute top-3 sm:top-4 right-3 sm:right-4 flex flex-col gap-1.5 sm:gap-2 items-end z-10">
        <span className={`inline-flex items-center gap-1 px-2 sm:px-3 py-1 rounded-full text-xs font-medium border backdrop-blur-sm ${getTypeColor(post.type || 'patch')}`}>
          <span className="text-xs">{getTypeIcon(post.type || 'patch')}</span>
          {post.type?.toUpperCase() || 'PATCH'}
        </span>
      </div>

      <div className="flex flex-col lg:flex-row lg:items-start gap-4 sm:gap-6 relative z-10">
        <div className="flex-shrink-0">
          <div className="flex items-center gap-3 mb-3">
            <div className="w-10 h-10 sm:w-12 sm:h-12 bg-gradient-to-br from-primary/20 to-primary/10 rounded-full flex items-center justify-center">
              <span className="text-lg sm:text-xl">{getTypeIcon(post.type || 'patch')}</span>
            </div>
            <div className="flex flex-col">
              <span className="text-sm sm:text-base font-mono text-muted-foreground">
                v{post.version || '0.0.0'}
              </span>
              <span className="text-xs text-muted-foreground/70">
                Release #{totalPosts - index}
              </span>
            </div>
          </div>
        </div>

        <div className="flex-1 min-w-0">
          <div className="flex flex-wrap items-center gap-2 sm:gap-3 text-xs text-muted-foreground mb-3 sm:mb-4">
            {post.date && (
              <div className="flex items-center gap-1.5 bg-muted/50 px-2 sm:px-3 py-1.5 rounded-full">
                <Calendar className="w-3 h-3" />
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
              <div className="flex items-center gap-1.5 bg-muted/50 px-2 sm:px-3 py-1.5 rounded-full">
                <User className="w-3 h-3" />
                <span>{post.author}</span>
              </div>
            )}
          </div>

          <h2 className="text-lg sm:text-xl md:text-2xl font-bold text-foreground dark:text-white mb-3 group-hover:text-primary transition-colors leading-tight">
            {post.title}
          </h2>

          {post.description && (
            <p className="text-sm sm:text-base text-muted-foreground mb-4 sm:mb-6 leading-relaxed line-clamp-2">
              {post.description}
            </p>
          )}
          <Link
            href={`/changelog/${post.slug}`}
            className="inline-flex items-center gap-2 text-primary hover:text-primary/80 font-medium text-sm sm:text-base group/link bg-primary/10 hover:bg-primary/20 px-4 py-2 rounded-lg transition-all duration-200"
          >
            <span>Read Release Notes</span>
            <ArrowRight className="w-4 h-4 group-hover/link:translate-x-1 transition-transform" />
          </Link>
        </div>
      </div>
    </article>
  );
}

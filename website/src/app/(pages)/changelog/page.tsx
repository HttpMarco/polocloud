import Link from 'next/link';
import { Calendar, User, Tag, ArrowRight, MessageCircle, GitBranch } from 'lucide-react';

interface ChangelogPost {
  title: string;
  description?: string;
  date?: string;
  version?: string;
  type?: 'major' | 'minor' | 'patch' | 'hotfix';
  author?: string;
  slug: string;
}

import { getAllChangelogFiles } from '@/lib/github';

async function getChangelogPosts(): Promise<ChangelogPost[]> {
  try {
    console.log('Trying to fetch changelogs directly from GitHub...');
    const posts = await getAllChangelogFiles();
    console.log('Found changelogs from GitHub:', posts.length);
    
    return posts.map((entry) => ({
      title: entry.title,
      description: entry.description,
      date: entry.releaseDate,
      version: entry.version,
      type: entry.type,
      author: entry.author,
      slug: entry.slug,
    }));
  } catch (error) {
    console.error('Error fetching changelog posts from GitHub:', error);
    if (error instanceof Error) {
      console.error('Error details:', {
        message: error.message,
        stack: error.stack,
        name: error.name
      });
    }
  }
  
  console.log('No changelogs found, returning empty array');
  return [];
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

export default async function ChangelogsPage() {
  const posts = await getChangelogPosts();

  return (
    <div className="relative min-h-screen">
      <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]" />
      
      <div className="container mx-auto px-4 sm:px-6 py-8 sm:py-12 max-w-6xl relative z-10">
        <div className="text-center mb-12 sm:mb-16">
          <div className="flex items-center justify-center gap-3 mb-4 sm:mb-6">
            <div className="flex items-center justify-center w-8 h-8 sm:w-10 sm:h-10 bg-primary/10 text-primary rounded-full border border-primary/20">
              <GitBranch className="w-4 h-4 sm:w-5 sm:h-5" />
            </div>
          </div>
          
          <h1 className="text-2xl sm:text-3xl md:text-4xl lg:text-5xl font-black bg-gradient-to-r from-foreground via-primary to-foreground bg-clip-text text-transparent mb-4 sm:mb-6 leading-tight">
            Release Notes
          </h1>
          
          <p className="text-sm sm:text-base md:text-lg text-muted-foreground max-w-3xl mx-auto leading-relaxed px-4 sm:px-0">
            Stay updated with the latest features, improvements, and bug fixes in PoloCloud. 
            Track our development progress and see what&apos;s new in each release.
          </p>
        </div>

        <div className="space-y-3 sm:space-y-4">
          {posts.length === 0 ? (
            <div className="text-center py-8 sm:py-12">
              <GitBranch className="w-12 h-12 sm:w-16 sm:h-16 text-muted-foreground mx-auto mb-3 sm:mb-4" />
              <h3 className="text-lg sm:text-xl font-semibold text-foreground mb-2">No changelog entries yet</h3>
              <p className="text-sm sm:text-base text-muted-foreground">We&apos;ll post release notes here as we ship new versions.</p>
            </div>
          ) : (
            posts.map((post) => (
              <article
                key={post.slug}
                className="group bg-card/30 backdrop-blur-sm border border-border/50 rounded-lg p-3 sm:p-4 hover:bg-card/40 hover:border-border/70 transition-all duration-300 relative"
              >
                <div className="absolute top-2 sm:top-3 right-2 sm:right-3 flex flex-col gap-1.5 sm:gap-2 items-end">
                  <span className={`inline-flex items-center gap-1 px-1.5 py-0.5 rounded-full text-xs font-medium border ${getTypeColor(post.type || 'patch')}`}>
                    {post.type?.toUpperCase() || 'PATCH'}
                  </span>
                </div>

                <div className="flex flex-col lg:flex-row lg:items-start gap-3 sm:gap-4">
                  <div className="flex-shrink-0">
                    <div className="flex items-center gap-2 mb-2">
                      <span className="text-base sm:text-lg">{getTypeIcon(post.type || 'patch')}</span>
                      <div className="flex flex-col">
                        <span className="text-xs font-mono text-muted-foreground">
                          v{post.version || '0.0.0'}
                        </span>
                      </div>
                    </div>
                  </div>

                  <div className="flex-1 min-w-0">
                    <div className="flex flex-wrap items-center gap-2 sm:gap-3 text-xs text-muted-foreground mb-2 sm:mb-3">
                      {post.date && (
                        <div className="flex items-center gap-1 bg-muted/50 px-2 py-1 rounded-full">
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
                    </div>

                    <h2 className="text-base sm:text-lg md:text-xl font-bold text-foreground dark:text-white mb-2 group-hover:text-primary transition-colors">
                      {post.title}
                    </h2>

                    {post.description && (
                      <p className="text-xs sm:text-sm text-muted-foreground mb-2 sm:mb-3 leading-relaxed">
                        {post.description}
                      </p>
                    )}

                    <Link
                      href={`/changelog/${post.slug}`}
                      className="inline-flex items-center gap-1 text-primary hover:text-primary/80 font-medium text-xs sm:text-sm group/link"
                    >
                      Read Release Notes
                      <ArrowRight className="w-3 h-3 group-hover/link:translate-x-1 transition-transform" />
                    </Link>
                  </div>
                </div>
              </article>
            ))
          )}
        </div>

        <div className="mt-12 sm:mt-16 bg-gradient-to-r from-primary/10 via-primary/5 to-primary/10 border border-primary/20 rounded-xl p-4 sm:p-6 text-center">
          <MessageCircle className="w-6 h-6 sm:w-8 sm:w-8 text-primary mx-auto mb-2 sm:mb-3" />
          <h3 className="text-lg sm:text-xl font-bold text-foreground dark:text-white mb-2">
            Join Our Community
          </h3>
          <p className="text-xs sm:text-sm text-muted-foreground mb-3 sm:mb-4 max-w-2xl mx-auto px-2 sm:px-0">
            Get notified about new releases, discuss features, and connect with other PoloCloud users. 
            Never miss an update again!
          </p>
          <a
            href="https://discord.polocloud.de/"
            target="_blank"
            rel="noopener noreferrer"
            className="inline-flex items-center gap-2 bg-primary text-primary-foreground font-medium px-3 sm:px-4 py-2 rounded-lg hover:bg-primary/90 transition-colors text-xs sm:text-sm"
          >
            <MessageCircle className="w-3 h-3 sm:w-4 sm:w-4" />
            <span className="hidden sm:inline">Join Discord Server</span>
            <span className="sm:hidden">Join Discord</span>
          </a>
        </div>
      </div>
    </div>
  );
} 
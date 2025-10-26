import { notFound } from 'next/navigation';
import { PageLayout } from '@/components/layout/page-layout';
import { Calendar, User, Tag, ArrowLeft, GitBranch, ExternalLink, Download } from 'lucide-react';
import { MDXRemote } from 'next-mdx-remote/rsc';
import Link from 'next/link';
import { getAllChangelogFiles } from '@/lib/github';

interface ChangelogPost {
  title: string;
  description?: string;
  date?: string;
  version?: string;
  type?: 'major' | 'minor' | 'patch' | 'hotfix';
  author?: string;
  tags?: string[];
  changes?: string[];
  content: string;
}

async function getChangelogSlugs(): Promise<string[]> {
  try {
    const posts = await getAllChangelogFiles();
    return posts.map(post => post.slug);
  } catch (error) {
    console.error('Error getting changelog slugs:', error);
    return [];
  }
}

async function getChangelogPost(slug: string): Promise<ChangelogPost | null> {
  try {
    const posts = await getAllChangelogFiles();
    const post = posts.find(p => p.slug === slug);
    
    if (!post) {
      return null;
    }
    
    console.log('Content length:', post.content?.length || 0);
    
    return {
      title: post.title,
      description: post.description,
      date: post.releaseDate,
      version: post.version,
      type: post.type,
      author: post.author,
      content: post.content || '',
    };
  } catch (error) {
    console.error('Error getting changelog post:', error);
    return null;
  }
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

export async function generateStaticParams() {
  const slugs = await getChangelogSlugs();
  return slugs.map((slug) => ({
    slug,
  }));
}

export default async function ChangelogPage({ params }: { params: Promise<{ slug: string }> }) {
  const { slug } = await params;
  const post = await getChangelogPost(slug);
  
  if (!post) {
    notFound();
  }

  return (
    <PageLayout>
      <div className="relative min-h-screen">
        <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]" />
        
        <div className="container mx-auto px-4 sm:px-6 py-8 sm:py-12 max-w-4xl relative z-10">
          <div className="mb-6 sm:mb-8">
            <Link
              href="/changelog"
              className="inline-flex items-center gap-2 text-muted-foreground hover:text-foreground transition-colors group"
            >
              <ArrowLeft className="w-4 h-4 group-hover:-translate-x-1 transition-transform" />
              <span className="hidden sm:inline">Back to Changelog</span>
              <span className="sm:hidden">Back</span>
            </Link>
          </div>

          <div className="mb-8 sm:mb-12">
            <div className="flex flex-wrap items-center gap-2 sm:gap-4 text-xs sm:text-sm text-muted-foreground mb-6 sm:mb-8">
              {post.date && (
                <div className="flex items-center gap-2 bg-muted/50 px-3 sm:px-4 py-2 rounded-full">
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
                <div className="flex items-center gap-2 bg-muted/50 px-3 sm:px-4 py-2 rounded-full">
                  <User className="w-3 h-3 sm:w-4 sm:h-4" />
                  <span>{post.author}</span>
                </div>
              )}
              <div className="flex items-center gap-2 bg-primary/10 text-primary px-3 sm:px-4 py-2 rounded-full">
                <GitBranch className="w-3 h-3 sm:w-4 sm:h-4" />
                <span>v{post.version || '0.0.0'}</span>
              </div>
              <div className={`inline-flex items-center gap-2 px-3 sm:px-4 py-2 rounded-full text-sm font-medium border ${getTypeColor(post.type || 'patch')}`}>
                <span className="text-sm">{getTypeIcon(post.type || 'patch')}</span>
                {post.type?.toUpperCase() || 'PATCH'} RELEASE
              </div>
            </div>

            <h1 className="text-3xl sm:text-4xl md:text-5xl lg:text-6xl font-black text-foreground dark:text-white mb-6 sm:mb-8 leading-tight">
              {post.title}
            </h1>

            {post.description && (
              <div className="bg-gradient-to-r from-primary/10 via-primary/5 to-primary/10 border border-primary/20 rounded-xl p-4 sm:p-6 mb-6 sm:mb-8">
                <p className="text-base sm:text-lg text-muted-foreground leading-relaxed">
                  {post.description}
                </p>
              </div>
            )}
          </div>

          <article className="prose prose-sm sm:prose-lg dark:prose-invert max-w-none bg-card/30 backdrop-blur-sm border border-border/50 rounded-xl p-6 sm:p-8 mb-8 sm:mb-12 relative overflow-hidden">
            <div className="absolute inset-0 bg-gradient-to-br from-primary/5 via-transparent to-primary/5 pointer-events-none" />
            
            <div className="relative z-10">
              <MDXRemote 
                source={post.content}
                components={{
                  a: ({ href, children, ...props }) => {
                    if (href?.startsWith('/')) {
                      return <Link href={href} className="text-primary hover:text-primary/80 underline decoration-primary/30 hover:decoration-primary transition-all duration-200" {...props}>{children}</Link>;
                    }
                    return <a href={href} target="_blank" rel="noopener noreferrer" className="text-primary hover:text-primary/80 underline decoration-primary/30 hover:decoration-primary transition-all duration-200 inline-flex items-center gap-1" {...props}>{children}<ExternalLink className="w-3 h-3" /></a>;
                  },
                  h1: ({ children, ...props }) => (
                    <h1 className="text-2xl sm:text-3xl md:text-4xl font-black text-foreground dark:text-white mb-4 sm:mb-6 mt-6 sm:mt-8 first:mt-0 border-b border-border/20 pb-2" {...props}>{children}</h1>
                  ),
                  h2: ({ children, ...props }) => (
                    <h2 className="text-xl sm:text-2xl md:text-3xl font-bold text-foreground dark:text-white mb-3 sm:mb-4 mt-6 sm:mt-8 flex items-center gap-2" {...props}>
                      <span className="w-1 h-6 bg-primary rounded-full"></span>
                      {children}
                    </h2>
                  ),
                  h3: ({ children, ...props }) => (
                    <h3 className="text-lg sm:text-xl md:text-2xl font-bold text-foreground dark:text-white mb-2 sm:mb-3 mt-4 sm:mt-6" {...props}>{children}</h3>
                  ),
                  p: ({ children, ...props }) => (
                    <p className="text-sm sm:text-base text-muted-foreground leading-relaxed mb-3 sm:mb-4" {...props}>{children}</p>
                  ),
                  strong: ({ children, ...props }) => (
                    <strong className="font-bold text-foreground dark:text-white" {...props}>{children}</strong>
                  ),
                  code: ({ children, ...props }) => (
                    <code className="bg-muted text-foreground px-1.5 sm:px-2 py-0.5 sm:py-1 rounded text-xs sm:text-sm font-mono border border-border/30" {...props}>{children}</code>
                  ),
                  pre: ({ children, ...props }) => (
                    <pre className="bg-muted border border-border/50 rounded-lg p-3 sm:p-4 overflow-x-auto mb-3 sm:mb-4 text-xs sm:text-sm relative" {...props}>
                      <div className="absolute top-2 right-2 w-2 h-2 bg-green-500 rounded-full"></div>
                      {children}
                    </pre>
                  ),
                  ul: ({ children, ...props }) => (
                    <ul className="mb-3 sm:mb-4 pl-4 sm:pl-6 space-y-1.5 sm:space-y-2" {...props}>{children}</ul>
                  ),
                  ol: ({ children, ...props }) => (
                    <ol className="mb-3 sm:mb-4 pl-4 sm:pl-6 space-y-1.5 sm:space-y-2" {...props}>{children}</ol>
                  ),
                  li: ({ children, ...props }) => (
                    <li className="text-sm sm:text-base text-muted-foreground flex items-start gap-2" {...props}>
                      <span className="w-1.5 h-1.5 bg-primary rounded-full mt-2 flex-shrink-0"></span>
                      {children}
                    </li>
                  ),
                  blockquote: ({ children, ...props }) => (
                    <blockquote className="border-l-4 border-primary/30 pl-3 sm:pl-4 italic text-sm sm:text-base text-muted-foreground my-4 sm:my-6 bg-primary/5 rounded-r-lg py-2" {...props}>{children}</blockquote>
                  ),
                }}
              />
            </div>
          </article>

          <div className="bg-gradient-to-r from-card/30 via-card/20 to-card/30 backdrop-blur-sm border border-border/50 rounded-xl p-6 sm:p-8 relative overflow-hidden">
            <div className="absolute inset-0 bg-gradient-to-r from-primary/5 via-transparent to-primary/5 pointer-events-none" />
            
            <div className="relative z-10">
              <div className="flex flex-col lg:flex-row items-start lg:items-center justify-between gap-6">
                <div className="flex flex-col gap-4">
                  <h3 className="text-lg sm:text-xl font-bold text-foreground dark:text-white mb-2">
                    Release Information
                  </h3>
                  <div className="flex flex-col gap-3">
                    <div className="flex items-center gap-3 text-sm sm:text-base text-muted-foreground">
                      <div className="w-8 h-8 bg-primary/10 rounded-full flex items-center justify-center">
                        <User className="w-4 h-4 text-primary" />
                      </div>
                      <span>Released by <strong className="text-foreground">{post.author || 'PoloCloud Team'}</strong></span>
                    </div>
                    {post.date && (
                      <div className="flex items-center gap-3 text-sm sm:text-base text-muted-foreground">
                        <div className="w-8 h-8 bg-primary/10 rounded-full flex items-center justify-center">
                          <Calendar className="w-4 h-4 text-primary" />
                        </div>
                        <span>Released on <strong className="text-foreground">{new Date(post.date).toLocaleDateString('de-DE', {
                          year: 'numeric',
                          month: 'long',
                          day: 'numeric'
                        })}</strong></span>
                      </div>
                    )}
                    {post.version && (
                      <div className="flex items-center gap-3 text-sm sm:text-base text-muted-foreground">
                        <div className="w-8 h-8 bg-primary/10 rounded-full flex items-center justify-center">
                          <GitBranch className="w-4 h-4 text-primary" />
                        </div>
                        <span>Version <strong className="text-foreground">{post.version}</strong></span>
                      </div>
                    )}
                  </div>
                </div>

                <div className="flex flex-col sm:flex-row items-start sm:items-center gap-3 sm:gap-4">
                  <a
                    href="https://github.com/HttpMarco/polocloud/releases"
                    target="_blank"
                    rel="noopener noreferrer"
                    className="flex items-center gap-2 bg-primary text-primary-foreground font-medium px-4 py-2 rounded-lg hover:bg-primary/90 transition-colors text-sm"
                  >
                    <Download className="w-4 h-4" />
                    <span>Download Release</span>
                  </a>
                  <Link
                    href="/changelog"
                    className="flex items-center gap-2 bg-muted text-foreground font-medium px-4 py-2 rounded-lg hover:bg-muted/80 transition-colors text-sm"
                  >
                    <ArrowLeft className="w-4 h-4" />
                    <span>All Releases</span>
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </PageLayout>
  );
} 
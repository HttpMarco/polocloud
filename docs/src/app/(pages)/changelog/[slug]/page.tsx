import { notFound } from 'next/navigation';
import { PageLayout } from '@/components/layout/page-layout';
import { Calendar, User, Tag, ArrowLeft, GitBranch, ExternalLink, Download } from 'lucide-react';
import { readFileSync, readdirSync } from 'fs';
import { join } from 'path';
import matter from 'gray-matter';
import { MDXRemote } from 'next-mdx-remote/rsc';
import Link from 'next/link';

interface ChangelogPost {
  title: string;
  description?: string;
  date?: string;
  version?: string;
  type?: 'major' | 'minor' | 'patch';
  author?: string;
  tags?: string[];
  content: string;
}

function getChangelogSlugs(): string[] {
  const changelogDir = join(process.cwd(), 'content', 'changelog');
  const files = readdirSync(changelogDir).filter(file => file.endsWith('.mdx'));
  return files.map(file => file.replace('.mdx', ''));
}

function getChangelogPost(slug: string): ChangelogPost | null {
  try {
    const changelogDir = join(process.cwd(), 'content', 'changelog');
    const filePath = join(changelogDir, `${slug}.mdx`);
    const fileContent = readFileSync(filePath, 'utf8');
    const { data, content } = matter(fileContent);
    
    return {
      title: data.title || 'Untitled',
      description: data.description,
      date: data.date,
      version: data.version,
      type: data.type || 'patch',
      author: data.author,
      tags: data.tags || [],
      content,
    };
  } catch (error) {
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
    default:
      return '📝';
  }
}

export async function generateStaticParams() {
  const slugs = getChangelogSlugs();
  return slugs.map((slug) => ({
    slug,
  }));
}

export default async function ChangelogPage({ params }: { params: Promise<{ slug: string }> }) {
  const { slug } = await params;
  const post = getChangelogPost(slug);
  
  if (!post) {
    notFound();
  }

  return (
    <PageLayout>
      <div className="relative min-h-screen">
        <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]" />
        
        <div className="container mx-auto px-6 py-12 max-w-4xl relative z-10">
          <div className="mb-8">
            <Link
              href="/changelog"
              className="inline-flex items-center gap-2 text-muted-foreground hover:text-foreground transition-colors group"
            >
              <ArrowLeft className="w-4 h-4 group-hover:-translate-x-1 transition-transform" />
              Back to Changelog
            </Link>
          </div>

          <div className="mb-12">
            <div className="flex items-center gap-4 text-sm text-muted-foreground mb-6">
              {post.date && (
                <div className="flex items-center gap-2 bg-muted/50 px-3 py-1 rounded-full">
                  <Calendar className="w-4 h-4" />
                  <span>{new Date(post.date).toLocaleDateString('de-DE', {
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric'
                  })}</span>
                </div>
              )}
              {post.author && (
                <div className="flex items-center gap-2 bg-muted/50 px-3 py-1 rounded-full">
                  <User className="w-4 h-4" />
                  <span>{post.author}</span>
                </div>
              )}
            </div>

            <div className="flex items-center gap-4 mb-6">
              <span className="text-4xl">{getTypeIcon(post.type || 'patch')}</span>
              <div className="flex flex-col">
                <span className="text-lg font-mono text-muted-foreground">
                  v{post.version || '0.0.0'}
                </span>
                <span className={`inline-flex items-center gap-1 px-3 py-1 rounded-full text-sm font-medium border ${getTypeColor(post.type || 'patch')}`}>
                  {post.type?.toUpperCase() || 'PATCH'} RELEASE
                </span>
              </div>
            </div>
            
            <h1 className="text-4xl md:text-5xl font-black text-foreground dark:text-white mb-6 leading-tight">
              {post.title}
            </h1>
            
            {post.description && (
              <p className="text-xl text-muted-foreground mb-8 leading-relaxed max-w-3xl">
                {post.description}
              </p>
            )}
            
            {post.tags && (
              <div className="flex flex-wrap gap-2 mb-8">
                {post.tags.map((tag: string) => (
                  <span
                    key={tag}
                    className="inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-medium bg-primary/10 text-primary border border-primary/20 hover:bg-primary/20 transition-colors"
                  >
                    <Tag className="w-3 h-3" />
                    {tag}
                  </span>
                ))}
              </div>
            )}
          </div>

          <article className="prose prose-lg dark:prose-invert max-w-none bg-card/30 backdrop-blur-sm border border-border/50 rounded-xl p-8 mb-12">
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
                  <h1 className="text-3xl md:text-4xl font-black text-foreground dark:text-white mb-6 mt-8 first:mt-0" {...props}>{children}</h1>
                ),
                h2: ({ children, ...props }) => (
                  <h2 className="text-2xl md:text-3xl font-bold text-foreground dark:text-white mb-4 mt-8" {...props}>{children}</h2>
                ),
                h3: ({ children, ...props }) => (
                  <h3 className="text-xl md:text-2xl font-bold text-foreground dark:text-white mb-3 mt-6" {...props}>{children}</h3>
                ),
                p: ({ children, ...props }) => (
                  <p className="text-muted-foreground leading-relaxed mb-4" {...props}>{children}</p>
                ),
                strong: ({ children, ...props }) => (
                  <strong className="font-bold text-foreground dark:text-white" {...props}>{children}</strong>
                ),
                code: ({ children, ...props }) => (
                  <code className="bg-muted text-foreground px-2 py-1 rounded text-sm font-mono" {...props}>{children}</code>
                ),
                pre: ({ children, ...props }) => (
                  <pre className="bg-muted border border-border/50 rounded-lg p-4 overflow-x-auto mb-4" {...props}>{children}</pre>
                ),
                ul: ({ children, ...props }) => (
                  <ul className="mb-4 pl-6 space-y-2" {...props}>{children}</ul>
                ),
                ol: ({ children, ...props }) => (
                  <ol className="mb-4 pl-6 space-y-2" {...props}>{children}</ol>
                ),
                li: ({ children, ...props }) => (
                  <li className="text-muted-foreground" {...props}>{children}</li>
                ),
                blockquote: ({ children, ...props }) => (
                  <blockquote className="border-l-4 border-primary/30 pl-4 italic text-muted-foreground my-6" {...props}>{children}</blockquote>
                ),
              }}
            />
          </article>

          <div className="bg-card/30 backdrop-blur-sm border border-border/50 rounded-xl p-6">
            <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
              <div className="flex flex-col gap-2">
                <div className="flex items-center gap-2 text-sm text-muted-foreground">
                  <User className="w-4 h-4" />
                  <span>Released by <strong className="text-foreground">{post.author || 'PoloCloud Team'}</strong></span>
                </div>
                {post.date && (
                  <div className="flex items-center gap-2 text-sm text-muted-foreground">
                    <Calendar className="w-4 h-4" />
                    <span>Released on <strong className="text-foreground">{new Date(post.date).toLocaleDateString('de-DE')}</strong></span>
                  </div>
                )}
                {post.version && (
                  <div className="flex items-center gap-2 text-sm text-muted-foreground">
                    <GitBranch className="w-4 h-4" />
                    <span>Version <strong className="text-foreground">{post.version}</strong></span>
                  </div>
                )}
              </div>
              <div className="flex items-center gap-4">
                <a
                  href="https://github.com/HttpMarco/polocloud/releases"
                  target="_blank"
                  rel="noopener noreferrer"
                  className="flex items-center gap-2 text-muted-foreground hover:text-foreground transition-colors bg-muted/50 px-3 py-2 rounded-lg hover:bg-muted"
                >
                  <Download className="w-4 h-4" />
                  Download Release
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </PageLayout>
  );
} 
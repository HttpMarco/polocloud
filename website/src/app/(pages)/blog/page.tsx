import Link from 'next/link';
import { MessageCircle, FileText } from 'lucide-react';
import { BlogList } from '@/components/blog/blog-list';

interface BlogPost {
  title: string;
  description?: string;
  date?: string;
  author?: string;
  tags?: string[];
  slug: string;
  pinned?: boolean;
}

import { getAllBlogFiles } from '@/lib/github';

async function getBlogPosts(): Promise<BlogPost[]> {
  try {
    console.log('Trying to fetch blogs directly from GitHub...');
    const posts = await getAllBlogFiles();
    console.log('Found blogs from GitHub:', posts.length);
    
    return posts.map((entry) => ({
      title: entry.title,
      description: entry.description,
      date: entry.date,
      author: entry.author,
      tags: entry.tags || [],
      slug: entry.slug,
      pinned: entry.pinned || false,
    }));
  } catch (error) {
    console.error('Error fetching blog posts from GitHub:', error);
    if (error instanceof Error) {
      console.error('Error details:', {
        message: error.message,
        stack: error.stack,
        name: error.name
      });
    }
  }
  
  console.log('No blogs found, returning empty array');
  return [];
}

export default async function BlogIndexPage() {
  const blogPosts = await getBlogPosts();

  const pinnedPosts = blogPosts.filter(post => post.pinned);
  const unpinnedPosts = blogPosts.filter(post => !post.pinned);

  const sortedPinnedPosts = [...pinnedPosts].sort((a, b) => {
    if (!a.date || !b.date) return 0;
    return new Date(b.date).getTime() - new Date(a.date).getTime();
  });

  const sortedUnpinnedPosts = [...unpinnedPosts].sort((a, b) => {
    if (!a.date || !b.date) return 0;
    return new Date(b.date).getTime() - new Date(a.date).getTime();
  });

  return (
      <div className="relative min-h-screen">
        <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]" />
        
        <div className="container mx-auto px-4 sm:px-6 py-8 sm:py-12 max-w-6xl relative z-10">
          <div className="text-center mb-12 sm:mb-16">
            <div className="flex items-center justify-center gap-3 mb-4 sm:mb-6">
              <div className="flex items-center justify-center w-8 h-8 sm:w-10 sm:h-10 bg-primary/10 text-primary rounded-full border border-primary/20">
                <FileText className="w-4 h-4 sm:w-5 sm:h-5" />
              </div>
            </div>
            <h1 className="text-2xl sm:text-3xl md:text-4xl lg:text-5xl font-black bg-gradient-to-r from-foreground via-primary to-foreground bg-clip-text text-transparent mb-4 sm:mb-6 leading-tight">
              PoloCloud Blog
            </h1>
            <p className="text-sm sm:text-base md:text-lg text-muted-foreground max-w-3xl mx-auto leading-relaxed px-4 sm:px-0">
              Stay updated with the latest news, tutorials, and insights about Minecraft server hosting and PoloCloud development.
            </p>
          </div>

          <BlogList pinnedPosts={sortedPinnedPosts} unpinnedPosts={sortedUnpinnedPosts} />

          <div className="mt-12 sm:mt-16 bg-gradient-to-r from-primary/10 via-primary/5 to-primary/10 border border-primary/20 rounded-xl p-4 sm:p-6 text-center">
            <MessageCircle className="w-6 h-6 sm:w-8 sm:h-8 text-primary mx-auto mb-2 sm:mb-3" />
            <h3 className="text-lg sm:text-xl font-bold text-foreground dark:text-white mb-2">
              Join Our Community
            </h3>
            <p className="text-xs sm:text-sm text-muted-foreground mb-3 sm:mb-4 max-w-2xl mx-auto px-2 sm:px-0">
              Get notified about new blog posts, discuss features, and connect with other PoloCloud users. 
              Never miss an update again!
            </p>
            <a
              href="https://discord.polocloud.de/"
              target="_blank"
              rel="noopener noreferrer"
              className="inline-flex items-center gap-2 bg-primary text-primary-foreground font-medium px-3 sm:px-4 py-2 rounded-lg hover:bg-primary/90 transition-colors text-xs sm:text-sm"
            >
              <MessageCircle className="w-3 h-3 sm:w-4 sm:h-4" />
              <span className="hidden sm:inline">Join Discord Server</span>
              <span className="sm:hidden">Join Discord</span>
            </a>
          </div>
        </div>
      </div>
  );
} 
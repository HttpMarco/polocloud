import { NextResponse } from 'next/server';
import { getAllBlogFiles } from '@/lib/github';

interface BlogPost {
  slug: string;
  title: string;
  description: string;
  date: string;
  author: string;
  tags: string[];
  pinned: boolean;
  contentPreview: string;
  wordCount: number;
}

let blogCache: BlogPost[] | null = null;
let blogCacheTimestamp = 0;
const GITHUB_CACHE_DURATION = 5 * 60 * 1000;

export async function GET() {
  try {
    const now = Date.now();

    if (blogCache && (now - blogCacheTimestamp) < GITHUB_CACHE_DURATION) {
      const response = NextResponse.json({ posts: blogCache });
      response.headers.set('Cache-Control', 'public, max-age=300');
      return response;
    }

    const posts = await getAllBlogFiles();

    const apiPosts: BlogPost[] = posts.map(post => ({
      slug: post.slug,
      title: post.title,
      description: post.description,
      date: post.date,
      author: post.author,
      tags: post.tags,
      pinned: post.pinned,
      contentPreview: post.content ? post.content.substring(0, 150) + (post.content.length > 150 ? '...' : '') : '',
      wordCount: post.content ? post.content.split(/\s+/).filter(word => word.length > 0).length : 0,
    }));

    blogCache = apiPosts;
    blogCacheTimestamp = now;

    const response = NextResponse.json({ posts: apiPosts });
    response.headers.set('Cache-Control', 'public, max-age=300');
    return response;

  } catch (error) {
    console.error('Error fetching blog posts:', error);
    
    if (blogCache) {
      return NextResponse.json({ posts: blogCache });
    }

    return NextResponse.json({
      error: 'Failed to load blog posts',
      details: error instanceof Error ? error.message : 'Unknown error'
    }, { status: 500 });
  }
}

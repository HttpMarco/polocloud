import { NextResponse } from 'next/server';
import { getFileFromGitHub, GITHUB_REPO_CONFIG, BlogMeta } from '@/lib/github';
import matter from 'gray-matter';

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

    const metaFile = await getFileFromGitHub(GITHUB_REPO_CONFIG.metaFile);

    if (!metaFile) {
      return NextResponse.json({ posts: [] });
    }

    const meta: BlogMeta = JSON.parse(metaFile.content);
    const blogSection = meta.pages.find(p => p.title === "Blog");

    if (!blogSection) {
      return NextResponse.json({ posts: [] });
    }

    const posts = await Promise.all(
      blogSection.pages.map(async (page) => {
        try {
          const slug = page.url.replace('/blog/', '');
          const filePath = `${GITHUB_REPO_CONFIG.blogPath}/${slug}.mdx`;

          const file = await getFileFromGitHub(filePath);
          if (!file) return null;

          let frontmatter: Record<string, unknown>;
          let content: string;

          try {
            const parsed = matter(file.content);
            frontmatter = parsed.data;
            content = parsed.content;
          } catch (yamlError) {

            return null;
          }

          const preview = content.substring(0, 150);
          const wordCount = content.split(/\s+/).filter(word => word.length > 0).length;

          return {
            slug,
            title: (frontmatter.title as string) || page.title,
            description: (frontmatter.description as string) || '',
            date: (frontmatter.date as string) || '',
            author: (frontmatter.author as string) || '',
            tags: (frontmatter.tags as string[]) || [],
            pinned: (frontmatter.pinned as boolean) || false,
            contentPreview: preview + (content.length > 150 ? '...' : ''),
            wordCount,
          };
        } catch (error) {
          return null;
        }
      })
    );

    const validPosts = posts.filter(post => post !== null)
      .sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());

    blogCache = validPosts;
    blogCacheTimestamp = now;

    const response = NextResponse.json({ posts: validPosts });
    response.headers.set('Cache-Control', 'public, max-age=300');
    return response;

  } catch (error) {

    if (blogCache) {
      return NextResponse.json({ posts: blogCache });
    }

    return NextResponse.json({
      error: 'Failed to load blog posts',
      details: error instanceof Error ? error.message : 'Unknown error'
    }, { status: 500 });
  }
}

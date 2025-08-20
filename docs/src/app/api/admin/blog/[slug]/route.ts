import { NextRequest, NextResponse } from 'next/server';
import { getFileFromGitHub, GITHUB_REPO_CONFIG } from '@/lib/github';
import matter from 'gray-matter';

interface BlogPostData {
    slug: string;
    title: string;
    description: string;
    date: string;
    author: string;
    tags: string[];
    pinned: boolean;
    content: string;
}

const postCache = new Map<string, { data: BlogPostData; timestamp: number }>();
const POST_CACHE_DURATION = 10 * 60 * 1000;

export async function GET(
    request: NextRequest,
    { params }: { params: Promise<{ slug: string }> }
) {
    try {
        const { slug } = await params;

        if (!slug) {
            return NextResponse.json({ error: 'Slug is required' }, { status: 400 });
        }

        const now = Date.now();

        const cached = postCache.get(slug);
        if (cached && (now - cached.timestamp) < POST_CACHE_DURATION) {
            const response = NextResponse.json({ post: cached.data });
            response.headers.set('Cache-Control', 'public, max-age=600');
            return response;
        }

        const filePath = `${GITHUB_REPO_CONFIG.blogPath}/${slug}.mdx`;
        const file = await getFileFromGitHub(filePath);

        if (!file) {
            return NextResponse.json({ error: 'Blog post not found' }, { status: 404 });
        }

        let frontmatter: Record<string, unknown>;
        let content: string;

        try {
            const parsed = matter(file.content);
            frontmatter = parsed.data;
            content = parsed.content;
        } catch (yamlError) {
            return NextResponse.json({
                error: 'Invalid blog post format',
                details: 'YAML frontmatter could not be parsed'
            }, { status: 400 });
        }

        const post: BlogPostData = {
            slug,
            title: (frontmatter.title as string) || 'Untitled',
            description: (frontmatter.description as string) || '',
            date: (frontmatter.date as string) || '',
            author: (frontmatter.author as string) || '',
            tags: (frontmatter.tags as string[]) || [],
            pinned: (frontmatter.pinned as boolean) || false,
            content: content.trim(),
        };

        postCache.set(slug, { data: post, timestamp: now });

        const response = NextResponse.json({ post });
        response.headers.set('Cache-Control', 'public, max-age=600');
        return response;

    } catch (error) {
        return NextResponse.json({
            error: 'Failed to load blog post',
            details: error instanceof Error ? error.message : 'Unknown error'
        }, { status: 500 });
    }
}
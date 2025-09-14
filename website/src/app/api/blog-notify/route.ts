import { NextRequest, NextResponse } from 'next/server';
import { readdirSync, readFileSync } from 'fs';
import { join } from 'path';
import matter from 'gray-matter';

interface BlogPost {
  title: string;
  description?: string;
  date?: string;
  author?: string;
  tags?: string[];
  slug: string;
  pinned?: boolean;
}

function getBlogPosts(): BlogPost[] {
  const blogDir = join(process.cwd(), 'content', 'blog');
  const files = readdirSync(blogDir).filter(file => file.endsWith('.mdx') && file !== 'meta.json');
  
  const posts: BlogPost[] = [];
  
  for (const file of files) {
    const filePath = join(blogDir, file);
    const fileContent = readFileSync(filePath, 'utf8');
    const { data } = matter(fileContent);
    const slug = file.replace('.mdx', '');
    
    posts.push({
      title: data.title || 'Untitled',
      description: data.description,
      date: data.date,
      author: data.author,
      tags: data.tags || [],
      slug,
      pinned: data.pinned || false,
    });
  }
  
  return posts;
}

async function sendDiscordNotification(post: BlogPost) {
  const webhookUrl = process.env.DISCORD_WEBHOOK_URL;
  
  if (!webhookUrl) {
    throw new Error('DISCORD_WEBHOOK_URL environment variable not set');
  }

  const formattedDate = post.date 
    ? new Date(post.date).toLocaleDateString('de-DE')
    : 'Heute';

  const embed = {
    title: post.pinned 
      ? `📌 ANGEHEFTET: 📝 Neuer Blog-Post: ${post.title}`
      : `📝 Neuer Blog-Post: ${post.title}`,
    description: post.description || '',
    url: `https://polocloud.de/blog/${post.slug}`,
    color: post.pinned ? 16776960 : 3447003,
    fields: [
      {
        name: '👤 Autor',
        value: post.author || 'PoloCloud Team',
        inline: true
      },
      {
        name: '📅 Datum',
        value: formattedDate,
        inline: true
      }
    ],
    footer: {
      text: 'PoloCloud Blog'
    },
    timestamp: new Date().toISOString()
  };

  if (post.tags && post.tags.length > 0) {
    embed.fields.push({
      name: '🏷️ Tags',
      value: post.tags.join(', '),
      inline: false
    });
  }

  const payload = {
    embeds: [embed]
  };

  const response = await fetch(webhookUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    throw new Error(`Discord webhook failed: ${response.statusText}`);
  }

  return response.json();
}

export async function POST(request: NextRequest) {
  try {
    const { slug } = await request.json();
    
    if (!slug) {
      return NextResponse.json(
        { error: 'Slug is required' },
        { status: 400 }
      );
    }

    const posts = getBlogPosts();
    const post = posts.find(p => p.slug === slug);
    
    if (!post) {
      return NextResponse.json(
        { error: 'Blog post not found' },
        { status: 404 }
      );
    }

    await sendDiscordNotification(post);

    return NextResponse.json({
      success: true,
      message: `Discord notification sent for: ${post.title}`
    });

  } catch (error) {
    console.error('Error sending Discord notification:', error);
    return NextResponse.json(
      { error: 'Failed to send Discord notification' },
      { status: 500 }
    );
  }
}

export async function GET() {
  try {
    const posts = getBlogPosts();
    
    return NextResponse.json({
      success: true,
      posts: posts.map(post => ({
        slug: post.slug,
        title: post.title,
        pinned: post.pinned
      }))
    });

  } catch (error) {
    console.error('Error getting blog posts:', error);
    return NextResponse.json(
      { error: 'Failed to get blog posts' },
      { status: 500 }
    );
  }
} 
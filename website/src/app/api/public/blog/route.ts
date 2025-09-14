import { NextResponse } from 'next/server';
import { getAllBlogFiles } from '@/lib/github';

export async function GET() {
  try {
    console.log('Fetching blogs from GitHub...');
    const blogData = await getAllBlogFiles();
    console.log('Blog data:', blogData);
    
    if (!blogData || !Array.isArray(blogData)) {
      console.log('No blog data or invalid format, returning empty array');
      return NextResponse.json({ posts: [] });
    }
    
    return NextResponse.json({ posts: blogData });
  } catch (error) {
    console.error('Error listing blog entries:', error);
    console.error('Error details:', {
      message: error instanceof Error ? error.message : 'Unknown error',
      stack: error instanceof Error ? error.stack : 'No stack trace',
      name: error instanceof Error ? error.name : 'Unknown error type'
    });
    return NextResponse.json({ blog: [] });
  }
}

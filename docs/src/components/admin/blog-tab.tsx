"use client";

import { useState, useEffect } from 'react';
import { Plus, Edit, Trash2, Calendar, User, FileText, Sparkles, Eye, ExternalLink } from 'lucide-react';
import { useRouter } from 'next/navigation';

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

export function BlogTab() {
  const router = useRouter();
  const [posts, setPosts] = useState<BlogPost[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadPosts();
  }, []);

  const loadPosts = async () => {
    try {
      setLoading(true);
      const response = await fetch('/api/admin/blog/list', {
        credentials: 'include',
      });

      if (response.ok) {
        const data = await response.json();
        setPosts(data.posts || []);
      } else {
        console.error('Failed to load blog posts');
      }
    } catch (error) {
      console.error('Error loading blog posts:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (slug: string) => {
    if (!confirm('Are you sure you want to delete this blog post?')) return;

    try {
      const response = await fetch('/api/admin/blog/delete', {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ slug })
      });

      if (response.ok) {
        await loadPosts();
      } else {
        const error = await response.json();
        alert(`Error: ${error.error}`);
      }
    } catch (error) {
      console.error('Error deleting blog post:', error);
      alert('Failed to delete blog post');
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center p-6 sm:p-8">
        <div className="animate-spin rounded-full h-6 w-6 sm:h-8 sm:w-8 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="space-y-4 sm:space-y-6">
      <div className="flex flex-col sm:flex-row sm:justify-between sm:items-center gap-4 sm:gap-0">
        <h2 className="text-xl sm:text-2xl font-bold">Blog Management</h2>
        <div className="flex flex-col sm:flex-row gap-2 sm:gap-3">
          <button
            onClick={() => router.push('/admin/blog/create')}
            className="flex items-center justify-center gap-2 px-3 sm:px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:bg-primary/90 transition-colors text-sm"
          >
            <Plus className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
            <span className="hidden sm:inline">Create New Post</span>
            <span className="sm:hidden">Create Post</span>
          </button>
          <button
            onClick={loadPosts}
            className="flex items-center justify-center gap-2 px-3 sm:px-4 py-2 border border-border text-foreground rounded-lg hover:bg-muted transition-colors text-sm"
          >
            <FileText className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
            <span className="hidden sm:inline">Refresh</span>
            <span className="sm:hidden">Ref</span>
          </button>
        </div>
      </div>

      <div className="space-y-3 sm:space-y-4">
        {posts.length === 0 ? (
          <div className="text-center py-8 sm:py-12 text-muted-foreground">
            <FileText className="w-8 h-8 sm:w-12 sm:h-12 mx-auto mb-3 sm:mb-4 opacity-50" />
            <p className="text-base sm:text-lg">No blog posts yet</p>
            <p className="text-sm mb-3 sm:mb-4">Create your first blog post to get started</p>
            <button
              onClick={() => router.push('/admin/blog/create')}
              className="inline-flex items-center justify-center gap-2 px-3 sm:px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:bg-primary/90 transition-colors text-sm"
            >
              <Plus className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
              <span className="hidden sm:inline">Create First Post</span>
              <span className="sm:hidden">Create Post</span>
            </button>
          </div>
        ) : (
          posts.map((post) => (
            <div key={post.slug} className="bg-card border rounded-lg p-4 sm:p-6">
              <div className="flex flex-col sm:flex-row sm:justify-between sm:items-start gap-3 sm:gap-0 mb-3 sm:mb-4">
                <div className="flex flex-col sm:flex-row sm:items-center gap-2 sm:gap-3">
                  {post.pinned && (
                    <div className="bg-primary/10 text-primary px-2 py-1 rounded-full text-xs font-medium">
                      <Sparkles className="w-3 h-3 inline mr-1" />
                      Pinned
                    </div>
                  )}
                  <h3 className="text-lg sm:text-xl font-semibold">{post.title}</h3>
                </div>
                
                <div className="flex gap-2">
                  <button
                    onClick={() => window.open('/blog/' + post.slug, '_blank')}
                    className="p-2 text-muted-foreground hover:text-foreground hover:bg-muted rounded-lg transition-colors"
                    title="View post"
                  >
                    <Eye className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
                  </button>
                  <button
                    onClick={() => router.push(`/admin/blog/edit/${post.slug}`)}
                    className="p-2 text-muted-foreground hover:text-foreground hover:bg-muted rounded-lg transition-colors"
                    title="Edit post"
                  >
                    <Edit className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
                  </button>
                  <button
                    onClick={() => handleDelete(post.slug)}
                    className="p-2 text-red-600 hover:text-red-700 hover:bg-red-50 rounded-lg transition-colors"
                    title="Delete post"
                  >
                    <Trash2 className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
                  </button>
                </div>
              </div>

              {post.description && (
                <p className="text-sm sm:text-base text-muted-foreground mb-3 sm:mb-4">{post.description}</p>
              )}

              <div className="flex flex-col sm:flex-row sm:items-center gap-2 sm:gap-4 text-xs sm:text-sm text-muted-foreground mb-3 sm:mb-4">
                <div className="flex items-center gap-1">
                  <Calendar className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
                  {new Date(post.date).toLocaleDateString()}
                </div>
                <div className="flex items-center gap-1">
                  <User className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
                  {post.author}
                </div>
                <div className="flex items-center gap-1">
                  <FileText className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
                  {post.wordCount} words
                </div>
              </div>

              {post.tags.length > 0 && (
                <div className="flex flex-wrap gap-1.5 sm:gap-2">
                  {post.tags.map((tag) => (
                    <span
                      key={tag}
                      className="px-2 py-1 bg-muted/50 text-muted-foreground text-xs rounded-full"
                    >
                      {tag}
                    </span>
                  ))}
                </div>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
}

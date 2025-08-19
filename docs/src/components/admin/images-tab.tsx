"use client";

import { useState } from 'react';
import { ImageFile, NewImageFile } from './types';
import { Copy, Upload, Trash2, Image as ImageIcon, Filter, Search, RefreshCcw } from 'lucide-react';

interface ImagesTabProps {
  images: ImageFile[];
  loadingImages: boolean;
  onRefresh: () => void;
}

export function ImagesTab({
  images,
  loadingImages,
  onRefresh
}: ImagesTabProps) {
  const [newImage, setNewImage] = useState<NewImageFile>({
    file: null as File | null,
    category: 'general'
  });
  const [uploading, setUploading] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [categoryFilter, setCategoryFilter] = useState<string>('all');
  const [deletingImage, setDeletingImage] = useState<string | null>(null);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      if (file.size > 5 * 1024 * 1024) {
        alert('File too large. Maximum size is 5MB.');
        return;
      }
      setNewImage({ ...newImage, file });
    }
  };

  const handleUpload = async () => {
    if (!newImage.file) return;

    setUploading(true);
    try {
      const formData = new FormData();
      formData.append('file', newImage.file);
      formData.append('category', newImage.category || 'general');

      console.log('📤 Uploading image:', {
        fileName: newImage.file.name,
        fileSize: newImage.file.size,
        category: newImage.category
      });

      const res = await fetch('/api/admin/images', {
        method: 'POST',
        credentials: 'include',
        body: formData
      });

      if (!res.ok) {
        const error = await res.json();
        throw new Error(`Upload failed: ${error.error}`);
      }

      const result = await res.json();
      console.log('Upload successful:', result);

      setNewImage({ file: null, category: 'general' });

      onRefresh();
      
      alert('Image uploaded successfully!');
    } catch (error) {
      console.error('Error uploading image:', error);
      alert(`Upload failed: ${error instanceof Error ? error.message : 'Unknown error'}`);
    } finally {
      setUploading(false);
    }
  };

  const handleDelete = async (blobId: string) => {
    if (!confirm('Are you sure you want to delete this image?')) return;

    setDeletingImage(blobId);
    try {
      const res = await fetch('/api/admin/images', {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ blobId })
      });

      if (!res.ok) {
        const error = await res.json();
        throw new Error(`Delete failed: ${error.error}`);
      }

      onRefresh();
      alert('Image deleted successfully!');
    } catch (error) {
      console.error('Error deleting image:', error);
      alert(`Delete failed: ${error instanceof Error ? error.message : 'Unknown error'}`);
    } finally {
      setDeletingImage(null);
    }
  };

  const copyToClipboard = (text: string) => {
    navigator.clipboard.writeText(text);
    alert('Copied to clipboard!');
  };

  const formatFileSize = (bytes: number) => {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  };

  const filteredImages = images.filter(image => {
    const matchesQuery = !searchQuery || 
      image.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      image.category?.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesCategory = categoryFilter === 'all' || image.category === categoryFilter;
    return matchesQuery && matchesCategory;
  });

  const categories = ['all', 'general', 'partners', 'platforms', 'blog', 'changelog'];

  return (
    <div className="mb-4 sm:mb-6 p-4 sm:p-6 bg-gradient-to-br from-blue-500/5 via-transparent to-blue-500/5 border border-blue-500/20 rounded-xl">
      <div className="flex flex-col sm:flex-row sm:items-center gap-3 mb-4">
        <div className="flex items-center gap-2 sm:gap-3">
          <ImageIcon className="w-5 h-5 text-blue-500" />
          <h3 className="text-base sm:text-lg font-semibold text-foreground">Image Management</h3>
        </div>
        <button
          onClick={onRefresh}
          className="sm:ml-auto inline-flex items-center gap-2 px-2.5 sm:px-3 py-1 bg-blue-600/10 text-blue-600 rounded-lg hover:bg-blue-600/20 text-xs sm:text-sm transition-all duration-300"
        >
          <RefreshCcw className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
          <span className="hidden sm:inline">Refresh</span>
          <span className="sm:hidden">Ref</span>
        </button>
      </div>

      <div className="mb-4 sm:mb-6 p-3 sm:p-4 bg-background/30 rounded-lg border border-border/30">
        <h4 className="font-medium text-foreground text-sm sm:text-base mb-3">
          Upload New Image
        </h4>
        
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-3 sm:gap-4">
          <div>
            <label className="block text-xs sm:text-sm font-medium text-foreground mb-1">
              Image File *
            </label>
            <input
              type="file"
              accept="image/png,image/jpeg,image/svg+xml,image/webp"
              onChange={handleFileChange}
              className="w-full px-3 py-2 bg-background border border-border/50 rounded-lg text-foreground file:mr-4 file:py-1 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-blue-600 file:text-white hover:file:bg-blue-700"
            />
            {newImage.file && (
              <div className="text-xs text-blue-600 mt-1">
                ✓ {newImage.file.name} ({formatFileSize(newImage.file.size)})
              </div>
            )}
          </div>
          
          <div>
            <label className="block text-xs sm:text-sm font-medium text-foreground mb-1">
              Category
            </label>
            <select
              value={newImage.category}
              onChange={(e) => setNewImage({ ...newImage, category: e.target.value as 'general' | 'partners' | 'platforms' | 'blog' | 'changelog' })}
              className="w-full px-3 py-2 bg-background border border-border/50 rounded-lg text-foreground focus:outline-none focus:ring-2 focus:ring-blue-500/50 text-sm sm:text-base"
            >
              <option value="general">General</option>
              <option value="partners">Partners</option>
              <option value="platforms">Platforms</option>
              <option value="blog">Blog</option>
              <option value="changelog">Changelog</option>
            </select>
          </div>
          
          <div className="flex items-end">
            <button
              onClick={handleUpload}
              disabled={!newImage.file || uploading}
              className="w-full px-4 py-2 bg-blue-600 text-white rounded-lg font-medium hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2 text-sm sm:text-base transition-all duration-300"
            >
              {uploading ? (
                <>
                  <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
                  Uploading...
                </>
              ) : (
                <>
                  <Upload className="w-4 h-4" />
                  Upload
                </>
              )}
            </button>
          </div>
        </div>
      </div>

      <div className="mb-4 sm:mb-6 p-3 sm:p-4 bg-background/30 rounded-lg border border-border/30">
        <div className="flex flex-col sm:flex-row gap-3 sm:gap-4 items-start sm:items-center">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
            <input
              type="text"
              placeholder="Search images..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full pl-10 pr-4 py-2 bg-background border border-border/50 rounded-lg text-foreground focus:outline-none focus:ring-2 focus:ring-blue-500/50 text-sm sm:text-base"
            />
          </div>
          
          <div className="flex items-center gap-2">
            <Filter className="w-4 h-4 text-muted-foreground" />
            <select
              value={categoryFilter}
              onChange={(e) => setCategoryFilter(e.target.value)}
              className="px-3 py-2 bg-background border border-border/50 rounded-lg text-foreground focus:outline-none focus:ring-2 focus:ring-blue-500/50 text-sm sm:text-base"
            >
              {categories.map(category => (
                <option key={category} value={category}>
                  {category === 'all' ? 'All Categories' : category.charAt(0).toUpperCase() + category.slice(1)}
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>

      <div className="bg-background/30 rounded-lg border border-border/30 overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-muted/30 border-b border-border/50">
              <tr>
                <th className="px-4 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
                  Preview
                </th>
                <th className="px-4 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
                  Name
                </th>
                <th className="px-4 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
                  Category
                </th>
                <th className="px-4 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
                  Size
                </th>
                <th className="px-4 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
                  Uploaded
                </th>
                <th className="px-4 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-border/50">
              {loadingImages ? (
                <tr>
                  <td colSpan={6} className="px-4 py-8 text-center text-muted-foreground">
                    <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-blue-500 mx-auto mb-2"></div>
                    Loading images...
                  </td>
                </tr>
              ) : filteredImages.length === 0 ? (
                <tr>
                  <td colSpan={6} className="px-4 py-8 text-center text-muted-foreground">
                    <ImageIcon className="w-8 h-8 mx-auto mb-2 opacity-50" />
                    No images found
                  </td>
                </tr>
              ) : (
                filteredImages.map((image) => (
                  <tr key={image.id} className="hover:bg-muted/20">
                    <td className="px-4 py-3">
                      <div className="w-12 h-12 rounded-lg overflow-hidden bg-muted/30 border border-border/50">
                        <img
                          src={image.url}
                          alt={image.name}
                          className="w-full h-full object-cover"
                          onError={(e) => {
                            const target = e.target as HTMLImageElement;
                            target.style.display = 'none';
                            target.parentElement!.innerHTML = '<div class="w-full h-full flex items-center justify-center text-muted-foreground text-xs">Error</div>';
                          }}
                        />
                      </div>
                    </td>
                    <td className="px-4 py-3">
                      <div className="text-sm font-medium text-foreground">{image.name}</div>
                      <div className="text-xs text-muted-foreground">{image.type}</div>
                    </td>
                    <td className="px-4 py-3">
                      <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200">
                        {image.category}
                      </span>
                    </td>
                    <td className="px-4 py-3 text-sm text-muted-foreground">
                      {formatFileSize(image.size)}
                    </td>
                    <td className="px-4 py-3 text-sm text-muted-foreground">
                      {new Date(image.uploadedAt).toLocaleDateString()}
                    </td>
                    <td className="px-4 py-3">
                      <div className="flex items-center gap-2">
                        <button
                          onClick={() => copyToClipboard(image.url)}
                          className="p-1.5 text-muted-foreground hover:text-foreground hover:bg-muted/50 rounded transition-colors"
                          title="Copy URL"
                        >
                          <Copy className="w-4 h-4" />
                        </button>
                        <button
                          onClick={() => copyToClipboard(image.blobId)}
                          className="p-1.5 text-muted-foreground hover:text-foreground hover:bg-muted/50 rounded transition-colors"
                          title="Copy Blob ID"
                        >
                          <ImageIcon className="w-4 h-4" />
                        </button>
                        <button
                          onClick={() => handleDelete(image.blobId)}
                          disabled={deletingImage === image.blobId}
                          className="p-1.5 text-red-500 hover:text-red-700 hover:bg-red-50 dark:hover:bg-red-900/20 rounded transition-colors disabled:opacity-50"
                          title="Delete image"
                        >
                          {deletingImage === image.blobId ? (
                            <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-red-500"></div>
                          ) : (
                            <Trash2 className="w-4 h-4" />
                          )}
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      <div className="mt-4 text-sm text-muted-foreground text-center">
        Showing {filteredImages.length} of {images.length} images
        {categoryFilter !== 'all' && ` in ${categoryFilter} category`}
      </div>
    </div>
  );
}

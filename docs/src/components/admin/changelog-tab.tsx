"use client";

import { useState, useEffect } from 'react';
import { Plus, Edit, Trash2, Calendar, User, GitBranch, ExternalLink } from 'lucide-react';
import { ChangelogEntry, NewChangelogEntry, EditChangelogEntry } from './types';
import { useRouter } from 'next/navigation';

export function ChangelogTab() {
  const router = useRouter();
  const [changelog, setChangelog] = useState<ChangelogEntry[]>([]);
  const [loading, setLoading] = useState(true);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingEntry, setEditingEntry] = useState<ChangelogEntry | null>(null);
  const [formData, setFormData] = useState<NewChangelogEntry>({
    version: '',
    title: '',
    description: '',
    changes: [''],
    type: 'patch',
    releaseDate: new Date().toISOString().split('T')[0]
  });

  useEffect(() => {
    fetchChangelog();
  }, []);

  const fetchChangelog = async () => {
    try {
      setLoading(true);
      const response = await fetch('/api/admin/changelog/list');
      if (response.ok) {
        const data = await response.json();
        setChangelog(data.changelog || []);
      }
    } catch (error) {
      console.error('Error fetching changelog:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    try {
      const url = editingEntry ? '/api/admin/changelog/update' : '/api/admin/changelog/create';
      const method = editingEntry ? 'PUT' : 'POST';
      const body = editingEntry 
        ? { ...formData, slug: editingEntry.slug }
        : formData;

      const response = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
      });

      if (response.ok) {
        await fetchChangelog();
        resetForm();
        setShowCreateForm(false);
        setEditingEntry(null);
      } else {
        const error = await response.json();
        alert(`Error: ${error.error}`);
      }
    } catch (error) {
      console.error('Error saving changelog entry:', error);
      alert('Failed to save changelog entry');
    }
  };

  const handleDelete = async (slug: string) => {
    if (!confirm('Are you sure you want to delete this changelog entry?')) return;

    try {
      const response = await fetch('/api/admin/changelog/delete', {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ slug })
      });

      if (response.ok) {
        await fetchChangelog();
      } else {
        const error = await response.json();
        alert(`Error: ${error.error}`);
      }
    } catch (error) {
      console.error('Error deleting changelog entry:', error);
      alert('Failed to delete changelog entry');
    }
  };

  const handleEdit = (entry: ChangelogEntry) => {
    setEditingEntry(entry);
    setFormData({
      version: entry.version,
      title: entry.title,
      description: entry.description,
      changes: entry.changes,
      type: entry.type,
      releaseDate: entry.releaseDate
    });
    setShowCreateForm(true);
  };

  const resetForm = () => {
    setFormData({
      version: '',
      title: '',
      description: '',
      changes: [''],
      type: 'patch',
      releaseDate: new Date().toISOString().split('T')[0]
    });
  };

  const addChange = () => {
    setFormData(prev => ({
      ...prev,
      changes: [...prev.changes, '']
    }));
  };

  const removeChange = (index: number) => {
    setFormData(prev => ({
      ...prev,
      changes: prev.changes.filter((_, i) => i !== index)
    }));
  };

  const updateChange = (index: number, value: string) => {
    setFormData(prev => ({
      ...prev,
      changes: prev.changes.map((change, i) => i === index ? value : change)
    }));
  };

  const getTypeColor = (type: string) => {
    switch (type) {
      case 'major': return 'bg-red-100 text-red-800 dark:bg-red-900/20 dark:text-red-400';
      case 'minor': return 'bg-blue-100 text-blue-800 dark:bg-blue-900/20 dark:text-blue-400';
      case 'patch': return 'bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-400';
      case 'hotfix': return 'bg-orange-100 text-orange-800 dark:bg-orange-900/20 dark:text-orange-400';
      default: return 'bg-gray-100 text-gray-800 dark:bg-gray-900/20 dark:text-gray-400';
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center p-8">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold">Changelog Management</h2>
        <div className="flex gap-3">
          <button
            onClick={() => router.push('/admin/changelog/create')}
            className="flex items-center gap-2 px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:bg-primary/90 transition-colors"
          >
            <Plus className="w-4 h-4" />
            Create New Entry
          </button>
          <button
            onClick={() => {
              setShowCreateForm(true);
              setEditingEntry(null);
              resetForm();
            }}
            className="flex items-center gap-2 px-4 py-2 border border-border text-foreground rounded-lg hover:bg-muted transition-colors"
          >
            <Edit className="w-4 h-4" />
            Quick Edit
          </button>
        </div>
      </div>

      {showCreateForm && (
        <div className="bg-card border rounded-lg p-6">
          <h3 className="text-lg font-semibold mb-4">
            {editingEntry ? 'Edit Changelog Entry' : 'Quick Create Changelog Entry'}
          </h3>
          
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium mb-2">Version *</label>
                <input
                  type="text"
                  value={formData.version}
                  onChange={(e) => setFormData(prev => ({ ...prev, version: e.target.value }))}
                  placeholder="e.g., 1.2.3"
                  className="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                  required
                />
              </div>
              
              <div>
                <label className="block text-sm font-medium mb-2">Type *</label>
                <select
                  value={formData.type}
                  onChange={(e) => setFormData(prev => ({ ...prev, type: e.target.value as 'major' | 'minor' | 'patch' | 'hotfix' }))}
                  className="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                  required
                >
                  <option value="major">Major</option>
                  <option value="minor">Minor</option>
                  <option value="patch">Patch</option>
                  <option value="hotfix">Hotfix</option>
                </select>
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium mb-2">Title *</label>
              <input
                type="text"
                value={formData.title}
                onChange={(e) => setFormData(prev => ({ ...prev, title: e.target.value }))}
                placeholder="Release title"
                className="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium mb-2">Description *</label>
              <textarea
                value={formData.description}
                onChange={(e) => setFormData(prev => ({ ...prev, description: e.target.value }))}
                placeholder="Brief description of the release"
                rows={3}
                className="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium mb-2">Release Date *</label>
              <input
                type="date"
                value={formData.releaseDate}
                onChange={(e) => setFormData(prev => ({ ...prev, releaseDate: e.target.value }))}
                className="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium mb-2">Changes *</label>
              <div className="space-y-2">
                {formData.changes.map((change, index) => (
                  <div key={index} className="flex gap-2">
                    <input
                      type="text"
                      value={change}
                      onChange={(e) => updateChange(index, e.target.value)}
                      placeholder={`Change ${index + 1}`}
                      className="flex-1 px-3 py-2 border rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                      required
                    />
                    {formData.changes.length > 1 && (
                      <button
                        type="button"
                        onClick={() => removeChange(index)}
                        className="px-3 py-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    )}
                  </div>
                ))}
                <button
                  type="button"
                  onClick={addChange}
                  className="text-sm text-primary hover:text-primary/80 transition-colors"
                >
                  + Add another change
                </button>
              </div>
            </div>

            <div className="flex gap-3 pt-4">
              <button
                type="submit"
                className="px-6 py-2 bg-primary text-primary-foreground rounded-lg hover:bg-primary/90 transition-colors"
              >
                {editingEntry ? 'Update Entry' : 'Create Entry'}
              </button>
              <button
                type="button"
                onClick={() => {
                  setShowCreateForm(false);
                  setEditingEntry(null);
                  resetForm();
                }}
                className="px-6 py-2 border rounded-lg hover:bg-muted transition-colors"
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      )}

      <div className="space-y-4">
        {changelog.map((entry) => (
          <div key={entry.slug} className="bg-card border rounded-lg p-6">
            <div className="flex justify-between items-start mb-4">
              <div className="flex items-center gap-3">
                <span className={`px-3 py-1 rounded-full text-xs font-medium ${getTypeColor(entry.type)}`}>
                  {entry.type.toUpperCase()}
                </span>
                <h3 className="text-xl font-semibold">{entry.version}</h3>
                <h4 className="text-lg font-medium text-muted-foreground">{entry.title}</h4>
              </div>
              
              <div className="flex gap-2">
                <button
                  onClick={() => handleEdit(entry)}
                  className="p-2 text-muted-foreground hover:text-foreground hover:bg-muted rounded-lg transition-colors"
                >
                  <Edit className="w-4 h-4" />
                </button>
                <button
                  onClick={() => handleDelete(entry.slug)}
                  className="p-2 text-red-600 hover:text-red-700 hover:bg-red-50 rounded-lg transition-colors"
                >
                  <Trash2 className="w-4 h-4" />
                </button>
              </div>
            </div>

            <p className="text-muted-foreground mb-4">{entry.description}</p>

            <div className="space-y-2 mb-4">
              {entry.changes.map((change, index) => (
                <div key={index} className="flex items-start gap-2">
                  <span className="text-primary mt-1">•</span>
                  <span>{change}</span>
                </div>
              ))}
            </div>

            <div className="flex items-center gap-4 text-sm text-muted-foreground">
              <div className="flex items-center gap-1">
                <Calendar className="w-4 h-4" />
                {new Date(entry.releaseDate).toLocaleDateString()}
              </div>
              <div className="flex items-center gap-1">
                <User className="w-4 h-4" />
                {entry.author}
              </div>
              <div className="flex items-center gap-1">
                <GitBranch className="w-4 h-4" />
                {entry.slug}
              </div>
            </div>
          </div>
        ))}

        {changelog.length === 0 && !showCreateForm && (
          <div className="text-center py-12 text-muted-foreground">
            <GitBranch className="w-12 h-12 mx-auto mb-4 opacity-50" />
            <p className="text-lg">No changelog entries yet</p>
            <p className="text-sm mb-4">Create your first changelog entry to get started</p>
            <button
              onClick={() => router.push('/admin/changelog/create')}
              className="inline-flex items-center gap-2 px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:bg-primary/90 transition-colors"
            >
              <Plus className="w-4 h-4" />
              Create First Entry
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

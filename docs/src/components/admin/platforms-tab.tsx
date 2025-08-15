"use client";

import { Server, Plus, Trash2, RefreshCcw, ExternalLink, Image as ImageIcon, Settings, X, Save, Edit } from 'lucide-react';
import { Platform, NewPlatform, EditPlatform } from './types';
import { useState } from 'react';

interface PlatformsTabProps {
  platforms: Platform[];
  loadingPlatforms: boolean;
  newPlatform: NewPlatform;
  setNewPlatform: (platform: NewPlatform) => void;
  addingPlatform: boolean;
  onAddPlatform: () => void;
  onRemovePlatform: (platformId: string) => void;
  onRefresh: () => void;
  editingPlatform: EditPlatform | null;
  setEditingPlatform: (platform: EditPlatform | null) => void;
  onEditPlatform: (platform: Platform) => void;
  onUpdatePlatform: () => void;
  onCancelEdit: () => void;
}

const VERSION_OPTIONS = ['1.7-1.12', '1.12-1.16', '1.18-1.19', '1.20+'];

export function PlatformsTab({
  platforms,
  loadingPlatforms,
  newPlatform,
  setNewPlatform,
  addingPlatform,
  onAddPlatform,
  onRemovePlatform,
  onRefresh,
  editingPlatform,
  setEditingPlatform,
  onEditPlatform,
  onUpdatePlatform,
  onCancelEdit
}: PlatformsTabProps) {
  const [newAddonName, setNewAddonName] = useState('');

  const handleVersionChange = (version: string, status: 'supported' | 'not-supported' | 'partial' | 'not-possible') => {
    if (editingPlatform) {
      setEditingPlatform({
        ...editingPlatform,
        versions: { ...editingPlatform.versions, [version]: status }
      });
    } else {
      setNewPlatform({
        ...newPlatform,
        versions: { ...newPlatform.versions, [version]: status }
      });
    }
  };

  const handleAddonChange = (addon: string, status: 'supported' | 'not-supported' | 'partial' | 'not-possible') => {
    if (editingPlatform) {
      setEditingPlatform({
        ...editingPlatform,
        addons: { ...editingPlatform.addons, [addon]: status }
      });
    } else {
      setNewPlatform({
        ...newPlatform,
        addons: { ...newPlatform.addons, [addon]: status }
      });
    }
  };

  const handleAddAddon = () => {
    if (newAddonName.trim()) {
      if (editingPlatform) {
        if (!editingPlatform.addons[newAddonName.trim()]) {
          setEditingPlatform({
            ...editingPlatform,
            addons: { ...editingPlatform.addons, [newAddonName.trim()]: 'not-supported' }
          });
        }
      } else {
        if (!newPlatform.addons[newAddonName.trim()]) {
          setNewPlatform({
            ...newPlatform,
            addons: { ...newPlatform.addons, [newAddonName.trim()]: 'not-supported' }
          });
        }
      }
      setNewAddonName('');
    }
  };

  const handleRemoveAddon = (addonName: string) => {
    if (editingPlatform) {
      const newAddons = { ...editingPlatform.addons };
      delete newAddons[addonName];
      setEditingPlatform({
        ...editingPlatform,
        addons: newAddons
      });
    } else {
      const newAddons = { ...newPlatform.addons };
      delete newAddons[addonName];
      setNewPlatform({
        ...newPlatform,
        addons: newAddons
      });
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'supported': return 'text-emerald-500';
      case 'partial': return 'text-amber-500';
      case 'not-supported': return 'text-red-500';
      case 'not-possible': return 'text-gray-400';
      default: return 'text-gray-500';
    }
  };

  const getStatusLabel = (status: string) => {
    switch (status) {
      case 'supported': return 'Supported';
      case 'partial': return 'Partial';
      case 'not-supported': return 'Not Supported';
      case 'not-possible': return 'Not Possible';
      default: return status;
    }
  };

  const currentPlatform = editingPlatform || newPlatform;
  const isEditing = !!editingPlatform;

  return (
    <div className="mb-4 sm:mb-6 p-4 sm:p-6 bg-gradient-to-br from-blue-500/5 via-transparent to-blue-500/5 border border-blue-500/20 rounded-xl">
      <div className="flex flex-col sm:flex-row sm:items-center gap-3 mb-4">
        <div className="flex items-center gap-2 sm:gap-3">
          <Server className="w-5 h-5 text-blue-500" />
          <h3 className="text-base sm:text-lg font-semibold text-foreground">Platform Management</h3>
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
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2 sm:gap-0 mb-3">
          <h4 className="font-medium text-foreground text-sm sm:text-base">
            {isEditing ? `Edit Platform: ${editingPlatform?.name}` : 'Add New Platform'}
          </h4>
          {isEditing && (
            <button
              onClick={onCancelEdit}
              className="inline-flex items-center gap-2 px-2.5 sm:px-3 py-1.5 sm:py-2 bg-gray-600 text-white rounded-lg hover:bg-gray-700 transition-all duration-300 text-sm"
            >
              <X className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
              Cancel
            </button>
          )}
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 sm:gap-4 mb-3">
          <div>
            <label className="block text-xs sm:text-sm font-medium text-foreground mb-1">Platform Name *</label>
            <input
              type="text"
              value={currentPlatform.name}
              onChange={(e) => {
                if (isEditing) {
                  setEditingPlatform({ ...editingPlatform!, name: e.target.value });
                } else {
                  setNewPlatform({...newPlatform, name: e.target.value});
                }
              }}
              placeholder="e.g. Spigot"
              className="w-full px-3 py-2 bg-background border border-border/50 rounded-lg text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-blue-500/50 text-sm sm:text-base"
            />
          </div>
          <div>
            <label className="block text-xs sm:text-sm font-medium text-foreground mb-1">Icon Upload *</label>
            <div className="space-y-2">
              <input
                type="file"
                accept="image/png,image/jpeg,image/svg+xml,image/webp"
                onChange={(e) => {
                  const file = e.target.files?.[0];
                  if (file) {
                    if (file.size > 5 * 1024 * 1024) {
                      alert('File too large. Maximum size is 5MB.');
                      return;
                    }
                    if (isEditing) {
                      setEditingPlatform({...editingPlatform!, iconFile: file, icon: ''});
                    } else {
                      setNewPlatform({...newPlatform, iconFile: file, icon: ''});
                    }
                  }
                }}
                className="w-full px-3 py-2 bg-background border border-border/50 rounded-lg text-foreground file:mr-4 file:py-1 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-blue-600 file:text-white hover:file:bg-blue-700"
              />
              {(currentPlatform.iconFile || currentPlatform.icon) && (
                <div className="text-xs text-blue-600">
                  {currentPlatform.iconFile ? (
                    `✓ ${currentPlatform.iconFile.name} (${(currentPlatform.iconFile.size / 1024 / 1024).toFixed(2)}MB)`
                  ) : (
                    `✓ Current icon: ${currentPlatform.icon.split('/').pop()}`
                  )}
                </div>
              )}
            </div>
          </div>
        </div>

        <div className="mb-4">
          <label className="block text-xs sm:text-sm font-medium text-foreground mb-2">Version Support</label>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 sm:gap-4">
            {VERSION_OPTIONS.map(version => (
              <div key={version} className="space-y-2">
                <label className="block text-xs sm:text-sm font-medium text-foreground">{version}</label>
                <select
                  value={currentPlatform.versions[version] || 'not-supported'}
                  onChange={(e) => handleVersionChange(version, e.target.value as 'supported' | 'not-supported' | 'partial' | 'not-possible')}
                  className="w-full px-3 py-2 bg-background border border-border/50 rounded-lg text-foreground focus:outline-none focus:ring-2 focus:ring-blue-500/50 text-sm sm:text-base"
                >
                  <option value="supported">Supported</option>
                  <option value="not-supported">Not Supported</option>
                  <option value="partial">Partial</option>
                  <option value="not-possible">Not Possible</option>
                </select>
              </div>
            ))}
          </div>
        </div>

        <div className="mb-4">
          <label className="block text-xs sm:text-sm font-medium text-foreground mb-2">Addon Support</label>

          <div className="flex gap-2 mb-3">
            <input
              type="text"
              value={newAddonName}
              onChange={(e) => setNewAddonName(e.target.value)}
              placeholder="New addon name..."
              className="flex-1 px-3 py-2 bg-background border border-border/50 rounded-lg text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-blue-500/50 text-sm sm:text-base"
            />
            <button
              onClick={handleAddAddon}
              disabled={!newAddonName.trim() || !!currentPlatform.addons[newAddonName.trim()]}
              className="px-3 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-300"
            >
              <Plus className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
            </button>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 sm:gap-4">
            {Object.entries(currentPlatform.addons).map(([addon, status]) => (
              <div key={addon} className="space-y-2">
                <div className="flex items-center gap-2">
                  <label className="block text-xs sm:text-sm font-medium text-foreground flex-1">{addon}</label>
                  <button
                    onClick={() => handleRemoveAddon(addon)}
                    className="inline-flex items-center gap-1 px-2 py-1 bg-red-600/10 text-red-600 rounded-lg hover:bg-red-600/20 text-xs transition-all duration-300"
                  >
                    <X className="w-3 h-3" />
                  </button>
                </div>
                <select
                  value={status}
                  onChange={(e) => handleAddonChange(addon, e.target.value as 'supported' | 'not-supported' | 'partial' | 'not-possible')}
                  className="w-full px-3 py-2 bg-background border border-border/50 rounded-lg text-foreground focus:outline-none focus:ring-2 focus:ring-blue-500/50 text-sm sm:text-base"
                >
                  <option value="supported">Supported</option>
                  <option value="not-supported">Not Supported</option>
                  <option value="partial">Partial</option>
                  <option value="not-possible">Not Possible</option>
                </select>
              </div>
            ))}
          </div>
        </div>

        <button
          onClick={isEditing ? onUpdatePlatform : onAddPlatform}
          disabled={addingPlatform || !currentPlatform.name.trim() || (!currentPlatform.iconFile && !currentPlatform.icon)}
          className="inline-flex items-center justify-center gap-2 px-3 sm:px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-300 text-sm"
        >
          {addingPlatform ? (
            <>
              <RefreshCcw className="w-3.5 h-3.5 sm:w-4 sm:h-4 animate-spin" />
              <span className="hidden sm:inline">{isEditing ? 'Updating...' : 'Adding...'}</span>
              <span className="sm:hidden">{isEditing ? 'Update...' : 'Add...'}</span>
            </>
          ) : (
            <>
              {isEditing ? <Save className="w-3.5 h-3.5 sm:w-4 sm:h-4" /> : <Plus className="w-3.5 h-3.5 sm:w-4 sm:h-4" />}
              <span className="hidden sm:inline">{isEditing ? 'Update Platform' : 'Add Platform'}</span>
              <span className="sm:hidden">{isEditing ? 'Update' : 'Add'}</span>
            </>
          )}
        </button>
      </div>

      {loadingPlatforms ? (
        <div className="text-center py-6 sm:py-8 text-muted-foreground text-sm sm:text-base">
          <RefreshCcw className="w-6 h-6 sm:w-8 sm:h-8 animate-spin mx-auto mb-2" />
          Loading platforms...
        </div>
      ) : platforms.length === 0 ? (
        <div className="text-center py-6 sm:py-8 text-muted-foreground text-sm sm:text-base">
          No platforms found. Add your first platform above.
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3 sm:gap-4">
          {platforms.map((platform) => (
            <div
              key={platform.id}
              className="p-3 sm:p-4 bg-background/30 rounded-lg border border-border/30 hover:border-blue-500/30 transition-all duration-300"
            >
              <div className="flex flex-col sm:flex-row sm:items-start sm:justify-between gap-3 sm:gap-0 mb-3">
                <div className="flex items-center gap-2 sm:gap-3">
                  {platform.icon ? (
                    <img
                      src={platform.icon}
                      alt={platform.name}
                      className="w-10 h-10 sm:w-12 sm:h-12 rounded-lg border border-border/50 object-cover"
                    />
                  ) : (
                    <div className="w-10 h-10 sm:w-12 sm:h-12 rounded-lg bg-muted flex items-center justify-center">
                      <ImageIcon className="w-5 h-5 sm:w-6 sm:h-6 text-muted-foreground" />
                    </div>
                  )}
                  <div>
                    <h4 className="font-medium text-foreground text-sm sm:text-base">{platform.name}</h4>
                  </div>
                </div>
                <div className="flex gap-2">
                  <button
                    onClick={() => onEditPlatform(platform)}
                    className="inline-flex items-center gap-1 px-2 py-1 bg-blue-600/10 text-blue-600 rounded-lg hover:bg-blue-600/20 text-xs transition-all duration-300"
                  >
                    <Edit className="w-3 h-3" />
                    Edit
                  </button>
                  <button
                    onClick={() => onRemovePlatform(platform.id)}
                    className="inline-flex items-center gap-1 px-2 py-1 bg-red-600/10 text-red-600 rounded-lg hover:bg-red-600/20 text-xs transition-all duration-300"
                  >
                    <Trash2 className="w-3 h-3" />
                    Remove
                  </button>
                </div>
              </div>
              
              <div className="space-y-2 mb-3">
                <div className="text-xs text-muted-foreground">
                  <strong>Versions:</strong>
                  <div className="mt-1 space-y-1">
                    {VERSION_OPTIONS.map(version => (
                      <div key={version} className="flex justify-between items-center">
                        <span>{version}:</span>
                        <span className={`${getStatusColor(platform.versions[version] || 'not-supported')}`}>
                          {getStatusLabel(platform.versions[version] || 'not-supported')}
                        </span>
                      </div>
                    ))}
                  </div>
                </div>
                <div className="text-xs text-muted-foreground">
                  <strong>Addons:</strong>
                  <div className="mt-1 space-y-1">
                    {Object.entries(platform.addons).map(([addon, status]) => (
                      <div key={addon} className="flex justify-between items-center">
                        <span>{addon}:</span>
                        <span className={`${getStatusColor(status)}`}>
                          {getStatusLabel(status)}
                        </span>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
              
              <div className="text-xs text-muted-foreground">
                Added on {new Date(platform.addedAt).toLocaleDateString('de-DE')}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

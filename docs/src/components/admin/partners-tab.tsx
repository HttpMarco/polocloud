"use client";

import { Handshake, Plus, Trash2, RefreshCcw, ExternalLink, Image as ImageIcon, Save, Edit, X } from 'lucide-react';
import { Partner, NewPartner, EditPartner } from './types';

interface PartnersTabProps {
  partners: Partner[];
  loadingPartners: boolean;
  newPartner: NewPartner;
  setNewPartner: (partner: NewPartner) => void;
  addingPartner: boolean;
  onAddPartner: () => void;
  onRemovePartner: (partnerId: string) => void;
  onRefresh: () => void;
  editingPartner: EditPartner | null;
  setEditingPartner: (partner: EditPartner | null) => void;
  onEditPartner: (partner: Partner) => void;
  onUpdatePartner: () => void;
  onCancelEditPartner: () => void;
}

export function PartnersTab({
  partners,
  loadingPartners,
  newPartner,
  setNewPartner,
  addingPartner,
  onAddPartner,
  onRemovePartner,
  onRefresh,
  editingPartner,
  setEditingPartner,
  onEditPartner,
  onUpdatePartner,
  onCancelEditPartner
}: PartnersTabProps) {
  const currentPartner = editingPartner || newPartner;
  const isEditing = !!editingPartner;

  return (
    <div className="mb-6 p-6 bg-gradient-to-br from-green-500/5 via-transparent to-green-500/5 border border-green-500/20 rounded-xl">
      <div className="flex items-center gap-3 mb-4">
        <Handshake className="w-5 h-5 text-green-500" />
        <h3 className="text-lg font-semibold text-foreground">Partner Management</h3>
        <button
          onClick={onRefresh}
          className="ml-auto inline-flex items-center gap-2 px-3 py-1 bg-green-600/10 text-green-600 rounded-lg hover:bg-green-600/20 text-sm transition-all duration-300"
        >
          <RefreshCcw className="w-4 h-4" />
          Refresh
        </button>
      </div>

      <div className="mb-6 p-4 bg-background/30 rounded-lg border border-border/30">
        <div className="flex items-center justify-between mb-3">
          <h4 className="font-medium text-foreground">
            {isEditing ? `Edit Partner: ${editingPartner?.name}` : 'Add New Partner'}
          </h4>
          {isEditing && (
            <button
              onClick={onCancelEditPartner}
              className="inline-flex items-center gap-2 px-3 py-2 bg-gray-600 text-white rounded-lg hover:bg-gray-700 transition-all duration-300"
            >
              <X className="w-4 h-4" />
              Cancel
            </button>
          )}
        </div>
        <div className="grid md:grid-cols-2 gap-4 mb-3">
          <div>
            <label className="block text-sm font-medium text-foreground mb-1">Partner Name *</label>
            <input
              type="text"
              value={currentPartner.name}
              onChange={(e) => {
                if (isEditing) {
                  setEditingPartner({ ...editingPartner!, name: e.target.value });
                } else {
                  setNewPartner({...newPartner, name: e.target.value});
                }
              }}
              placeholder="e.g. HGLabor"
              className="w-full px-3 py-2 bg-background border border-border/50 rounded-lg text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-green-500/50"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-foreground mb-1">Logo Upload *</label>
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
                      setEditingPartner({...editingPartner!, logoFile: file, logo: ''});
                    } else {
                      setNewPartner({...newPartner, logoFile: file, logo: ''});
                    }
                  }
                }}
                className="w-full px-3 py-2 bg-background border border-border/50 rounded-lg text-foreground file:mr-4 file:py-1 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-green-600 file:text-white hover:file:bg-green-700"
              />
              {(currentPartner.logoFile || currentPartner.logo) && (
                <div className="text-xs text-green-600">
                  {currentPartner.logoFile ? (
                    `✓ ${currentPartner.logoFile.name} (${(currentPartner.logoFile.size / 1024 / 1024).toFixed(2)}MB)`
                  ) : (
                    `✓ Current logo: ${currentPartner.logo.split('/').pop()}`
                  )}
                </div>
              )}
            </div>
          </div>
          <div>
            <label className="block text-sm font-medium text-foreground mb-1">Website URL</label>
            <input
              type="url"
              value={currentPartner.website}
              onChange={(e) => {
                if (isEditing) {
                  setEditingPartner({ ...editingPartner!, website: e.target.value });
                } else {
                  setNewPartner({...newPartner, website: e.target.value});
                }
              }}
              placeholder="https://partner-website.com"
              className="w-full px-3 py-2 bg-background border border-border/50 rounded-lg text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-green-500/50"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-foreground mb-1">Description</label>
            <input
              type="text"
              value={currentPartner.description}
              onChange={(e) => {
                if (isEditing) {
                  setEditingPartner({ ...editingPartner!, description: e.target.value });
                } else {
                  setNewPartner({...newPartner, description: e.target.value});
                }
              }}
              placeholder="Brief description of the partner"
              className="w-full px-3 py-2 bg-background border border-border/50 rounded-lg text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-green-500/50"
            />
          </div>
        </div>
        <button
          onClick={isEditing ? onUpdatePartner : onAddPartner}
          disabled={addingPartner || !currentPartner.name.trim() || (!currentPartner.logo.trim() && !currentPartner.logoFile)}
          className="inline-flex items-center gap-2 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-300"
        >
          {addingPartner ? (
            <>
              <RefreshCcw className="w-4 h-4 animate-spin" />
              {isEditing ? 'Updating...' : 'Adding...'}
            </>
          ) : (
            <>
              {isEditing ? <Save className="w-4 h-4" /> : <Plus className="w-4 h-4" />}
              {isEditing ? 'Update Partner' : 'Add Partner'}
            </>
          )}
        </button>
      </div>

      {loadingPartners ? (
        <div className="text-center py-8 text-muted-foreground">
          <RefreshCcw className="w-8 h-8 animate-spin mx-auto mb-2" />
          Loading partners...
        </div>
      ) : partners.length === 0 ? (
        <div className="text-center py-8 text-muted-foreground">
          No partners found. Add your first partner above.
        </div>
      ) : (
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
          {partners.map((partner) => (
            <div
              key={partner.id}
              className="p-4 bg-background/30 rounded-lg border border-border/30 hover:border-green-500/30 transition-all duration-300"
            >
              <div className="flex items-start justify-between mb-3">
                <div className="flex items-center gap-3">
                  {partner.logo ? (
                    <img
                      src={partner.logo}
                      alt={partner.name}
                      className="w-12 h-12 rounded-lg border border-border/50 object-cover"
                    />
                  ) : (
                    <div className="w-12 h-12 rounded-lg bg-muted flex items-center justify-center">
                      <ImageIcon className="w-6 h-6 text-muted-foreground" />
                    </div>
                  )}
                  <div>
                    <h4 className="font-medium text-foreground">{partner.name}</h4>
                    {partner.website && (
                      <a
                        href={partner.website}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="inline-flex items-center gap-1 text-xs text-blue-600 hover:text-blue-700 transition-colors"
                      >
                        <ExternalLink className="w-3 h-3" />
                        Visit Website
                      </a>
                    )}
                  </div>
                </div>
                <div className="flex gap-2">
                  <button
                    onClick={() => onEditPartner(partner)}
                    className="inline-flex items-center gap-1 px-2 py-1 bg-blue-600/10 text-blue-600 rounded-lg hover:bg-blue-600/20 text-xs transition-all duration-300"
                  >
                    <Edit className="w-3 h-3" />
                    Edit
                  </button>
                  <button
                    onClick={() => onRemovePartner(partner.id)}
                    className="inline-flex items-center gap-1 px-2 py-1 bg-red-600/10 text-red-600 rounded-lg hover:bg-red-600/20 text-xs transition-all duration-300"
                  >
                    <Trash2 className="w-3 h-3" />
                    Remove
                  </button>
                </div>
              </div>
              
              {partner.description && (
                <p className="text-sm text-muted-foreground mb-3">{partner.description}</p>
              )}
              
                             <div className="text-xs text-muted-foreground">
                 Added on {new Date(partner.addedAt).toLocaleDateString('de-DE')}
               </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

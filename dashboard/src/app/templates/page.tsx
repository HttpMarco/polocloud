'use client'

import { useState, useEffect, useMemo, useCallback } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';

import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Label } from '@/components/ui/label';
import {
    FileText,
    Search,
    Plus,
    Edit,
    Trash2,
    Loader2,
    HardDrive,
    Activity,
    Save,
    X,
    Check
} from 'lucide-react';
import { Template } from '@/types/templates';
import { motion, AnimatePresence } from 'framer-motion';
import GlobalNavbar from '@/components/global-navbar';
import { API_ENDPOINTS } from '@/lib/api';
import { usePermissions } from '@/hooks/usePermissions';

const formatFileSize = (size: unknown): string => {
    if (size === undefined || size === null || size === '') {
        return 'Empty';
    }

    if (typeof size === 'string') {
        if (size.toLowerCase() === 'empty' || size === '') {
            return 'Empty';
        }

        if (size.includes('MB') || size.includes('KB') || size.includes('GB') || size.includes('B')) {
            return size;
        }

        const parsed = parseFloat(size);
        if (!isNaN(parsed)) {
            size = formatFileSize(parsed);
        } else {
            return 'Empty';
        }
    }

    if (typeof size === 'number') {
        if (size === 0) {
            return 'Empty';
        }

        if (size < 1024) {
            return `${size} B`;
        } else if (size < 1024 * 1024) {
            return `${Math.round(size / 1024)} KB`;
        } else if (size < 1024 * 1024 * 1024) {
            return `${Math.round(size / (1024 * 1024))} MB`;
        } else {
            return `${Math.round(size / (1024 * 1024 * 1024))} GB`;
        }
    }

    return 'Empty';
};

export default function TemplatesPage() {
    const [templates, setTemplates] = useState<Template[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [deletingTemplates, setDeletingTemplates] = useState<string[]>([]);

    const { hasPermission } = usePermissions();
    const canCreateTemplate = hasPermission('polocloud.templates.create');
    const canEditTemplate = hasPermission('polocloud.templates.edit');
    const canDeleteTemplate = hasPermission('polocloud.templates.delete');

    const [editModalOpen, setEditModalOpen] = useState(false);
    const [editingTemplate, setEditingTemplate] = useState<Template | null>(null);
    const [editName, setEditName] = useState('');
    const [isSaving, setIsSaving] = useState(false);
    const [editError, setEditError] = useState<string | null>(null);
    const [editValidationError, setEditValidationError] = useState<string | null>(null);

    const [createModalOpen, setCreateModalOpen] = useState(false);
    const [newTemplateName, setNewTemplateName] = useState('');
    const [isCreating, setIsCreating] = useState(false);
    const [createError, setCreateError] = useState<string | null>(null);
    const [createValidationError, setCreateValidationError] = useState<string | null>(null);

    const [successModalOpen, setSuccessModalOpen] = useState(false);
    const [successMessage, setSuccessMessage] = useState('');
    const [errorModalOpen, setErrorModalOpen] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [deleteConfirmModal, setDeleteConfirmModal] = useState<{ open: boolean; templateName: string | null }>({ open: false, templateName: null });

    const withLoading = async (fn: () => Promise<void>) => {
        try {
            setIsLoading(true);
            setError(null);
            await fn();
        } catch {
            setError('Operation failed');
        } finally {
            setIsLoading(false);
        }
    };

    const withSaving = async (fn: () => Promise<void>) => {
        try {
            setIsSaving(true);
            setEditError(null);
            await fn();
        } catch {
            setEditError('Operation failed');
        } finally {
            setIsSaving(false);
        }
    };

    const withCreating = async (fn: () => Promise<void>) => {
        try {
            setIsCreating(true);
            setCreateError(null);
            await fn();
        } catch {
            setCreateError('Operation failed');
        } finally {
            setIsCreating(false);
        }
    };

    const showSuccess = (message: string) => {
        setSuccessMessage(message);
        setSuccessModalOpen(true);
    };

    const showError = (message: string) => {
        setErrorMessage(message);
        setErrorModalOpen(true);
    };

    const validateTemplateName = (name: string): { isValid: boolean; error: string | null } => {
        const trimmedName = name.trim();

        if (trimmedName.length < 3) {
            return { isValid: false, error: 'Template name must be at least 3 characters long' };
        }

        if (trimmedName.length > 50) {
            return { isValid: false, error: 'Template name must be less than 50 characters' };
        }

        if (name.includes(' ')) {
            return { isValid: false, error: 'Template name cannot contain spaces' };
        }

        if (!/^[a-zA-Z_]+$/.test(trimmedName)) {
            return { isValid: false, error: 'Template name can only contain letters and underscores' };
        }

        return { isValid: true, error: null };
    };

    const handleCreateNameChange = (value: string) => {
        setNewTemplateName(value);
        if (value.trim()) {
            const validation = validateTemplateName(value);
            setCreateValidationError(validation.error);
        } else {
            setCreateValidationError(null);
        }
    };

    const handleEditNameChange = (value: string) => {
        setEditName(value);
        if (value.trim()) {
            const validation = validateTemplateName(value);
            setEditValidationError(validation.error);
        } else {
            setEditValidationError(null);
        }
    };

    const loadTemplates = useCallback(async () => {
        await withLoading(async () => {
            const response = await fetch(API_ENDPOINTS.TEMPLATES.LIST);
            if (response.ok) {
                const data = await response.json();

                if (Array.isArray(data)) {
                    const validTemplates = data.filter(template =>
                        template &&
                        typeof template === 'object' &&
                        template.name &&
                        typeof template.name === 'string'
                    );
                    setTemplates(validTemplates);
                } else {
                    setError('Invalid response format from server');
                }
            } else {
                const errorData = await response.json();
                setError(errorData.error || 'Failed to load templates');
            }
        });
    }, []);

    useEffect(() => {
        loadTemplates();
    }, [loadTemplates]);

    const handleDeleteTemplate = async (templateName: string) => {
        setDeleteConfirmModal({ open: true, templateName });
    };

    const confirmDeleteTemplate = async () => {
        const templateName = deleteConfirmModal.templateName;
        if (!templateName || deletingTemplates.includes(templateName)) return;

        setDeletingTemplates(prev => [...prev, templateName]);

        try {
            const response = await fetch(API_ENDPOINTS.TEMPLATES.DELETE(templateName), {
                method: 'DELETE'
            });

            if (response.ok || response.status === 202) {
                setTemplates(prev => prev.filter(t => t.name !== templateName));
                showSuccess(`Template "${templateName}" deleted successfully!`);
            } else {
                const errorData = await response.json();
                showError(errorData.error || 'Failed to delete template');
            }
        } catch {
            showError('Failed to delete template');
        } finally {
            setDeletingTemplates(prev => prev.filter(name => name !== templateName));
            setDeleteConfirmModal({ open: false, templateName: null });
        }
    };

    const openEditModal = (template: Template) => {
        setEditingTemplate(template);
        setEditName(template.name);
        setEditError(null);
        setEditValidationError(null);
        setEditModalOpen(true);
    };

    const closeEditModal = () => {
        setEditModalOpen(false);
        setEditingTemplate(null);
        setEditName('');
        setEditError(null);
        setEditValidationError(null);
    };

    const handleSaveEdit = async () => {
        if (!editingTemplate || !editName.trim()) {
            setEditError('Template name is required');
            return;
        }

        const validation = validateTemplateName(editName);
        if (!validation.isValid) {
            setEditError(validation.error);
            return;
        }

        if (editName === editingTemplate.name) {
            setEditError('No changes made');
            return;
        }

        await withSaving(async () => {
            const response = await fetch(API_ENDPOINTS.TEMPLATES.EDIT(editingTemplate.name), {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    name: editName.trim()
                })
            });

            if (response.ok || response.status === 202) {
                setTemplates(prev => prev.map(t =>
                    t.name === editingTemplate.name
                        ? { ...t, name: editName.trim() }
                        : t
                ));

                showSuccess('Template updated successfully!');
                closeEditModal();
            } else {
                const errorData = await response.json();
                showError(errorData.error || 'Failed to update template');
            }
        });
    };

    const openCreateModal = () => {
        setCreateModalOpen(true);
        setNewTemplateName('');
        setCreateError(null);
        setCreateValidationError(null);
    };

    const closeCreateModal = () => {
        setCreateModalOpen(false);
        setNewTemplateName('');
        setCreateError(null);
        setCreateValidationError(null);
    };

    const handleCreateTemplate = async () => {
        if (!newTemplateName.trim()) return;

        const validation = validateTemplateName(newTemplateName);
        if (!validation.isValid) {
            setCreateError(validation.error);
            return;
        }

        const backendIp = localStorage.getItem('backendIp');
        if (!backendIp) {
            setCreateError('No backend connection found. Please connect to backend first.');
            return;
        }

        await withCreating(async () => {
            const response = await fetch(API_ENDPOINTS.TEMPLATES.CREATE, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ name: newTemplateName.trim() }),
            });

            if (response.ok) {
                const newTemplate = await response.json();

                let validTemplate: Template;

                if (newTemplate && newTemplate.name && typeof newTemplate.name === 'string') {
                    validTemplate = newTemplate;
                } else if (typeof newTemplate === 'string') {
                    validTemplate = { name: newTemplate, size: 0 };
                } else if (newTemplate && typeof newTemplate === 'object') {
                    const name = newTemplate.name || newTemplate.templateName || newTemplate.id || newTemplateName.trim();
                    if (name) {
                        validTemplate = {
                            name: String(name),
                            size: newTemplate.size || newTemplate.fileSize || 0
                        };
                    } else {
                        throw new Error('Could not extract template name from response');
                    }
                } else {
                    validTemplate = { name: newTemplateName.trim(), size: 0 };
                }

                setTemplates(prev => [...prev, validTemplate]);
                showSuccess('Template created successfully!');
                closeCreateModal();
            } else {
                const errorData = await response.json();
                showError(errorData.message || 'Failed to create template');
            }
        });
    };

    const filteredTemplates = useMemo(() => {
        return templates.filter(template =>
            template.name && template.name.toLowerCase().includes(searchTerm.toLowerCase())
        );
    }, [templates, searchTerm]);

    const stats = useMemo(() => {
        const validTemplates = templates.filter(template => template && template.name);
        const totalTemplates = validTemplates.length;
        const totalSize = validTemplates.reduce((sum, t) => {
            const size: string | number = t.size;
            if (typeof size === 'string') {
                if (size.toLowerCase() === 'empty' || size === '') {
                    return sum;
                }

                const match = size.match(/(\d+(?:\.\d+)?)\s*(MB|KB|GB|B)/i);
                if (match) {
                    const value = parseFloat(match[1]);
                    const unit = match[2].toUpperCase();
                    switch (unit) {
                        case 'B': return sum + value;
                        case 'KB': return sum + (value * 1024);
                        case 'MB': return sum + (value * 1024 * 1024);
                        case 'GB': return sum + (value * 1024 * 1024 * 1024);
                        default: return sum;
                    }
                }
                return sum;
            }
            return sum + (size || 0);
        }, 0);

        return {
            total: totalTemplates,
            size: totalSize > 0 ? Math.round(totalSize / 1024 / 1024) : 0
        };
    }, [templates]);

    if (isLoading) {
        return (
            <div className="w-full h-full flex items-center justify-center">
                <div className="w-8 h-8 border-2 border-primary border-t-transparent rounded-full animate-spin"></div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="w-full h-full flex items-center justify-center">
                <div className="text-center">
                    <p className="text-red-500 mb-4">{error}</p>
                    <Button onClick={loadTemplates}>Retry</Button>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20 ultra-smooth-scroll">
            <GlobalNavbar />

            <div className="h-2"></div>

            <div className="px-6 py-6">
                <div className="mb-8">
                    <motion.h1
                        className="text-4xl font-bold text-foreground mb-3"
                        initial={{ opacity: 0, y: -20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.6 }}
                    >
                        Templates
                    </motion.h1>
                    <motion.p
                        className="text-lg text-muted-foreground"
                        initial={{ opacity: 0, y: -10 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.6, delay: 0.1 }}
                    >
                        Manage your server templates and configurations
                    </motion.p>
                </div>
            </div>

            <div className="px-6 pb-8">
                <motion.div
                    className="grid grid-cols-1 md:grid-cols-2 gap-6"
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.6, delay: 0.2 }}
                >
                    <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm">
                        <CardContent className="p-4">
                            <div className="flex items-center space-x-3 mb-2">
                                <div className="w-10 h-10 rounded-full bg-gradient-to-br from-[oklch(75.54% 0.1534 231.639,0.2)] to-[oklch(75.54% 0.1534 231.639,0.1)] flex items-center justify-center border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-lg">
                                    <FileText className="h-5 w-5 text-[oklch(75.54% 0.1534 231.639)]" />
                                </div>
                                <div>
                                    <p className="text-sm font-medium text-muted-foreground">Total Templates</p>
                                    <p className="text-2xl font-bold text-foreground">{stats.total}</p>
                                </div>
                            </div>
                        </CardContent>
                    </Card>

                    <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm">
                        <CardContent className="p-4">
                            <div className="flex items-center space-x-3 mb-2">
                                <div className="w-10 h-10 rounded-full bg-gradient-to-br from-[oklch(75.54% 0.1534 231.639,0.2)] to-[oklch(75.54% 0.1534 231.639,0.1)] flex items-center justify-center border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-lg">
                                    <HardDrive className="h-5 w-5 text-[oklch(75.54% 0.1534 231.639)]" />
                                </div>
                                <div>
                                    <p className="text-sm font-medium text-muted-foreground">Total Size</p>
                                    <p className="text-2xl font-bold text-foreground">
                                        {formatFileSize(stats.size * 1024 * 1024)}
                                    </p>
                                </div>
                            </div>
                        </CardContent>
                    </Card>
                </motion.div>
            </div>

            <div className="px-6 pb-6">
                <motion.div
                    className="flex flex-col sm:flex-row gap-4"
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.6, delay: 0.3 }}
                >
                    <div className="flex-1">
                        <div className="relative">
                            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                            <Input
                                placeholder="Search templates by name..."
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                                className="pl-10 h-9 text-sm border-border/50 focus:border-[oklch(75.54% 0.1534 231.639)] focus:ring-2 focus:ring-[oklch(75.54% 0.1534 231.639,0.2)] transition-all duration-200"
                            />
                        </div>
                    </div>

                    <Button
                        onClick={openCreateModal}
                        disabled={!canCreateTemplate}
                        className={`h-9 px-4 text-sm font-medium transition-all duration-200 ${
                            canCreateTemplate 
                                ? 'hover:opacity-90 shadow-lg shadow-[0_0_20px_rgba(75.54%,15.34%,231.639,0.3)] hover:shadow-[0_0_30px_rgba(75.54%,15.34%,231.639,0.4)]' 
                                : 'opacity-40 cursor-not-allowed bg-muted text-muted-foreground border border-border/30'
                        }`}
                        style={{ 
                            backgroundColor: canCreateTemplate ? 'oklch(75.54% .1534 231.639)' : undefined
                        }}
                    >
                        <Plus className="w-4 h-4 mr-2" />
                        Create Template
                    </Button>
                </motion.div>
            </div>

            <div className="flex-1 px-6 pb-6 overflow-auto modern-scrollbar">
                {filteredTemplates.length > 0 ? (
                    <motion.div
                        className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5 gap-4"
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        transition={{ duration: 0.6, delay: 0.4 }}
                    >
                        <AnimatePresence>
                            {filteredTemplates.filter(template => template && template.name).map((template, index) => (
                                <motion.div
                                    key={template.name}
                                    initial={{ opacity: 0, y: 40, scale: 0.85 }}
                                    animate={{ opacity: 1, y: 0, scale: 1 }}
                                    exit={{ opacity: 0, y: -20, scale: 0.8 }}
                                    transition={{
                                        duration: 0.8,
                                        delay: index * 0.1,
                                        ease: [0.34, 1.56, 0.64, 1]
                                    }}
                                    layout
                                >
                                    <Card className="border-border/40 flex flex-col relative bg-gradient-to-br from-card/80 via-card/60 to-card/80 shadow-lg transition-all duration-300 group">
                                        <div className="absolute inset-0 bg-[radial-gradient(circle_at_30%_20%,rgba(75.54%,15.34%,0.03)_0%,transparent_50%)] opacity-60 rounded-t-lg"/>

                                        <CardHeader className="pb-4 flex-shrink-0 relative z-10">
                                            <div className="flex items-center gap-3">
                                                <div className="flex items-center gap-3">
                                                    <div className="p-2 rounded-xl bg-background/50 border border-border/30 transition-colors duration-200">
                                                        <FileText className="w-8 h-8 text-[oklch(75.54% 0.1534 231.639)]" />
                                                    </div>
                                                    <div className="flex-1 min-w-0">
                                                        <CardTitle className="text-lg font-bold text-foreground truncate transition-colors duration-200">
                                                            {template.name}
                                                        </CardTitle>
                                                        <div className="flex items-center gap-2 mt-1">
                                                            <p className="text-sm text-muted-foreground">
                                                                Template
                                                            </p>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </CardHeader>

                                        <CardContent className="flex-1 flex flex-col space-y-4 pt-0 relative z-10">
                                            <div className="grid grid-cols-2 gap-4">
                                                <div className="bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] rounded-lg p-3 border border-[oklch(75.54%_0.1534_231.639,0.3)]">
                                                    <div className="flex items-center gap-2 mb-2">
                                                        <HardDrive className="w-4 h-4 text-muted-foreground" />
                                                        <span className="text-xs font-medium text-muted-foreground uppercase tracking-wide">Size</span>
                                                    </div>
                                                    <div className="text-sm font-semibold text-foreground">
                                                        {formatFileSize(template.size)}
                                                    </div>
                                                </div>

                                                <div className="bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] rounded-lg p-3 border border-[oklch(75.54%_0.1534_231.639,0.3)]">
                                                    <div className="flex items-center gap-2 mb-2">
                                                        <Activity className="w-4 h-4 text-muted-foreground" />
                                                        <span className="text-xs font-medium text-muted-foreground uppercase tracking-wide">Status</span>
                                                    </div>
                                                    <div className="text-sm font-semibold text-foreground">
                                                        Active
                                                    </div>
                                                </div>
                                            </div>

                                            <div className="flex items-center gap-2 pt-2">
                                                <Button
                                                    variant="outline"
                                                    size="sm"
                                                    disabled={!canEditTemplate}
                                                    className={`flex-1 h-8 text-xs transition-all duration-200 ${
                                                        !canEditTemplate 
                                                            ? 'opacity-40 cursor-not-allowed bg-muted text-muted-foreground border border-border/30' 
                                                            : 'border-border/30 text-muted-foreground hover:text-foreground hover:border-border/50'
                                                    }`}
                                                    onClick={() => canEditTemplate && openEditModal(template)}
                                                >
                                                    <Edit className="w-3 h-3 mr-1" />
                                                    Edit
                                                </Button>

                                                <Button
                                                    variant="outline"
                                                    size="sm"
                                                    disabled={!canDeleteTemplate || deletingTemplates.includes(template.name)}
                                                    className={`h-8 w-8 p-0 transition-all duration-200 ${
                                                        !canDeleteTemplate 
                                                            ? 'opacity-40 cursor-not-allowed bg-muted text-muted-foreground border border-border/30' 
                                                            : 'border-red-500/30 text-red-500 hover:text-red-600 hover:border-red-500/50'
                                                    }`}
                                                    onClick={() => canDeleteTemplate && handleDeleteTemplate(template.name)}
                                                >
                                                    {deletingTemplates.includes(template.name) ? (
                                                        <Loader2 className="h-4 w-4 animate-spin" />
                                                    ) : (
                                                        <Trash2 className="h-4 w-4" />
                                                    )}
                                                </Button>
                                            </div>
                                        </CardContent>
                                    </Card>
                                </motion.div>
                            ))}
                        </AnimatePresence>
                    </motion.div>
                ) : (
                    <div className="text-center py-12">
                        <div className="space-y-4">
                            <FileText className="w-16 h-16 text-muted-foreground mx-auto opacity-50" />
                            <div>
                                <h3 className="text-lg font-semibold text-foreground">
                                    {searchTerm ? 'No templates found' : 'No templates are currently available'}
                                </h3>
                                <p className="text-muted-foreground mt-1">
                                    {searchTerm
                                        ? 'Try adjusting your search'
                                        : 'No templates are currently available'
                                    }
                                </p>
                            </div>
                            {searchTerm && (
                                <Button
                                    onClick={() => setSearchTerm('')}
                                    variant="outline"
                                >
                                    Clear Search
                                </Button>
                            )}
                        </div>
                    </div>
                )}
            </div>

            <Dialog open={editModalOpen} onOpenChange={setEditModalOpen}>
                <DialogContent className="max-w-md bg-background border border-border">
                    <DialogHeader>
                        <DialogTitle className="flex items-center gap-2 text-foreground">
                            <Edit className="w-5 h-5 text-[oklch(75.54% 0.1534 231.639)]" />
                            Edit Template
                        </DialogTitle>
                    </DialogHeader>

                    <div className="space-y-4 mt-6">
                        <div className="space-y-2">
                            <Label htmlFor="edit-template-name" className="text-sm font-medium text-foreground">
                                Template Name
                            </Label>
                            <Input
                                id="edit-template-name"
                                placeholder="Enter template name"
                                value={editName}
                                onChange={(e) => handleEditNameChange(e.target.value)}
                                className="h-10 text-sm border-border/50 focus:border-[oklch(75.54% 0.1534 231.639)] focus:ring-2 focus:ring-[oklch(75.54% 0.1534 231.639,0.2)] transition-all duration-200"
                            />
                            {editingTemplate && (
                                <p className="text-xs text-muted-foreground">
                                    Current name: {editingTemplate.name}
                                </p>
                            )}
                        </div>

                        {editError && (
                            <div className="p-3 bg-red-500/10 border border-red-500/20 rounded-lg">
                                <p className="text-sm text-red-500">{editError}</p>
                            </div>
                        )}

                        {editValidationError && (
                            <div className="p-3 bg-yellow-500/10 border border-yellow-500/20 rounded-lg">
                                <p className="text-sm text-yellow-500">{editValidationError}</p>
                            </div>
                        )}

                        <div className="flex items-center gap-3 pt-4">
                            <Button
                                onClick={handleSaveEdit}
                                disabled={isSaving || !editName.trim() || (editingTemplate ? editName === editingTemplate.name : false) || editValidationError !== null}
                                className="flex-1 h-10 text-sm font-medium hover:opacity-90 transition-all duration-200 shadow-lg shadow-[0_0_20px_rgba(75.54%,15.34%,231.639,0.3)] hover:shadow-[0_0_30px_rgba(75.54%,15.34%,231.639,0.4)]"
                                style={{ backgroundColor: 'oklch(75.54% .1534 231.639)' }}
                            >
                                {isSaving ? (
                                    <>
                                        <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                                        Saving...
                                    </>
                                ) : (
                                    <>
                                        <Save className="w-4 h-4 mr-2" />
                                        Save Changes
                                    </>
                                )}
                            </Button>

                            <Button
                                variant="outline"
                                onClick={closeEditModal}
                                disabled={isSaving}
                                className="h-10 px-6 text-sm border-border/50 text-muted-foreground hover:text-foreground hover:border-border/50"
                            >
                                <X className="w-4 h-4" />
                            </Button>
                        </div>
                    </div>
                </DialogContent>
            </Dialog>

            <Dialog open={createModalOpen} onOpenChange={setCreateModalOpen}>
                <DialogContent className="max-w-md bg-background border border-border">
                    <DialogHeader>
                        <DialogTitle className="flex items-center gap-2 text-foreground">
                            <Plus className="w-5 h-5 text-[oklch(75.54% 0.1534 231.639)]" />
                            Create New Template
                        </DialogTitle>
                    </DialogHeader>

                    <div className="space-y-4 mt-6">
                        <div className="space-y-2">
                            <Label htmlFor="create-template-name" className="text-sm font-medium text-foreground">
                                Template Name
                            </Label>
                            <Input
                                id="create-template-name"
                                placeholder="Enter template name"
                                value={newTemplateName}
                                onChange={(e) => handleCreateNameChange(e.target.value)}
                                className="h-10 text-sm border-border/50 focus:border-[oklch(75.54% 0.1534 231.639)] focus:ring-2 focus:ring-[oklch(75.54% 0.1534 231.639,0.2)] transition-all duration-200"
                            />
                        </div>

                        {createError && (
                            <div className="p-3 bg-red-500/10 border border-red-500/20 rounded-lg">
                                <p className="text-sm text-red-500">{createError}</p>
                            </div>
                        )}

                        {createValidationError && (
                            <div className="p-3 bg-yellow-500/10 border border-yellow-500/20 rounded-lg">
                                <p className="text-sm text-yellow-500">{createValidationError}</p>
                            </div>
                        )}

                        <div className="flex items-center gap-3 pt-4">
                            <Button
                                onClick={handleCreateTemplate}
                                disabled={isCreating || !newTemplateName.trim() || createValidationError !== null}
                                className="flex-1 h-10 text-sm font-medium hover:opacity-90 transition-all duration-200 shadow-lg shadow-[0_0_20px_rgba(75.54%,15.34%,231.639,0.3)] hover:shadow-[0_0_30px_rgba(75.54%,15.34%,231.639,0.4)]"
                                style={{ backgroundColor: 'oklch(75.54% .1534 231.639)' }}
                            >
                                {isCreating ? (
                                    <>
                                        <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                                        Creating...
                                    </>
                                ) : (
                                    <>
                                        <Plus className="w-4 h-4 mr-2" />
                                        Create Template
                                    </>
                                )}
                            </Button>

                            <Button
                                variant="outline"
                                onClick={closeCreateModal}
                                disabled={isCreating}
                                className="h-10 px-6 text-sm border-border/50 text-muted-foreground hover:text-foreground hover:border-border/50"
                            >
                                <X className="w-4 h-4" />
                            </Button>
                        </div>
                    </div>
                </DialogContent>
            </Dialog>

            <Dialog open={deleteConfirmModal.open} onOpenChange={(open) => {
                if (!open) {
                    setDeleteConfirmModal({ open: false, templateName: null });
                }
            }}>
                <DialogContent className="bg-background/95 border border-border/50 backdrop-blur-sm max-w-md">
                    <motion.div
                        initial={{ scale: 0.9, opacity: 0 }}
                        animate={{ scale: 1, opacity: 1 }}
                        transition={{ duration: 0.2 }}
                        className="text-center"
                    >
                        <motion.div
                            initial={{ scale: 0, rotate: -180 }}
                            animate={{ scale: 1, rotate: 0 }}
                            transition={{ duration: 0.4, delay: 0.1 }}
                            className="mx-auto w-16 h-16 bg-red-500/20 rounded-full flex items-center justify-center mb-4"
                        >
                            <Trash2 className="w-8 h-8 text-red-500" />
                        </motion.div>

                        <motion.h3
                            initial={{ y: 20, opacity: 0 }}
                            animate={{ y: 0, opacity: 1 }}
                            transition={{ duration: 0.3, delay: 0.2 }}
                            className="text-xl font-bold text-foreground mb-2"
                        >
                            Delete Template
                        </motion.h3>

                        <motion.p
                            initial={{ y: 20, opacity: 0 }}
                            animate={{ y: 0, opacity: 1 }}
                            transition={{ duration: 0.3, delay: 0.3 }}
                            className="text-muted-foreground mb-6"
                        >
                            Are you sure you want to delete the template &quot;{deleteConfirmModal.templateName}&quot;?
                            <br />
                            <span className="text-red-400 font-medium">This action cannot be undone.</span>
                        </motion.p>

                        <motion.div
                            initial={{ y: 20, opacity: 0 }}
                            animate={{ y: 0, opacity: 1 }}
                            transition={{ duration: 0.3, delay: 0.4 }}
                            className="flex gap-3 justify-center"
                        >
                            <Button
                                variant="outline"
                                onClick={() => setDeleteConfirmModal({ open: false, templateName: null })}
                                disabled={deletingTemplates.includes(deleteConfirmModal.templateName || '')}
                                className="border-border/50 text-foreground hover:bg-background/50 min-w-[100px]"
                            >
                                Cancel
                            </Button>

                            <Button
                                onClick={confirmDeleteTemplate}
                                disabled={deletingTemplates.includes(deleteConfirmModal.templateName || '')}
                                className="bg-red-600 hover:bg-red-700 text-white min-w-[100px] relative overflow-hidden"
                            >
                                {deletingTemplates.includes(deleteConfirmModal.templateName || '') ? (
                                    <>
                                        <motion.div
                                            className="absolute inset-0 bg-red-400"
                                            initial={{ x: '-100%' }}
                                            animate={{ x: '100%' }}
                                            transition={{ duration: 1, repeat: Infinity, ease: 'easeInOut' }}
                                        />
                                        <span className="relative z-10 flex items-center gap-2">
                                            <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
                                            Deleting...
                                        </span>
                                    </>
                                ) : (
                                    <span className="flex items-center gap-2">
                                        <Trash2 className="w-4 h-4" />
                                        Delete
                                    </span>
                                )}
                            </Button>
                        </motion.div>
                    </motion.div>
                </DialogContent>
            </Dialog>

            <Dialog open={successModalOpen} onOpenChange={setSuccessModalOpen}>
                <DialogContent className="bg-background/95 border border-border/50 backdrop-blur-sm max-w-md">
                    <motion.div
                        initial={{ scale: 0.9, opacity: 0 }}
                        animate={{ scale: 1, opacity: 1 }}
                        transition={{ duration: 0.2 }}
                        className="text-center"
                    >
                        <motion.div
                            initial={{ scale: 0, rotate: -180 }}
                            animate={{ scale: 1, rotate: 0 }}
                            transition={{ duration: 0.4, delay: 0.1 }}
                            className="mx-auto w-16 h-16 bg-green-500/20 rounded-full flex items-center justify-center mb-4"
                        >
                            <Check className="w-8 h-8 text-green-500" />
                        </motion.div>

                        <motion.h3
                            initial={{ y: 20, opacity: 0 }}
                            animate={{ y: 0, opacity: 1 }}
                            transition={{ duration: 0.3, delay: 0.2 }}
                            className="text-xl font-bold text-foreground mb-2"
                        >
                            Success
                        </motion.h3>

                        <motion.p
                            initial={{ y: 20, opacity: 0 }}
                            animate={{ y: 0, opacity: 1 }}
                            transition={{ duration: 0.3, delay: 0.3 }}
                            className="text-muted-foreground mb-6"
                        >
                            {successMessage}
                        </motion.p>

                        <motion.div
                            initial={{ y: 20, opacity: 0 }}
                            animate={{ y: 0, opacity: 1 }}
                            transition={{ duration: 0.3, delay: 0.4 }}
                            className="flex justify-center"
                        >
                            <Button
                                onClick={() => {
                                    setSuccessModalOpen(false);
                                    setSuccessMessage('');
                                }}
                                className="bg-green-600 hover:bg-green-700 text-white min-w-[100px]"
                            >
                                <Check className="w-4 h-4 mr-2" />
                                OK
                            </Button>
                        </motion.div>
                    </motion.div>
                </DialogContent>
            </Dialog>

            <Dialog open={errorModalOpen} onOpenChange={setErrorModalOpen}>
                <DialogContent className="bg-background/95 border border-border/50 backdrop-blur-sm max-w-md">
                    <motion.div
                        initial={{ scale: 0.9, opacity: 0 }}
                        animate={{ scale: 1, opacity: 1 }}
                        transition={{ duration: 0.2 }}
                        className="text-center"
                    >
                        <motion.div
                            initial={{ scale: 0, rotate: -180 }}
                            animate={{ scale: 1, rotate: 0 }}
                            transition={{ duration: 0.4, delay: 0.1 }}
                            className="mx-auto w-16 h-16 bg-red-500/20 rounded-full flex items-center justify-center mb-4"
                        >
                            <X className="w-8 h-8 text-red-500" />
                        </motion.div>

                        <motion.h3
                            initial={{ y: 20, opacity: 0 }}
                            animate={{ y: 0, opacity: 1 }}
                            transition={{ duration: 0.3, delay: 0.2 }}
                            className="text-xl font-bold text-foreground mb-2"
                        >
                            Error
                        </motion.h3>

                        <motion.p
                            initial={{ y: 20, opacity: 0 }}
                            animate={{ y: 0, opacity: 1 }}
                            transition={{ duration: 0.3, delay: 0.3 }}
                            className="text-muted-foreground mb-6"
                        >
                            {errorMessage}
                        </motion.p>

                        <motion.div
                            initial={{ y: 20, opacity: 0 }}
                            animate={{ y: 0, opacity: 1 }}
                            transition={{ duration: 0.3, delay: 0.4 }}
                            className="flex justify-center"
                        >
                            <Button
                                onClick={() => {
                                    setErrorModalOpen(false);
                                    setErrorMessage('');
                                }}
                                className="bg-red-600 hover:bg-red-700 text-white min-w-[100px]"
                            >
                                <X className="w-4 h-4 mr-2" />
                                OK
                            </Button>
                        </motion.div>
                    </motion.div>
                </DialogContent>
            </Dialog>
        </div>
    );
}

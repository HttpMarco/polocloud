'use client'

import { useState, useEffect, useCallback } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Dialog, DialogContent } from '@/components/ui/dialog';
import { ArrowLeft, Server, HardDrive, Activity, Save, X, Check, AlertCircle } from 'lucide-react';
import { motion } from 'framer-motion';

import { API_ENDPOINTS } from '@/lib/api';
import GlobalNavbar from '@/components/global-navbar';
import { Group } from '@/types/groups';

export default function GroupEditPage() {
    const params = useParams();
    const router = useRouter();
    const groupName = params.name as string;

    const [group, setGroup] = useState<Group | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [isSaving, setIsSaving] = useState(false);
    const [hasChanges, setHasChanges] = useState(false);

    const [successModalOpen, setSuccessModalOpen] = useState(false);
    const [errorModalOpen, setErrorModalOpen] = useState(false);
    const [confirmModalOpen, setConfirmModalOpen] = useState(false);
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const [formData, setFormData] = useState({
        minMemory: '',
        maxMemory: '',
        minOnlineService: '',
        maxOnlineService: '',
        percentageToStartNewService: ''
    });

    const [originalData, setOriginalData] = useState({
        minMemory: '',
        maxMemory: '',
        minOnlineService: '',
        maxOnlineService: '',
        percentageToStartNewService: ''
    });

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
            await fn();
        } catch {
            setErrorMessage('Operation failed');
            setErrorModalOpen(true);
        } finally {
            setIsSaving(false);
        }
    };

    const loadGroup = useCallback(async () => {
        await withLoading(async () => {
            const response = await fetch(API_ENDPOINTS.GROUPS.LIST);
            if (response.ok) {
                const data = await response.json();
                const foundGroup = data.find((g: Group) => g.name === groupName);

                if (foundGroup) {
                    setGroup(foundGroup);

                    const newFormData = {
                        minMemory: foundGroup.minMemory.toString(),
                        maxMemory: foundGroup.maxMemory.toString(),
                        minOnlineService: foundGroup.minOnlineService.toString(),
                        maxOnlineService: foundGroup.maxOnlineService.toString(),
                        percentageToStartNewService: foundGroup.percentageToStartNewService.toString()
                    };

                    setFormData(newFormData);
                    setOriginalData(newFormData);
                } else {
                    setError('Group not found');
                }
            } else {
                setError('Failed to load group');
            }
        });
    }, [groupName]);

    useEffect(() => {
        if (groupName) {
            loadGroup();
        }
    }, [groupName, loadGroup]);

    const handleInputChange = (field: keyof typeof formData, value: string) => {
        let processedValue = value;
        if (field === 'minMemory' || field === 'maxMemory' || field === 'minOnlineService' || field === 'maxOnlineService' || field === 'percentageToStartNewService') {
            if (field === 'percentageToStartNewService') {
                processedValue = value.replace(/[^0-9]/g, '');
                if (processedValue === '' || parseInt(processedValue) < 0) {
                    processedValue = '0';
                }
                if (parseInt(processedValue) > 100) {
                    processedValue = '100';
                }
            } else {
                processedValue = value.replace(/[^0-9]/g, '');
                if (processedValue === '' || parseInt(processedValue) <= 0) {
                    processedValue = '1';
                }
            }
        }

        setFormData(prev => ({ ...prev, [field]: processedValue }));

        const newData = { ...formData, [field]: processedValue };
        const hasChangesNow = Object.keys(newData).some(key =>
            newData[key as keyof typeof newData] !== originalData[key as keyof typeof originalData]
        );
        setHasChanges(hasChangesNow);
    };

    const handleSave = async () => {
        if (!hasChanges) return;

        await withSaving(async () => {
            const response = await fetch(API_ENDPOINTS.GROUPS.EDIT(groupName), {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    minMemory: parseInt(formData.minMemory),
                    maxMemory: parseInt(formData.maxMemory),
                    minOnlineService: parseInt(formData.minOnlineService),
                    maxOnlineService: parseInt(formData.maxOnlineService),
                    percentageToStartNewService: parseFloat(formData.percentageToStartNewService)
                })
            });

            if (response.ok) {
                setOriginalData(formData);
                setHasChanges(false);

                setSuccessMessage('Group updated successfully!');
                setSuccessModalOpen(true);
            } else {
                const errorData = await response.json();
                setErrorMessage(errorData.error || 'Failed to update group');
                setErrorModalOpen(true);
            }
        });
    };

    const handleCancel = () => {
        if (hasChanges) {
            setConfirmModalOpen(true);
        } else {
            router.push(`/groups/${groupName}`);
        }
    };

    const handleConfirmCancel = () => {
        setConfirmModalOpen(false);
        router.push(`/groups/${groupName}`);
    };

    if (isLoading) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20 ultra-smooth-scroll">
                <GlobalNavbar />
                <div className="flex items-center justify-center min-h-screen">
                    <div className="text-center">
                        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-[oklch(0.7554_0.1534_231.639)] mx-auto mb-4"></div>
                        <p className="text-muted-foreground">Loading group...</p>
                    </div>
                </div>
            </div>
        );
    }

    if (error || !group) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20 ultra-smooth-scroll">
                <GlobalNavbar />
                <div className="text-center py-12">
                    <Server className="w-16 h-16 text-muted-foreground mx-auto opacity-50 mb-4" />
                    <h3 className="text-lg font-semibold text-foreground mb-2">
                        {error || 'Group not found'}
                    </h3>
                    <Button onClick={() => router.push('/groups')} variant="outline">
                        Back to Groups
                    </Button>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20 ultra-smooth-scroll">
            <GlobalNavbar />
            
            <div className="flex flex-col max-w-none w-full mx-auto p-8">
                <motion.div
                    className="text-center mb-12 group-header"
                    initial={{ opacity: 0, y: -30 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.8, ease: "easeOut" }}
                >
                    <motion.div
                        className="flex items-center justify-center gap-4 mb-6"
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.8, delay: 0.1 }}
                    >
                        <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => router.push(`/groups/${groupName}`)}
                            className="hover:bg-background/50 text-muted-foreground hover:text-foreground"
                        >
                            <ArrowLeft className="w-4 h-4 mr-2" />
                            Back to Group
                        </Button>
                    </motion.div>

                    <motion.h1
                        className="text-5xl font-bold bg-gradient-to-r from-foreground via-foreground to-muted-foreground bg-clip-text text-transparent mb-4"
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.8, delay: 0.2 }}
                    >
                        Edit Group: {group.name}
                    </motion.h1>
                    <motion.p
                        className="text-xl text-muted-foreground max-w-3xl mx-auto leading-relaxed mb-8"
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.8, delay: 0.4 }}
                    >
                        Modify group configuration settings
                    </motion.p>

                    <motion.div 
                        className="flex gap-4 justify-center"
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.8, delay: 0.6 }}
                    >
                        <Button
                            variant="outline"
                            onClick={handleCancel}
                            className="h-11 px-6 text-sm border-border/50 text-foreground hover:bg-background/50"
                        >
                            <X className="w-4 h-4 mr-2" />
                            Cancel
                        </Button>
                        <Button
                            onClick={handleSave}
                            disabled={!hasChanges || isSaving}
                            className="h-11 px-6 text-sm font-medium hover:opacity-90 transition-all duration-200 shadow-lg shadow-[0_0_20px_rgba(75.54%,15.34%,231.639,0.3)] hover:shadow-[0_0_30px_rgba(75.54%,15.34%,231.639,0.4)] disabled:opacity-50"
                            style={{ backgroundColor: 'oklch(75.54% .1534 231.639)' }}
                        >
                            <Save className="w-4 h-4 mr-2" />
                            {isSaving ? 'Saving...' : 'Save Changes'}
                        </Button>
                    </motion.div>
                </motion.div>

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    <div className="lg:col-span-2 space-y-8">
                        <motion.div
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.8, delay: 0.8 }}
                        >
                            <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm rounded-xl">
                                <CardHeader className="pb-4">
                                    <CardTitle className="flex items-center gap-3 text-foreground text-xl">
                                        <div className="p-2 bg-[oklch(75.54%_0.1534_231.639,0.1)] rounded-lg">
                                            <HardDrive className="w-5 h-5 text-[oklch(75.54% 0.1534 231.639)]" />
                                        </div>
                                        Memory Configuration
                                    </CardTitle>
                                </CardHeader>
                                <CardContent className="space-y-6">
                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                        <div className="space-y-3">
                                            <Label htmlFor="minMemory" className="text-sm font-medium text-foreground">Minimum Memory (MB)</Label>
                                            <Input
                                                id="minMemory"
                                                type="number"
                                                min="1"
                                                step="1"
                                                value={formData.minMemory}
                                                onChange={(e) => handleInputChange('minMemory', e.target.value)}
                                                placeholder="512"
                                                className="border-border/50 bg-background/50 focus:border-[oklch(75.54%_0.1534_231.639,0.5)] focus:ring-[oklch(75.54%_0.1534_231.639,0.2)]"
                                            />
                                        </div>

                                        <div className="space-y-3">
                                            <Label htmlFor="maxMemory" className="text-sm font-medium text-foreground">Maximum Memory (MB)</Label>
                                            <Input
                                                id="maxMemory"
                                                type="number"
                                                min="1"
                                                step="1"
                                                value={formData.maxMemory}
                                                onChange={(e) => handleInputChange('maxMemory', e.target.value)}
                                                placeholder="2048"
                                                className="border-border/50 bg-background/50 focus:border-[oklch(75.54%_0.1534_231.639,0.5)] focus:ring-[oklch(75.54%_0.1534_231.639,0.2)]"
                                            />
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>
                        </motion.div>
                        <motion.div
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.8, delay: 1.0 }}
                        >
                            <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm rounded-xl">
                                <CardHeader className="pb-4">
                                    <CardTitle className="flex items-center gap-3 text-foreground text-xl">
                                        <div className="p-2 bg-[oklch(75.54%_0.1534_231.639,0.1)] rounded-lg">
                                            <Activity className="w-5 h-5 text-[oklch(75.54% 0.1534 231.639)]" />
                                        </div>
                                        Service Limits
                                    </CardTitle>
                                </CardHeader>
                                <CardContent className="space-y-6">
                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                        <div className="space-y-3">
                                            <Label htmlFor="minOnlineService" className="text-sm font-medium text-foreground">Minimum Services Online</Label>
                                            <Input
                                                id="minOnlineService"
                                                type="number"
                                                min="1"
                                                step="1"
                                                value={formData.minOnlineService}
                                                onChange={(e) => handleInputChange('minOnlineService', e.target.value)}
                                                placeholder="1"
                                                className="border-border/50 bg-background/50 focus:border-[oklch(75.54%_0.1534_231.639,0.5)] focus:ring-[oklch(75.54%_0.1534_231.639,0.2)]"
                                            />
                                        </div>

                                        <div className="space-y-3">
                                            <Label htmlFor="maxOnlineService" className="text-sm font-medium text-foreground">Maximum Services Online</Label>
                                            <Input
                                                id="maxOnlineService"
                                                type="number"
                                                min="1"
                                                step="1"
                                                value={formData.maxOnlineService}
                                                onChange={(e) => handleInputChange('maxOnlineService', e.target.value)}
                                                placeholder="5"
                                                className="border-border/50 bg-background/50 focus:border-[oklch(75.54%_0.1534_231.639,0.5)] focus:ring-[oklch(75.54%_0.1534_231.639,0.2)]"
                                            />
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>
                        </motion.div>
                        <motion.div
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.8, delay: 1.2 }}
                        >
                            <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm rounded-xl">
                                <CardHeader className="pb-4">
                                    <CardTitle className="flex items-center gap-3 text-foreground text-xl">
                                        <div className="p-2 bg-[oklch(75.54%_0.1534_231.639,0.1)] rounded-lg">
                                            <Server className="w-5 h-5 text-[oklch(75.54% 0.1534 231.639)]" />
                                        </div>
                                        Service Threshold
                                    </CardTitle>
                                </CardHeader>
                                <CardContent className="space-y-6">
                                    <div className="space-y-3">
                                        <Label htmlFor="percentageToStartNewService" className="text-sm font-medium text-foreground">New Service Threshold (%)</Label>
                                        <Input
                                            id="percentageToStartNewService"
                                            type="number"
                                            step="1"
                                            value={formData.percentageToStartNewService}
                                            onChange={(e) => handleInputChange('percentageToStartNewService', e.target.value)}
                                            placeholder="80"
                                            min="0"
                                            max="100"
                                            className="border-border/50 bg-background/50 focus:border-[oklch(75.54%_0.1534_231.639,0.5)] focus:ring-[oklch(75.54%_0.1534_231.639,0.2)]"
                                        />
                                        <p className="text-sm text-muted-foreground">
                                            Percentage of memory usage to trigger starting a new service
                                        </p>
                                    </div>
                                </CardContent>
                            </Card>
                        </motion.div>
                    </div>
                    <div className="space-y-6">
                        <motion.div
                            initial={{ opacity: 0, x: 20 }}
                            animate={{ opacity: 1, x: 0 }}
                            transition={{ duration: 0.8, delay: 1.4 }}
                        >
                            <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm rounded-xl sticky top-6">
                                <CardHeader className="pb-4">
                                    <CardTitle className="flex items-center gap-3 text-foreground text-xl">
                                        <div className="p-2 bg-[oklch(75.54%_0.1534_231.639,0.1)] rounded-lg">
                                            <Server className="w-5 h-5 text-[oklch(75.54% 0.1534 231.639)]" />
                                        </div>
                                        Group Preview
                                    </CardTitle>
                                </CardHeader>
                                <CardContent className="space-y-6">
                                    <div className="space-y-4">
                                        <div className="flex items-center justify-between py-2 border-b border-border/20">
                                            <span className="text-sm font-medium text-muted-foreground">Name:</span>
                                            <span className="text-sm font-semibold text-foreground">{group.name}</span>
                                        </div>

                                        <div className="flex items-center justify-between py-2 border-b border-border/20">
                                            <span className="text-sm font-medium text-muted-foreground">Platform:</span>
                                            <span className="text-sm font-semibold text-foreground">
                                                {typeof group.platform === 'string'
                                                    ? group.platform
                                                    : `${group.platform.name} ${group.platform.version}`}
                                            </span>
                                        </div>

                                        <div className="flex items-center justify-between py-2 border-b border-border/20">
                                            <span className="text-sm font-medium text-muted-foreground">Memory:</span>
                                            <span className="text-sm font-semibold text-foreground">
                                                {formData.minMemory && formData.maxMemory
                                                    ? `${formData.minMemory}MB - ${formData.maxMemory}MB`
                                                    : 'Not set'}
                                            </span>
                                        </div>

                                        <div className="flex items-center justify-between py-2 border-b border-border/20">
                                            <span className="text-sm font-medium text-muted-foreground">Services:</span>
                                            <span className="text-sm font-semibold text-foreground">
                                                {formData.minOnlineService && formData.maxOnlineService
                                                    ? `${formData.minOnlineService} - ${formData.maxOnlineService}`
                                                    : 'Not set'}
                                            </span>
                                        </div>

                                        <div className="flex items-center justify-between py-2">
                                            <span className="text-sm font-medium text-muted-foreground">Threshold:</span>
                                            <span className="text-sm font-semibold text-foreground">
                                                {formData.percentageToStartNewService
                                                    ? `${formData.percentageToStartNewService}%`
                                                    : 'Not set'}
                                            </span>
                                        </div>
                                    </div>

                                    {hasChanges && (
                                        <div className="pt-4 border-t border-border/30">
                                            <div className="flex items-center gap-2 text-sm text-amber-500 font-medium bg-amber-500/10 px-3 py-2 rounded-lg">
                                                <AlertCircle className="w-4 h-4" />
                                                You have unsaved changes
                                            </div>
                                        </div>
                                    )}
                                </CardContent>
                            </Card>
                        </motion.div>
                    </div>
                </div>
            </div>
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
                                    router.push(`/groups/${groupName}`);
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

            <Dialog open={confirmModalOpen} onOpenChange={setConfirmModalOpen}>
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
                            className="mx-auto w-16 h-16 bg-amber-500/20 rounded-full flex items-center justify-center mb-4"
                        >
                            <AlertCircle className="w-8 h-8 text-amber-500" />
                        </motion.div>

                        <motion.h3
                            initial={{ y: 20, opacity: 0 }}
                            animate={{ y: 0, opacity: 1 }}
                            transition={{ duration: 0.3, delay: 0.2 }}
                            className="text-xl font-bold text-foreground mb-2"
                        >
                            Unsaved Changes
                        </motion.h3>

                        <motion.p
                            initial={{ y: 20, opacity: 0 }}
                            animate={{ y: 0, opacity: 1 }}
                            transition={{ duration: 0.3, delay: 0.3 }}
                            className="text-muted-foreground mb-6"
                        >
                            You have unsaved changes. Are you sure you want to cancel?
                        </motion.p>

                        <motion.div
                            initial={{ y: 20, opacity: 0 }}
                            animate={{ y: 0, opacity: 1 }}
                            transition={{ duration: 0.3, delay: 0.4 }}
                            className="flex gap-3 justify-center"
                        >
                            <Button
                                variant="outline"
                                onClick={() => setConfirmModalOpen(false)}
                                className="border-border/50 text-foreground hover:bg-background/50 min-w-[100px]"
                            >
                                Keep Editing
                            </Button>

                            <Button
                                onClick={handleConfirmCancel}
                                className="bg-amber-600 hover:bg-amber-700 text-white min-w-[100px]"
                            >
                                <X className="w-4 h-4 mr-2" />
                                Discard
                            </Button>
                        </motion.div>
                    </motion.div>
                </DialogContent>
            </Dialog>
        </div>
    );
}

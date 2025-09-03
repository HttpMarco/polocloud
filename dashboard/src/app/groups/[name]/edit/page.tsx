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

    const loadGroup = useCallback(async () => {
        try {
            setIsLoading(true);
            setError(null);

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
        } catch {
            setError('Failed to load group');
        } finally {
            setIsLoading(false);
        }
    }, [groupName]);

    useEffect(() => {
        if (groupName) {
            loadGroup();
        }
    }, [groupName, loadGroup]);

    const handleInputChange = (field: keyof typeof formData, value: string) => {
        setFormData(prev => ({ ...prev, [field]: value }));

        const newData = { ...formData, [field]: value };
        const hasChangesNow = Object.keys(newData).some(key =>
            newData[key as keyof typeof newData] !== originalData[key as keyof typeof originalData]
        );
        setHasChanges(hasChangesNow);
    };

    const handleSave = async () => {
        if (!hasChanges) return;

        setIsSaving(true);
        try {
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
        } catch {
            setErrorMessage('Failed to update group');
            setErrorModalOpen(true);
        } finally {
            setIsSaving(false);
        }
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

            <div className="h-2"></div>

            <div className="px-6 py-6">
                <div className="mb-8">
                    <motion.div
                        initial={{ opacity: 0, y: -20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.5 }}
                        className="flex items-center gap-4 mb-4"
                    >
                        <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => router.push(`/groups/${groupName}`)}
                            className="hover:bg-background/50"
                        >
                            <ArrowLeft className="w-4 h-4 mr-2" />
                            Back to Group
                        </Button>
                    </motion.div>

                    <motion.h1
                        className="text-4xl font-bold text-foreground mb-3"
                        initial={{ opacity: 0, y: -20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.6 }}
                    >
                        Edit Group: {group.name}
                    </motion.h1>
                    <motion.p
                        className="text-lg text-muted-foreground"
                        initial={{ opacity: 0, y: -10 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.6, delay: 0.1 }}
                    >
                        Modify group configuration settings
                    </motion.p>
                </div>

                <motion.div
                    initial={{ opacity: 0, y: -10 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.6, delay: 0.2 }}
                    className="flex items-center gap-3 mb-8"
                >
                    <Button
                        variant="outline"
                        onClick={handleCancel}
                        className="border-border/50 text-foreground hover:bg-background/50"
                    >
                        <X className="w-4 h-4 mr-2" />
                        Cancel
                    </Button>
                    <Button
                        onClick={handleSave}
                        disabled={!hasChanges || isSaving}
                        className="bg-[oklch(0.7554_0.1534_231.639)] text-white hover:opacity-90 disabled:opacity-50"
                    >
                        <Save className="w-4 h-4 mr-2" />
                        {isSaving ? 'Saving...' : 'Save Changes'}
                    </Button>
                </motion.div>
            </div>

            <div className="px-6 pb-6">
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                    <div className="lg:col-span-2 space-y-6">
                        <motion.div
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.5, delay: 0.1 }}
                        >
                            <Card className="border-border/50 bg-card/50 backdrop-blur-sm">
                                <CardHeader>
                                    <CardTitle className="flex items-center gap-2 text-xl">
                                        <HardDrive className="w-5 h-5 text-[oklch(0.7554_0.1534_231.639)]" />
                                        Memory Configuration
                                    </CardTitle>
                                </CardHeader>
                                <CardContent className="space-y-4">
                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                        <div className="space-y-2">
                                            <Label htmlFor="minMemory">Minimum Memory (MB)</Label>
                                            <Input
                                                id="minMemory"
                                                type="number"
                                                value={formData.minMemory}
                                                onChange={(e) => handleInputChange('minMemory', e.target.value)}
                                                placeholder="512"
                                                className="border-border/50 bg-background/50"
                                            />
                                        </div>

                                        <div className="space-y-2">
                                            <Label htmlFor="maxMemory">Maximum Memory (MB)</Label>
                                            <Input
                                                id="maxMemory"
                                                type="number"
                                                value={formData.maxMemory}
                                                onChange={(e) => handleInputChange('maxMemory', e.target.value)}
                                                placeholder="2048"
                                                className="border-border/50 bg-background/50"
                                            />
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>
                        </motion.div>

                        <motion.div
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.5, delay: 0.2 }}
                        >
                            <Card className="border-border/50 bg-card/50 backdrop-blur-sm">
                                <CardHeader>
                                    <CardTitle className="flex items-center gap-2 text-xl">
                                        <Activity className="w-5 h-5 text-[oklch(0.7554_0.1534_231.639)]" />
                                        Service Limits
                                    </CardTitle>
                                </CardHeader>
                                <CardContent className="space-y-4">
                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                        <div className="space-y-2">
                                            <Label htmlFor="minOnlineService">Minimum Services Online</Label>
                                            <Input
                                                id="minOnlineService"
                                                type="number"
                                                value={formData.minOnlineService}
                                                onChange={(e) => handleInputChange('minOnlineService', e.target.value)}
                                                placeholder="1"
                                                className="border-border/50 bg-background/50"
                                            />
                                        </div>

                                        <div className="space-y-2">
                                            <Label htmlFor="maxOnlineService">Maximum Services Online</Label>
                                            <Input
                                                id="maxOnlineService"
                                                type="number"
                                                value={formData.maxOnlineService}
                                                onChange={(e) => handleInputChange('maxOnlineService', e.target.value)}
                                                placeholder="5"
                                                className="border-border/50 bg-background/50"
                                            />
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>
                        </motion.div>

                        <motion.div
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.5, delay: 0.3 }}
                        >
                            <Card className="border-border/50 bg-card/50 backdrop-blur-sm">
                                <CardHeader>
                                    <CardTitle className="flex items-center gap-2 text-xl">
                                        <Server className="w-5 h-5 text-[oklch(0.7554_0.1534_231.639)]" />
                                        Service Threshold
                                    </CardTitle>
                                </CardHeader>
                                <CardContent className="space-y-4">
                                    <div className="space-y-2">
                                        <Label htmlFor="percentageToStartNewService">New Service Threshold (%)</Label>
                                        <Input
                                            id="percentageToStartNewService"
                                            type="number"
                                            value={formData.percentageToStartNewService}
                                            onChange={(e) => handleInputChange('percentageToStartNewService', e.target.value)}
                                            placeholder="80"
                                            min="0"
                                            max="100"
                                            step="0.1"
                                            className="border-border/50 bg-background/50"
                                        />
                                        <p className="text-xs text-muted-foreground">
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
                            transition={{ duration: 0.5, delay: 0.4 }}
                        >
                            <Card className="border-border/50 bg-card/50 backdrop-blur-sm sticky top-6">
                                <CardHeader>
                                    <CardTitle className="text-lg">Group Preview</CardTitle>
                                </CardHeader>
                                <CardContent className="space-y-4">
                                    <div className="space-y-3">
                                        <div className="flex items-center justify-between">
                                            <span className="text-sm text-muted-foreground">Name:</span>
                                            <span className="text-sm font-medium text-foreground">{group.name}</span>
                                        </div>

                                        <div className="flex items-center justify-between">
                                            <span className="text-sm text-muted-foreground">Platform:</span>
                                            <span className="text-sm font-medium text-foreground">
                                                {typeof group.platform === 'string'
                                                    ? group.platform
                                                    : `${group.platform.name} ${group.platform.version}`}
                                            </span>
                                        </div>

                                        <div className="flex items-center justify-between">
                                            <span className="text-sm text-muted-foreground">Memory:</span>
                                            <span className="text-sm font-medium text-foreground">
                                                {formData.minMemory && formData.maxMemory
                                                    ? `${formData.minMemory}MB - ${formData.maxMemory}MB`
                                                    : 'Not set'}
                                            </span>
                                        </div>

                                        <div className="flex items-center justify-between">
                                            <span className="text-sm text-muted-foreground">Services:</span>
                                            <span className="text-sm font-medium text-foreground">
                                                {formData.minOnlineService && formData.maxOnlineService
                                                    ? `${formData.minOnlineService} - ${formData.maxOnlineService}`
                                                    : 'Not set'}
                                            </span>
                                        </div>

                                        <div className="flex items-center justify-between">
                                            <span className="text-sm text-muted-foreground">Threshold:</span>
                                            <span className="text-sm font-medium text-foreground">
                                                {formData.percentageToStartNewService
                                                    ? `${formData.percentageToStartNewService}%`
                                                    : 'Not set'}
                                            </span>
                                        </div>
                                    </div>

                                    {hasChanges && (
                                        <div className="pt-4 border-t border-border/30">
                                            <div className="text-xs text-amber-500 font-medium">
                                                ⚠️ You have unsaved changes
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

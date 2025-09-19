'use client'

import { useState, useEffect, useCallback } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Dialog, DialogContent } from '@/components/ui/dialog';
import { Server, HardDrive, Activity, Settings, Users, Trash2, Edit, Cpu } from 'lucide-react';
import { motion } from 'framer-motion';

import { API_ENDPOINTS } from '@/lib/api';
import GlobalNavbar from '@/components/global-navbar';
import { Group } from '@/types/groups';
import { toast } from 'sonner';
import { usePermissions } from '@/hooks/usePermissions';

interface Service {
    name: string;
    state: string;
    groupName?: string;
    [key: string]: string | number | boolean | undefined;
}

export default function GroupOverviewPage() {
    const params = useParams();
    const router = useRouter();
    const groupName = params.name as string;
    
    const [group, setGroup] = useState<Group | null>(null);
    const [groupServices, setGroupServices] = useState<Service[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [deleteModalOpen, setDeleteModalOpen] = useState(false);
    const [isDeleting, setIsDeleting] = useState(false);

    const { hasPermission } = usePermissions();
    const canEditGroup = hasPermission('polocloud.group.edit');
    const canDeleteGroup = hasPermission('polocloud.group.delete');

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

    const loadGroupServices = useCallback(async () => {
        try {
            const response = await fetch(API_ENDPOINTS.SERVICES.LIST);
            if (response.ok) {
                const allServices: Service[] = await response.json();

                const servicesForGroup = allServices.filter(service => 
                    service.groupName === groupName
                );
                
                setGroupServices(servicesForGroup);
            }
        } catch (error) {
        console.warn('Error in page:', error);
      }}, [groupName]);

    useEffect(() => {
        if (groupName) {
            loadGroup();
            loadGroupServices();
        }
    }, [groupName, loadGroup, loadGroupServices]);



    const handleDeleteGroup = () => {
        setDeleteModalOpen(true);
    };

    const confirmDelete = async () => {
        setIsDeleting(true);
        try {
            const response = await fetch(API_ENDPOINTS.GROUPS.DELETE(groupName), {
                method: 'DELETE'
            });

            if (response.ok) {

                router.push('/groups');
            } else {
                const errorData = await response.json();
                toast.error(errorData.error || 'Failed to delete group');
            }
        } catch {
            toast.error('Failed to delete group');
        } finally {
            setIsDeleting(false);
            setDeleteModalOpen(false);
        }
    };

    if (isLoading) {
        return (
            <div className="w-full h-full flex items-center justify-center">
                <div className="w-8 h-8 border-2 border-primary border-t-transparent rounded-full animate-spin"></div>
            </div>
        );
    }

    if (error || !group) {
        return (
            <div className="w-full h-full flex items-center justify-center">
                <div className="text-center">
                    <p className="text-red-500 mb-4">{error || 'Group not found'}</p>
                    <Button onClick={() => router.push('/groups')}>Back to Groups</Button>
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
                    <motion.h1
                        className="text-5xl font-bold bg-gradient-to-r from-foreground via-foreground to-muted-foreground bg-clip-text text-transparent mb-4"
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.8, delay: 0.2 }}
                    >
                        {group.name}
                    </motion.h1>
                    <motion.p
                        className="text-xl text-muted-foreground max-w-3xl mx-auto leading-relaxed mb-8"
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.8, delay: 0.4 }}
                    >
                        Detailed overview and configuration for this server group
                    </motion.p>

                    <motion.div 
                        className="flex gap-4 justify-center"
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.8, delay: 0.6 }}
                    >
                        <Button
                            onClick={() => router.push(`/groups/${groupName}/edit`)}
                            disabled={!canEditGroup}
                            className={`h-11 px-6 text-sm font-medium transition-all duration-200 ${
                                canEditGroup 
                                    ? 'hover:opacity-90 shadow-lg shadow-[0_0_20px_rgba(75.54%,15.34%,231.639,0.3)] hover:shadow-[0_0_30px_rgba(75.54%,15.34%,231.639,0.4)]' 
                                    : 'opacity-40 cursor-not-allowed bg-muted text-muted-foreground border border-border/30'
                            }`}
                            style={{ 
                                backgroundColor: canEditGroup ? 'oklch(75.54% .1534 231.639)' : undefined
                            }}
                        >
                            <Edit className="w-4 h-4 mr-2" />
                            Edit Group
                        </Button>
                        
                        <Button
                            variant="outline"
                            onClick={handleDeleteGroup}
                            disabled={!canDeleteGroup}
                            className={`h-11 px-6 text-sm transition-all duration-200 ${
                                canDeleteGroup 
                                    ? 'border-red-500/30 text-red-600 hover:bg-red-500/10 hover:border-red-500/50' 
                                    : 'opacity-40 cursor-not-allowed bg-muted text-muted-foreground border border-border/30'
                            }`}
                        >
                            <Trash2 className="w-4 h-4 mr-2" />
                            Delete Group
                        </Button>
                    </motion.div>
                </motion.div>

                <motion.div
                    className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-12"
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.8, delay: 0.8 }}
                >
                    <motion.div
                        className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm rounded-xl p-6"
                        whileHover={{ scale: 1.02, y: -2 }}
                        transition={{ duration: 0.2 }}
                    >
                        <div className="flex items-center gap-3 mb-4">
                            <div className="p-2 bg-[oklch(75.54%_0.1534_231.639,0.1)] rounded-lg">
                                <HardDrive className="w-5 h-5 text-[oklch(75.54% 0.1534 231.639)]" />
                            </div>
                            <div>
                                <h3 className="text-sm font-medium text-muted-foreground uppercase tracking-wide">Memory Range</h3>
                                <p className="text-2xl font-bold text-foreground">{group.minMemory} - {group.maxMemory} MB</p>
                            </div>
                        </div>
                    </motion.div>

                    <motion.div
                        className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm rounded-xl p-6"
                        whileHover={{ scale: 1.02, y: -2 }}
                        transition={{ duration: 0.2 }}
                    >
                        <div className="flex items-center gap-3 mb-4">
                            <div className="p-2 bg-[oklch(75.54%_0.1534_231.639,0.1)] rounded-lg">
                                <Activity className="w-5 h-5 text-[oklch(75.54% 0.1534 231.639)]" />
                            </div>
                            <div>
                                <h3 className="text-sm font-medium text-muted-foreground uppercase tracking-wide">Service Range</h3>
                                <p className="text-2xl font-bold text-foreground">{group.minOnlineService} - {group.maxOnlineService}</p>
                            </div>
                        </div>
                    </motion.div>

                    <motion.div
                        className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm rounded-xl p-6"
                        whileHover={{ scale: 1.02, y: -2 }}
                        transition={{ duration: 0.2 }}
                    >
                        <div className="flex items-center gap-3 mb-4">
                            <div className="p-2 bg-[oklch(75.54%_0.1534_231.639,0.1)] rounded-lg">
                                <Cpu className="w-5 h-5 text-[oklch(75.54% 0.1534 231.639)]" />
                            </div>
                            <div>
                                <h3 className="text-sm font-medium text-muted-foreground uppercase tracking-wide">Start Threshold</h3>
                                <p className="text-2xl font-bold text-foreground">{group.percentageToStartNewService}%</p>
                            </div>
                        </div>
                    </motion.div>
                </motion.div>

                <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">

                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.8, delay: 1.0 }}
                    >
                        <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm rounded-xl">
                            <CardHeader className="pb-4">
                                <CardTitle className="flex items-center gap-3 text-foreground text-xl">
                                    <div className="p-2 bg-[oklch(75.54%_0.1534_231.639,0.1)] rounded-lg">
                                        <Server className="w-5 h-5 text-[oklch(75.54% 0.1534 231.639)]" />
                                    </div>
                                    Basic Information
                                </CardTitle>
                            </CardHeader>
                            <CardContent className="space-y-6">
                                <div className="space-y-4">
                                    <div className="flex justify-between items-center py-3 border-b border-border/20">
                                        <span className="text-sm font-medium text-muted-foreground">Platform</span>
                                        <span className="text-sm font-semibold text-foreground">{group.platform.name} {group.platform.version}</span>
                                    </div>
                                    <div className="flex justify-between items-center py-3 border-b border-border/20">
                                        <span className="text-sm font-medium text-muted-foreground">Created</span>
                                        <span className="text-sm font-semibold text-foreground">
                                            {group.createdAt ? 
                                                new Date(group.createdAt).toLocaleDateString('en-US', {
                                                    year: 'numeric',
                                                    month: 'short',
                                                    day: 'numeric',
                                                    hour: '2-digit',
                                                    minute: '2-digit'
                                                }) : 
                                                'Unknown'
                                            }
                                        </span>
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
                                        <Settings className="w-5 h-5 text-[oklch(75.54% 0.1534 231.639)]" />
                                    </div>
                                    Properties & Settings
                                </CardTitle>
                            </CardHeader>
                            <CardContent className="space-y-6">
                                <div className="space-y-4">
                                    <div className="flex justify-between items-center py-3 border-b border-border/20">
                                        <span className="text-sm font-medium text-muted-foreground">Static Service</span>
                                        <Badge 
                                            className={`text-xs px-3 py-1 ${
                                                group.groupProperties?.static 
                                                    ? 'bg-green-500/20 text-green-600 border border-green-500/30' 
                                                    : 'bg-red-500/20 text-red-600 border border-red-500/30'
                                            }`}
                                        >
                                            {group.groupProperties?.static ? "Yes" : "No"}
                                        </Badge>
                                    </div>
                                    <div className="flex justify-between items-center py-3">
                                        <span className="text-sm font-medium text-muted-foreground">Fallback</span>
                                        <Badge 
                                            className={`text-xs px-3 py-1 ${
                                                group.groupProperties?.fallback 
                                                    ? 'bg-green-500/20 text-green-600 border border-green-500/30' 
                                                    : 'bg-red-500/20 text-red-600 border border-red-500/30'
                                            }`}
                                        >
                                            {group.groupProperties?.fallback ? "Yes" : "No"}
                                        </Badge>
                                    </div>
                                </div>
                            </CardContent>
                        </Card>
                    </motion.div>
                </div>

                <div className="mt-8 space-y-8">

                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.8, delay: 1.4 }}
                    >
                        <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm rounded-xl">
                            <CardHeader className="pb-4">
                                <CardTitle className="flex items-center gap-3 text-foreground text-xl">
                                    <div className="p-2 bg-[oklch(75.54%_0.1534_231.639,0.1)] rounded-lg">
                                        <Users className="w-5 h-5 text-[oklch(75.54% 0.1534 231.639)]" />
                                    </div>
                                    Templates ({group.templates?.length || 0})
                                </CardTitle>
                            </CardHeader>
                            <CardContent>
                                {group.templates && group.templates.length > 0 ? (
                                    <div className="flex flex-wrap gap-3">
                                        {group.templates.map((template, idx) => (
                                            <motion.div
                                                key={idx}
                                                whileHover={{ scale: 1.05 }}
                                                whileTap={{ scale: 0.95 }}
                                            >
                                                <Badge 
                                                    variant="secondary" 
                                                    className="text-sm px-4 py-2 text-muted-foreground bg-background/50 border border-border/30 hover:bg-background/70 transition-colors duration-200 cursor-pointer"
                                                    onClick={() => router.push('/templates')}
                                                >
                                                    {template.name}
                                                </Badge>
                                            </motion.div>
                                        ))}
                                    </div>
                                ) : (
                                    <div className="text-center py-12">
                                        <div className="w-20 h-20 bg-[oklch(75.54%_0.1534_231.639,0.1)] rounded-full flex items-center justify-center mx-auto mb-6">
                                            <Users className="w-10 h-10 text-[oklch(75.54% 0.1534 231.639)]" />
                                        </div>
                                        <h3 className="text-lg font-semibold text-foreground mb-2">No Templates</h3>
                                        <p className="text-muted-foreground mb-6">No templates configured for this group</p>
                                        <Button
                                            variant="outline"
                                            className="border-border/50 text-foreground hover:bg-background/50"
                                            onClick={() => router.push('/templates')}
                                        >
                                            Browse Templates
                                        </Button>
                                    </div>
                                )}
                            </CardContent>
                        </Card>
                    </motion.div>

                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.8, delay: 1.6 }}
                    >
                        <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm rounded-xl">
                            <CardHeader className="pb-4">
                                <CardTitle className="flex items-center gap-3 text-foreground text-xl">
                                    <div className="p-2 bg-[oklch(75.54%_0.1534_231.639,0.1)] rounded-lg">
                                        <Server className="w-5 h-5 text-[oklch(75.54% 0.1534 231.639)]" />
                                    </div>
                                    Services ({groupServices?.length || 0})
                                </CardTitle>
                            </CardHeader>
                            <CardContent>
                                {groupServices && groupServices.length > 0 ? (
                                    <div className="space-y-4">
                                        {groupServices.map((service, idx) => (
                                            <motion.div 
                                                key={idx} 
                                                className="flex items-center justify-between bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] rounded-xl px-6 py-4 border border-[oklch(75.54%_0.1534_231.639,0.3)] hover:bg-gradient-to-r hover:from-[oklch(75.54%_0.1534_231.639,0.15)] hover:to-[oklch(75.54%_0.1534_231.639,0.08)] transition-all duration-200"
                                                whileHover={{ scale: 1.02, y: -2 }}
                                                transition={{ duration: 0.2 }}
                                            >
                                                <div className="flex items-center gap-4">
                                                    <div className={`w-4 h-4 rounded-full ${service.state === 'ONLINE' ? 'bg-green-500' : service.state === 'STARTING' ? 'bg-yellow-500' : 'bg-red-500'}`} />
                                                    <div>
                                                        <div className="text-base font-semibold text-foreground">
                                                            {service.name}
                                                        </div>
                                                        <div className="text-sm text-muted-foreground">
                                                            Group: {group.name}
                                                        </div>
                                                    </div>
                                                </div>
                                                
                                                <div className="flex items-center gap-4">
                                                    <Badge 
                                                        variant="secondary" 
                                                        className={`text-sm px-4 py-2 ${
                                                            service.state === 'ONLINE' ? 'bg-green-500/20 text-green-600 border-green-500/30' :
                                                            service.state === 'STARTING' ? 'bg-yellow-500/20 text-yellow-600 border-yellow-500/30' :
                                                            'bg-red-500/20 text-red-600 border-red-500/30'
                                                        }`}
                                                    >
                                                        {service.state}
                                                    </Badge>
                                                </div>
                                            </motion.div>
                                        ))}
                                    </div>
                                ) : (
                                    <div className="text-center py-12">
                                        <div className="w-20 h-20 bg-[oklch(75.54%_0.1534_231.639,0.1)] rounded-full flex items-center justify-center mx-auto mb-6">
                                            <Server className="w-10 h-10 text-[oklch(75.54% 0.1534 231.639)]" />
                                        </div>
                                        <h3 className="text-lg font-semibold text-foreground mb-2">No Services</h3>
                                        <p className="text-muted-foreground mb-6">No services running in this group</p>
                                        <Button
                                            variant="outline"
                                            className="border-border/50 text-foreground hover:bg-background/50"
                                            onClick={() => router.push('/services')}
                                        >
                                            Browse Services
                                        </Button>
                                    </div>
                                )}
                            </CardContent>
                        </Card>
                    </motion.div>
                </div>
            </div>
            <Dialog open={deleteModalOpen} onOpenChange={setDeleteModalOpen}>
                <DialogContent className="bg-background/95 border border-border/50 backdrop-blur-sm max-w-md">
                    <motion.div
                        initial={{ scale: 0.9, opacity: 0 }}
                        animate={{ scale: 1, opacity: 1 }}
                        transition={{ duration: 0.2 }}
                        className="text-center"
                    >
                        {}
                        <motion.div
                            initial={{ scale: 0, rotate: -180 }}
                            animate={{ scale: 1, rotate: 0 }}
                            transition={{ duration: 0.4, delay: 0.1 }}
                            className="mx-auto w-16 h-16 bg-red-500/20 rounded-full flex items-center justify-center mb-4"
                        >
                            <Trash2 className="w-8 h-8 text-red-500" />
                        </motion.div>

                        {}
                        <motion.h3
                            initial={{ y: 20, opacity: 0 }}
                            animate={{ y: 0, opacity: 1 }}
                            transition={{ duration: 0.3, delay: 0.2 }}
                            className="text-xl font-bold text-foreground mb-2"
                        >
                            Delete Group
                        </motion.h3>

                        {}
                        <motion.p
                            initial={{ y: 20, opacity: 0 }}
                            animate={{ y: 0, opacity: 1 }}
                            transition={{ duration: 0.3, delay: 0.3 }}
                            className="text-muted-foreground mb-6"
                        >
                            Are you sure you want to delete the group &quot;{groupName}&quot;? 
                            <br />
                            <span className="text-red-400 font-medium">This action cannot be undone.</span>
                        </motion.p>

                        {}
                        <motion.div
                            initial={{ y: 20, opacity: 0 }}
                            animate={{ y: 0, opacity: 1 }}
                            transition={{ duration: 0.3, delay: 0.4 }}
                            className="flex gap-3 justify-center"
                        >
                            <Button
                                variant="outline"
                                onClick={() => setDeleteModalOpen(false)}
                                disabled={isDeleting}
                                className="border-border/50 text-foreground hover:bg-background/50 min-w-[100px]"
                            >
                                Cancel
                            </Button>
                            
                            <Button
                                onClick={confirmDelete}
                                disabled={isDeleting}
                                className="bg-red-600 hover:bg-red-700 text-white min-w-[100px] relative overflow-hidden"
                            >
                                {isDeleting ? (
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
        </div>
    );
}

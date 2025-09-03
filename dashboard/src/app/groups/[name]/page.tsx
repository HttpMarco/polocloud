'use client'

import { useState, useEffect, useCallback } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Dialog, DialogContent } from '@/components/ui/dialog';
import { Server, HardDrive, Activity, Settings, Users, Calendar, Trash2, Edit, Cpu } from 'lucide-react';
import { motion } from 'framer-motion';

import { API_ENDPOINTS } from '@/lib/api';
import GlobalNavbar from '@/components/global-navbar';
import { Group } from '@/types/groups';

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
        } catch {}
    }, [groupName]);

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
                alert(errorData.error || 'Failed to delete group');
            }
        } catch {
            alert('Failed to delete group');
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
            {}
            <GlobalNavbar />
            
            <div className="h-2"></div>
            
            {}
            <div className="px-6 py-6">
                <div className="mb-8">
                    <motion.h1 
                        className="text-4xl font-bold text-foreground mb-3"
                        initial={{ opacity: 0, y: -20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.6 }}
                    >
                        {group.name}
                    </motion.h1>
                    <motion.p 
                        className="text-lg text-muted-foreground"
                        initial={{ opacity: 0, y: -10 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.6, delay: 0.1 }}
                    >
                        Detailed overview and configuration for this server group
                    </motion.p>
                </div>
            </div>

            {}
            <div className="px-6 pb-8">
                <motion.div 
                    className="flex gap-3"
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.6, delay: 0.1 }}
                >
                    <Button
                        onClick={() => router.push(`/groups/${groupName}/edit`)}
                        className="h-9 px-4 text-sm font-medium hover:opacity-90 transition-all duration-200 shadow-lg shadow-[0_0_20px_rgba(75.54%,15.34%,231.639,0.3)] hover:shadow-[0_0_30px_rgba(75.54%,15.34%,231.639,0.4)]"
                        style={{ backgroundColor: 'oklch(75.54% .1534 231.639)' }}
                    >
                        <Edit className="w-4 h-4 mr-2" />
                        Edit Group
                    </Button>
                    
                    <Button
                        variant="outline"
                        onClick={handleDeleteGroup}
                        className="h-9 px-4 text-sm border-red-500/30 text-red-600 hover:bg-red-500/10 hover:border-red-500/50 transition-all duration-200"
                    >
                        <Trash2 className="w-4 h-4 mr-2" />
                        Delete Group
                    </Button>
                </motion.div>
            </div>

            {}
            <div className="px-6 pb-8">
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                    
                    {}
                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.6, delay: 0.2 }}
                    >
                        <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm">
                            <CardHeader>
                                <CardTitle className="flex items-center gap-2 text-foreground">
                                    <Server className="w-4 h-4 text-[oklch(75.54% 0.1534 231.639)]" />
                                    Basic Information
                                </CardTitle>
                            </CardHeader>
                            <CardContent className="space-y-4">
                                <div className="grid grid-cols-2 gap-4">
                                                                            <div className="bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] rounded-lg p-3 border border-[oklch(75.54%_0.1534_231.639,0.3)]">
                                            <div className="flex items-center gap-2 mb-2">
                                                <HardDrive className="w-3 h-3 text-muted-foreground" />
                                                <span className="text-xs font-medium text-muted-foreground uppercase tracking-wide">Memory Range</span>
                                            </div>
                                            <div className="text-sm font-semibold text-foreground">
                                                {group.minMemory} - {group.maxMemory} MB
                                            </div>
                                        </div>

                                    <div className="bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] rounded-lg p-3 border border-[oklch(75.54%_0.1534_231.639,0.3)]">
                                        <div className="flex items-center gap-2 mb-2">
                                            <Activity className="w-3 h-3 text-muted-foreground" />
                                            <span className="text-xs font-medium text-muted-foreground uppercase tracking-wide">Service Range</span>
                                        </div>
                                        <div className="text-sm font-semibold text-foreground">
                                            {group.minOnlineService} - {group.maxOnlineService}
                                        </div>
                                    </div>
                                </div>

                                <div className="bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] rounded-lg p-3 border border-[oklch(75.54%_0.1534_231.639,0.3)]">
                                    <div className="flex items-center gap-2 mb-2">
                                        <Cpu className="w-3 h-3 text-muted-foreground" />
                                        <span className="text-xs font-medium text-muted-foreground uppercase tracking-wide">Start Threshold</span>
                                    </div>
                                    <div className="text-sm font-semibold text-foreground">
                                        {group.percentageToStartNewService}%
                                    </div>
                                </div>
                            </CardContent>
                        </Card>
                    </motion.div>

                    {}
                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.6, delay: 0.3 }}
                    >
                        <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm">
                            <CardHeader>
                                <CardTitle className="flex items-center gap-2 text-foreground">
                                    <Settings className="w-4 h-4 text-[oklch(75.54% 0.1534 231.639)]" />
                                    Properties & Settings
                                </CardTitle>
                            </CardHeader>
                            <CardContent className="space-y-4">
                                <div className="grid grid-cols-2 gap-4">
                                    <div className="bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] rounded-lg p-3 border border-[oklch(75.54%_0.1534_231.639,0.3)]">
                                        <div className="flex items-center justify-between">
                                            <span className="text-xs text-muted-foreground">Static Service</span>
                                            <span className={`text-xs font-medium px-2 py-1 rounded-full ${group.properties?.staticService ? 'bg-green-500/20 text-green-600 border border-green-500/30' : 'bg-red-500/20 text-red-600 border border-red-500/30'}`}>
                                                {group.properties?.staticService ? "Yes" : "No"}
                                            </span>
                                        </div>
                                    </div>

                                    <div className="bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] rounded-lg p-3 border border-[oklch(75.54%_0.1534_231.639,0.3)]">
                                        <div className="flex items-center justify-between">
                                            <span className="text-xs text-muted-foreground">Fallback</span>
                                            <span className={`text-xs font-medium px-2 py-1 rounded-full ${group.properties?.fallback ? 'bg-green-500/20 text-green-600 border border-green-500/30' : 'bg-red-500/20 text-red-600 border border-red-500/30'}`}>
                                                {group.properties?.fallback ? "Yes" : "No"}
                                            </span>
                                        </div>
                                    </div>
                                </div>

                                <div className="bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] rounded-lg p-3 border border-[oklch(75.54%_0.1534_231.639,0.3)]">
                                    <div className="flex items-center gap-2 mb-2">
                                        <Calendar className="w-3 h-3 text-muted-foreground" />
                                        <span className="text-xs font-medium text-muted-foreground uppercase tracking-wide">Created</span>
                                    </div>
                                    <div className="text-sm font-semibold text-foreground">
                                        {new Date(group.information.createdAt).toLocaleDateString('de-DE', {
                                            year: 'numeric',
                                            month: 'long',
                                            day: 'numeric',
                                            hour: '2-digit',
                                            minute: '2-digit'
                                        })}
                                    </div>
                                </div>
                            </CardContent>
                        </Card>
                    </motion.div>

                    {}
                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.6, delay: 0.4 }}
                        className="lg:col-span-2"
                    >
                        <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm">
                            <CardHeader>
                                <CardTitle className="flex items-center gap-2 text-foreground">
                                    <Users className="w-4 h-4 text-[oklch(75.54% 0.1534 231.639)]" />
                                    Templates ({group.templates?.length || 0})
                                </CardTitle>
                            </CardHeader>
                            <CardContent>
                                {group.templates && group.templates.length > 0 ? (
                                    <div className="flex flex-wrap gap-2">
                                        {group.templates.map((template, idx) => (
                                            <Badge 
                                                key={idx} 
                                                variant="secondary" 
                                                className="text-xs px-2 py-1 text-muted-foreground bg-background/50 border border-border/30 hover:bg-background/70 transition-colors duration-200 cursor-pointer"
                                                onClick={() => router.push(`/templates/${template.name}`)}
                                            >
                                                {template.name}
                                            </Badge>
                                        ))}
                                    </div>
                                ) : (
                                    <div className="text-center py-8">
                                        <Users className="w-16 h-16 text-muted-foreground mx-auto opacity-50 mb-4" />
                                        <p className="text-muted-foreground">No templates configured for this group</p>
                                        <Button
                                            variant="outline"
                                            className="mt-4 border-border/50 text-foreground hover:bg-background/50"
                                            onClick={() => router.push('/templates')}
                                        >
                                            Browse Templates
                                        </Button>
                                    </div>
                                )}
                            </CardContent>
                        </Card>
                    </motion.div>

                    {}
                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.6, delay: 0.5 }}
                        className="lg:col-span-2"
                    >
                        <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm">
                            <CardHeader>
                                <CardTitle className="flex items-center gap-2 text-foreground">
                                    <Server className="w-4 h-4 text-[oklch(75.54% 0.1534 231.639)]" />
                                    Services ({groupServices?.length || 0})
                                </CardTitle>
                            </CardHeader>
                            <CardContent>
                                {groupServices && groupServices.length > 0 ? (
                                    <div className="space-y-3">
                                        {groupServices.map((service, idx) => (
                                            <div key={idx} className="flex items-center justify-between bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] rounded-lg px-4 py-3 border border-[oklch(75.54%_0.1534_231.639,0.3)] hover:bg-gradient-to-r hover:from-[oklch(75.54%_0.1534_231.639,0.15)] hover:to-[oklch(75.54%_0.1534_231.639,0.08)] transition-all duration-200">
                                                <div className="flex items-center gap-3">
                                                    <div className={`w-3 h-3 rounded-full ${service.state === 'ONLINE' ? 'bg-green-500' : service.state === 'STARTING' ? 'bg-yellow-500' : 'bg-red-500'}`} />
                                                    <div>
                                                        <div className="text-sm font-medium text-foreground">
                                                            {service.name}
                                                        </div>
                                                        <div className="text-xs text-muted-foreground">
                                                            Group: {group.name}
                                                        </div>
                                                    </div>
                                                </div>
                                                
                                                <div className="flex items-center gap-3">
                                                    <Badge 
                                                        variant="secondary" 
                                                        className={`text-xs px-3 py-1 h-6 ${
                                                            service.state === 'ONLINE' ? 'bg-green-500/20 text-green-600 border-green-500/30' :
                                                            service.state === 'STARTING' ? 'bg-yellow-500/20 text-yellow-600 border-yellow-500/30' :
                                                            'bg-red-500/20 text-red-600 border-red-500/30'
                                                        }`}
                                                    >
                                                        {service.state}
                                                    </Badge>
                                                    
                                                    <Button
                                                        variant="outline"
                                                        size="sm"
                                                        className="h-8 px-3 text-xs border-border/50 text-foreground hover:bg-background/50 hover:border-border transition-all duration-200"
                                                        onClick={() => router.push('/services')}
                                                    >
                                                        View
                                                    </Button>
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                ) : (
                                    <div className="text-center py-8">
                                        <Server className="w-16 h-16 text-muted-foreground mx-auto opacity-50 mb-4" />
                                        <p className="text-muted-foreground">No services running in this group</p>
                                        <Button
                                            variant="outline"
                                            className="mt-4 border-border/50 text-foreground hover:bg-background/50"
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

            {}
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

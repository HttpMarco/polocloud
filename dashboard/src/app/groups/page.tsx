'use client'

import { useState, useEffect, useMemo, useCallback } from 'react';
import { useRouter } from 'next/navigation';
import { Button } from '@/components/ui/button';
import { API_ENDPOINTS } from '@/lib/api'
import { motion } from 'framer-motion';

import { GroupCard } from '@/components/groups/group-card';
import { GroupStats } from '@/components/groups/group-stats';
import { GroupFilters } from '@/components/groups/group-filters';
import { GroupHeader } from '@/components/groups/group-header';
import { GroupEmptyState } from '@/components/groups/group-empty-state';
import GlobalNavbar from '@/components/global-navbar';
import { Group } from '@/types/groups';

export default function GroupsPage() {
    const router = useRouter();
    const [groups, setGroups] = useState<Group[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedPlatform, setSelectedPlatform] = useState<string>('all');

    const withLoading = async (fn: () => Promise<void>) => {
        try {
            setIsLoading(true);
            await fn();
        } catch {
        } finally {
            setIsLoading(false);
        }
    };

    const loadGroups = useCallback(async () => {
        await withLoading(async () => {
            const response = await fetch(API_ENDPOINTS.GROUPS.LIST);
            const data = await response.json();
            
            if (response.ok && Array.isArray(data)) {
                setGroups(data);
            } else if (response.status === 400 && data.message === 'No groups found') {
                setGroups([]);
            }
        });
    }, []);

    useEffect(() => {
        loadGroups();
    }, [loadGroups]);



    const getPlatformName = (group: Group): string => {
        if (typeof group.platform === 'string') {
            return group.platform;
        } else if (group.platform && typeof group.platform === 'object' && 'name' in group.platform) {
            return group.platform.name;
        }
        return 'default';
    };

    const filteredGroups = useMemo(() => {
        return groups.filter(group => {
            const platformName = getPlatformName(group);
            const matchesSearch = group.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                platformName.toLowerCase().includes(searchTerm.toLowerCase());
            const matchesPlatform = selectedPlatform === 'all' || platformName === selectedPlatform;

            return matchesSearch && matchesPlatform;
        });
    }, [groups, searchTerm, selectedPlatform]);

    const availablePlatforms = useMemo(() => {
        const platformNames = Array.from(new Set(groups.map(group => getPlatformName(group))));
        return platformNames.sort();
    }, [groups]);

    const stats = useMemo(() => {
        const totalGroups = groups.length;
        const totalMemory = groups.reduce((sum, g) => sum + g.maxMemory, 0);
        const totalServices = groups.reduce((sum, g) => sum + g.maxOnlineService, 0);
        const totalTemplates = groups.reduce((sum, g) => sum + (g.templates?.length || 0), 0);

        return {
            total: totalGroups,
            memory: Math.round(totalMemory),
            services: totalServices,
            templates: totalTemplates
        };
    }, [groups]);

    if (isLoading) {
        return (
            <div className="w-full h-full flex items-center justify-center">
                <div className="w-8 h-8 border-2 border-primary border-t-transparent rounded-full animate-spin"></div>
            </div>
        );
    }


    return (
        <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20 ultra-smooth-scroll">
            {}
            <GlobalNavbar />
            
            <div className="h-2"></div>
            
            {}
            <GroupHeader />

            {}
            <div className="px-6 pb-8">
                <GroupStats stats={stats} />
            </div>

            {}
            <div className="px-6 pb-6">
                <GroupFilters
                    searchTerm={searchTerm}
                    setSearchTerm={setSearchTerm}
                    selectedPlatform={selectedPlatform}
                    setSelectedPlatform={setSelectedPlatform}
                    availablePlatforms={availablePlatforms}
                    onCreateClick={() => router.push('/groups/create')}
                />
            </div>

            {}
            <div className="flex-1 px-6 pb-6 overflow-auto modern-scrollbar">
                {filteredGroups.length > 0 ? (
                    <motion.div 
                        className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5 gap-6 justify-items-center"
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        transition={{ duration: 0.6, delay: 0.4 }}
                    >
                        {filteredGroups.map((group, index) => (
                            <GroupCard
                                key={group.name}
                                group={group}
                                index={index}
                                onClick={() => router.push(`/groups/${group.name}`)}
                            />
                        ))}
                    </motion.div>
                ) : (
                    <GroupEmptyState
                        searchTerm={searchTerm}
                        selectedPlatform={selectedPlatform}
                        onClearFilters={() => {
                                        setSearchTerm('');
                                        setSelectedPlatform('all');
                                    }}
                    />
                )}
            </div>
        </div>
    );
}

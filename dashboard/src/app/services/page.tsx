'use client'

import { useState, useEffect, useMemo } from 'react';

import { Button } from '@/components/ui/button';
import { Service } from '@/types/services';
import { motion } from 'framer-motion';
import { API_ENDPOINTS } from '@/lib/api';
import GlobalNavbar from '@/components/global-navbar';
import { useWebSocketSystem } from '@/hooks/useWebSocketSystem';
import { ServiceCard } from '@/components/services/service-card';
import { ServiceStats } from '@/components/services/service-stats';
import { ServiceFilters } from '@/components/services/service-filters';
import { ServiceHeader } from '@/components/services/service-header';
import { ServiceEmptyState } from '@/components/services/service-empty-state';
import { toast } from 'sonner';

export default function ServicesPage() {
    const [services, setServices] = useState<Service[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedGroup, setSelectedGroup] = useState<string>('all');
    const [selectedType, setSelectedType] = useState<string>('all');
    const [restartingServices, setRestartingServices] = useState<string[]>([]);

    useWebSocketSystem({
        path: '/services/update',
        autoConnect: true,
        onMessage: (message) => {
            try {
                let updateData;
                
                if (typeof message.data === 'string') {
                    try {
                        updateData = JSON.parse(message.data);
                    } catch {
                        return;
                    }
                } else if (message.data && typeof message.data === 'object') {
                    updateData = message.data;
                } else if (message && message.serviceName) {
                    updateData = message;
                } else {
                    return;
                }

                if (updateData && updateData.serviceName && updateData.state) {

                    setServices(prev => prev.map(service => 
                        service.name === updateData.serviceName 
                            ? { 
                                ...service, 
                                state: updateData.state,

                                ...(updateData.state === 'STARTING' || updateData.state === 'PREPARING' ? {
                                    playerCount: -1,
                                    maxPlayerCount: -1,
                                    cpuUsage: -1,
                                    memoryUsage: -1,
                                    maxMemory: -1
                                } : {}),

                                ...(updateData.state === 'STOPPING' || updateData.state === 'STOPPED' ? {
                                    playerCount: 0,
                                    maxPlayerCount: 0,
                                    cpuUsage: 0,
                                    memoryUsage: 0,
                                    maxMemory: 0
                                } : {})
                            }
                            : service
                    ));

                    // Reload services when state changes to get updated stats
                    if (updateData.state === 'ONLINE') {
                        // Wait a bit longer for ONLINE to ensure stats are updated
                        setTimeout(() => {
                            loadServices();
                        }, 2000);
                    } else if (updateData.state === 'OFFLINE' || updateData.state === 'STOPPED') {
                        // Quick reload for offline states
                        setTimeout(() => {
                            loadServices();
                        }, 500);
                    }
                } else {
                }
            } catch {
            }
        }
    });

    

    useEffect(() => {
        loadServices();
    }, []);

    const loadServices = async () => {
        try {
            setIsLoading(true);
            setError(null);
            
            const response = await fetch(API_ENDPOINTS.SERVICES.LIST);
            if (response.ok) {
                const data = await response.json();
                
                if (Array.isArray(data)) {
                    setServices(data);
                } else {

                    setError('Invalid response format from server');
                }
            } else {
                const errorData = await response.json();
                setError(errorData.error || 'Failed to load services');
            }
        } catch {
            setError('Failed to load services');
        } finally {
            setIsLoading(false);
        }
    };

    const handleRestartService = async (serviceName: string) => {
        if (restartingServices.includes(serviceName)) return;

        setRestartingServices(prev => [...prev, serviceName]);

        try {
            const response = await fetch(API_ENDPOINTS.SERVICES.RESTART(serviceName), {
                method: 'PATCH'
            });

            if (response.ok) {


            } else {
                const errorData = await response.json();
                toast.error(errorData.error || 'Failed to restart service');
            }
        } catch {
            toast.error('Failed to restart service');
        } finally {
            setRestartingServices(prev => prev.filter(name => name !== serviceName));
        }
    };

    const filteredServices = useMemo(() => {
        return services.filter(service => {
            const matchesSearch = service.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                service.groupName.toLowerCase().includes(searchTerm.toLowerCase());
            const matchesGroup = selectedGroup === 'all' || service.groupName === selectedGroup;
            const matchesType = selectedType === 'all' || service.type === selectedType;

            return matchesSearch && matchesGroup && matchesType;
        });
    }, [services, searchTerm, selectedGroup, selectedType]);

    const availableGroups = useMemo(() => {
        const groups = Array.from(new Set(services.map(service => service.groupName)));
        return groups.sort();
    }, [services]);

    const availableTypes = useMemo(() => {
        const types = Array.from(new Set(services.map(service => service.type)));
        return types.sort();
    }, [services]);

    const stats = useMemo(() => {
        const totalServices = services.length;
        const onlineServices = services.filter(s => s.state === 'ONLINE').length;
        const totalPlayers = services.reduce((sum, s) => sum + s.playerCount, 0);
        const totalMemory = services.reduce((sum, s) => sum + s.memoryUsage, 0);

        return {
            total: totalServices,
            online: onlineServices,
            players: totalPlayers,
            memory: Math.round(totalMemory)
        };
    }, [services]);

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
                    <Button onClick={loadServices}>Retry</Button>
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
            <ServiceHeader />

            {}
            <div className="px-6 pb-8">
                <ServiceStats stats={stats} />
            </div>

            {}
            <div className="px-6 pb-6">
                <ServiceFilters
                    searchTerm={searchTerm}
                    setSearchTerm={setSearchTerm}
                    selectedGroup={selectedGroup}
                    setSelectedGroup={setSelectedGroup}
                    selectedType={selectedType}
                    setSelectedType={setSelectedType}
                    availableGroups={availableGroups}
                    availableTypes={availableTypes}
                />
            </div>

            {}
            <div className="flex-1 px-6 pb-6 overflow-auto modern-scrollbar">
                {filteredServices.length > 0 ? (
                    <motion.div 
                        className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5 gap-6 justify-items-center"
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        transition={{ duration: 0.6, delay: 0.4 }}
                    >
                        {filteredServices.map((service, index) => (
                            <ServiceCard
                                key={service.name}
                                service={service}
                                index={index}
                                restartingServices={restartingServices}
                                onRestart={handleRestartService}
                            />
                        ))}
                    </motion.div>
                ) : (
                    <ServiceEmptyState
                        searchTerm={searchTerm}
                        selectedGroup={selectedGroup}
                        selectedType={selectedType}
                        onClearFilters={() => {
                                        setSearchTerm('');
                                        setSelectedGroup('all');
                                        setSelectedType('all');
                                    }}
                    />
                )}
            </div>
        </div>
    );
}

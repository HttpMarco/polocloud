'use client'

import { useState, useEffect, useMemo } from 'react';

import { Button } from '@/components/ui/button';
import { Service } from '@/types/services';
import { motion } from 'framer-motion';
import { API_ENDPOINTS } from '@/lib/api';
import GlobalNavbar from '@/components/global-navbar';
import { useSidebarData } from '@/components/sidebar-data-provider';
import { ServiceCard } from '@/components/services/service-card';
import { ServiceStats } from '@/components/services/service-stats';
import { ServiceFilters } from '@/components/services/service-filters';
import { ServiceHeader } from '@/components/services/service-header';
import { ServiceEmptyState } from '@/components/services/service-empty-state';
import { toast } from 'sonner';

export default function ServicesPage() {
    const { services: sidebarServices, isLoading: sidebarLoading } = useSidebarData();
    const [services, setServices] = useState<Service[]>(sidebarServices);
    const [isLoading, setIsLoading] = useState(sidebarLoading);
    const [error, setError] = useState<string | null>(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedGroup, setSelectedGroup] = useState<string>('all');
    const [selectedType, setSelectedType] = useState<string>('all');
    const [restartingServices, setRestartingServices] = useState<string[]>([]);

    // Use shared services data from SidebarDataProvider
    useEffect(() => {
        setServices(sidebarServices);
        setIsLoading(sidebarLoading);
    }, [sidebarServices, sidebarLoading]);

    // Listen for service state updates to show toasts
    useEffect(() => {
        const handleServiceStateUpdate = (event: CustomEvent) => {
            const { serviceName, state } = event.detail;
            if (state === 'ONLINE') {
                toast.success(`Service ${serviceName} is now online`);
            }
        };

        window.addEventListener('serviceStateUpdate', handleServiceStateUpdate as EventListener);
        
        return () => {
            window.removeEventListener('serviceStateUpdate', handleServiceStateUpdate as EventListener);
        };
    }, []);

    // Debug info for WebSocket status
    useEffect(() => {
        const debugElement = document.getElementById('websocket-debug-services');
        if (debugElement) {
            debugElement.textContent = `Services WebSocket: INITIALIZING...`;
            debugElement.dataset.status = 'initializing';
        }

        // Listen for WebSocket connection events
        const handleWebSocketConnect = () => {
            const debugElement = document.getElementById('websocket-debug-services');
            if (debugElement) {
                debugElement.dataset.status = 'connected';
                debugElement.dataset.lastConnect = Date.now().toString();
                debugElement.textContent = `Services WebSocket: CONNECTED at ${new Date().toLocaleTimeString()}`;
                debugElement.className = 'text-xs font-mono p-2 bg-green-100 dark:bg-green-900/20 text-green-800 dark:text-green-200 rounded border border-green-300 dark:border-green-700';
            }
        };

        const handleWebSocketDisconnect = () => {
            const debugElement = document.getElementById('websocket-debug-services');
            if (debugElement) {
                debugElement.dataset.status = 'disconnected';
                debugElement.dataset.lastDisconnect = Date.now().toString();
                debugElement.textContent = `Services WebSocket: DISCONNECTED at ${new Date().toLocaleTimeString()}`;
                debugElement.className = 'text-xs font-mono p-2 bg-red-100 dark:bg-red-900/20 text-red-800 dark:text-red-200 rounded border border-red-300 dark:border-red-700';
            }
        };

        const handleWebSocketError = (event: CustomEvent) => {
            const debugElement = document.getElementById('websocket-debug-services');
            if (debugElement) {
                debugElement.dataset.status = 'error';
                debugElement.dataset.lastError = Date.now().toString();
                debugElement.textContent = `Services WebSocket: ERROR - ${event.detail?.message || 'Unknown error'}`;
                debugElement.className = 'text-xs font-mono p-2 bg-yellow-100 dark:bg-yellow-900/20 text-yellow-800 dark:text-yellow-200 rounded border border-yellow-300 dark:border-yellow-700';
            }
        };

        const handleServiceStateUpdate = (event: CustomEvent) => {
            const { serviceName, state } = event.detail;
            
            // Update service state debug
            const serviceDebugElement = document.getElementById('service-state-debug');
            if (serviceDebugElement) {
                serviceDebugElement.textContent = `Service State Change: ${serviceName} -> ${state} at ${new Date().toLocaleTimeString()}`;
                serviceDebugElement.dataset.lastService = serviceName;
                serviceDebugElement.dataset.lastState = state;
                serviceDebugElement.dataset.lastChange = Date.now().toString();
                serviceDebugElement.className = 'text-xs font-mono p-2 bg-blue-100 dark:bg-blue-900/20 text-blue-800 dark:text-blue-200 rounded border border-blue-300 dark:border-blue-700';
            }

            // Update last update time
            const lastUpdateElement = document.getElementById('last-update-time');
            if (lastUpdateElement) {
                lastUpdateElement.textContent = new Date().toLocaleTimeString();
            }
        };

        // Add event listeners
        window.addEventListener('websocketConnect', handleWebSocketConnect as EventListener);
        window.addEventListener('websocketDisconnect', handleWebSocketDisconnect as EventListener);
        window.addEventListener('websocketError', handleWebSocketError as EventListener);
        window.addEventListener('serviceStateUpdate', handleServiceStateUpdate as EventListener);

        return () => {
            window.removeEventListener('websocketConnect', handleWebSocketConnect as EventListener);
            window.removeEventListener('websocketDisconnect', handleWebSocketDisconnect as EventListener);
            window.removeEventListener('websocketError', handleWebSocketError as EventListener);
            window.removeEventListener('serviceStateUpdate', handleServiceStateUpdate as EventListener);
        };
    }, []);

    

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
            
            {/* Debug Information Panel */}
            <div className="px-6 pb-4">
                <div className="bg-card border border-border rounded-lg p-4 space-y-3">
                    <h3 className="text-sm font-semibold text-foreground">WebSocket Debug Information</h3>
                    
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        {/* WebSocket Status */}
                        <div className="space-y-2">
                            <div 
                                id="websocket-debug-services"
                                className="text-xs font-mono p-2 bg-muted rounded border"
                                data-status="disconnected"
                            >
                                Services WebSocket: DISCONNECTED
                            </div>
                        </div>
                        
                        {/* Service State Changes */}
                        <div className="space-y-2">
                            <div 
                                id="service-state-debug"
                                className="text-xs font-mono p-2 bg-muted rounded border"
                                data-last-service=""
                                data-last-state=""
                            >
                                Service State Changes: None yet
                            </div>
                        </div>
                    </div>
                    
                    {/* Connection Info */}
                    <div className="text-xs text-muted-foreground">
                        <div>Last Update: <span id="last-update-time">Never</span></div>
                        <div>Total Services: {services.length}</div>
                        <div>Online Services: {services.filter(s => s.state === 'ONLINE').length}</div>
                    </div>
                </div>
            </div>
            
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

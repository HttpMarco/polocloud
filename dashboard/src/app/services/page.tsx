'use client'

import { useState, useEffect, useMemo } from 'react';

import { Button } from '@/components/ui/button';
import { Service } from '@/types/services';
import { motion } from 'framer-motion';
import { API_ENDPOINTS } from '@/lib/api';
import GlobalNavbar from '@/components/global-navbar';
import { useSidebarData } from '@/components/sidebar-data-provider';
import { useWebSocketSystem } from '@/hooks/useWebSocketSystem';
import { ServiceCard } from '@/components/services/service-card';
import { ServiceStats } from '@/components/services/service-stats';
import { ServiceFilters } from '@/components/services/service-filters';
import { ServiceHeader } from '@/components/services/service-header';
import { ServiceEmptyState } from '@/components/services/service-empty-state';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Wifi, WifiOff, Clock, Activity } from 'lucide-react';

export default function ServicesPage() {
    const { services: sidebarServices, isLoading: sidebarLoading } = useSidebarData();
    const [services, setServices] = useState<Service[]>(sidebarServices);
    const [isLoading, setIsLoading] = useState(sidebarLoading);
    const [error, setError] = useState<string | null>(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedGroup, setSelectedGroup] = useState<string>('all');
    const [selectedType, setSelectedType] = useState<string>('all');
    const [restartingServices, setRestartingServices] = useState<string[]>([]);
    
    // Debug state
    const [showDebugInfo, setShowDebugInfo] = useState(false);
    const [websocketStatus, setWebsocketStatus] = useState<string>('DISCONNECTED');
    const [lastPing, setLastPing] = useState<Date | null>(null);
    const [pingInterval, setPingInterval] = useState<NodeJS.Timeout | null>(null);
    const [reconnectInterval, setReconnectInterval] = useState<NodeJS.Timeout | null>(null);
    const [serviceStateChanges, setServiceStateChanges] = useState<Array<{serviceName: string, state: string, timestamp: Date}>>([]);
    const [lastUpdate, setLastUpdate] = useState<Date | null>(null);
    const [connectionAttempts, setConnectionAttempts] = useState(0);
    const [lastError, setLastError] = useState<string | null>(null);
    
    // Direct WebSocket connection for services page
    const [backendCredentials, setBackendCredentials] = useState<{backendIp: string | null, token: string | null}>({
        backendIp: null,
        token: null
    });

    // Use shared services data from SidebarDataProvider
    useEffect(() => {
        setServices(sidebarServices);
        setIsLoading(sidebarLoading);
    }, [sidebarServices, sidebarLoading]);
    
    // Load credentials for direct WebSocket
    useEffect(() => {
        const loadCredentials = async () => {
            const backendIp = localStorage.getItem('backendIp');
            
            try {
                const response = await fetch('/api/auth/token');
                if (response.ok) {
                    const data = await response.json();
                    if (data.success && data.token) {
                        setBackendCredentials({ backendIp, token: data.token });
                        return;
                    }
                }
            } catch {
                // Silent fallback
            }
            
            // Fallback to cookie parsing
            let token = localStorage.getItem('token');
            if (!token) {
                const cookies = document.cookie.split(';');
                for (const cookie of cookies) {
                    const [name, value] = cookie.trim().split('=');
                    if (name === 'token') {
                        token = value;
                        break;
                    }
                }
            }
            
            if (!token) {
                const tokenMatch = document.cookie.match(/token=([^;]+)/);
                if (tokenMatch) {
                    token = tokenMatch[1];
                }
            }
            
            setBackendCredentials({ backendIp, token: token || null });
        };
        
        loadCredentials();
    }, []);
    
    // Direct WebSocket connection
    const shouldConnect = !!backendCredentials.backendIp && !!backendCredentials.token;
    
    const { connect, disconnect, sendMessage } = useWebSocketSystem({
        backendIp: backendCredentials.backendIp || undefined,
        token: backendCredentials.token || undefined,
        path: '/services/update',
        autoConnect: false,
        onConnect: () => {
            setWebsocketStatus('CONNECTED');
            setLastUpdate(new Date());
            setConnectionAttempts(0);
            setLastError(null);
            window.dispatchEvent(new CustomEvent('websocketConnect'));
            
            // Clear any existing reconnect interval
            if (reconnectInterval) {
                clearInterval(reconnectInterval);
                setReconnectInterval(null);
            }
            
            // Start ping interval
            const interval = setInterval(() => {
                sendMessage({ type: 'heartbeat', data: { timestamp: Date.now() } });
                setLastPing(new Date());
            }, 30000); // Ping every 30 seconds
            setPingInterval(interval);
        },
        onDisconnect: () => {
            setWebsocketStatus('DISCONNECTED');
            window.dispatchEvent(new CustomEvent('websocketDisconnect'));
            
            // Clear ping interval
            if (pingInterval) {
                clearInterval(pingInterval);
                setPingInterval(null);
            }
            
            // Start reconnection attempts
            if (shouldConnect && connectionAttempts < 5) {
                const reconnectTimer = setTimeout(() => {
                    setConnectionAttempts(prev => prev + 1);
                    setWebsocketStatus('RECONNECTING');
                    connect().catch(() => {
                        setWebsocketStatus('ERROR');
                    });
                }, 5000); // Try to reconnect after 5 seconds
                setReconnectInterval(reconnectTimer);
            }
        },
        onError: (error) => {
            setWebsocketStatus('ERROR');
            setLastError(error.message);
            window.dispatchEvent(new CustomEvent('websocketError', {
                detail: { message: error.message }
            }));
            
            // Start reconnection attempts on error
            if (shouldConnect && connectionAttempts < 5) {
                const reconnectTimer = setTimeout(() => {
                    setConnectionAttempts(prev => prev + 1);
                    setWebsocketStatus('RECONNECTING');
                    connect().catch(() => {
                        setWebsocketStatus('ERROR');
                    });
                }, 5000);
                setReconnectInterval(reconnectTimer);
            }
        },
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
                } else {
                    updateData = message;
                }
                
                if (updateData && updateData.serviceName && updateData.state) {
                    // Track state changes for debug
                    setServiceStateChanges(prev => [
                        { serviceName: updateData.serviceName, state: updateData.state, timestamp: new Date() },
                        ...prev.slice(0, 9) // Keep last 10 changes
                    ]);
                    setLastUpdate(new Date());
                    
                    // Update local state
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
                                } : {}),
                                ...(updateData || {})
                            }
                            : service
                    ));
                    
                    // Dispatch custom event
                    window.dispatchEvent(new CustomEvent('serviceStateUpdate', {
                        detail: { serviceName: updateData.serviceName, state: updateData.state, updateData }
                    }));
                }
            } catch {
                // Silent error handling
            }
        }
    });

    // Manually connect when credentials are ready
    useEffect(() => {
        if (shouldConnect) {
            setWebsocketStatus('CONNECTING');
            setConnectionAttempts(0);
            connect().catch(() => {
                setWebsocketStatus('ERROR');
            });
        }
    }, [shouldConnect, connect]);

    // Cleanup intervals on unmount
    useEffect(() => {
        return () => {
            if (pingInterval) {
                clearInterval(pingInterval);
            }
            if (reconnectInterval) {
                clearTimeout(reconnectInterval);
            }
        };
    }, [pingInterval, reconnectInterval]);

    // Force refresh services data periodically
    useEffect(() => {
        const refreshInterval = setInterval(() => {
            if (websocketStatus === 'CONNECTED') {
                // Refresh services data every 2 minutes to ensure we have latest state
                loadServices();
            }
        }, 120000); // 2 minutes

        return () => clearInterval(refreshInterval);
    }, [websocketStatus]);


    

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
                // Force refresh services after restart to ensure we get the latest state
                setTimeout(() => {
                    loadServices();
                }, 2000); // Wait 2 seconds for restart to begin
                
                // Also refresh again after 10 seconds to catch the final state
                setTimeout(() => {
                    loadServices();
                }, 10000);
            } else {
                // Silent error handling
            }
        } catch {
            // Silent error handling
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
            
            {/* Debug Toggle Button */}
            <div className="px-6 pb-2">
                <Button
                    variant="outline"
                    size="sm"
                    onClick={() => setShowDebugInfo(!showDebugInfo)}
                    className="text-xs"
                >
                    {showDebugInfo ? 'Hide' : 'Show'} WebSocket Debug
                </Button>
            </div>
            
            {/* Debug Information Panel */}
            {showDebugInfo && (
                <div className="px-6 pb-4">
                    <Card className="bg-card border-border">
                        <CardHeader className="pb-3">
                            <CardTitle className="text-sm flex items-center gap-2">
                                <Activity className="w-4 h-4" />
                                WebSocket Debug Information
                            </CardTitle>
                        </CardHeader>
                        <CardContent className="space-y-3">
                            <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-xs">
                                <div>
                                    <div className="text-muted-foreground">Services WebSocket:</div>
                                    <Badge 
                                        variant={websocketStatus === 'CONNECTED' ? 'default' : websocketStatus === 'CONNECTING' || websocketStatus === 'RECONNECTING' ? 'secondary' : 'destructive'}
                                        className="text-xs"
                                    >
                                        {websocketStatus === 'CONNECTED' && <Wifi className="w-3 h-3 mr-1" />}
                                        {(websocketStatus === 'DISCONNECTED' || websocketStatus === 'ERROR') && <WifiOff className="w-3 h-3 mr-1" />}
                                        {websocketStatus}
                                    </Badge>
                                </div>
                                <div>
                                    <div className="text-muted-foreground">Service State Changes:</div>
                                    <div className="font-mono text-xs">{serviceStateChanges.length}</div>
                                </div>
                                <div>
                                    <div className="text-muted-foreground">Last Update:</div>
                                    <div className="font-mono text-xs">
                                        {lastUpdate ? lastUpdate.toLocaleTimeString() : 'Never'}
                                    </div>
                                </div>
                                <div>
                                    <div className="text-muted-foreground">Last Ping:</div>
                                    <div className="font-mono text-xs">
                                        {lastPing ? lastPing.toLocaleTimeString() : 'Never'}
                                    </div>
                                </div>
                            </div>
                            
                            <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-xs">
                                <div>
                                    <div className="text-muted-foreground">Connection Attempts:</div>
                                    <div className="font-mono text-xs">{connectionAttempts}/5</div>
                                </div>
                                <div>
                                    <div className="text-muted-foreground">Last Error:</div>
                                    <div className="font-mono text-xs text-red-500 truncate">
                                        {lastError || 'None'}
                                    </div>
                                </div>
                                <div>
                                    <div className="text-muted-foreground">Auto Refresh:</div>
                                    <div className="font-mono text-xs">Every 2min</div>
                                </div>
                                <div>
                                    <div className="text-muted-foreground">Heartbeat:</div>
                                    <div className="font-mono text-xs">Every 30s</div>
                                </div>
                            </div>
                            
                            <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-xs">
                                <div>
                                    <div className="text-muted-foreground">Total Services:</div>
                                    <div className="font-mono">{services.length}</div>
                                </div>
                                <div>
                                    <div className="text-muted-foreground">Online Services:</div>
                                    <div className="font-mono">{services.filter(s => s.state === 'ONLINE').length}</div>
                                </div>
                                <div>
                                    <div className="text-muted-foreground">Backend IP:</div>
                                    <div className="font-mono text-xs">{backendCredentials.backendIp || 'Missing'}</div>
                                </div>
                                <div>
                                    <div className="text-muted-foreground">Token:</div>
                                    <div className="font-mono text-xs">
                                        {backendCredentials.token ? 'Present' : 'Missing'}
                                    </div>
                                </div>
                            </div>
                            
                            {serviceStateChanges.length > 0 && (
                                <div>
                                    <div className="text-muted-foreground text-xs mb-2">Recent State Changes:</div>
                                    <div className="space-y-1 max-h-32 overflow-y-auto">
                                        {serviceStateChanges.slice(0, 5).map((change, index) => (
                                            <div key={index} className="flex justify-between text-xs font-mono">
                                                <span>{change.serviceName}</span>
                                                <Badge variant="outline" className="text-xs">
                                                    {change.state}
                                                </Badge>
                                                <span className="text-muted-foreground">
                                                    {change.timestamp.toLocaleTimeString()}
                                                </span>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            )}
                            
                            <div className="flex gap-2 pt-2">
                                <Button
                                    variant="outline"
                                    size="sm"
                                    onClick={() => {
                                        if (websocketStatus === 'CONNECTED') {
                                            disconnect();
                                        } else {
                                            connect();
                                        }
                                    }}
                                    className="text-xs"
                                >
                                    {websocketStatus === 'CONNECTED' ? 'Disconnect' : 'Connect'}
                                </Button>
                                <Button
                                    variant="outline"
                                    size="sm"
                                    onClick={() => {
                                        sendMessage({ type: 'heartbeat', data: { timestamp: Date.now() } });
                                        setLastPing(new Date());
                                    }}
                                    className="text-xs"
                                    disabled={websocketStatus !== 'CONNECTED'}
                                >
                                    <Clock className="w-3 h-3 mr-1" />
                                    Send Ping
                                </Button>
                                <Button
                                    variant="outline"
                                    size="sm"
                                    onClick={() => {
                                        loadServices();
                                        setLastUpdate(new Date());
                                    }}
                                    className="text-xs"
                                >
                                    <Activity className="w-3 h-3 mr-1" />
                                    Refresh Services
                                </Button>
                            </div>
                        </CardContent>
                    </Card>
                </div>
            )}
            
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

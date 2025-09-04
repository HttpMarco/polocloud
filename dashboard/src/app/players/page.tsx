'use client'

import { useState, useEffect, useMemo, useCallback } from 'react';
import Image from 'next/image';
import { useRouter } from 'next/navigation';
import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Badge } from '@/components/ui/badge';

import {
    Users,
    Search,
    ChevronLeft,
    ChevronRight,
    Activity,
    Globe
} from 'lucide-react';

import { Player, PlayerListResponse } from '@/types/players';
import { motion } from 'framer-motion';

import { API_ENDPOINTS } from '@/lib/api';
import GlobalNavbar from '@/components/global-navbar';

export default function PlayersPage() {
    const router = useRouter();
    const [players, setPlayers] = useState<Player[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [currentPage, setCurrentPage] = useState(1);
    const [pageSize] = useState(20);
    const [totalPlayers, setTotalPlayers] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    const loadPlayers = useCallback(async () => {
        try {
            setIsLoading(true);
            setError(null);
            
            const response = await fetch(API_ENDPOINTS.PLAYERS.LIST_WITH_PAGINATION(currentPage, pageSize));
            if (response.ok) {
                const data: PlayerListResponse = await response.json();

                setPlayers(data.data);
                setTotalPlayers(data.total);
                setTotalPages(data.totalPages);
            } else {
                const errorData = await response.json();
                setError(errorData.error || 'Failed to load players');
            }
        } catch {
            setError('Failed to load players');
        } finally {
            setIsLoading(false);
        }
    }, [currentPage, pageSize]);

    useEffect(() => {
        loadPlayers();
    }, [loadPlayers]);

    const filteredPlayers = useMemo(() => {
        return players.filter(player => 
            player.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
            player.currentServiceName.toLowerCase().includes(searchTerm.toLowerCase())
        );
    }, [players, searchTerm]);

    const stats = useMemo(() => {
        const onlinePlayers = players.filter(p => p.currentServiceName && p.currentServiceName !== 'null').length;
        const offlinePlayers = players.filter(p => !p.currentServiceName || p.currentServiceName === 'null').length;

        return {
            total: totalPlayers,
            online: onlinePlayers,
            offline: offlinePlayers
        };
    }, [players, totalPlayers]);

    const handlePageChange = (newPage: number) => {
        if (newPage >= 1 && newPage <= totalPages) {
            setCurrentPage(newPage);
        }
    };



    if (isLoading) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20 ultra-smooth-scroll">
                <GlobalNavbar />
                <div className="flex items-center justify-center min-h-[400px]">
                    <div className="text-center">
                        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-[oklch(0.7554_0.1534_231.639)] mx-auto mb-4"></div>
                        <p className="text-muted-foreground">Loading players...</p>
                    </div>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20 ultra-smooth-scroll">
                <GlobalNavbar />
                <div className="text-center py-12">
                    <Users className="w-16 h-16 text-muted-foreground mx-auto opacity-50 mb-4" />
                    <h3 className="text-lg font-semibold text-foreground mb-2">
                        {error}
                    </h3>
                    <Button onClick={loadPlayers} variant="outline">
                        Retry
                    </Button>
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
                        Players
                    </motion.h1>
                    <motion.p 
                        className="text-lg text-muted-foreground"
                        initial={{ opacity: 0, y: -10 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.6, delay: 0.1 }}
                    >
                        Manage and monitor all registered players
                    </motion.p>
                </div>
            </div>

            {}
            <div className="px-6 pb-8">
                <motion.div 
                    className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.6, delay: 0.2 }}
                >
                    <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm">
                        <CardContent className="p-4">
                            <div className="flex items-center space-x-3 mb-2">
                                <div className="w-10 h-10 rounded-full bg-gradient-to-br from-[oklch(75.54% 0.1534 231.639,0.2)] to-[oklch(75.54% 0.1534 231.639,0.1)] flex items-center justify-center border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-lg">
                                    <Users className="h-5 w-5 text-[oklch(75.54% 0.1534 231.639)]" />
                                </div>
                                <div>
                                    <p className="text-sm font-medium text-muted-foreground">Total Players</p>
                                    <p className="text-2xl font-bold text-foreground">{stats.total}</p>
                                </div>
                            </div>
                        </CardContent>
                    </Card>

                    <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm">
                        <CardContent className="p-4">
                            <div className="flex items-center space-x-3 mb-2">
                                <div className="w-10 h-10 rounded-full bg-gradient-to-br from-[oklch(75.54% 0.1534 231.639,0.2)] to-[oklch(75.54% 0.1534 231.639,0.1)] flex items-center justify-center border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-lg">
                                    <Activity className="h-5 w-5 text-[oklch(75.54% 0.1534 231.639)]" />
                                </div>
                                <div>
                                    <p className="text-sm font-medium text-muted-foreground">Online Players</p>
                                    <p className="text-2xl font-bold text-foreground">{stats.online}</p>
                                </div>
                            </div>
                        </CardContent>
                    </Card>

                    <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm">
                        <CardContent className="p-4">
                            <div className="flex items-center space-x-3 mb-2">
                                <div className="w-10 h-10 rounded-full bg-gradient-to-br from-[oklch(75.54% 0.1534 231.639,0.2)] to-[oklch(75.54% 0.1534 231.639,0.1)] flex items-center justify-center border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-lg">
                                    <Globe className="h-5 w-5 text-[oklch(75.54% 0.1534 231.639)]" />
                                </div>
                                <div>
                                    <p className="text-sm font-medium text-muted-foreground">Offline Players</p>
                                    <p className="text-2xl font-bold text-foreground">{stats.offline}</p>
                                </div>
                            </div>
                        </CardContent>
                    </Card>
                </motion.div>
            </div>

            {}
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
                                placeholder="Search players by name or service..."
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                                className="pl-10 h-9 text-sm border-border/50 focus:border-[oklch(75.54% 0.1534 231.639)] focus:ring-2 focus:ring-[oklch(75.54% 0.1534 231.639,0.2)] transition-all duration-200"
                            />
                        </div>
                    </div>
                </motion.div>
            </div>

            {}
            <div className="flex-1 px-6 pb-6 overflow-auto modern-scrollbar">
                {filteredPlayers.length > 0 ? (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5 gap-4">
                        {filteredPlayers.map((player, index) => (
                            <motion.div
                                key={player.name}
                                initial={{ opacity: 0, y: 20 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ delay: index * 0.1 }}
                                exit={{ opacity: 0, y: -20 }}
                            >
                                <Card 
                                    className="border-border/50 bg-card/50 backdrop-blur-sm cursor-pointer hover:bg-card/70 transition-all duration-200"
                                    onClick={() => router.push(`/players/${player.name}`)}
                                >
                                    <CardContent className="p-4">
                                        <div className="flex items-center space-x-3 mb-3">
                                            <div className="w-12 h-12 rounded-lg overflow-hidden border border-border">
                                                <Image
                                                    src={`https://mineskin.eu/helm/${player.name}/64`}
                                                    alt={`${player.name} Minecraft Head`}
                                                    width={48}
                                                    height={48}
                                                    className="w-12 h-12"
                                                    onError={(e) => {
                                                        e.currentTarget.src = 'https://mineskin.eu/helm/MHF_Question/64'
                                                    }}
                                                />
                                            </div>
                                            <div className="flex-1 min-w-0">
                                                <h3 className="font-semibold text-foreground truncate">{player.name}</h3>
                                                <p className="text-sm text-muted-foreground truncate">
                                                    {player.currentServiceName && player.currentServiceName !== 'null' 
                                                        ? player.currentServiceName 
                                                        : 'Offline'
                                                    }
                                                </p>
                                            </div>
                                        </div>
                                        
                                        <div className="space-y-2">
                                            <div className="flex items-center justify-between text-sm">
                                                <span className="text-muted-foreground">Status:</span>
                                                <Badge 
                                                    variant={player.currentServiceName && player.currentServiceName !== 'null' ? 'default' : 'secondary'}
                                                    className={player.currentServiceName && player.currentServiceName !== 'null' 
                                                        ? 'bg-green-500/10 text-green-500 border-green-500/20' 
                                                        : 'bg-muted text-muted-foreground'
                                                    }
                                                >
                                                    {player.currentServiceName && player.currentServiceName !== 'null' ? 'Online' : 'Offline'}
                                                </Badge>
                                            </div>
                                            
                                            <div className="text-xs text-muted-foreground">
                                                UUID: {player.uuid.substring(0, 8)}...
                                            </div>
                                        </div>
                                    </CardContent>
                                </Card>
                            </motion.div>
                        ))}
                    </div>
                ) : (
                    <div className="text-center py-12">
                        <div className="space-y-4">
                            <Users className="w-16 h-16 text-muted-foreground mx-auto opacity-50" />
                            <div>
                                <h3 className="text-lg font-semibold text-foreground">
                                    No players found
                                </h3>
                                <p className="text-muted-foreground mt-1">
                                    {searchTerm
                                        ? 'Try adjusting your search'
                                        : 'No players are currently registered'
                                    }
                                </p>
                            </div>
                            {searchTerm && (
                                <Button
                                    onClick={() => setSearchTerm('')}
                                    variant="outline"
                                    className="border-border/50 text-foreground hover:bg-background/50"
                                >
                                    Clear Search
                                </Button>
                            )}
                        </div>
                    </div>
                )}

                {}
                {totalPages > 1 && (
                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.6, delay: 0.4 }}
                        className="flex items-center justify-center gap-2 mt-8"
                    >
                        <Button
                            variant="outline"
                            size="sm"
                            onClick={() => handlePageChange(currentPage - 1)}
                            disabled={currentPage <= 1}
                            className="border-border/50 text-foreground hover:bg-background/50"
                        >
                            <ChevronLeft className="w-4 h-4 mr-1" />
                            Previous
                        </Button>
                        
                        <div className="flex items-center gap-1">
                            {Array.from({ length: Math.min(5, totalPages) }, (_, i) => {
                                let pageNum;
                                if (totalPages <= 5) {
                                    pageNum = i + 1;
                                } else if (currentPage <= 3) {
                                    pageNum = i + 1;
                                } else if (currentPage >= totalPages - 2) {
                                    pageNum = totalPages - 4 + i;
                                } else {
                                    pageNum = currentPage - 2 + i;
                                }
                                
                                return (
                                    <Button
                                        key={pageNum}
                                        variant={currentPage === pageNum ? 'default' : 'outline'}
                                        size="sm"
                                        onClick={() => handlePageChange(pageNum)}
                                        className={`w-8 h-8 p-0 ${
                                            currentPage === pageNum 
                                                ? 'bg-[oklch(0.7554_0.1534_231.639)] text-white' 
                                                : 'border-border/50 text-foreground hover:bg-background/50'
                                        }`}
                                    >
                                        {pageNum}
                                    </Button>
                                );
                            })}
                        </div>
                        
                        <Button
                            variant="outline"
                            size="sm"
                            onClick={() => handlePageChange(currentPage + 1)}
                            disabled={currentPage >= totalPages}
                            className="border-border/50 text-foreground hover:bg-background/50"
                        >
                            Next
                            <ChevronRight className="w-4 h-4 ml-1" />
                        </Button>
                    </motion.div>
                )}
            </div>
        </div>
    );
}

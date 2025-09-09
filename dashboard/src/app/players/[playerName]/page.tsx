'use client'

import { useState, useEffect } from 'react'
import Image from 'next/image';
import { useParams, useRouter } from 'next/navigation';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { motion } from 'framer-motion';
import { ArrowLeft, User, Server } from 'lucide-react';
import GlobalNavbar from '@/components/global-navbar';

import { Player } from '@/types/players';

export default function PlayerDetailPage() {
    const router = useRouter();
    const params = useParams();
    const playerName = params.playerName as string;
    
    const [player, setPlayer] = useState<Player | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchPlayer = async () => {
            if (!playerName) {
                setError('Player name not found');
                setIsLoading(false);
                return;
            }

            try {
                setIsLoading(true);
                setError(null);
                
                const response = await fetch(`/api/players/${playerName}`);
                
                if (response.ok) {
                    const data = await response.json();
                    setPlayer(data);
                } else {
                    const errorData = await response.json();
                    setError(errorData.error || 'Failed to fetch player');
                }
            } catch {
                setError('Failed to fetch player');
            } finally {
                setIsLoading(false);
            }
        };

        fetchPlayer();
    }, [playerName]);

    const handleBackClick = () => {
        router.push('/players');
    };

    const containerVariants = {
        hidden: { opacity: 0 },
        visible: {
            opacity: 1,
            transition: {
                duration: 0.5,
                staggerChildren: 0.1
            }
        }
    };

    const itemVariants = {
        hidden: { opacity: 0, y: 20 },
        visible: { opacity: 1, y: 0 }
    };

    if (isLoading) {
        return (
            <div className="w-full h-full flex items-center justify-center">
                <div className="w-8 h-8 border-2 border-primary border-t-transparent rounded-full animate-spin"></div>
            </div>
        );
    }

    if (error || !player) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20 ultra-smooth-scroll">
                <GlobalNavbar />
                
                <div className="h-2"></div>

                <div className="flex flex-col items-center justify-center h-64 text-center p-6">
                    <User className="w-16 h-16 text-muted-foreground mb-4" />
                    <h3 className="text-lg font-semibold text-foreground mb-2">Player not found</h3>
                    <p className="text-muted-foreground mb-4">
                        {error || 'The requested player could not be found'}
                    </p>
                    <Button onClick={handleBackClick} variant="outline">
                        <ArrowLeft className="w-4 h-4 mr-2" />
                        Back to Players
                    </Button>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20 ultra-smooth-scroll">
            <GlobalNavbar />
            
            <div className="h-2"></div>
            


            <motion.div
                className="flex flex-1 flex-col px-6 pb-6 gap-6"
                variants={containerVariants}
                initial="hidden"
                animate="visible"
            >
                <motion.div variants={itemVariants} className="flex items-center gap-4">
                    <Button onClick={handleBackClick} variant="outline" size="sm">
                        <ArrowLeft className="w-4 h-4 mr-2" />
                        Back
                    </Button>
                    <div className="flex-1">
                        <h1 className="text-3xl font-bold text-foreground">{player.name}</h1>
                        <p className="text-muted-foreground">Player Details</p>
                    </div>
                </motion.div>

                <motion.div
                    variants={itemVariants}
                    className="grid grid-cols-1 lg:grid-cols-2 gap-6"
                >
                    <Card className="border-border/50">
                        <CardHeader>
                            <CardTitle className="flex items-center gap-2">
                                <User className="w-5 h-5" />
                                Player Profile
                            </CardTitle>
                        </CardHeader>
                        <CardContent className="space-y-4">
                            <div className="flex items-center justify-center mb-4">
                                <div className="relative">
                                    <Image
                                        src={`https://mineskin.eu/helm/${player.name}/80.png`}
                                        alt={`${player.name}'s Minecraft skin`}
                                        width={80}
                                        height={80}
                                        className="h-20 w-20 rounded-lg border-2 border-border/50"
                                        onError={(e) => {
                                            e.currentTarget.src = 'https://mineskin.eu/helm/HttpMarco/80.png';
                                        }}
                                    />
                                    {player.currentServiceName && player.currentServiceName !== 'null' && (
                                        <div className="absolute -top-1 -right-1 w-4 h-4 bg-green-500 rounded-full border-2 border-background animate-pulse"></div>
                                    )}
                                </div>
                            </div>

                            <div className="space-y-3">
                                <div className="flex justify-between items-center">
                                    <span className="text-sm font-medium text-muted-foreground">Username:</span>
                                    <span className="text-sm text-foreground font-mono">{player.name}</span>
                                </div>
                                <div className="flex justify-between items-center">
                                    <span className="text-sm font-medium text-muted-foreground">UUID:</span>
                                    <span className="text-sm text-foreground font-mono">{player.uuid}</span>
                                </div>
                                <div className="flex justify-between items-center">
                                    <span className="text-sm font-medium text-muted-foreground">Status:</span>
                                    <div className={`px-2 py-1 rounded-full text-xs font-medium border ${
                                        player.currentServiceName && player.currentServiceName !== 'null'
                                            ? 'bg-green-500/10 text-green-500 border-green-500/30'
                                            : 'bg-gray-500/10 text-gray-500 border-gray-500/30'
                                    }`}>
                                        {player.currentServiceName && player.currentServiceName !== 'null' ? 'Online' : 'Offline'}
                                    </div>
                                </div>
                            </div>
                        </CardContent>
                    </Card>

                    <Card className="border-border/50">
                        <CardHeader>
                            <CardTitle className="flex items-center gap-2">
                                <Server className="w-5 h-5" />
                                Current Service
                            </CardTitle>
                        </CardHeader>
                        <CardContent className="space-y-4">
                            {player.currentServiceName && player.currentServiceName !== 'null' ? (
                                <div className="space-y-3">
                                    <div className="flex justify-between items-center">
                                        <span className="text-sm font-medium text-muted-foreground">Service:</span>
                                        <Badge variant="outline" className="text-sm">
                                            {player.currentServiceName}
                                        </Badge>
                                    </div>
                                    <div className="flex justify-between items-center">
                                        <span className="text-sm font-medium text-muted-foreground">Status:</span>
                                        <Badge variant="secondary" className="text-sm">
                                            Connected
                                        </Badge>
                                    </div>
                                </div>
                            ) : (
                                <div className="text-center py-8">
                                    <Server className="w-12 h-12 text-muted-foreground mx-auto mb-3" />
                                    <p className="text-muted-foreground">Not connected to any service</p>
                                </div>
                            )}
                        </CardContent>
                    </Card>
                </motion.div>
            </motion.div>
        </div>
    );
}

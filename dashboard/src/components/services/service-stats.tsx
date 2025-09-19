'use client';

import { Card, CardContent } from '@/components/ui/card';
import { motion } from 'framer-motion';
import {
    Server,
    HardDrive,
    Users,
    Activity
} from 'lucide-react';

interface ServiceStatsProps {
    stats: {
        total: number;
        online: number;
        players: number;
        memory: number;
    };
}

export function ServiceStats({ stats }: ServiceStatsProps) {
    return (
        <motion.div 
            className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.2 }}
        >
            {}
            <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm">
                <CardContent className="p-4">
                    <div className="flex items-center space-x-3 mb-2">
                        <div className="w-10 h-10 rounded-full bg-gradient-to-br from-[oklch(75.54% 0.1534 231.639,0.2)] to-[oklch(75.54% 0.1534 231.639,0.1)] flex items-center justify-center border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-lg">
                            <Server className="h-5 w-5 text-[oklch(75.54% 0.1534 231.639)]" />
                        </div>
                        <div>
                            <p className="text-sm font-medium text-muted-foreground">Total Services</p>
                            <p className="text-2xl font-bold text-foreground">{stats.total}</p>
                        </div>
                    </div>
                </CardContent>
            </Card>

            {}
            <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm">
                <CardContent className="p-4">
                    <div className="flex items-center space-x-3 mb-2">
                        <div className="w-10 h-10 rounded-full bg-gradient-to-br from-[oklch(75.54% 0.1534 231.639,0.2)] to-[oklch(75.54% 0.1534 231.639,0.1)] flex items-center justify-center border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-lg">
                            <Activity className="h-5 w-5 text-[oklch(75.54% 0.1534 231.639)]" />
                        </div>
                        <div>
                            <p className="text-sm font-medium text-muted-foreground">Online</p>
                            <p className="text-2xl font-bold text-foreground">{stats.online}</p>
                        </div>
                    </div>
                </CardContent>
            </Card>

            {}
            <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm">
                <CardContent className="p-4">
                    <div className="flex items-center space-x-3 mb-2">
                        <div className="w-10 h-10 rounded-full bg-gradient-to-br from-[oklch(75.54% 0.1534 231.639,0.2)] to-[oklch(75.54% 0.1534 231.639,0.1)] flex items-center justify-center border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-lg">
                            <Users className="h-5 w-5 text-[oklch(75.54% 0.1534 231.639)]" />
                        </div>
                        <div>
                            <p className="text-sm font-medium text-muted-foreground">Total Players</p>
                            <p className="text-2xl font-bold text-foreground">{stats.players}</p>
                        </div>
                    </div>
                </CardContent>
            </Card>

            {}
            <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm">
                <CardContent className="p-4">
                    <div className="flex items-center space-x-3 mb-2">
                        <div className="w-10 h-10 rounded-full bg-gradient-to-br from-[oklch(75.54% 0.1534 231.639,0.2)] to-[oklch(75.54% 0.1534 231.639,0.1)] flex items-center justify-center border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-lg">
                            <HardDrive className="h-5 w-5 text-[oklch(75.54% 0.1534 231.639)]" />
                        </div>
                        <div>
                            <p className="text-sm font-medium text-muted-foreground">Memory Used</p>
                            <p className="text-2xl font-bold text-foreground">{stats.memory} MB</p>
                        </div>
                    </div>
                </CardContent>
            </Card>
        </motion.div>
    );
}

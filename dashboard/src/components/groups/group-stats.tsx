'use client';

import { Card, CardContent } from '@/components/ui/card';
import { motion } from 'framer-motion';
import { Server, HardDrive, Activity, Settings } from 'lucide-react';

interface GroupStatsProps {
    stats: {
        total: number;
        memory: number;
        services: number;
        templates: number;
    };
}

export function GroupStats({ stats }: GroupStatsProps) {
    const statCards = [
        {
            title: 'Total Groups',
            value: stats.total,
            icon: Server,
            color: 'from-[oklch(75.54% 0.1534 231.639,0.2)] to-[oklch(75.54% 0.1534 231.639,0.1)]',
            borderColor: 'border-[oklch(75.54% 0.1534 231.639,0.3)]',
            iconColor: 'text-[oklch(75.54% 0.1534 231.639)]'
        },
        {
            title: 'Total Memory',
            value: `${stats.memory} MB`,
            icon: HardDrive,
            color: 'from-[oklch(75.54% 0.1534 231.639,0.2)] to-[oklch(75.54% 0.1534 231.639,0.1)]',
            borderColor: 'border-[oklch(75.54% 0.1534 231.639,0.3)]',
            iconColor: 'text-[oklch(75.54% 0.1534 231.639)]'
        },
        {
            title: 'Max Services',
            value: stats.services,
            icon: Activity,
            color: 'from-[oklch(75.54% 0.1534 231.639,0.2)] to-[oklch(75.54% 0.1534 231.639,0.1)]',
            borderColor: 'border-[oklch(75.54% 0.1534 231.639,0.3)]',
            iconColor: 'text-[oklch(75.54% 0.1534 231.639)]'
        },
        {
            title: 'Templates',
            value: stats.templates,
            icon: Settings,
            color: 'from-[oklch(75.54% 0.1534 231.639,0.2)] to-[oklch(75.54% 0.1534 231.639,0.1)]',
            borderColor: 'border-[oklch(75.54% 0.1534 231.639,0.3)]',
            iconColor: 'text-[oklch(75.54% 0.1534 231.639)]'
        }
    ];

    return (
        <motion.div 
            className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.2 }}
        >
            {statCards.map((card) => (
                <Card 
                    key={card.title}
                    className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border shadow-2xl backdrop-blur-sm"
                    style={{ borderColor: card.borderColor }}
                >
                    <CardContent className="p-4">
                        <div className="flex items-center space-x-3 mb-2">
                            <div 
                                className={`w-10 h-10 rounded-full bg-gradient-to-br ${card.color} flex items-center justify-center border shadow-lg`}
                                style={{ borderColor: card.borderColor }}
                            >
                                <card.icon className={`h-5 w-5 ${card.iconColor}`} />
                            </div>
                            <div>
                                <p className="text-sm font-medium text-muted-foreground">{card.title}</p>
                                <p className="text-2xl font-bold text-foreground">{card.value}</p>
                            </div>
                        </div>
                    </CardContent>
                </Card>
            ))}
        </motion.div>
    );
}

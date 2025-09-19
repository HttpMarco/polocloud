'use client';

import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Activity, Boxes, LucideIcon, Server, TrendingUp, TrendingDown, Minus } from 'lucide-react';
import { motion } from 'framer-motion';
import { useRouter } from 'next/navigation';


interface DashboardCardProps {
    title: string;
    icon: LucideIcon;
    value: string;
    subValue: string;
    trend: string;
    trendDirection: 'up' | 'down' | 'stable';
    onClick?: () => void;
}

const DashboardCard = ({ title, icon, value, subValue, trend, trendDirection, onClick }: DashboardCardProps) => {
    const LucideIcon = icon;

    const getTrendIcon = () => {
        switch (trendDirection) {
            case 'up':
                return <TrendingUp className="w-3 h-3" />;
            case 'down':
                return <TrendingDown className="w-3 h-3" />;
            case 'stable':
                return <Minus className="w-3 h-3" />;
            default:
                return null;
        }
    };

    const getTrendBadgeColor = () => {
        switch (trendDirection) {
            case 'up':
                return 'bg-green-500/10 text-green-500 border-green-500/20';
            case 'down':
                return 'bg-red-500/10 text-red-500 border-red-500/20';
            case 'stable':
                return 'bg-blue-500/10 text-blue-500 border-blue-500/20';
            default:
                return 'bg-muted text-muted-foreground border-border';
        }
    };

    const handleClick = () => {
        if (onClick) {
            onClick();
        }
    };

    const CardComponent = (
        <Card className="relative overflow-hidden bg-gradient-to-br from-card/80 via-card/60 to-card/80 border border-border/40 shadow-xl backdrop-blur-sm min-h-[160px] rounded-2xl hover:shadow-2xl transition-all duration-300 hover:scale-[1.02]">
            <div className="absolute inset-0 bg-[radial-gradient(circle_at_30%_20%,rgba(75.54%,15.34%,0.05)_0%,transparent_50%)] opacity-60 rounded-2xl" />
            
            <div className="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r from-[oklch(75.54% .1534 231.639)] via-[oklch(75.54% .1534 231.639,0.8)] to-[oklch(75.54% .1534 231.639)] rounded-t-2xl" />
            
            <div className="absolute inset-0 rounded-2xl shadow-[0_0_50px_rgba(75.54%,15.34%,231.639,0.2)] pointer-events-none" />

            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-3 relative z-10">
                <CardTitle className="text-sm font-medium text-foreground/90">{title}</CardTitle>
                <Badge variant="secondary" className={`${getTrendBadgeColor()} text-xs font-medium`}>
                    {getTrendIcon()}
                    <span className="ml-1">{trend}</span>
                </Badge>
            </CardHeader>

            <CardContent className="relative z-10">
                <div className="mb-4">
                    <div className="text-4xl font-bold text-foreground mb-2">{value}</div>
                    <p className="text-sm text-muted-foreground leading-relaxed">{subValue}</p>
                </div>

                <div className="absolute top-4 right-4 opacity-10">
                    <LucideIcon className="size-20 text-foreground" />
                </div>
            </CardContent>
        </Card>
    );

    if (onClick) {
        return (
            <motion.div
                onClick={handleClick}
                className="cursor-pointer"
                whileHover={{ scale: 1.02 }}
                whileTap={{ scale: 0.98 }}
                transition={{ type: "spring", stiffness: 400, damping: 17 }}
            >
                {CardComponent}
            </motion.div>
        );
    }

    return (
        <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
        >
            {CardComponent}
        </motion.div>
    );
};

interface DashboardStatsProps {
    stats: {
        groups: { count: number; trend: string; trendDirection: 'up' | 'down' | 'stable' };
        services: { count: number; trend: string; trendDirection: 'up' | 'down' | 'stable' };
    };
    isLoadingGroups: boolean;
    isLoadingServices: boolean;
}

export function DashboardStats({ stats, isLoadingGroups, isLoadingServices }: DashboardStatsProps) {
    const router = useRouter();

    const dashboardCards = [
        {
            title: "System Status",
            icon: Activity,
            value: "Healthy",
            subValue: "No major incidents in the last 24 hours",
            trend: "+2.5%",
            trendDirection: 'up' as const
        },
        {
            title: "Active Groups",
            icon: Boxes,
            value: isLoadingGroups ? "..." : stats.groups.count.toString(),
            subValue: isLoadingGroups ? "Loading groups..." : `+${stats.groups.count} active group${stats.groups.count !== 1 ? 's' : ''} in last 7 days`,
            trend: stats.groups.trend,
            trendDirection: stats.groups.trendDirection,
            onClick: () => router.push('/groups')
        },
        {
            title: "Running Services",
            icon: Server,
            value: isLoadingServices ? "..." : stats.services.count.toString(),
            subValue: isLoadingServices ? "Loading services..." : `+${stats.services.count} active service${stats.services.count !== 1 ? 's' : ''} in last 24 hours`,
            trend: stats.services.trend,
            trendDirection: stats.services.trendDirection,
            onClick: () => router.push('/services')
        }
    ];

    return (
        <div className="grid gap-6 lg:gap-8 grid-cols-1 md:grid-cols-3 w-full max-w-6xl">
            {dashboardCards.map((card) => (
                <motion.div key={card.title} className="w-full">
                    <DashboardCard
                        title={card.title}
                        icon={card.icon}
                        value={card.value}
                        subValue={card.subValue}
                        trend={card.trend}
                        trendDirection={card.trendDirection}
                        onClick={card.onClick}
                    />
                </motion.div>
            ))}
        </div>
    );
}

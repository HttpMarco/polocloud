'use client';

import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip';
import { motion } from 'framer-motion';
import { useState } from 'react';
import {
    Play,
    Square,
    Loader2,
    Activity,
    Users,
    Cpu,
    HardDrive,
    Settings,
    Terminal,
    RefreshCcw,
    Copy,
    Check
} from 'lucide-react';
import { Service } from '@/types/services';
import { usePermissions } from '@/hooks/usePermissions';

interface ServiceCardProps {
    service: Service;
    index: number;
    restartingServices: string[];
    onRestart: (serviceName: string) => void;
}

const formatServiceValue = (value: number, unit: string = ''): string => {
    if (value < 0) {
        return 'Loading...';
    }
    return `${value}${unit}`;
};

const formatPlayerCount = (current: number, max: number): string => {
    if (current < 0 || max < 0) {
        return 'Loading...';
    }
    return `${current} / ${max}`;
};

const formatMemory = (current: number, max: number): string => {
    if (current < 0 || max < 0) {
        return 'Loading...';
    }
    return `${current} / ${max} MB`;
};

const truncateHostname = (hostname: string, maxLength: number = 25): string => {
    if (hostname.length <= maxLength) {
        return hostname;
    }
    return hostname.substring(0, maxLength) + '...';
};

export function ServiceCard({ service, index, restartingServices, onRestart }: ServiceCardProps) {
    const isRestarting = restartingServices.includes(service.name);
    const [copied, setCopied] = useState(false);

    const { hasPermission } = usePermissions();
    const canAccessTerminal = hasPermission('polocloud.service.screen');
    const canRestartService = hasPermission('polocloud.service.restart');

    const handleCopyHostname = async () => {
        try {
            await navigator.clipboard.writeText(`${service.hostname}:${service.port}`);
            setCopied(true);
            setTimeout(() => setCopied(false), 2000);
        } catch (err) {
            console.error('Failed to copy hostname:', err);
        }
    };

    return (
        <TooltipProvider>
            <motion.div
                initial={{ opacity: 0, y: 40, scale: 0.85 }}
                animate={{ opacity: 1, y: 0, scale: 1 }}
                transition={{
                    duration: 0.8,
                    delay: index * 0.1,
                    ease: [0.34, 1.56, 0.64, 1]
                }}
            >
                <Card className="border-border/40 flex flex-col relative bg-gradient-to-br from-card/80 via-card/60 to-card/80 shadow-lg transition-all duration-300 group w-80 h-96">
                <div className="absolute inset-0 bg-[radial-gradient(circle_at_30%_20%,rgba(75.54%,15.34%,0.03)_0%,transparent_50%)] opacity-60 rounded-t-lg"/>
                
                <CardHeader className="pb-4 flex-shrink-0 relative z-10">
                    <div className="flex items-center gap-3">
                        <div className="flex items-center gap-3">
                            <div className="p-2">
                                {service.state === 'ONLINE' ? (
                                    <Play className="h-6 w-6 text-green-500" />
                                ) : service.state === 'OFFLINE' || service.state === 'STOPPING' || service.state === 'STOPPED' ? (
                                    <Square className="h-6 w-6 text-red-500" />
                                ) : (
                                    <Loader2 className="h-6 w-6 text-yellow-500 animate-spin" />
                                )}
                            </div>
                            <div className="flex-1 min-w-0">
                                <CardTitle className="text-lg font-bold text-foreground truncate transition-colors duration-200">
                                    {service.name}
                                </CardTitle>
                                <div className="flex items-center gap-2 mt-1">
                                    <Tooltip>
                                        <TooltipTrigger asChild>
                                            <button
                                                onClick={handleCopyHostname}
                                                className="text-sm text-muted-foreground hover:text-foreground transition-colors duration-200 cursor-pointer flex items-center gap-1 group"
                                                title="Click to copy full hostname"
                                            >
                                                <span className="truncate">
                                                    {truncateHostname(`${service.hostname}:${service.port}`)}
                                                </span>
                                                {copied ? (
                                                    <Check className="w-3 h-3 text-green-500" />
                                                ) : (
                                                    <Copy className="w-3 h-3 opacity-0 group-hover:opacity-100 transition-opacity" />
                                                )}
                                            </button>
                                        </TooltipTrigger>
                                        <TooltipContent side="bottom" className="max-w-xs">
                                            <div className="text-center">
                                                <p className="font-medium">Full Hostname:</p>
                                                <p className="text-xs break-all">{service.hostname}:{service.port}</p>
                                                <p className="text-xs text-muted-foreground mt-1">
                                                    {copied ? 'Copied to clipboard!' : 'Click to copy'}
                                                </p>
                                            </div>
                                        </TooltipContent>
                                    </Tooltip>
                                </div>
                            </div>
                        </div>
                    </div>

                    <Badge
                        className="absolute top-4 right-4 text-xs h-6 px-3 transition-all duration-200 rounded-full bg-white/10 border border-white/20 text-white"
                    >
                        {service.type}
                    </Badge>
                </CardHeader>

                <CardContent className="flex-1 flex flex-col space-y-4 pt-0 relative z-10">
                    {}
                    <div className="grid grid-cols-2 gap-4">
                        <div className="bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] rounded-lg p-3 border border-[oklch(75.54%_0.1534_231.639,0.3)]">
                            <div className="flex items-center gap-2 mb-2">
                                <Activity className="w-4 h-4 text-muted-foreground" />
                                <span className="text-xs font-medium text-muted-foreground uppercase tracking-wide">Status</span>
                            </div>
                            <div className="text-sm font-semibold text-foreground">
                                <Badge
                                    variant="outline"
                                    className={`text-xs h-6 px-3 ${
                                        service.state === 'ONLINE'
                                            ? 'border-green-500/50 text-green-500 bg-green-500/10'
                                            : service.state === 'OFFLINE' || service.state === 'STOPPING' || service.state === 'STOPPED'
                                                ? 'border-red-500/50 text-red-500 bg-red-500/10'
                                                : 'border-yellow-500/50 text-yellow-500 bg-yellow-500/10'
                                    }`}
                                >
                                    {service.state}
                                </Badge>
                            </div>
                        </div>

                        <div className="bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] rounded-lg p-3 border border-[oklch(75.54%_0.1534_231.639,0.3)]">
                            <div className="flex items-center gap-2 mb-2">
                                <Users className="w-4 h-4 text-muted-foreground" />
                                <span className="text-xs font-medium text-muted-foreground uppercase tracking-wide">Players</span>
                            </div>
                            <div className="text-sm font-semibold text-foreground truncate" title={`${service.playerCount} / ${service.maxPlayerCount}`}>
                                {formatPlayerCount(service.playerCount, service.maxPlayerCount)}
                            </div>
                        </div>
                    </div>

                    {}
                    <div className="grid grid-cols-2 gap-4">
                        <div className="bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] rounded-lg p-3 border border-[oklch(75.54%_0.1534_231.639,0.3)]">
                            <div className="flex items-center gap-2 mb-2">
                                <Cpu className="w-4 h-4 text-muted-foreground" />
                                <span className="text-xs font-medium text-muted-foreground uppercase tracking-wide">CPU</span>
                            </div>
                            <div className="text-sm font-semibold text-foreground truncate" title={`${service.cpuUsage}%`}>
                                {formatServiceValue(service.cpuUsage, '%')}
                            </div>
                        </div>

                        <div className="bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] rounded-lg p-3 border border-[oklch(75.54%_0.1534_231.639,0.3)]">
                            <div className="flex items-center gap-2 mb-2">
                                <HardDrive className="w-4 h-4 text-muted-foreground" />
                                <span className="text-xs font-medium text-muted-foreground uppercase tracking-wide">Memory</span>
                            </div>
                            <div className="text-sm font-semibold text-foreground truncate" title={`${service.memoryUsage} / ${service.maxMemory} MB`}>
                                {formatMemory(service.memoryUsage, service.maxMemory)}
                            </div>
                        </div>
                    </div>

                    {}
                    <div className="flex items-center gap-3 pt-2 min-h-[32px]">
                        <div className="flex items-center gap-2">
                            <Settings className="w-4 h-4 text-muted-foreground" />
                            <span className="text-sm font-medium text-muted-foreground">Advanced</span>
                        </div>
                        
                        {service.properties?.staticService === 'true' && (
                            <Badge 
                                variant="outline" 
                                className="text-xs px-2 py-1 border-green-500/50 text-green-500 bg-green-500/10"
                            >
                                Static: Yes
                            </Badge>
                        )}
                        
                        {service.properties?.fallback === 'true' && (
                            <Badge 
                                variant="outline" 
                                className="text-xs px-2 py-1 border-blue-500/50 text-blue-500 bg-blue-500/10"
                            >
                                Fallback: Yes
                            </Badge>
                        )}
                    </div>

                    {}
                    <div className="pt-2">
                        <div className="flex items-center justify-center gap-3">
                            <Button
                                variant="outline"
                                size="sm"
                                className={`flex-1 transition-all duration-200 ${
                                    !canAccessTerminal 
                                        ? 'opacity-40 cursor-not-allowed bg-muted text-muted-foreground border border-border/30' 
                                        : 'border-border/50 text-foreground'
                                }`}
                                title={!canAccessTerminal ? "No permission to access terminal" : "Open Console"}
                                disabled={service.state === 'OFFLINE' || !canAccessTerminal}
                                onClick={() => canAccessTerminal && (window.location.href = `/services/${service.name}/screen`)}
                            >
                                <Terminal className="w-4 h-4 mr-2" />
                                Terminal
                            </Button>

                            <Button
                                variant="outline"
                                size="sm"
                                className={`flex-1 transition-all duration-200 ${
                                    !canRestartService 
                                        ? 'opacity-40 cursor-not-allowed bg-muted text-muted-foreground border border-border/30' 
                                        : 'border-border/50 text-foreground'
                                }`}
                                title={!canRestartService ? "No permission to restart service" : "Restart Service"}
                                disabled={
                                    service.state === 'STARTING' ||
                                    service.state === 'OFFLINE' ||
                                    isRestarting ||
                                    !canRestartService
                                }
                                onClick={() => canRestartService && onRestart(service.name)}
                            >
                                {isRestarting ? (
                                    <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                                ) : (
                                    <RefreshCcw className="w-4 h-4 mr-2" />
                                )}
                                Restart
                            </Button>
                        </div>
                    </div>
                </CardContent>
            </Card>
        </motion.div>
        </TooltipProvider>
    );
}

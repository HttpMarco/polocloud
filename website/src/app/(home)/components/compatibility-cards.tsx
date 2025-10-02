import { Check, X, AlertTriangle, Minus, Server } from 'lucide-react';
import Image from 'next/image';
import { useEffect, useState } from 'react';

interface Platform {
    id: string;
    name: string;
    icon: string;
    versions: {
        [key: string]: 'supported' | 'not-supported' | 'partial' | 'not-possible';
    };
    addons: {
        [key: string]: 'supported' | 'not-supported' | 'partial' | 'not-possible';
    };
    addedAt: string;
    addedBy: string;
}

const StatusIcon = ({ status }: { status: string }) => {
    switch (status) {
        case 'supported':
            return <Check className="w-3 h-3 text-emerald-500" />;
        case 'partial':
            return <AlertTriangle className="w-3 h-3 text-amber-500" />;
        case 'not-supported':
            return <X className="w-3 h-3 text-red-500" />;
        case 'not-possible':
            return <Minus className="w-3 h-3 text-gray-400" />;
        default:
            return <Minus className="w-3 h-3 text-gray-400" />;
    }
};

const getStatusColor = (status: string) => {
    switch (status) {
        case 'supported': return 'text-emerald-500';
        case 'partial': return 'text-amber-500';
        case 'not-supported': return 'text-red-500';
        case 'not-possible': return 'text-gray-400';
        default: return 'text-gray-500';
    }
};

const getStatusLabel = (status: string) => {
    switch (status) {
        case 'supported': return 'Supported';
        case 'partial': return 'Partial';
        case 'not-supported': return 'Not Supported';
        case 'not-possible': return 'Not Possible';
        default: return status;
    }
};

const PlatformIcon = ({ platform }: { platform: Platform }) => {
    if (!platform.icon) {
        return (
            <div className="w-12 h-12 rounded-lg bg-muted flex items-center justify-center border border-border/50">
                <Server className="w-6 h-6 text-muted-foreground" />
            </div>
        );
    }

    return (
        <div className="w-12 h-12 rounded-lg border border-border/50 overflow-hidden flex-shrink-0">
            <Image
                src={platform.icon}
                alt={`${platform.name} icon`}
                width={48}
                height={48}
                className="w-full h-full object-cover"
                onError={(e) => {
                    console.warn(`Failed to load icon for ${platform.name}:`, platform.icon);
                    e.currentTarget.style.display = 'none';
                }}
            />
        </div>
    );
};

export function CompatibilityCards() {
    const [platforms, setPlatforms] = useState<Platform[]>([]);
    const [loading, setLoading] = useState(true);
    const [isVisible, setIsVisible] = useState(false);

    const versionColumns = ['1.7-1.12', '1.12-1.16', '1.18-1.19', '1.20+'];

    useEffect(() => {
        const fetchPlatforms = async () => {
            try {
                const response = await fetch('/api/public/platforms');
                if (response.ok) {
                    const data = await response.json();
                    setPlatforms(data.platforms || []);
                } else {
                    console.error('Failed to fetch platforms');
                }
            } catch (error) {
                console.error('Error fetching platforms:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchPlatforms();
    }, []);

    useEffect(() => {
        const timer = setTimeout(() => {
            setIsVisible(true);
        }, 100);

        return () => clearTimeout(timer);
    }, []);

    if (loading) {
        return (
            <div className="w-full max-w-7xl mx-auto p-6">
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    {[...Array(9)].map((_, i) => (
                        <div
                            key={i}
                            className="p-4 bg-gradient-to-br from-background/40 via-background/30 to-background/20 rounded-2xl border border-border/20 animate-pulse"
                        >
                            <div className="flex items-center gap-3 mb-4">
                                <div className="w-12 h-12 bg-muted rounded-lg"></div>
                                <div className="flex-1">
                                    <div className="h-5 bg-muted rounded w-24 mb-1"></div>
                                    <div className="h-3 bg-muted rounded w-16"></div>
                                </div>
                            </div>
                            <div className="space-y-4">
                                <div>
                                    <div className="flex items-center gap-2 mb-2">
                                        <div className="w-6 h-6 bg-muted rounded-lg"></div>
                                        <div className="h-4 bg-muted rounded w-16"></div>
                                    </div>
                                    <div className="space-y-1">
                                        {[...Array(4)].map((_, j) => (
                                            <div key={j} className="p-1.5 rounded-md bg-background/20">
                                                <div className="flex justify-between items-center">
                                                    <div className="h-3 bg-muted rounded w-12"></div>
                                                    <div className="h-3 bg-muted rounded w-16"></div>
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                                <div>
                                    <div className="flex items-center gap-2 mb-2">
                                        <div className="w-6 h-6 bg-muted rounded-lg"></div>
                                        <div className="h-4 bg-muted rounded w-12"></div>
                                    </div>
                                    <div className="space-y-1">
                                        {[...Array(2)].map((_, j) => (
                                            <div key={j} className="p-1.5 rounded-md bg-background/20">
                                                <div className="flex justify-between items-center">
                                                    <div className="h-3 bg-muted rounded w-16"></div>
                                                    <div className="h-3 bg-muted rounded w-12"></div>
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        );
    }

    if (platforms.length === 0) {
        return (
            <div className="w-full max-w-7xl mx-auto p-6">
                <div className="bg-background/30 rounded-lg border border-border/30 p-8 text-center text-muted-foreground">
                    No platform compatibility data available.
                </div>
            </div>
        );
    }

    return (
        <div className="w-full max-w-7xl mx-auto p-6">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {platforms.map((platform, index) => (
                    <div
                        key={platform.id}
                        className={`group relative overflow-hidden bg-gradient-to-br from-background/40 via-background/30 to-background/20 rounded-2xl border border-border/20 hover:border-primary/30 transition-all duration-700 hover:shadow-2xl hover:shadow-primary/10 backdrop-blur-sm ${
                            isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                        }`}
                        style={{
                            transitionDelay: `${index * 150}ms`
                        }}
                    >
                        <div className="absolute inset-0 bg-gradient-to-br from-primary/5 via-transparent to-primary/10 opacity-0 group-hover:opacity-100 transition-opacity duration-700" />
                        
                        <div className="relative p-4">
                            <div className="flex items-center gap-3 mb-4">
                                <div className="relative">
                                    <PlatformIcon platform={platform} />
                                    <div className="absolute -inset-1 bg-gradient-to-r from-primary/20 to-primary/10 rounded-xl blur-sm opacity-0 group-hover:opacity-100 transition-opacity duration-500" />
                                </div>
                                <div className="flex-1">
                                    <h4 className="font-bold text-foreground text-lg mb-1 group-hover:text-primary transition-colors duration-300">
                                        {platform.name}
                                    </h4>
                                    <p className="text-xs text-muted-foreground">
                                        Added {new Date(platform.addedAt).toLocaleDateString('en-US', { 
                                            month: 'short', 
                                            day: 'numeric' 
                                        })}
                                    </p>
                                </div>
                            </div>

                            <div className="space-y-4">
                                <div>
                                    <div className="flex items-center gap-2 mb-2">
                                        <div className="w-6 h-6 rounded-lg bg-primary/10 flex items-center justify-center">
                                            <Server className="w-3 h-3 text-primary" />
                                        </div>
                                        <h5 className="font-medium text-foreground text-sm">Versions</h5>
                                    </div>
                                    <div className="space-y-1">
                                        {versionColumns.map((version) => {
                                            const status = platform.versions[version] || 'not-supported';
                                            return (
                                                <div key={version} className="flex items-center justify-between p-1.5 rounded-md bg-background/20 border border-border/10 hover:border-border/20 transition-colors duration-200">
                                                    <span className="text-xs font-medium text-foreground">{version}</span>
                                                    <div className="flex items-center gap-1">
                                                        <StatusIcon status={status} />
                                                        <span className={`text-xs font-semibold ${getStatusColor(status)}`}>
                                                            {getStatusLabel(status)}
                                                        </span>
                                                    </div>
                                                </div>
                                            );
                                        })}
                                    </div>
                                </div>

                                {Object.keys(platform.addons).length > 0 && (
                                    <div>
                                        <div className="flex items-center gap-2 mb-2">
                                            <div className="w-6 h-6 rounded-lg bg-emerald-500/10 flex items-center justify-center">
                                                <Check className="w-3 h-3 text-emerald-500" />
                                            </div>
                                            <h5 className="font-medium text-foreground text-sm">Addons</h5>
                                        </div>
                                        <div className="space-y-1">
                                            {Object.entries(platform.addons).map(([addon, status]) => (
                                                <div key={addon} className="flex items-center justify-between p-1.5 rounded-md bg-background/20 border border-border/10 hover:border-border/20 transition-colors duration-200">
                                                    <span className="text-xs font-medium text-foreground">{addon}</span>
                                                    <div className="flex items-center gap-1">
                                                        <StatusIcon status={status} />
                                                        <span className={`text-xs font-semibold ${getStatusColor(status)}`}>
                                                            {getStatusLabel(status)}
                                                        </span>
                                                    </div>
                                                </div>
                                            ))}
                                        </div>
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

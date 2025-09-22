import { Check, X, AlertTriangle, Minus } from 'lucide-react';
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

const StatusIcon = ({ status }: { status: 'supported' | 'not-supported' | 'partial' | 'not-possible' }) => {
    switch (status) {
        case 'supported':
            return <Check className="w-4 h-4 text-emerald-500" />;
        case 'not-supported':
            return <X className="w-4 h-4 text-red-500" />;
        case 'partial':
            return <AlertTriangle className="w-4 h-4 text-amber-500" />;
        case 'not-possible':
            return <Minus className="w-4 h-4 text-gray-400" />;
    }
};

const PlatformIcon = ({ platform }: { platform: Platform }) => {
    if (!platform.icon) {
        return null;
    }

    return (
        <div className="w-5 h-5 mr-2 flex-shrink-0">
            <Image
                src={platform.icon}
                alt={`${platform.name} icon`}
                width={20}
                height={20}
                className="w-full h-full object-contain rounded"
                onError={(e) => {
                    console.warn(`Failed to load icon for ${platform.name}:`, platform.icon);
                    e.currentTarget.style.display = 'none';
                }}
            />
        </div>
    );
};

export function CompatibilityTable() {
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
                <div className="bg-white/10 dark:bg-black/20 backdrop-blur-xl border border-white/20 dark:border-white/10 rounded-2xl shadow-2xl overflow-hidden dark:shadow-[0_0_50px_rgba(0,120,255,0.15)] shadow-[0_0_50px_rgba(0,120,255,0.1)] ring-1 ring-white/20 dark:ring-white/10">
                    <div className="p-8 text-center text-muted-foreground">
                        Loading platform compatibility data...
                    </div>
                </div>
            </div>
        );
    }

    if (platforms.length === 0) {
        return (
            <div className="w-full max-w-7xl mx-auto p-6">
                <div className="bg-white/10 dark:bg-black/20 backdrop-blur-xl border border-white/20 dark:border-white/10 rounded-2xl shadow-2xl overflow-hidden dark:shadow-[0_0_50px_rgba(0,120,255,0.15)] shadow-[0_0_50px_rgba(0,120,255,0.1)] ring-1 ring-white/20 dark:ring-white/10">
                    <div className="p-8 text-center text-muted-foreground">
                        No platform compatibility data available.
                    </div>
                </div>
            </div>
        );
    }

    const allAddons = new Set<string>();
    platforms.forEach(platform => {
        Object.keys(platform.addons).forEach(addon => allAddons.add(addon));
    });
    const addonColumns = Array.from(allAddons);

    return (
        <div className={`w-full max-w-7xl mx-auto p-6 transition-all duration-1000 ease-out ${
            isVisible ? 'opacity-100 translate-x-0' : 'opacity-0 translate-x-16'
        }`}>
            <div className="bg-white/10 dark:bg-black/20 backdrop-blur-xl border border-white/20 dark:border-white/10 rounded-2xl shadow-2xl overflow-hidden dark:shadow-[0_0_50px_rgba(0,120,255,0.15)] shadow-[0_0_50px_rgba(0,120,255,0.1)] ring-1 ring-white/20 dark:ring-white/10">
                <div className="overflow-x-auto">
                    <table className="w-full">
                        <thead>
                        <tr className={`border-b border-white/10 dark:border-white/5 transition-all duration-500 delay-200 ${
                            isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'
                        }`}>
                            <th className="px-6 py-5 text-left font-semibold text-foreground min-w-[140px] dark:text-white/90 bg-white/5 dark:bg-white/5">
                                Platform
                            </th>
                            {platforms.map((platform, index) => (
                                <th key={platform.id} className={`px-4 py-5 text-center font-semibold text-foreground border-l border-white/10 dark:border-white/5 dark:text-white/90 bg-white/5 dark:bg-white/5 transition-all duration-500 delay-${300 + index * 50}`}>
                                    <div className={`flex items-center justify-center gap-2 transition-all duration-500 delay-${400 + index * 50} ${
                                        isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'
                                    }`}>
                                        <PlatformIcon platform={platform} />
                                        <span>{platform.name}</span>
                                    </div>
                                </th>
                            ))}
                        </tr>
                        </thead>

                        <tbody>
                        <tr className={`border-b border-white/10 dark:border-white/5 transition-all duration-500 delay-400 ${
                            isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'
                        }`}>
                            <td className="px-6 py-4 text-left font-bold text-foreground bg-white/10 dark:bg-white/10 dark:text-white/80">
                                Version support
                            </td>
                            {platforms.map((platform) => (
                                <td key={platform.id} className="px-4 py-4 text-center border-l border-white/10 dark:border-white/5 bg-white/10 dark:bg-white/10">
                                </td>
                            ))}
                        </tr>

                        {versionColumns.map((version, index) => (
                            <tr key={version} className={`border-b border-white/10 dark:border-white/5 transition-all duration-500 delay-${500 + index * 100} ${
                                index % 2 === 0 ? 'bg-transparent' : 'bg-white/5 dark:bg-white/5'
                            } ${isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'}`}>
                                <td className="px-6 py-4 text-left font-medium text-muted-foreground pl-8 dark:text-white/70">
                                    {version}
                                </td>
                                {platforms.map((platform) => {
                                    const status = platform.versions[version] || 'not-supported';
                                    return (
                                        <td key={platform.id} className="px-4 py-4 text-center border-l border-white/10 dark:border-white/5">
                                            <div className="flex justify-center">
                                                <StatusIcon status={status} />
                                            </div>
                                        </td>
                                    );
                                })}
                            </tr>
                        ))}

                        {addonColumns.length > 0 && (
                            <>
                                <tr className={`border-b border-white/10 dark:border-white/5 transition-all duration-500 delay-900 ${
                                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'
                                }`}>
                                    <td className="px-6 py-4 text-left font-bold text-foreground bg-white/10 dark:bg-white/10 dark:text-white/80">
                                        Addons
                                    </td>
                                    {platforms.map((platform) => (
                                        <td key={platform.id} className="px-4 py-4 text-center border-l border-white/10 dark:border-white/5 bg-white/10 dark:bg-white/10">
                                        </td>
                                    ))}
                                </tr>

                                {addonColumns.map((addon, index) => (
                                    <tr key={addon} className={`border-b border-white/10 dark:border-white/5 transition-all duration-500 delay-${1000 + index * 100} ${
                                        index % 2 === 0 ? 'bg-transparent' : 'bg-white/5 dark:bg-white/5'
                                    } ${isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'}`}>
                                        <td className="px-6 py-4 text-left font-medium text-muted-foreground pl-8 dark:text-white/70">
                                            {addon}
                                        </td>
                                        {platforms.map((platform) => {
                                            const status = platform.addons[addon] || 'not-supported';
                                            return (
                                                <td key={platform.id} className="px-4 py-4 text-center border-l border-white/10 dark:border-white/5">
                                                    <div className="flex justify-center">
                                                        <StatusIcon status={status} />
                                                    </div>
                                                </td>
                                            );
                                        })}
                                    </tr>
                                ))}
                            </>
                        )}
                        </tbody>
                    </table>
                </div>

                <div className={`px-6 py-4 bg-white/10 dark:bg-white/10 border-t border-white/20 dark:border-white/10 transition-all duration-500 delay-1200 ${
                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'
                }`}>
                    <div className="flex flex-wrap gap-8 text-sm text-muted-foreground dark:text-white/60">
                        <div className="flex items-center gap-2">
                            <Check className="w-4 h-4 text-emerald-500" />
                            <span>Supported</span>
                        </div>
                        <div className="flex items-center gap-2">
                            <X className="w-4 h-4 text-red-500" />
                            <span>Not Supported</span>
                        </div>
                        <div className="flex items-center gap-2">
                            <AlertTriangle className="w-4 h-4 text-amber-500" />
                            <span>Partial Support</span>
                        </div>
                        <div className="flex items-center gap-2">
                            <Minus className="w-4 h-4 text-gray-400" />
                            <span>Not Possible</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
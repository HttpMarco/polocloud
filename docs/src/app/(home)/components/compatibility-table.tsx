import { Check, X, AlertTriangle, Minus } from 'lucide-react';
import Image from 'next/image';
import { useEffect, useState } from 'react';
import { motion } from 'framer-motion';

interface CompatibilityData {
    platform: string;
    '1.7-1.12': 'supported' | 'not-supported' | 'partial' | 'not-possible';
    '1.12-1.16': 'supported' | 'not-supported' | 'partial' | 'not-possible';
    '1.18-1.19': 'supported' | 'not-supported' | 'partial' | 'not-possible';
    '1.20+': 'supported' | 'not-supported' | 'partial' | 'not-possible';
    'Severmobs': 'supported' | 'not-supported' | 'partial' | 'not-possible';
    'Signs': 'supported' | 'not-supported' | 'partial' | 'not-possible';
}

const compatibilityData: CompatibilityData[] = [
    {
        platform: 'Vanilla',
        '1.7-1.12': 'supported',
        '1.12-1.16': 'supported',
        '1.18-1.19': 'supported',
        '1.20+': 'supported',
        'Severmobs': 'not-possible',
        'Signs': 'not-possible',
    },
    {
        platform: 'Fabric',
        '1.7-1.12': 'not-possible',
        '1.12-1.16': 'partial',
        '1.18-1.19': 'supported',
        '1.20+': 'supported',
        'Severmobs': 'supported',
        'Signs': 'supported',
    },
    {
        platform: 'Spigot',
        '1.7-1.12': 'supported',
        '1.12-1.16': 'supported',
        '1.18-1.19': 'supported',
        '1.20+': 'supported',
        'Severmobs': 'supported',
        'Signs': 'supported',
    },
    {
        platform: 'Paper',
        '1.7-1.12': 'supported',
        '1.12-1.16': 'supported',
        '1.18-1.19': 'supported',
        '1.20+': 'supported',
        'Severmobs': 'supported',
        'Signs': 'supported',
    },
    {
        platform: 'Purpur',
        '1.7-1.12': 'not-possible',
        '1.12-1.16': 'partial',
        '1.18-1.19': 'supported',
        '1.20+': 'supported',
        'Severmobs': 'supported',
        'Signs': 'supported',
    },
    {
        platform: 'Limbo',
        '1.7-1.12': 'not-possible',
        '1.12-1.16': 'not-possible',
        '1.18-1.19': 'supported',
        '1.20+': 'supported',
        'Severmobs': 'not-possible',
        'Signs': 'not-possible',
    },
    {
        platform: 'Leaf',
        '1.7-1.12': 'not-possible',
        '1.12-1.16': 'not-possible',
        '1.18-1.19': 'not-possible',
        '1.20+': 'supported',
        'Severmobs': 'supported',
        'Signs': 'supported',
    },
    {
        platform: 'Pumpkin',
        '1.7-1.12': 'not-possible',
        '1.12-1.16': 'not-possible',
        '1.18-1.19': 'not-possible',
        '1.20+': 'partial',
        'Severmobs': 'not-supported',
        'Signs': 'not-supported',
    },
];

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

const PlatformIcon = ({ platform }: { platform: string }) => {
    const getIconPath = (platformName: string) => {
        const iconMap: { [key: string]: string } = {
            'Fabric': '/fabric.png',
            'Spigot': '/spigot.svg',
            'Paper': '/paper.svg',
            'Purpur': '/purpur.svg',
            'Limbo': '/limbo.jpg',
            'Leaf': '/leaf.svg',
            'Pumpkin': '/pumkin.svg',
        };
        return iconMap[platformName] || null;
    };

    const iconPath = getIconPath(platform);
    
    if (!iconPath) {
        return null;
    }

    return (
        <div className="w-5 h-5 mr-2 flex-shrink-0">
            <Image
                src={iconPath}
                alt={`${platform} icon`}
                width={20}
                height={20}
                className="w-full h-full object-contain"
                onError={(e) => {
                    console.warn(`Failed to load icon for ${platform}:`, iconPath);
                    e.currentTarget.style.display = 'none';
                }}
            />
        </div>
    );
};

export function CompatibilityTable() {
    const platforms = ['Vanilla', 'Fabric', 'Spigot', 'Paper', 'Purpur', 'Limbo', 'Leaf', 'Pumpkin'];
    const versionColumns = ['1.7-1.12', '1.12-1.16', '1.18-1.19', '1.20+'];
    const addonColumns = ['Severmobs', 'Signs'];

    return (
        <motion.div
            className="w-full max-w-7xl mx-auto p-6"
            initial={{ opacity: 0, x: 120 }}
            whileInView={{ opacity: 1, x: 0 }}
            viewport={{ once: true, amount: 0.3 }}
            transition={{ duration: 0.8, ease: 'easeOut' }}
        >
            <motion.div 
                className="bg-white/10 dark:bg-black/20 backdrop-blur-xl border border-white/20 dark:border-white/10 rounded-2xl shadow-2xl overflow-hidden dark:shadow-[0_0_50px_rgba(0,120,255,0.15)] shadow-[0_0_50px_rgba(0,120,255,0.1)] ring-1 ring-white/20 dark:ring-white/10"
                whileHover={{
                    rotateX: 3,
                    rotateY: -18,
                    scale: 1.02,
                    boxShadow: '0 8px 32px 0 rgba(0,80,255,0.18)',
                }}
                transition={{ type: 'spring', stiffness: 300, damping: 24 }}
                style={{
                    background: 'rgba(255,255,255,0.1)',
                    backdropFilter: 'blur(10px)',
                    WebkitBackdropFilter: 'blur(10px)',
                }}
            >
                <div className="overflow-x-auto">
                    <table className="w-full">
                        <thead>
                        <tr className={`border-b border-white/10 dark:border-white/5 transition-all duration-500 delay-200`}>
                            <th className="px-6 py-5 text-left font-semibold text-foreground min-w-[140px] dark:text-white/90 bg-white/5 dark:bg-white/5">
                                Platform
                            </th>
                            {platforms.map((platform, index) => (
                                <th key={platform} className={`px-4 py-5 text-center font-semibold text-foreground border-l border-white/10 dark:border-white/5 dark:text-white/90 bg-white/5 dark:bg-white/5 transition-all duration-500 delay-${300 + index * 50}`}>
                                    <div className={`flex items-center justify-center gap-2 transition-all duration-500 delay-${400 + index * 50}`}>
                                        <PlatformIcon platform={platform} />
                                        <span>{platform}</span>
                                    </div>
                                </th>
                            ))}
                        </tr>
                        </thead>

                        <tbody>
                        <tr className={`border-b border-white/10 dark:border-white/5 transition-all duration-500 delay-400`}>
                            <td className="px-6 py-4 text-left font-bold text-foreground bg-white/10 dark:bg-white/10 dark:text-white/80">
                                Version support
                            </td>
                            {platforms.map((platform) => (
                                <td key={platform} className="px-4 py-4 text-center border-l border-white/10 dark:border-white/5 bg-white/10 dark:bg-white/10">
                                </td>
                            ))}
                        </tr>

                        {versionColumns.map((version, index) => (
                            <tr
                                key={version}
                                className={`border-b border-white/10 dark:border-white/5 transition-all duration-500 delay-${500 + index * 100} ${
                                index % 2 === 0 ? 'bg-transparent' : 'bg-white/5 dark:bg-white/5'
                                }`}
                            >
                                <td className="px-6 py-4 text-left font-medium text-muted-foreground pl-8 dark:text-white/70">
                                    {version}
                                </td>
                                {platforms.map((platform) => {
                                    const data = compatibilityData.find(d => d.platform === platform);
                                    return (
                                        <td key={platform} className="px-4 py-4 text-center border-l border-white/10 dark:border-white/5">
                                            <div className="flex justify-center">
                                                <StatusIcon status={data?.[version as keyof CompatibilityData] as 'supported' | 'not-supported' | 'partial' | 'not-possible'} />
                                            </div>
                                        </td>
                                    );
                                })}
                            </tr>
                        ))}

                        <tr className={`border-b border-white/10 dark:border-white/5 transition-all duration-500 delay-900`}>
                            <td className="px-6 py-4 text-left font-bold text-foreground bg-white/10 dark:bg-white/10 dark:text-white/80">
                                Addons
                            </td>
                            {platforms.map((platform) => (
                                <td key={platform} className="px-4 py-4 text-center border-l border-white/10 dark:border-white/5 bg-white/10 dark:bg-white/10">
                                </td>
                            ))}
                        </tr>

                        {addonColumns.map((addon, index) => (
                            <tr
                                key={addon}
                                className={`border-b border-white/10 dark:border-white/5 transition-all duration-500 delay-${1000 + index * 100} ${
                                index % 2 === 0 ? 'bg-transparent' : 'bg-white/5 dark:bg-white/5'
                                }`}
                            >
                                <td className="px-6 py-4 text-left font-medium text-muted-foreground pl-8 dark:text-white/70">
                                    {addon}
                                </td>
                                {platforms.map((platform) => {
                                    const data = compatibilityData.find(d => d.platform === platform);
                                    return (
                                        <td key={platform} className="px-4 py-4 text-center border-l border-white/10 dark:border-white/5">
                                            <div className="flex justify-center">
                                                <StatusIcon status={data?.[addon as keyof CompatibilityData] as 'supported' | 'not-supported' | 'partial' | 'not-possible'} />
                                            </div>
                                        </td>
                                    );
                                })}
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>

                <div className={`px-6 py-4 bg-white/10 dark:bg-white/10 border-t border-white/20 dark:border-white/10 transition-all duration-500 delay-1200`}>
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
            </motion.div>
        </motion.div>
    );
}
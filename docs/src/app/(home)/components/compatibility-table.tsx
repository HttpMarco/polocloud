import { Check, X, AlertTriangle, Minus } from 'lucide-react';
import Image from 'next/image';

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
        'Severmobs': 'not-supported',
        'Signs': 'not-supported',
    },
    {
        platform: 'Farbric',
        '1.7-1.12': 'not-supported',
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
        '1.7-1.12': 'not-supported',
        '1.12-1.16': 'not-supported',
        '1.18-1.19': 'partial',
        '1.20+': 'supported',
        'Severmobs': 'supported',
        'Signs': 'supported',
    },
    {
        platform: 'Limbo',
        '1.7-1.12': 'not-supported',
        '1.12-1.16': 'not-supported',
        '1.18-1.19': 'supported',
        '1.20+': 'supported',
        'Severmobs': 'not-supported',
        'Signs': 'not-supported',
    },
    {
        platform: 'Leaf',
        '1.7-1.12': 'not-supported',
        '1.12-1.16': 'not-supported',
        '1.18-1.19': 'not-supported',
        '1.20+': 'supported',
        'Severmobs': 'supported',
        'Signs': 'supported',
    },
    {
        platform: 'Pumkin',
        '1.7-1.12': 'not-supported',
        '1.12-1.16': 'not-supported',
        '1.18-1.19': 'not-supported',
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
            'Farbric': '/fabric.png',
            'Spigot': '/spigot.svg',
            'Paper': '/paper.svg',
            'Purpur': '/purpur.svg',
            'Limbo': '/limbo.jpg',
            'Leaf': '/leaf.svg',
            'Pumkin': '/pumkin.svg',
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
            />
        </div>
    );
};

export function CompatibilityTable() {
    const platforms = ['Vanilla', 'Farbric', 'Spigot', 'Paper', 'Purpur', 'Limbo', 'Leaf', 'Pumkin'];
    const versionColumns = ['1.7-1.12', '1.12-1.16', '1.18-1.19', '1.20+'];
    const addonColumns = ['Severmobs', 'Signs'];

    return (
        <div className="w-full max-w-7xl mx-auto p-6">
            <div className="bg-card/50 backdrop-blur-sm border border-border/50 rounded-xl shadow-lg overflow-hidden dark:bg-black/80 dark:border-white/10 shadow-[0_0_25px_rgba(0,120,255,0.3)] dark:shadow-[0_0_25px_rgba(0,120,255,0.2)] ring-1 ring-[rgba(0,120,255,0.3)] dark:ring-[rgba(0,120,255,0.2)]">
                <div className="overflow-x-auto">
                    <table className="w-full">
                        {/* Platform Headers */}
                        <thead>
                        <tr className="border-b border-border/50 dark:border-white/10">
                            <th className="px-6 py-5 text-left font-semibold text-foreground min-w-[140px] dark:text-white/90">
                                Platform
                            </th>
                            {platforms.map((platform) => (
                                <th key={platform} className="px-4 py-5 text-center font-semibold text-foreground border-l border-border/30 dark:border-white/5 dark:text-white/90">
                                    <div className="flex items-center justify-center gap-2">
                                        <PlatformIcon platform={platform} />
                                        <span>{platform}</span>
                                    </div>
                                </th>
                            ))}
                        </tr>
                        </thead>

                        <tbody>
                        {/* Version Support Section */}
                        <tr className="border-b border-border/30 dark:border-white/5">
                            <td className="px-6 py-4 text-left font-bold text-foreground bg-muted/30 dark:bg-white/5 dark:text-white/80">
                                Version support
                            </td>
                            {platforms.map((platform) => (
                                <td key={platform} className="px-4 py-4 text-center border-l border-border/30 bg-muted/30 dark:border-white/5 dark:bg-white/5">
                                </td>
                            ))}
                        </tr>

                        {versionColumns.map((version, index) => (
                            <tr key={version} className={`border-b border-border/30 hover:bg-muted/20 transition-colors dark:border-white/5 dark:hover:bg-white/5 ${
                                index % 2 === 0 ? 'bg-transparent' : 'bg-muted/10 dark:bg-white/[0.02]'
                            }`}>
                                <td className="px-6 py-4 text-left font-medium text-muted-foreground pl-8 dark:text-white/70">
                                    {version}
                                </td>
                                {platforms.map((platform) => {
                                    const data = compatibilityData.find(d => d.platform === platform);
                                    return (
                                        <td key={platform} className="px-4 py-4 text-center border-l border-border/30 dark:border-white/5">
                                            <div className="flex justify-center">
                                                <StatusIcon status={data?.[version as keyof CompatibilityData] as 'supported' | 'not-supported' | 'partial' | 'not-possible'} />
                                            </div>
                                        </td>
                                    );
                                })}
                            </tr>
                        ))}

                        {/* Addons Section */}
                        <tr className="border-b border-border/30 dark:border-white/5">
                            <td className="px-6 py-4 text-left font-bold text-foreground bg-muted/30 dark:bg-white/5 dark:text-white/80">
                                Addons
                            </td>
                            {platforms.map((platform) => (
                                <td key={platform} className="px-4 py-4 text-center border-l border-border/30 bg-muted/30 dark:border-white/5 dark:bg-white/5">
                                </td>
                            ))}
                        </tr>

                        {addonColumns.map((addon, index) => (
                            <tr key={addon} className={`border-b border-border/30 hover:bg-muted/20 transition-colors dark:border-white/5 dark:hover:bg-white/5 ${
                                index % 2 === 0 ? 'bg-transparent' : 'bg-muted/10 dark:bg-white/[0.02]'
                            }`}>
                                <td className="px-6 py-4 text-left font-medium text-muted-foreground pl-8 dark:text-white/70">
                                    {addon}
                                </td>
                                {platforms.map((platform) => {
                                    const data = compatibilityData.find(d => d.platform === platform);
                                    return (
                                        <td key={platform} className="px-4 py-4 text-center border-l border-border/30 dark:border-white/5">
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

                {/* Legend */}
                <div className="px-6 py-4 bg-muted/20 border-t border-border/50 dark:bg-white/5 dark:border-white/10">
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
'use client';
import { useEffect, useState, useRef } from 'react';
import { Download, ExternalLink, Package, Star, Zap, Shield, Code, Rocket } from 'lucide-react';

interface Addon {
    name: string;
    description: string;
    version: string;
    category: 'bukkit' | 'proxy';
    downloadUrl: string;
    githubUrl: string;
    features: string[];
    icon: React.ComponentType<{ className?: string }>;
    color: string;
    gradient: string;
}

const addons: Addon[] = [
    {
        name: 'polocloud-hub',
        description: 'Advanced hub management system for PoloCloud servers with seamless server switching and lobby management.',
        version: '1.0.0',
        category: 'proxy',
        downloadUrl: 'https://github.com/HttpMarco/polocloud-hub/releases',
        githubUrl: 'https://github.com/HttpMarco/polocloud/tree/master/addons/hub',
        features: [
            'Server hub management',
            'Automatic server switching',
            'Lobby system integration',
            'Player distribution'
        ],
        icon: Rocket,
        color: 'text-blue-400',
        gradient: 'from-blue-500/20 to-blue-600/20'
    },
    {
        name: 'polocloud-proxy',
        description: 'Enhanced proxy addon with custom MOTD, Tablist integration, and advanced player management features.',
        version: '1.0.0',
        category: 'proxy',
        downloadUrl: 'https://github.com/HttpMarco/polocloud-proxy/releases',
        githubUrl: 'https://github.com/HttpMarco/polocloud/tree/master/addons/signs',
        features: [
            'Custom Tablist',
            'Dynamic MOTD',
            'Player management',
            'Server status display'
        ],
        icon: Shield,
        color: 'text-green-400',
        gradient: 'from-green-500/20 to-green-600/20'
    },
];

const AddonCard = ({ addon }: { addon: Addon }) => {
    const [isHovered, setIsHovered] = useState(false);

    const getCategoryColor = (category: string) => {
        switch (category) {
            case 'bukkit': return 'bg-green-500/20 border-green-500/30 text-green-400';
            case 'proxy': return 'bg-blue-500/20 border-blue-500/30 text-blue-400';
            default: return 'bg-gray-500/20 border-gray-500/30 text-gray-400';
        }
    };

    const getCategoryLabel = (category: string) => {
        switch (category) {
            case 'bukkit': return 'Bukkit';
            case 'proxy': return 'Proxy';
            default: return 'Addon';
        }
    };

    return (
        <div
            className={`group relative bg-gradient-to-br from-card/30 via-card/20 to-card/30 backdrop-blur-xl border border-border/50 rounded-xl p-6 transition-all duration-500 hover:scale-105 hover:-translate-y-2 shadow-xl hover:shadow-2xl`}
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
        >
            <div className={`absolute inset-0 bg-gradient-to-br ${addon.gradient} opacity-0 group-hover:opacity-100 transition-opacity duration-500 rounded-xl`} />
            <div className="absolute top-0 right-0 w-24 h-24 bg-primary/10 rounded-full blur-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-500" />

            <div className="relative z-10">
                <div className={`absolute top-4 right-4 px-3 py-1 rounded-full text-xs font-semibold border backdrop-blur-sm ${getCategoryColor(addon.category)}`}>
                    {getCategoryLabel(addon.category)}
                </div>

                <div className="mb-4">
                    <div className="flex items-center gap-3 mb-3">
                        <div className={`w-12 h-12 bg-gradient-to-br ${addon.gradient} rounded-xl flex items-center justify-center shadow-lg group-hover:scale-110 transition-transform duration-300`}>
                            <addon.icon className={`w-6 h-6 ${addon.color}`} />
                        </div>
                        <div>
                            <h3 className="text-lg md:text-xl font-black text-foreground dark:text-white group-hover:text-primary transition-colors duration-300">
                                {addon.name}
                            </h3>
                            <div className="flex items-center gap-2">
                <span className="text-xs text-muted-foreground dark:text-white/60">
                  v{addon.version}
                </span>
                                <div className="w-1.5 h-1.5 bg-green-400 rounded-full animate-pulse" />
                            </div>
                        </div>
                    </div>
                    <p className="text-sm text-muted-foreground dark:text-white/70 leading-relaxed">
                        {addon.description}
                    </p>
                </div>

                <div className="mb-6">
                    <h4 className="text-sm font-bold text-foreground dark:text-white mb-3 flex items-center gap-2">
                        <Zap className="w-4 h-4 text-primary" />
                        Key Features
                    </h4>
                    <div className="grid grid-cols-1 gap-2">
                        {addon.features.map((feature, index) => (
                            <div key={index} className="flex items-center gap-2 text-muted-foreground dark:text-white/70 group/feature">
                                <div className="w-1.5 h-1.5 bg-primary rounded-full group-hover/feature:scale-150 transition-transform duration-300" />
                                <span className="text-xs font-medium">{feature}</span>
                            </div>
                        ))}
                    </div>
                </div>

                <div className="flex gap-3">
                    <button
                        onClick={() => window.open(addon.downloadUrl, '_blank')}
                        className="flex-1 inline-flex items-center justify-center gap-2 px-4 py-2 bg-gradient-to-r from-primary to-primary/90 hover:from-primary/90 hover:to-primary text-primary-foreground rounded-lg text-xs font-semibold transition-all duration-300 hover:scale-105 shadow-lg hover:shadow-xl group/btn"
                    >
                        <Download className="w-3 h-3 group-hover/btn:animate-bounce" />
                        Download
                    </button>
                    <button
                        onClick={() => window.open(addon.githubUrl, '_blank')}
                        className="inline-flex items-center justify-center gap-2 px-4 py-2 bg-card/50 hover:bg-card border border-border/50 rounded-lg text-xs font-semibold transition-all duration-300 hover:scale-105 group/btn"
                    >
                        <ExternalLink className="w-3 h-3 group-hover/btn:translate-x-1 transition-transform" />
                        GitHub
                    </button>
                </div>
            </div>

            <div className={`absolute inset-0 rounded-xl border-2 border-primary/20 opacity-0 transition-opacity duration-500 pointer-events-none ${isHovered ? 'opacity-100' : ''}`} />
        </div>
    );
};

export function AddonsSection() {
    const [isVisible, setIsVisible] = useState(false);
    const sectionRef = useRef<HTMLElement>(null);

    useEffect(() => {
        const observer = new IntersectionObserver(
            ([entry]) => {
                if (entry.isIntersecting) {
                    setIsVisible(true);
                }
            },
            { threshold: 0.1, rootMargin: '0px 0px -100px 0px' }
        );

        if (sectionRef.current) {
            observer.observe(sectionRef.current);
        }

        return () => observer.disconnect();
    }, []);

    return (
        <section ref={sectionRef} className="relative py-32 overflow-hidden">
            <div className="absolute top-0 left-0 right-0 h-48 bg-gradient-to-b from-background via-background/95 via-background/80 to-transparent" />
            <div className="absolute inset-0 bg-gradient-to-b from-background via-muted/5 to-muted/5" />
            <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]" />

            <div className="absolute top-20 left-10 w-32 h-32 bg-primary/5 rounded-full blur-3xl animate-pulse" />
            <div className="absolute bottom-20 right-10 w-40 h-40 bg-primary/5 rounded-full blur-3xl animate-pulse delay-1000" />

            <div className="relative container mx-auto px-6">
                <div className={`text-center mb-20 transition-all duration-1000 ease-out ${
                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                }`}>
                    <h2 className={`text-3xl md:text-4xl lg:text-5xl font-black mb-8 bg-gradient-to-r from-foreground via-primary to-foreground bg-clip-text text-transparent transition-all duration-1000 delay-200 ${
                        isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                    }`}>
                        Addons
                    </h2>
                    <p className={`text-base md:text-lg text-muted-foreground max-w-4xl mx-auto leading-relaxed transition-all duration-1000 delay-400 ${
                        isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                    }`}>
                        Extend your PoloCloud experience with our official addons. Each addon is designed to enhance your server management capabilities and provide powerful features.
                    </p>
                </div>
                <div className={`grid grid-cols-1 lg:grid-cols-2 gap-8 transition-all duration-1000 delay-600 ${
                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                }`}>
                    {addons.map((addon, index) => (
                        <div
                            key={addon.name}
                            className={`transition-all duration-1000 delay-${(index + 1) * 200} ${
                                isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                            }`}
                        >
                            <AddonCard addon={addon} />
                        </div>
                    ))}
                </div>

                <div className={`text-center mt-16 transition-all duration-1000 delay-1000 ${
                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                }`}>
                    <div className="bg-gradient-to-r from-primary/10 to-primary/5 border border-primary/20 rounded-2xl p-8 max-w-2xl mx-auto">
                        <h3 className="text-2xl font-bold text-foreground dark:text-white mb-4">
                            Need a Custom Addon?
                        </h3>
                        <p className="text-muted-foreground dark:text-white/70 mb-6">
                            We can develop custom addons tailored to your specific needs. Contact us to discuss your requirements.
                        </p>
                        <a
                            href="https://discord.polocloud.de"
                            target="_blank"
                            rel="noopener noreferrer"
                            className="inline-flex items-center gap-3 px-8 py-4 bg-gradient-to-r from-primary to-primary/90 hover:from-primary/90 hover:to-primary text-primary-foreground rounded-xl font-semibold text-lg transition-all duration-300 shadow-[0_0_20px_rgba(0,120,255,0.3)] hover:shadow-[0_0_30px_rgba(0,120,255,0.5)] hover:scale-105 group"
                        >
                            <Code className="w-5 h-5 group-hover:rotate-12 transition-transform" />
                            Request Custom Addon
                            <ExternalLink className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
                        </a>
                    </div>
                </div>
            </div>

            <div className="absolute bottom-0 left-0 right-0 h-48 bg-gradient-to-t from-background via-background/95 via-background/80 to-transparent" />
        </section>
    );
} 
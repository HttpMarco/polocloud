'use client';
import { useEffect, useState, useRef } from 'react';
import Link from 'next/link';
import { ArrowLeft, Calendar, Star, Package, Code, Server } from 'lucide-react';

interface RoadmapItem {
    id: string;
    title: string;
    description: string;
    category: 'ui' | 'platforms' | 'bot' | 'addons';
    estimatedDate?: string;
    tags?: string[];
}

interface RoadmapColumn {
    id: string;
    title: string;
    color: string;
    items: RoadmapItem[];
}

const roadmapData: RoadmapColumn[] = [
    {
        id: 'no-status',
        title: 'No Status',
        color: 'border-orange-500',
        items: [
            {
                id: 'threaded-server-startup',
                title: 'Threaded server startup',
                description: '',
                category: 'platforms',
                tags: ['improvement', 'prototype-5'],
            },
            {
                id: 'terminal-command-types',
                title: 'Terminal Command types lay out',
                description: '',
                category: 'ui',
                tags: ['improvement', 'prototype-5'],
            },
        ]
    },
    {
        id: 'todo',
        title: 'Todo',
        color: 'border-sky-500',
        items: [
            {
                id: 'status-lines',
                title: 'Status Lines',
                description: '',
                category: 'ui',
                tags: ['new requirement', 'prototype-5'],
            },
            {
                id: 'gate-support',
                title: 'Gate Support',
                description: '',
                category: 'addons',
                tags: ['new requirement', 'prototype-5'],
            },
            {
                id: 'fabric-forwarding',
                title: 'Fabric needs forwarding',
                description: '',
                category: 'platforms',
                tags: ['improvement', 'prototype-5'],
            },
            {
                id: 'forge-mod-support',
                title: 'Support Forge and any Mod Servers',
                description: '',
                category: 'platforms',
                tags: ['improvement', 'new requirement', 'prototype-5'],
            },
            {
                id: 'reduce-launcher-size',
                title: 'Reduce launcher file size',
                description: '',
                category: 'ui',
                tags: ['improvement', 'prototype-5'],
            },
        ]
    },
    {
        id: 'in-progress',
        title: 'In Progress',
        color: 'border-purple-500',
        items: []
    },
    {
        id: 'quality-testing',
        title: 'Quality-Testing',
        color: 'border-pink-500',
        items: []
    },
    {
        id: 'done',
        title: 'Done',
        color: 'border-green-500',
        items: [
            {
                id: 'shutdown-message',
                title: 'wrong shutdown message for static services',
                description: '',
                category: 'platforms',
                tags: ['improvement', 'prototype-5'],
            },
            {
                id: 'group-delete-error',
                title: 'Error when deleting a group and recreating it with the same name',
                description: '',
                category: 'ui',
                tags: ['bug', 'prototype-5'],
            },
            {
                id: 'crowdin-subscription',
                title: 'We need to update crowdin subscription to make it work again',
                description: '',
                category: 'addons',
                tags: ['bug', 'prototype-5'],
            },
            {
                id: 'github-sponsors',
                title: 'Setup GitHub Sponsors',
                description: '',
                category: 'addons',
                tags: ['new requirement', 'prototype-5'],
            },
            {
                id: 'cloud-port-ping',
                title: 'Error while ping cloud port in Minecraft',
                description: '',
                category: 'platforms',
                tags: ['bug', 'improvement', 'prototype-5'],
            },
        ]
    }
];

const getCategoryIcon = (category: string) => {
    switch (category) {
        case 'ui': return <Star className="w-4 h-4" />;
        case 'platforms': return <Server className="w-4 h-4" />;
        case 'bot': return <Code className="w-4 h-4" />;
        case 'addons': return <Package className="w-4 h-4" />;
        default: return <Code className="w-4 h-4" />;
    }
};

const getCategoryColor = (category: string) => {
    switch (category) {
        case 'ui': return 'bg-blue-500/20 border-blue-500/30 text-blue-400';
        case 'platforms': return 'bg-purple-500/20 border-purple-500/30 text-purple-400';
        case 'bot': return 'bg-green-500/20 border-green-500/30 text-green-400';
        case 'addons': return 'bg-orange-500/20 border-orange-500/30 text-orange-400';
        default: return 'bg-gray-500/20 border-gray-500/30 text-gray-400';
    }
};

const RoadmapCard = ({ item }: { item: RoadmapItem }) => {
    return (
        <div className="bg-card/40 backdrop-blur-sm border border-border/40 rounded-xl p-4 mb-3 hover:bg-card/60 transition-all duration-300 hover:shadow-lg hover:border-border/60">
            {item.tags && (
                <div className="flex flex-wrap gap-2 mb-2">
                    {item.tags.map((tag) => (
                        <span key={tag} className={
                            tag === 'improvement' ? 'bg-yellow-100 text-yellow-800 px-2 py-0.5 rounded-full text-xs font-semibold' :
                                tag === 'new requirement' ? 'bg-green-100 text-green-800 px-2 py-0.5 rounded-full text-xs font-semibold' :
                                    tag === 'prototype-5' ? 'bg-blue-100 text-blue-800 px-2 py-0.5 rounded-full text-xs font-semibold' :
                                        tag === 'bug' ? 'bg-red-100 text-red-800 px-2 py-0.5 rounded-full text-xs font-semibold' :
                                            'bg-gray-100 text-gray-800 px-2 py-0.5 rounded-full text-xs font-semibold'
                        }>{tag}</span>
                    ))}
                </div>
            )}
            <h3 className="font-bold text-foreground dark:text-white mb-2 text-sm leading-tight">
                {item.title}
            </h3>
            <p className="text-xs text-muted-foreground dark:text-white/70 mb-3 leading-relaxed">
                {item.description}
            </p>
            {item.estimatedDate && (
                <div className="flex items-center gap-1.5 text-xs text-muted-foreground dark:text-white/60 bg-muted/30 px-2 py-1 rounded-md w-fit">
                    <Calendar className="w-3 h-3" />
                    <span className="font-medium">{item.estimatedDate}</span>
                </div>
            )}
        </div>
    );
};

const RoadmapColumn = ({ column }: { column: RoadmapColumn }) => {
    return (
        <div className="flex flex-col h-full">
            <div className={`mb-6 p-4 bg-card/50 backdrop-blur-sm border border-border/40 rounded-xl ${column.color} border-t-4 shadow-lg`}>
                <h2 className="font-bold text-foreground dark:text-white text-sm uppercase tracking-wider mb-1">
                    {column.title}
                </h2>
                <div className="text-xs text-muted-foreground dark:text-white/60 font-medium">
                    {column.items.length} items
                </div>
            </div>

            <div className="flex-1 space-y-3 max-h-[600px] overflow-y-auto pr-2 scrollbar-thin scrollbar-thumb-border/50 scrollbar-track-transparent">
                {column.items.map((item) => (
                    <RoadmapCard key={item.id} item={item} />
                ))}
            </div>
        </div>
    );
};

export function RoadmapContent() {
    const [isVisible, setIsVisible] = useState(false);
    const contentRef = useRef<HTMLElement>(null);

    useEffect(() => {
        const observer = new IntersectionObserver(
            ([entry]) => {
                if (entry.isIntersecting) {
                    setIsVisible(true);
                }
            },
            { threshold: 0.1, rootMargin: '0px 0px -100px 0px' }
        );

        if (contentRef.current) {
            observer.observe(contentRef.current);
        }

        return () => observer.disconnect();
    }, []);

    return (
        <section ref={contentRef} className="relative py-32 overflow-hidden">
            <div className="absolute inset-0 bg-gradient-to-b from-background via-muted/5 to-muted/5" />

            <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]" />

            <div className="relative container mx-auto px-6">
                <div className={`mb-8 transition-all duration-1000 ease-out ${
                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                }`}>
                    <Link
                        href="/"
                        className="inline-flex items-center gap-2 px-4 py-2 bg-card/50 hover:bg-card border border-border/50 rounded-lg text-sm font-medium transition-all duration-300 hover:scale-105 backdrop-blur-sm"
                    >
                        <ArrowLeft className="w-4 h-4" />
                        Back to Home
                    </Link>
                </div>

                <div className={`text-center mb-16 transition-all duration-1000 ease-out ${
                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                }`}>
                    <h1 className={`text-4xl md:text-5xl font-bold mb-6 bg-gradient-to-r from-foreground to-muted-foreground bg-clip-text text-transparent transition-all duration-1000 delay-200 ${
                        isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                    }`}>
                        Development Roadmap
                    </h1>
                    <p className={`text-xl text-muted-foreground max-w-3xl mx-auto leading-relaxed transition-all duration-1000 delay-400 ${
                        isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                    }`}>
                        Track our progress and see what&apos;s coming next for PoloCloud
                    </p>
                </div>

                <div className={`mb-12 transition-all duration-1000 delay-500 ${
                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                }`}>
                    <div className="bg-card/40 backdrop-blur-sm border border-border/40 rounded-2xl p-8 shadow-lg">
                        <h3 className="font-bold text-foreground dark:text-white mb-6 text-lg">Tags</h3>
                        <div className="flex flex-wrap gap-6">
                            <div className="flex items-center gap-3">
                                <span className="bg-yellow-100 text-yellow-800 px-3 py-1 rounded-full text-sm font-semibold">improvement</span>
                            </div>
                            <div className="flex items-center gap-3">
                                <span className="bg-green-100 text-green-800 px-3 py-1 rounded-full text-sm font-semibold">new requirement</span>
                            </div>
                            <div className="flex items-center gap-3">
                                <span className="bg-blue-100 text-blue-800 px-3 py-1 rounded-full text-sm font-semibold">prototype-5</span>
                            </div>
                            <div className="flex items-center gap-3">
                                <span className="bg-red-100 text-red-800 px-3 py-1 rounded-full text-sm font-semibold">bug</span>
                            </div>
                        </div>
                    </div>
                </div>

                <div className={`transition-all duration-1000 delay-600 ${
                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                }`}>
                    <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 2xl:grid-cols-5 gap-8">
                        {roadmapData.map((column, index) => (
                            <div
                                key={column.id}
                                className={`transition-all duration-1000 delay-${(index + 1) * 100} ${
                                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                                }`}
                            >
                                <RoadmapColumn column={column} />
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </section>
    );
}
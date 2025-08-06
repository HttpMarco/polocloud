'use client';
import { useEffect, useState, useRef } from 'react';
import Link from 'next/link';
import { ArrowLeft, Calendar, Star, Package, Code, Server } from 'lucide-react';

interface RoadmapItem {
    id: string;
    title: string;
    description: string;
    category: 'ui' | 'platforms' | 'bot' | 'addons';
    priority: 'high' | 'medium' | 'low';
    estimatedDate?: string;
}

interface RoadmapColumn {
    id: string;
    title: string;
    color: string;
    items: RoadmapItem[];
}

const roadmapData: RoadmapColumn[] = [
    {
        id: 'future',
        title: 'IN PLANNING (FUTURE)',
        color: 'border-yellow-500',
        items: []
    },
    {
        id: 'soon',
        title: 'IN PLANNING (SOON)',
        color: 'border-sky-500',
        items: []
    },
    {
        id: 'in-progress',
        title: 'IN PROGRESS',
        color: 'border-purple-500',
        items: []
    },
    {
        id: 'coming-soon',
        title: 'COMING SOON',
        color: 'border-orange-500',
        items: []
    },
    {
        id: 'published',
        title: 'PUBLISHED',
        color: 'border-green-500',
        items: []
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

const getPriorityColor = (priority: string) => {
    switch (priority) {
        case 'high': return 'bg-red-500/20 text-red-400';
        case 'medium': return 'bg-yellow-500/20 text-yellow-400';
        case 'low': return 'bg-green-500/20 text-green-400';
        default: return 'bg-gray-500/20 text-gray-400';
    }
};

const RoadmapCard = ({ item }: { item: RoadmapItem }) => {
    return (
        <div className="bg-card/40 backdrop-blur-sm border border-border/40 rounded-xl p-4 mb-3 hover:bg-card/60 transition-all duration-300 hover:shadow-lg hover:border-border/60">
            <div className="flex items-center justify-between mb-3">
                <div className={`inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-xs font-medium border ${getCategoryColor(item.category)}`}>
                    {getCategoryIcon(item.category)}
                    <span className="capitalize font-semibold">{item.category}</span>
                </div>
                <div className={`px-2.5 py-1 rounded-full text-xs font-medium ${getPriorityColor(item.priority)}`}>
                    {item.priority.toUpperCase()}
                </div>
            </div>

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
                        <h3 className="font-bold text-foreground dark:text-white mb-6 text-lg">Categories</h3>
                        <div className="flex flex-wrap gap-6">
                            <div className="flex items-center gap-3">
                                <div className="w-4 h-4 bg-blue-500 rounded-full shadow-sm"></div>
                                <span className="text-sm text-muted-foreground dark:text-white/70 font-medium">UI</span>
                            </div>
                            <div className="flex items-center gap-3">
                                <div className="w-4 h-4 bg-purple-500 rounded-full shadow-sm"></div>
                                <span className="text-sm text-muted-foreground dark:text-white/70 font-medium">Platforms</span>
                            </div>
                            <div className="flex items-center gap-3">
                                <div className="w-4 h-4 bg-green-500 rounded-full shadow-sm"></div>
                                <span className="text-sm text-muted-foreground dark:text-white/70 font-medium">Bot</span>
                            </div>
                            <div className="flex items-center gap-3">
                                <div className="w-4 h-4 bg-orange-500 rounded-full shadow-sm"></div>
                                <span className="text-sm text-muted-foreground dark:text-white/70 font-medium">Addons</span>
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
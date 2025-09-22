'use client';

import { Star, GitFork, Download, Package, GitCommit } from 'lucide-react';
import { useEffect, useState } from 'react';


interface GitHubStats {
    stars: number;
    forks: number;
    releases: number;
    downloads: number;
    commits: number;
    lastUpdated: string;
}

interface StatCardProps {
    icon: React.ReactNode;
    value: number;
    label: string;
}

const StatCard = ({ icon, value, label }: StatCardProps) => {
    const [displayValue, setDisplayValue] = useState(0);
    const [shouldStartCounting, setShouldStartCounting] = useState(false);

    useEffect(() => {
        const startTimer = setTimeout(() => {
            setShouldStartCounting(true);
        }, 1000);

        return () => clearTimeout(startTimer);
    }, []);

    useEffect(() => {
        if (!shouldStartCounting) return;

        const duration = 800;
        const steps = 30;
        const increment = value / steps;
        const stepDuration = duration / steps;

        let currentValue = 0;
        const timer = setInterval(() => {
            currentValue += increment;
            if (currentValue >= value) {
                setDisplayValue(value);
                clearInterval(timer);
            } else {
                setDisplayValue(Math.floor(currentValue));
            }
        }, stepDuration);

        return () => clearInterval(timer);
    }, [value, shouldStartCounting]);

    return (
        <div className="group flex flex-col items-center justify-center text-center transition-all duration-300 hover:scale-105 transform">
            <div className="flex items-center justify-center w-12 h-12 sm:w-16 sm:h-16 bg-primary/10 rounded-full border border-primary/20 mb-2 sm:mb-4 group-hover:scale-110 transition-transform duration-300 text-primary">
                {icon}
            </div>
            <div className="flex flex-col items-center">
                <span className="text-xl sm:text-2xl md:text-3xl font-bold text-foreground dark:text-white mb-1 sm:mb-2">
                    {displayValue.toLocaleString()}
                </span>
                <span className="text-xs sm:text-sm text-muted-foreground font-medium uppercase tracking-wide">
                    {label}
                </span>
            </div>
        </div>
    );
};

export function GitHubStatsComponent() {
    const [stats, setStats] = useState<GitHubStats | null>(null);
    const [loading, setLoading] = useState(true);


    useEffect(() => {
        const fetchStats = async () => {
            try {
                setLoading(true);


                let response;
                try {
                    response = await fetch('/api/github-stats');
                } catch {
                    console.log('API route failed, using fallback data');
                    throw new Error('API not available');
                }

                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }

                const data = await response.json();

                if (data.error) {
                    throw new Error(data.error);
                }

                setStats(data);
            } catch (err) {
                console.error('Failed to fetch GitHub stats:', err);


                setStats({
                    stars: 61,
                    forks: 22,
                    releases: 4,
                    downloads: 0,
                    commits: 3206,
                    lastUpdated: new Date().toISOString(),
                });
            } finally {
                setLoading(false);
            }
        };

        fetchStats();
    }, []);

    if (loading) {
        return (
            <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-5 gap-6 sm:gap-8 md:gap-12 mb-12 sm:mb-16 justify-items-center">
                {[1, 2, 3, 4, 5].map((i) => (
                    <div key={i} className="flex flex-col items-center justify-center text-center">
                        <div className="w-12 h-12 sm:w-16 sm:h-16 bg-gradient-to-br from-muted/30 to-muted/50 rounded-full mb-2 sm:mb-4 animate-pulse relative overflow-hidden">
                            <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/20 to-transparent animate-pulse" style={{
                                animationDuration: '2s',
                                animationIterationCount: 'infinite',
                                transform: 'translateX(-100%)',
                                animationName: 'shimmer'
                            }}></div>
                        </div>
                        <div className="flex flex-col items-center gap-1 sm:gap-2">
                            <div className="w-16 sm:w-20 h-8 sm:h-10 bg-gradient-to-r from-muted/30 to-muted/50 rounded animate-pulse relative overflow-hidden">
                                <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/20 to-transparent animate-pulse" style={{
                                    animationDuration: '2s',
                                    animationIterationCount: 'infinite',
                                    transform: 'translateX(-100%)',
                                    animationName: 'shimmer'
                                }}></div>
                            </div>
                            <div className="w-20 sm:w-24 h-3 sm:h-4 bg-gradient-to-r from-muted/30 to-muted/50 rounded animate-pulse relative overflow-hidden">
                                <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/20 to-transparent animate-pulse" style={{
                                    animationDuration: '2s',
                                    animationIterationCount: 'infinite',
                                    transform: 'translateX(-100%)',
                                    animationName: 'shimmer'
                                }}></div>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        );
    }

    if (!stats) {
        return (
            <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-5 gap-6 sm:gap-8 md:gap-12 mb-12 sm:mb-16 justify-items-center">
                <div className="flex flex-col items-center justify-center text-center">
                    <div className="flex items-center justify-center w-12 h-12 sm:w-16 sm:h-16 bg-primary/10 rounded-full border border-primary/20 mb-2 sm:mb-4 text-primary">
                        <Star className="w-6 h-6 sm:w-8 sm:h-8" />
                    </div>
                    <div className="flex flex-col items-center">
                        <span className="text-xl sm:text-2xl md:text-3xl font-bold text-foreground dark:text-white mb-1 sm:mb-2">61</span>
                        <span className="text-xs sm:text-sm text-muted-foreground font-medium uppercase tracking-wide">Stars</span>
                    </div>
                </div>
                <div className="flex flex-col items-center justify-center text-center">
                    <div className="flex items-center justify-center w-12 h-12 sm:w-16 sm:h-16 bg-primary/10 rounded-full border border-primary/20 mb-2 sm:mb-4 text-primary">
                        <GitFork className="w-6 h-6 sm:w-8 sm:h-8" />
                    </div>
                    <div className="flex flex-col items-center">
                        <span className="text-xl sm:text-2xl md:text-3xl font-bold text-foreground dark:text-white mb-1 sm:mb-2">22</span>
                        <span className="text-xs sm:text-sm text-muted-foreground font-medium uppercase tracking-wide">Forks</span>
                    </div>
                </div>
                <div className="flex flex-col items-center justify-center text-center">
                    <div className="flex items-center justify-center w-12 h-12 sm:w-16 sm:h-16 bg-primary/10 rounded-full border border-primary/20 mb-2 sm:mb-4 text-primary">
                        <Package className="w-6 h-6 sm:w-8 sm:h-8" />
                    </div>
                    <div className="flex flex-col items-center">
                        <span className="text-xl sm:text-2xl md:text-3xl font-bold text-foreground dark:text-white mb-1 sm:mb-2">4</span>
                        <span className="text-xs sm:text-sm text-muted-foreground font-medium uppercase tracking-wide">Releases</span>
                    </div>
                </div>
                <div className="flex flex-col items-center justify-center text-center">
                    <div className="flex items-center justify-center w-12 h-12 sm:w-16 sm:h-16 bg-primary/10 rounded-full border border-primary/20 mb-2 sm:mb-4 text-primary">
                        <Download className="w-6 h-6 sm:w-8 sm:h-8" />
                    </div>
                    <div className="flex flex-col items-center">
                        <span className="text-xl sm:text-2xl md:text-3xl font-bold text-foreground dark:text-white mb-1 sm:mb-2">0</span>
                        <span className="text-xs sm:text-sm text-muted-foreground font-medium uppercase tracking-wide">Downloads</span>
                    </div>
                </div>
                <div className="flex flex-col items-center justify-center text-center">
                    <div className="flex items-center justify-center w-12 h-12 sm:w-16 sm:h-16 bg-primary/10 rounded-full border border-primary/20 mb-2 sm:mb-4 text-primary">
                        <GitCommit className="w-6 h-6 sm:w-8 sm:h-8" />
                    </div>
                    <div className="flex flex-col items-center">
                        <span className="text-xl sm:text-2xl md:text-3xl font-bold text-foreground dark:text-white mb-1 sm:mb-2">3206</span>
                        <span className="text-xs sm:text-sm text-muted-foreground font-medium uppercase tracking-wide">Commits</span>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-5 gap-6 sm:gap-8 md:gap-12 mb-12 sm:mb-16 justify-items-center">
            <StatCard icon={<Star className="w-6 h-6 sm:w-8 sm:h-8" />} value={stats.stars} label="Stars" />
            <StatCard icon={<GitFork className="w-6 h-6 sm:w-8 sm:h-8" />} value={stats.forks} label="Forks" />
            <StatCard icon={<Package className="w-6 h-6 sm:w-8 sm:h-8" />} value={stats.releases} label="Releases" />
            <StatCard icon={<Download className="w-6 h-6 sm:w-8 sm:h-8" />} value={stats.downloads} label="Downloads" />
            <StatCard icon={<GitCommit className="w-6 h-6 sm:w-8 sm:h-8" />} value={stats.commits} label="Commits" />
        </div>
    );
} 
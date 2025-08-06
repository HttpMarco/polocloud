'use client';

import { Star, GitFork, Download, Package } from 'lucide-react';
import { useEffect, useState } from 'react';

interface GitHubStats {
    stars: number;
    forks: number;
    releases: number;
    downloads: number;
    lastUpdated: string;
}

interface StatCardProps {
    icon: React.ReactNode;
    value: number;
    label: string;
    color: string;
}

const StatCard = ({ icon, value, label, color }: StatCardProps) => {
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
        <div className="flex flex-col items-center justify-center bg-card/30 backdrop-blur-sm border border-border/30 rounded-xl px-8 py-6 transition-all duration-300 hover:bg-card/40 hover:border-border/50 hover:shadow-lg w-44 h-24 shadow-[0_0_25px_rgba(0,120,255,0.3)] dark:shadow-[0_0_25px_rgba(0,120,255,0.2)] ring-1 ring-[rgba(0,120,255,0.3)] dark:ring-[rgba(0,120,255,0.2)] hover:scale-105 hover:-translate-y-1 transform">
            <div className={`${color} mb-2 transition-transform duration-300 group-hover:scale-110`}>
                {icon}
            </div>
            <div className="flex flex-col items-center text-center">
                <span className="text-xl font-bold">{displayValue.toLocaleString()}</span>
                <span className="text-sm text-muted-foreground mt-1">{label}</span>
            </div>
        </div>
    );
};

export function GitHubStatsComponent() {
    const [stats, setStats] = useState<GitHubStats | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchStats = async () => {
            try {
                setLoading(true);
                setError(null);

                let response;
                try {
                    response = await fetch('/api/github-stats');
                } catch (apiError) {
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
                setError('Failed to load statistics');

                setStats({
                    stars: 61,
                    forks: 22,
                    releases: 4,
                    downloads: 0,
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
            <div className="flex flex-wrap justify-center gap-8 mb-16">
                {[1, 2, 3, 4].map((i) => (
                    <div key={i} className="flex flex-col items-center justify-center bg-card/30 backdrop-blur-sm border border-border/30 rounded-xl px-8 py-6 animate-pulse w-44 h-24 shadow-[0_0_25px_rgba(0,120,255,0.3)] dark:shadow-[0_0_25px_rgba(0,120,255,0.2)] ring-1 ring-[rgba(0,120,255,0.3)] dark:ring-[rgba(0,120,255,0.2)]">
                        <div className="w-6 h-6 bg-muted rounded mb-2" />
                        <div className="flex flex-col items-center gap-1">
                            <div className="w-12 h-6 bg-muted rounded" />
                            <div className="w-16 h-4 bg-muted rounded" />
                        </div>
                    </div>
                ))}
            </div>
        );
    }

    if (error || !stats) {
        return (
            <div className="flex flex-wrap justify-center gap-8 mb-16">
                <div className="flex flex-col items-center justify-center bg-card/30 backdrop-blur-sm border border-border/30 rounded-xl px-8 py-6 w-44 h-24 shadow-[0_0_25px_rgba(0,120,255,0.3)] dark:shadow-[0_0_25px_rgba(0,120,255,0.2)] ring-1 ring-[rgba(0,120,255,0.3)] dark:ring-[rgba(0,120,255,0.2)]">
                    <Star className="w-6 h-6 text-yellow-500 mb-2" />
                    <div className="flex flex-col items-center text-center">
                        <span className="text-xl font-bold">61</span>
                        <span className="text-sm text-muted-foreground">Stars</span>
                    </div>
                </div>
                <div className="flex flex-col items-center justify-center bg-card/30 backdrop-blur-sm border border-border/30 rounded-xl px-8 py-6 w-44 h-24 shadow-[0_0_25px_rgba(0,120,255,0.3)] dark:shadow-[0_0_25px_rgba(0,120,255,0.2)] ring-1 ring-[rgba(0,120,255,0.3)] dark:ring-[rgba(0,120,255,0.2)]">
                    <GitFork className="w-6 h-6 text-blue-500 mb-2" />
                    <div className="flex flex-col items-center text-center">
                        <span className="text-xl font-bold">22</span>
                        <span className="text-sm text-muted-foreground">Forks</span>
                    </div>
                </div>
                <div className="flex flex-col items-center justify-center bg-card/30 backdrop-blur-sm border border-border/30 rounded-xl px-8 py-6 w-44 h-24 shadow-[0_0_25px_rgba(0,120,255,0.3)] dark:shadow-[0_0_25px_rgba(0,120,255,0.2)] ring-1 ring-[rgba(0,120,255,0.3)] dark:ring-[rgba(0,120,255,0.2)]">
                    <Package className="w-6 h-6 text-purple-500 mb-2" />
                    <div className="flex flex-col items-center text-center">
                        <span className="text-xl font-bold">4</span>
                        <span className="text-sm text-muted-foreground">Releases</span>
                    </div>
                </div>
                <div className="flex flex-col items-center justify-center bg-card/30 backdrop-blur-sm border border-border/30 rounded-xl px-8 py-6 w-44 h-24 shadow-[0_0_25px_rgba(0,120,255,0.3)] dark:shadow-[0_0_25px_rgba(0,120,255,0.2)] ring-1 ring-[rgba(0,120,255,0.3)] dark:ring-[rgba(0,120,255,0.2)]">
                    <Download className="w-6 h-6 text-emerald-500 mb-2" />
                    <div className="flex flex-col items-center text-center">
                        <span className="text-xl font-bold">0</span>
                        <span className="text-sm text-muted-foreground">Downloads</span>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="flex flex-wrap justify-center gap-8 mb-16">
            <StatCard icon={<Star className="w-6 h-6" />} value={stats.stars} label="Stars" color="text-yellow-500" />
            <StatCard icon={<GitFork className="w-6 h-6" />} value={stats.forks} label="Forks" color="text-blue-500" />
            <StatCard icon={<Package className="w-6 h-6" />} value={stats.releases} label="Releases" color="text-purple-500" />
            <StatCard icon={<Download className="w-6 h-6" />} value={stats.downloads} label="Downloads" color="text-emerald-500" />
        </div>
    );
} 
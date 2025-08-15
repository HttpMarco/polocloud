'use client';

import { Star, GitFork, Download, Package, GitCommit } from 'lucide-react';
import { useEffect, useState, useRef, useCallback } from 'react';
import { motion, AnimatePresence } from 'framer-motion';

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
    index: number;
    isVisible: boolean;
}

const StatCard = ({ icon, value, label, index, isVisible }: StatCardProps) => {
    const [displayValue, setDisplayValue] = useState(0);
    const [hasStarted, setHasStarted] = useState(false);

    useEffect(() => {
        if (isVisible && !hasStarted) {
            const startTimer = setTimeout(() => {
                setHasStarted(true);
            }, index * 200); // Staggered start für jede Karte

            return () => clearTimeout(startTimer);
        }
    }, [isVisible, hasStarted, index]);

    useEffect(() => {
        if (!hasStarted) return;

        const duration = 1200;
        const steps = 60;
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
    }, [value, hasStarted]);

    return (
        <motion.div
            className="group flex flex-col items-center justify-center text-center transition-all duration-500 hover:scale-105 transform"
            initial={{ opacity: 0, y: 30, scale: 0.8 }}
            animate={isVisible ? { opacity: 1, y: 0, scale: 1 } : { opacity: 0, y: 30, scale: 0.8 }}
            transition={{ 
                duration: 0.6, 
                delay: index * 0.1,
                ease: [0.25, 0.46, 0.45, 0.94]
            }}
            whileHover={{ 
                y: -8,
                transition: { duration: 0.3 }
            }}
        >
            <motion.div 
                className="flex items-center justify-center w-12 h-12 sm:w-16 sm:h-16 bg-primary/10 rounded-full border border-primary/20 mb-2 sm:mb-4 group-hover:scale-110 transition-all duration-300 text-primary"
                whileHover={{ 
                    scale: 1.1,
                    boxShadow: "0 0 20px rgba(59, 130, 246, 0.3)"
                }}
                initial={{ rotate: -180, scale: 0 }}
                animate={isVisible ? { rotate: 0, scale: 1 } : { rotate: -180, scale: 0 }}
                transition={{ 
                    duration: 0.6, 
                    delay: index * 0.1 + 0.2,
                    ease: "easeOut"
                }}
            >
                {icon}
            </motion.div>
            <div className="flex flex-col items-center">
                <motion.span 
                    className="text-xl sm:text-2xl md:text-3xl font-bold text-foreground dark:text-white mb-1 sm:mb-2"
                    initial={{ opacity: 0, y: 20 }}
                    animate={isVisible ? { opacity: 1, y: 0 } : { opacity: 0, y: 20 }}
                    transition={{ 
                        duration: 0.5, 
                        delay: index * 0.1 + 0.4
                    }}
                >
                    {displayValue.toLocaleString()}
                </motion.span>
                <motion.span 
                    className="text-xs sm:text-sm text-muted-foreground font-medium uppercase tracking-wide"
                    initial={{ opacity: 0, y: 10 }}
                    animate={isVisible ? { opacity: 1, y: 0 } : { opacity: 0, y: 10 }}
                    transition={{ 
                        duration: 0.5, 
                        delay: index * 0.1 + 0.6
                    }}
                >
                    {label}
                </motion.span>
            </div>
        </motion.div>
    );
};

// Verbesserte Skeleton-Komponente
const StatSkeleton = ({ index }: { index: number }) => (
    <motion.div 
        className="flex flex-col items-center justify-center text-center"
        initial={{ opacity: 0, y: 30 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ 
            duration: 0.5, 
            delay: index * 0.1,
            ease: "easeOut"
        }}
    >
        <div className="w-12 h-12 sm:w-16 sm:h-16 bg-gradient-to-br from-muted/20 to-muted/40 rounded-full mb-2 sm:mb-4 relative overflow-hidden">
            <motion.div 
                className="absolute inset-0 bg-gradient-to-r from-transparent via-white/30 to-transparent"
                animate={{ 
                    x: ["-100%", "100%"]
                }}
                transition={{ 
                    duration: 2,
                    repeat: Infinity,
                    ease: "linear",
                    delay: index * 0.2
                }}
            />
        </div>
        <div className="flex flex-col items-center gap-1 sm:gap-2">
            <div className="w-16 sm:w-20 h-8 sm:h-10 bg-gradient-to-r from-muted/20 to-muted/40 rounded relative overflow-hidden">
                <motion.div 
                    className="absolute inset-0 bg-gradient-to-r from-transparent via-white/30 to-transparent"
                    animate={{ 
                        x: ["-100%", "100%"]
                    }}
                    transition={{ 
                        duration: 2,
                        repeat: Infinity,
                        ease: "linear",
                        delay: index * 0.2 + 0.5
                    }}
                />
            </div>
            <div className="w-20 sm:w-24 h-3 sm:h-4 bg-gradient-to-r from-muted/20 to-muted/40 rounded relative overflow-hidden">
                <motion.div 
                    className="absolute inset-0 bg-gradient-to-r from-transparent via-white/30 to-transparent"
                    animate={{ 
                        x: ["-100%", "100%"]
                    }}
                    transition={{ 
                        duration: 2,
                        repeat: Infinity,
                        ease: "linear",
                        delay: index * 0.2 + 1
                    }}
                />
            </div>
        </div>
    </motion.div>
);

export function GitHubStatsComponent() {
    const [stats, setStats] = useState<GitHubStats | null>(null);
    const [loading, setLoading] = useState(true);
    const [isVisible, setIsVisible] = useState(false);
    const [hasLoaded, setHasLoaded] = useState(false);
    const containerRef = useRef<HTMLDivElement>(null);

    // Intersection Observer für Lazy Loading
    const observerCallback = useCallback((entries: IntersectionObserverEntry[]) => {
        const [entry] = entries;
        if (entry.isIntersecting && !hasLoaded) {
            setIsVisible(true);
            setHasLoaded(true);
        }
    }, [hasLoaded]);

    useEffect(() => {
        const observer = new IntersectionObserver(observerCallback, {
            threshold: 0.1,
            rootMargin: '50px'
        });

        if (containerRef.current) {
            observer.observe(containerRef.current);
        }

        return () => observer.disconnect();
    }, [observerCallback]);

    useEffect(() => {
        if (!isVisible) return;

        const fetchStats = async () => {
            try {
                setLoading(true);
                
                // Verzögerung für bessere UX
                await new Promise(resolve => setTimeout(resolve, 300));

                let response;
                try {
                    response = await fetch('/api/github-stats', {
                        signal: AbortSignal.timeout(5000) // 5s Timeout
                    });
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
                
                // Fallback-Daten
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
    }, [isVisible]);

    if (!isVisible) {
        return (
            <div 
                ref={containerRef}
                className="flex flex-wrap justify-center sm:justify-start gap-6 sm:gap-8 md:gap-12 mb-12 sm:mb-16"
            >
                {[1, 2, 3, 4, 5].map((i) => (
                    <StatSkeleton key={i} index={i} />
                ))}
            </div>
        );
    }

    if (loading) {
        return (
            <div className="flex flex-wrap justify-center sm:justify-start gap-6 sm:gap-8 md:gap-12 mb-12 sm:mb-16">
                {[1, 2, 3, 4, 5].map((i) => (
                    <StatSkeleton key={i} index={i} />
                ))}
            </div>
        );
    }

    if (!stats) {
        return (
            <div className="flex flex-wrap justify-center sm:justify-start gap-6 sm:gap-8 md:gap-12 mb-12 sm:mb-16">
                <StatCard icon={<Star className="w-6 h-6 sm:w-8 sm:h-8" />} value={61} label="Stars" index={0} isVisible={isVisible} />
                <StatCard icon={<GitFork className="w-6 h-6 sm:w-8 sm:h-8" />} value={22} label="Forks" index={1} isVisible={isVisible} />
                <StatCard icon={<Package className="w-6 h-6 sm:w-8 sm:h-8" />} value={4} label="Releases" index={2} isVisible={isVisible} />
                <StatCard icon={<Download className="w-6 h-6 sm:w-8 sm:h-8" />} value={0} label="Downloads" index={3} isVisible={isVisible} />
                <StatCard icon={<GitCommit className="w-6 h-6 sm:w-8 sm:h-8" />} value={3206} label="Commits" index={4} isVisible={isVisible} />
            </div>
        );
    }

    return (
        <motion.div 
            className="flex flex-wrap justify-center sm:justify-start gap-6 sm:gap-8 md:gap-12 mb-12 sm:mb-16"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ duration: 0.5 }}
        >
            <StatCard icon={<Star className="w-6 h-6 sm:w-8 sm:h-8" />} value={stats.stars} label="Stars" index={0} isVisible={isVisible} />
            <StatCard icon={<GitFork className="w-6 h-6 sm:w-8 sm:h-8" />} value={stats.forks} label="Forks" index={1} isVisible={isVisible} />
            <StatCard icon={<Package className="w-6 h-6 sm:w-8 sm:h-8" />} value={stats.releases} label="Releases" index={2} isVisible={isVisible} />
            <StatCard icon={<Download className="w-6 h-6 sm:w-8 sm:h-8" />} value={stats.downloads} label="Downloads" index={3} isVisible={isVisible} />
            <StatCard icon={<GitCommit className="w-6 h-6 sm:w-8 sm:h-8" />} value={stats.commits} label="Commits" index={4} isVisible={isVisible} />
        </motion.div>
    );
} 
'use client';

import { motion } from 'framer-motion';
import { useState, useEffect } from 'react';
import { AddonsHeader, AddonsList, AddonsCTA } from '@/components/home/addons';
import { ModrinthOrganization } from '@/lib/modrinth';

export function AddonsSection() {
    const [organization, setOrganization] = useState<ModrinthOrganization | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [retryCount, setRetryCount] = useState(0);

    const fetchModrinthData = async () => {
        try {
            setLoading(true);
            setError(null);
            console.log('🔄 Fetching Modrinth data... (attempt:', retryCount + 1, ')');

            const response = await fetch('/api/modrinth-organization', {
                cache: 'no-store',
                headers: {
                    'Cache-Control': 'no-cache'
                }
            });
            
            console.log('Modrinth API response status:', response.status);
            
            if (!response.ok) {
                throw new Error(`Failed to fetch Modrinth data: ${response.status} ${response.statusText}`);
            }
            
            const data: ModrinthOrganization = await response.json();
            console.log('Modrinth data received:', data);
            console.log('Projects count:', data.projects?.length || 0);
            
            if (data.projects && data.projects.length > 0) {
                setOrganization(data);
                setError(null);
            } else {
                throw new Error('No projects found in Modrinth data');
            }
        } catch (err) {
            console.error('Failed to fetch Modrinth data:', err);
            const errorMessage = err instanceof Error ? err.message : 'Failed to load Modrinth projects';
            setError(errorMessage);

            if (retryCount < 2) {
                console.log(`Retrying in 2 seconds... (${retryCount + 1}/3)`);
                setTimeout(() => {
                    setRetryCount(prev => prev + 1);
                }, 2000);
            }
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchModrinthData();
    }, [retryCount]);

    // Force refresh on component mount to ensure data is loaded
    useEffect(() => {
        const timer = setTimeout(() => {
            if (!organization && !loading) {
                console.log('🔄 Force refreshing Modrinth data...');
                fetchModrinthData();
            }
        }, 1000);

        return () => clearTimeout(timer);
    }, [organization, loading]);

    return (
        <section className="relative py-16 sm:py-20 md:py-24 lg:py-32 overflow-hidden">
            <motion.div
                className="absolute top-0 left-0 right-0 h-32 sm:h-48 md:h-64 bg-gradient-to-b from-background via-background/95 via-background/80 to-transparent"
                initial={{ opacity: 0, scaleY: 0 }}
                animate={{ opacity: 1, scaleY: 1 }}
                transition={{ duration: 1.5, ease: "easeOut" }}
            />

            <motion.div
                className="absolute inset-0 bg-gradient-to-b from-background via-muted/5 to-muted/5"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ duration: 1.2, delay: 0.3 }}
            />

            <motion.div
                className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]"
                initial={{ opacity: 0, scale: 1.1 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{ duration: 1.5, delay: 0.6, ease: "easeOut" }}
            />

            <motion.div
                className="absolute top-10 sm:top-20 left-5 sm:left-10 w-16 h-16 sm:w-32 sm:h-32 bg-primary/5 rounded-full blur-3xl"
                initial={{ opacity: 0, scale: 0, rotate: 180, x: -100 }}
                animate={{ 
                    opacity: 1, 
                    scale: 1, 
                    x: 0,
                    y: [0, -20, 0],
                    rotate: [0, 5, 0],
                }}
                transition={{
                    duration: 1.8,
                    delay: 0.8,
                    type: 'spring',
                    stiffness: 100,
                    y: { duration: 4, repeat: Infinity, ease: "easeInOut" },
                    rotate: { duration: 4, repeat: Infinity, ease: "easeInOut" }
                }}
            />
            <motion.div
                className="absolute bottom-10 sm:bottom-20 right-5 sm:right-10 w-20 h-20 sm:w-40 sm:h-40 bg-primary/5 rounded-full blur-3xl"
                initial={{ opacity: 0, scale: 0, rotate: -180, x: 100 }}
                animate={{ 
                    opacity: 1, 
                    scale: 1, 
                    x: 0,
                    y: [0, 20, 0],
                    rotate: [0, -5, 0],
                }}
                transition={{
                    duration: 1.8,
                    delay: 1.0,
                    type: 'spring',
                    stiffness: 100,
                    y: { duration: 5, repeat: Infinity, ease: "easeInOut" },
                    rotate: { duration: 5, repeat: Infinity, ease: "easeInOut" }
                }}
            />

            <motion.div
                className="absolute top-1/3 right-1/4 w-12 h-12 sm:w-24 sm:h-24 bg-primary/3 rounded-full blur-2xl"
                initial={{ opacity: 0, scale: 0 }}
                animate={{ 
                    opacity: 1, 
                    scale: 1,
                    x: [0, 15, 0],
                    y: [0, -15, 0],
                }}
                transition={{ 
                    duration: 1.5, 
                    delay: 1.2,
                    x: { duration: 6, repeat: Infinity, ease: "easeInOut" },
                    y: { duration: 6, repeat: Infinity, ease: "easeInOut" }
                }}
            />

            <div className="relative container mx-auto px-3 sm:px-4 md:px-6">
                <AddonsHeader />
                <AddonsList projects={organization?.projects || []} loading={loading} />
                {error && (
                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        className="text-center py-8"
                    >
                        <p className="text-muted-foreground mb-4">{error}</p>
                        <button
                            onClick={() => {
                                setRetryCount(0);
                                fetchModrinthData();
                            }}
                            className="px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:bg-primary/90 transition-colors"
                        >
                            Retry
                        </button>
                    </motion.div>
                )}
                <AddonsCTA />
            </div>
        </section>
    );
} 
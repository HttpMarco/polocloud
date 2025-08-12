'use client';

import { motion } from 'framer-motion';
import { useState, useEffect } from 'react';
import { AddonsHeader, AddonsList, AddonsCTA } from '@/components/home/addons';
import { ModrinthOrganization } from '@/lib/modrinth';

export function AddonsSection() {
    const [organization, setOrganization] = useState<ModrinthOrganization | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchModrinthData = async () => {
            try {
                setLoading(true);
                setError(null);

                const response = await fetch('/api/modrinth-organization');
                if (!response.ok) {
                    throw new Error('Failed to fetch Modrinth data');
                }
                const data: ModrinthOrganization = await response.json();
                setOrganization(data);
            } catch (err) {
                console.error('Failed to fetch Modrinth data:', err);
                setError('Failed to load Modrinth projects');
            } finally {
                setLoading(false);
            }
        };

        fetchModrinthData();
    }, []);

    return (
        <section className="relative py-32 overflow-hidden">
            <motion.div
                className="absolute top-0 left-0 right-0 h-64 bg-gradient-to-b from-background via-background/95 via-background/80 to-transparent"
                initial={{ opacity: 0, scaleY: 0 }}
                whileInView={{ opacity: 1, scaleY: 1 }}
                viewport={{ once: true, amount: 0.3 }}
                transition={{ duration: 1.5, ease: "easeOut" }}
            />

            <motion.div
                className="absolute inset-0 bg-gradient-to-b from-background via-muted/5 to-muted/5"
                initial={{ opacity: 0 }}
                whileInView={{ opacity: 1 }}
                viewport={{ once: true, amount: 0.3 }}
                transition={{ duration: 1.2, delay: 0.3 }}
            />

            <motion.div
                className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]"
                initial={{ opacity: 0, scale: 1.1 }}
                whileInView={{ opacity: 1, scale: 1 }}
                viewport={{ once: true, amount: 0.3 }}
                transition={{ duration: 1.5, delay: 0.6, ease: "easeOut" }}
            />

            <motion.div
                className="absolute top-20 left-10 w-32 h-32 bg-primary/5 rounded-full blur-3xl"
                initial={{ opacity: 0, scale: 0, rotate: 180, x: -100 }}
                whileInView={{ opacity: 1, scale: 1, rotate: 0, x: 0 }}
                viewport={{ once: true, amount: 0.3 }}
                animate={{
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
                className="absolute bottom-20 right-10 w-40 h-40 bg-primary/5 rounded-full blur-3xl"
                initial={{ opacity: 0, scale: 0, rotate: -180, x: 100 }}
                whileInView={{ opacity: 1, scale: 1, rotate: 0, x: 0 }}
                viewport={{ once: true, amount: 0.3 }}
                animate={{
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
                className="absolute top-1/3 right-1/4 w-24 h-24 bg-primary/3 rounded-full blur-2xl"
                initial={{ opacity: 0, scale: 0 }}
                whileInView={{ opacity: 1, scale: 1 }}
                viewport={{ once: true, amount: 0.3 }}
                animate={{
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

            <div className="relative container mx-auto px-6">
                <AddonsHeader />
                <AddonsList projects={organization?.projects || []} loading={loading} />
                <AddonsCTA />
            </div>
        </section>
    );
} 
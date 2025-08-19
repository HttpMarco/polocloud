'use client';

import { motion } from 'framer-motion';
import { Users, GitBranch, Star, Heart } from 'lucide-react';
import { useState, useEffect } from 'react';
import { 
    ContributorsHeader, 
    ContributorsStats, 
    ContributorsList, 
    ContributorsCTA,
    ContributorStats, 
    StatData 
} from '@/components/home/contributors';
import { GitHubContributor } from '@/lib/github';

export function ContributorsSection() {
    const [contributors, setContributors] = useState<GitHubContributor[]>([]);
    const [stats, setStats] = useState<ContributorStats>({
        totalContributors: 0,
        projectStars: 0,
        projectForks: 0,
        projectReleases: 0
    });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchContributors = async () => {
            try {
                setLoading(true);
                setError(null);

                const contributorsResponse = await fetch('/api/github-contributors');
                if (!contributorsResponse.ok) {
                    throw new Error('Failed to fetch contributors');
                }
                const contributorsData: GitHubContributor[] = await contributorsResponse.json();

                const statsResponse = await fetch('/api/github-stats');
                let statsData = null;
                if (statsResponse.ok) {
                    statsData = await statsResponse.json();
                }

                setContributors(contributorsData);
                setStats({
                    totalContributors: contributorsData.length,
                    projectStars: statsData?.stars || 61,
                    projectForks: statsData?.forks || 22,
                    projectReleases: statsData?.releases || 4
                });

            } catch (err) {
                console.error('Failed to fetch contributors:', err);
                setError('Failed to load contributors');

                setContributors([
                    {
                        login: 'HttpMarco',
                        id: 1,
                        avatar_url: 'https://github.com/HttpMarco.png',
                        contributions: 156,
                        commits: 156,
                        html_url: 'https://github.com/HttpMarco',
                        type: 'User'
                    }
                ]);
                setStats({
                    totalContributors: 1,
                    projectStars: 61,
                    projectForks: 22,
                    projectReleases: 4
                });
            } finally {
                setLoading(false);
            }
        };

        fetchContributors();
    }, []);

    const statsData: StatData[] = [
        { label: "Total Contributors", value: stats.totalContributors.toString(), icon: Users },
        { label: "Project Stars", value: stats.projectStars.toString(), icon: Star },
        { label: "Project Forks", value: stats.projectForks.toString(), icon: GitBranch },
        { label: "Releases", value: stats.projectReleases.toString(), icon: Heart }
    ];

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
                className="absolute top-1/2 left-1/4 w-24 h-24 bg-primary/3 rounded-full blur-2xl"
                initial={{ opacity: 0, scale: 0, y: 50 }}
                whileInView={{ opacity: 1, scale: 1, y: 0 }}
                viewport={{ once: true, amount: 0.3 }}
                animate={{
                    x: [0, 15, 0],
                    y: [0, -15, 0],
                }}
                transition={{
                    duration: 1.2,
                    delay: 1.2,
                    type: 'spring',
                    stiffness: 150,
                    x: { duration: 6, repeat: Infinity, ease: "easeInOut" },
                    y: { duration: 6, repeat: Infinity, ease: "easeInOut" }
                }}
            />

            <motion.div
                className="absolute top-1/3 right-1/3 w-20 h-20 bg-primary/4 rounded-full blur-2xl"
                initial={{ opacity: 0, scale: 0 }}
                whileInView={{ opacity: 1, scale: 1 }}
                viewport={{ once: true, amount: 0.3 }}
                animate={{
                    x: [0, -10, 0],
                    y: [0, 10, 0],
                }}
                transition={{
                    duration: 1.5,
                    delay: 1.4,
                    x: { duration: 7, repeat: Infinity, ease: "easeInOut" },
                    y: { duration: 7, repeat: Infinity, ease: "easeInOut" }
                }}
            />

            <div className="relative container mx-auto px-6">
                <ContributorsHeader />
                <ContributorsStats statsData={statsData} />
                <ContributorsList contributors={contributors} loading={loading} />
                <ContributorsCTA />
            </div>
        </section>
    );
}
'use client';

import { motion } from 'framer-motion';
import { Github } from 'lucide-react';
import { GitHubContributor } from '@/lib/github';

interface ContributorsListProps {
    contributors: GitHubContributor[];
    loading: boolean;
}

export function ContributorsList({ contributors, loading }: ContributorsListProps) {
    const containerVariants = {
        hidden: { opacity: 0 },
        visible: {
            opacity: 1,
            transition: {
                staggerChildren: 0.1,
                delayChildren: 0.2
            }
        }
    };

    const itemVariants = {
        hidden: {
            opacity: 0,
            y: 30,
            scale: 0.9,
            rotateX: -15
        },
        visible: {
            opacity: 1,
            y: 0,
            scale: 1,
            rotateX: 0,
            transition: {
                type: "spring" as const,
                stiffness: 200,
                damping: 20,
                duration: 0.6
            }
        }
    };

    if (loading) {
        return (
            <motion.div
                initial={{ opacity: 0, y: 20 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true, amount: 0.1 }}
                transition={{ duration: 0.6, delay: 0.3 }}
                className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"
            >
                {[1, 2, 3, 4, 5, 6].map((i) => (
                    <div
                        key={i}
                        className="bg-card/30 backdrop-blur-sm border border-border/50 rounded-xl p-6 animate-pulse"
                    >
                        <div className="flex items-center mb-4">
                            <div className="w-16 h-16 bg-muted/50 rounded-full mr-4" />
                            <div>
                                <div className="w-24 h-4 bg-muted/50 rounded mb-2" />
                                <div className="w-20 h-3 bg-muted/50 rounded" />
                            </div>
                        </div>
                        <div className="text-center">
                            <div className="w-16 h-4 bg-muted/50 rounded mb-1" />
                            <div className="w-20 h-3 bg-muted/50 rounded" />
                        </div>
                    </div>
                ))}
            </motion.div>
        );
    }

    return (
        <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true, amount: 0.1 }}
            transition={{ duration: 0.6, delay: 0.3 }}
            className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"
        >
            {contributors.map((contributor, index) => (
                <motion.div
                    key={contributor.login}
                    variants={itemVariants}
                    className="bg-card/30 backdrop-blur-sm border border-border/50 rounded-xl p-6 hover:bg-card/40 hover:border-border/70 transition-all duration-300 group cursor-pointer relative"
                >
                    <motion.div
                        className="flex items-center mb-4"
                        transition={{ type: "spring", stiffness: 400 }}
                    >
                        <motion.div
                            className="w-16 h-16 rounded-full overflow-hidden mr-4 ring-2 ring-primary/20 group-hover:ring-primary/40 transition-all duration-200 relative"
                            transition={{ type: "spring", stiffness: 300 }}
                        >
                            <img
                                src={contributor.avatar_url}
                                alt={contributor.login}
                                className="w-full h-full object-cover"
                                onError={(e) => {
                                    const target = e.target as HTMLImageElement;
                                    target.src = `https://ui-avatars.com/api/?name=${contributor.login}&background=6366f1&color=fff&size=64`;
                                }}
                            />
                        </motion.div>

                        {index < 3 && (
                            <div className="absolute -top-1 -right-1 w-6 h-5 rounded-full flex items-center justify-center text-xs font-bold text-white shadow-lg z-10">
                                {index === 0 && (
                                    <div className="w-full h-full rounded-full bg-gradient-to-br from-yellow-400 to-yellow-600 flex items-center justify-center shadow-lg px-1 relative overflow-hidden">
                                        <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/30 to-transparent animate-pulse" style={{
                                            animationDuration: '2s',
                                            animationIterationCount: 'infinite',
                                            transform: 'translateX(-100%)',
                                            animationName: 'shimmer'
                                        }}></div>
                                        <span className="text-white font-bold text-[10px] relative z-10">#1</span>
                                    </div>
                                )}
                                {index === 1 && (
                                    <div className="w-full h-full rounded-full bg-gradient-to-br from-gray-300 to-gray-500 flex items-center justify-center shadow-lg px-1 relative overflow-hidden">
                                        <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/30 to-transparent animate-pulse" style={{
                                            animationDuration: '2s',
                                            animationIterationCount: 'infinite',
                                            transform: 'translateX(-100%)',
                                            animationName: 'shimmer'
                                        }}></div>
                                        <span className="text-white font-bold text-[10px] relative z-10">#2</span>
                                    </div>
                                )}
                                {index === 2 && (
                                    <div className="w-full h-full rounded-full bg-gradient-to-br from-amber-600 to-amber-800 flex items-center justify-center shadow-lg px-1 relative overflow-hidden">
                                        <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/30 to-transparent animate-pulse" style={{
                                            animationDuration: '2s',
                                            animationIterationCount: 'infinite',
                                            transform: 'translateX(-100%)',
                                            animationName: 'shimmer'
                                        }}></div>
                                        <span className="text-white font-bold text-[10px] relative z-10">#3</span>
                                    </div>
                                )}
                            </div>
                        )}
                        <div>
                            <h3 className="text-lg font-bold text-foreground dark:text-white group-hover:text-primary transition-colors duration-200">
                                {contributor.login}
                            </h3>
                            <p className="text-sm text-muted-foreground dark:text-white/70">
                                {contributor.commits} commits
                            </p>
                        </div>
                    </motion.div>

                    <div className="text-center">
                        <motion.a
                            href={contributor.html_url}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="inline-flex items-center gap-2 text-sm text-primary hover:text-primary/80 transition-colors duration-200"
                            whileHover={{
                                scale: 1.05,
                                x: 5,
                                textShadow: "0 0 8px rgba(99, 102, 241, 0.5)"
                            }}
                            whileTap={{ scale: 0.95 }}
                            transition={{ type: "spring", stiffness: 400 }}
                        >
                            <Github className="w-4 h-4" />
                            View Profile
                        </motion.a>
                    </div>
                </motion.div>
            ))}
        </motion.div>
    );
}

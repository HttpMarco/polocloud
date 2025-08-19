'use client';

import { motion } from 'framer-motion';
import { StatData } from './types';

interface ContributorsStatsProps {
    statsData: StatData[];
}

export function ContributorsStats({ statsData }: ContributorsStatsProps) {
    const statsContainerVariants = {
        hidden: { opacity: 0 },
        visible: {
            opacity: 1,
            transition: {
                staggerChildren: 0.08,
                delayChildren: 0.1
            }
        }
    };

    const statsItemVariants = {
        hidden: {
            opacity: 0,
            y: 20,
            scale: 0.8,
            rotateY: -10
        },
        visible: {
            opacity: 1,
            y: 0,
            scale: 1,
            rotateY: 0,
            transition: {
                type: "spring" as const,
                stiffness: 300,
                damping: 25,
                duration: 0.5
            }
        }
    };

    return (
        <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true, amount: 0.1 }}
            transition={{ duration: 0.6, delay: 0.2 }}
            className="grid grid-cols-2 md:grid-cols-4 gap-6 mb-16"
        >
            {statsData.map((stat, index) => (
                <motion.div
                    key={stat.label}
                    variants={statsItemVariants}
                    whileHover={{
                        scale: 1.05,
                        rotateY: 8,
                        rotateX: 2,
                        boxShadow: "0 20px 40px rgba(0, 0, 0, 0.15)",
                        transition: { type: "spring", stiffness: 300, damping: 20 }
                    }}
                    whileTap={{ scale: 0.98 }}
                    className="bg-card/30 backdrop-blur-sm border border-border/50 rounded-xl p-6 text-center hover:bg-card/40 hover:border-border/70 transition-all duration-300 cursor-pointer"
                >
                    <motion.div
                        className="w-12 h-12 bg-gradient-to-br from-primary/20 to-primary/10 rounded-xl flex items-center justify-center mx-auto mb-4"
                        whileHover={{
                            scale: 1.1,
                            rotate: 5,
                            boxShadow: "0 0 20px rgba(99, 102, 241, 0.3)"
                        }}
                        transition={{ type: "spring", stiffness: 300 }}
                    >
                        <stat.icon className="w-6 h-6 text-primary" />
                    </motion.div>
                    <div className="text-2xl md:text-3xl font-black text-foreground dark:text-white mb-2">
                        {stat.value}
                    </div>
                    <div className="text-sm text-muted-foreground dark:text-white/70 font-medium">
                        {stat.label}
                    </div>
                </motion.div>
            ))}
        </motion.div>
    );
}

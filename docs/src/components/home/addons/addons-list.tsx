'use client';

import { motion } from 'framer-motion';
import { Package, Download, Users, Star, ExternalLink, Code, Zap, FileText } from 'lucide-react';
import { ModrinthProject } from '@/lib/modrinth';

interface AddonsListProps {
    projects: ModrinthProject[];
    loading: boolean;
}

export function AddonsList({ projects, loading }: AddonsListProps) {
    const getProjectIcon = (projectTypes: string[]) => {
        const projectType = projectTypes[0] || 'plugin';
        switch (projectType) {
            case 'plugin':
                return <Zap className="w-6 h-6 text-primary" />;
            case 'mod':
                return <Package className="w-6 h-6 text-primary" />;
            default:
                return <FileText className="w-6 h-6 text-primary" />;
        }
    };

    const cleanDescription = (description: string) => {
        let cleaned = description.replace(/\[([^\]]+)\]\([^)]+\)/g, '$1');
        cleaned = cleaned.replace(/\*\*([^*]+)\*\*/g, '$1');
        cleaned = cleaned.replace(/\*([^*]+)\*/g, '$1');
        cleaned = cleaned.replace(/`([^`]+)`/g, '$1');
        cleaned = cleaned.replace(/^#{1,6}\s+/gm, '');
        cleaned = cleaned.replace(/^[-*+]\s+/gm, '');
        cleaned = cleaned.replace(/^\d+\.\s+/gm, '');
        cleaned = cleaned.replace(/```[\s\S]*?```/g, '');
        cleaned = cleaned.replace(/`([^`]+)`/g, '$1');
        cleaned = cleaned.replace(/ID:\s*[a-zA-Z0-9]+/gi, '');
        cleaned = cleaned.replace(/Project ID:\s*[a-zA-Z0-9]+/gi, '');
        cleaned = cleaned.replace(/[a-zA-Z0-9]{8,}/g, '');
        cleaned = cleaned.replace(/\n\s*\n/g, '\n');
        cleaned = cleaned.trim();

        if (cleaned.length > 150) {
            cleaned = cleaned.substring(0, 147) + '...';
        }

        return cleaned;
    };

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
                variants={containerVariants}
                initial="hidden"
                whileInView="visible"
                viewport={{ once: true, amount: 0.1 }}
                className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"
            >
                {[1, 2, 3].map((i) => (
                    <motion.div
                        key={`loading-${i}`}
                        variants={itemVariants}
                        className="bg-card/30 backdrop-blur-sm border border-border/50 rounded-xl p-6 animate-pulse"
                    >
                        <div className="flex items-center mb-4">
                            <div className="w-12 h-12 bg-muted/50 rounded-lg mr-4" />
                            <div>
                                <div className="w-32 h-4 bg-muted/50 rounded mb-2" />
                                <div className="w-24 h-3 bg-muted/50 rounded" />
                            </div>
                        </div>
                        <div className="space-y-2">
                            <div className="w-full h-3 bg-muted/50 rounded" />
                            <div className="w-3/4 h-3 bg-muted/50 rounded" />
                        </div>
                        <div className="flex justify-between mt-4">
                            <div className="w-16 h-4 bg-muted/50 rounded" />
                            <div className="w-16 h-4 bg-muted/50 rounded" />
                        </div>
                    </motion.div>
                ))}
            </motion.div>
        );
    }

    return (
        <motion.div
            variants={containerVariants}
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true, amount: 0.1 }}
            className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"
        >
            {projects.map((project, index) => (
                <motion.div
                    key={project.id}
                    variants={itemVariants}
                    whileHover={{
                        scale: 1.03,
                        rotateY: 8,
                        rotateX: 2,
                        boxShadow: "0 20px 40px rgba(0, 0, 0, 0.15)",
                        transition: { type: "spring", stiffness: 300, damping: 20 }
                    }}
                    whileTap={{ scale: 0.98 }}
                    className="bg-card/30 backdrop-blur-sm border border-border/50 rounded-xl p-6 hover:bg-card/40 hover:border-border/70 transition-all duration-300 group cursor-pointer"
                >
                    <motion.div
                        className="flex items-center mb-4"
                        whileHover={{ x: 5 }}
                        transition={{ type: "spring", stiffness: 400 }}
                    >
                        <motion.div
                            className="w-12 h-12 bg-gradient-to-br from-primary/20 to-primary/10 rounded-lg flex items-center justify-center mr-4"
                            whileHover={{
                                scale: 1.1,
                                rotate: 5,
                                boxShadow: "0 0 20px rgba(99, 102, 241, 0.3)"
                            }}
                            transition={{ type: "spring", stiffness: 300 }}
                        >
                            {getProjectIcon(project.project_types)}
                        </motion.div>
                        <div>
                            <h3 className="text-lg font-bold text-foreground dark:text-white group-hover:text-primary transition-colors duration-200">
                                {project.name}
                            </h3>
                            <p className="text-sm text-muted-foreground dark:text-white/70">
                                {project.project_types[0] || 'plugin'}
                            </p>
                        </div>
                    </motion.div>

                    <motion.p
                        className="text-sm text-muted-foreground dark:text-white/70 mb-4 line-clamp-3"
                        whileHover={{ color: "hsl(var(--muted-foreground) / 0.9)" }}
                        transition={{ duration: 0.2 }}
                    >
                        {cleanDescription(project.summary || project.description)}
                    </motion.p>

                    <motion.div
                        className="flex justify-between items-center mb-4"
                        initial={{ opacity: 0, y: 10 }}
                        whileInView={{ opacity: 1, y: 0 }}
                        viewport={{ once: true }}
                        transition={{ delay: 0.3 + index * 0.1 }}
                    >
                        <div className="flex items-center gap-2 text-sm text-muted-foreground">
                            <Download className="w-4 h-4" />
                            {project.downloads}
                        </div>
                        <div className="flex items-center gap-2 text-sm text-muted-foreground">
                            <Users className="w-4 h-4" />
                            {project.followers}
                        </div>
                        <div className="flex items-center gap-2 text-sm text-muted-foreground">
                            <Code className="w-4 h-4" />
                            {project.versions[0] || '1.0.0'}
                        </div>
                    </motion.div>

                    <motion.div
                        className="flex flex-wrap gap-2 mb-4"
                        initial={{ opacity: 0, scale: 0.9 }}
                        whileInView={{ opacity: 1, scale: 1 }}
                        viewport={{ once: true }}
                        transition={{ delay: 0.4 + index * 0.1 }}
                    >
                        {project.loaders.slice(0, 2).map((loader) => (
                            <motion.span
                                key={`${project.id}-loader-${loader}`}
                                className="px-2 py-1 bg-primary/10 text-primary text-xs rounded-md font-medium"
                                whileHover={{ scale: 1.05, backgroundColor: "hsl(var(--primary) / 0.2)" }}
                                transition={{ type: "spring", stiffness: 400 }}
                            >
                                {loader}
                            </motion.span>
                        ))}
                    </motion.div>

                    <motion.div
                        className="flex justify-end"
                        initial={{ opacity: 0, x: 20 }}
                        whileInView={{ opacity: 1, x: 0 }}
                        viewport={{ once: true }}
                        transition={{ delay: 0.5 + index * 0.1 }}
                    >
                        <motion.a
                            href={`https://modrinth.com/${project.project_types[0] || 'plugin'}/${project.slug}`}
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
                            <Download className="w-4 h-4" />
                            Download
                        </motion.a>
                    </motion.div>
                </motion.div>
            ))}
        </motion.div>
    );
}

"use client"

import { motion } from "framer-motion";
import { Package, Download, Users, Star, ExternalLink, Code, Zap, FileText } from "lucide-react";
import { useState, useEffect } from "react";
import { ModrinthOrganization, ModrinthProject } from "@/lib/modrinth";

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

    const getProjectIcon = (projectType: string) => {
        switch (projectType) {
            case 'plugin':
                return <Zap className="w-6 h-6 text-primary" />;
            case 'mod':
                return <Package className="w-6 h-6 text-primary" />;
            default:
                return <FileText className="w-6 h-6 text-primary" />;
        }
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
                <motion.div
                    className="text-center mb-20"
                    initial={{ opacity: 0, y: 50 }}
                    whileInView={{ opacity: 1, y: 0 }}
                    viewport={{ once: true, amount: 0.3 }}
                    transition={{ duration: 1.2, ease: 'easeOut' }}
                >
                    <motion.h2
                        className="text-3xl md:text-4xl lg:text-5xl font-black mb-8 bg-gradient-to-r from-foreground via-primary to-foreground bg-clip-text text-transparent tracking-tight leading-tight"
                        initial={{ opacity: 0, y: 30, scale: 0.9, rotateX: -15 }}
                        whileInView={{ opacity: 1, y: 0, scale: 1, rotateX: 0 }}
                        viewport={{ once: true, amount: 0.3 }}
                        transition={{ duration: 1.2, delay: 0.3, type: 'spring', stiffness: 200 }}
                    >
                        Addons
                    </motion.h2>
                    <motion.p
                        className="text-base md:text-lg text-muted-foreground max-w-4xl mx-auto leading-relaxed"
                        initial={{ opacity: 0, y: 20, scale: 0.95 }}
                        whileInView={{ opacity: 1, y: 0, scale: 1 }}
                        viewport={{ once: true, amount: 0.3 }}
                        transition={{ duration: 1.2, delay: 0.6, ease: 'easeOut' }}
                    >
                        Extend your PoloCloud experience with our official addons. Each addon is designed to enhance your server management capabilities and provide powerful features.
                    </motion.p>
                </motion.div>

                {loading ? (
                    <motion.div
                        variants={containerVariants}
                        initial="hidden"
                        whileInView="visible"
                        viewport={{ once: true, amount: 0.1 }}
                        className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"
                    >
                        {[1, 2, 3].map((i) => (
                            <motion.div
                                key={i}
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
                ) : (
                    <motion.div
                        variants={containerVariants}
                        initial="hidden"
                        whileInView="visible"
                        viewport={{ once: true, amount: 0.1 }}
                        className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"
                    >
                        {organization?.projects.map((project, index) => (
                            <motion.div
                                key={project.project_id}
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
                                        {getProjectIcon(project.project_type)}
                                    </motion.div>
                                    <div>
                                        <h3 className="text-lg font-bold text-foreground dark:text-white group-hover:text-primary transition-colors duration-200">
                                            {project.title}
                                        </h3>
                                        <p className="text-sm text-muted-foreground dark:text-white/70">
                                            {project.project_type}
                                        </p>
                                    </div>
                                </motion.div>

                                <motion.p
                                    className="text-sm text-muted-foreground dark:text-white/70 mb-4 line-clamp-3"
                                    whileHover={{ color: "hsl(var(--muted-foreground) / 0.9)" }}
                                    transition={{ duration: 0.2 }}
                                >
                                    {project.description}
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
                                        {project.download_count}
                                    </div>
                                    <div className="flex items-center gap-2 text-sm text-muted-foreground">
                                        <Users className="w-4 h-4" />
                                        {project.follower_count}
                                    </div>
                                    <div className="flex items-center gap-2 text-sm text-muted-foreground">
                                        <Code className="w-4 h-4" />
                                        {project.latest_version}
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
                                            key={loader}
                                            className="px-2 py-1 bg-primary/10 text-primary text-xs rounded-md font-medium"
                                            whileHover={{ scale: 1.05, backgroundColor: "hsl(var(--primary) / 0.2)" }}
                                            transition={{ type: "spring", stiffness: 400 }}
                                        >
                                            {loader}
                                        </motion.span>
                                    ))}
                                    {project.game_versions.slice(0, 1).map((version) => (
                                        <motion.span
                                            key={version}
                                            className="px-2 py-1 bg-muted/50 text-muted-foreground text-xs rounded-md"
                                            whileHover={{ scale: 1.05 }}
                                            transition={{ type: "spring", stiffness: 400 }}
                                        >
                                            {version}
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
                                        href={`https://modrinth.com/${project.project_type}/${project.slug}`}
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
                )}

                <motion.div
                    initial={{ opacity: 0, y: 30, scale: 0.9 }}
                    whileInView={{ opacity: 1, y: 0, scale: 1 }}
                    viewport={{ once: true }}
                    transition={{ duration: 0.8, delay: 1, type: "spring", stiffness: 200 }}
                    className="text-center mt-16"
                >
                    <motion.div
                        whileHover={{
                            scale: 1.05,
                            boxShadow: "0 20px 40px rgba(99, 102, 241, 0.3)",
                            y: -2
                        }}
                        whileTap={{ scale: 0.95 }}
                        className="inline-flex items-center gap-3 px-8 py-4 bg-gradient-to-r from-primary to-primary/80 text-white rounded-xl font-semibold text-lg hover:from-primary/90 hover:to-primary/70 transition-all duration-300 shadow-lg hover:shadow-xl"
                    >
                        <motion.div
                            animate={{ rotate: [0, 10, -10, 0] }}
                            transition={{ duration: 2, repeat: Infinity, ease: "easeInOut" }}
                        >
                            <Package className="w-5 h-5" />
                        </motion.div>
                        View on Modrinth
                    </motion.div>
                    <motion.p
                        className="text-sm text-muted-foreground dark:text-white/70 mt-4"
                        initial={{ opacity: 0 }}
                        whileInView={{ opacity: 1 }}
                        viewport={{ once: true }}
                        transition={{ delay: 1.2 }}
                    >
                        Check out all our projects!
                    </motion.p>
                </motion.div>
            </div>
        </section>
    );
} 
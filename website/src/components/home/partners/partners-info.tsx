'use client';

import { useEffect, useState, useRef } from 'react';
import { ExternalLink, Handshake, Users, Star, Cloud, Award, Zap } from 'lucide-react';
import { motion } from 'framer-motion';
import { Benefit } from './types';

export function PartnersInfo() {
    const infoRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const observer = new IntersectionObserver(
            ([entry]) => {
                if (entry.isIntersecting) {

                }
            },
            { threshold: 0.1, rootMargin: '0px 0px -100px 0px' }
        );

        if (infoRef.current) {
            observer.observe(infoRef.current);
        }

        return () => observer.disconnect();
    }, []);

    const benefits: Benefit[] = [
        {
            icon: Users,
            title: "Partner Listing",
            description: "Featured on polocloud.de partner list",
            color: "from-blue-500/20 to-blue-600/20",
            iconColor: "text-blue-400"
        },
        {
            icon: Handshake,
            title: "Premium Support",
            description: "Direct contact person for support",
            color: "from-green-500/20 to-green-600/20",
            iconColor: "text-green-400"
        },
        {
            icon: Star,
            title: "Higher Priority",
            description: "Priority handling for issues",
            color: "from-yellow-500/20 to-yellow-600/20",
            iconColor: "text-yellow-400"
        },
        {
            icon: Cloud,
            title: "Custom Addons",
            description: "Development of special addons",
            color: "from-purple-500/20 to-purple-600/20",
            iconColor: "text-purple-400"
        },
        {
            icon: Award,
            title: "Exclusive Badges",
            description: "Special partner badges and recognition",
            color: "from-pink-500/20 to-pink-600/20",
            iconColor: "text-pink-400"
        },
        {
            icon: Zap,
            title: "Early Access",
            description: "Access to new features before release",
            color: "from-orange-500/20 to-orange-600/20",
            iconColor: "text-orange-400"
        }
    ];

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

    const cardVariants = {
        hidden: {
            opacity: 0,
            y: 20,
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
                stiffness: 300,
                damping: 20
            }
        }
    };

    return (
        <motion.div
            ref={infoRef}
            className="relative overflow-hidden bg-gradient-to-br from-card/50 via-card/30 to-card/50 backdrop-blur-xl border border-border/50 rounded-2xl p-6 shadow-2xl"
            initial={{ opacity: 0, y: 50 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true, amount: 0.3 }}
            transition={{ duration: 0.8, ease: "easeOut" }}
        >
            <div className="absolute inset-0 bg-gradient-to-br from-primary/5 via-transparent to-primary/5" />
            <div className="absolute top-0 right-0 w-24 h-24 bg-primary/10 rounded-full blur-2xl" />
            <div className="absolute bottom-0 left-0 w-20 h-20 bg-primary/10 rounded-full blur-xl" />
            <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-32 h-32 bg-primary/5 rounded-full blur-2xl" />

            <div className="relative z-10">
                <div className="text-center mb-8">
                    <motion.div
                        className="w-14 h-14 bg-gradient-to-br from-primary/20 to-primary/10 rounded-xl flex items-center justify-center mx-auto mb-4 shadow-lg"
                        initial={{ opacity: 0, scale: 0.8, rotate: -180 }}
                        whileInView={{ opacity: 1, scale: 1, rotate: 0 }}
                        viewport={{ once: true, amount: 0.3 }}
                        transition={{ duration: 0.8, delay: 0.1, type: "spring", stiffness: 200 }}
                    >
                        <Handshake className="w-7 h-7 text-primary" />
                    </motion.div>
                    <motion.h3
                        className="text-2xl md:text-3xl font-black text-foreground dark:text-white mb-4 tracking-tight leading-tight bg-gradient-to-r from-foreground via-primary to-foreground bg-clip-text text-transparent"
                        initial={{ opacity: 0, y: 20 }}
                        whileInView={{ opacity: 1, y: 0 }}
                        viewport={{ once: true, amount: 0.3 }}
                        transition={{ duration: 0.6, delay: 0.2 }}
                    >
                        Partner with PoloCloud
                    </motion.h3>
                    <motion.p
                        className="text-base md:text-lg text-muted-foreground dark:text-white/70 max-w-4xl mx-auto leading-relaxed"
                        initial={{ opacity: 0, y: 20 }}
                        whileInView={{ opacity: 1, y: 0 }}
                        viewport={{ once: true, amount: 0.3 }}
                        transition={{ duration: 0.6, delay: 0.4 }}
                    >
                        Partner with PoloCloud and help shape the future of Minecraft server management.
                        Enjoy exclusive benefits and grow your business with us.
                    </motion.p>
                </div>

                <div className="mb-8">
                    <motion.h4
                        className="text-xl md:text-2xl font-black text-foreground dark:text-white text-center mb-6 tracking-tight leading-tight"
                        initial={{ opacity: 0, y: 20 }}
                        whileInView={{ opacity: 1, y: 0 }}
                        viewport={{ once: true, amount: 0.3 }}
                        transition={{ duration: 0.6, delay: 0.6 }}
                    >
                        Exclusive Partner Benefits
                    </motion.h4>
                    <motion.div
                        className="grid md:grid-cols-2 lg:grid-cols-3 gap-4"
                        variants={containerVariants}
                        initial="hidden"
                        whileInView="visible"
                        viewport={{ once: true, amount: 0.3 }}
                    >
                        {benefits.map((benefit, index) => (
                            <motion.div
                                key={index}
                                className="group text-center p-4 bg-gradient-to-br backdrop-blur-sm rounded-xl border border-border/30 shadow-lg relative overflow-hidden hover:shadow-xl transition-all duration-300"
                                variants={cardVariants}
                                whileHover={{
                                    scale: 1.05,
                                    y: -5,
                                    transition: { type: "spring", stiffness: 400, damping: 20 }
                                }}
                                whileTap={{ scale: 0.98 }}
                                style={{
                                    background: `linear-gradient(135deg, ${benefit.color.split(' ')[1].replace('to-', '')}20, ${benefit.color.split(' ')[0].replace('from-', '')}20)`
                                }}
                            >
                                <motion.div
                                    className="absolute inset-0 bg-gradient-to-br from-white/10 to-transparent opacity-0 group-hover:opacity-100 transition-all duration-300"
                                />

                                <motion.div
                                    className={`w-12 h-12 bg-gradient-to-br rounded-xl flex items-center justify-center mx-auto mb-3 relative z-10 shadow-lg`}
                                    style={{
                                        background: `linear-gradient(135deg, ${benefit.color.split(' ')[1].replace('to-', '')}40, ${benefit.color.split(' ')[0].replace('from-', '')}40)`
                                    }}
                                    whileHover={{
                                        scale: 1.2,
                                        rotate: 360,
                                        transition: { duration: 0.6, ease: "easeInOut" }
                                    }}
                                >
                                    <div className={`w-6 h-6 ${benefit.iconColor}`}>
                                        <benefit.icon className="w-full h-full" />
                                    </div>
                                </motion.div>

                                <motion.h5
                                    className="font-black text-foreground dark:text-white mb-2 text-base leading-tight relative z-10"
                                    transition={{ type: "spring", stiffness: 400 }}
                                >
                                    {benefit.title}
                                </motion.h5>

                                <motion.p
                                    className="text-xs text-muted-foreground dark:text-white/70 font-medium relative z-10 leading-relaxed"
                                    initial={{ opacity: 0 }}
                                    whileInView={{ opacity: 1 }}
                                    transition={{ delay: 0.3 + index * 0.1 }}
                                >
                                    {benefit.description}
                                </motion.p>
                            </motion.div>
                        ))}
                    </motion.div>
                </div>

                <motion.div
                    className="text-center"
                    initial={{ opacity: 0, y: 30 }}
                    whileInView={{ opacity: 1, y: 0 }}
                    viewport={{ once: true, amount: 0.3 }}
                    transition={{ duration: 0.8, delay: 1.0 }}
                >
                    <div className="relative overflow-hidden bg-gradient-to-r from-primary/15 via-primary/10 to-primary/15 border border-primary/30 rounded-xl p-6 mb-6 shadow-lg">
                        <div className="absolute inset-0 bg-gradient-to-br from-primary/5 via-transparent to-primary/5" />
                        <div className="absolute top-0 right-0 w-16 h-16 bg-primary/20 rounded-full blur-xl" />
                        <div className="absolute bottom-0 left-0 w-12 h-12 bg-primary/20 rounded-full blur-lg" />

                        <div className="relative z-10">
                            <h4 className="text-lg font-bold text-foreground dark:text-white mb-3">
                                Ready to Partner?
                            </h4>
                            <p className="text-sm text-muted-foreground dark:text-white/70 mb-4 leading-relaxed">
                                Join our Discord to discuss partnership opportunities and take your business to the next level.
                            </p>
                            <motion.a
                                href="https://discord.com/channels/1401907740489678912/1402064495857369189"
                                target="_blank"
                                rel="noopener noreferrer"
                                className="inline-flex items-center gap-2 px-6 py-3 bg-gradient-to-r from-primary to-primary/90 hover:from-primary/90 hover:to-primary text-primary-foreground rounded-lg font-semibold text-base shadow-[0_0_20px_rgba(0,120,255,0.3)] hover:shadow-[0_0_30px_rgba(0,120,255,0.5)] group transition-all duration-300"
                                whileHover={{
                                    scale: 1.05,
                                    boxShadow: "0 0 40px rgba(0,120,255,0.6)",
                                    y: -2
                                }}
                                whileTap={{ scale: 0.95 }}
                            >
                                <Handshake className="w-4 h-4 group-hover:rotate-12 transition-transform duration-300" />
                                Apply for Partnership
                                <ExternalLink className="w-3 h-3 group-hover:translate-x-1 transition-transform duration-300" />
                            </motion.a>
                        </div>
                    </div>
                </motion.div>
            </div>
        </motion.div>
    );
}

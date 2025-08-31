'use client';

import { useEffect, useState, useRef } from 'react';
import { ExternalLink, Star, Award } from 'lucide-react';
import { motion } from 'framer-motion';
import { Partner } from './types';

export function PartnersCarousel() {
    const [isVisible, setIsVisible] = useState(false);
    const [partners, setPartners] = useState<Partner[]>([]);
    const [loading, setLoading] = useState(true);
    const carouselRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const observer = new IntersectionObserver(
            ([entry]) => {
                if (entry.isIntersecting) {
                    setIsVisible(true);
                }
            },
            { threshold: 0.1, rootMargin: '0px 0px -100px 0px' }
        );

        if (carouselRef.current) {
            observer.observe(carouselRef.current);
        }

        return () => observer.disconnect();
    }, []);

    useEffect(() => {
        const fetchPartners = async () => {
            try {
                const response = await fetch('/api/public/partners');
                if (response.ok) {
                    const data = await response.json();
                    setPartners(data.partners || []);
                } else {
                    console.error('Failed to fetch partners');
                }
            } catch (error) {
                console.error('Error fetching partners:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchPartners();
    }, []);

    return (
        <div
            ref={carouselRef}
            className={`transition-all duration-1000 ease-out ${
                isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
            }`}
        >
            <div className="relative overflow-hidden bg-gradient-to-br from-card/30 via-card/20 to-card/30 backdrop-blur-xl border border-border/50 rounded-xl p-8 shadow-2xl">
                <div className="absolute inset-0 bg-gradient-to-br from-primary/5 via-transparent to-primary/5" />
                <div className="absolute top-0 right-0 w-24 h-24 bg-primary/10 rounded-full blur-2xl" />
                <div className="absolute bottom-0 left-0 w-20 h-20 bg-primary/10 rounded-full blur-xl" />

                <div className="relative z-10">
                    <div className="text-center mb-8">
                        <div className="w-12 h-12 bg-gradient-to-br from-primary/20 to-primary/10 rounded-xl flex items-center justify-center mx-auto mb-4 shadow-lg">
                            <Star className="w-6 h-6 text-primary" />
                        </div>
                        <h3 className="text-2xl md:text-3xl font-black text-foreground dark:text-white mb-4">
                            Our Partners
                        </h3>
                        <p className="text-base md:text-lg text-muted-foreground dark:text-white/70 leading-relaxed max-w-2xl mx-auto">
                            Discover our trusted partners and integrations that make PoloCloud the ultimate Minecraft hosting solution.
                        </p>
                    </div>

                    <div className="relative overflow-hidden max-w-2xl mx-auto">
                        {loading ? (
                            <div className="text-center text-muted-foreground py-8">
                                Loading partners...
                            </div>
                        ) : partners.length === 0 ? (
                            <div className="text-center text-muted-foreground py-8">
                                No partners available.
                            </div>
                        ) : partners.length === 1 ? (
                            <div className="flex justify-center">
                                <motion.div
                                    className="flex flex-col items-center"
                                    initial={{ opacity: 0, scale: 0.8 }}
                                    whileInView={{ opacity: 1, scale: 1 }}
                                    viewport={{ once: true }}
                                    transition={{ delay: 0.1 }}
                                >
                                    <div
                                        className="w-32 h-24 bg-gradient-to-br from-background/50 to-background/30 backdrop-blur-sm border border-border/30 rounded-lg flex items-center justify-center mb-3 shadow-lg hover:shadow-xl transition-all duration-300 group cursor-pointer"
                                        onClick={() => {
                                            if (partners[0].website) {
                                                window.open(partners[0].website, '_blank', 'noopener,noreferrer');
                                            }
                                        }}
                                    >
                                        <img
                                            src={partners[0].logo}
                                            alt={partners[0].name}
                                            className="w-full h-full object-contain p-3 group-hover:scale-110 transition-transform duration-300"
                                            onError={(e) => {
                                                console.warn(`Failed to load logo for ${partners[0].name}:`, partners[0].logo);
                                                e.currentTarget.style.display = 'none';
                                            }}
                                        />
                                    </div>
                                    <span className="text-sm font-semibold text-foreground dark:text-white/80 text-center">
                                        {partners[0].name}
                                    </span>
                                    {partners[0].description && (
                                        <span className="text-xs text-muted-foreground text-center max-w-32 mt-1 leading-tight">
                                            {partners[0].description}
                                        </span>
                                    )}
                                    {partners[0].website && (
                                        <ExternalLink className="w-3 h-3 text-muted-foreground opacity-0 group-hover:opacity-100 transition-opacity duration-300 mt-1" />
                                    )}
                                </motion.div>
                            </div>
                        ) : partners.length <= 3 ? (
                            <div className="flex justify-center items-center gap-6">
                                {partners.map((partner, index) => (
                                    <motion.div
                                        key={partner.id}
                                        className="flex flex-col items-center"
                                        initial={{ opacity: 0, y: 20 }}
                                        whileInView={{ opacity: 1, y: 0 }}
                                        viewport={{ once: true }}
                                        transition={{ delay: index * 0.1 }}
                                    >
                                        <div
                                            className="w-28 h-20 bg-gradient-to-br from-background/50 to-background/30 backdrop-blur-sm border border-border/30 rounded-lg flex items-center justify-center mb-2 shadow-lg hover:shadow-xl transition-all duration-300 group cursor-pointer"
                                            onClick={() => {
                                                if (partner.website) {
                                                    window.open(partner.website, '_blank', 'noopener,noreferrer');
                                                }
                                            }}
                                        >
                                            <img
                                                src={partner.logo}
                                                alt={partner.name}
                                                className="w-full h-full object-contain p-2 group-hover:scale-110 transition-transform duration-300"
                                                onError={(e) => {
                                                    console.warn(`Failed to load logo for ${partner.name}:`, partner.logo);
                                                    e.currentTarget.style.display = 'none';
                                                }}
                                            />
                                        </div>
                                        <span className="text-xs font-semibold text-foreground dark:text-white/80 text-center">
                                            {partner.name}
                                        </span>
                                        {partner.description && (
                                            <span className="text-xs text-muted-foreground text-center max-w-28 mt-1 leading-tight">
                                                {partner.description}
                                            </span>
                                        )}
                                        {partner.website && (
                                            <ExternalLink className="w-3 h-3 text-muted-foreground opacity-0 group-hover:opacity-100 transition-opacity duration-300 mt-1" />
                                        )}
                                    </motion.div>
                                ))}
                            </div>
                        ) : (
                            <div className="relative">
                                <div className="flex animate-scroll">
                                    {partners.map((partner, index) => (
                                        <div
                                            key={`first-${partner.id}`}
                                            className="flex-shrink-0 flex flex-col items-center mx-3"
                                        >
                                            <div
                                                className="w-28 h-20 bg-gradient-to-br from-background/50 to-background/30 backdrop-blur-sm border border-border/30 rounded-lg flex items-center justify-center mb-2 shadow-lg hover:shadow-xl transition-all duration-300 group cursor-pointer"
                                                onClick={() => {
                                                    if (partner.website) {
                                                        window.open(partner.website, '_blank', 'noopener,noreferrer');
                                                    }
                                                }}
                                            >
                                                <img
                                                    src={partner.logo}
                                                    alt={partner.name}
                                                    className="w-full h-full object-contain p-2 group-hover:scale-110 transition-transform duration-300"
                                                    onError={(e) => {
                                                        console.warn(`Failed to load logo for ${partner.name}:`, partner.logo);
                                                        e.currentTarget.style.display = 'none';
                                                    }}
                                                />
                                            </div>
                                            <span className="text-xs font-semibold text-foreground dark:text-white/80 text-center">
                                                {partner.name}
                                            </span>
                                            {partner.description && (
                                                <span className="text-xs text-muted-foreground text-center max-w-28 mt-1 leading-tight">
                                                    {partner.description}
                                                </span>
                                            )}
                                            {partner.website && (
                                                <ExternalLink className="w-3 h-3 text-muted-foreground opacity-0 group-hover:opacity-100 transition-opacity duration-300 mt-1" />
                                            )}
                                        </div>
                                    ))}
                                    {partners.map((partner, index) => (
                                        <div
                                            key={`second-${partner.id}`}
                                            className="flex-shrink-0 flex flex-col items-center mx-3"
                                        >
                                            <div
                                                className="w-28 h-20 bg-gradient-to-br from-background/50 to-background/30 backdrop-blur-sm border border-border/30 rounded-lg flex items-center justify-center mb-2 shadow-lg hover:shadow-xl transition-all duration-300 group cursor-pointer"
                                                onClick={() => {
                                                    if (partner.website) {
                                                        window.open(partner.website, '_blank', 'noopener,noreferrer');
                                                    }
                                                }}
                                            >
                                                <img
                                                    src={partner.logo}
                                                    alt={partner.name}
                                                    className="w-full h-full object-contain p-2 group-hover:scale-110 transition-transform duration-300"
                                                    onError={(e) => {
                                                        console.warn(`Failed to load logo for ${partner.name}:`, partner.logo);
                                                        e.currentTarget.style.display = 'none';
                                                    }}
                                                />
                                            </div>
                                            <span className="text-xs font-semibold text-foreground dark:text-white/80 text-center">
                                                {partner.name}
                                            </span>
                                            {partner.description && (
                                                <span className="text-xs text-muted-foreground text-center max-w-28 mt-1 leading-tight">
                                                    {partner.description}
                                                </span>
                                            )}
                                            {partner.website && (
                                                <ExternalLink className="w-3 h-3 text-muted-foreground opacity-0 group-hover:opacity-100 transition-opacity duration-300 mt-1" />
                                            )}
                                        </div>
                                    ))}
                                </div>
                            </div>
                        )}
                    </div>

                    <motion.div
                        className="text-center mt-8"
                        initial={{ opacity: 0, y: 20 }}
                        whileInView={{ opacity: 1, y: 0 }}
                        viewport={{ once: true }}
                        transition={{ delay: 0.5 }}
                    >
                        <div className="inline-flex items-center gap-2 px-6 py-3 bg-gradient-to-r from-primary/10 to-primary/5 border border-primary/20 text-primary rounded-lg text-sm font-semibold hover:from-primary/20 hover:to-primary/10 transition-all duration-300 cursor-pointer group">
                            <Award className="w-4 h-4 group-hover:scale-110 transition-transform" />
                            Become a partner
                            <ExternalLink className="w-3 h-3 group-hover:translate-x-1 transition-transform" />
                        </div>
                    </motion.div>
                </div>
            </div>
        </div>
    );
}

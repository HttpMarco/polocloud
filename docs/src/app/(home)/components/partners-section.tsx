'use client';
import { useEffect, useState, useRef } from 'react';
import { ExternalLink, Users, Handshake, Star, Cloud, Server } from 'lucide-react';

const PartnershipInfo = () => {
    const [isVisible, setIsVisible] = useState(false);
    const infoRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const observer = new IntersectionObserver(
            ([entry]) => {
                if (entry.isIntersecting) {
                    setIsVisible(true);
                }
            },
            { threshold: 0.1, rootMargin: '0px 0px -100px 0px' }
        );

        if (infoRef.current) {
            observer.observe(infoRef.current);
        }

        return () => observer.disconnect();
    }, []);

    return (
        <div
            ref={infoRef}
            className={`bg-gradient-to-br from-blue-500/10 via-blue-500/5 to-blue-500/10 backdrop-blur-sm border border-blue-500/20 rounded-2xl p-8 transition-all duration-1000 ease-out ${
                isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
            }`}
        >
            <div className="text-center mb-8">
                <h3 className="text-2xl md:text-3xl lg:text-4xl font-black text-foreground dark:text-white mb-4 tracking-tight leading-tight">
                    Official Partner Program
                </h3>
                <p className="text-base md:text-lg lg:text-xl text-muted-foreground dark:text-white/60 max-w-3xl mx-auto leading-relaxed font-light">
                    Join our exclusive network of official partners and help shape the future of Minecraft server management.
                </p>
            </div>

            <div className="mb-8">
                <h4 className="text-xl md:text-2xl lg:text-3xl font-black text-foreground dark:text-white text-center mb-6 tracking-tight leading-tight">
                    Partner Benefits
                </h4>
                <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
                    <div className="text-center p-4 bg-blue-500/10 backdrop-blur-sm rounded-xl border border-blue-500/20 shadow-[0_0_15px_rgba(0,120,255,0.1)] dark:shadow-[0_0_15px_rgba(0,120,255,0.05)]">
                        <div className="w-12 h-12 bg-blue-500/20 rounded-full flex items-center justify-center mx-auto mb-3">
                            <Users className="w-6 h-6 text-blue-400" />
                        </div>
                        <h5 className="font-black text-foreground dark:text-white mb-2 text-base md:text-lg leading-tight">
                            Partner List
                        </h5>
                        <p className="text-xs md:text-sm text-muted-foreground dark:text-white/60 font-light">
                            Featured on polocloud.de partner list
                        </p>
                    </div>

                    <div className="text-center p-4 bg-blue-500/10 backdrop-blur-sm rounded-xl border border-blue-500/20 shadow-[0_0_15px_rgba(0,120,255,0.1)] dark:shadow-[0_0_15px_rgba(0,120,255,0.05)]">
                        <div className="w-12 h-12 bg-blue-500/20 rounded-full flex items-center justify-center mx-auto mb-3">
                            <Handshake className="w-6 h-6 text-blue-400" />
                        </div>
                        <h5 className="font-black text-foreground dark:text-white mb-2 text-base md:text-lg leading-tight">
                            Premium Support
                        </h5>
                        <p className="text-xs md:text-sm text-muted-foreground dark:text-white/60 font-light">
                            Direct contact person for support
                        </p>
                    </div>

                    <div className="text-center p-4 bg-blue-500/10 backdrop-blur-sm rounded-xl border border-blue-500/20 shadow-[0_0_15px_rgba(0,120,255,0.1)] dark:shadow-[0_0_15px_rgba(0,120,255,0.05)]">
                        <div className="w-12 h-12 bg-blue-500/20 rounded-full flex items-center justify-center mx-auto mb-3">
                            <Star className="w-6 h-6 text-blue-400" />
                        </div>
                        <h5 className="font-black text-foreground dark:text-white mb-2 text-base md:text-lg leading-tight">
                            Higher Priority
                        </h5>
                        <p className="text-xs md:text-sm text-muted-foreground dark:text-white/60 font-light">
                            Priority handling for issues
                        </p>
                    </div>

                    <div className="text-center p-4 bg-blue-500/10 backdrop-blur-sm rounded-xl border border-blue-500/20 shadow-[0_0_15px_rgba(0,120,255,0.1)] dark:shadow-[0_0_15px_rgba(0,120,255,0.05)]">
                        <div className="w-12 h-12 bg-blue-500/20 rounded-full flex items-center justify-center mx-auto mb-3">
                            <Cloud className="w-6 h-6 text-blue-400" />
                        </div>
                        <h5 className="font-black text-foreground dark:text-white mb-2 text-base md:text-lg leading-tight">
                            Custom Addons
                        </h5>
                        <p className="text-xs md:text-sm text-muted-foreground dark:text-white/60 font-light">
                            Development of special addons
                        </p>
                    </div>
                </div>
            </div>

            <div className="text-center">
                <a
                    href="https://discord.com/channels/1401907740489678912/1402064495857369189"
                    target="_blank"
                    rel="noopener noreferrer"
                    className="inline-flex items-center gap-3 px-8 py-4 bg-blue-600 hover:bg-blue-700 text-white rounded-xl font-semibold text-lg transition-all duration-300 shadow-[0_0_20px_rgba(59,130,246,0.4)] hover:shadow-[0_0_25px_rgba(59,130,246,0.6)] hover:scale-105"
                >
                    <Handshake className="w-5 h-5" />
                    Apply for Partnership
                </a>
                <p className="text-sm text-muted-foreground dark:text-white/50 mt-4">
                    Join our Discord to discuss partnership opportunities
                </p>
            </div>
        </div>
    );
};

const PartnersShowcase = () => {
    const [isVisible, setIsVisible] = useState(false);
    const partnersRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const observer = new IntersectionObserver(
            ([entry]) => {
                if (entry.isIntersecting) {
                    setIsVisible(true);
                }
            },
            { threshold: 0.1, rootMargin: '0px 0px -100px 0px' }
        );

        if (partnersRef.current) {
            observer.observe(partnersRef.current);
        }

        return () => observer.disconnect();
    }, []);

    return (
        <div
            ref={partnersRef}
            className={`text-center py-16 transition-all duration-1000 ease-out ${
                isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
            }`}
        >
            <div className="bg-card/20 backdrop-blur-sm border border-border/30 rounded-2xl p-12 max-w-2xl mx-auto">
                <div className="w-20 h-20 bg-blue-500/20 rounded-full flex items-center justify-center mx-auto mb-6">
                    <span className="text-4xl font-bold text-blue-400">?</span>
                </div>
                <h3 className="text-3xl font-bold text-foreground dark:text-white mb-4">
                    Could be you
                </h3>
                <p className="text-lg text-muted-foreground dark:text-white/60 mb-6 leading-relaxed">
                    We&apos;re looking for cloud providers and server networks to join our official partner program. Be the first to partner with PoloCloud.
                </p>
                <div className="inline-flex items-center gap-2 px-4 py-2 bg-blue-500/10 border border-blue-500/30 text-blue-400 rounded-lg text-sm font-medium">
                    <Star className="w-4 h-4" />
                    Apply now to be featured here
                </div>
            </div>
        </div>
    );
};

export function PartnersSection() {
    const [isVisible, setIsVisible] = useState(false);
    const sectionRef = useRef<HTMLElement>(null);

    useEffect(() => {
        const observer = new IntersectionObserver(
            ([entry]) => {
                if (entry.isIntersecting) {
                    setIsVisible(true);
                }
            },
            { threshold: 0.1, rootMargin: '0px 0px -100px 0px' }
        );

        if (sectionRef.current) {
            observer.observe(sectionRef.current);
        }

        return () => observer.disconnect();
    }, []);

    return (
        <section ref={sectionRef} className="relative py-32 overflow-hidden">
            <div className="absolute top-0 left-0 right-0 h-48 bg-gradient-to-b from-background via-background/95 via-background/80 to-transparent" />

            <div className="absolute inset-0 bg-gradient-to-b from-background via-muted/5 to-muted/5" />

            <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]" />

            <div className="relative container mx-auto px-6">
                <div className={`text-center mb-20 transition-all duration-1000 ease-out ${
                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                }`}>
                    <h2 className={`text-4xl md:text-5xl font-bold mb-6 bg-gradient-to-r from-foreground to-muted-foreground bg-clip-text text-transparent transition-all duration-1000 delay-200 ${
                        isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                    }`}>
                        Official Partners
                    </h2>
                    <p className={`text-xl text-muted-foreground max-w-3xl mx-auto leading-relaxed transition-all duration-1000 delay-400 ${
                        isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                    }`}>
                        We partner with leading cloud providers and server networks to deliver the best Minecraft hosting experience.
                    </p>
                </div>

                <div className={`transition-all duration-1000 delay-600 ${
                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                }`}>
                    <PartnersShowcase />
                </div>

                <div className={`transition-all duration-1000 delay-800 ${
                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                }`}>
                    <PartnershipInfo />
                </div>
            </div>

            <div className="absolute bottom-0 left-0 right-0 h-48 bg-gradient-to-t from-background via-background/95 via-background/80 to-transparent" />
        </section>
    );
}
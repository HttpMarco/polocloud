'use client';
import { useEffect, useState, useRef } from 'react';
import { ExternalLink, Users, Handshake, Star, Cloud, Server, Award, Zap, Shield, Gift } from 'lucide-react';

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

    const benefits = [
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

    return (
        <div
            ref={infoRef}
            className={`bg-gradient-to-br from-card/50 via-card/30 to-card/50 backdrop-blur-xl border border-border/50 rounded-3xl p-8 shadow-2xl transition-all duration-1000 ease-out ${
                isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
            }`}
        >
            <div className="text-center mb-12">
                <h3 className="text-3xl md:text-4xl lg:text-5xl font-black text-foreground dark:text-white mb-6 tracking-tight leading-tight">
                    Partner with PoloCloud
                </h3>
                <p className="text-lg md:text-xl text-muted-foreground dark:text-white/70 max-w-4xl mx-auto leading-relaxed">
                    Partner with PoloCloud and help shape the future of Minecraft server management.
                    Enjoy exclusive benefits and grow your business with us.
                </p>
            </div>

            <div className="mb-12">
                <h4 className="text-2xl md:text-3xl font-black text-foreground dark:text-white text-center mb-10 tracking-tight leading-tight">
                    Exclusive Partner Benefits
                </h4>
                <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {benefits.map((benefit, index) => (
                        <div
                            key={index}
                            className={`group text-center p-6 bg-gradient-to-br ${benefit.color} backdrop-blur-sm rounded-2xl border border-border/30 shadow-lg hover:shadow-xl transition-all duration-300 hover:scale-105 hover:-translate-y-1`}
                        >
                            <div className={`w-16 h-16 bg-gradient-to-br ${benefit.color} rounded-2xl flex items-center justify-center mx-auto mb-4 group-hover:scale-110 transition-transform duration-300`}>
                                <benefit.icon className={`w-8 h-8 ${benefit.iconColor}`} />
                            </div>
                            <h5 className="font-black text-foreground dark:text-white mb-3 text-lg leading-tight">
                                {benefit.title}
                            </h5>
                            <p className="text-sm text-muted-foreground dark:text-white/70 font-medium">
                                {benefit.description}
                            </p>
                        </div>
                    ))}
                </div>
            </div>

            <div className="text-center">
                <div className="bg-gradient-to-r from-primary/10 to-primary/5 border border-primary/20 rounded-2xl p-8 mb-8">
                    <h4 className="text-xl font-bold text-foreground dark:text-white mb-4">
                        Ready to Partner?
                    </h4>
                    <p className="text-muted-foreground dark:text-white/70 mb-6">
                        Join our Discord to discuss partnership opportunities and take your business to the next level.
                    </p>
                    <a
                        href="https://discord.com/channels/1401907740489678912/1402064495857369189"
                        target="_blank"
                        rel="noopener noreferrer"
                        className="inline-flex items-center gap-3 px-8 py-4 bg-gradient-to-r from-primary to-primary/90 hover:from-primary/90 hover:to-primary text-primary-foreground rounded-xl font-semibold text-lg transition-all duration-300 shadow-[0_0_20px_rgba(0,120,255,0.3)] hover:shadow-[0_0_30px_rgba(0,120,255,0.5)] hover:scale-105 group"
                    >
                        <Handshake className="w-5 h-5 group-hover:rotate-12 transition-transform" />
                        Apply for Partnership
                        <ExternalLink className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
                    </a>
                </div>
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
            <div className="bg-gradient-to-br from-card/30 via-card/20 to-card/30 backdrop-blur-xl border border-border/50 rounded-3xl p-12 max-w-3xl mx-auto shadow-2xl relative overflow-hidden">
                <div className="absolute inset-0 bg-gradient-to-br from-primary/5 via-transparent to-primary/5" />
                <div className="absolute top-0 right-0 w-32 h-32 bg-primary/10 rounded-full blur-3xl" />
                <div className="absolute bottom-0 left-0 w-24 h-24 bg-primary/10 rounded-full blur-2xl" />

                <div className="relative z-10">
                    <div className="w-24 h-24 bg-gradient-to-br from-primary/20 to-primary/10 rounded-3xl flex items-center justify-center mx-auto mb-8 shadow-lg">
                        <Star className="w-12 h-12 text-primary" />
                    </div>
                    <h3 className="text-4xl font-black text-foreground dark:text-white mb-6">
                        Could be you
                    </h3>
                    <p className="text-xl text-muted-foreground dark:text-white/70 mb-8 leading-relaxed max-w-2xl mx-auto">
                        We&apos;re looking for cloud providers and server networks to join our official partner program.
                        Be the first to partner with PoloCloud and get exclusive benefits.
                    </p>
                    <div className="inline-flex items-center gap-3 px-6 py-3 bg-gradient-to-r from-primary/10 to-primary/5 border border-primary/20 text-primary rounded-xl text-lg font-semibold">
                        <Award className="w-5 h-5" />
                        Apply now to be featured here
                    </div>
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

            <div className="absolute top-20 left-10 w-32 h-32 bg-primary/5 rounded-full blur-3xl animate-pulse" />
            <div className="absolute bottom-20 right-10 w-40 h-40 bg-primary/5 rounded-full blur-3xl animate-pulse delay-1000" />

            <div className="relative container mx-auto px-6">
                <div className={`text-center mb-20 transition-all duration-1000 ease-out ${
                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                }`}>
                    <h2 className={`text-5xl md:text-6xl lg:text-7xl font-black mb-8 bg-gradient-to-r from-foreground via-primary to-foreground bg-clip-text text-transparent transition-all duration-1000 delay-200 ${
                        isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                    }`}>
                        Official Partners
                    </h2>
                    <p className={`text-xl md:text-2xl text-muted-foreground max-w-4xl mx-auto leading-relaxed transition-all duration-1000 delay-400 ${
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
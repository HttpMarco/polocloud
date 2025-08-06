'use client';
import { useEffect, useState, useRef } from 'react';
import { Server, Zap, Shield, Users, Cloud, Code } from 'lucide-react';

interface FeatureCard {
    icon: React.ReactNode;
    title: string;
    description: string;
    color: string;
}

const features: FeatureCard[] = [
    {
        icon: <Server className="w-6 h-6" />,
        title: 'Multi Platform',
        description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
        color: 'text-blue-400'
    },
    {
        icon: <Zap className="w-6 h-6" />,
        title: 'Easy Setup',
        description: 'Get your server running in minutes with our intuitive setup process. No complex configuration required.',
        color: 'text-yellow-400'
    },
    {
        icon: <Shield className="w-6 h-6" />,
        title: 'Lorem Ipsum Feature 1',
        description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
        color: 'text-green-400'
    },
    {
        icon: <Users className="w-6 h-6" />,
        title: 'Lorem Ipsum Feature 2',
        description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
        color: 'text-purple-400'
    },
    {
        icon: <Cloud className="w-6 h-6" />,
        title: 'Developer Friendly',
        description: 'Easy to work with us and easy cloud API. Comprehensive developer tools and documentation to integrate with your workflow.',
        color: 'text-indigo-400'
    },
    {
        icon: <Code className="w-6 h-6" />,
        title: 'Multilingual',
        description: 'We have Crowdin and support many languages. Our platform is available in multiple languages for global accessibility.',
        color: 'text-orange-400'
    }
];

export function AboutSection() {
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
            <div className="absolute inset-0 bg-gradient-to-b from-background via-muted/5 to-muted/5" />

            <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]" />

            <div className="relative container mx-auto px-6">
                <div className={`text-center mb-20 transition-all duration-1000 ease-out ${
                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                }`}>
                    <h2 className={`text-4xl md:text-5xl font-bold mb-6 bg-gradient-to-r from-foreground to-muted-foreground bg-clip-text text-transparent transition-all duration-1000 delay-200 ${
                        isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                    }`}>
                        About PoloCloud
                    </h2>
                    <p className={`text-xl text-muted-foreground max-w-3xl mx-auto leading-relaxed transition-all duration-1000 delay-400 ${
                        isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                    }`}>
                        The ultimate cloud platform for Minecraft server hosting. Built for performance, designed for simplicity,
                        and trusted by thousands of server owners worldwide.
                    </p>
                </div>

                <div className={`grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8 transition-all duration-1000 delay-600 ${
                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                }`}>
                    {features.map((feature, index) => (
                        <div
                            key={feature.title}
                            className={`bg-card/40 backdrop-blur-sm border border-border/40 rounded-xl p-6 hover:bg-card/60 transition-all duration-300 hover:shadow-lg hover:border-border/60 group ${
                                isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                            }`}
                            style={{ transitionDelay: `${(index + 1) * 100}ms` }}
                        >
                            <div className={`w-12 h-12 bg-card/60 rounded-lg flex items-center justify-center mb-4 group-hover:scale-110 transition-transform duration-300 ${feature.color}`}>
                                {feature.icon}
                            </div>
                            <h3 className="font-bold text-foreground dark:text-white mb-3 text-lg">
                                {feature.title}
                            </h3>
                            <p className="text-muted-foreground dark:text-white/70 leading-relaxed">
                                {feature.description}
                            </p>
                        </div>
                    ))}
                </div>
            </div>
        </section>
    );
}
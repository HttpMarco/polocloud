'use client';

import Link from 'next/link';
import { ArrowRight, Github, BookOpen } from 'lucide-react';
import { GitHubStatsComponent } from './github-stats';
import { useEffect, useState } from 'react';

export function HeroSection() {
    const [isVisible, setIsVisible] = useState(false);

    useEffect(() => {
        const timer = setTimeout(() => {
            setIsVisible(true);
        }, 100);

        return () => clearTimeout(timer);
    }, []);

    return (
        <section className="relative overflow-hidden bg-gradient-to-br from-background via-background to-muted/20 min-h-screen flex items-center justify-center">
            <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]" />

            <div className="relative container mx-auto px-6 py-20 z-10">
                <div className={`text-center max-w-4xl mx-auto transition-all duration-1000 ease-out ${
                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                }`}>
                    <h1 className={`text-5xl md:text-7xl lg:text-8xl font-black mb-8 text-foreground dark:text-white transition-all duration-1000 delay-200 tracking-tight leading-tight ${
                        isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                    }`}>
                        PoloCloud
                    </h1>

                    <p className={`text-xl md:text-2xl lg:text-3xl text-muted-foreground mb-16 max-w-4xl mx-auto leading-relaxed font-light transition-all duration-1000 delay-400 ${
                        isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                    }`}>
                        Deploy and manage your Minecraft servers with ease.
                        <span className="block mt-2 text-lg md:text-xl lg:text-2xl font-normal">
              Built for performance, designed for simplicity.
            </span>
                    </p>

                    <div className={`transition-all duration-1000 delay-600 ${
                        isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                    }`}>
                        <GitHubStatsComponent />
                    </div>

                    <div className={`flex flex-col sm:flex-row gap-6 justify-center transition-all duration-1000 delay-800 relative z-20 ${
                        isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                    }`}>
                        <Link
                            href="/docs/cloud"
                            className="group bg-[rgba(0,120,255,0.9)] hover:bg-[rgba(0,120,255,1)] text-white px-8 py-3 rounded-xl font-semibold text-lg transition-all duration-300 shadow-[0_0_20px_rgba(0,120,255,0.3)] hover:shadow-[0_0_25px_rgba(0,120,255,0.4)] flex items-center justify-center gap-3 relative z-10"
                        >
                            <BookOpen className="w-5 h-5" />
                            Get Started
                            <ArrowRight className="w-5 h-5 group-hover:translate-x-1 transition-transform" />
                        </Link>
                        <Link
                            href="https://github.com/HttpMarco/polocloud"
                            className="group bg-card/50 hover:bg-card border border-border/50 px-8 py-3 rounded-xl font-semibold text-lg transition-all duration-300 backdrop-blur-sm shadow-lg hover:shadow-xl flex items-center justify-center gap-3 relative z-10"
                        >
                            <Github className="w-5 h-5" />
                            View on GitHub
                        </Link>
                    </div>
                </div>
            </div>

            <div className="absolute bottom-0 left-0 right-0 h-64 bg-gradient-to-t from-background via-background/95 via-background/80 to-transparent z-5" />
        </section>
    );
} 
'use client';

import { CompatibilityTable } from './compatibility-table';
import { useEffect, useState, useRef } from 'react';

export function CompatibilitySection() {
    const [isVisible, setIsVisible] = useState(false);
    const sectionRef = useRef<HTMLElement>(null);

    useEffect(() => {
        const observer = new IntersectionObserver(
            ([entry]) => {
                if (entry.isIntersecting) {
                    setIsVisible(true);
                }
            },
            {
                threshold: 0.1,
                rootMargin: '0px 0px -100px 0px'
            }
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
                    <h2 className={`text-3xl md:text-4xl lg:text-5xl font-black mb-6 bg-gradient-to-r from-foreground to-muted-foreground bg-clip-text text-transparent transition-all duration-1000 delay-200 tracking-tight leading-tight ${
                        isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                    }`}>
                        Platform Compatibility
                    </h2>
                    <p className={`text-lg md:text-xl lg:text-2xl text-muted-foreground max-w-4xl mx-auto leading-relaxed font-light transition-all duration-1000 delay-400 ${
                        isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                    }`}>
                        Check which Minecraft versions and platforms are supported by PoloCloud.
                        <span className="block mt-2 text-base md:text-lg lg:text-xl font-normal">
              Our comprehensive compatibility ensures you can run your server on your preferred setup.
            </span>
                    </p>
                </div>
                <div className={`transition-all duration-1000 delay-600 ${
                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                }`}>
                    <CompatibilityTable />
                </div>
            </div>

            <div className="absolute bottom-0 left-0 right-0 h-48 bg-gradient-to-t from-background via-background/95 via-background/80 to-transparent" />
        </section>
    );
}
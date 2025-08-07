'use client';

import { CompatibilityTable } from './compatibility-table';
import { useEffect, useState, useRef } from 'react';

export function CompatibilitySection() {
  const [isVisible, setIsVisible] = useState(false);
  const [isBuilding, setIsBuilding] = useState(false);
  const sectionRef = useRef<HTMLElement>(null);

  useEffect(() => {
    const observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) {
          setIsVisible(true);
          setTimeout(() => {
            setIsBuilding(true);
          }, 400);
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
      <div className={`absolute top-0 left-0 right-0 h-48 bg-gradient-to-b from-background via-background/95 via-background/80 to-transparent transition-all duration-1200 ${
        isVisible ? 'opacity-100' : 'opacity-0'
      }`} />

      <div className={`absolute inset-0 bg-gradient-to-b from-background via-muted/5 to-muted/5 transition-all duration-1200 delay-300 ${
        isVisible ? 'opacity-100' : 'opacity-0'
      }`} />

      <div className={`absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)] transition-all duration-1200 delay-600 ${
        isVisible ? 'opacity-100' : 'opacity-0'
      }`} />

      <div className={`absolute top-20 left-10 w-32 h-32 bg-primary/5 rounded-full blur-3xl animate-pulse transition-all duration-1200 delay-800 ${
        isVisible ? 'opacity-100 scale-100 rotate-0' : 'opacity-0 scale-0 rotate-180'
      }`} />
      <div className={`absolute bottom-20 right-10 w-40 h-40 bg-primary/5 rounded-full blur-3xl animate-pulse delay-1000 transition-all duration-1200 delay-1000 ${
        isVisible ? 'opacity-100 scale-100 -rotate-0' : 'opacity-0 scale-0 -rotate-180'
      }`} />
      <div className={`absolute top-1/2 left-1/4 w-24 h-24 bg-primary/3 rounded-full blur-2xl animate-pulse transition-all duration-1200 delay-1200 ${
        isVisible ? 'opacity-100 scale-100' : 'opacity-0 scale-0'
      }`} />

      <div className="relative container mx-auto px-6">
        <div className={`text-center mb-20 transition-all duration-1200 ease-out ${
          isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-12'
        }`}>
          <h2 className={`text-3xl md:text-4xl lg:text-5xl font-black mb-8 bg-gradient-to-r from-foreground via-primary to-foreground bg-clip-text text-transparent transition-all duration-1200 delay-300 tracking-tight leading-tight ${
            isVisible ? 'opacity-100 translate-y-0 scale-100' : 'opacity-0 translate-y-8 scale-95'
          }`}>
            Platform Compatibility
          </h2>
          <p className={`text-base md:text-lg text-muted-foreground max-w-4xl mx-auto leading-relaxed transition-all duration-1200 delay-600 ${
            isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
          }`}>
            Check which Minecraft versions and platforms are supported by PoloCloud.
            <span className={`block mt-2 text-sm md:text-base font-normal transition-all duration-1200 delay-800 ${
              isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'
            }`}>
              Our comprehensive compatibility ensures you can run your server on your preferred setup.
            </span>
          </p>
        </div>
        
        <div className={`transition-all duration-1200 delay-900 ${
          isBuilding ? 'opacity-100 translate-y-0 scale-100' : 'opacity-0 translate-y-16 scale-90'
        }`}>
          <div className={`transition-all duration-1000 delay-1000 ${
            isBuilding ? 'opacity-100' : 'opacity-0'
          }`}>
            <CompatibilityTable />
          </div>
        </div>
      </div>

      <div className={`absolute bottom-0 left-0 right-0 h-48 bg-gradient-to-t from-background via-background/95 via-background/80 to-transparent transition-all duration-1200 delay-1200 ${
        isVisible ? 'opacity-100' : 'opacity-0'
      }`} />
    </section>
  );
} 
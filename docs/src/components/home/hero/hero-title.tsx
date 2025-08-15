'use client';

import { TextGenerateEffect } from '@/utils/text-generate-effect';

interface HeroTitleProps {
    isVisible: boolean;
    latestVersion: string;
}

export function HeroTitle({ isVisible, latestVersion }: HeroTitleProps) {
    return (
        <>
            <div className={`inline-flex items-center gap-2 px-3 py-1 rounded-full bg-primary/10 border border-primary/20 text-primary text-sm font-medium mb-4 sm:mb-6 transition-all duration-1000 delay-100 ${
                isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
            }`}>
                <div className="w-2 h-2 bg-primary rounded-full animate-pulse"></div>
                <span className="text-xs sm:text-sm">{latestVersion}</span>
            </div>

            <h1 className={`text-4xl sm:text-4xl md:text-5xl lg:text-6xl xl:text-7xl 2xl:text-8xl font-black mb-4 sm:mb-6 md:mb-8 text-foreground dark:text-white transition-all duration-1000 delay-200 tracking-tight leading-tight ${
                isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
            }`}>
                <TextGenerateEffect words="PoloCloud" />
            </h1>

            <p className={`text-xl sm:text-lg md:text-xl lg:text-2xl xl:text-3xl text-muted-foreground mb-8 sm:mb-12 md:mb-16 max-w-4xl mx-auto lg:mx-0 leading-relaxed font-light transition-all duration-1000 delay-400 ${
                isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
            }`}>
                Deploy and manage your Minecraft servers with ease.
                <span className="block mt-2 text-lg sm:text-base md:text-lg lg:text-xl xl:text-2xl font-normal">
                    Built for performance, designed for simplicity.
                </span>
            </p>
        </>
    );
}

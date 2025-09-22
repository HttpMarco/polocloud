'use client';

import Link from 'next/link';
import { ArrowRight, Github, BookOpen } from 'lucide-react';

interface HeroActionsProps {
    isVisible: boolean;
}

export function HeroActions({ isVisible }: HeroActionsProps) {
    return (
        <div className={`flex flex-col sm:flex-row gap-3 sm:gap-4 md:gap-6 justify-center lg:justify-start transition-all duration-1000 delay-800 relative z-20 ${
            isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
        }`}>
            <Link
                href="/download"
                className="group bg-[rgba(0,120,255,0.9)] hover:bg-[rgba(0,120,255,1)] text-white px-4 sm:px-6 md:px-8 py-2.5 sm:py-3 rounded-xl font-semibold text-sm sm:text-base md:text-lg transition-all duration-300 shadow-[0_0_20px_rgba(0,120,255,0.3)] hover:shadow-[0_0_25px_rgba(0,120,255,0.4)] flex items-center justify-center gap-2 sm:gap-3 relative z-10"
            >
                <BookOpen className="w-4 h-4 sm:w-5 sm:h-5" />
                Download
                <ArrowRight className="w-4 h-4 sm:w-5 sm:h-5 group-hover:translate-x-1 transition-transform" />
            </Link>
            <Link
                href="https://github.com/HttpMarco/polocloud"
                className="group bg-card/50 hover:bg-card border border-border/50 px-4 sm:px-6 md:px-8 py-2.5 sm:py-3 rounded-xl font-semibold text-sm sm:text-base md:text-lg transition-all duration-300 backdrop-blur-sm shadow-lg hover:shadow-xl flex items-center justify-center gap-2 sm:gap-3 relative z-10"
            >
                <Github className="w-4 h-4 sm:w-5 sm:h-5" />
                View on GitHub
            </Link>
        </div>
    );
}

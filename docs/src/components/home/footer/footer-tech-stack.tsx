'use client';

import { Code } from 'lucide-react';

export function FooterTechStack() {
    return (
        <div className="flex flex-col gap-4 sm:col-span-2 lg:col-span-1 text-center sm:text-left">
            <h3 className="font-minecraft font-bold text-foreground dark:text-white text-lg flex items-center justify-center sm:justify-start gap-2">
                <Code className="w-5 h-5 text-primary" />
                Built with
            </h3>
            <div className="flex items-center justify-center sm:justify-start gap-3">
                <a 
                    href="https://nextjs.org" 
                    target="_blank" 
                    rel="noopener noreferrer"
                    className="text-muted-foreground dark:text-white/60 hover:text-foreground dark:hover:text-white transition-all duration-300 hover:scale-105 transform p-2 rounded-lg border border-border/30 dark:border-white/10 hover:border-border/50 dark:hover:border-white/20"
                    title="Next.js"
                >
                    <svg className="w-5 h-5" viewBox="0 0 128 128" fill="currentColor">
                        <path d="M64 0C28.7 0 0 28.7 0 64s28.7 64 64 64c11.2 0 21.7-2.9 30.8-7.9L48.4 55.3v36.6h-6.8V41.8h6.8l50.5 75.8C116.4 106.2 128 86.5 128 64c0-35.3-28.7-64-64-64zm22.1 84.6l-7.5-11.3V41.8h7.5v42.8z"/>
                    </svg>
                </a>
                <a 
                    href="https://tailwindcss.com" 
                    target="_blank" 
                    rel="noopener noreferrer"
                    className="text-muted-foreground dark:text-white/60 hover:text-foreground dark:hover:text-white transition-all duration-300 hover:scale-105 transform p-2 rounded-lg border border-border/30 dark:border-white/10 hover:border-border/50 dark:hover:border-white/20"
                    title="Tailwind CSS"
                >
                    <svg className="w-5 h-5" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M12.001,4.8c-3.2,0-5.2,1.6-6,4.8c1.2-1.6,2.6-2.2,4.2-1.8c0.913,0.228,1.565,0.89,2.288,1.624 C13.666,10.618,15.027,12,18.001,12c3.2,0,5.2-1.6,6-4.8c-1.2,1.6-2.6,2.2-4.2,1.8c-0.913-0.228-1.565-0.89-2.288-1.624 C16.337,6.182,14.976,4.8,12.001,4.8z M6.001,12c-3.2,0-5.2,1.6-6,4.8c1.2-1.6,2.6-2.2,4.2-1.8c0.913,0.228,1.565,0.89,2.288,1.624 c1.177,1.194,2.538,2.576,5.512,2.576c3.2,0,5.2-1.6,6-4.8c-1.2,1.6-2.6,2.2-4.2,1.8c-0.913-0.228-1.565-0.89-2.288-1.624 C10.337,13.382,8.976,12,6.001,12z"/>
                    </svg>
                </a>
                <a 
                    href="https://fumadocs.dev" 
                    target="_blank" 
                    rel="noopener noreferrer"
                    className="text-muted-foreground dark:text-white/60 hover:text-foreground dark:hover:text-white transition-all duration-300 hover:scale-105 transform p-2 rounded-lg border border-border/30 dark:border-white/10 hover:border-border/50 dark:hover:border-white/20"
                    title="Fumadocs"
                >
                    <svg className="w-5 h-5" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"/>
                    </svg>
                </a>
            </div>
        </div>
    );
}

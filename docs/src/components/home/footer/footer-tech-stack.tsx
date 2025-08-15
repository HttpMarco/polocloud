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

            <div className="flex items-center justify-center sm:justify-start gap-3">
                <a 
                    href="https://www.framer.com/motion/" 
                    target="_blank" 
                    rel="noopener noreferrer"
                    className="text-muted-foreground dark:text-white/60 hover:text-foreground dark:hover:text-white transition-all duration-300 hover:scale-105 transform p-2 rounded-lg border border-border/30 dark:border-white/10 hover:border-border/50 dark:hover:border-white/20"
                    title="Framer Motion"
                >
                    <svg className="w-5 h-5" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M12 0C5.373 0 0 5.373 0 12s5.373 12 12 12 12-5.373 12-12S18.627 0 12 0zm0 22C6.486 22 2 17.514 2 12S6.486 2 12 2s10 4.486 10 10-4.486 10-10 10zm-1-6v2h2v-2h1a2.5 2.5 0 1 0 0-5h-4a.5.5 0 1 1 0-1h5a2.5 2.5 0 0 1 0 5h-1v-2h-2v2h-1a.5.5 0 1 1 0 1h1zm-6-4a2.5 2.5 0 1 0 0-5H4v2h2v2h1a.5.5 0 1 1 0 1H6v2h2v2H4v2h4a2.5 2.5 0 0 0 0-5H6v-2h1z"/>
                    </svg>
                </a>
                <a 
                    href="https://ui.shadcn.com" 
                    target="_blank" 
                    rel="noopener noreferrer"
                    className="text-muted-foreground dark:text-white/60 hover:text-foreground dark:hover:text-white transition-all duration-300 hover:scale-105 transform p-2 rounded-lg border border-border/30 dark:border-white/10 hover:border-border/50 dark:hover:border-white/20"
                    title="Shadcn/ui"
                >
                    <svg className="w-5 h-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                        <path d="m19.01 11.55-7.46 7.46c-.46.46-.46 1.19 0 1.65a1.16 1.16 0 0 0 1.64 0l7.46-7.46c.46-.46.46-1.19 0-1.65s-1.19-.46-1.65 0ZM19.17 3.34c-.46-.46-1.19-.46-1.65 0L3.34 17.52c-.46.46-.46 1.19 0 1.65a1.16 1.16 0 0 0 1.64 0L19.16 4.99c.46-.46.46-1.19 0-1.65Z"/>
                    </svg>
                </a>
                <a 
                    href="https://vercel.com" 
                    target="_blank" 
                    rel="noopener noreferrer"
                    className="text-muted-foreground dark:text-white/60 hover:text-foreground dark:hover:text-white transition-all duration-300 hover:scale-105 transform p-2 rounded-lg border border-border/30 dark:border-white/10 hover:border-border/50 dark:hover:border-white/20"
                    title="Vercel"
                >
                    <svg className="w-5 h-5" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M24 22.525H0l12-21.05 12 21.05z"/>
                    </svg>
                </a>
            </div>
        </div>
    );
}

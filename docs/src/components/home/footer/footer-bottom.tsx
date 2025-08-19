'use client';

export function FooterBottom() {
    return (
        <>
            <div className="mt-8 pt-8 border-t border-border/30 dark:border-white/10">
                <div className="text-center">
                    <div className="flex flex-col sm:flex-row items-center justify-center gap-2 text-muted-foreground dark:text-white/60 font-minecraft mb-2">
                        <span className="text-sm">Made with</span>
                        <svg className="w-4 h-4 text-red-500 animate-pulse" fill="currentColor" viewBox="0 0 20 20">
                            <path fillRule="evenodd" d="M3.172 5.172a4 4 0 015.656 0L10 6.343l1.172-1.171a4 4 0 115.656 5.656L10 17.657l-6.828-6.829a4 4 0 010-5.656z" clipRule="evenodd" />
                        </svg>
                        <span className="text-sm">by</span>
                        <a 
                            href="https://github.com/jakubbbdev" 
                            target="_blank" 
                            rel="noopener noreferrer"
                            className="text-foreground dark:text-white hover:text-blue-500 dark:hover:text-blue-400 transition-all duration-300 font-medium hover:scale-105 transform text-sm"
                        >
                            jakubbbdev
                        </a>
                    </div>
                    <p className="text-sm text-muted-foreground dark:text-white/50 font-minecraft">
                        &copy; 2025 PoloCloud. All rights reserved.
                    </p>
                </div>
            </div>

            <div className="mt-6 pt-6">
                <div className="text-center">
                    <p className="text-xs text-muted-foreground/70 dark:text-white/40 font-minecraft px-4">
                        NOT AN OFFICIAL MINECRAFT SERVICE. NOT APPROVED BY OR ASSOCIATED WITH MOJANG OR MICROSOFT.
                    </p>
                </div>
            </div>
        </>
    );
}

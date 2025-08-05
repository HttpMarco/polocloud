import Link from 'next/link';
import Image from 'next/image';

export function Footer() {
    return (
        <footer className="bg-card/30 backdrop-blur-sm border-t border-border/50 dark:bg-black/40 dark:border-white/10 mt-32">
            <div className="container mx-auto px-6 py-12">
                <div className="flex flex-col md:flex-row justify-between items-center gap-8">
                    <div className="flex items-center gap-2 text-muted-foreground dark:text-white/60 font-minecraft">
                        <span>Made with</span>
                        <svg className="w-4 h-4 text-red-500 animate-pulse" fill="currentColor" viewBox="0 0 20 20">
                            <path fillRule="evenodd" d="M3.172 5.172a4 4 0 015.656 0L10 6.343l1.172-1.171a4 4 0 115.656 5.656L10 17.657l-6.828-6.829a4 4 0 010-5.656z" clipRule="evenodd" />
                        </svg>
                        <span>by</span>
                        <a
                            href="https://github.com/jakubbbdev"
                            target="_blank"
                            rel="noopener noreferrer"
                            className="text-foreground dark:text-white hover:text-blue-500 dark:hover:text-blue-400 transition-all duration-300 font-medium hover:scale-105 transform"
                        >
                            jakubbbdev
                        </a>
                    </div>

                    <div className="text-center">
                        <div className="flex items-center justify-center gap-3 mb-2">
                            <Image
                                src="/logo.png"
                                alt="PoloCloud Logo"
                                width={32}
                                height={32}
                                className="w-8 h-8 hover:scale-110 transition-transform duration-300"
                            />
                            <h3 className="text-lg font-bold text-foreground dark:text-white font-minecraft">PoloCloud</h3>
                        </div>
                        <p className="text-sm text-muted-foreground dark:text-white/60 font-minecraft">
                            Deploy and manage your Minecraft servers with ease
                        </p>
                    </div>

                    <div className="flex items-center gap-6">
                        <a
                            href="https://github.com/HttpMarco/polocloud"
                            target="_blank"
                            rel="noopener noreferrer"
                            className="text-muted-foreground dark:text-white/60 hover:text-foreground dark:hover:text-white transition-all duration-300 hover:scale-105 transform font-minecraft"
                        >
                            GitHub
                        </a>
                        <Link
                            href="/docs"
                            className="text-muted-foreground dark:text-white/60 hover:text-foreground dark:hover:text-white transition-all duration-300 hover:scale-105 transform font-minecraft"
                        >
                            Documentation
                        </Link>
                    </div>
                </div>

                <div className="mt-8 pt-8 border-t border-border/30 dark:border-white/10">
                    <div className="flex flex-col md:flex-row justify-between items-center gap-4 text-sm text-muted-foreground dark:text-white/50">
                        <span className="font-minecraft">&copy; 2025 PoloCloud. All rights reserved.</span>
                        <div className="flex items-center gap-4">
                            <span className="font-minecraft">Built with Next.js, Tailwind CSS & Fumadocs</span>
                        </div>
                    </div>
                </div>

                <div className="mt-6 pt-6 border-t border-border/20 dark:border-white/5">
                    <div className="text-center">
                        <p className="text-xs text-muted-foreground/70 dark:text-white/40 font-minecraft">
                            Not affiliated with Mojang Studios
                        </p>
                    </div>
                </div>
            </div>
        </footer>
    );
}
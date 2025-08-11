'use client';

import { useState } from 'react';
import { Menu, X, Home, BookOpen, Map, FileText, Newspaper, MessageSquare, Download, Heart, User } from 'lucide-react';
import Link from 'next/link';

export function MobileNav() {
    const [isOpen, setIsOpen] = useState(false);

    const toggleMenu = () => {
        setIsOpen(!isOpen);
    };

    const closeMenu = () => {
        setIsOpen(false);
    };

    return (
        <div className="relative">
            <button
                onClick={toggleMenu}
                className="p-2.5 rounded-xl bg-background/90 border border-border/50 hover:border-border/70 hover:bg-background/95 transition-all duration-200 shadow-md hover:shadow-lg"
                aria-label="Toggle mobile menu"
            >
                {isOpen ? (
                    <X className="w-5 h-5 text-foreground" />
                ) : (
                    <Menu className="w-5 h-5 text-foreground" />
                )}
            </button>

            {isOpen && (
                <div className="fixed inset-0 z-50 lg:hidden">
                    <div
                        className="absolute inset-0 bg-black/60 backdrop-blur-sm"
                        onClick={closeMenu}
                    />
                    
                    <div className="absolute right-0 top-16 w-80 h-full bg-background/95 backdrop-blur-xl border-l border-border/50 shadow-2xl">
                        <div className="flex items-center justify-between p-4 border-b border-border/30">
                            <h2 className="text-lg font-bold text-foreground">Menu</h2>
                            <button
                                onClick={closeMenu}
                                className="p-2 rounded-lg bg-muted/50 hover:bg-muted/70 transition-colors duration-200"
                            >
                                <X className="w-5 h-5 text-muted-foreground" />
                            </button>
                        </div>

                        <div className="p-4 space-y-6 overflow-y-auto h-[calc(100vh-5rem)]">
                            <div className="space-y-3">
                                <h3 className="text-sm font-semibold text-muted-foreground uppercase tracking-wider">
                                    Navigation
                                </h3>
                                <div className="space-y-2">
                                    <Link
                                        href="/"
                                        onClick={closeMenu}
                                        className="flex items-center gap-3 px-4 py-3 rounded-lg text-foreground hover:bg-muted/30 transition-all duration-200"
                                    >
                                        <Home className="w-5 h-5 text-primary" />
                                        <span>Home</span>
                                    </Link>
                                    <Link
                                        href="/docs/cloud"
                                        onClick={closeMenu}
                                        className="flex items-center gap-3 px-4 py-3 rounded-lg text-foreground hover:bg-muted/30 transition-all duration-200"
                                    >
                                        <BookOpen className="w-5 h-5 text-primary" />
                                        <span>Documentation</span>
                                    </Link>
                                    <Link
                                        href="/roadmap"
                                        onClick={closeMenu}
                                        className="flex items-center gap-3 px-4 py-3 rounded-lg text-foreground hover:bg-muted/30 transition-all duration-200"
                                    >
                                        <Map className="w-5 h-5 text-primary" />
                                        <span>Roadmap</span>
                                    </Link>
                                    <Link
                                        href="/changelogs"
                                        onClick={closeMenu}
                                        className="flex items-center gap-3 px-4 py-3 rounded-lg text-foreground hover:bg-muted/30 transition-all duration-200"
                                    >
                                        <FileText className="w-5 h-5 text-primary" />
                                        <span>Changelogs</span>
                                    </Link>
                                    <Link
                                        href="/blog"
                                        onClick={closeMenu}
                                        className="flex items-center gap-3 px-4 py-3 rounded-lg text-foreground hover:bg-muted/30 transition-all duration-200"
                                    >
                                        <Newspaper className="w-5 h-5 text-primary" />
                                        <span>Blog</span>
                                    </Link>
                                    <Link
                                        href="/feedback"
                                        onClick={closeMenu}
                                        className="flex items-center gap-3 px-4 py-3 rounded-lg text-foreground hover:bg-muted/30 transition-all duration-200"
                                    >
                                        <MessageSquare className="w-5 h-5 text-primary" />
                                        <span>Feedback</span>
                                    </Link>
                                </div>
                            </div>

                            <div className="space-y-3">
                                <h3 className="text-sm font-semibold text-muted-foreground uppercase tracking-wider">
                                    Actions
                                </h3>
                                <div className="space-y-2">
                                    <Link
                                        href="/download"
                                        onClick={closeMenu}
                                        className="flex items-center gap-3 px-4 py-3 rounded-lg text-foreground hover:bg-muted/30 transition-all duration-200"
                                    >
                                        <Download className="w-5 h-5 text-primary" />
                                        <span>Download</span>
                                    </Link>
                                    <Link
                                        href="/sponsors"
                                        onClick={closeMenu}
                                        className="flex items-center gap-3 px-4 py-3 rounded-lg text-foreground hover:bg-muted/30 transition-all duration-200"
                                    >
                                        <Heart className="w-5 h-5 text-primary" />
                                        <span>Sponsors</span>
                                    </Link>
                                    <Link
                                        href="/admin"
                                        onClick={closeMenu}
                                        className="flex items-center gap-3 px-4 py-3 rounded-lg text-foreground hover:bg-muted/30 transition-all duration-200"
                                    >
                                        <User className="w-5 h-5 text-primary" />
                                        <span>Dashboard</span>
                                    </Link>
                                </div>
                            </div>

                            <div className="space-y-3">
                                <h3 className="text-sm font-semibold text-muted-foreground uppercase tracking-wider">
                                    Quick Links
                                </h3>
                                <div className="space-y-2">
                                    <Link
                                        href="/docs/cloud/installation"
                                        onClick={closeMenu}
                                        className="block px-4 py-3 rounded-lg text-foreground hover:bg-muted/30 transition-all duration-200"
                                    >
                                        Installation Guide
                                    </Link>
                                    <Link
                                        href="/docs/cloud/configuration"
                                        onClick={closeMenu}
                                        className="block px-4 py-3 rounded-lg text-foreground hover:bg-muted/30 transition-all duration-200"
                                    >
                                        Configuration
                                    </Link>
                                    <Link
                                        href="/docs/cloud/api"
                                        onClick={closeMenu}
                                        className="block px-4 py-3 rounded-lg text-foreground hover:bg-muted/30 transition-all duration-200"
                                    >
                                        API Reference
                                    </Link>
                                </div>
                            </div>

                            <div className="pt-4 border-t border-border/30">
                                <div className="text-center">
                                    <p className="text-xs text-muted-foreground">
                                        Made with ❤️ by jakubbbdev
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

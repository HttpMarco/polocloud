import Link from 'next/link';
import { ArrowLeft, Search, Home, BookOpen } from 'lucide-react';

export default function NotFound() {
    return (
        <div className="min-h-screen bg-background relative overflow-hidden">
            <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]" />

            <div className="absolute top-20 left-10 w-32 h-32 bg-primary/5 rounded-full blur-3xl animate-pulse" />
            <div className="absolute bottom-20 right-10 w-40 h-40 bg-primary/3 rounded-full blur-3xl animate-pulse" />
            <div className="absolute top-1/2 left-1/4 w-24 h-24 bg-primary/2 rounded-full blur-2xl animate-pulse" />

            <div className="relative z-10 flex flex-col items-center justify-center min-h-screen px-4 text-center">
                <div className="flex items-center gap-2 text-sm text-muted-foreground mb-8">
                    <div className="w-2 h-2 bg-primary rounded-full"></div>
                    <span>404 — Page not found</span>
                </div>

                <h1 className="text-4xl md:text-6xl font-bold text-primary mb-6 leading-tight">
                    This page doesn&apos;t exist{' '}
                    <span className="text-muted-foreground">(anymore)</span>
                </h1>

                <p className="text-lg md:text-xl text-muted-foreground max-w-2xl mx-auto mb-12 leading-relaxed">
                    The page you&apos;re looking for was moved, deleted, never created or you mistyped the URL.
                    Check the homepage or dive into the documentation to continue exploring PoloCloud.
                </p>

                <div className="flex flex-col sm:flex-row gap-4 mb-16">
                    <Link
                        href="/"
                        className="inline-flex items-center gap-3 px-6 py-3 bg-primary text-primary-foreground rounded-lg hover:bg-primary/90 transition-all duration-200 font-medium group"
                    >
                        <ArrowLeft className="w-5 h-5 group-hover:-translate-x-1 transition-transform" />
                        <span>Back to homepage</span>
                    </Link>

                    <Link
                        href="/download"
                        className="inline-flex items-center gap-3 px-6 py-3 bg-muted text-foreground rounded-lg hover:bg-muted/80 transition-all duration-200 font-medium group"
                    >
                        <Search className="w-5 h-5 group-hover:scale-110 transition-transform" />
                        <span>Download PoloCloud</span>
                    </Link>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 max-w-2xl mx-auto">
                    <Link
                        href="/changelog"
                        className="flex items-center gap-3 p-4 bg-card/30 backdrop-blur-sm border border-border/50 rounded-lg hover:bg-card/50 hover:border-border/70 transition-all duration-200 group"
                    >
                        <div className="w-10 h-10 bg-primary/20 rounded-lg flex items-center justify-center group-hover:bg-primary/30 transition-colors">
                            <BookOpen className="w-5 h-5 text-primary" />
                        </div>
                        <div className="text-left">
                            <div className="font-medium text-foreground">Changelog</div>
                            <div className="text-sm text-muted-foreground">Latest updates</div>
                        </div>
                    </Link>

                    <Link
                        href="/blog"
                        className="flex items-center gap-3 p-4 bg-card/30 backdrop-blur-sm border border-border/50 rounded-lg hover:bg-card/50 hover:border-border/70 transition-all duration-200 group"
                    >
                        <div className="w-10 h-10 bg-primary/20 rounded-lg flex items-center justify-center group-hover:bg-primary/30 transition-colors">
                            <Search className="w-5 h-5 text-primary" />
                        </div>
                        <div className="text-left">
                            <div className="font-medium text-foreground">Blog</div>
                            <div className="text-sm text-muted-foreground">News & articles</div>
                        </div>
                    </Link>

                    <Link
                        href="/feedback"
                        className="flex items-center gap-3 p-4 bg-card/30 backdrop-blur-sm border border-border/50 rounded-lg hover:bg-card/50 hover:border-border/70 transition-all duration-200 group"
                    >
                        <div className="w-10 h-10 bg-primary/20 rounded-lg flex items-center justify-center group-hover:bg-primary/30 transition-colors">
                            <Home className="w-5 h-5 text-primary" />
                        </div>
                        <div className="text-left">
                            <div className="font-medium text-foreground">Feedback</div>
                            <div className="text-sm text-muted-foreground">Share your thoughts</div>
                        </div>
                    </Link>
                </div>

                <div className="mt-16 text-sm text-muted-foreground">
                    Error code: 404_NOT_FOUND
                </div>
            </div>
        </div>
    );
}

import { Clock, Sparkles, ArrowRight, Puzzle, Users, BarChart3 } from 'lucide-react';
import Link from 'next/link';

export function ComingSoonSection() {
    return (
        <section className="py-16 md:py-24 relative overflow-hidden">
            <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]" />

            <div className="container mx-auto px-6 max-w-6xl relative z-10">
                <div className="text-center mb-16">
                    <div className="flex items-center justify-center gap-3 mb-6">
                        <div className="flex items-center justify-center w-12 h-12 bg-primary/10 text-primary rounded-full border border-primary/20">
                            <Sparkles className="w-6 h-6" />
                        </div>
                    </div>
                    <h2 className="text-3xl md:text-4xl lg:text-5xl font-black bg-gradient-to-r from-foreground via-primary to-foreground bg-clip-text text-transparent mb-6 leading-tight">
                        Coming Soon
                    </h2>
                    <p className="text-base md:text-lg text-muted-foreground max-w-3xl mx-auto leading-relaxed">
                        We&apos;re working hard to bring you amazing new features. Stay tuned for exciting updates!
                    </p>
                </div>

                <div className="grid gap-8 md:grid-cols-2 lg:grid-cols-3 mb-16">
                    <div className="group bg-card/50 hover:bg-card border border-border/50 hover:border-border rounded-xl p-6 transition-all duration-300 hover:shadow-lg backdrop-blur-sm hover:scale-[1.02] transform relative overflow-hidden">
                        <div className="absolute top-4 right-4 flex items-center gap-1 bg-primary/10 text-primary px-2 py-1 rounded-full text-xs font-medium border border-primary/20">
                            <Clock className="w-3 h-3" />
                            Soon
                        </div>

                        <div className="flex items-center justify-center w-12 h-12 bg-primary/10 text-primary rounded-lg mb-4">
                            <Puzzle className="w-6 h-6" />
                        </div>

                        <h3 className="text-xl font-bold text-foreground dark:text-white mb-3">
                            Official Addons
                        </h3>
                        <p className="text-muted-foreground mb-4 leading-relaxed">
                            Expand your server with our official addons including advanced plugins and performance boosters.
                        </p>

                        <div className="flex items-center gap-2 text-sm text-muted-foreground">
                            <Clock className="w-4 h-4" />
                            <span>v3.0.0-pre-5</span>
                        </div>
                    </div>

                    <div className="group bg-card/50 hover:bg-card border border-border/50 hover:border-border rounded-xl p-6 transition-all duration-300 hover:shadow-lg backdrop-blur-sm hover:scale-[1.02] transform relative overflow-hidden">
                        <div className="absolute top-4 right-4 flex items-center gap-1 bg-primary/10 text-primary px-2 py-1 rounded-full text-xs font-medium border border-primary/20">
                            <Clock className="w-3 h-3" />
                            Soon
                        </div>

                        <div className="flex items-center justify-center w-12 h-12 bg-primary/10 text-primary rounded-lg mb-4">
                            <Users className="w-6 h-6" />
                        </div>

                        <h3 className="text-xl font-bold text-foreground dark:text-white mb-3">
                            Partner Programm
                        </h3>
                        <p className="text-muted-foreground mb-4 leading-relaxed">
                            Join our exclusive partner program and earn rewards while helping grow the PoloCloud community.
                        </p>

                        <div className="flex items-center gap-2 text-sm text-muted-foreground">
                            <Clock className="w-4 h-4" />
                            <span>v3.0.0-pre-5</span>
                        </div>
                    </div>


                    <div className="group bg-card/50 hover:bg-card border border-border/50 hover:border-border rounded-xl p-6 transition-all duration-300 hover:shadow-lg backdrop-blur-sm hover:scale-[1.02] transform relative overflow-hidden">

                        <div className="absolute top-4 right-4 flex items-center gap-1 bg-primary/10 text-primary px-2 py-1 rounded-full text-xs font-medium border border-primary/20">
                            <Clock className="w-3 h-3" />
                            Soon
                        </div>

                        <div className="flex items-center justify-center w-12 h-12 bg-primary/10 text-primary rounded-lg mb-4">
                            <BarChart3 className="w-6 h-6" />
                        </div>

                        <h3 className="text-xl font-bold text-foreground dark:text-white mb-3">
                            More Stats
                        </h3>
                        <p className="text-muted-foreground mb-4 leading-relaxed">
                            Get detailed analytics, player statistics, server performance metrics and advanced reporting tools.
                        </p>

                        <div className="flex items-center gap-2 text-sm text-muted-foreground">
                            <Clock className="w-4 h-4" />
                            <span>v3.0.0-pre-5</span>
                        </div>
                    </div>
                </div>

                <div className="text-center">
                    <div className="bg-gradient-to-r from-primary/10 via-primary/5 to-primary/10 border border-primary/20 rounded-xl p-8 max-w-2xl mx-auto">
                        <h3 className="text-xl font-bold text-foreground dark:text-white mb-3">
                            Stay Updated
                        </h3>
                        <p className="text-muted-foreground mb-6">
                            Be the first to know when these features are available. Join our community for early access and exclusive updates.
                        </p>
                        <div className="flex flex-col sm:flex-row gap-4 justify-center">
                            <a
                                href="https://discord.polocloud.de"
                                target="_blank"
                                rel="noopener noreferrer"
                                className="inline-flex items-center gap-2 bg-primary text-primary-foreground font-medium px-6 py-3 rounded-lg hover:bg-primary/90 transition-colors"
                            >
                                Join Discord
                                <ArrowRight className="w-4 h-4" />
                            </a>
                            <Link
                                href="/blog"
                                className="inline-flex items-center gap-2 bg-muted/50 text-foreground font-medium px-6 py-3 rounded-lg hover:bg-muted transition-colors border border-border/50"
                            >
                                Read Blog
                                <ArrowRight className="w-4 h-4" />
                            </Link>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
}
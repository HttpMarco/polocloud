import Link from 'next/link';
import { ArrowRight, Github, BookOpen } from 'lucide-react';
import { CompatibilityTable } from './components/compatibility-table';

export default function HomePage() {
    return (
        <main className="flex flex-1 flex-col">
            <section className="relative overflow-hidden bg-gradient-to-br from-background via-background to-muted/20 min-h-screen flex items-center justify-center">
                <div className="absolute inset-0 bg-grid-white/[0.02] bg-[size:50px_50px] dark:bg-grid-white/[0.01]" />

                <div className="relative container mx-auto px-6 py-20">
                    <div className="text-center max-w-4xl mx-auto">
                        <h2 className="text-6xl md:text-8xl font-bold mb-8 bg-gradient-to-r from-foreground via-foreground to-muted-foreground bg-clip-text text-transparent">
                            Simplest and easiest
                        </h2>
                        <h3 className="text-5xl md:text-7xl font-bold mb-12 bg-gradient-to-r from-[rgba(0,120,255,0.8)] to-[rgba(0,120,255,0.6)] bg-clip-text text-transparent">
                            Cloud for Minecraft
                        </h3>

                        <p className="text-2xl md:text-3xl text-muted-foreground mb-16 max-w-4xl mx-auto leading-relaxed">
                            Deploy and manage your Minecraft servers with ease.
                            Built for performance, designed for simplicity.
                        </p>

                        <div className="flex flex-col sm:flex-row gap-6 justify-center">
                            <Link
                                href="/docs/cloud"
                                className="group bg-[rgba(0,120,255,0.9)] hover:bg-[rgba(0,120,255,1)] text-white px-8 py-3 rounded-xl font-semibold text-lg transition-all duration-300 shadow-[0_0_20px_rgba(0,120,255,0.3)] hover:shadow-[0_0_25px_rgba(0,120,255,0.4)] flex items-center justify-center gap-3"
                            >
                                <BookOpen className="w-5 h-5" />
                                Get Started
                                <ArrowRight className="w-5 h-5 group-hover:translate-x-1 transition-transform" />
                            </Link>
                            <Link
                                href="https://github.com/HttpMarco/polocloud"
                                className="group bg-card/50 hover:bg-card border border-border/50 px-8 py-3 rounded-xl font-semibold text-lg transition-all duration-300 backdrop-blur-sm shadow-lg hover:shadow-xl flex items-center justify-center gap-3"
                            >
                                <Github className="w-5 h-5" />
                                View on GitHub
                            </Link>
                        </div>
                    </div>
                </div>
            </section>

            <section className="py-20 bg-muted/10">
                <div className="container mx-auto px-6">
                    <div className="text-center mb-12">
                        <h2 className="text-3xl md:text-4xl font-bold mb-4">Platform Compatibility</h2>
                        <p className="text-muted-foreground max-w-2xl mx-auto text-lg">
                            Check which Minecraft versions and platforms are supported by PoloCloud.
                            Our comprehensive compatibility ensures you can run your server on your preferred setup.
                        </p>
                    </div>
                    <CompatibilityTable />
                </div>
            </section>
        </main>
    );
}

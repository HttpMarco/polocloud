'use client';

import { motion } from 'framer-motion';
import { AlertTriangle, ArrowLeft } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useRouter } from 'next/navigation';
import GlobalNavbar from '@/components/global-navbar';

export default function FeatureNotAvailablePage() {
    const router = useRouter();

    const handleGoBack = () => {
        router.back();
    };

    const handleGoHome = () => {
        router.push('/');
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20">
            <GlobalNavbar />

            <main className="flex items-center justify-center h-[calc(100vh-4rem)]">
                <motion.div
                    className="text-center max-w-md px-6"
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.6 }}
                >
                    <motion.div
                        initial={{ scale: 0, rotate: -180 }}
                        animate={{ scale: 1, rotate: 0 }}
                        transition={{ duration: 0.6, delay: 0.2 }}
                        className="mx-auto w-24 h-24 bg-orange-500/20 rounded-full flex items-center justify-center mb-6"
                    >
                        <AlertTriangle className="w-12 h-12 text-orange-500" />
                    </motion.div>

                    <motion.h1
                        initial={{ y: 20, opacity: 0 }}
                        animate={{ y: 0, opacity: 1 }}
                        transition={{ duration: 0.6, delay: 0.3 }}
                        className="text-3xl font-bold text-foreground mb-4"
                    >
                        Feature Not Available
                    </motion.h1>

                    <motion.p
                        initial={{ y: 20, opacity: 0 }}
                        animate={{ y: 0, opacity: 1 }}
                        transition={{ duration: 0.6, delay: 0.4 }}
                        className="text-muted-foreground text-lg mb-8 leading-relaxed"
                    >
                        This feature is currently not available or is still under development.
                        We&apos;re working hard to bring you the best experience possible.
                    </motion.p>

                    <motion.div
                        initial={{ y: 20, opacity: 0 }}
                        animate={{ y: 0, opacity: 1 }}
                        transition={{ duration: 0.6, delay: 0.5 }}
                        className="flex flex-col sm:flex-row gap-3 justify-center"
                    >
                        <Button
                            onClick={handleGoBack}
                            variant="outline"
                            className="flex items-center gap-2 px-6 py-3 border-border/50 text-muted-foreground hover:text-foreground hover:border-border/50"
                        >
                            <ArrowLeft className="w-4 h-4" />
                            Go Back
                        </Button>

                        <Button
                            onClick={handleGoHome}
                            className="px-6 py-3 bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639)] to-[oklch(75.54%_0.1534_231.639,0.8)] hover:from-[oklch(75.54%_0.1534_231.639,0.9)] hover:to-[oklch(75.54%_0.1534_231.639,0.7)] text-white shadow-lg hover:shadow-xl transition-all duration-200"
                        >
                            Go to Dashboard
                        </Button>
                    </motion.div>
                </motion.div>
            </main>
        </div>
    );
}

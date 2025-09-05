'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { CheckCircle, AlertCircle, Server, Shield, ArrowRight } from 'lucide-react';

interface OnboardingData {
    backendUrl: string;
    username: string;
    password: string;
    confirmPassword: string;
}

interface OverviewStepProps {
    data: OnboardingData;
}

export function OverviewStep({ data }: OverviewStepProps) {
    const [isCreating] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleFinish = async () => {
        try {
            const adminUsername = localStorage.getItem('adminUsername');
            const adminPassword = localStorage.getItem('adminPassword');
            const isLoggedIn = localStorage.getItem('isLoggedIn');

            if (!adminUsername || !adminPassword || !isLoggedIn) {
                throw new Error('Admin account not found. Please go back and create the account first.');
            }

            window.location.href = '/';
        } catch (error) {
            setError(error instanceof Error ? error.message : 'Failed to launch dashboard. Please try again.');
        }
    };

    return (
        <div className="space-y-6">
            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.5 }}
                className="text-center mb-8"
            >
                <motion.div
                    className="mx-auto mb-4 size-16 rounded-full bg-[oklch(75.54% .1534 231.639,0.2)] flex items-center justify-center shadow-[0_0_20px_rgba(75,54%,15.34%,0.6)] border-2 border-[oklch(75.54% .1534 231.639)]"
                    initial={{ scale: 0 }}
                    animate={{ scale: 1 }}
                    transition={{ duration: 0.5, delay: 0.2 }}
                >
                    <CheckCircle className="size-8 text-[oklch(75.54% .1534 231.639)]" />
                </motion.div>

                <h1 className="text-3xl font-bold text-foreground mb-2">Setup Complete!</h1>
                
                <p className="text-lg text-muted-foreground">
                    Your PoloCloud instance is ready to launch
                </p>
            </motion.div>

            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.5, delay: 0.3 }}
            >
                <Card>
                    <CardHeader>
                        <CardTitle>
                            Configuration Summary
                        </CardTitle>
                        <CardDescription>
                            Here&apos;s what we&apos;ve configured for your PoloCloud instance
                        </CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-4">
                        <motion.div
                            className="flex items-start gap-4 p-5 rounded-xl bg-gradient-to-r from-blue-500/5 to-blue-500/10 border border-blue-500/20 hover:border-blue-500/30 transition-all duration-300 hover:shadow-md"
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.5, delay: 0.4 }}
                        >
                            <div className="size-12 rounded-xl bg-blue-500/15 flex items-center justify-center flex-shrink-0 border border-blue-500/20">
                                <Server className="size-6 text-blue-500" />
                            </div>
                            <div className="flex-1 min-w-0">
                                <h4 className="font-semibold text-foreground mb-2 text-lg">Backend Connection</h4>
                                <p className="text-sm text-muted-foreground mb-3 leading-relaxed">
                                    Successfully connected to your PoloCloud backend
                                </p>
                                <div className="flex items-center gap-2">
                                    <span className="text-sm font-mono text-blue-600 bg-blue-500/10 px-3 py-1.5 rounded-lg border border-blue-500/20">
                                        {data.backendUrl || 'Not configured'}
                                    </span>
                                </div>
                            </div>
                        </motion.div>

                        <motion.div
                            className="flex items-start gap-4 p-5 rounded-xl bg-gradient-to-r from-green-500/5 to-green-500/10 border border-green-500/20 hover:border-green-500/30 transition-all duration-300 hover:shadow-md"
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.5, delay: 0.5 }}
                        >
                            <div className="size-12 rounded-xl bg-green-500/15 flex items-center justify-center flex-shrink-0 border border-green-500/20">
                                <Shield className="size-6 text-green-500" />
                            </div>
                            <div className="flex-1 min-w-0">
                                <h4 className="font-semibold text-foreground mb-2 text-lg">Admin Account</h4>
                                <p className="text-sm text-muted-foreground mb-3 leading-relaxed">
                                    Administrator account ready to be created
                                </p>
                                <div className="flex items-center gap-2">
                                    <span className="text-sm font-mono text-green-600 bg-green-500/10 px-3 py-1.5 rounded-lg border border-green-500/20">
                                        Username: {data.username || 'Not configured'}
                                    </span>
                                </div>
                            </div>
                        </motion.div>

                        <motion.div
                            className="flex items-start gap-4 p-5 rounded-xl bg-gradient-to-r from-purple-500/5 to-purple-500/10 border border-purple-500/20 hover:border-purple-500/30 transition-all duration-300 hover:shadow-md"
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.5, delay: 0.6 }}
                        >
                            <div className="size-12 rounded-xl bg-purple-500/15 flex items-center justify-center flex-shrink-0 border border-purple-500/20">
                                <CheckCircle className="size-6 text-purple-500" />
                            </div>
                            <div className="flex-1 min-w-0">
                                <h4 className="font-semibold text-foreground mb-2 text-lg">System Status</h4>
                                <p className="text-sm text-muted-foreground mb-3 leading-relaxed">
                                    All systems are ready and operational
                                </p>
                                <div className="flex items-center gap-2">
                                    <span className="text-sm font-mono text-purple-600 bg-purple-500/10 px-3 py-1.5 rounded-lg border border-purple-500/20">
                                        Ready to launch
                                    </span>
                                </div>
                            </div>
                        </motion.div>
                    </CardContent>
                </Card>
            </motion.div>

            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.5, delay: 0.8 }}
                className="space-y-4"
            >
                {error && (
                    <motion.div
                        initial={{ opacity: 0, y: -10 }}
                        animate={{ opacity: 1, y: 0 }}
                        className="flex items-center space-x-3 p-4 bg-gradient-to-r from-red-500/10 to-red-500/5 border border-red-500/30 rounded-xl"
                    >
                        <AlertCircle className="h-6 w-6 text-red-500" />
                        <div className="flex-1">
                            <span className="text-red-400 font-semibold text-lg">Error</span>
                            <p className="text-red-300 text-sm mt-1">{error}</p>
                        </div>
                    </motion.div>
                )}

                <div className="flex justify-center">
                    <motion.div
                        initial={{ scale: 0.8, opacity: 0 }}
                        animate={{ scale: 1, opacity: 1 }}
                        transition={{ duration: 0.6, delay: 0.8, type: "spring", stiffness: 100 }}
                        whileHover={{ scale: 1.05 }}
                        whileTap={{ scale: 0.95 }}
                    >
                        <Button
                            onClick={handleFinish}
                            disabled={isCreating}
                            size="default"
                            className="px-6 py-2 text-sm bg-gradient-to-r from-primary to-primary/80 hover:from-primary/90 hover:to-primary/70 text-primary-foreground shadow-lg hover:shadow-xl transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed relative overflow-hidden"
                        >
                            {isCreating ? (
                                <>
                                    <motion.div
                                        className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"
                                        animate={{ rotate: 360 }}
                                        transition={{ duration: 1, repeat: Infinity, ease: "linear" }}
                                    />
                                    Launching Dashboard...
                                </>
                            ) : (
                                <>
                                    <motion.span
                                        initial={{ opacity: 0, x: -10 }}
                                        animate={{ opacity: 1, x: 0 }}
                                        transition={{ duration: 0.4, delay: 1.0 }}
                                    >
                                        Launch PoloCloud Dashboard
                                    </motion.span>
                                    <motion.div
                                        initial={{ opacity: 0, x: 10, rotate: -45 }}
                                        animate={{ opacity: 1, x: 0, rotate: 0 }}
                                        transition={{ duration: 0.4, delay: 1.2, type: "spring", stiffness: 200 }}
                                        className="ml-2"
                                    >
                                        <ArrowRight className="size-4" />
                                    </motion.div>
                                </>
                            )}
                        </Button>
                    </motion.div>
                </div>
            </motion.div>
        </div>
    );
}
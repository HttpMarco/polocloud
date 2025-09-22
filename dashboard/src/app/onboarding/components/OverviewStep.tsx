'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { CheckCircle, AlertCircle, Server, Shield, ArrowRight, Sparkles, Zap, Star } from 'lucide-react';
import { getCredentialsCookie } from '@/lib/auth-credentials';

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
            const credentials = getCredentialsCookie();
            const isLoggedIn = localStorage.getItem('isLoggedIn');

            if (!credentials || !credentials.username || !credentials.password || !isLoggedIn) {
                throw new Error('Admin account not found. Please go back and create the account first.');
            }

            window.location.href = '/';
        } catch (error) {
            setError(error instanceof Error ? error.message : 'Failed to launch dashboard. Please try again.');
        }
    };

    return (
        <div className="space-y-8">
            <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.8, ease: "easeOut" }}
                className="text-center relative"
            >
                <motion.div
                    className="absolute inset-0 -z-10"
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    transition={{ duration: 1, delay: 0.5 }}
                >
                    <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-96 h-96 bg-gradient-to-r from-[oklch(75.54% .1534 231.639,0.1)] to-[oklch(75.54% .1534 231.639,0.05)] rounded-full blur-3xl" />
                </motion.div>


                <motion.h1 
                    className="text-4xl font-bold mb-4 text-foreground"
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.6, delay: 0.6 }}
                >
                    Setup Complete!
                </motion.h1>
                
                <motion.p 
                    className="text-xl text-muted-foreground max-w-md mx-auto leading-relaxed"
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.6, delay: 0.8 }}
                >
                    Your PoloCloud instance is ready to launch and manage your Minecraft servers
                </motion.p>

                <motion.div
                    className="inline-flex items-center gap-2 px-4 py-2 mt-4 bg-muted/50 border border-border rounded-full"
                    initial={{ opacity: 0, scale: 0.8 }}
                    animate={{ opacity: 1, scale: 1 }}
                    transition={{ duration: 0.5, delay: 1 }}
                >
                    <CheckCircle className="size-4 text-muted-foreground" />
                    <span className="text-sm font-medium text-muted-foreground">All systems operational</span>
                </motion.div>
            </motion.div>

            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.5, delay: 0.4 }}
            >
                <Card className="bg-card/50 border border-border/50 shadow-lg">
                    <CardHeader className="text-center pb-4">
                        <div className="mx-auto mb-3 size-10 rounded-lg bg-muted flex items-center justify-center border border-border">
                            <Sparkles className="size-5 text-muted-foreground" />
                        </div>
                        <CardTitle className="text-xl font-semibold text-foreground">
                            Configuration Summary
                        </CardTitle>
                        <CardDescription className="text-sm text-muted-foreground">
                            Everything is configured and ready to go
                        </CardDescription>
                    </CardHeader>
                    
                    <CardContent className="space-y-3">
                        <div className="flex items-center gap-4 p-4 rounded-lg bg-blue-500/5 border border-blue-500/20">
                            <div className="size-10 rounded-lg bg-blue-500/10 flex items-center justify-center">
                                <Server className="size-5 text-blue-500" />
                            </div>
                            <div className="flex-1 min-w-0">
                                <div className="flex items-center gap-2 mb-1">
                                    <h4 className="font-semibold text-foreground">Backend Connection</h4>
                                </div>
                                <p className="text-sm text-muted-foreground mb-2">
                                    Successfully connected to your PoloCloud backend
                                </p>
                                <div className="flex items-center gap-2">
                                    <span className="text-xs font-mono text-blue-600 bg-blue-500/10 px-2 py-1 rounded">
                                        {data.backendUrl || 'Not configured'}
                                    </span>
                                    <div className="flex items-center gap-1 text-green-600">
                                        <div className="size-1.5 bg-green-500 rounded-full" />
                                        <span className="text-xs">Online</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="flex items-center gap-4 p-4 rounded-lg bg-blue-500/5 border border-blue-500/20">
                            <div className="size-10 rounded-lg bg-blue-500/10 flex items-center justify-center">
                                <Shield className="size-5 text-blue-500" />
                            </div>
                            <div className="flex-1 min-w-0">
                                <div className="flex items-center gap-2 mb-1">
                                    <h4 className="font-semibold text-foreground">Admin Account</h4>
                                </div>
                                <p className="text-sm text-muted-foreground mb-2">
                                    Administrator account created and ready
                                </p>
                                <div className="flex items-center gap-2">
                                    <span className="text-xs font-mono text-blue-600 bg-blue-500/10 px-2 py-1 rounded">
                                        @{data.username || 'Not configured'}
                                    </span>
                                    <div className="flex items-center gap-1 text-green-600">
                                        <Star className="size-3" />
                                        <span className="text-xs">Admin</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="flex items-center gap-4 p-4 rounded-lg bg-blue-500/5 border border-blue-500/20">
                            <div className="size-10 rounded-lg bg-blue-500/10 flex items-center justify-center">
                                <Zap className="size-5 text-blue-500" />
                            </div>
                            <div className="flex-1 min-w-0">
                                <div className="flex items-center gap-2 mb-1">
                                    <h4 className="font-semibold text-foreground">System Status</h4>
                                </div>
                                <p className="text-sm text-muted-foreground mb-2">
                                    All systems operational and ready
                                </p>
                                <div className="flex items-center gap-2">
                                    <span className="text-xs font-mono text-blue-600 bg-blue-500/10 px-2 py-1 rounded">
                                        Ready to Launch
                                    </span>
                                    <div className="flex items-center gap-1 text-green-600">
                                        <div className="size-1.5 bg-green-500 rounded-full" />
                                        <span className="text-xs">100%</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </CardContent>
                </Card>
            </motion.div>
            <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.8, delay: 1.0 }}
                className="space-y-6"
            >
                {error && (
                    <motion.div
                        initial={{ opacity: 0, y: -20, scale: 0.9 }}
                        animate={{ opacity: 1, y: 0, scale: 1 }}
                        className="flex items-center space-x-4 p-5 bg-gradient-to-r from-red-500/10 via-red-500/5 to-red-500/10 border border-red-500/30 rounded-2xl backdrop-blur-sm"
                    >
                        <motion.div
                            animate={{ scale: [1, 1.1, 1] }}
                            transition={{ duration: 1, repeat: Infinity }}
                        >
                            <AlertCircle className="h-6 w-6 text-red-500" />
                        </motion.div>
                        <div className="flex-1">
                            <span className="text-red-400 font-bold text-lg">Error</span>
                            <p className="text-red-300 text-sm mt-1">{error}</p>
                        </div>
                    </motion.div>
                )}
                <div className="flex justify-center">
                    <motion.div
                        className="relative"
                        initial={{ scale: 0.8, opacity: 0 }}
                        animate={{ scale: 1, opacity: 1 }}
                        transition={{ duration: 0.8, delay: 1.2, type: "spring", stiffness: 100 }}
                    >
                        <motion.div
                            className="absolute inset-0 bg-gradient-to-r from-[oklch(75.54% .1534 231.639)] to-[oklch(75.54% .1534 231.639,0.5)] blur-xl rounded-2xl"
                            animate={{ 
                                scale: [1, 1.1, 1],
                                opacity: [0.3, 0.6, 0.3]
                            }}
                            transition={{ 
                                duration: 2, 
                                repeat: Infinity, 
                                ease: "easeInOut" 
                            }}
                        />
                        
                        <Button
                            onClick={handleFinish}
                            disabled={isCreating}
                            size="lg"
                            className="relative px-8 py-4 text-lg font-bold bg-gradient-to-r from-[oklch(75.54% .1534 231.639)] via-[oklch(75.54% .1534 231.639,0.9)] to-[oklch(75.54% .1534 231.639)] hover:from-[oklch(70% .1534 231.639)] hover:via-[oklch(70% .1534 231.639,0.9)] hover:to-[oklch(70% .1534 231.639)] text-white shadow-2xl hover:shadow-[0_20px_40px_rgba(75,54%,15.34%,0.4)] transition-all duration-500 disabled:opacity-50 disabled:cursor-not-allowed rounded-2xl border-2 border-[oklch(75.54% .1534 231.639,0.3)] backdrop-blur-sm overflow-hidden group hover:scale-105 active:scale-95"
                        >
                            <motion.div
                                className="absolute inset-0 bg-gradient-to-r from-transparent via-white/20 to-transparent"
                                animate={{ x: ['-100%', '100%'] }}
                                transition={{ duration: 2, repeat: Infinity, ease: "easeInOut" }}
                            />
                            
                            <div className="relative flex items-center gap-3">
                                {isCreating ? (
                                    <>
                                        <motion.div
                                            className="animate-spin rounded-full h-6 w-6 border-b-2 border-white"
                                            animate={{ rotate: 360 }}
                                            transition={{ duration: 1, repeat: Infinity, ease: "linear" }}
                                        />
                                        <span>Launching Dashboard...</span>
                                    </>
                                ) : (
                                    <>
                                        <CheckCircle className="size-6" />
                                        <span>Launch PoloCloud Dashboard</span>
                                        <ArrowRight className="size-5" />
                                    </>
                                )}
                            </div>
                        </Button>
                    </motion.div>
                </div>

                <motion.div
                    className="text-center"
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    transition={{ duration: 0.6, delay: 1.8 }}
                >
                    <p className="text-sm text-muted-foreground">
                        Ready to manage your Minecraft servers with PoloCloud
                    </p>
                </motion.div>
            </motion.div>
        </div>
    );
}

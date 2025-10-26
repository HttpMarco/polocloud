'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { CheckCircle, AlertCircle, Wifi, Zap } from 'lucide-react';

interface OnboardingData {
    backendUrl: string;
    username: string;
    password: string;
    confirmPassword: string;
}

interface BackendConnectStepProps {
    data: OnboardingData;
    setData: (data: OnboardingData) => void;
    onNext: () => void;
}

export function BackendConnectStep({ data, setData, onNext }: BackendConnectStepProps) {
    const [isConnecting, setIsConnecting] = useState(false);
    const [isConnected, setIsConnected] = useState(false);
    const [connectionError, setConnectionError] = useState<string | null>(null);

    const handleConnect = async () => {
        if (!data.backendUrl.trim()) {
            setConnectionError('Please enter a backend URL');
            return;
        }

        setIsConnecting(true);
        setConnectionError(null);

        try {
            const response = await fetch(`/api/health?backend_ip=${encodeURIComponent(data.backendUrl.trim())}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.error || `API responded with status: ${response.status}`);
            }

            const result = await response.json();
            if (!result.success) {
                throw new Error(result.error || 'Backend connection failed');
            }

            setIsConnected(true);
            localStorage.setItem('backendIp', data.backendUrl.trim());
            document.cookie = `backend_ip=${encodeURIComponent(data.backendUrl.trim())}; path=/; max-age=${7 * 24 * 60 * 60}; secure; samesite=lax`;

            setTimeout(() => {
                onNext();
            }, 1000);
            
        } catch (error) {
            setConnectionError(`Connection failed: ${error instanceof Error ? error.message : 'Unknown error'}`);
        } finally {
            setIsConnecting(false);
        }
    };

    const handleReset = () => {
        setIsConnected(false);
        setConnectionError(null);
    };

    return (
        <div className="space-y-6">
            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.5 }}
            >
                <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-border/50 shadow-2xl backdrop-blur-sm relative overflow-hidden group">
                    <CardHeader className="text-center pb-8 relative z-10">

                        <CardTitle className="text-2xl font-bold bg-gradient-to-r from-foreground via-[oklch(0.7554 0.1534 231.639)] to-foreground bg-clip-text text-transparent mb-2">
                            Backend Connection
                        </CardTitle>
                        <CardDescription className="text-base text-muted-foreground max-w-2xl mx-auto">
                            Connect to your PoloCloud backend to get started with the setup process.
                            <br />
    
                        </CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-8 relative z-10">
                        <motion.div 
                            className="space-y-4"
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6, delay: 0.2 }}
                        >
                            <Label htmlFor="backendUrl" className="text-base font-semibold text-foreground">
                                Backend URL
                            </Label>
                            <div className="flex space-x-3">
                                <div className="relative flex-1 group">
                                    <motion.div
                                        className="absolute inset-0 bg-gradient-to-r from-[oklch(0.7554 0.1534 231.639,0.1)] to-[oklch(0.7554 0.1534 231.639,0.05)] rounded-lg blur-xl opacity-0 group-hover:opacity-100 transition-opacity duration-300"
                                        style={{ zIndex: -1 }}
                                    />
                                    <Input
                                        id="backendUrl"
                                        placeholder="127.0.0.1:8080"
                                        value={data.backendUrl}
                                        onChange={(e) => setData({ ...data, backendUrl: e.target.value })}
                                        disabled={isConnected}
                                        className="h-11 text-base border-2 border-border/50 focus:border-[oklch(0.7554 0.1534 231.639)] focus:ring-4 focus:ring-[oklch(0.7554 0.1534 231.639,0.1)] transition-all duration-300 bg-card/50 backdrop-blur-sm shadow-lg"
                                    />
                                </div>
                            </div>
                        </motion.div>

                        <motion.div
                            initial={{ opacity: 0, scale: 0.95 }}
                            animate={{ opacity: 1, scale: 1 }}
                            transition={{ duration: 0.4 }}
                        >
                            {isConnected ? (
                                <div className="space-y-3">
                                    <div className="flex items-center space-x-3 p-4 bg-gradient-to-r from-green-500/10 to-green-500/5 border border-green-500/30 rounded-xl">
                                        <motion.div
                                            initial={{ scale: 0 }}
                                            animate={{ scale: 1 }}
                                            transition={{ duration: 0.3, delay: 0.2 }}
                                        >
                                            <CheckCircle className="h-6 w-6 text-green-500" />
                                        </motion.div>
                                        <div>
                                            <span className="text-green-400 font-semibold text-lg">
                                                Connection established successfully!
                                            </span>
                                            <p className="text-green-300 text-sm mt-1">
                                                Your backend is ready and accessible
                                            </p>
                                        </div>
                                    </div>
                                    
                                    {}
                                    <div className="flex items-center space-x-3 p-4 bg-gradient-to-r from-blue-500/10 to-blue-500/5 border border-blue-500/30 rounded-xl">
                                        <motion.div
                                            initial={{ scale: 0 }}
                                            animate={{ scale: 1 }}
                                            transition={{ duration: 0.3, delay: 0.4 }}
                                        >
                                            <Wifi className="h-6 w-6 text-blue-500" />
                                        </motion.div>
                                        <div>
                                            <span className="text-blue-400 font-semibold text-lg">
                                                Middleware Active
                                            </span>
                                            <p className="text-blue-300 text-sm mt-1">
                                                API requests will now route through your backend
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            ) : connectionError ? (
                                <div className="flex items-center space-x-3 p-4 bg-gradient-to-r from-red-500/10 to-red-500/5 border border-red-500/30 rounded-xl">
                                    <AlertCircle className="h-6 w-6 text-red-500" />
                                    <div className="flex-1">
                                        <span className="text-red-400 font-semibold text-lg">Connection failed</span>
                                        <p className="text-red-300 text-sm mt-1">{connectionError}</p>
                                    </div>
                                </div>
                            ) : (
                                <div className="flex items-center space-x-3 p-4 bg-gradient-to-r from-[oklch(75.54% .1534 231.639,0.1)] to-[oklch(75.54% .1534 231.639,0.05)] border border-[oklch(75.54% .1534 231.639,0.3)] rounded-xl">
                                    <Wifi className="h-6 w-6 text-[oklch(75.54% .1534 231.639)]" />
                                    <div>
                                        <span className="text-[oklch(75.54% .1534 231.639)] font-semibold text-lg">
                                            Ready to connect
                                        </span>
                                        <p className="text-[oklch(75.54% .1534 231.639,0.8)] text-sm mt-1">
                                            Enter your backend URL and click connect
                                        </p>
                                    </div>
                                </div>
                            )}
                        </motion.div>

                        <motion.div 
                            className="pt-6 flex space-x-4"
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6, delay: 0.8 }}
                        >
                            {isConnected ? (
                                <motion.div className="flex-1" whileHover={{ scale: 1.02 }} whileTap={{ scale: 0.98 }}>
                                    <Button
                                        onClick={handleReset}
                                        variant="outline"
                                        className="w-full h-11 text-base border-2 border-red-500/30 hover:border-red-500/50 hover:bg-red-500/10 hover:text-red-500 transition-all duration-300 shadow-lg hover:shadow-xl"
                                    >
                                        <AlertCircle className="h-4 w-4 mr-2" />
                                        Reset Connection
                                    </Button>
                                </motion.div>
                            ) : (
                                <motion.div className="flex-1" whileHover={{ scale: 1.02 }} whileTap={{ scale: 0.98 }}>
                                    <Button
                                        onClick={handleConnect}
                                        variant="default"
                                        disabled={isConnecting || !data.backendUrl.trim()}
                                        className="w-full h-11 text-base text-white shadow-2xl hover:shadow-[0_20px_40px_rgba(0,0,0,0.3)] transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed font-semibold"
                                        style={{
                                            backgroundColor: 'oklch(75.54% .1534 231.639)'
                                        }}
                                        onMouseEnter={(e) => {
                                            e.currentTarget.style.backgroundColor = 'oklch(70% .1534 231.639)';
                                        }}
                                        onMouseLeave={(e) => {
                                            e.currentTarget.style.backgroundColor = 'oklch(75.54% .1534 231.639)';
                                        }}
                                    >
                                        {isConnecting ? (
                                            <>
                                                <motion.div
                                                    className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2"
                                                    animate={{ rotate: 360 }}
                                                    transition={{ duration: 1, repeat: Infinity, ease: "linear" }}
                                                />
                                                Connecting...
                                            </>
                                        ) : (
                                            <>
                                                <Zap className="h-5 w-5 mr-2" />
                                                Connect to Backend
                                            </>
                                        )}
                                    </Button>
                                </motion.div>
                            )}
                        </motion.div>
                    </CardContent>
                </Card>
            </motion.div>
        </div>
    );
}

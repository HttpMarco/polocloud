'use client';

import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Eye, EyeOff, LogIn, User, Lock, Wifi, CheckCircle, AlertCircle, Zap, Settings } from 'lucide-react';
import { useRouter } from 'next/navigation';
import { motion } from 'framer-motion';
import Image from 'next/image';
import { FlyingParticles } from './components/FlyingParticles';
import { setCredentialsCookie } from '@/lib/auth-credentials';

export default function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [backendUrl, setBackendUrl] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [isConnecting, setIsConnecting] = useState(false);
    const [isConnected, setIsConnected] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [connectionError, setConnectionError] = useState<string | null>('');
    const router = useRouter();

    const handleBackendConnect = async () => {
        if (!backendUrl.trim()) {
            setConnectionError('Please enter a backend URL');
            return;
        }

        setIsConnecting(true);
        setConnectionError(null);

        try {
            const response = await fetch(`/api/health?backend_ip=${encodeURIComponent(backendUrl.trim())}`, {
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

            localStorage.setItem('backendIp', backendUrl.trim());
            document.cookie = `backend_ip=${encodeURIComponent(backendUrl.trim())}; path=/; max-age=${7 * 24 * 60 * 60}; secure; samesite=lax`;

        } catch (error) {
            setConnectionError(`Connection failed: ${error instanceof Error ? error.message : 'Unknown error'}`);
        } finally {
            setIsConnecting(false);
        }
    };

    const handleResetConnection = () => {
        setIsConnected(false);
        setConnectionError(null);
        setBackendUrl('');
        localStorage.removeItem('backendIp');
        document.cookie = 'backend_ip=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT';
    };

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!username.trim() || !password.trim()) {
            setErrorMessage('Please fill in all fields');
            return;
        }

        if (!isConnected) {
            setErrorMessage('Please connect to backend first');
            return;
        }

        setIsLoading(true);
        setErrorMessage('');

        try {

            const backendIp = localStorage.getItem('backendIp');
            if (!backendIp) {
                throw new Error('No backend connection found. Please connect to backend first.');
            }

            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username: username.trim(),
                    password: password,
                    backendIp: backendIp
                })
            });

            if (response.ok) {
                const userResponse = await fetch('/api/auth/me');
                if (userResponse.ok) {
                    const userData = await userResponse.json();
                    const backendNeedsChange = userData.user?.hasChangedPassword === false;
                    const localPasswordChanged = localStorage.getItem(`passwordChanged_${userData.user?.username || username.trim()}`);
                    const needsPasswordChange = backendNeedsChange || (!localPasswordChanged && userData.user?.hasChangedPassword === undefined);

                    if (userData.authenticated && userData.user && needsPasswordChange) {
                        localStorage.setItem('isLoggedIn', 'true');
                        localStorage.setItem('needsPasswordChange', 'true');

                        const backendIp = localStorage.getItem('backendIp');
                        if (backendIp) {
                            setCredentialsCookie({
                                backendIp: backendIp,
                                username: username.trim(),
                                password: password
                            });
                        }

                        window.dispatchEvent(new CustomEvent('user-logged-in', {
                            detail: {
                                username: username.trim(),
                                isLoggedIn: true,
                                needsPasswordChange: true
                            }
                        }));

                        router.push('/');
                    } else {
                        localStorage.setItem('isLoggedIn', 'true');

                        const backendIp = localStorage.getItem('backendIp');
                        if (backendIp) {
                            setCredentialsCookie({
                                backendIp: backendIp,
                                username: username.trim(),
                                password: password
                            });
                        }

                        window.dispatchEvent(new CustomEvent('user-logged-in', {
                            detail: {
                                username: username.trim(),
                                isLoggedIn: true
                            }
                        }));

                        router.push('/');
                    }
                } else {
                    localStorage.setItem('isLoggedIn', 'true');

                    const backendIp = localStorage.getItem('backendIp');
                    if (backendIp) {
                        setCredentialsCookie({
                            backendIp: backendIp,
                            username: username.trim(),
                            password: password
                        });
                    }

                    window.dispatchEvent(new CustomEvent('user-logged-in', {
                        detail: {
                            username: username.trim(),
                            isLoggedIn: true
                        }
                    }));

                    router.push('/');
                }
            } else {
                const errorData = await response.json();
                throw new Error(errorData.error || 'Login failed');
            }
        } catch (error) {
            setErrorMessage(error instanceof Error ? error.message : 'Login failed');
        } finally {
            setIsLoading(false);
        }
    };

    if (!isConnected) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20 relative overflow-hidden">
                <FlyingParticles />

                <div className="flex items-center justify-center min-h-screen p-4 relative z-10">
                    <motion.div
                        className="w-full max-w-md space-y-6"
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.6, ease: "easeOut" }}
                    >
                        <motion.div
                            className="text-center space-y-4"
                            initial={{ opacity: 0, scale: 0.8 }}
                            animate={{ opacity: 1, scale: 1 }}
                            transition={{ duration: 0.8, delay: 0.2, ease: "easeOut" }}
                        >
                            <motion.div
                                className="mx-auto w-16 h-16 flex items-center justify-center"
                                initial={{ rotate: -180, scale: 0 }}
                                animate={{ rotate: 0, scale: 1 }}
                                transition={{ duration: 1, delay: 0.4, ease: "easeOut" }}
                            >
                                <Image src="/logo.png" alt="PoloCloud Logo" width={200} height={200} className="h-full w-full object-contain" />
                            </motion.div>
                            <motion.h1
                                className="text-2xl font-bold bg-gradient-to-r from-foreground via-[oklch(0.7554 0.1534 231.639)] to-foreground bg-clip-text text-transparent"
                                initial={{ opacity: 0, y: 10 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ duration: 0.6, delay: 0.6, ease: "easeOut" }}
                            >
                                Backend Connection
                            </motion.h1>
                            <motion.p
                                className="text-base text-muted-foreground"
                                initial={{ opacity: 0, y: 10 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ duration: 0.6, delay: 0.8, ease: "easeOut" }}
                            >
                                Connect to your PoloCloud backend to continue
                            </motion.p>
                        </motion.div>

                        <motion.div
                            initial={{ opacity: 0, y: 30, scale: 0.95 }}
                            animate={{ opacity: 1, y: 0, scale: 1 }}
                            transition={{ duration: 0.8, delay: 1, ease: "easeOut" }}
                        >
                            <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-border/50 shadow-2xl backdrop-blur-sm relative overflow-hidden">
                                <CardHeader className="text-center space-y-2 pb-4">
                                    <motion.div
                                        className="mx-auto mb-3 size-12 rounded-xl bg-gradient-to-br from-[oklch(0.7554 0.1534 231.639,0.2)] to-[oklch(0.7554 0.1534 231.639,0.1)] flex items-center justify-center border border-[oklch(0.7554 0.1534 231.639,0.3)] shadow-lg"
                                        whileHover={{ scale: 1.05, rotate: 5 }}
                                        transition={{ duration: 0.3 }}
                                    >
                                        <Wifi className="size-6 text-[oklch(0.7554 0.1534 231.639)]" />
                                    </motion.div>
                                    <CardTitle className="text-xl font-bold bg-gradient-to-r from-foreground via-[oklch(0.7554 0.1534 231.639)] to-foreground bg-clip-text text-transparent">
                                        Connect Backend
                                    </CardTitle>
                                    <CardDescription className="text-sm text-muted-foreground">
                                        Enter your backend URL to establish connection
                                    </CardDescription>
                                </CardHeader>
                                <CardContent className="space-y-4">
                                    <motion.div
                                        className="space-y-3"
                                        initial={{ opacity: 0, x: -20 }}
                                        animate={{ opacity: 1, x: 0 }}
                                        transition={{ duration: 0.6, delay: 1.2, ease: "easeOut" }}
                                    >
                                        <Label htmlFor="backendUrl" className="text-base font-semibold text-foreground flex items-center gap-2">
                                            <Wifi className="size-4 text-[oklch(0.7554 0.1534 231.639)]" />
                                            Backend URL
                                        </Label>
                                        <Input
                                            id="backendUrl"
                                            type="text"
                                            placeholder="127.0.0.1:8080"
                                            value={backendUrl}
                                            onChange={(e) => setBackendUrl(e.target.value)}
                                            disabled={isConnecting}
                                            className="h-9 text-sm border-2 border-border/50 focus:border-[oklch(0.7554 0.1534 231.639)] focus:ring-4 focus:ring-[oklch(0.7554 0.1534 231.639,0.1)] transition-all duration-300 bg-card/50 backdrop-blur-sm shadow-lg"
                                        />
                                    </motion.div>

                                    {}
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
                                                        <CheckCircle className="h-5 w-5 text-green-500" />
                                                    </motion.div>
                                                    <div>
                                                        <span className="text-green-400 font-semibold text-sm">
                                                            Connection established successfully!
                                                        </span>
                                                        <p className="text-green-300 text-xs mt-1">
                                                            Your backend is ready and accessible
                                                        </p>
                                                    </div>
                                                </div>

                                                <div className="flex items-center space-x-3 p-4 bg-gradient-to-r from-blue-500/10 to-blue-500/5 border border-blue-500/30 rounded-xl">
                                                    <motion.div
                                                        initial={{ scale: 0 }}
                                                        animate={{ scale: 1 }}
                                                        transition={{ duration: 0.3, delay: 0.4 }}
                                                    >
                                                        <Wifi className="h-5 w-5 text-blue-500" />
                                                    </motion.div>
                                                    <div>
                                                        <span className="text-blue-400 font-semibold text-sm">
                                                            Middleware Active
                                                        </span>
                                                        <p className="text-blue-300 text-xs mt-1">
                                                            API requests will now route through your backend
                                                        </p>
                                                    </div>
                                                </div>
                                            </div>
                                        ) : connectionError ? (
                                            <div className="flex items-center space-x-3 p-4 bg-gradient-to-r from-red-500/10 to-red-500/5 border border-red-500/30 rounded-xl">
                                                <AlertCircle className="h-5 w-5 text-red-500" />
                                                <div className="flex-1">
                                                    <span className="text-red-400 font-semibold text-sm">Connection failed</span>
                                                    <p className="text-red-300 text-xs mt-1">{connectionError}</p>
                                                </div>
                                            </div>
                                        ) : (
                                            <div className="flex items-center space-x-3 p-4 bg-gradient-to-r from-[oklch(75.54% .1534 231.639,0.1)] to-[oklch(75.54% .1534 231.639,0.05)] border border-[oklch(75.54% .1534 231.639,0.3)] rounded-xl">
                                                <Wifi className="h-5 w-5 text-[oklch(75.54% .1534 231.639)]" />
                                                <div>
                                                    <span className="text-[oklch(75.54% .1534 231.639)] font-semibold text-sm">
                                                        Ready to connect
                                                    </span>
                                                    <p className="text-[oklch(75.54% .1534 231.639,0.8)] text-xs mt-1">
                                                        Enter your backend URL and click connect
                                                    </p>
                                                </div>
                                            </div>
                                        )}
                                    </motion.div>

                                    <motion.div
                                        className="pt-4 flex space-x-4"
                                        initial={{ opacity: 0, y: 20 }}
                                        animate={{ opacity: 1, y: 0 }}
                                        transition={{ duration: 0.6, delay: 1.4, ease: "easeOut" }}
                                    >
                                        {isConnected ? (
                                            <motion.div className="flex-1" whileHover={{ scale: 1.02 }} whileTap={{ scale: 0.98 }}>
                                                <Button
                                                    onClick={handleResetConnection}
                                                    variant="outline"
                                                    className="w-full h-9 text-sm border-2 border-red-500/30 hover:border-red-500/50 hover:bg-red-500/10 hover:text-red-500 transition-all duration-300 shadow-lg hover:shadow-xl"
                                                >
                                                    <AlertCircle className="h-4 w-4 mr-2" />
                                                    Reset Connection
                                                </Button>
                                            </motion.div>
                                        ) : (
                                            <motion.div className="flex-1" whileHover={{ scale: 1.02 }} whileTap={{ scale: 0.98 }}>
                                                <Button
                                                    onClick={handleBackendConnect}
                                                    variant="default"
                                                    disabled={isConnecting || !backendUrl.trim()}
                                                    className="w-full h-9 text-sm text-white shadow-2xl hover:shadow-[0_20px_40px_rgba(0,0,0,0.3)] transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed font-semibold"
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

                        <motion.div
                            className="text-center space-y-4"
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6, delay: 1.5, ease: "easeOut" }}
                        >
                            <motion.div
                                className="relative"
                                whileHover={{ scale: 1.02 }}
                                whileTap={{ scale: 0.98 }}
                            >
                                <Button
                                    variant="outline"
                                    className="w-full h-10 text-sm font-medium border-2 border-dashed border-muted-foreground/30 hover:border-primary/50 hover:bg-primary/5 transition-all duration-300"
                                    onClick={() => router.push('/onboarding')}
                                >
                                    <Settings className="h-4 w-4 mr-2" />
                                    You are server owner?
                                </Button>
                            </motion.div>

                            <div className="relative">
                                <div className="absolute inset-0 flex items-center">
                                    <span className="w-full border-t border-muted-foreground/20" />
                                </div>
                                <div className="relative flex justify-center text-xs uppercase">
                                    <span className="bg-background px-2 text-muted-foreground">or</span>
                                </div>
                            </div>

                            <div className="space-y-2">
                                <p className="text-sm text-muted-foreground">
                                    Connect to existing backend
                                </p>
                            </div>
                        </motion.div>

                    </motion.div>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20 relative overflow-hidden">
            <FlyingParticles />

            <div className="flex items-center justify-center min-h-screen p-4 relative z-10">
                <motion.div
                    className="w-full max-w-sm space-y-6"
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.6, ease: "easeOut" }}
                >
                    <motion.div
                        className="text-center space-y-4"
                        initial={{ opacity: 0, scale: 0.8 }}
                        animate={{ opacity: 1, scale: 1 }}
                        transition={{ duration: 0.8, delay: 0.2, ease: "easeOut" }}
                    >
                        <motion.div
                            className="mx-auto w-16 h-16 flex items-center justify-center"
                            initial={{ rotate: -180, scale: 0 }}
                            animate={{ rotate: 0, scale: 1 }}
                            transition={{ duration: 1, delay: 0.4, ease: "easeOut" }}
                        >
                            <Image src="/logo.png" alt="PoloCloud Logo" width={200} height={200} className="h-full w-full object-contain" />
                        </motion.div>
                        <motion.h1
                            className="text-2xl font-bold bg-gradient-to-r from-foreground via-[oklch(0.7554 0.1534 231.639)] to-foreground bg-clip-text text-transparent"
                            initial={{ opacity: 0, y: 10 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6, delay: 0.6, ease: "easeOut" }}
                        >
                            Welcome back
                        </motion.h1>
                        <motion.p
                            className="text-base text-muted-foreground"
                            initial={{ opacity: 0, y: 10 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6, delay: 0.8, ease: "easeOut" }}
                        >
                            Sign in to your PoloCloud account
                        </motion.p>
                    </motion.div>

                    {}
                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.6, delay: 1, ease: "easeOut" }}
                    >
                        <div className="flex items-center space-x-3 p-4 bg-gradient-to-r from-green-500/10 to-green-500/5 border border-green-500/30 rounded-xl">
                            <CheckCircle className="h-5 w-5 text-green-500" />
                            <div>
                                <span className="text-green-400 font-semibold text-sm">
                                    Backend Connected
                                </span>
                                <p className="text-green-300 text-xs mt-1">
                                    {localStorage.getItem('backendIp')}
                                </p>
                            </div>
                        </div>
                    </motion.div>

                    <motion.div
                        initial={{ opacity: 0, y: 30, scale: 0.95 }}
                        animate={{ opacity: 1, y: 0, scale: 1 }}
                        transition={{ duration: 0.8, delay: 1.2, ease: "easeOut" }}
                    >
                        <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-border/50 shadow-2xl backdrop-blur-sm relative overflow-hidden">
                            <CardHeader className="text-center space-y-2 pb-4">
                                <motion.div
                                    className="mx-auto mb-3 size-12 rounded-xl bg-gradient-to-br from-[oklch(0.7554 0.1534 231.639,0.2)] to-[oklch(0.7554 0.1534 231.639,0.1)] flex items-center justify-center border border-[oklch(0.7554 0.1534 231.639,0.3)] shadow-lg"
                                    whileHover={{ scale: 1.05, rotate: 5 }}
                                    transition={{ duration: 0.3 }}
                                >
                                    <User className="size-6 text-[oklch(0.7554 0.1534 231.639)]" />
                                </motion.div>
                                <CardTitle className="text-xl font-bold bg-gradient-to-r from-foreground via-[oklch(0.7554 0.1534 231.639)] to-foreground bg-clip-text text-transparent">
                                    Sign In
                                </CardTitle>
                                <CardDescription className="text-sm text-muted-foreground">
                                    Enter your credentials to access your account
                                </CardDescription>
                            </CardHeader>
                            <CardContent className="space-y-4">
                                <form onSubmit={handleLogin} className="space-y-4">
                                    <motion.div
                                        className="space-y-3"
                                        initial={{ opacity: 0, x: -20 }}
                                        animate={{ opacity: 1, x: 0 }}
                                        transition={{ duration: 0.6, delay: 1.4, ease: "easeOut" }}
                                    >
                                        <Label htmlFor="username" className="text-base font-semibold text-foreground flex items-center gap-2">
                                            <User className="size-4 text-[oklch(0.7554 0.1534 231.639)]" />
                                            Username
                                        </Label>
                                        <div className="flex items-center space-x-4">
                                            <div className="flex-1 group">
                                                <Input
                                                    id="username"
                                                    type="text"
                                                    placeholder="Enter your username"
                                                    value={username}
                                                    onChange={(e) => setUsername(e.target.value)}
                                                    disabled={isLoading}
                                                    className="h-9 text-sm border-2 border-border/50 focus:border-[oklch(0.7554 0.1534 231.639)] focus:ring-4 focus:ring-[oklch(0.7554 0.1534 231.639,0.1)] transition-all duration-300 bg-card/50 backdrop-blur-sm shadow-lg"
                                                />
                                            </div>
                                            <motion.div
                                                className="relative"
                                                whileHover={{ scale: 1.05 }}
                                                transition={{ duration: 0.2 }}
                                            >
                                                <div className="w-9 h-9 rounded-lg overflow-hidden border-2 border-[oklch(0.7554 0.1534 231.639,0.3)] bg-muted/20 shadow-lg">
                                                    <Image
                                                        src={username.trim() ? `https://mineskin.eu/helm/${username}/64` : 'https://mineskin.eu/helm/MHF_Question/64'}
                                                        alt={`${username || 'Username'} Minecraft Head`}
                                                        width={36}
                                                        height={36}
                                                        className="w-9 h-9 object-cover"
                                                        onError={(e) => {
                                                            e.currentTarget.src = 'https://mineskin.eu/helm/MHF_Question/64'
                                                        }}
                                                    />
                                                </div>
                                            </motion.div>
                                        </div>
                                    </motion.div>

                                    <motion.div
                                        className="space-y-3"
                                        initial={{ opacity: 0, x: -20 }}
                                        animate={{ opacity: 1, x: 0 }}
                                        transition={{ duration: 0.6, delay: 1.6, ease: "easeOut" }}
                                    >
                                        <Label htmlFor="password" className="text-base font-semibold text-foreground flex items-center gap-2">
                                            <Lock className="size-4 text-[oklch(0.7554 0.1534 231.639)]" />
                                            Password
                                        </Label>
                                        <div className="relative">
                                            <Input
                                                id="password"
                                                type={showPassword ? 'text' : 'password'}
                                                placeholder="Enter your password"
                                                value={password}
                                                onChange={(e) => setPassword(e.target.value)}
                                                disabled={isLoading}
                                                className="pr-10 h-9 text-sm border-2 border-border/50 focus:border-[oklch(0.7554 0.1534 231.639)] transition-all duration-300 bg-card/50"
                                            />
                                            <Button
                                                type="button"
                                                variant="ghost"
                                                size="sm"
                                                className="absolute right-0 top-0 h-full px-3 hover:bg-transparent"
                                                onClick={() => setShowPassword(!showPassword)}
                                                disabled={isLoading}
                                            >
                                                {showPassword ? (
                                                    <EyeOff className="h-4 w-4 text-muted-foreground" />
                                                ) : (
                                                    <Eye className="h-4 w-4 text-muted-foreground" />
                                                )}
                                            </Button>
                                        </div>
                                    </motion.div>

                                    {}
                                    {errorMessage && (
                                        <motion.div
                                            initial={{ opacity: 0, y: 10 }}
                                            animate={{ opacity: 1, y: 0 }}
                                            transition={{ duration: 0.3 }}
                                            className="flex items-center space-x-3 p-4 bg-gradient-to-r from-red-500/10 to-red-500/5 border border-red-500/30 rounded-xl"
                                        >
                                            <div className="size-2 bg-red-500 rounded-full"></div>
                                            <p className="text-sm text-red-400 font-medium">
                                                {errorMessage}
                                            </p>
                                        </motion.div>
                                    )}

                                    <motion.div
                                        initial={{ opacity: 0, y: 20 }}
                                        animate={{ opacity: 1, y: 0 }}
                                        transition={{ duration: 0.6, delay: 1.8, ease: "easeOut" }}
                                        className="pt-4"
                                    >
                                        <motion.div whileHover={{ scale: 1.02 }} whileTap={{ scale: 0.98 }}>
                                            <Button
                                                type="submit"
                                                className="w-full h-9 text-sm text-white shadow-2xl hover:shadow-[0_20px_40px_rgba(0,0,0,0.3)] transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed font-semibold"
                                                disabled={isLoading}
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
                                                {isLoading ? (
                                                    <>
                                                        <motion.div
                                                            className="animate-spin rounded-full h-6 w-6 border-b-2 border-white mr-3"
                                                            animate={{ rotate: 360 }}
                                                            transition={{ duration: 1, repeat: Infinity, ease: "linear" }}
                                                        />
                                                        Signing in...
                                                    </>
                                                ) : (
                                                    <>
                                                        <LogIn className="h-6 w-6 mr-3" />
                                                        Sign In
                                                    </>
                                                )}
                                            </Button>
                                        </motion.div>
                                    </motion.div>
                                </form>
                            </CardContent>
                        </Card>
                    </motion.div>

                    <motion.div
                        className="text-center space-y-4"
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.6, delay: 2, ease: "easeOut" }}
                    >
                        <motion.div
                            className="relative"
                            whileHover={{ scale: 1.02 }}
                            whileTap={{ scale: 0.98 }}
                        >
                            <Button
                                variant="outline"
                                className="w-full h-10 text-sm font-medium border-2 border-dashed border-muted-foreground/30 hover:border-primary/50 hover:bg-primary/5 transition-all duration-300"
                                onClick={() => router.push('/onboarding')}
                            >
                                <Settings className="h-4 w-4 mr-2" />
                                You are server owner?
                            </Button>
                        </motion.div>

                        <div className="relative">
                            <div className="absolute inset-0 flex items-center">
                                <span className="w-full border-t border-muted-foreground/20" />
                            </div>
                            <div className="relative flex justify-center text-xs uppercase">
                                <span className="bg-background px-2 text-muted-foreground">or</span>
                            </div>
                        </div>

                        <div className="space-y-2">
                            <p className="text-sm text-muted-foreground">
                                Don&apos;t have an account?{' '}
                            </p>
                            <p className="text-sm text-muted-foreground">
                                Contact a Server Administrator to create an account.
                            </p>
                        </div>
                    </motion.div>
                </motion.div>
            </div>
        </div>
    );
}

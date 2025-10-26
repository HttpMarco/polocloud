'use client';

import { useState, useEffect, useCallback } from 'react'
import Image from 'next/image';
import { motion } from 'framer-motion';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { User, Lock, Eye, EyeOff, ArrowRight, AlertCircle } from 'lucide-react';
import { setCredentialsCookie } from '@/lib/auth-credentials';

interface OnboardingData {
    backendUrl: string;
    username: string;
    password: string;
    confirmPassword: string;
}

interface AdminAccountStepProps {
    data: OnboardingData;
    setData: (data: OnboardingData) => void;
    onNext: () => void;
}

export function AdminAccountStep({ data, setData, onNext }: AdminAccountStepProps) {
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [isValid, setIsValid] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [isCreating, setIsCreating] = useState(false);

    const validateForm = useCallback(() => {
        const isValidForm =
            data.username.trim().length >= 3 &&
            data.password.length >= 8 &&
            data.password === data.confirmPassword &&
            data.password.trim() !== '';

        setIsValid(isValidForm);
        return isValidForm;
    }, [data.username, data.password, data.confirmPassword]);

    useEffect(() => {
        validateForm();
    }, [data.username, data.password, data.confirmPassword, validateForm]);

    const handleInputChange = (field: keyof OnboardingData, value: string) => {
        setData({ ...data, [field]: value });
    };

    const handleCreateAdminAccount = async () => {
        if (!isValid) return;
        
        setIsCreating(true);
        setError(null);

        try {
            const backendIp = localStorage.getItem('backendIp') || data.backendUrl;
            if (!backendIp) {
                throw new Error('Backend IP not found. Please connect to backend first.');
            }

            const response = await fetch('/api/onboarding/create-admin', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    username: data.username,
                    password: data.password,
                    roleId: -1,
                    backendIp: backendIp
                })
            });

            if (response.ok) {
                setCredentialsCookie({
                    backendIp: backendIp,
                    username: data.username,
                    password: data.password
                });

                localStorage.setItem('isLoggedIn', 'true');
                
                onNext();
            } else {
                const errorData = await response.json();
                throw new Error(errorData.error || `Failed to create admin account: ${response.status}`);
            }
        } catch (error) {

            setError(error instanceof Error ? error.message : 'Failed to create admin account. Please try again.');
        } finally {
            setIsCreating(false);
        }
    };

    const getPasswordStrength = (password: string) => {
        if (password.length === 0) return { strength: 0, color: 'bg-muted', text: '' };
        if (password.length < 8) return { strength: 1, color: 'bg-red-500', text: 'Weak' };
        if (password.length < 12) return { strength: 2, color: 'bg-yellow-500', text: 'Medium' };
        return { strength: 3, color: 'bg-green-500', text: 'Strong' };
    };

    const passwordStrength = getPasswordStrength(data.password);

    return (
        <div className="space-y-6">
            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.5 }}
            >
                <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-border/50 shadow-2xl backdrop-blur-sm relative overflow-hidden group">
                    <CardHeader className="text-center pb-8 relative z-10">
                        <motion.div
                            className="mx-auto mb-4 size-16 rounded-2xl bg-gradient-to-br from-[oklch(0.7554 0.1534 231.639,0.2)] to-[oklch(0.7554 0.1534 231.639,0.1)] flex items-center justify-center border border-[oklch(0.7554 0.1534 231.639,0.3)] shadow-lg"
                            whileHover={{ scale: 1.05, rotate: 5 }}
                            transition={{ duration: 0.3 }}
                        >
                            <User className="size-8 text-[oklch(0.7554 0.1534 231.639)]" />
                        </motion.div>
                        <CardTitle className="text-3xl font-bold bg-gradient-to-r from-foreground via-[oklch(0.7554 0.1534 231.639)] to-foreground bg-clip-text text-transparent mb-3">
                            Create Admin Account
                        </CardTitle>
                        <CardDescription className="text-lg text-muted-foreground max-w-2xl mx-auto">
                            Set up your first administrator account for PoloCloud
                        </CardDescription>

                        <div className="mt-6 pt-6 border-t border-border/30">
                            <p className="text-sm text-muted-foreground mb-3 font-medium">Requirements:</p>
                            <div className="flex flex-wrap justify-center gap-4 text-sm text-muted-foreground">
                                <span className="flex items-center gap-2 px-3 py-2 bg-green-500/10 rounded-lg border border-green-500/20">
                                    <div className="size-2 bg-green-500 rounded-full"></div>
                                    Use your Minecraft name
                                </span>
                                <span className="flex items-center gap-2 px-3 py-2 bg-green-500/10 rounded-lg border border-green-500/20">
                                    <div className="size-2 bg-green-500 rounded-full"></div>
                                    Password: min. 8 characters
                                </span>
                            </div>
                        </div>
                    </CardHeader>
                    <CardContent className="space-y-8 relative z-10">
                        <motion.div
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6, delay: 0.2 }}
                            className="space-y-4"
                        >
                            <Label htmlFor="username" className="text-lg font-semibold text-foreground flex items-center gap-2">
                                <User className="size-5 text-[oklch(0.7554 0.1534 231.639)]" />
                                Username
                            </Label>
                            <div className="flex items-center space-x-4">
                                <div className="flex-1 group">
                                    <Input
                                        id="username"
                                        placeholder="admin"
                                        value={data.username}
                                        onChange={(e) => handleInputChange('username', e.target.value)}
                                        className="h-10 text-sm border border-border/50 focus:border-[oklch(0.7554 0.1534 231.639)] focus:ring-2 focus:ring-[oklch(0.7554 0.1534 231.639,0.2)] transition-all duration-300 bg-background/50"
                                    />
                                </div>
                                <motion.div 
                                    className="relative"
                                    whileHover={{ scale: 1.05 }}
                                    transition={{ duration: 0.2 }}
                                >
                                    <div className="w-10 h-10 rounded-lg overflow-hidden border border-[oklch(0.7554 0.1534 231.639,0.3)] bg-muted/20 shadow-lg">
                                        <Image 
                                            src={data.username.trim() ? `https://mineskin.eu/helm/${data.username}/64` : 'https://mineskin.eu/helm/MHF_Question/64'}
                                            alt={`${data.username || 'Username'} Minecraft Head`}
                                            width={40}
                                            height={40}
                                            className="w-10 h-10 object-cover"
                                            onError={(e) => {
                                                e.currentTarget.src = 'https://mineskin.eu/helm/MHF_Question/64'
                                            }}
                                        />
                                    </div>
                                </motion.div>
                            </div>
                            {data.username.trim().length > 0 && data.username.trim().length < 3 && (
                                <motion.p
                                    initial={{ opacity: 0, y: -10 }}
                                    animate={{ opacity: 1, y: 0 }}
                                    className="text-sm text-red-400 flex items-center gap-2"
                                >
                                    <AlertCircle className="size-4" />
                                    Username must be at least 3 characters long
                                </motion.p>
                            )}
                        </motion.div>

                        <motion.div
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6, delay: 0.4 }}
                            className="space-y-4"
                        >
                            <Label htmlFor="password" className="text-lg font-semibold text-foreground flex items-center gap-2">
                                <Lock className="size-5 text-[oklch(0.7554 0.1534 231.639)]" />
                                Password
                            </Label>
                            <div className="relative">
                                <Lock className="absolute left-3 top-3 h-4 w-4 text-[oklch(0.7554 0.1534 231.639)]" />
                                <Input
                                    id="password"
                                    type={showPassword ? 'text' : 'password'}
                                    placeholder="••••••••"
                                    value={data.password}
                                    onChange={(e) => handleInputChange('password', e.target.value)}
                                    className="pl-10 pr-10 h-10 text-sm border border-border/50 focus:border-[oklch(0.7554 0.1534 231.639)] focus:ring-2 focus:ring-[oklch(0.7554 0.1534 231.639,0.2)] transition-all duration-300 bg-background/50"
                                />
                                <Button
                                    type="button"
                                    variant="ghost"
                                    size="sm"
                                    className="absolute right-0 top-0 h-full px-2 hover:bg-transparent"
                                    onClick={() => setShowPassword(!showPassword)}
                                >
                                    {showPassword ? <EyeOff className="h-4 w-4" /> : <Eye className="h-4 w-4" />}
                                </Button>
                            </div>

                            {data.password.length > 0 && (
                                <motion.div
                                    initial={{ opacity: 0, y: -10 }}
                                    animate={{ opacity: 1, y: 0 }}
                                    className="space-y-2"
                                >
                                    <div className="flex space-x-1">
                                        {[1, 2, 3].map((level) => (
                                            <motion.div
                                                key={level}
                                                className={`h-2 flex-1 rounded transition-all ${
                                                    level <= passwordStrength.strength ? passwordStrength.color : 'bg-muted'
                                                }`}
                                                initial={{ scaleX: 0 }}
                                                animate={{ scaleX: 1 }}
                                                transition={{ duration: 0.3, delay: level * 0.1 }}
                                            />
                                        ))}
                                    </div>
                                    <p className={`text-sm font-medium ${
                                        passwordStrength.strength === 1 ? 'text-red-400' :
                                            passwordStrength.strength === 2 ? 'text-yellow-400' :
                                                'text-green-400'
                                    }`}>
                                        {passwordStrength.text}
                                    </p>
                                </motion.div>
                            )}

                            {data.password.length > 0 && data.password.length < 8 && (
                                <motion.p
                                    initial={{ opacity: 0, y: -10 }}
                                    animate={{ opacity: 1, y: 0 }}
                                    className="text-sm text-red-400 flex items-center gap-2"
                                >
                                    <AlertCircle className="size-4" />
                                    Password must be at least 8 characters long
                                </motion.p>
                            )}
                        </motion.div>

                        <motion.div
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6, delay: 0.6 }}
                            className="space-y-4"
                        >
                            <Label htmlFor="confirmPassword" className="text-lg font-semibold text-foreground flex items-center gap-2">
                                <Lock className="size-5 text-[oklch(0.7554 0.1534 231.639)]" />
                                Confirm Password
                            </Label>
                            <div className="relative">
                                <Lock className="absolute left-3 top-3 h-4 w-4 text-[oklch(0.7554 0.1534 231.639)]" />
                                <Input
                                    id="confirmPassword"
                                    type={showConfirmPassword ? 'text' : 'password'}
                                    placeholder="••••••••"
                                    value={data.confirmPassword}
                                    onChange={(e) => handleInputChange('confirmPassword', e.target.value)}
                                    className="pl-10 pr-10 h-10 text-sm border border-border/50 focus:border-[oklch(0.7554 0.1534 231.639)] focus:ring-2 focus:ring-[oklch(0.7554 0.1534 231.639,0.2)] transition-all duration-300 bg-background/50"
                                />
                                <Button
                                    type="button"
                                    variant="ghost"
                                    size="sm"
                                    className="absolute right-0 top-0 h-full px-2 hover:bg-transparent"
                                    onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                                >
                                    {showConfirmPassword ? <EyeOff className="h-4 w-4" /> : <Eye className="h-4 w-4" />}
                                </Button>
                            </div>

                            {data.confirmPassword.length > 0 && data.password !== data.confirmPassword && (
                                <motion.p
                                    initial={{ opacity: 0, y: -10 }}
                                    animate={{ opacity: 1, y: 0 }}
                                    className="text-sm text-red-400 flex items-center gap-2"
                                >
                                    <AlertCircle className="size-4" />
                                    Passwords do not match
                                </motion.p>
                            )}
                        </motion.div>

                        <motion.div
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.6, delay: 0.8 }}
                            className="pt-6"
                        >
                            <motion.div whileHover={{ scale: 1.02 }} whileTap={{ scale: 0.98 }}>
                                <Button
                                    onClick={handleCreateAdminAccount}
                                    disabled={!isValid || isCreating}
                                    className="w-full h-10 text-sm text-white shadow-lg hover:shadow-xl transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed font-medium"
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
                                    {isCreating ? (
                                        <>
                                            <motion.div
                                                className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"
                                                animate={{ rotate: 360 }}
                                                transition={{ duration: 1, repeat: Infinity, ease: "linear" }}
                                            />
                                            Creating Admin Account...
                                        </>
                                    ) : (
                                        <>
                                            <ArrowRight className="h-4 w-4 mr-2" />
                                            Next Step
                                        </>
                                    )}
                                </Button>
                            </motion.div>
                        </motion.div>

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
                    </CardContent>
                </Card>
            </motion.div>
        </div>
    );
}

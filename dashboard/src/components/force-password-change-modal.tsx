'use client';

import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Dialog, DialogContent } from '@/components/ui/dialog';
import { Lock, Eye, EyeOff, Check } from 'lucide-react';
import { motion } from 'framer-motion';
import { API_ENDPOINTS } from '@/lib/api';

interface ForcePasswordChangeModalProps {
    isOpen: boolean;
    onPasswordChanged: () => void;
}

export function ForcePasswordChangeModal({ isOpen, onPasswordChanged }: ForcePasswordChangeModalProps) {
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [showNewPassword, setShowNewPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!newPassword.trim() || !confirmPassword.trim()) {
            setError('Please fill in all fields');
            return;
        }

        if (newPassword !== confirmPassword) {
            setError('Passwords do not match');
            return;
        }

        if (newPassword.length < 8) {
            setError('Password must be at least 8 characters long');
            return;
        }

        setIsLoading(true);
        setError('');

        try {
            const response = await fetch(API_ENDPOINTS.USER.CHANGE_PASSWORD, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    newPassword: newPassword.trim()
                })
            });

            const result = await response.json();

            if (result.success) {
                const username = localStorage.getItem('adminUsername');
                if (username) {
                    localStorage.setItem(`passwordChanged_${username}`, 'true');
                }

                setSuccess(true);
                setTimeout(() => {
                    onPasswordChanged();
                }, 2000);
            } else {
                setError(result.message || 'Failed to change password');
            }
        } catch {
            setError('Failed to change password. Please try again.');
        } finally {
            setIsLoading(false);
        }
    };



    return (
        <Dialog open={isOpen} onOpenChange={() => {}} modal>
            <DialogContent className="bg-background/95 border border-border/50 backdrop-blur-sm max-w-md">
                <motion.div
                    initial={{ scale: 0.9, opacity: 0 }}
                    animate={{ scale: 1, opacity: 1 }}
                    transition={{ duration: 0.2 }}
                    className="text-center"
                >
                    {!success ? (
                        <>
                            <motion.div
                                initial={{ scale: 0, rotate: -180 }}
                                animate={{ scale: 1, rotate: 0 }}
                                transition={{ duration: 0.4, delay: 0.1 }}
                                className="mx-auto w-16 h-16 bg-amber-500/20 rounded-full flex items-center justify-center mb-4"
                            >
                                <Lock className="w-8 h-8 text-amber-500" />
                            </motion.div>

                            <motion.h3
                                initial={{ y: 20, opacity: 0 }}
                                animate={{ y: 0, opacity: 1 }}
                                transition={{ duration: 0.3, delay: 0.2 }}
                                className="text-xl font-bold text-foreground mb-2"
                            >
                                Change Your Password
                            </motion.h3>

                            <motion.p
                                initial={{ y: 20, opacity: 0 }}
                                animate={{ y: 0, opacity: 1 }}
                                transition={{ duration: 0.3, delay: 0.3 }}
                                className="text-muted-foreground mb-6"
                            >
                                For security reasons, you must change your password before continuing.
                            </motion.p>

                            <form onSubmit={handleSubmit} className="space-y-4">
                                {error && (
                                    <motion.div
                                        initial={{ opacity: 0, y: -10 }}
                                        animate={{ opacity: 1, y: 0 }}
                                        className="bg-red-50 border border-red-200 rounded-lg p-3"
                                    >
                                        <p className="text-red-700 text-sm">{error}</p>
                                    </motion.div>
                                )}

                                <div className="space-y-2">
                                    <Label htmlFor="new-password" className="text-sm font-medium text-left block">
                                        New Password
                                    </Label>
                                    <div className="relative">
                                        <Input
                                            id="new-password"
                                            type={showNewPassword ? "text" : "password"}
                                            placeholder="Enter new password"
                                            value={newPassword}
                                            onChange={(e) => setNewPassword(e.target.value)}
                                            disabled={isLoading}
                                            className="pr-10"
                                        />
                                        <Button
                                            type="button"
                                            variant="ghost"
                                            size="sm"
                                            className="absolute right-0 top-0 h-full px-3 py-2 hover:bg-transparent"
                                            onClick={() => setShowNewPassword(!showNewPassword)}
                                            disabled={isLoading}
                                        >
                                            {showNewPassword ? (
                                                <EyeOff className="h-4 w-4" />
                                            ) : (
                                                <Eye className="h-4 w-4" />
                                            )}
                                        </Button>
                                    </div>
                                </div>

                                <div className="space-y-2">
                                    <Label htmlFor="confirm-password" className="text-sm font-medium text-left block">
                                        Confirm Password
                                    </Label>
                                    <div className="relative">
                                        <Input
                                            id="confirm-password"
                                            type={showConfirmPassword ? "text" : "password"}
                                            placeholder="Confirm new password"
                                            value={confirmPassword}
                                            onChange={(e) => setConfirmPassword(e.target.value)}
                                            disabled={isLoading}
                                            className="pr-10"
                                        />
                                        <Button
                                            type="button"
                                            variant="ghost"
                                            size="sm"
                                            className="absolute right-0 top-0 h-full px-3 py-2 hover:bg-transparent"
                                            onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                                            disabled={isLoading}
                                        >
                                            {showConfirmPassword ? (
                                                <EyeOff className="h-4 w-4" />
                                            ) : (
                                                <Eye className="h-4 w-4" />
                                            )}
                                        </Button>
                                    </div>
                                </div>

                                <motion.div
                                    initial={{ y: 20, opacity: 0 }}
                                    animate={{ y: 0, opacity: 1 }}
                                    transition={{ duration: 0.3, delay: 0.4 }}
                                    className="flex justify-center pt-4"
                                >
                                    <Button
                                        type="submit"
                                        disabled={isLoading || !newPassword.trim() || !confirmPassword.trim() || newPassword !== confirmPassword || newPassword.length < 8}
                                        className="bg-amber-600 hover:bg-amber-700 text-white min-w-[120px]"
                                    >
                                        {isLoading ? (
                                            <>
                                                <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin mr-2" />
                                                Changing...
                                            </>
                                        ) : (
                                            <>
                                                <Lock className="w-4 h-4 mr-2" />
                                                Change Password
                                            </>
                                        )}
                                    </Button>
                                </motion.div>
                            </form>
                        </>
                    ) : (
                        <>
                            <motion.div
                                initial={{ scale: 0, rotate: -180 }}
                                animate={{ scale: 1, rotate: 0 }}
                                transition={{ duration: 0.4, delay: 0.1 }}
                                className="mx-auto w-16 h-16 bg-green-500/20 rounded-full flex items-center justify-center mb-4"
                            >
                                <Check className="w-8 h-8 text-green-500" />
                            </motion.div>

                            <motion.h3
                                initial={{ y: 20, opacity: 0 }}
                                animate={{ y: 0, opacity: 1 }}
                                transition={{ duration: 0.3, delay: 0.2 }}
                                className="text-xl font-bold text-foreground mb-2"
                            >
                                Password Changed Successfully!
                            </motion.h3>

                            <motion.p
                                initial={{ y: 20, opacity: 0 }}
                                animate={{ y: 0, opacity: 1 }}
                                transition={{ duration: 0.3, delay: 0.3 }}
                                className="text-muted-foreground mb-6"
                            >
                                You can now continue to the dashboard.
                            </motion.p>
                        </>
                    )}
                </motion.div>
            </DialogContent>
        </Dialog>
    );
}

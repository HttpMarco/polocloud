"use client"

import { useState, useEffect } from 'react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from '@/components/ui/dialog'
import { Plus, Copy, RefreshCw, Check } from 'lucide-react'
import { motion, AnimatePresence } from 'framer-motion'
import Image from 'next/image'
import { usePermissions } from '@/hooks/usePermissions'

interface AddUserModalProps {
    onUserAdded: (username: string, password: string) => void
}

function generatePassword(): string {
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*'
    let password = ''
    for (let i = 0; i < 12; i++) {
        password += chars.charAt(Math.floor(Math.random() * chars.length))
    }
    return password
}

export function AddUserModal({ onUserAdded }: AddUserModalProps) {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [isOpen, setIsOpen] = useState(false)
    const [isLoading, setIsLoading] = useState(false)
    const [copied, setCopied] = useState(false)
    const [showSuccess, setShowSuccess] = useState(false)
    const [error, setError] = useState('')

    const { hasPermission } = usePermissions()
    const canCreateUser = hasPermission('polocloud.user.create')

    const withLoading = async (fn: () => Promise<void>) => {
        try {
            setIsLoading(true);
            setError('');
            await fn();
        } catch (error) {
            setError(error instanceof Error ? error.message : 'Ein Fehler ist aufgetreten');
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        if (isOpen) {
            setPassword('')
            setShowSuccess(false)
            setError('')
        }
    }, [isOpen])

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        if (!username.trim()) return

        await withLoading(async () => {
            const backendIp = localStorage.getItem('backendIp')
            if (!backendIp) {
                throw new Error('Backend IP nicht gefunden. Bitte verbinde dich zuerst mit dem Backend.')
            }

            const response = await fetch(`/api/users`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username: username.trim(),
                    roleId: 0,
                    backendIp: backendIp
                }),
            })

            if (!response.ok) {
                const errorData = await response.json()
                throw new Error(errorData.message || 'Fehler beim Erstellen des Users')
            }

            const result = await response.json()

            const finalPassword = result.password || generatePassword()

            setPassword(finalPassword)

            setShowSuccess(true)

            setTimeout(() => {
                onUserAdded(username.trim(), finalPassword)
                setUsername('')
                setPassword('')
                setIsOpen(false)
                setShowSuccess(false)
            }, 3000)
        })
    }

    const generateNewPassword = () => {
        setPassword(generatePassword())
    }

    const copyPassword = async () => {
        try {
            await navigator.clipboard.writeText(password)
            setCopied(true)
            setTimeout(() => setCopied(false), 2000)
        } catch (error) {
        console.error('Error in add-user-modal:', error);
      }}

    return (
        <Dialog open={isOpen} onOpenChange={canCreateUser ? setIsOpen : undefined}>
            <DialogTrigger asChild disabled={!canCreateUser}>
                <motion.div
                    whileHover={{ scale: canCreateUser ? 1.02 : 1 }}
                    whileTap={{ scale: canCreateUser ? 0.98 : 1 }}
                >
                    <Button
                        disabled={!canCreateUser}
                        className={`h-9 px-4 text-sm font-medium transition-all duration-200 ${
                            canCreateUser 
                                ? 'hover:opacity-90 shadow-lg shadow-[0_0_20px_rgba(75.54%,15.34%,231.639,0.3)] hover:shadow-[0_0_30px_rgba(75.54%,15.34%,231.639,0.4)]' 
                                : 'opacity-40 cursor-not-allowed bg-muted text-muted-foreground border border-border/30'
                        }`}
                        style={{ 
                            backgroundColor: canCreateUser ? 'oklch(75.54% .1534 231.639)' : undefined
                        }}
                    >
                        <Plus className="w-4 h-4 mr-2" />
                        Add User
                    </Button>
                </motion.div>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[500px]">
                <AnimatePresence mode="wait">
                    {!showSuccess ? (
                        <motion.div
                            key="form"
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            exit={{ opacity: 0, y: -20 }}
                            transition={{ duration: 0.3 }}
                        >
                            <DialogHeader>
                                <DialogTitle className="text-xl font-bold">Add New User</DialogTitle>
                                <DialogDescription>
                                    Enter the username for the new user. A secure password will be generated automatically.
                                </DialogDescription>
                            </DialogHeader>

                            {}
                            {error && (
                                <motion.div
                                    initial={{ opacity: 0, y: -10 }}
                                    animate={{ opacity: 1, y: 0 }}
                                    className="bg-red-50 border border-red-200 rounded-lg p-3 mb-4"
                                >
                                    <p className="text-red-700 text-sm">{error}</p>
                                </motion.div>
                            )}

                            <form onSubmit={handleSubmit}>
                                <div className="space-y-6 py-4">
                                    {}
                                    <div className="space-y-3">
                                        <Label htmlFor="username" className="text-sm font-medium">
                                            Username
                                        </Label>
                                        <div className="flex items-center space-x-3">
                                            <div className="w-12 h-12 rounded-lg overflow-hidden border border-border">
                                                <Image
                                                    src={`https://mineskin.eu/helm/${username || 'MHF_Question'}/64`}
                                                    alt={`${username || 'User'} Minecraft Head`}
                                                    width={48}
                                                    height={48}
                                                    className="w-12 h-12"
                                                    onError={(e) => {
                                                        e.currentTarget.src = 'https://mineskin.eu/helm/MHF_Question/64'
                                                    }}
                                                />
                                            </div>
                                            <Input
                                                id="username"
                                                value={username}
                                                onChange={(e) => setUsername(e.target.value)}
                                                placeholder="Enter username"
                                                className="flex-1"
                                                disabled={isLoading}
                                            />
                                        </div>
                                    </div>

                                    {password && (
                                        <div className="space-y-3">
                                            <Label htmlFor="password" className="text-sm font-medium">
                                                Generated Password
                                            </Label>
                                            <div className="flex items-center space-x-2">
                                                <Input
                                                    id="password"
                                                    value={password}
                                                    readOnly
                                                    className="flex-1 font-mono text-sm"
                                                    disabled={isLoading}
                                                />
                                                <Button
                                                    type="button"
                                                    variant="outline"
                                                    size="sm"
                                                    onClick={copyPassword}
                                                    disabled={isLoading}
                                                    className="px-3"
                                                >
                                                    <AnimatePresence mode="wait">
                                                        {copied ? (
                                                            <motion.div
                                                                key="check"
                                                                initial={{ scale: 0 }}
                                                                animate={{ scale: 1 }}
                                                                exit={{ scale: 0 }}
                                                            >
                                                                <Check className="w-4 h-4 text-green-600" />
                                                            </motion.div>
                                                        ) : (
                                                            <motion.div
                                                                key="copy"
                                                                initial={{ scale: 0 }}
                                                                animate={{ scale: 1 }}
                                                                exit={{ scale: 0 }}
                                                            >
                                                                <Copy className="w-4 h-4" />
                                                            </motion.div>
                                                        )}
                                                    </AnimatePresence>
                                                </Button>
                                                <Button
                                                    type="button"
                                                    variant="outline"
                                                    size="sm"
                                                    onClick={generateNewPassword}
                                                    disabled={isLoading}
                                                    className="px-3"
                                                >
                                                    <RefreshCw className="w-4 h-4" />
                                                </Button>
                                            </div>
                                            <p className="text-xs text-muted-foreground">
                                                Click the refresh button to generate a new password, or copy the current one.
                                            </p>
                                        </div>
                                    )}
                                </div>
                                <DialogFooter>
                                    <Button
                                        type="button"
                                        variant="outline"
                                        onClick={() => setIsOpen(false)}
                                        disabled={isLoading}
                                    >
                                        Cancel
                                    </Button>
                                    <Button
                                        type="submit"
                                        disabled={!username.trim() || isLoading}
                                        style={{ backgroundColor: 'oklch(75.54% .1534 231.639)' }}
                                        className="hover:opacity-90"
                                    >
                                        {isLoading ? (
                                            <motion.div
                                                animate={{ rotate: 360 }}
                                                transition={{ duration: 1, repeat: Infinity, ease: "linear" }}
                                                className="mr-2"
                                            >
                                                <RefreshCw className="w-4 h-4" />
                                            </motion.div>
                                        ) : null}
                                        {isLoading ? 'Creating...' : 'Create User'}
                                    </Button>
                                </DialogFooter>
                            </form>
                        </motion.div>
                    ) : (
                        <motion.div
                            key="success"
                            initial={{ opacity: 0, scale: 0.8 }}
                            animate={{ opacity: 1, scale: 1 }}
                            exit={{ opacity: 0, scale: 0.8 }}
                            transition={{ duration: 0.4 }}
                            className="flex flex-col items-center justify-center py-12"
                        >
                            <motion.div
                                initial={{ scale: 0 }}
                                animate={{ scale: 1 }}
                                transition={{ delay: 0.2, type: "spring", stiffness: 200 }}
                                className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mb-4"
                            >
                                <Check className="w-8 h-8 text-green-600" />
                            </motion.div>
                            <motion.h3
                                initial={{ opacity: 0, y: 10 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ delay: 0.3 }}
                                className="text-xl font-bold mb-2"
                            >
                                User Created Successfully!
                            </motion.h3>
                            <motion.p
                                initial={{ opacity: 0, y: 10 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ delay: 0.4 }}
                                className="text-muted-foreground text-center mb-4"
                            >
                                The new user &quot;{username}&quot; has been created successfully!
                            </motion.p>

                            <motion.div
                                initial={{ opacity: 0, y: 10 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ delay: 0.5 }}
                                className="bg-muted/50 border border-border rounded-lg p-4 w-full max-w-sm"
                            >
                                <p className="text-sm font-medium text-foreground mb-2">Generated Password:</p>
                                <div className="flex items-center space-x-2">
                                    <code className="bg-background px-3 py-2 rounded text-sm font-mono text-primary flex-1">
                                        {password}
                                    </code>
                                    <Button
                                        type="button"
                                        variant="outline"
                                        size="sm"
                                        onClick={copyPassword}
                                        className="px-3"
                                    >
                                        {copied ? (
                                            <Check className="w-4 h-4 text-green-600" />
                                        ) : (
                                            <Copy className="w-4 h-4" />
                                        )}
                                    </Button>
                                </div>
                                <p className="text-xs text-muted-foreground mt-2">
                                    Please save this password securely!
                                </p>
                            </motion.div>
                        </motion.div>
                    )}
                </AnimatePresence>
            </DialogContent>
        </Dialog>
    )
}

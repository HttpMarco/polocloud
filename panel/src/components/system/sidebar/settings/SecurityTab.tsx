import {Button} from '@/components/ui/button';
import {Trash2, Monitor, Smartphone, Globe, Laptop, Tablet, Lock, Eye, EyeOff} from 'lucide-react';
import {toast} from 'sonner';
import {useEffect, useState} from 'react';
import {tokensApi, userApi} from '@/lib/api';
import {Input} from '@/components/ui/input';
import {Label} from '@/components/ui/label';
import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogDescription,
} from '@/components/ui/dialog';
import { permissionService } from '@/lib/utils/PermissionService';

interface TokenData {
    ip: string;
    userUUID: string;
    userAgent: string;
    lastActivity: number;
}

interface ParsedTokenData extends TokenData {
    device: string;
    browser: string;
    location: string;
    isCurrent: boolean;
    formattedLastActivity: string;
}

export function SecurityTab() {
    const [tokens, setTokens] = useState<ParsedTokenData[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [revealedIPs, setRevealedIPs] = useState<Set<string>>(new Set());

    const [isPasswordModalOpen, setIsPasswordModalOpen] = useState(false);
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [showNewPassword, setShowNewPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [isChangingPassword, setIsChangingPassword] = useState(false);

    useEffect(() => {
        fetchTokens();
    }, []);

    const toggleIPVisibility = (ip: string) => {
        setRevealedIPs(prev => {
            const newSet = new Set(prev);
            if (newSet.has(ip)) {
                newSet.delete(ip);
            } else {
                newSet.add(ip);
            }
            return newSet;
        });
    };

    const fetchTokens = async () => {
        if (!permissionService.hasPermission('polocloud.user.self.tokens')) {
            setTokens([]);
            return;
        }

        setIsLoading(true);
        try {
            const result = await tokensApi.getTokens();


            if (result.success && result.data) {
                if (Array.isArray(result.data)) {
                    const parsedTokens = await parseTokensData(result.data);
                    setTokens(parsedTokens);
                } else {
                    return;
                }
            } else {
            }
        } catch (error) {
        } finally {
            setIsLoading(false);
        }
    };

    const parseTokensData = async (rawTokens: any[]): Promise<ParsedTokenData[]> => {
        const parsedTokens: ParsedTokenData[] = [];


        for (const token of rawTokens) {
            try {

                if (!token.ip || !token.userAgent || !token.userUUID) {
                    continue;
                }

                const {device, browser} = parseUserAgent(token.userAgent);
                const location = await getLocationFromIP(token.ip);
                const isCurrent = false;
                const lastActivity = formatLastActivity(token.lastActivity);

                const parsedToken: ParsedTokenData = {
                    ip: token.ip,
                    userUUID: token.userUUID,
                    userAgent: token.userAgent,
                    lastActivity: token.lastActivity,
                    device,
                    browser,
                    location,
                    isCurrent,
                    formattedLastActivity: lastActivity
                };

                parsedTokens.push(parsedToken);

            } catch (error) {
                parsedTokens.push({
                    ip: token.ip || 'Unknown',
                    userUUID: token.userUUID || 'Unknown',
                    userAgent: token.userAgent || 'Unknown',
                    lastActivity: token.lastActivity || 0,
                    device: 'Unknown',
                    browser: 'Unknown',
                    location: 'Unknown',
                    isCurrent: false,
                    formattedLastActivity: 'Unknown'
                });
            }
        }

        return parsedTokens;
    };

    const parseUserAgent = (userAgent: string): { device: string; browser: string } => {
        const ua = userAgent.toLowerCase();

        let device = 'Unknown';
        if (ua.includes('mobile') || ua.includes('android') || ua.includes('iphone')) {
            device = 'Mobile';
        } else if (ua.includes('tablet') || ua.includes('ipad')) {
            device = 'Tablet';
        } else if (ua.includes('windows') || ua.includes('macintosh') || ua.includes('linux')) {
            device = 'Desktop';
        } else if (ua.includes('laptop')) {
            device = 'Laptop';
        }

        let browser = 'Unknown';
        if (ua.includes('chrome')) {
            browser = 'Chrome';
        } else if (ua.includes('firefox')) {
            browser = 'Firefox';
        } else if (ua.includes('safari') && !ua.includes('chrome')) {
            browser = 'Safari';
        } else if (ua.includes('edge')) {
            browser = 'Edge';
        } else if (ua.includes('opera')) {
            browser = 'Opera';
        }

        return {device, browser};
    };

    const getLocationFromIP = async (ip: string): Promise<string> => {
        try {

            if (ip.startsWith('192.168.') || ip.startsWith('10.') || ip.startsWith('172.') || ip === 'localhost' || ip === '127.0.0.1') {
                return 'Local Network';
            }

            const response = await fetch(`https://ipapi.co/${ip}/json/`);

            if (!response.ok) {
                return 'Unknown Location';
            }

            const data = await response.json();

            if (data.city && data.country_name) {
                return `${data.city}, ${data.country_name}`;
            } else if (data.country_name) {
                return data.country_name;
            } else if (data.region) {
                return data.region;
            } else {
                return 'Unknown Location';
            }
        } catch (error) {
            return 'Unknown Location';
        }
    };

    const handleChangePassword = () => {
        if (!permissionService.hasPermission('polocloud.user.self.change-password')) {
            toast.error('No permission to change password. Required permission: polocloud.user.self.change-password');
            return;
        }

        setIsPasswordModalOpen(true);
    };

    const handlePasswordChangeSubmit = async () => {
        if (!newPassword.trim() || !confirmPassword.trim()) {
            toast.error('Please fill in all fields');
            return;
        }

        if (newPassword !== confirmPassword) {
            toast.error('Passwords do not match');
            return;
        }

        if (newPassword.length < 8) {
            toast.error('Password must be at least 8 characters long');
            return;
        }

        if (!permissionService.hasPermission('polocloud.user.self.change-password')) {
            toast.error('No permission to change password. Required permission: polocloud.user.self.change-password');
            return;
        }

        setIsChangingPassword(true);
        try {
            const result = await userApi.changePassword(newPassword);

            if (result.success) {
                toast.success('Password changed successfully! You will be logged out in 3 seconds...');
                setIsPasswordModalOpen(false);
                setNewPassword('');
                setConfirmPassword('');

                setTimeout(() => {
                    window.location.href = '/login';
                }, 3000);
            } else {
                toast.error(`Failed to change password: ${result.message}`);
            }
        } catch (error) {
            toast.error('Failed to change password. Please try again.');
        } finally {
            setIsChangingPassword(false);
        }
    };

    const handlePasswordModalClose = () => {
        setIsPasswordModalOpen(false);
        setNewPassword('');
        setConfirmPassword('');
        setShowNewPassword(false);
        setShowConfirmPassword(false);
    };

    const handleRevokeSession = async () => {
        if (!permissionService.hasPermission('polocloud.user.self.token.delete')) {
            toast.error('No permission to revoke sessions. Required permission: polocloud.user.self.token.delete');
            return;
        }

        try {
            
            toast.info('Session management functionality is currently unavailable.');
        } catch (error) {
            
            toast.error('Failed to revoke session. Please try again.');
        }
    };


    const getDeviceIcon = (device: string) => {
        switch (device.toLowerCase()) {
            case 'desktop':
                return <Monitor className="h-4 w-4"/>;
            case 'laptop':
                return <Laptop className="h-4 w-4"/>;
            case 'mobile':
                return <Smartphone className="h-4 w-4"/>;
            case 'tablet':
                return <Tablet className="h-4 w-4"/>;
            default:
                return <Globe className="h-4 w-4"/>;
        }
    };

    const formatLastActivity = (timestamp: number): string => {
        if (!timestamp) return 'Never';

        const now = Date.now();
        const diff = now - timestamp;
        const minutes = Math.floor(diff / (1000 * 60));
        const hours = Math.floor(diff / (1000 * 60 * 60));
        const days = Math.floor(diff / (1000 * 60 * 60 * 24));

        if (minutes < 1) return 'Just now';
        if (minutes < 60) return `${minutes}m ago`;
        if (hours < 24) return `${hours}h ago`;
        if (days < 7) return `${days}d ago`;

        return new Date(timestamp).toLocaleDateString();
    };

    return (
        <div className="space-y-6">
            <div>
                <h3 className="text-2xl font-bold mb-2">Security Settings</h3>
                <p className="text-muted-foreground">
                    Manage your account security and active sessions
                </p>
            </div>

            <div className="bg-card border border-border rounded-lg p-6">
                <h4 className="text-lg font-semibold mb-4 flex items-center gap-2">
                    <Lock className="h-5 w-5"/>
                    Change Password
                </h4>
                <div className="space-y-4">
                    <p className="text-sm text-muted-foreground">
                        Update your account password to keep your account secure. Your new password must be at least 8
                        characters long.
                    </p>
                    <Button
                        onClick={handleChangePassword}
                        className={`w-full ${
                            !permissionService.hasPermission('polocloud.user.self.change-password')
                                ? 'opacity-50 cursor-not-allowed'
                                : ''
                        }`}
                        disabled={!permissionService.hasPermission('polocloud.user.self.change-password')}
                        title={
                            !permissionService.hasPermission('polocloud.user.self.change-password')
                                ? 'No permission to change password'
                                : 'Change Password'
                        }
                    >
                        <Lock className="h-4 w-4 mr-2"/>
                        Change Password
                        {!permissionService.hasPermission('polocloud.user.self.change-password') && (
                            <span className="ml-2 text-xs opacity-75">(No Permission)</span>
                        )}
                    </Button>
                </div>
            </div>

            <div className="bg-card border border-border rounded-lg p-6">
                <div className="flex items-center justify-between mb-4">
                    <h4 className="text-lg font-semibold">Active Sessions</h4>
                    <Button
                        variant="outline"
                        size="sm"
                        onClick={fetchTokens}
                        disabled={isLoading || !permissionService.hasPermission('polocloud.user.self.tokens')}
                        className={`${
                            !permissionService.hasPermission('polocloud.user.self.tokens')
                                ? 'opacity-50 cursor-not-allowed'
                                : ''
                        }`}
                        title={
                            !permissionService.hasPermission('polocloud.user.self.tokens')
                                ? 'No permission to view sessions'
                                : 'Refresh sessions'
                        }
                    >
                        {isLoading ? 'Refreshing...' : 'Refresh'}
                    </Button>
                </div>

                {isLoading ? (
                    <div className="flex items-center justify-center py-8">
                        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
                    </div>
                ) : !permissionService.hasPermission('polocloud.user.self.tokens') ? (
                    <div className="text-center py-8 text-muted-foreground">
                        <p>No permission to view sessions</p>
                        <p className="text-xs mt-2">Required permission: polocloud.user.self.tokens</p>
                    </div>
                ) : tokens.length === 0 ? (
                    <div className="text-center py-8 text-muted-foreground">
                        <p>No active sessions found</p>
                    </div>
                ) : (
                    <div
                        className={`space-y-3 ${tokens.length > 2 ? 'max-h-96 overflow-y-auto pr-2 scrollbar-thin scrollbar-track-muted/30 scrollbar-thumb-muted-foreground/40 hover:scrollbar-thumb-muted-foreground/60' : ''}`}>
                        {tokens.map((token) => (
                            <div key={token.ip}
                                 className="bg-card border border-border rounded-lg p-4 hover:bg-muted/20 transition-colors">
                                <div className="flex items-start justify-between">
                                    <div className="flex items-center space-x-3">
                                        <div className="flex items-center space-x-2">
                                            <div className="p-2 bg-primary/10 rounded-lg">
                                                {getDeviceIcon(token.device)}
                                            </div>
                                            <div className="flex flex-col">
                                                <span className="text-sm font-semibold">{token.device}</span>
                                                <span className="text-xs text-muted-foreground">{token.browser}</span>
                                            </div>
                                        </div>

                                        {token.isCurrent && (
                                            <span
                                                className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800 border border-green-200">
                        Current Session
                      </span>
                                        )}
                                    </div>

                                    {!token.isCurrent && (
                                        <Button
                                            variant="ghost"
                                            size="sm"
                                            className={`p-2 h-8 w-8 ${
                                                !permissionService.hasPermission('polocloud.user.self.token.delete')
                                                    ? 'opacity-50 cursor-not-allowed text-gray-400'
                                                    : 'text-red-500 hover:text-red-600 hover:bg-red-50/50'
                                            }`}
                                            onClick={() => handleRevokeSession()}
                                            disabled={!permissionService.hasPermission('polocloud.user.self.token.delete')}
                                            title={
                                                !permissionService.hasPermission('polocloud.user.self.token.delete')
                                                    ? 'No permission to revoke sessions'
                                                    : 'Revoke this session'
                                            }
                                        >
                                            <Trash2 className="h-4 w-4"/>
                                        </Button>
                                    )}
                                </div>

                                <div className="mt-3 pt-3 border-t border-border/50">
                                    <div className="grid grid-cols-2 gap-4 text-sm">
                                        <div>
                                            <span className="text-muted-foreground">Location:</span>
                                            <div className="font-medium">{token.location}</div>
                                        </div>
                                        <div>
                                            <span className="text-muted-foreground">Last Online:</span>
                                            <div className="font-medium">{token.formattedLastActivity}</div>
                                        </div>
                                        <div className="col-span-2">
                                            <span className="text-muted-foreground">IP Address:</span>
                                            <div
                                                className="font-mono text-xs cursor-pointer hover:text-foreground transition-colors mt-1"
                                                onClick={() => toggleIPVisibility(token.ip)}
                                                title="Click to reveal/hide IP"
                                            >
                        <span
                            className={`transition-all duration-200 ${
                                revealedIPs.has(token.ip)
                                    ? 'blur-none'
                                    : 'blur-sm'
                            }`}
                        >
                          {token.ip}
                        </span>
                                            </div>
                                        </div>
                                        <div className="col-span-2">
                                            <span className="text-muted-foreground">User UUID:</span>
                                            <div className="font-mono text-xs mt-1">{token.userUUID}</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            <Dialog open={isPasswordModalOpen} onOpenChange={handlePasswordModalClose}>
                <DialogContent className="sm:max-w-md">
                    <DialogHeader>
                        <DialogTitle className="flex items-center gap-2">
                            <Lock className="h-5 w-5"/>
                            Change Password
                        </DialogTitle>
                        <DialogDescription>
                            Update your account password to keep your account secure. You will be automatically logged
                            out after changing your password.
                        </DialogDescription>
                    </DialogHeader>
                    <div className="space-y-4 py-4">
                        <div className="space-y-2">
                            <Label htmlFor="new-password" className="text-sm font-medium">
                                New Password <span className="text-red-500">*</span>
                            </Label>
                            <div className="relative">
                                <Input
                                    id="new-password"
                                    type={showNewPassword ? "text" : "password"}
                                    value={newPassword}
                                    onChange={(e) => setNewPassword(e.target.value)}
                                    placeholder="Enter new password"
                                    required
                                    className="pr-10"
                                />
                                <Button
                                    type="button"
                                    variant="ghost"
                                    size="sm"
                                    className="absolute right-0 top-0 h-full px-3 hover:bg-transparent"
                                    onClick={() => setShowNewPassword(!showNewPassword)}
                                    title={showNewPassword ? "Hide password" : "Show password"}
                                >
                                    {showNewPassword ? <EyeOff className="h-4 w-4"/> : <Eye className="h-4 w-4"/>}
                                </Button>
                            </div>
                            <p className="text-xs text-muted-foreground">
                                Password must be at least 8 characters long
                            </p>
                        </div>

                        <div className="space-y-2">
                            <Label htmlFor="confirm-password" className="text-sm font-medium">
                                Confirm New Password <span className="text-red-500">*</span>
                            </Label>
                            <div className="relative">
                                <Input
                                    id="confirm-password"
                                    type={showConfirmPassword ? "text" : "password"}
                                    value={confirmPassword}
                                    onChange={(e) => setConfirmPassword(e.target.value)}
                                    placeholder="Confirm new password"
                                    required
                                    className="pr-10"
                                />
                                <Button
                                    type="button"
                                    variant="ghost"
                                    size="sm"
                                    className="absolute right-0 top-0 h-full px-3 hover:bg-transparent"
                                    onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                                    title={showConfirmPassword ? "Hide password" : "Show password"}
                                >
                                    {showConfirmPassword ? <EyeOff className="h-4 w-4"/> : <Eye className="h-4 w-4"/>}
                                </Button>
                            </div>
                            {confirmPassword && newPassword !== confirmPassword && (
                                <p className="text-xs text-red-500">
                                    Passwords do not match
                                </p>
                            )}
                        </div>
                    </div>
                    <div className="flex gap-3">
                        <Button
                            variant="outline"
                            onClick={handlePasswordModalClose}
                            className="flex-1"
                            disabled={isChangingPassword}
                        >
                            Cancel
                        </Button>
                        <Button
                            onClick={handlePasswordChangeSubmit}
                            className="flex-1"
                            disabled={isChangingPassword || !newPassword.trim() || !confirmPassword.trim() || newPassword !== confirmPassword || newPassword.length < 8}
                        >
                            {isChangingPassword ? (
                                <>
                                    <div
                                        className="w-4 h-4 border-2 border-current border-t-transparent rounded-full animate-spin mr-2"/>
                                    Changing...
                                </>
                            ) : (
                                'Change Password'
                            )}
                        </Button>
                    </div>
                </DialogContent>
            </Dialog>
        </div>
    );
}

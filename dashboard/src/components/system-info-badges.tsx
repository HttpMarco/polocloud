'use client'

import { useState, useEffect } from 'react';
import { Badge } from '@/components/ui/badge';
import { Clock, Activity } from 'lucide-react';
import { formatUptime } from '@/lib/utils';

interface SystemInfo {
    runtime: string;
    uptime: number;
}

export default function SystemInfoBadges() {
    const [systemInfo, setSystemInfo] = useState<SystemInfo | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [token, setToken] = useState<string | null>(null);

    useEffect(() => {
        const getToken = async () => {
            try {
                const response = await fetch('/api/auth/status');
                if (response.ok) {
                    const authData = await response.json();
                    setToken(authData.token);
                } else {
                }
            } catch {
            }
        };

        getToken();
    }, []);

    useEffect(() => {
        const loadSystemInfo = async () => {
            if (!token) {
                setIsLoading(false);
                return;
            }

            try {
                const response = await fetch('/api/system/information', {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok) {
                    const data = await response.json();

                    setSystemInfo({
                        runtime: data.runtime || 'Unknown',
                        uptime: data.uptime || 0
                    });
                } else {
                    setSystemInfo({
                        runtime: 'Unknown',
                        uptime: 0
                    });
                }
            } catch {
                setSystemInfo({
                    runtime: 'Unknown',
                    uptime: 0
                });
            } finally {
                setIsLoading(false);
            }
        };

        loadSystemInfo();

        const interval = setInterval(loadSystemInfo, 5000);

        return () => clearInterval(interval);
    }, [token]);

    if (isLoading || !systemInfo) {
        return (
            <div className="flex space-x-2">
                <Badge variant="outline" className="px-2 py-1">
                    <Clock className="w-3 h-3 mr-1" />
                    Loading...
                </Badge>
                <Badge variant="outline" className="px-2 py-1">
                    <Activity className="w-3 h-3 mr-1" />
                    Loading...
                </Badge>
            </div>
        );
    }

    return (
        <div className="flex space-x-2">
            <Badge variant="secondary" className="px-2 py-1 text-xs bg-purple-500/10 text-purple-500 border-purple-500/20 hover:bg-purple-500/20">
                <Clock className="w-3 h-3 mr-1" />
                Runtime: {systemInfo.runtime}
            </Badge>
            <Badge variant="secondary" className="px-2 py-1 text-xs bg-blue-500/10 text-blue-500 border-blue-500/20 hover:bg-blue-500/20">
                <Activity className="w-3 h-3 mr-1" />
                Uptime: {formatUptime(systemInfo.uptime)}
            </Badge>
        </div>
    );
}

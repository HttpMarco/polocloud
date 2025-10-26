'use client';

import { DashboardStats } from '@/components/dashboard/dashboard-stats';
import { DashboardCharts } from '@/components/dashboard/dashboard-charts';
import { ForcePasswordChangeModal } from '@/components/force-password-change-modal';
import { motion } from 'framer-motion';
import { useState, useEffect, useMemo, useCallback } from 'react';

import { API_ENDPOINTS } from '@/lib/api';
import GlobalNavbar from '@/components/global-navbar';

export default function DashboardPage() {

    const [groupCount, setGroupCount] = useState<number>(0);
    const [isLoadingGroups, setIsLoadingGroups] = useState(true);
    const [groupTrend, setGroupTrend] = useState<string>('+0%');
    const [groupTrendDirection, setGroupTrendDirection] = useState<'up' | 'down' | 'stable'>('stable');
    const [serviceCount, setServiceCount] = useState<number>(0);
    const [isLoadingServices, setIsLoadingServices] = useState(true);
    const [serviceTrend, setServiceTrend] = useState<string>('0%');
    const [serviceTrendDirection, setServiceTrendDirection] = useState<'up' | 'down' | 'stable'>('stable');

    const [realMemoryUsage, setRealMemoryUsage] = useState<number | null>(null);
    const [realCpuUsage, setRealCpuUsage] = useState<number | null>(null);
    const [avgMemory, setAvgMemory] = useState<number | null>(null);
    const [avgCpu, setAvgCpu] = useState<number | null>(null);
    const [timeRange, setTimeRange] = useState<'10m' | '1d' | '3d' | '7d'>('7d');
    const [chartData, setChartData] = useState<Array<{
        timestamp: string,
        memory: number,
        cpu: number,
        time: string
    }>>([]);

    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [showPasswordChangeModal, setShowPasswordChangeModal] = useState(false);

    const handleSystemError = () => {
        setRealMemoryUsage(null);
        setRealCpuUsage(null);
    };

    const handleTrendError = () => {
        setGroupTrend('+0%');
        setGroupTrendDirection('stable');
    };

    const handleServiceTrendError = () => {
        setServiceTrend('0%');
        setServiceTrendDirection('stable');
    };

    const setTrendData = (percentage: number, setTrend: (value: string) => void, setDirection: (direction: 'up' | 'down' | 'stable') => void) => {
        if (percentage > 0) {
            setTrend(`+${Math.round(percentage)}%`);
            setDirection('up');
        } else if (percentage < 0) {
            setTrend(`${Math.round(percentage)}%`);
            setDirection('down');
        } else {
            setTrend('+0%');
            setDirection('stable');
        }
    };

    const getTimeRangeInSeconds = useCallback(() => {
        switch (timeRange) {
            case '10m':
                return 600;
            case '1d':
                return 86400;
            case '3d':
                return 259200;
            case '7d':
                return 604800;
            default:
                return 604800;
        }
    }, [timeRange]);

    const generateMockChartData = useCallback((selectedTimeRange: string) => {
        const now = Date.now();
        let points: number;
        let interval: number;

        switch (selectedTimeRange) {
            case '10m':
                points = 10;
                interval = 60 * 1000;
                break;
            case '1d':
                points = 12;
                interval = 2 * 60 * 60 * 1000;
                break;
            case '3d':
                points = 12;
                interval = 6 * 60 * 60 * 1000;
                break;
            case '7d':
                points = 14;
                interval = 12 * 60 * 60 * 1000;
                break;
            default:
                points = 24;
                interval = 60 * 60 * 1000;
        }

        const data = [];
        for (let i = points - 1; i >= 0; i--) {
            const timestamp = now - (i * interval);
            const baseMemory = 2048;
            const baseCpu = 25;

            const memoryVariation = 0.7 + (Math.random() * 0.6);
            const cpuVariation = 0.6 + (Math.random() * 0.8);

            const memory = Math.max(1000, Math.min(4000, baseMemory * memoryVariation));
            const cpu = Math.max(10, Math.min(80, baseCpu * cpuVariation));

            const date = new Date(timestamp);
            let timeString: string;

            switch (selectedTimeRange) {
                case '10m':
                    const hours = date.getHours().toString().padStart(2, '0');
                    const minutes = date.getMinutes().toString().padStart(2, '0');
                    timeString = `${hours}:${minutes}`;
                    break;
                case '1d':
                    const hour = date.getHours();
                    timeString = `${hour.toString().padStart(2, '0')}:00`;
                    break;
                case '3d':
                    const month3d = date.toLocaleDateString('en-US', {month: 'short'});
                    const day3d = date.getDate();
                    const hour3d = date.getHours();
                    timeString = `${month3d} ${day3d}, ${hour3d}:00`;
                    break;
                case '7d':
                    const month7d = date.toLocaleDateString('en-US', {month: 'short'});
                    const day7d = date.getDate();
                    const hour7d = date.getHours();
                    timeString = `${month7d} ${day7d}, ${hour7d}:00`;
                    break;
                default:
                    const defaultHours = date.getHours().toString().padStart(2, '0');
                    const defaultMinutes = date.getMinutes().toString().padStart(2, '0');
                    timeString = `${defaultHours}:${defaultMinutes}`;
            }

            data.push({
                timestamp: timestamp.toString(),
                memory: Math.round(memory),
                cpu: Math.round(cpu),
                time: timeString
            });
        }

        setChartData(data);
    }, []);

    useEffect(() => {

        const token = localStorage.getItem('token') || document.cookie.includes('token=');
        const isLoggedInStatus = localStorage.getItem('isLoggedIn') === 'true';
        const needsPasswordChange = localStorage.getItem('needsPasswordChange') === 'true';

        setIsLoggedIn(!!token && isLoggedInStatus);

        if (needsPasswordChange) {
            setShowPasswordChangeModal(true);
        }
    }, []);

    const handlePasswordChanged = () => {
        localStorage.removeItem('needsPasswordChange');
        setShowPasswordChangeModal(false);
    };

    useEffect(() => {
        const fetchGroupCount = async () => {
            try {
                setIsLoadingGroups(true);

                const listResponse = await fetch(API_ENDPOINTS.GROUPS.LIST);
                if (listResponse.ok) {
                    const groups = await listResponse.json();
                    const totalCount = Array.isArray(groups) ? groups.length : 0;
                    setGroupCount(totalCount);
                }

                const now = Date.now();
                const sevenDaysAgo = now - (7 * 24 * 60 * 60 * 1000);
                
                const trendResponse = await fetch(`${API_ENDPOINTS.GROUPS.COUNT_WITH_RANGE(sevenDaysAgo, now)}`);
                if (trendResponse.ok) {
                    const trendData = await trendResponse.json();
                    const percentage = trendData.data?.percentage || 0;
                    setTrendData(percentage, setGroupTrend, setGroupTrendDirection);
                } else {
                    handleTrendError();
                }
            } catch {
                handleTrendError();
            } finally {
                setIsLoadingGroups(false);
            }
        };

        const fetchServiceCount = async () => {
            try {
                setIsLoadingServices(true);

                const countResponse = await fetch(API_ENDPOINTS.SERVICES.COUNT);
                if (countResponse.ok) {
                    const countData = await countResponse.json();
                    const runningCount = countData.serviceCount || 0;
                    const onlinePercentage = countData.percentage || 0;
                    
                    setServiceCount(runningCount);

                    if (onlinePercentage === 100) {
                        setServiceTrend('100%');
                        setServiceTrendDirection('up');
                    } else if (onlinePercentage > 0) {
                        setServiceTrend(`${onlinePercentage}%`);
                        setServiceTrendDirection('up');
                    } else {
                        setServiceTrend('0%');
                        setServiceTrendDirection('down');
                    }
                } else {
                    handleServiceTrendError();
                }
            } catch  {
                handleServiceTrendError();
            } finally {
                setIsLoadingServices(false);
            }
        };

        fetchGroupCount();
        fetchServiceCount();
    }, []);

    const loadSystemInfo = useCallback(async () => {
        try {
            const response = await fetch(API_ENDPOINTS.SYSTEM.INFORMATION);
            if (response.ok) {
                const data = await response.json();

                let totalMemoryUsage = 0;
                let totalCpuUsage = 0;
                
                if (data.memoryUsage && typeof data.memoryUsage === 'object') {
                    totalMemoryUsage = data.memoryUsage.total || 0;
                } else if (typeof data.memoryUsage === 'number') {
                    totalMemoryUsage = data.memoryUsage;
                }
                
                if (data.cpuUsage && typeof data.cpuUsage === 'object') {
                    totalCpuUsage = data.cpuUsage.total || 0;
                } else if (typeof data.cpuUsage === 'number') {
                    totalCpuUsage = data.cpuUsage;
                }

                if (totalMemoryUsage > 0 || totalCpuUsage > 0) {
                    setRealMemoryUsage(totalMemoryUsage);
                    setRealCpuUsage(totalCpuUsage);
                } else {
                    handleSystemError();
                }
            } else {
                handleSystemError();
            }
        } catch {
            handleSystemError();
        }
    }, []);

    const loadSystemAverage = useCallback(async () => {
        try {
            const now = Date.now();
            const from = now - getTimeRangeInSeconds() * 1000;
            
            const response = await fetch(API_ENDPOINTS.SYSTEM.AVERAGE_WITH_RANGE(from, now));
            if (response.ok) {
                const data = await response.json();

                const totalAvgRam = data.avgRam?.total || 0;
                const totalAvgCpu = data.avgCpu?.total || 0;

                if (totalAvgRam !== undefined && totalAvgCpu !== undefined) {
                    setAvgMemory(totalAvgRam);
                    setAvgCpu(totalAvgCpu);
                } else {
                    setAvgMemory(null);
                    setAvgCpu(null);
                }
            } else {
                setAvgMemory(null);
                setAvgCpu(null);
            }
        } catch {
            setAvgMemory(null);
            setAvgCpu(null);
        }
    }, [getTimeRangeInSeconds]);

    const loadChartData = useCallback(async () => {
        try {
            const now = Math.floor(Date.now() / 1000);
            const from = now - getTimeRangeInSeconds();
            
            let endpoint: string;
            switch (timeRange) {
                case '10m':
                    endpoint = 'minutes';
                    break;
                case '1d':
                    endpoint = 'hours';
                    break;
                case '3d':
                case '7d':
                    endpoint = 'days';
                    break;
                default:
                    endpoint = 'days';
            }
            
            const response = await fetch(API_ENDPOINTS.SYSTEM.DATA_WITH_RANGE(endpoint, from, now));
            
            if (response.ok) {
                const data = await response.json();
                
                if (!data || data.length === 0) {
                    generateMockChartData(timeRange);
                    return;
                }
                

                const transformedData = data.map((item: { timestamp: number; avgRam: number; avgCpu: number }) => {

                    let timeString: string;
                    const date = new Date(item.timestamp);
                    
                    switch (timeRange) {
                        case '10m':
                            timeString = date.toLocaleTimeString('de-DE', {hour: '2-digit', minute: '2-digit'});
                            break;
                        case '1d':
                            timeString = date.toLocaleTimeString('de-DE', {hour: '2-digit'});
                            break;
                        case '3d':
                        case '7d':
                            timeString = date.toLocaleDateString('de-DE', {month: 'short', day: 'numeric'});
                            break;
                        default:
                            timeString = date.toLocaleTimeString('de-DE', {hour: '2-digit', minute: '2-digit'});
                    }
                    
                    return {
                        timestamp: item.timestamp.toString(),
                        memory: item.avgRam,
                        cpu: item.avgCpu,
                        time: timeString
                    };
                });
                
                setChartData(transformedData);
            } else {
                generateMockChartData(timeRange);
            }
        } catch {
            generateMockChartData(timeRange);
        }
    }, [timeRange, generateMockChartData, getTimeRangeInSeconds]);

    const memoizedChartData = useMemo(() => chartData, [chartData]);

    useEffect(() => {
        loadChartData();
        loadSystemAverage();
    }, [loadChartData, loadSystemAverage]);

    const [isClient, setIsClient] = useState(false);
    
    useEffect(() => {
        setIsClient(true);
    }, []);

    useEffect(() => {
        loadChartData();
        loadSystemAverage();
    }, [timeRange, loadChartData, loadSystemAverage]);

    useEffect(() => {
        if (chartData.length > 0) {

        }
    }, [chartData]);

    useEffect(() => {
        const interval = setInterval(() => {
            loadSystemInfo();
        }, 5000);

        return () => {
            clearInterval(interval);
        };
    }, [loadSystemInfo]);







    const containerVariants = {
        hidden: { opacity: 0 },
        visible: {
            opacity: 1,
            transition: {
                duration: 1.5,
                staggerChildren: 0.25
            }
        }
    };

    const itemVariants = {
        hidden: {
            opacity: 0,
            y: 20,
            scale: 0.98
        },
        visible: {
            opacity: 1,
            y: 0,
            scale: 1,
            transition: {
                duration: 1.0,
                type: "spring" as const,
                stiffness: 100,
                damping: 20
            }
        }
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20 ultra-smooth-scroll">
            {}
            <GlobalNavbar />

            <div className="flex flex-col max-w-none w-full mx-auto p-8">
                {isLoggedIn && (
                    <motion.div
                        className="text-center mb-16 dashboard-header"
                        initial={{ opacity: 0, y: -30 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.8, ease: "easeOut" }}
                    >
                        <motion.h1
                            className="text-5xl font-bold bg-gradient-to-r from-foreground via-foreground to-muted-foreground bg-clip-text text-transparent mb-4"
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.8, delay: 0.2 }}
                        >
                            PoloCloud Dashboard
                        </motion.h1>
                        <motion.p
                            className="text-xl text-muted-foreground max-w-3xl mx-auto leading-relaxed"
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.8, delay: 0.4 }}
                        >
                            Monitor your cloud infrastructure and manage resources efficiently
                        </motion.p>
                    </motion.div>
                )}
                        
                {!isLoggedIn && (
                        <motion.div
                        className="text-center mb-16 dashboard-header"
                        initial={{ opacity: 0, y: -30 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.8, ease: "easeOut" }}
                    >
                        <motion.h1
                            className="text-5xl font-bold bg-gradient-to-r from-foreground via-foreground to-muted-foreground bg-clip-text text-transparent mb-4"
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.8, delay: 0.2 }}
                        >
                            PoloCloud Dashboard
                        </motion.h1>
                        <motion.p
                            className="text-xl text-muted-foreground max-w-3xl mx-auto leading-relaxed"
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.8, delay: 0.4 }}
                        >
                            Monitor your cloud infrastructure and manage resources efficiently
                        </motion.p>
                    </motion.div>
                )}

                <motion.div
                    className="flex flex-1 flex-col gap-8"
                    variants={containerVariants}
                    initial="hidden"
                    animate="visible"
                >
                    {}
                    <motion.div
                        className="flex justify-center"
                        variants={itemVariants}
                    >
                        <DashboardStats
                            stats={{
                                groups: {
                                    count: groupCount,
                                    trend: groupTrend,
                                    trendDirection: groupTrendDirection
                                },
                                services: {
                                    count: serviceCount,
                                    trend: serviceTrend,
                                    trendDirection: serviceTrendDirection
                                }
                            }}
                            isLoadingGroups={isLoadingGroups}
                            isLoadingServices={isLoadingServices}
                        />
                    </motion.div>

                    {}
                    <motion.div
                        className="w-full"
                        variants={itemVariants}
                    >
                        <DashboardCharts
                            realMemoryUsage={realMemoryUsage || 0}
                            realCpuUsage={realCpuUsage || 0}
                            avgMemory={avgMemory}
                            avgCpu={avgCpu}
                            timeRange={timeRange}
                            setTimeRange={setTimeRange}
                            memoizedChartData={memoizedChartData}
                            isClient={isClient}
                            memorySpring={{ value: realMemoryUsage || 0 }}
                            cpuSpring={{ value: realCpuUsage || 0 }}
                            avgMemorySpring={{ value: avgMemory || 0 }}
                            avgCpuSpring={{ value: avgCpu || 0 }}
                        />
                    </motion.div>
                </motion.div>
            </div>

            <ForcePasswordChangeModal
                isOpen={showPasswordChangeModal}
                onPasswordChanged={handlePasswordChanged}
            />
        </div>
    );
  }

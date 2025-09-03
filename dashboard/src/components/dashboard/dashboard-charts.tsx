'use client';

import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { motion } from 'framer-motion';
import { DashboardStatsBoxes } from './dashboard-stats-boxes';

import {
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    ResponsiveContainer,
    Area,
    ComposedChart,
    Legend
} from 'recharts';

interface DashboardChartsProps {
    realMemoryUsage: number;
    realCpuUsage: number;
    avgMemory: number | null;
    avgCpu: number | null;
    timeRange: '10m' | '1d' | '3d' | '7d';
    setTimeRange: (range: '10m' | '1d' | '3d' | '7d') => void;
    memoizedChartData: Array<{ timestamp: string; memory: number; cpu: number }>;
    isClient: boolean;
    memorySpring: { value: number };
    cpuSpring: { value: number };
    avgMemorySpring: { value: number };
    avgCpuSpring: { value: number };
}

export function DashboardCharts({
    realMemoryUsage,
    realCpuUsage,
    avgMemory,
    avgCpu,
    timeRange,
    setTimeRange,
    memoizedChartData,
    isClient,
    memorySpring,
    cpuSpring,
    avgMemorySpring,
    avgCpuSpring
}: DashboardChartsProps) {
    return (
        <motion.div
            className="w-full"
            initial={{ opacity: 0, y: 25 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 1.2, type: "spring", stiffness: 80, damping: 25 }}
        >
            <Card className="relative overflow-hidden bg-gradient-to-br from-card/80 via-card/60 to-card/80 border border-border/40 shadow-lg shadow-[0_0_40px_rgba(75.54%,15.34%,231.639,0.4)] min-h-[160px] w-full rounded-2xl">
                <div className="absolute inset-0 bg-[radial-gradient(circle_at_30%_20%,rgba(75.54%,15.34%,0.05)_0%,transparent_50%)] opacity-60 rounded-2xl"/>
                <div className="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r from-[oklch(75.54%,15.34%,231.639)] via-[oklch(75.54%,15.34%,231.639,0.8)] to-[oklch(75.54%,15.34%,231.639)] rounded-t-2xl"/>
                <div className="absolute inset-0 rounded-2xl shadow-[0_0_60px_rgba(75.54%,15.34%,231.639,0.3)] pointer-events-none"/>

                <CardHeader className="relative z-10">
                    <CardTitle className="flex items-center gap-2 text-lg font-medium text-foreground/90">
                        <div className="w-3 h-3 bg-[oklch(75.54%,15.34%,231.639)] rounded-full animate-pulse"></div>
                        System Information
                    </CardTitle>
                </CardHeader>

                <CardContent className="relative z-10">
                    {}
                    <DashboardStatsBoxes
                        realMemoryUsage={realMemoryUsage}
                        realCpuUsage={realCpuUsage}
                        avgMemory={avgMemory}
                        avgCpu={avgCpu}
                        memorySpring={memorySpring}
                        cpuSpring={cpuSpring}
                        avgMemorySpring={avgMemorySpring}
                        avgCpuSpring={avgCpuSpring}
                    />

                    {}
                    <div className="flex items-center justify-between p-4 rounded-xl bg-gradient-to-br from-card/50 to-card/30 border border-border/50 backdrop-blur-sm mb-4">
                        <div className="flex items-center gap-4">
                            <div className="flex items-center gap-2">
                                <div className="w-4 h-4 rounded-full bg-blue-500 shadow-lg"></div>
                                <span className="text-sm font-medium text-foreground">Memory Usage (MB)</span>
                            </div>
                            <div className="flex items-center gap-2">
                                <div className="w-4 h-4 rounded-full bg-purple-500 shadow-lg"></div>
                                <span className="text-sm font-medium text-foreground">CPU Usage (%)</span>
                            </div>
                        </div>
                        <div className="flex items-center gap-2">
                            <select
                                value={timeRange}
                                onChange={(e) => setTimeRange(e.target.value as '10m' | '1d' | '3d' | '7d')}
                                className="px-3 py-1.5 text-sm rounded-lg bg-background/50 border border-border/50 text-foreground hover:border-primary/50 transition-colors focus:outline-none focus:ring-2 focus:ring-primary/20"
                            >
                                <option value="10m">Last 10 minutes</option>
                                <option value="1d">Last 24 hours</option>
                                <option value="3d">Last 3 days</option>
                                <option value="7d">Last 7 days</option>
                            </select>
                        </div>
                    </div>

                    {}
                    <div className="w-full h-[280px]">
                        {memoizedChartData.length > 0 ? (
                            <ResponsiveContainer width="100%" height="100%">
                                <ComposedChart data={memoizedChartData} margin={{top: 20, right: 30, left: 20, bottom: 20}}>
                                    <defs>
                                        <linearGradient id="memoryGradient" x1="0" y1="0" x2="0" y2="1">
                                            <stop offset="5%" stopColor="rgb(59, 130, 246)" stopOpacity={0.3}/>
                                            <stop offset="95%" stopColor="rgb(59, 130, 246)" stopOpacity={0.05}/>
                                        </linearGradient>
                                        <linearGradient id="cpuGradient" x1="0" y1="0" x2="0" y2="1">
                                            <stop offset="5%" stopColor="rgb(147, 51, 234)" stopOpacity={0.3}/>
                                            <stop offset="95%" stopColor="rgb(147, 51, 234)" stopOpacity={0.05}/>
                                        </linearGradient>
                                    </defs>

                                    <CartesianGrid
                                        strokeDasharray="3 3"
                                        stroke="rgba(255,255,255,0.1)"
                                        strokeWidth={0.5}
                                    />

                                    <XAxis
                                        dataKey="time"
                                        axisLine={false}
                                        tickLine={false}
                                        tick={{fontSize: 12, fill: 'rgba(255,255,255,0.7)'}}
                                        tickMargin={10}
                                        interval="preserveStartEnd"
                                    />

                                    <YAxis
                                        yAxisId="left"
                                        orientation="left"
                                        axisLine={false}
                                        tickLine={false}
                                        tick={{fontSize: 12, fill: 'rgba(255,255,255,0.7)'}}
                                        tickMargin={10}
                                        tickFormatter={(value) => `${Math.round(value)}`}
                                        label={{value: 'Memory (MB)', angle: -90, position: 'insideLeft', style: {textAnchor: 'middle', fill: 'rgba(255,255,255,0.7)', fontSize: 12}}}
                                    />

                                    <YAxis
                                        yAxisId="right"
                                        orientation="right"
                                        axisLine={false}
                                        tickLine={false}
                                        tick={{fontSize: 12, fill: 'rgba(255,255,255,0.7)'}}
                                        tickMargin={10}
                                        tickFormatter={(value) => `${Math.round(value)}`}
                                        label={{value: 'CPU (%)', angle: 90, position: 'insideRight', style: {textAnchor: 'middle', fill: 'rgba(255,255,255,0.7)', fontSize: 12}}}
                                    />

                                    <Tooltip
                                        content={({active, payload, label}) => {
                                            if (active && payload && payload.length) {
                                                const memoryData = payload.find(p => p.name === 'Memory');
                                                const cpuData = payload.find(p => p.name === 'CPU');
                                                
                                                return (
                                                    <div className="bg-background/95 border border-border/50 rounded-lg p-3 shadow-lg backdrop-blur-sm">
                                                        <p className="text-sm font-medium text-foreground mb-2">{label}</p>
                                                        {memoryData && (
                                                            <p className="text-sm text-blue-400">
                                                                Memory: {Math.round(memoryData.value as number)} MB
                                                            </p>
                                                        )}
                                                        {cpuData && (
                                                            <p className="text-sm text-purple-400">
                                                                CPU: {Math.round(cpuData.value as number)}%
                                                            </p>
                                                        )}
                                                    </div>
                                                );
                                            }
                                            return null;
                                        }}
                                        cursor={{stroke: 'rgba(255,255,255,0.3)', strokeWidth: 1}}
                                    />

                                    <Legend
                                        verticalAlign="top"
                                        height={36}
                                        content={({payload}) => (
                                            <div className="flex justify-center gap-6 mb-4">
                                                {payload?.map((entry, index) => (
                                                    <div key={index} className="flex items-center gap-2">
                                                        <div
                                                            className="w-3 h-3 rounded-full"
                                                            style={{backgroundColor: entry.color}}
                                                        />
                                                        <span className="text-sm text-foreground/80">
                                                            {entry.value === 'Memory' ? 'Memory (MB)' : 'CPU (%)'}
                                                        </span>
                                                    </div>
                                                ))}
                                            </div>
                                        )}
                                    />

                                    <Area
                                        type="monotone"
                                        dataKey="memory"
                                        yAxisId="left"
                                        stroke="rgb(59, 130, 246)"
                                        strokeWidth={2}
                                        fill="url(#memoryGradient)"
                                        name="Memory"
                                        dot={{fill: 'rgb(59, 130, 246)', strokeWidth: 2, r: 3}}
                                        activeDot={{r: 5, stroke: 'rgb(59, 130, 246)', strokeWidth: 2}}
                                    />

                                    <Line
                                        type="monotone"
                                        dataKey="cpu"
                                        yAxisId="right"
                                        stroke="rgb(147, 51, 234)"
                                        strokeWidth={2}
                                        name="CPU"
                                        dot={{fill: 'rgb(147, 51, 234)', strokeWidth: 2, r: 3}}
                                        activeDot={{r: 5, stroke: 'rgb(147, 51, 234)', strokeWidth: 2}}
                                    />
                                </ComposedChart>
                            </ResponsiveContainer>
                        ) : (
                            <div className="flex flex-col items-center justify-center h-full text-center">
                                <div className="text-lg font-medium text-foreground/70 mb-2">Chart wird geladen...</div>
                                <div className="text-sm text-muted-foreground">Chart Data: {memoizedChartData.length} Punkte</div>
                                <div className="text-xs text-muted-foreground mt-2">Time Range: {timeRange}</div>
                                {isClient && (
                                    <div className="text-xs text-muted-foreground">Last Update: {new Date().toLocaleTimeString('de-DE')}</div>
                                )}
                            </div>
                        )}
                    </div>
                </CardContent>
            </Card>
        </motion.div>
    );
}

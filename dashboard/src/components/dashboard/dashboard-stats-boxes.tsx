'use client';



interface DashboardStatsBoxesProps {
    realMemoryUsage: number;
    realCpuUsage: number;
    avgMemory: number | null;
    avgCpu: number | null;
    memorySpring: { value: number };
    cpuSpring: { value: number };
    avgMemorySpring: { value: number };
    avgCpuSpring: { value: number };
}

export function DashboardStatsBoxes({
    realMemoryUsage,
    realCpuUsage,
    avgMemory,
    avgCpu,
    memorySpring,
    cpuSpring,
    avgMemorySpring,
    avgCpuSpring
}: DashboardStatsBoxesProps) {
    return (
        <div className="grid grid-cols-4 gap-4 mb-4">
            {}
            <div className="relative overflow-hidden bg-gradient-to-br from-card/80 via-card/60 to-card/80 border border-border/40 shadow-lg rounded-2xl h-16">
                <div className="absolute inset-0 bg-[radial-gradient(circle_at_30%_20%,rgba(75.54%,15.34%,0.03)_0%,transparent_50%)] opacity-60 rounded-2xl"/>
                <div className="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r from-[oklch(75.54%,15.34%,231.639)] to-[oklch(75.54%,15.34%,231.639)] rounded-t-2xl"/>
                <div className="relative z-10 text-center p-2 h-full flex flex-col justify-center">
                    <div className="flex items-center justify-center gap-1 mb-1">
                        <div className="w-3 h-3 text-blue-500"></div>
                        <span className="text-xs font-medium text-muted-foreground">Current Memory</span>
                    </div>
                    <div className="text-lg font-bold text-foreground">
                        {realMemoryUsage !== null && realMemoryUsage !== undefined ? (
                            <span>
                                {Math.round(memorySpring.value)} MB
                            </span>
                        ) : (
                            <span>--</span>
                        )}
                    </div>
                </div>
            </div>

            {}
            <div className="relative overflow-hidden bg-gradient-to-br from-card/80 via-card/60 to-card/80 border border-border/40 shadow-lg rounded-2xl h-16">
                <div className="absolute inset-0 bg-[radial-gradient(circle_at_30%_20%,rgba(75.54%,15.34%,0.03)_0%,transparent_50%)] opacity-60 rounded-2xl"/>
                <div className="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r from-[oklch(75.54%,15.34%,231.639)] to-[oklch(75.54%,15.34%,231.639)] rounded-t-2xl"/>
                <div className="relative z-10 text-center p-2 h-full flex flex-col justify-center">
                    <div className="flex items-center justify-center gap-1 mb-1">
                        <div className="w-3 h-3 text-purple-500"></div>
                        <span className="text-xs font-medium text-muted-foreground">Current CPU</span>
                    </div>
                    <div className="text-lg font-bold text-foreground">
                        {realCpuUsage !== null && realCpuUsage !== undefined ? (
                            <span>
                                {Math.round(cpuSpring.value * 10) / 10}%
                            </span>
                        ) : (
                            <span>--</span>
                        )}
                    </div>
                </div>
            </div>

            {}
            <div className="relative overflow-hidden bg-gradient-to-br from-card/80 via-card/60 to-card/80 border border-border/40 shadow-lg rounded-2xl h-16">
                <div className="absolute inset-0 bg-[radial-gradient(circle_at_30%_20%,rgba(75.54%,15.34%,0.03)_0%,transparent_50%)] opacity-60 rounded-2xl"/>
                <div className="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r from-[oklch(75.54%,15.34%,231.639)] to-[oklch(75.54%,15.34%,231.639)] rounded-t-2xl"/>
                <div className="relative z-10 text-center p-2 h-full flex flex-col justify-center">
                    <div className="flex items-center justify-center gap-1 mb-1">
                        <div className="w-3 h-3 text-green-500"></div>
                        <span className="text-xs font-medium text-muted-foreground">Avg Memory</span>
                    </div>
                    <div className="text-lg font-bold text-foreground">
                        {avgMemory !== null && avgMemory !== undefined ? (
                            <span>
                                {Math.round(avgMemorySpring.value)} MB
                            </span>
                        ) : (
                            <span>--</span>
                        )}
                    </div>
                </div>
            </div>

            {}
            <div className="relative overflow-hidden bg-gradient-to-br from-card/80 via-card/60 to-card/80 border border-border/40 shadow-lg rounded-2xl h-16">
                <div className="absolute inset-0 bg-[radial-gradient(circle_at_30%_20%,rgba(75.54%,15.34%,0.03)_0%,transparent_50%)] opacity-60 rounded-2xl"/>
                <div className="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r from-[oklch(75.54%,15.34%,231.639)] to-[oklch(75.54%,15.34%,231.639)] rounded-t-2xl"/>
                <div className="relative z-10 text-center p-2 h-full flex flex-col justify-center">
                    <div className="flex items-center justify-center gap-1 mb-1">
                        <div className="w-3 h-3 text-orange-500"></div>
                        <span className="text-xs font-medium text-muted-foreground">Avg CPU</span>
                    </div>
                    <div className="text-lg font-bold text-foreground">
                        {avgCpu !== null && avgCpu !== undefined ? (
                            <span>
                                {Math.round(avgCpuSpring.value * 10) / 10}%
                            </span>
                        ) : (
                            <span>--</span>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}

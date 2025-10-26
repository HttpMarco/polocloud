'use client';

import { useEffect, useState } from 'react';
import { GitHubStatsComponent } from './github-stats';
import { 
    HeroTitle, 
    HeroActions, 
    HeroTerminal, 
    HeroBackground,
    TerminalLine 
} from '@/components/home/hero';

export function HeroSection() {
    const [isVisible, setIsVisible] = useState(false);
    const [showCommand, setShowCommand] = useState(false);
    const [showLogsPhase1, setShowLogsPhase1] = useState(false);
    const [showLogsPhase2, setShowLogsPhase2] = useState(false);
    const [showLogsPhase3, setShowLogsPhase3] = useState(false);
    const [typedText, setTypedText] = useState('');
    const [currentIndex, setCurrentIndex] = useState(0);
    const [latestVersion, setLatestVersion] = useState('v3.0.0-pre.5-SNAPSHOT');

    const commandText = "java -jar polocloud-launcher.jar";

    const terminalLinesPhase1: TerminalLine[] = [
        { time: "16:42:01", level: "INFO", message: `Starting PoloCloud ${latestVersion.replace('v', '')} Agent...`, color: "text-gray-300" },
        { time: "16:42:01", level: "WARN", message: "You are using a snapshot version of polocloud. This version is not recommended for production use!", color: "text-yellow-400" }
    ];

    const terminalLinesPhase2: TerminalLine[] = [
        { time: "16:42:01", level: "INFO", message: "You are running the latest version of the agent.", color: "text-gray-300" },
        { time: "16:42:04", level: "INFO", message: "Successfully started gRPC server on port 8932", color: "text-gray-300" },
        { time: "16:42:04", level: "INFO", message: "Using runtime: LocalRuntime", color: "text-gray-300" },
        { time: "16:42:04", level: "INFO", message: "Load groups (2): lobby, proxy", color: "text-gray-300" },
        { time: "16:42:04", level: "INFO", message: "Load 12 platforms with 207 versions.", color: "text-gray-300" },
        { time: "16:42:04", level: "INFO", message: "The agent is now successfully started and ready to use!", color: "text-gray-300", highlight: "successfully", highlightColor: "text-cyan-400" },
        { time: "16:42:04", level: "INFO", message: "The service lobby-1 is now starting...", color: "text-gray-300", highlight: "lobby-1", highlightColor: "text-cyan-400" },
        { time: "16:42:04", level: "INFO", message: "The service proxy-1 is now starting...", color: "text-gray-300", highlight: "proxy-1", highlightColor: "text-cyan-400" }
    ];

    const terminalLinesPhase3: TerminalLine[] = [
        { time: "16:42:08", level: "INFO", message: "The service proxy-1 is now online.", color: "text-gray-300", highlight: "proxy-1", highlightColor: "text-cyan-400" },
        { time: "16:42:30", level: "INFO", message: "The service lobby-1 is now online.", color: "text-gray-300", highlight: "lobby-1", highlightColor: "text-cyan-400" }
    ];

    useEffect(() => {
        const timer = setTimeout(() => {
            setIsVisible(true);
        }, 100);

        const commandTimer = setTimeout(() => {
            setShowCommand(true);
        }, 1500);

        return () => {
            clearTimeout(timer);
            clearTimeout(commandTimer);
        };
    }, []);

    useEffect(() => {
        const fetchLatestVersion = async () => {
            try {
                const response = await fetch('/api/github-releases');
                if (response.ok) {
                    const data = await response.json();
                    if (data.latestVersion) {
                        setLatestVersion(data.latestVersion);
                    }
                }
            } catch (error) {
                console.log('Failed to fetch latest version, using fallback');
            }
        };

        fetchLatestVersion();
    }, []);

    useEffect(() => {
        if (showCommand && currentIndex < commandText.length) {
            const timeout = setTimeout(() => {
                setTypedText(commandText.slice(0, currentIndex + 1));
                setCurrentIndex(currentIndex + 1);
            }, 100);
            return () => clearTimeout(timeout);
        } else if (showCommand && currentIndex >= commandText.length) {
            const logsTimer = setTimeout(() => {
                setShowLogsPhase1(true);
            }, 500);
            return () => clearTimeout(logsTimer);
        }
    }, [showCommand, currentIndex, commandText]);

    useEffect(() => {
        if (showLogsPhase1) {
            const phase2Timer = setTimeout(() => {
                setShowLogsPhase2(true);
            }, 2000);
            return () => clearTimeout(phase2Timer);
        }
    }, [showLogsPhase1]);

    useEffect(() => {
        if (showLogsPhase2) {
            const phase3Timer = setTimeout(() => {
                setShowLogsPhase3(true);
            }, 2000);
            return () => clearTimeout(phase3Timer);
        }
    }, [showLogsPhase2]);

    return (
        <section className="relative overflow-hidden bg-gradient-to-br from-background via-background to-muted/20 min-h-screen flex items-center justify-center">
            <HeroBackground />

            <div className="relative container mx-auto px-3 sm:px-4 md:px-6 py-8 sm:py-12 md:py-16 lg:py-20 z-10">
                <div className={`grid lg:grid-cols-2 gap-6 sm:gap-8 lg:gap-12 items-center transition-all duration-1000 ease-out ${
                    isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                }`}>
                    <div className="order-2 lg:order-1 text-center lg:text-left">
                        <HeroTitle isVisible={isVisible} latestVersion={latestVersion} />

                        <div className={`transition-all duration-1000 delay-600 ${
                            isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
                        }`}>
                            <GitHubStatsComponent />
                        </div>

                        <HeroActions isVisible={isVisible} />
                    </div>

                    <div className="hidden lg:block order-1 lg:order-2">
                        <HeroTerminal
                            showCommand={showCommand}
                            showLogsPhase1={showLogsPhase1}
                            showLogsPhase2={showLogsPhase2}
                            showLogsPhase3={showLogsPhase3}
                            typedText={typedText}
                            currentIndex={currentIndex}
                            commandText={commandText}
                            latestVersion={latestVersion}
                            terminalLinesPhase1={terminalLinesPhase1}
                            terminalLinesPhase2={terminalLinesPhase2}
                            terminalLinesPhase3={terminalLinesPhase3}
                        />
                    </div>
                </div>
            </div>

            <div className="absolute bottom-0 left-0 right-0 h-32 sm:h-48 md:h-64 bg-gradient-to-t from-background via-background/95 via-background/80 to-transparent z-5" />
        </section>
    );
} 
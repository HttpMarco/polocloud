'use client';

import { motion } from 'framer-motion';
import { TerminalLine } from './types';

interface HeroTerminalProps {
    showCommand: boolean;
    showLogsPhase1: boolean;
    showLogsPhase2: boolean;
    showLogsPhase3: boolean;
    typedText: string;
    currentIndex: number;
    commandText: string;
    latestVersion: string;
    terminalLinesPhase1: TerminalLine[];
    terminalLinesPhase2: TerminalLine[];
    terminalLinesPhase3: TerminalLine[];
}

export function HeroTerminal({
    showCommand,
    showLogsPhase1,
    showLogsPhase2,
    showLogsPhase3,
    typedText,
    currentIndex,
    commandText,
    latestVersion,
    terminalLinesPhase1,
    terminalLinesPhase2,
    terminalLinesPhase3
}: HeroTerminalProps) {
    const renderMessage = (message: string, highlight?: string, highlightColor?: string) => {
        if (!highlight || !highlightColor) {
            return <span>{message}</span>;
        }

        const parts = message.split(highlight);
        return (
            <>
                {parts.map((part, index) => (
                    <span key={index}>
                        {part}
                        {index < parts.length - 1 && (
                            <span className={highlightColor}>{highlight}</span>
                        )}
                    </span>
                ))}
            </>
        );
    };

    return (
        <motion.div
            className="order-1 lg:order-2 ml-6"
            initial={{ opacity: 0, x: 50 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.8, delay: 0.3 }}
        >
            <div className="bg-[#282c34] rounded-lg shadow-2xl border border-border/50 overflow-hidden h-[16rem] sm:h-[20rem] md:h-[24rem] lg:h-[26rem]">
                <div className="bg-[#21252b] px-2 sm:px-3 md:px-4 py-2 sm:py-3 flex items-center justify-between border-b border-border/30">
                    <div className="flex items-center gap-1 sm:gap-2">
                        <div className="w-2 h-2 sm:w-3 sm:h-3 bg-red-500 rounded-full"></div>
                        <div className="w-2 h-2 sm:w-3 sm:h-3 bg-yellow-500 rounded-full"></div>
                        <div className="w-2 h-2 sm:w-3 sm:h-3 bg-green-500 rounded-full"></div>
                    </div>
                    <div className="text-xs text-muted-foreground font-mono">
                        PoloCloud Terminal
                    </div>
                    <div className="w-8 sm:w-12 md:w-16"></div>
                </div>

                <div className="p-2 sm:p-3 md:p-4 font-mono text-xs sm:text-sm">
                    {showCommand && (
                        <div className="flex items-center gap-1 sm:gap-2 mb-3 sm:mb-4">
                            <span className="text-cyan-400 font-mono text-xs sm:text-sm">/home/polocloud</span>
                            <span className="text-gray-400 font-mono text-xs sm:text-sm">$</span>
                            <span className="text-gray-300 font-mono text-xs sm:text-sm">
                                {typedText}
                                {currentIndex < commandText.length && (
                                    <motion.span
                                        className="text-gray-300"
                                        animate={{ opacity: [1, 0, 1] }}
                                        transition={{ duration: 0.8, repeat: Infinity }}
                                    >
                                        |
                                    </motion.span>
                                )}
                            </span>
                        </div>
                    )}

                    {showLogsPhase1 && terminalLinesPhase1.map((line, index) => (
                        <motion.div
                            key={`phase1-${index}`}
                            className="flex items-start mb-1"
                            initial={{ opacity: 0, x: -20 }}
                            animate={{ opacity: 1, x: 0 }}
                            transition={{ duration: 0.3, delay: index * 0.1 }}
                        >
                            <span className="text-gray-500 font-mono text-xs w-16 flex-shrink-0">
                                {line.time}
                            </span>
                            <span className="text-gray-400 font-mono text-xs mx-1">
                                |
                            </span>
                            <span className={`font-mono text-xs w-12 flex-shrink-0 mx-1 ${
                                line.level === 'WARN' ? 'text-yellow-400' : 'text-gray-300'
                            }`}>
                                {line.level}
                            </span>
                            <span className={`font-mono text-xs mx-1 ${line.color}`}>
                                {renderMessage(line.message, line.highlight, line.highlightColor)}
                            </span>
                        </motion.div>
                    ))}

                    {showLogsPhase2 && terminalLinesPhase2.map((line, index) => (
                        <motion.div
                            key={`phase2-${index}`}
                            className="flex items-start mb-1"
                            initial={{ opacity: 0, x: -20 }}
                            animate={{ opacity: 1, x: 0 }}
                            transition={{ duration: 0.3, delay: index * 0.1 }}
                        >
                            <span className="text-gray-500 font-mono text-xs w-16 flex-shrink-0">
                                {line.time}
                            </span>
                            <span className="text-gray-400 font-mono text-xs mx-1">
                                |
                            </span>
                            <span className={`font-mono text-xs w-12 flex-shrink-0 mx-1 ${
                                line.level === 'WARN' ? 'text-yellow-400' : 'text-white'
                            }`}>
                                {line.level}
                            </span>
                            <span className={`font-mono text-xs mx-1 ${line.color}`}>
                                {renderMessage(line.message, line.highlight, line.highlightColor)}
                            </span>
                        </motion.div>
                    ))}

                    {showLogsPhase3 && terminalLinesPhase3.map((line, index) => (
                        <motion.div
                            key={`phase3-${index}`}
                            className="flex items-start mb-1"
                            initial={{ opacity: 0, x: -20 }}
                            animate={{ opacity: 1, x: 0 }}
                            transition={{ duration: 0.3, delay: index * 0.1 }}
                        >
                            <span className="text-gray-500 font-mono text-xs w-16 flex-shrink-0">
                                {line.time}
                            </span>
                            <span className="text-gray-400 font-mono text-xs mx-1">
                                |
                            </span>
                            <span className={`font-mono text-xs w-12 flex-shrink-0 mx-1 ${
                                line.level === 'WARN' ? 'text-yellow-400' : 'text-white'
                            }`}>
                                {line.level}
                            </span>
                            <span className={`font-mono text-xs mx-1 ${line.color}`}>
                                {renderMessage(line.message, line.highlight, line.highlightColor)}
                            </span>
                        </motion.div>
                    ))}

                    {showLogsPhase3 && (
                        <motion.div
                            className="flex items-center gap-2 mt-2"
                            initial={{ opacity: 0 }}
                            animate={{ opacity: 1 }}
                            transition={{ duration: 0.3, delay: 1.5 }}
                        >
                            <span className="text-gray-300 font-mono text-sm">
                                {renderMessage(`polocloud@${latestVersion.replace('v', '')}`, "polocloud", "text-cyan-400")}
                            </span>
                            <span className="text-gray-400 font-mono text-sm">»</span>
                            <motion.span
                                className="text-gray-300 font-mono text-xs"
                                animate={{ opacity: [1, 0, 1] }}
                                transition={{ duration: 1, repeat: Infinity }}
                            >
                                _
                            </motion.span>
                        </motion.div>
                    )}
                </div>
            </div>
        </motion.div>
    );
}

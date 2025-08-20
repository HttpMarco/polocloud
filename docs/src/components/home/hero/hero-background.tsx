'use client';

import { motion } from 'framer-motion';

export function HeroBackground() {
    return (
        <>
            <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]" />

            <div className="absolute inset-0 overflow-hidden pointer-events-none">
                <motion.div
                    className="absolute w-2 h-2 bg-primary/20 rounded-full"
                    animate={{
                        x: [0, 100, 0],
                        y: [0, -50, 0],
                        opacity: [0.3, 0.8, 0.3],
                    }}
                    transition={{
                        duration: 8,
                        repeat: Infinity,
                        ease: "easeInOut",
                    }}
                    style={{
                        left: "10%",
                        top: "20%",
                    }}
                />

                <motion.div
                    className="absolute w-1 h-1 bg-primary/30 rounded-full"
                    animate={{
                        x: [0, -80, 0],
                        y: [0, 60, 0],
                        opacity: [0.2, 0.6, 0.2],
                    }}
                    transition={{
                        duration: 12,
                        repeat: Infinity,
                        ease: "easeInOut",
                        delay: 2,
                    }}
                    style={{
                        left: "80%",
                        top: "30%",
                    }}
                />

                <motion.div
                    className="absolute w-1.5 h-1.5 bg-primary/25 rounded-full"
                    animate={{
                        x: [0, 60, 0],
                        y: [0, -40, 0],
                        opacity: [0.4, 0.7, 0.4],
                    }}
                    transition={{
                        duration: 10,
                        repeat: Infinity,
                        ease: "easeInOut",
                        delay: 4,
                    }}
                    style={{
                        left: "20%",
                        top: "70%",
                    }}
                />

                <motion.div
                    className="absolute w-1 h-1 bg-primary/15 rounded-full"
                    animate={{
                        x: [0, -40, 0],
                        y: [0, 80, 0],
                        opacity: [0.3, 0.5, 0.3],
                    }}
                    transition={{
                        duration: 15,
                        repeat: Infinity,
                        ease: "easeInOut",
                        delay: 1,
                    }}
                    style={{
                        left: "70%",
                        top: "60%",
                    }}
                />

                <motion.div
                    className="absolute w-2 h-2 bg-primary/20 rounded-full"
                    animate={{
                        x: [0, 120, 0],
                        y: [0, -30, 0],
                        opacity: [0.2, 0.8, 0.2],
                    }}
                    transition={{
                        duration: 14,
                        repeat: Infinity,
                        ease: "easeInOut",
                        delay: 3,
                    }}
                    style={{
                        left: "40%",
                        top: "10%",
                    }}
                />

                <motion.div
                    className="absolute w-1 h-1 bg-primary/25 rounded-full"
                    animate={{
                        x: [0, -60, 0],
                        y: [0, 50, 0],
                        opacity: [0.3, 0.6, 0.3],
                    }}
                    transition={{
                        duration: 11,
                        repeat: Infinity,
                        ease: "easeInOut",
                        delay: 5,
                    }}
                    style={{
                        left: "90%",
                        top: "80%",
                    }}
                />

                <motion.div
                    className="absolute w-1.5 h-1.5 bg-primary/20 rounded-full"
                    animate={{
                        x: [0, 80, 0],
                        y: [0, -60, 0],
                        opacity: [0.4, 0.7, 0.4],
                    }}
                    transition={{
                        duration: 13,
                        repeat: Infinity,
                        ease: "easeInOut",
                        delay: 2.5,
                    }}
                    style={{
                        left: "15%",
                        top: "40%",
                    }}
                />

                <motion.div
                    className="absolute w-1 h-1 bg-primary/30 rounded-full"
                    animate={{
                        x: [0, -100, 0],
                        y: [0, 40, 0],
                        opacity: [0.2, 0.5, 0.2],
                    }}
                    transition={{
                        duration: 16,
                        repeat: Infinity,
                        ease: "easeInOut",
                        delay: 6,
                    }}
                    style={{
                        left: "60%",
                        top: "90%",
                    }}
                />

                <motion.div
                    className="absolute w-1.5 h-1.5 bg-primary/25 rounded-full"
                    animate={{
                        x: [0, 70, 0],
                        y: [0, -40, 0],
                        opacity: [0.3, 0.6, 0.3],
                    }}
                    transition={{
                        duration: 12,
                        repeat: Infinity,
                        ease: "easeInOut",
                        delay: 1.5,
                    }}
                    style={{
                        left: "85%",
                        top: "30%",
                    }}
                />

                <motion.div
                    className="absolute w-1 h-1 bg-primary/20 rounded-full"
                    animate={{
                        x: [0, -50, 0],
                        y: [0, 90, 0],
                        opacity: [0.2, 0.4, 0.2],
                    }}
                    transition={{
                        duration: 18,
                        repeat: Infinity,
                        ease: "easeInOut",
                        delay: 3.5,
                    }}
                    style={{
                        left: "25%",
                        top: "85%",
                    }}
                />

                <motion.div
                    className="absolute w-2 h-2 bg-primary/15 rounded-full"
                    animate={{
                        x: [0, 90, 0],
                        y: [0, -70, 0],
                        opacity: [0.1, 0.3, 0.1],
                    }}
                    transition={{
                        duration: 20,
                        repeat: Infinity,
                        ease: "easeInOut",
                        delay: 4.5,
                    }}
                    style={{
                        left: "75%",
                        top: "15%",
                    }}
                />

                <motion.div
                    className="absolute w-1 h-1 bg-primary/35 rounded-full"
                    animate={{
                        x: [0, -80, 0],
                        y: [0, 60, 0],
                        opacity: [0.4, 0.7, 0.4],
                    }}
                    transition={{
                        duration: 14,
                        repeat: Infinity,
                        ease: "easeInOut",
                        delay: 2,
                    }}
                    style={{
                        left: "45%",
                        top: "75%",
                    }}
                />

                <motion.div
                    className="absolute w-1.5 h-1.5 bg-primary/20 rounded-full"
                    animate={{
                        x: [0, 60, 0],
                        y: [0, -50, 0],
                        opacity: [0.2, 0.5, 0.2],
                    }}
                    transition={{
                        duration: 16,
                        repeat: Infinity,
                        ease: "easeInOut",
                        delay: 5.5,
                    }}
                    style={{
                        left: "5%",
                        top: "25%",
                    }}
                />

                <motion.div
                    className="absolute w-1 h-1 bg-primary/30 rounded-full"
                    animate={{
                        x: [0, -70, 0],
                        y: [0, 30, 0],
                        opacity: [0.3, 0.6, 0.3],
                    }}
                    transition={{
                        duration: 13,
                        repeat: Infinity,
                        ease: "easeInOut",
                        delay: 1,
                    }}
                    style={{
                        left: "95%",
                        top: "50%",
                    }}
                />

                <motion.div
                    className="absolute w-2 h-2 bg-primary/25 rounded-full"
                    animate={{
                        x: [0, 40, 0],
                        y: [0, -90, 0],
                        opacity: [0.2, 0.4, 0.2],
                    }}
                    transition={{
                        duration: 17,
                        repeat: Infinity,
                        ease: "easeInOut",
                        delay: 6.5,
                    }}
                    style={{
                        left: "35%",
                        top: "5%",
                    }}
                />
            </div>
        </>
    );
}

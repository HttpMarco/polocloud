'use client';

import { useMemo } from 'react';
import { motion } from 'framer-motion';

export function FlyingParticles() {
    const particles = useMemo(() => {
        return Array.from({ length: 25 }).map((_, i) => {
            const size = Math.random() * 3 + 1;
            const delay = Math.random() * 2;
            const duration = Math.random() * 15 + 10;
            const left = Math.random() * 100;
            const top = Math.random() * 100;
            const xVariation = Math.random() * 150 - 75;
            const yVariation = Math.random() * 200 - 100;
            const zVariation = Math.random() * 250 - 125;
            const wVariation = Math.random() * 300 - 150;
            
            return {
                id: i,
                size,
                delay,
                duration,
                left,
                top,
                xVariation,
                yVariation,
                zVariation,
                wVariation
            };
        });
    }, []);

    return (
        <div className="fixed inset-0 overflow-hidden pointer-events-none z-0">
            {particles.map((particle) => (
                <motion.div
                    key={particle.id}
                    className="absolute rounded-full"
                    style={{
                        width: `${particle.size}px`,
                        height: `${particle.size}px`,
                        left: `${particle.left}%`,
                        top: `${particle.top}%`,
                        backgroundColor: 'oklch(0.7554 0.1534 231.639)'
                    }}
                    animate={{
                        y: [0, -100, -200, -300, -400],
                        x: [0, particle.xVariation, particle.yVariation, particle.zVariation, particle.wVariation],
                        opacity: [0, 0.3, 0.6, 0.8, 0],
                        scale: [0.2, 0.8, 1.2, 1.5, 0],
                    }}
                    transition={{
                        duration: particle.duration,
                        repeat: Infinity,
                        delay: particle.delay,
                        ease: "linear"
                    }}
                />
            ))}
        </div>
    );
}

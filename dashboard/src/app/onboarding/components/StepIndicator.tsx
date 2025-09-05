'use client';

import { motion } from 'framer-motion';
import Image from 'next/image';

interface StepIndicatorProps {
    currentStep: number;
    steps: { label: string; description: string; }[];
}

export function StepIndicator({ currentStep, steps }: StepIndicatorProps) {
    return (
        <motion.div
            className="text-center mb-16"
            initial={{ opacity: 0, y: -30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8, ease: "easeOut" }}
        >
            <motion.div
                className="flex justify-center"
                initial={{ scale: 0, rotate: -180 }}
                animate={{ scale: 1, rotate: 0 }}
                transition={{ duration: 0.8, delay: 0.2, ease: "easeOut" }}
            >
                <Image
                    src="/logo.png"
                    alt="PoloCloud Logo"
                    width={64}
                    height={64}
                    className="size-16 object-contain"
                />
            </motion.div>
            
            <div className="mt-8">
                <h2 className="text-2xl font-bold text-foreground mb-2">
                    {steps[currentStep]?.label || 'Setup'}
                </h2>
                <p className="text-muted-foreground">
                    {steps[currentStep]?.description || 'Configure your PoloCloud instance'}
                </p>
            </div>
        </motion.div>
    );
}

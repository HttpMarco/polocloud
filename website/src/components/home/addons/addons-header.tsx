'use client';

import { motion } from 'framer-motion';

export function AddonsHeader() {
    return (
        <motion.div
            className="text-center mb-12 sm:mb-16 md:mb-20"
            initial={{ opacity: 0, y: 50 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true, amount: 0.3 }}
            transition={{ duration: 1.2, ease: 'easeOut' }}
        >
            <motion.h2
                className="text-2xl sm:text-3xl md:text-4xl lg:text-5xl font-black mb-4 sm:mb-6 md:mb-8 bg-gradient-to-r from-foreground via-primary to-foreground bg-clip-text text-transparent tracking-tight leading-tight"
                initial={{ opacity: 0, y: 30, scale: 0.9, rotateX: -15 }}
                whileInView={{ opacity: 1, y: 0, scale: 1, rotateX: 0 }}
                viewport={{ once: true, amount: 0.3 }}
                transition={{ duration: 1.2, delay: 0.3, type: 'spring', stiffness: 200 }}
            >
                Addons
            </motion.h2>
            <motion.p
                className="text-sm sm:text-base md:text-lg text-muted-foreground max-w-4xl mx-auto leading-relaxed px-3 sm:px-0"
                initial={{ opacity: 0, y: 20, scale: 0.95 }}
                whileInView={{ opacity: 1, y: 0, scale: 1 }}
                viewport={{ once: true, amount: 0.3 }}
                transition={{ duration: 1.2, delay: 0.6, ease: 'easeOut' }}
            >
                Extend your PoloCloud experience with our official addons. Each addon is designed to enhance your server management capabilities and provide powerful features.
            </motion.p>
        </motion.div>
    );
}

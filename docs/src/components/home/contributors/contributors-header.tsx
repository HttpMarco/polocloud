'use client';

import { motion } from 'framer-motion';

export function ContributorsHeader() {
    return (
        <motion.div
            className="text-center mb-20"
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true, amount: 0.3 }}
            transition={{ duration: 0.8, ease: 'easeOut' }}
        >
            <h2 className="text-3xl md:text-4xl lg:text-5xl font-black mb-8 bg-gradient-to-r from-foreground via-primary to-foreground bg-clip-text text-transparent tracking-tight leading-tight">
                Our Amazing Contributors
            </h2>
            <p className="text-base md:text-lg text-muted-foreground max-w-4xl mx-auto leading-relaxed">
                Meet the talented developers, designers, and engineers who make PoloCloud possible.
                <span className="block mt-2 text-sm md:text-base font-normal">
                    Every contribution, big or small, helps us build the future of Minecraft hosting.
                </span>
            </p>
        </motion.div>
    );
}

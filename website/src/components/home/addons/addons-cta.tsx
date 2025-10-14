'use client';

import { motion } from 'framer-motion';
import { Package } from 'lucide-react';

export function AddonsCTA() {
    return (
        <motion.div
            initial={{ opacity: 0, y: 30, scale: 0.9 }}
            whileInView={{ opacity: 1, y: 0, scale: 1 }}
            viewport={{ once: true }}
            transition={{ duration: 0.8, delay: 1, type: "spring", stiffness: 200 }}
            className="text-center mt-12 sm:mt-16"
        >
            <motion.a
                href="https://modrinth.com/organization/polocloud"
                target="_blank"
                rel="noopener noreferrer"
                whileHover={{
                    scale: 1.05,
                    boxShadow: "0 20px 40px rgba(0, 0, 0, 0.2)",
                    y: -2
                }}
                whileTap={{ scale: 0.95 }}
                className="inline-flex items-center gap-2 sm:gap-3 px-6 sm:px-8 py-3 sm:py-4 bg-gradient-to-r from-primary to-primary/80 text-white rounded-xl font-semibold text-base sm:text-lg hover:from-primary/90 hover:to-primary/70 transition-all duration-300 shadow-lg hover:shadow-xl cursor-pointer"
            >
                <motion.div
                    animate={{ rotate: [0, 10, -10, 0] }}
                    transition={{ duration: 2, repeat: Infinity, ease: "easeInOut" }}
                >
                    <Package className="w-4 h-4 sm:w-5 sm:h-5" />
                </motion.div>
                View on Modrinth
            </motion.a>
            <motion.p
                className="text-xs sm:text-sm text-muted-foreground dark:text-white/70 mt-3 sm:mt-4"
                initial={{ opacity: 0 }}
                whileInView={{ opacity: 1 }}
                viewport={{ once: true }}
                transition={{ delay: 1.2 }}
            >
                Check out all our projects!
            </motion.p>
        </motion.div>
    );
}

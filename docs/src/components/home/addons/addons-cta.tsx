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
            className="text-center mt-16"
        >
            <motion.a
                href="https://modrinth.com/organization/polocloud"
                target="_blank"
                rel="noopener noreferrer"
                whileHover={{
                    scale: 1.05,
                    boxShadow: "0 20px 40px rgba(99, 102, 241, 0.3)",
                    y: -2
                }}
                whileTap={{ scale: 0.95 }}
                className="inline-flex items-center gap-3 px-8 py-4 bg-gradient-to-r from-primary to-primary/80 text-white rounded-xl font-semibold text-lg hover:from-primary/90 hover:to-primary/70 transition-all duration-300 shadow-lg hover:shadow-xl cursor-pointer"
            >
                <motion.div
                    animate={{ rotate: [0, 10, -10, 0] }}
                    transition={{ duration: 2, repeat: Infinity, ease: "easeInOut" }}
                >
                    <Package className="w-5 h-5" />
                </motion.div>
                View on Modrinth
            </motion.a>
            <motion.p
                className="text-sm text-muted-foreground dark:text-white/70 mt-4"
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

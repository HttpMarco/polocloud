'use client';

import { motion } from 'framer-motion';

export function GroupHeader() {
    return (
        <div className="px-6 py-6">
            <div className="mb-8">
                <motion.h1 
                    className="text-4xl font-bold text-foreground mb-3"
                    initial={{ opacity: 0, y: -20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.6 }}
                >
                    Groups
                </motion.h1>
                <motion.p 
                    className="text-lg text-muted-foreground"
                    initial={{ opacity: 0, y: -10 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.6, delay: 0.1 }}
                >
                    Manage your server groups and their services efficiently
                </motion.p>
            </div>
        </div>
    );
}

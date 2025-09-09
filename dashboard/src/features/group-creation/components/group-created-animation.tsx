'use client';

import { motion, AnimatePresence } from 'framer-motion';
import { CheckCircle } from 'lucide-react';
import { useEffect } from 'react';
import { GroupCreatedAnimationProps } from '../types/group-creation.types';

export function GroupCreatedAnimation({ isVisible, onAnimationComplete }: GroupCreatedAnimationProps) {
  useEffect(() => {
    if (isVisible) {
      const timer = setTimeout(() => {
        onAnimationComplete();
      }, 3000);
      
      return () => clearTimeout(timer);
    }
  }, [isVisible, onAnimationComplete]);

  return (
    <AnimatePresence>
      {isVisible && (
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          transition={{ duration: 0.3 }}
          className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm"
        >
          {}
          <motion.div
            initial={{ scale: 0.5, opacity: 0 }}
            animate={{ scale: 1, opacity: 1 }}
            exit={{ scale: 0.5, opacity: 0 }}
            transition={{ duration: 0.4, type: "spring", stiffness: 300, damping: 25 }}
            className="relative bg-card border border-border/50 shadow-2xl rounded-xl p-6 max-w-sm w-full mx-4 text-center"
          >
            {}
            <motion.div
              initial={{ scale: 0, rotate: -180 }}
              animate={{ scale: 1, rotate: 0 }}
              transition={{ duration: 0.5, delay: 0.1, type: "spring", stiffness: 200 }}
              className="mx-auto w-16 h-16 bg-green-500 rounded-full flex items-center justify-center mb-4"
            >
              <CheckCircle className="w-8 h-8 text-white" />
            </motion.div>

            {}
            <motion.div
              initial={{ y: 20, opacity: 0 }}
              animate={{ y: 0, opacity: 1 }}
              transition={{ duration: 0.4, delay: 0.2 }}
              className="space-y-3"
            >
              <h2 className="text-xl font-semibold text-foreground">
                Group Created Successfully!
              </h2>
              <p className="text-sm text-muted-foreground">
                Your new service group has been created and is now ready to use.
              </p>
              <p className="text-xs text-muted-foreground">
                Redirecting in 3 seconds...
              </p>
            </motion.div>

            {}
            <motion.div
              initial={{ y: 20, opacity: 0 }}
              animate={{ y: 0, opacity: 1 }}
              transition={{ duration: 0.4, delay: 0.3 }}
              className="mt-6"
            >
              <motion.button
                whileHover={{ scale: 1.02 }}
                whileTap={{ scale: 0.98 }}
                onClick={onAnimationComplete}
                className="px-4 py-2 bg-[oklch(0.7554 0.1534 231.639)] text-white font-medium rounded-lg hover:opacity-90 transition-opacity duration-200"
              >
                Continue
              </motion.button>
            </motion.div>
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  );
}

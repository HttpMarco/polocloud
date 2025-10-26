'use client';

import { CompatibilityCards } from './compatibility-cards';
import { motion } from 'framer-motion';

export function CompatibilitySection() {
  return (
    <section className="relative py-20 sm:py-24 lg:py-32 overflow-hidden">
      <motion.div 
        className="absolute top-0 left-0 right-0 h-32 sm:h-40 lg:h-48 bg-gradient-to-b from-background via-background/95 via-background/80 to-transparent"
        initial={{ opacity: 0 }}
        whileInView={{ opacity: 1 }}
        viewport={{ once: true, amount: 0.3 }}
        transition={{ duration: 1.2 }}
      />

      <motion.div 
        className="absolute inset-0 bg-gradient-to-b from-background via-muted/5 to-muted/5"
        initial={{ opacity: 0 }}
        whileInView={{ opacity: 1 }}
        viewport={{ once: true, amount: 0.3 }}
        transition={{ duration: 1.2, delay: 0.3 }}
      />

      <motion.div 
        className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]"
        initial={{ opacity: 0 }}
        whileInView={{ opacity: 1 }}
        viewport={{ once: true, amount: 0.3 }}
        transition={{ duration: 1.2, delay: 0.6 }}
      />

      <motion.div 
        className="absolute top-16 sm:top-20 left-6 sm:left-10 w-24 sm:w-32 h-24 sm:h-32 bg-primary/5 rounded-full blur-3xl animate-pulse"
        initial={{ opacity: 0, scale: 0, rotate: 180 }}
        whileInView={{ opacity: 1, scale: 1, rotate: 0 }}
        viewport={{ once: true, amount: 0.3 }}
        transition={{ duration: 1.2, delay: 0.8, type: 'spring', stiffness: 100 }}
      />
      <motion.div 
        className="absolute bottom-16 sm:bottom-20 right-6 sm:right-10 w-32 sm:w-40 h-32 sm:h-40 bg-primary/5 rounded-full blur-3xl animate-pulse delay-1000"
        initial={{ opacity: 0, scale: 0, rotate: -180 }}
        whileInView={{ opacity: 1, scale: 1, rotate: 0 }}
        viewport={{ once: true, amount: 0.3 }}
        transition={{ duration: 1.2, delay: 1.0, type: 'spring', stiffness: 100 }}
      />
      <motion.div 
        className="absolute top-1/2 left-1/4 w-20 sm:w-24 h-20 sm:h-24 bg-primary/3 rounded-full blur-2xl animate-pulse"
        initial={{ opacity: 0, scale: 0, y: 50 }}
        whileInView={{ opacity: 1, scale: 1, y: 0 }}
        viewport={{ once: true, amount: 0.3 }}
        transition={{ duration: 1.2, delay: 1.2, type: 'spring', stiffness: 150 }}
      />

      <div className="relative container mx-auto px-4 sm:px-6">
        <motion.div 
          className="text-center mb-16 sm:mb-20"
          initial={{ opacity: 0, y: 50 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true, amount: 0.3 }}
          transition={{ duration: 1.2, ease: 'easeOut' }}
        >
          <motion.h2 
            className="text-2xl sm:text-3xl md:text-4xl lg:text-5xl font-black mb-6 sm:mb-8 bg-gradient-to-r from-foreground via-primary to-foreground bg-clip-text text-transparent tracking-tight leading-tight"
            initial={{ opacity: 0, y: 30, scale: 0.9 }}
            whileInView={{ opacity: 1, y: 0, scale: 1 }}
            viewport={{ once: true, amount: 0.3 }}
            transition={{ duration: 1.2, delay: 0.3, type: 'spring', stiffness: 200 }}
          >
            Platform Compatibility
          </motion.h2>
          <motion.p 
            className="text-sm sm:text-base md:text-lg text-muted-foreground max-w-4xl mx-auto leading-relaxed px-4"
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true, amount: 0.3 }}
            transition={{ duration: 1.2, delay: 0.6, ease: 'easeOut' }}
          >
            Check which Minecraft versions and platforms are supported by PoloCloud.
            <motion.span 
              className="block mt-2 text-xs sm:text-sm md:text-base font-normal"
              initial={{ opacity: 0, y: 15 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true, amount: 0.3 }}
              transition={{ duration: 1.2, delay: 0.8, ease: 'easeOut' }}
            >
              Our comprehensive compatibility ensures you can run your server on your preferred setup.
            </motion.span>
          </motion.p>
        </motion.div>
        
        <motion.div 
          initial={{ opacity: 0, y: 80, scale: 0.9 }}
          whileInView={{ opacity: 1, y: 0, scale: 1 }}
          viewport={{ once: true, amount: 0.3 }}
          transition={{ duration: 1.5, delay: 0.9, type: 'spring', stiffness: 150, damping: 20 }}
        >
          <motion.div 
            initial={{ opacity: 0 }}
            whileInView={{ opacity: 1 }}
            viewport={{ once: true, amount: 0.3 }}
            transition={{ duration: 1.0, delay: 1.0 }}
          >
            <CompatibilityCards />
          </motion.div>
        </motion.div>
      </div>

      <motion.div 
        className="absolute bottom-0 left-0 right-0 h-32 sm:h-40 lg:h-48 bg-gradient-to-t from-background via-background/95 via-background/80 to-transparent"
        initial={{ opacity: 0 }}
        whileInView={{ opacity: 1 }}
        viewport={{ once: true, amount: 0.3 }}
        transition={{ duration: 1.2, delay: 1.2 }}
      />
    </section>
  );
} 
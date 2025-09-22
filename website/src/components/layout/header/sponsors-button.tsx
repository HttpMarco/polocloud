'use client';

import { Heart } from 'lucide-react';

export function SponsorsButton() {
  return (
    <a
      href="https://github.com/sponsors/HttpMarco"
      target="_blank"
      rel="noopener noreferrer"
      className="flex items-center justify-center w-10 h-10 text-sm font-semibold text-pink-600 hover:text-pink-700 transition-all duration-300 hover:scale-105 rounded-lg bg-pink-50 hover:bg-pink-100 dark:bg-pink-950/30 dark:hover:bg-pink-950/50 shadow-md hover:shadow-lg border border-pink-200 hover:border-pink-300 dark:border-pink-800 dark:hover:border-pink-700 group relative overflow-hidden flex-shrink-0"
      title="Sponsor HttpMarco on GitHub"
    >
      <div className="absolute inset-0 bg-gradient-to-r from-pink-500/0 via-pink-500/5 to-pink-500/0 translate-x-[-100%] group-hover:translate-x-[100%] transition-transform duration-700" />

      <Heart className="w-4 h-4 group-hover:scale-110 transition-transform duration-300 relative z-10 group-hover:animate-pulse" />
    </a>
  );
} 
'use client';

import { Download } from 'lucide-react';
import Link from 'next/link';

export function DownloadButton() {
  return (
    <Link
      href="/download"
      className="flex items-center justify-center w-10 h-10 text-sm font-semibold text-primary hover:text-primary/80 transition-all duration-300 hover:scale-105 rounded-lg bg-primary/10 hover:bg-primary/20 dark:bg-primary/20 dark:hover:bg-primary/30 shadow-md hover:shadow-lg border border-primary/30 hover:border-primary/50 dark:border-primary/40 dark:hover:border-primary/60 group relative overflow-hidden flex-shrink-0"
      title="Download"
    >
      <div className="absolute inset-0 bg-gradient-to-r from-primary/0 via-primary/10 to-primary/0 translate-x-[-100%] group-hover:translate-x-[100%] transition-transform duration-700" />

      <Download className="w-4 h-4 group-hover:scale-110 transition-transform duration-300 relative z-10 group-hover:animate-bounce" />
    </Link>
  );
} 
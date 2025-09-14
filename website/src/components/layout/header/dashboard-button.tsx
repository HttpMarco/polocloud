'use client';

import { LayoutDashboard } from 'lucide-react';
import Link from 'next/link';

export function DashboardButton() {
  return (
    <a
      href="https://dashboard.polocloud.de/onboarding"
      target="_blank"
      rel="noopener noreferrer"
      className="flex items-center justify-center w-10 h-10 text-sm font-semibold text-emerald-600 hover:text-emerald-700 transition-all duration-300 hover:scale-105 rounded-lg bg-emerald-50 hover:bg-emerald-100 dark:bg-emerald-950/30 dark:hover:bg-emerald-950/50 shadow-md hover:shadow-lg border border-emerald-200 hover:border-emerald-300 dark:border-emerald-800 dark:hover:border-emerald-700 group relative overflow-hidden flex-shrink-0"
      title="Dashboard"
    >
      <div className="absolute inset-0 bg-gradient-to-r from-emerald-500/0 via-emerald-500/5 to-emerald-500/0 translate-x-[-100%] group-hover:translate-x-[100%] transition-transform duration-700" />

      <LayoutDashboard className="w-4 h-4 group-hover:scale-110 transition-transform duration-300 relative z-10 group-hover:rotate-12" />
    </a>
  );
} 
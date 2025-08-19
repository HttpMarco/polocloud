'use client';

import { MessageSquare } from 'lucide-react';
import Link from 'next/link';

export function FeedbackButton() {
  return (
    <Link
      href="/feedback"
      className="flex items-center gap-2 px-4 py-2 text-sm font-medium text-muted-foreground hover:text-foreground transition-all duration-300 hover:scale-105 rounded-lg hover:bg-card/50 backdrop-blur-sm border border-transparent hover:border-border/30 group"
    >
      <MessageSquare className="w-4 h-4 group-hover:scale-110 transition-transform duration-300" />
      <span>Feedback</span>
    </Link>
  );
}

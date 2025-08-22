'use client';

import { Home } from 'lucide-react';
import { useRouter, usePathname } from 'next/navigation';

export function HomeDropdown() {
  const router = useRouter();
  const pathname = usePathname();

  const goToHome = () => {
    if (pathname !== '/') {
      // Wenn nicht auf der Homepage, dorthin navigieren
      router.push('/');
    } else {
      // Wenn bereits auf der Homepage, zur Hero-Sektion scrollen
      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      });
    }
  };

  return (
    <button
      onClick={goToHome}
      className="flex items-center gap-2 px-4 py-2 text-sm font-medium text-muted-foreground hover:text-foreground transition-all duration-300 hover:scale-105 rounded-lg hover:bg-card/50 backdrop-blur-sm border border-transparent hover:border-border/30"
    >
      <Home className="w-4 h-4" />
      <span>Home</span>
    </button>
  );
} 
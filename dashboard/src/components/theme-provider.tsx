'use client';

import { useEffect, useRef } from 'react';

export function ThemeProvider({ children }: { children: React.ReactNode }) {
  const isInitialized = useRef(false);

  useEffect(() => {

    if (!isInitialized.current) {
      const savedTheme = localStorage.getItem('theme') || 'dark';
      document.documentElement.classList.remove('dark', 'darker', 'ocean', 'purple', 'forest', 'sunset', 'light');
      document.documentElement.classList.add(savedTheme);
      isInitialized.current = true;
    }
  }, []);

  return <>{children}</>;
}

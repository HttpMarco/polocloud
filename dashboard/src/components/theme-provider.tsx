'use client';

import { useEffect, useRef } from 'react';

export function ThemeProvider({ children }: { children: React.ReactNode }) {
  const isInitialized = useRef(false);

  useEffect(() => {

    if (!isInitialized.current) {
      const savedTheme = localStorage.getItem('theme') || 'dark';
      const allThemes = ['dark', 'darker', 'ocean', 'purple', 'forest', 'sunset', 'neon', 'rose', 'coffee', 'mountain', 'cosmic', 'light'];
      document.documentElement.classList.remove(...allThemes);
      document.documentElement.classList.add(savedTheme);
      isInitialized.current = true;
    }
  }, []);

  return <>{children}</>;
}

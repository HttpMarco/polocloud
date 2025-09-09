'use client';

import { useEffect, useState } from 'react';
import { useRouter, usePathname } from 'next/navigation';

interface AuthGuardProps {
  children: React.ReactNode;
}

export function AuthGuard({ children }: AuthGuardProps) {
  const [isChecking, setIsChecking] = useState(true);
  const router = useRouter();
  const pathname = usePathname();

  useEffect(() => {
    const checkAuthentication = async () => {
      if (pathname === '/login' || pathname === '/onboarding') {
        setIsChecking(false);
        return;
      }

      try {
        const response = await fetch('/api/auth/token');
        if (response.ok) {
          const data = await response.json();
          if (data.success && data.token) {
            setIsChecking(false);
            return;
          }
        }
      } catch {
      }

      let hasToken = false;

      const cookies = document.cookie.split(';');
      for (const cookie of cookies) {
        const [name, value] = cookie.trim().split('=');
        if (name === 'token' && value) {
          hasToken = true;
          break;
        }
      }

      if (!hasToken) {
        const tokenMatch = document.cookie.match(/token=([^;]+)/);
        if (tokenMatch && tokenMatch[1]) {
          hasToken = true;
        }
      }

      if (!hasToken) {
        router.push('/onboarding');
        return;
      }

      setIsChecking(false);
    };

    checkAuthentication();
  }, [pathname, router]);

  if (isChecking) {
    return (
      <div className="w-full h-screen flex items-center justify-center bg-background">
        <div className="w-8 h-8 border-2 border-primary border-t-transparent rounded-full animate-spin"></div>
      </div>
    );
  }

  return <>{children}</>;
}

'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';

interface AuthState {
  isAuthenticated: boolean;
  isLoading: boolean;
  user: {
    username: string;
    userUUID: string;
    role: any;
  } | null;
}

export function useAuth() {
  const [authState, setAuthState] = useState<AuthState>({
    isAuthenticated: false,
    isLoading: true,
    user: null
  });
  
  const router = useRouter();

  useEffect(() => {
    const checkAuth = async () => {
      try {
        const isLoggedIn = localStorage.getItem('isLoggedIn');
        
        if (!isLoggedIn || isLoggedIn !== 'true') {
          setAuthState({
            isAuthenticated: false,
            isLoading: false,
            user: null
          });
          return;
        }

        const response = await fetch('/api/auth/me');
        
        if (response.ok) {
          const data = await response.json();
          
          if (data.authenticated && data.user) {
            setAuthState({
              isAuthenticated: true,
              isLoading: false,
              user: data.user
            });
          } else {
            localStorage.removeItem('isLoggedIn');
            setAuthState({
              isAuthenticated: false,
              isLoading: false,
              user: null
            });
          }
        } else {

          localStorage.removeItem('isLoggedIn');
          setAuthState({
            isAuthenticated: false,
            isLoading: false,
            user: null
          });
        }
      } catch (error) {
        console.error('Auth check failed:', error);
        localStorage.removeItem('isLoggedIn');
        setAuthState({
          isAuthenticated: false,
          isLoading: false,
          user: null
        });
      }
    };

    checkAuth();
  }, []);

  const logout = () => {
    localStorage.removeItem('isLoggedIn');
    setAuthState({
      isAuthenticated: false,
      isLoading: false,
      user: null
    });
    router.push('/login');
  };

  return {
    ...authState,
    logout
  };
}

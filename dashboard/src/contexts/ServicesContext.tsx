'use client';

import React, { createContext, useContext, useState, useCallback, useEffect } from 'react';
import { useServicesWebSocket } from '@/hooks/useServicesWebSocket';
import { Service } from '@/types/services';
import { API_ENDPOINTS } from '@/lib/api';

interface ServicesContextType {
  services: Service[];
  isLoading: boolean;
  error: string | null;
  connectionInfo: any;
  isConnected: boolean;
  loadServices: () => Promise<void>;
  refreshServices: () => Promise<void>;
}

const ServicesContext = createContext<ServicesContextType | undefined>(undefined);

export function ServicesProvider({ children }: { children: React.ReactNode }) {
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  
  const { services, setServices, connectionInfo, isConnected } = useServicesWebSocket();

  const loadServices = useCallback(async () => {
    try {
      setIsLoading(true);
      setError(null);
      
      const response = await fetch(API_ENDPOINTS.SERVICES.LIST);
      if (response.ok) {
        const data = await response.json();
        
        if (Array.isArray(data)) {
          setServices(data);
        } else {
          setError('Invalid response format from server');
        }
      } else {
        const errorData = await response.json();
        setError(errorData.error || 'Failed to load services');
      }
    } catch {
      setError('Failed to load services');
    } finally {
      setIsLoading(false);
    }
  }, [setServices]);

  const refreshServices = useCallback(async () => {
    await loadServices();
  }, [loadServices]);

  useEffect(() => {
    loadServices();
  }, [loadServices]);

  const value: ServicesContextType = {
    services,
    isLoading,
    error,
    connectionInfo,
    isConnected,
    loadServices,
    refreshServices
  };

  return (
    <ServicesContext.Provider value={value}>
      {children}
    </ServicesContext.Provider>
  );
}

export function useServices() {
  const context = useContext(ServicesContext);
  if (context === undefined) {
    throw new Error('useServices must be used within a ServicesProvider');
  }
  return context;
}

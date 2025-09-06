import { useState, useCallback, useEffect } from 'react';
import { useWebSocketSystem } from './useWebSocketSystem';
import { Service } from '@/types/services';

interface UseServicesWebSocketReturn {
  services: Service[];
  setServices: React.Dispatch<React.SetStateAction<Service[]>>;
  connectionInfo: any;
  isConnected: boolean;
  connect: () => Promise<void>;
  disconnect: () => void;
}

export function useServicesWebSocket(): UseServicesWebSocketReturn {
  const [services, setServices] = useState<Service[]>([]);

  const { connectionInfo, isConnected, connect, disconnect } = useWebSocketSystem({
    path: '/services/update',
    autoConnect: true,
    onMessage: (message) => {
      try {
        let updateData;
        
        if (typeof message.data === 'string') {
          try {
            updateData = JSON.parse(message.data);
          } catch {
            return;
          }
        } else if (message.data && typeof message.data === 'object') {
          updateData = message.data;
        } else if (message && message.serviceName) {
          updateData = message;
        } else {
          return;
        }

        if (updateData && updateData.serviceName && updateData.state) {
          setServices(prev => prev.map(service => 
            service.name === updateData.serviceName 
              ? { 
                  ...service, 
                  state: updateData.state,

                  ...(updateData.state === 'STARTING' || updateData.state === 'PREPARING' ? {
                    playerCount: -1,
                    maxPlayerCount: -1,
                    cpuUsage: -1,
                    memoryUsage: -1,
                    maxMemory: -1
                  } : {}),

                  ...(updateData.state === 'STOPPING' || updateData.state === 'STOPPED' ? {
                    playerCount: 0,
                    maxPlayerCount: 0,
                    cpuUsage: 0,
                    memoryUsage: 0,
                    maxMemory: 0
                  } : {})
                }
              : service
          ));
        }
      } catch {
        // Silent fail
      }
    }
  });

  return {
    services,
    setServices,
    connectionInfo,
    isConnected,
    connect,
    disconnect
  };
}

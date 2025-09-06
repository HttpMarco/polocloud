import React, { useEffect, useRef, useState, useCallback, createContext, useContext, ReactNode } from 'react';
import { WebSocketSystem, WebSocketMessage, ConnectionStatus, ConnectionInfo, createWebSocketSystem } from '@/lib/websocket-system';
import { processTerminalLog } from '@/lib/ansi-utils';

const globalMessageCache = new Map<string, number>();
const GLOBAL_DUPLICATE_THRESHOLD = 1000;

interface CentralWebSocketContextType {
  connectionInfo: ConnectionInfo;
  isConnected: boolean;
  services: any[];
  setServices: (services: any[]) => void;
  updateService: (serviceName: string, updates: any) => void;
  terminalLogs: string[];
  addTerminalLog: (log: string) => void;
  clearTerminalLogs: () => void;
  sendTerminalCommand: (command: string) => Promise<void>;
  sendServiceCommand: (serviceName: string, command: string) => Promise<void>;
}

const CentralWebSocketContext = createContext<CentralWebSocketContextType | undefined>(undefined);

export function CentralWebSocketProvider({ children }: { children: ReactNode }) {
  const wsSystemRef = useRef<WebSocketSystem | null>(null);
  const [connectionInfo, setConnectionInfo] = useState<ConnectionInfo>({
    status: 'disconnected',
    method: 'websocket',
    protocol: 'ws',
    reconnectAttempts: 0
  });

  const [services, setServices] = useState<any[]>([]);
  const [terminalLogs, setTerminalLogs] = useState<string[]>([]);
  const lastMessageRef = useRef<string>('');
  const lastMessageTimeRef = useRef<number>(0);

  const handleMessage = useCallback((message: WebSocketMessage) => {
    if (typeof message.data === 'string') {
      const now = Date.now();
      const messageKey = `${message.data}_${message.type || 'unknown'}`;
      
      const lastSeen = globalMessageCache.get(messageKey);
      if (lastSeen && now - lastSeen < GLOBAL_DUPLICATE_THRESHOLD) {
        return;
      }
      
      globalMessageCache.set(messageKey, now);
      
      if (now % 10000 < 100) {
        globalMessageCache.forEach((timestamp, key) => {
          if (now - timestamp > GLOBAL_DUPLICATE_THRESHOLD * 2) {
            globalMessageCache.delete(key);
          }
        });
      }
    }

    // Handle different message types
    if (message.type === 'log' || (typeof message.data === 'string' && message.data.includes('|'))) {
      // Terminal logs
      const messageData = message.data as string;
      
      if (messageData === lastMessageRef.current && Date.now() - lastMessageTimeRef.current < 500) {
        return;
      }
      
      lastMessageRef.current = messageData;
      lastMessageTimeRef.current = Date.now();
      
      const cleanedMessage = processTerminalLog(messageData, { removeColors: true });
      setTerminalLogs(prev => [...prev, cleanedMessage]);
    } else if (typeof message.data === 'object') {
      // Service updates
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
        } else if (message && (message as any).serviceName) {
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
      } catch (error) {
        console.error('Error processing service update:', error);
      }
    }
  }, []);

  const handleConnect = useCallback(() => {
    console.log('Central WebSocket connected');
  }, []);

  const handleDisconnect = useCallback(() => {
    console.log('Central WebSocket disconnected');
  }, []);

  const handleError = useCallback((error: Error) => {
    console.error('Central WebSocket error:', error);
  }, []);

  const handleStatusChange = useCallback((status: ConnectionStatus) => {
    setConnectionInfo(prev => {
      const newInfo = { ...prev, status };
      return newInfo;
    });
  }, []);

  // Initialize WebSocket connection
  useEffect(() => {
    const initializeWebSocket = async () => {
      if (wsSystemRef.current) {
        wsSystemRef.current.disconnect();
        wsSystemRef.current = null;
        
        await new Promise(resolve => setTimeout(resolve, 200));
      }

      // Connect to services update endpoint
      wsSystemRef.current = createWebSocketSystem({
        backendIp: undefined,
        path: '/services/update',
        token: undefined,
        onMessage: handleMessage,
        onConnect: handleConnect,
        onDisconnect: handleDisconnect,
        onError: handleError,
        onStatusChange: handleStatusChange
      });

      if (wsSystemRef.current) {
        wsSystemRef.current.connect().catch(() => {
          // Silent fail - will retry automatically
        });
      }
    };

    initializeWebSocket();

    return () => {
      if (wsSystemRef.current) {
        wsSystemRef.current.disconnect();
        wsSystemRef.current = null;
      }
    };
  }, []);

  // Initialize Terminal WebSocket connection
  useEffect(() => {
    let terminalWsSystem: WebSocketSystem | null = null;

    const initializeTerminalWebSocket = async () => {
      terminalWsSystem = createWebSocketSystem({
        backendIp: undefined,
        path: '/logs',
        token: undefined,
        onMessage: (message) => {
          if (typeof message.data === 'string') {
            const messageData = message.data;
            
            if (messageData === lastMessageRef.current && Date.now() - lastMessageTimeRef.current < 500) {
              return;
            }
            
            lastMessageRef.current = messageData;
            lastMessageTimeRef.current = Date.now();
            
            const cleanedMessage = processTerminalLog(messageData, { removeColors: true });
            setTerminalLogs(prev => [...prev, cleanedMessage]);
          }
        },
        onConnect: () => {
          console.log('Terminal WebSocket connected');
        },
        onDisconnect: () => {
          console.log('Terminal WebSocket disconnected');
        },
        onError: (error) => {
          console.error('Terminal WebSocket error:', error);
        },
        onStatusChange: () => {}
      });

      if (terminalWsSystem) {
        terminalWsSystem.connect().catch(() => {
          // Silent fail - will retry automatically
        });
      }
    };

    initializeTerminalWebSocket();

    return () => {
      if (terminalWsSystem) {
        terminalWsSystem.disconnect();
        terminalWsSystem = null;
      }
    };
  }, []);

  // Update connection info
  useEffect(() => {
    const updateInfo = () => {
      if (wsSystemRef.current) {
        const info = wsSystemRef.current.getConnectionInfo();
        setConnectionInfo(info);
      }
    };

    const timeout = setTimeout(updateInfo, 100);
    
    return () => clearTimeout(timeout);
  }, []);

  const updateService = useCallback((serviceName: string, updates: any) => {
    setServices(prev => prev.map(service => 
      service.name === serviceName 
        ? { ...service, ...updates }
        : service
    ));
  }, []);

  const addTerminalLog = useCallback((log: string) => {
    setTerminalLogs(prev => [...prev, log]);
  }, []);

  const clearTerminalLogs = useCallback(() => {
    setTerminalLogs([]);
  }, []);

  const sendTerminalCommand = useCallback(async (command: string) => {
    try {
      addTerminalLog(`polocloud@3.0.0-pre.6-SNAPSHOT » ${command}`);
      
      const response = await fetch('/api/terminal/command', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          command: command.trim()
        })
      });

      if (!response.ok) {
        const error = await response.json().catch(() => ({ error: 'Failed to send command' }));
        addTerminalLog(`Error: ${error.error || 'Command failed'}`);
      }
    } catch (error) {
      addTerminalLog(`Error: Failed to send command`);
    }
  }, [addTerminalLog]);

  const sendServiceCommand = useCallback(async (serviceName: string, command: string) => {
    try {
      addTerminalLog(`polocloud@${serviceName} » ${command}`);
      
      const response = await fetch(`/api/services/${serviceName}/command`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          command: command.trim()
        })
      });

      if (!response.ok) {
        const error = await response.json().catch(() => ({ error: 'Failed to send command' }));
        addTerminalLog(`Error: ${error.error || 'Command failed'}`);
      }
    } catch (error) {
      addTerminalLog(`Error: Failed to send command`);
    }
  }, [addTerminalLog]);

  const value: CentralWebSocketContextType = {
    connectionInfo,
    isConnected: connectionInfo.status === 'connected',
    services,
    setServices,
    updateService,
    terminalLogs,
    addTerminalLog,
    clearTerminalLogs,
    sendTerminalCommand,
    sendServiceCommand
  };

  return React.createElement(CentralWebSocketContext.Provider, { value }, children);
}

export function useCentralWebSocket() {
  const context = useContext(CentralWebSocketContext);
  if (context === undefined) {
    throw new Error('useCentralWebSocket must be used within a CentralWebSocketProvider');
  }
  return context;
}

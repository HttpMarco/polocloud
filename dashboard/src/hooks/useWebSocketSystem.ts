import { useEffect, useRef, useState, useCallback } from 'react';
import { WebSocketSystem, WebSocketMessage, ConnectionStatus, ConnectionInfo, createWebSocketSystem } from '@/lib/websocket-system';
import { processTerminalLog } from '@/lib/ansi-utils';

const globalMessageCache = new Map<string, number>();
const GLOBAL_DUPLICATE_THRESHOLD = 1000;

interface UseWebSocketSystemProps {
  backendIp?: string;
  path: string;
  token?: string;
  autoConnect?: boolean;
  serviceName?: string; // ✅ Service-Name für Proxy
  onMessage?: (message: WebSocketMessage) => void;
  onConnect?: () => void;
  onDisconnect?: () => void;
  onError?: (error: Error) => void;
}

interface UseWebSocketSystemReturn {
  connectionInfo: ConnectionInfo;
  isConnected: boolean;

  connect: () => Promise<void>;
  disconnect: () => void;
  sendMessage: (message: WebSocketMessage) => void;

  wsSystem: WebSocketSystem | null;
}

export function useWebSocketSystem({
  backendIp,
  path,
  token,
  autoConnect = true,
  serviceName,
  onMessage,
  onConnect,
  onDisconnect,
  onError
}: UseWebSocketSystemProps): UseWebSocketSystemReturn {
  
  const wsSystemRef = useRef<WebSocketSystem | null>(null);
  const [connectionInfo, setConnectionInfo] = useState<ConnectionInfo>({
    status: 'disconnected',
    method: 'websocket',
    protocol: 'ws',
    reconnectAttempts: 0
  });

  const onMessageRef = useRef(onMessage);
  const onConnectRef = useRef(onConnect);
  const onDisconnectRef = useRef(onDisconnect);
  const onErrorRef = useRef(onError);

  useEffect(() => {
    onMessageRef.current = onMessage;
    onConnectRef.current = onConnect;
    onDisconnectRef.current = onDisconnect;
    onErrorRef.current = onError;
  });

  const handleMessage = useCallback((message: WebSocketMessage) => {
    if (typeof message.data === 'string') {
      const now = Date.now();
      const messageKey = `${message.data}_${path}`;
      
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
    
    onMessageRef.current?.(message);
  }, [path]);

  const handleConnect = useCallback(() => {
    onConnectRef.current?.();
  }, []);

  const handleDisconnect = useCallback(() => {
    onDisconnectRef.current?.();
  }, []);

  const handleError = useCallback((error: Error) => {
    onErrorRef.current?.(error);
  }, []);

  const handleStatusChange = useCallback((status: ConnectionStatus) => {
    setConnectionInfo(prev => {
      const newInfo = { ...prev, status };
      return newInfo;
    });
  }, []);

  useEffect(() => {
    const initializeWebSocket = async () => {
      if (wsSystemRef.current) {
        wsSystemRef.current.disconnect();
        wsSystemRef.current = null;
        
        await new Promise(resolve => setTimeout(resolve, 200));
      }

      // ✅ VERBESSERT: Production-Proxy für HTTPS-Frontend
      const isHttpsFrontend = typeof window !== 'undefined' && window.location.protocol === 'https:';
      const isLocalBackend = backendIp && (
        backendIp.includes('localhost') || 
        backendIp.includes('127.0.0.1') || 
        backendIp.startsWith('192.168.') ||
        backendIp.startsWith('10.')
      );

              if (isHttpsFrontend && !isLocalBackend) {
          // ✅ Production: Server-Sent Events über Proxy
          const proxyUrl = `/api/websocket-proxy?path=${encodeURIComponent(path)}${serviceName ? `&service=${encodeURIComponent(serviceName)}` : ''}`;
          
          console.log(`${serviceName || 'WebSocket'} Proxy Debug:`, {
            isHttpsFrontend,
            isLocalBackend,
            path,
            serviceName,
            proxyUrl,
            backendIp,
            windowLocation: typeof window !== 'undefined' ? window.location.href : 'undefined'
          });
          
          const eventSource = new EventSource(proxyUrl);
          
          console.log(`${serviceName || 'WebSocket'} EventSource created:`, proxyUrl);
        
        eventSource.onmessage = (event) => {
          try {
            const data = JSON.parse(event.data);
            console.log(`${serviceName || 'WebSocket'} SSE Message:`, data);
            handleMessage(data);
          } catch (error) {
            console.error('Failed to parse SSE message:', error);
          }
        };

        eventSource.onerror = (error) => {
          console.error(`${serviceName || 'WebSocket'} EventSource error:`, error);
          handleStatusChange('disconnected');
        };

        eventSource.onopen = () => {
          console.log(`${serviceName || 'WebSocket'} EventSource connected`);
          handleStatusChange('connected');
        };

        // Store EventSource for cleanup
        (wsSystemRef as unknown as { current: { disconnect: () => void; getConnectionInfo: () => { status: string }; connect: () => Promise<void>; sendMessage: () => void } }).current = { 
          disconnect: () => eventSource.close(),
          getConnectionInfo: () => ({ status: connectionInfo.status }),
          connect: () => Promise.resolve(),
          sendMessage: () => {}
        };

      } else {
        // ✅ Development: Direkte WebSocket-Verbindung
        console.log(`${serviceName || 'WebSocket'} Development Mode:`, {
          backendIp,
          path,
          serviceName,
          isHttpsFrontend,
          isLocalBackend
        });
        
        wsSystemRef.current = createWebSocketSystem({
          backendIp,
          path,
          token,
          onMessage: handleMessage,
          onConnect: handleConnect,
          onDisconnect: handleDisconnect,
          onError: handleError,
          onStatusChange: handleStatusChange
        });

        if (autoConnect) {
          setTimeout(() => {
            if (wsSystemRef.current && wsSystemRef.current.getConnectionInfo().status === 'disconnected') {
              wsSystemRef.current.connect().catch(() => {
                // Connection failed
              });
            }
          }, 100);
        }
      }
    };

    initializeWebSocket();

    return () => {
      if (wsSystemRef.current) {
        wsSystemRef.current.disconnect();
        wsSystemRef.current = null;
      }
    };
  }, [backendIp, path, token, autoConnect, serviceName, handleMessage, handleConnect, handleDisconnect, handleError, handleStatusChange, connectionInfo.status]);

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

  const connect = useCallback(async () => {
    if (!wsSystemRef.current) {
      throw new Error('WebSocket system not initialized');
    }
    
    await wsSystemRef.current.connect();
  }, []);

  const disconnect = useCallback(() => {
    if (wsSystemRef.current) {
      wsSystemRef.current.disconnect();
    }
  }, []);

  const sendMessage = useCallback((message: WebSocketMessage) => {
    if (!wsSystemRef.current) {
      return;
    }
    
    wsSystemRef.current.sendMessage(message);
  }, []);

  return {
    connectionInfo,
    isConnected: connectionInfo.status === 'connected',
    connect,
    disconnect,
    sendMessage,
    wsSystem: wsSystemRef.current
  };
}

export function useTerminalWebSocket(backendIp?: string, token?: string, autoConnect: boolean = true) {
  const [logs, setLogs] = useState<string[]>([]);
  const lastMessageRef = useRef<string>('');
  const lastMessageTimeRef = useRef<number>(0);
  
  const { connectionInfo, isConnected, connect, disconnect } = useWebSocketSystem({
    backendIp,
    path: '/logs', // ✅ Terminal-Logs Pfad
    token,
    autoConnect,
    serviceName: 'terminal', // ✅ Terminal-Identifier für Proxy
    onMessage: (message) => {
      // ✅ VERBESSERT: Gleiche Message-Filterung wie Service-WebSocket
      if (typeof message.data === 'string') {
        const now = Date.now();
        const messageData = message.data;

        if (messageData === lastMessageRef.current && now - lastMessageTimeRef.current < 500) {
          return;
        }
        
        lastMessageRef.current = messageData;
        lastMessageTimeRef.current = now;
        
        const cleanedMessage = processTerminalLog(messageData, { removeColors: true });
        setLogs(prev => [...prev, cleanedMessage]);
      }
    }
  });

  const sendCommand = useCallback(async (command: string) => {
    try {

      setLogs(prev => [...prev, `polocloud@3.0.0-pre.6-SNAPSHOT » ${command}`]);
      
      const isHttpsFrontend = typeof window !== 'undefined' && window.location.protocol === 'https:';
      const isLocalBackend = backendIp && (
        backendIp.includes('localhost') || 
        backendIp.includes('127.0.0.1') || 
        backendIp.startsWith('192.168.') ||
        backendIp.startsWith('10.')
      );
      
      // ✅ VERBESSERT: WebSocket-Proxy für Production
      const useProxy = isHttpsFrontend && !isLocalBackend;
      const apiRoute = useProxy ? '/api/terminal/command-proxy' : '/api/terminal/command';
      const response = await fetch(apiRoute, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          command: command.trim()
        })
      });

      if (response.ok) {
      } else {
        const error = await response.json().catch(() => ({ error: 'Failed to send command' }));
        setLogs(prev => [...prev, `Error: ${error.error || 'Command failed'}`]);
      }
    } catch {
      setLogs(prev => [...prev, `Error: Failed to send command`]);
    }
  }, [backendIp]);

  const clearLogs = useCallback(() => {
    setLogs([]);
  }, []);

  return {
    logs,
    connectionInfo,
    isConnected,
    connect,
    disconnect,
    sendCommand,
    clearLogs
  };
}

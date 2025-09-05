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
    console.log('📨 Message received:', message);
    
    if (typeof message.data === 'string') {
      const now = Date.now();
      const messageKey = `${message.data}_${path}`;
      
      const lastSeen = globalMessageCache.get(messageKey);
      if (lastSeen && now - lastSeen < GLOBAL_DUPLICATE_THRESHOLD) {
        console.log('🔄 Duplicate message ignored');
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
    console.log('✅ WebSocket connected!');
    onConnectRef.current?.();
  }, []);

  const handleDisconnect = useCallback(() => {
    console.log('❌ WebSocket disconnected!');
    onDisconnectRef.current?.();
  }, []);

  const handleError = useCallback((error: Error) => {
    console.error('❌ WebSocket error:', error);
    onErrorRef.current?.(error);
  }, []);

  const handleStatusChange = useCallback((status: ConnectionStatus) => {
    console.log('🔄 Status changed:', status);
    setConnectionInfo(prev => {
      const newInfo = { ...prev, status };
      return newInfo;
    });
  }, []);

  useEffect(() => {
    const initializeWebSocket = async () => {
      console.log('🚀 Initializing WebSocket System...');
      console.log('📋 Config:', { backendIp, path, token, autoConnect });
      
      if (wsSystemRef.current) {
        wsSystemRef.current.disconnect();
        wsSystemRef.current = null;
        
        await new Promise(resolve => setTimeout(resolve, 200));
      }

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

      console.log('✅ WebSocket System created');

      if (autoConnect) {
        console.log('🔄 Auto-connecting in 100ms...');
        setTimeout(() => {
          if (wsSystemRef.current && wsSystemRef.current.getConnectionInfo().status === 'disconnected') {
            console.log('🔗 Starting connection...');
            wsSystemRef.current.connect().catch((error) => {
              console.error('❌ Connection failed:', error);
            });
          }
        }, 100);
      }
    };

    initializeWebSocket();

    return () => {
      if (wsSystemRef.current) {
        wsSystemRef.current.disconnect();
        wsSystemRef.current = null;
      }
    };
  }, [autoConnect, backendIp, handleConnect, handleDisconnect, handleError, handleMessage, handleStatusChange, path, token]);

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
    path: '/logs',
    token,
    autoConnect,
    onMessage: (message) => {
    
      if (message.type === 'log' && typeof message.data === 'string') {
        
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

  // ✅ DEBUG: Debug-Funktionen für die Konsole
  const debugInfo = useCallback(() => {
    if (wsSystemRef.current) {
      console.log('🔍 WebSocket Debug Info:');
      console.log('📋 Config:', { backendIp, path, token, autoConnect });
      console.log('🔗 Connection Info:', wsSystemRef.current.getConnectionInfo());
      console.log('🐛 Full Debug Info:', wsSystemRef.current.getFullDebugInfo());
    } else {
      console.log('❌ WebSocket System not initialized');
    }
  }, [backendIp, path, token, autoConnect]);

  // ✅ DEBUG: Globale Debug-Funktion verfügbar machen
  if (typeof window !== 'undefined') {
    (window as any).debugWebSocket = debugInfo;
  }

  return {
    logs,
    connectionInfo,
    isConnected,
    connect,
    disconnect,
    sendCommand,
    clearLogs,
    debugInfo
  };
}

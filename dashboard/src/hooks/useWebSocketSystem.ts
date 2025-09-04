import { useEffect, useRef, useState, useCallback } from 'react';
import { WebSocketSystem, WebSocketMessage, ConnectionStatus, ConnectionInfo, createWebSocketSystem } from '@/lib/websocket-system';
import { processTerminalLog } from '@/lib/ansi-utils';



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
    onMessageRef.current?.(message);
  }, []);

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
            });
          } else {}
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
  }, []);

  useEffect(() => {
    const updateInfo = () => {
      if (wsSystemRef.current) {
        const info = wsSystemRef.current.getConnectionInfo();
        setConnectionInfo(info);
      }
    };

    const timeout = setTimeout(updateInfo, 100);
    
    return () => clearTimeout(timeout);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [autoConnect, backendIp, handleConnect, handleDisconnect, handleError, handleMessage, handleStatusChange, path, token]);

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
  const processedMessagesRef = useRef<Set<string>>(new Set());
  
  const { connectionInfo, isConnected, connect, disconnect } = useWebSocketSystem({
    backendIp,
    path: '/logs',
    token,
    autoConnect,
    onMessage: (message) => {
      if ((message.type === 'log' || message.type === 'message') && typeof message.data === 'string') {
        const messageData = message.data;
        const now = Date.now();

        const messageKey = `${messageData}_${Math.floor(now / 1000)}`;

        if (processedMessagesRef.current.has(messageKey)) {
          return;
        }

        if (messageData === lastMessageRef.current && now - lastMessageTimeRef.current < 2000) {
          return;
        }

        processedMessagesRef.current.add(messageKey);
        lastMessageRef.current = messageData;
        lastMessageTimeRef.current = now;

        if (processedMessagesRef.current.size > 50) {
          processedMessagesRef.current.clear();
        }
        
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

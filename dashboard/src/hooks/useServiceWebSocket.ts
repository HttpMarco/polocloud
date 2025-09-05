import { useState, useRef, useCallback } from 'react';
import { useWebSocketSystem } from './useWebSocketSystem';
import { processTerminalLog } from '@/lib/ansi-utils';

export function useServiceWebSocket(serviceName: string, backendIp?: string, token?: string, autoConnect: boolean = true) {
  const [logs, setLogs] = useState<string[]>([]);
  const lastMessageRef = useRef<string>('');
  const lastMessageTimeRef = useRef<number>(0);
  
  const { connectionInfo, isConnected, connect, disconnect } = useWebSocketSystem({
    backendIp,
    path: `/service/${serviceName}/screen`,
    token,
    autoConnect,
    onMessage: (message) => {
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
      setLogs(prev => [...prev, `polocloud@${serviceName} » ${command}`]);
      
      const response = await fetch(`/api/services/${serviceName}/command`, {
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
  }, [serviceName]);

  const clearLogs = useCallback(() => {
    setLogs([]);
  }, []);

  return { logs, connectionInfo, isConnected, connect, disconnect, sendCommand, clearLogs };
}

import { WebSocketSystem, WebSocketMessage, ConnectionStatus } from './websocket-system';

interface GlobalWebSocketConfig {
  backendIp?: string;
  path: string;
  token?: string;
  onMessage?: (message: WebSocketMessage) => void;
  onConnect?: () => void;
  onDisconnect?: () => void;
  onError?: (error: Error) => void;
  onStatusChange?: (status: ConnectionStatus) => void;
}

class GlobalWebSocketManager {
  private static instance: GlobalWebSocketManager;
  private wsSystem: WebSocketSystem | null = null;
  private subscribers: Map<string, GlobalWebSocketConfig> = new Map();
  private connectionInfo: {
    status: ConnectionStatus;
    method: 'websocket' | 'sse' | 'polling';
    protocol: 'ws' | 'wss' | 'http' | 'https';
    reconnectAttempts: number;
    lastError?: string;
  } = {
    status: 'disconnected',
    method: 'websocket',
    protocol: 'ws',
    reconnectAttempts: 0
  };

  private constructor() {}

  static getInstance(): GlobalWebSocketManager {
    if (!GlobalWebSocketManager.instance) {
      GlobalWebSocketManager.instance = new GlobalWebSocketManager();
    }
    return GlobalWebSocketManager.instance;
  }

  subscribe(id: string, config: GlobalWebSocketConfig): () => void {
    this.subscribers.set(id, config);
    
    // Wenn bereits verbunden, sofort onConnect aufrufen
    if (this.connectionInfo.status === 'connected') {
      config.onConnect?.();
    }
    
    // Wenn noch nicht verbunden, verbinden
    if (!this.wsSystem) {
      this.connect();
    }
    
    return () => {
      this.subscribers.delete(id);
      // Wenn keine Subscriber mehr, trennen
      if (this.subscribers.size === 0) {
        this.disconnect();
      }
    };
  }

  private async connect(): Promise<void> {
    if (this.wsSystem) {
      return;
    }

    // Verwende die erste verfügbare Konfiguration
    const firstConfig = Array.from(this.subscribers.values())[0];
    if (!firstConfig) return;

    this.wsSystem = new WebSocketSystem({
      backendIp: firstConfig.backendIp,
      path: firstConfig.path,
      token: firstConfig.token,
      onMessage: (message) => {
        // Broadcast an alle Subscriber
        this.subscribers.forEach(config => {
          config.onMessage?.(message);
        });
      },
      onConnect: () => {
        this.connectionInfo.status = 'connected';
        this.subscribers.forEach(config => {
          config.onConnect?.();
        });
      },
      onDisconnect: () => {
        this.connectionInfo.status = 'disconnected';
        this.subscribers.forEach(config => {
          config.onDisconnect?.();
        });
      },
      onError: (error) => {
        this.connectionInfo.status = 'error';
        this.connectionInfo.lastError = error.message;
        this.subscribers.forEach(config => {
          config.onError?.(error);
        });
      },
      onStatusChange: (status) => {
        this.connectionInfo.status = status;
        this.subscribers.forEach(config => {
          config.onStatusChange?.(status);
        });
      }
    });

    try {
      await this.wsSystem.connect();
    } catch (error) {
      console.error('Global WebSocket connection failed:', error);
    }
  }

  private disconnect(): void {
    if (this.wsSystem) {
      this.wsSystem.disconnect();
      this.wsSystem = null;
    }
    this.connectionInfo.status = 'disconnected';
  }

  getConnectionInfo() {
    return { ...this.connectionInfo };
  }

  isConnected(): boolean {
    return this.connectionInfo.status === 'connected';
  }
}

export const globalWebSocketManager = GlobalWebSocketManager.getInstance();

export function useGlobalWebSocket(config: GlobalWebSocketConfig) {
  const id = Math.random().toString(36).substr(2, 9);
  
  return {
    subscribe: (callback: () => void) => {
      const unsubscribe = globalWebSocketManager.subscribe(id, config);
      callback();
      return unsubscribe;
    },
    getConnectionInfo: () => globalWebSocketManager.getConnectionInfo(),
    isConnected: () => globalWebSocketManager.isConnected()
  };
}

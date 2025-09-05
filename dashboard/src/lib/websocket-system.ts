import { logError } from './error-handling';

export interface WebSocketMessage {
  type: 'log' | 'command' | 'status' | 'error' | 'heartbeat';
  data: string | object | number | boolean | null;
  timestamp?: number;
  service?: string;
}

export interface WebSocketSystemConfig {
  backendIp?: string;
  path: string;
  token?: string;
  onMessage?: (message: WebSocketMessage) => void;
  onConnect?: () => void;
  onDisconnect?: () => void;
  onError?: (error: Error) => void;
  onStatusChange?: (status: ConnectionStatus) => void;
}

export type ConnectionStatus = 'disconnected' | 'connecting' | 'connected' | 'reconnecting' | 'error';

export interface ConnectionInfo {
  status: ConnectionStatus;
  method: 'websocket' | 'sse' | 'polling';
  protocol: 'ws' | 'wss' | 'http' | 'https';
  reconnectAttempts: number;
  lastError?: string;
}

export class WebSocketSystem {
  private config: WebSocketSystemConfig;
  private ws: WebSocket | null = null;
  private eventSource: EventSource | null = null;
  private pollingInterval: NodeJS.Timeout | null = null;

  private status: ConnectionStatus = 'disconnected';
  private method: 'websocket' | 'sse' | 'polling' = 'websocket';
  private protocol: 'ws' | 'wss' | 'http' | 'https' = 'ws';

  private reconnectAttempts = 0;
  private maxReconnectAttempts = 5;
  private reconnectDelay = 3000;
  private reconnectTimeout: NodeJS.Timeout | null = null;

  private heartbeatInterval: NodeJS.Timeout | null = null;
  private lastHeartbeat: number = 0;

  private lastMessageKey = '';
  private lastMessageTime = 0;
  
  constructor(config: WebSocketSystemConfig) {
    this.config = config;
  }

  private async getBackendIpAndToken(): Promise<{ backendIp: string; token: string } | null> {
    let backendIp = this.config.backendIp;
    let token = this.config.token;

    if (typeof window !== 'undefined') {
      if (!backendIp) {
        const storedBackendIp = localStorage.getItem('backendIp');
        if (storedBackendIp) {
          backendIp = decodeURIComponent(storedBackendIp);
        }
      }

      if (!token) {
        try {
          const response = await fetch('/api/auth/token');
          if (response.ok) {
            const data = await response.json();
            if (data.success && data.token) {
              token = data.token;
            } else {}
          } else {}
        } catch (error) {
          logError(error, { 
            component: 'WebSocketSystem', 
            action: 'connectionAttempt' 
          });
        }
      }
    }


    if (!backendIp || !token) {
      return null;
    }

    // Don't automatically convert to HTTPS here - let the connection methods handle it
    // This allows us to test HTTPS support first before forcing it
    return { backendIp, token };
  }

  public async connect(): Promise<void> {
    if (this.status === 'connecting' || this.status === 'connected') {
      return;
    }

    this.disconnect();

    const credentials = await this.getBackendIpAndToken();
    if (!credentials) {
      const error = new Error('BackendIP or Token not available');
      this.handleError(error);
      throw error;
    }

    this.updateStatus('connecting');
    
    // Check if we should skip direct WebSocket due to protocol mismatch
    const isFrontendHttps = typeof window !== 'undefined' && window.location.protocol === 'https:';
    const isBackendHttp = credentials.backendIp.startsWith('http://') || 
                         (!credentials.backendIp.startsWith('https://') && !credentials.backendIp.includes('localhost') && !credentials.backendIp.includes('127.0.0.1'));
    
    if (isFrontendHttps && isBackendHttp) {
      // Skip direct WebSocket and use proxy methods directly
      try {
        await this.tryProxyWebSocket();
      } catch (error) {
        logError(error, { 
          component: 'WebSocketSystem', 
          action: 'tryProxyWebSocket' 
        });
        this.startPolling();
      }
    } else {
      // Try direct WebSocket first
      try {
        await this.tryDirectWebSocket();
      } catch (error) {
        logError(error, { 
          component: 'WebSocketSystem', 
          action: 'tryDirectWebSocket' 
        });
        try {
          await this.tryProxyWebSocket();
        } catch (error) {
          logError(error, { 
            component: 'WebSocketSystem', 
            action: 'tryProxyWebSocket' 
          });
                  this.startPolling();
        }
      }
    }
  }

  private async tryDirectWebSocket(): Promise<void> {
    return new Promise(async (resolve, reject) => {
      try {
        const credentials = await this.getBackendIpAndToken();
        if (!credentials) {
          reject(new Error('BackendIP or Token not available'));
          return;
        }

        const { backendIp, token } = credentials;
        const protocol = this.determineWebSocketProtocol(backendIp);
        
        // Ensure proper URL construction for WebSocket
        let wsUrl: string;
        if (backendIp.startsWith('http://') || backendIp.startsWith('https://')) {
          // Backend IP already includes protocol
          const baseUrl = backendIp.replace(/^https?:\/\//, '');
          wsUrl = `${protocol}://${baseUrl}/polocloud/api/v3${this.config.path}?token=${token}`;
        } else {
          // Backend IP is just IP:port
          wsUrl = `${protocol}://${backendIp}/polocloud/api/v3${this.config.path}?token=${token}`;
        }

        this.ws = new WebSocket(wsUrl);
        this.protocol = protocol;
        this.method = 'websocket';
        
        const timeout = setTimeout(() => {
          this.ws?.close();
          reject(new Error('WebSocket connection timeout'));
        }, 5000);
        
        this.ws.onopen = () => {
          clearTimeout(timeout);
          this.updateStatus('connected');
          this.reconnectAttempts = 0;
          this.startHeartbeat();
          this.config.onConnect?.();
          resolve();
        };
        
        this.ws.onmessage = (event) => {
          this.handleMessage(event.data);
        };
        
        this.ws.onclose = (event) => {
          clearTimeout(timeout);
          this.handleDisconnect();
          
          if (event.code !== 1000) {
            this.scheduleReconnect();
          }
        };
        
        this.ws.onerror = (error) => {
          clearTimeout(timeout);
          this.handleError(new Error('WebSocket error'));
          reject(error);
        };
        
      } catch (error) {
        reject(error);
      }
    });
  }

  private async tryProxyWebSocket(): Promise<void> {
    return new Promise(async (resolve, reject) => {
      try {
        const credentials = await this.getBackendIpAndToken();
        if (!credentials) {
          reject(new Error('BackendIP or Token not available'));
          return;
        }

        const { backendIp } = credentials;
        const sseUrl = `/api/websocket/stream?backendIp=${encodeURIComponent(backendIp)}&path=${encodeURIComponent(this.config.path)}`;
        
        this.eventSource = new EventSource(sseUrl);
        this.method = 'sse';
        this.protocol = 'https';
        
        const timeout = setTimeout(() => {
          this.eventSource?.close();
          reject(new Error('SSE connection timeout'));
        }, 5000);
        
        this.eventSource.onopen = () => {
          clearTimeout(timeout);
          this.updateStatus('connected');
          this.reconnectAttempts = 0;
          this.config.onConnect?.();
          resolve();
        };
        
        this.eventSource.onmessage = (event) => {
          try {
            const data = JSON.parse(event.data);
            this.handleMessage(data);
          } catch (error) {
            logError(error, { 
              component: 'WebSocketSystem', 
              action: 'handleWebSocketMessage' 
            });
          }
        };
        
        this.eventSource.onerror = () => {
          clearTimeout(timeout);
          this.eventSource?.close();
          this.eventSource = null;
          reject(new Error('SSE connection error'));
        };
        
      } catch (error) {
        reject(error);
      }
    });
  }


  private startPolling(): void {
    this.method = 'polling';
    this.protocol = 'https';
    this.updateStatus('connected');
    this.reconnectAttempts = 0;
    this.config.onConnect?.();
    
    this.pollingInterval = setInterval(async () => {
      try {
        const credentials = await this.getBackendIpAndToken();
        if (!credentials) {
          throw new Error('BackendIP or Token not available');
        }

        const { backendIp } = credentials;

        const response = await fetch('/api/websocket/poll', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            backendIp: backendIp,
            path: this.config.path
          })
        });
        
        if (response.ok) {
          const data = await response.json();
          if (data.messages && data.messages.length > 0) {
            data.messages.forEach((message: WebSocketMessage) => {
              this.handleMessage(message);
            });
          }
        } else {
          throw new Error(`Polling failed: ${response.status}`);
        }
        
      } catch (error) {
        this.handleError(error as Error);
      }
    }, 2000);
  }



  private startHeartbeat(): void {
    this.heartbeatInterval = setInterval(() => {
      if (this.ws && this.ws.readyState === WebSocket.OPEN) {
        this.ws.send(JSON.stringify({ type: 'heartbeat', timestamp: Date.now() }));
        this.lastHeartbeat = Date.now();
      }
    }, 60000);
  }

  private stopHeartbeat(): void {
    if (this.heartbeatInterval) {
      clearInterval(this.heartbeatInterval);
      this.heartbeatInterval = null;
    }
  }

  private scheduleReconnect(): void {
    if (this.status === 'connecting' || this.status === 'connected') {
      return;
    }
    
    if (this.reconnectAttempts >= this.maxReconnectAttempts) {
      this.updateStatus('error');
      this.config.onError?.(new Error('Max reconnect attempts reached'));
      return;
    }
    
    this.updateStatus('reconnecting');
    this.reconnectAttempts++;
    
    const delay = Math.min(this.reconnectDelay + (this.reconnectAttempts * 2000), 15000);
    
    this.reconnectTimeout = setTimeout(() => {
      this.connect().catch((error) => {
        logError(error, { 
          component: 'WebSocketSystem', 
          action: 'reconnect' 
        });
        // If it's a protocol mismatch error, don't try to reconnect
        if (error.message.includes('HTTPS frontend with HTTP backend')) {
          this.updateStatus('error');
          this.config.onError?.(new Error('Protocol mismatch: HTTPS frontend cannot connect to HTTP backend'));
          return;
        }
      });
    }, delay);
  }

  public async sendMessage(message: WebSocketMessage): Promise<void> {
    if (this.method === 'websocket' && this.ws?.readyState === WebSocket.OPEN) {
      this.ws.send(JSON.stringify(message));
    } else {
      const credentials = await this.getBackendIpAndToken();
      if (!credentials) {
        this.config.onError?.(new Error('Cannot send message: BackendIP or Token not available'));
        return;
      }

      const { backendIp } = credentials;
      
      fetch('/api/websocket/send', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          backendIp: backendIp,
          path: this.config.path,
          message
        })
      }).catch(error => {
        this.config.onError?.(error);
      });
    }
  }

  public disconnect(): void {
    this.stopHeartbeat();
    
    if (this.reconnectTimeout) {
      clearTimeout(this.reconnectTimeout);
      this.reconnectTimeout = null;
    }
    
    if (this.pollingInterval) {
      clearInterval(this.pollingInterval);
      this.pollingInterval = null;
    }
    
    if (this.ws) {
      this.ws.close(1000);
      this.ws = null;
    }
    
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
    }

    this.reconnectAttempts = 0;
    this.updateStatus('disconnected');
  }

  public getConnectionInfo(): ConnectionInfo {
    return {
      status: this.status,
      method: this.method,
      protocol: this.protocol,
      reconnectAttempts: this.reconnectAttempts,
      lastError: undefined
    };
  }

  public isConnected(): boolean {
    return this.status === 'connected';
  }

  private determineWebSocketProtocol(backendIp: string): 'ws' | 'wss' {
    // Check if frontend is running on HTTPS
    const isFrontendHttps = typeof window !== 'undefined' && window.location.protocol === 'https:';
    
    // If frontend is HTTPS, we MUST use WSS for security reasons
    if (isFrontendHttps) {
      return 'wss';
    }
    
    // For HTTP frontend, check backend configuration
    const isLocalBackend = backendIp.includes('localhost') ||
                          backendIp.includes('127.0.0.1') || 
                          backendIp.startsWith('192.168.') ||
                          backendIp.startsWith('10.');

    if (isLocalBackend) {
      return 'ws';
    }

    const isHttpsBackend = backendIp.includes('https') || 
                          backendIp.includes(':443') ||
                          (!backendIp.includes(':') && !backendIp.includes('http'));
    return isHttpsBackend ? 'wss' : 'ws';
  }


  private handleMessage(data: string | WebSocketMessage): void {
    try {
      
      let message: WebSocketMessage;
      
      if (typeof data === 'string') {
        try {
          message = JSON.parse(data);
        } catch (error) {
          logError(error, { 
            component: 'WebSocketSystem', 
            action: 'parseMessage' 
          });
          message = {
            type: 'log',
            data: data,
            timestamp: Date.now()
          };
        }
      } else {
        message = data;
      }

      if (message.type === 'heartbeat') {
        this.lastHeartbeat = Date.now();
        return;
      }

      if ((message as unknown as { type: string }).type === 'connected' || (message as unknown as { type: string }).type === 'disconnected') {
        this.config.onMessage?.(message);
        return;
      }

      if (message.type === 'log' || (message as unknown as { type: string }).type === 'message') {

        this.config.onMessage?.(message);
        return;
      }

      this.config.onMessage?.(message);
      
    } catch (error) {
      logError(error, { 
        component: 'WebSocketSystem', 
        action: 'handleMessage' 
      });
    }
  }

  private handleDisconnect(): void {
    this.stopHeartbeat();
    this.updateStatus('disconnected');
    this.config.onDisconnect?.();
  }

  private handleError(error: Error): void {
    this.updateStatus('error');
    this.config.onError?.(error);
  }

  private updateStatus(newStatus: ConnectionStatus): void {
    if (this.status !== newStatus) {
      this.status = newStatus;
      this.config.onStatusChange?.(newStatus);
    }
  }
}

export function createWebSocketSystem(config: WebSocketSystemConfig): WebSocketSystem {
  return new WebSocketSystem(config);
}
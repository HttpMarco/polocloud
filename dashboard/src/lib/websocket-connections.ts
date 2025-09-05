interface WebSocketMessage {
  type: 'message' | 'disconnect';
  data?: string;
  code?: number;
  timestamp: number;
}

export const wsConnections = new Map<string, WebSocket>();
export const messageQueues = new Map<string, WebSocketMessage[]>();

export function cleanupConnection(connectionKey: string) {
  const ws = wsConnections.get(connectionKey);
  if (ws) {
    try {
      ws.close();
    } catch (error) {
      console.error('Error closing WebSocket connection:', error);
    }
  }
  wsConnections.delete(connectionKey);
  messageQueues.delete(connectionKey);
}

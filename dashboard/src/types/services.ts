export interface Service {
  name: string;
  state: string;
  type: string;
  groupName: string;
  hostname: string;
  port: number;
  templates: string[];
  information: {
    createdAt: number;
  };
  properties: Record<string, string>;
  minMemory: number;
  maxMemory: number;
  playerCount: number;
  maxPlayerCount: number;
  memoryUsage: number;
  cpuUsage: number;
  motd: string;
}

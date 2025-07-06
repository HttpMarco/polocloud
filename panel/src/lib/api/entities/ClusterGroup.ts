import { Platform } from '@/lib/api/entities/Platform';

export class ClusterGroup {
  name: string = '';
  platform?: Platform;
  maxMemory: number = 0;
  maxPlayers: number = 0;
  fallback: boolean = false;
  staticService: boolean = false;
  minOnlineServerInstances: number = 0;
  maxOnlineServerInstances: number = 0;

  constructor(data: Partial<ClusterGroup>) {
    Object.assign(this, data);
  }
}

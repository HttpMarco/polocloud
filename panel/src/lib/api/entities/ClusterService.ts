import { ClusterGroup } from '@/lib/api/entities/ClusterGroup';
import { ClusterPlayer } from '@/lib/api/entities/ClusterPlayer';

export class ClusterService {
  name: string = '';
  group?: ClusterGroup;
  orderedId: number = 0;
  id: string = '';
  hostname: string = '';
  port: number = 0;
  maxPlayers: number = 0;
  runningNode: string = '';
  state: ServiceState = ServiceState.PREPARED;
  logs: string[] = [];
  currentMemory: number = 0;
  details: string = '';
  onlinePlayers: ClusterPlayer[] = [];

  constructor(data: Partial<ClusterService>) {
    Object.assign(this, data);
  }
}

export enum ServiceState {
  PREPARED = 'PREPARED',
  STARTING = 'STARTING',
  ONLINE = 'ONLINE',
  STOPPING = 'STOPPING',
}

const SPECIFIC_SERVICE_STATE_COLOR_MAP: Record<ServiceState, string> = {
  ONLINE: 'green',
  PREPARED: 'gray',
  STARTING: 'yellow',
  STOPPING: 'red',
};

export const getServiceStatusColor = (status?: ServiceState) => {
  if (!status) {
    return SPECIFIC_SERVICE_STATE_COLOR_MAP.PREPARED;
  }
  const color = SPECIFIC_SERVICE_STATE_COLOR_MAP[status];
  if (color) {
    return color;
  }

  return SPECIFIC_SERVICE_STATE_COLOR_MAP.PREPARED;
};

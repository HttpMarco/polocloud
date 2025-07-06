export class ClusterPlayer {
  uniqueId: string = '';
  name: string = '';
  currentProxyName: string = '';
  currentServerName: string = '';
  details: string = '';

  constructor(data: Partial<ClusterPlayer>) {
    Object.assign(this, data);
  }
}

export class Instance {
  id: string = '';
  name: string = '';
  hostname: string = '';
  secret: string = '';
  state: 'ONLINE' | 'MAINTENANCE' | 'OFFLINE' = 'ONLINE';
  description: string = '';

  constructor(data: Partial<Instance>) {
    Object.assign(this, data);
  }
}

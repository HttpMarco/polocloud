export class NodeEndpoint {
  data?: NodeData;

  situation: 'INITIALIZE' | 'SYNC' | 'RECHABLE' | 'STOPPING' | 'STOPPED' =
    'INITIALIZE';
  constructor(data: Partial<NodeEndpoint>) {
    Object.assign(this, data);
  }
}

interface NodeData {
  name: string;
  hostName: string;
  port: number;
  details: string;
}

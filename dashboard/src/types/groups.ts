
export interface PlatformModel {
  name: string;
  version: string;
}

export interface TemplateModel {
  name: string;
  size: number;
}

export interface GroupInformationModel {
  createdAt: number;
}
export interface Group {
  name: string;
  minMemory: number;
  maxMemory: number;
  minOnlineService: number;
  maxOnlineService: number;
  platform: PlatformModel;
  percentageToStartNewService: number;
  createdAt?: number;
  templates?: TemplateModel[];
  groupProperties?: Record<string, unknown>;
}



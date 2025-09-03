export interface Player {
  name: string;
  uuid: string;
  currentServiceName: string;
}

export interface PlayerListResponse {
  page: number;
  size: number;
  total: number;
  totalPages: number;
  data: Player[];
}

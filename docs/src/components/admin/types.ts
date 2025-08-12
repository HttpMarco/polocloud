export type MeResp = {
  authenticated: boolean;
  user?: {
    id?: string;
    username: string;
    name?: string;
    avatar?: string;
    isGitHubAdmin?: boolean;
    loginTime?: string;
  }
};

export type Feedback = {
  id: string;
  userId: string;
  username: string;
  rating: number;
  description: string;
  createdAt: string;
  avatar: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  approvedBy?: string;
  approvedAt?: string;
  rejectedBy?: string;
  rejectedAt?: string;
};

export type AdminUser = {
  username: string;
  role: 'ADMIN' | 'SUPER_ADMIN';
  addedBy: string;
  addedAt: string;
  avatar?: string;
  name?: string;
};

export type Partner = {
  id: string;
  name: string;
  logo: string;
  website: string;
  description: string;
  createdAt: string;
};

export type NewPartner = {
  name: string;
  logo: string;
  website: string;
  description: string;
};

export type ActiveTab = 'feedback' | 'users' | 'partners' | 'blog';

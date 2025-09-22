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
  addedAt: string;
};

export type NewPartner = {
  name: string;
  logo: string;
  logoFile?: File;
  website: string;
  description: string;
};

export type EditPartner = {
  id: string;
  name: string;
  logo: string;
  logoFile?: File;
  website: string;
  description: string;
};

export type Platform = {
  id: string;
  name: string;
  icon: string;
  versions: {
    [key: string]: 'supported' | 'not-supported' | 'partial' | 'not-possible';
  };
  addons: {
    [key: string]: 'supported' | 'not-supported' | 'partial' | 'not-possible';
  };
  addedAt: string;
  addedBy: string;
};

export type NewPlatform = {
  name: string;
  icon: string;
  iconFile?: File;
  versions: {
    [key: string]: 'supported' | 'not-supported' | 'partial' | 'not-possible';
  };
  addons: {
    [key: string]: 'supported' | 'not-supported' | 'partial' | 'not-possible';
  };
};

export type EditPlatform = {
  id: string;
  name: string;
  icon: string;
  iconFile?: File;
  versions: {
    [key: string]: 'supported' | 'not-supported' | 'partial' | 'not-possible';
  };
  addons: {
    [key: string]: 'supported' | 'not-supported' | 'partial' | 'not-possible';
  };
};

export type ActiveTab = 'feedback' | 'users' | 'partners' | 'platforms' | 'changelog' | 'blog' | 'images';

export type ChangelogEntry = {
  slug: string;
  version: string;
  title: string;
  description: string;
  type: 'major' | 'minor' | 'patch' | 'hotfix';
  releaseDate: string;
  author: string;
  content: string;
};

export type NewChangelogEntry = {
  version: string;
  title: string;
  description: string;
  type: 'major' | 'minor' | 'patch' | 'hotfix';
  releaseDate: string;
  content: string;
};

export type EditChangelogEntry = {
  slug: string;
  version: string;
  title: string;
  description: string;
  type: 'major' | 'minor' | 'patch' | 'hotfix';
  releaseDate: string;
  content: string;
};

export type ImageFile = {
  id: string;
  name: string;
  url: string;
  blobId: string;
  size: number;
  type: string;
  uploadedAt: string;
  uploadedBy: string;
  category?: 'general' | 'partners' | 'platforms' | 'blog' | 'changelog';
};

export type NewImageFile = {
  file: File | null;
  category?: 'general' | 'partners' | 'platforms' | 'blog' | 'changelog';
};

export interface GitHubStats {
  stars: number;
  forks: number;
  releases: number;
  downloads: number;
  lastUpdated: string;
}

export interface GitHubRelease {
  id: number;
  name: string;
  tag_name: string;
  assets: Array<{
    id: number;
    name: string;
    download_count: number;
  }>;
  created_at: string;
}

const GITHUB_API_BASE = 'https://api.github.com';
const REPO_OWNER = 'HttpMarco';
const REPO_NAME = 'polocloud';
const GITHUB_TOKEN = process.env.GITHUB_TOKEN;

let serverCache: GitHubStats | null = null;
let serverCacheTimestamp: number = 0;
const SERVER_CACHE_DURATION = 10 * 60 * 1000;

let lastRequestTime: number = 0;
const MIN_REQUEST_INTERVAL = 1000;

async function makeGitHubRequest(url: string): Promise<Response> {
  const now = Date.now();
  const timeSinceLastRequest = now - lastRequestTime;
  
  if (timeSinceLastRequest < MIN_REQUEST_INTERVAL) {
    await new Promise(resolve => setTimeout(resolve, MIN_REQUEST_INTERVAL - timeSinceLastRequest));
  }
  
  lastRequestTime = Date.now();
  
  const headers: HeadersInit = {
    'Accept': 'application/vnd.github.v3+json',
    'User-Agent': 'PoloCloud-Docs'
  };
  
  if (GITHUB_TOKEN) {
    headers['Authorization'] = `token ${GITHUB_TOKEN}`;
  }
  
  const response = await fetch(url, { headers });
  
  if (response.status === 403) {
    throw new Error('GitHub API rate limit exceeded');
  }
  
  if (!response.ok) {
    throw new Error(`GitHub API error: ${response.status} ${response.statusText}`);
  }
  
  return response;
}

export async function fetchGitHubStats(): Promise<GitHubStats> {
  try {
    const now = Date.now();
    if (serverCache && (now - serverCacheTimestamp) < SERVER_CACHE_DURATION) {
      return serverCache;
    }

    const repoResponse = await makeGitHubRequest(`${GITHUB_API_BASE}/repos/${REPO_OWNER}/${REPO_NAME}`);
    const repoData = await repoResponse.json();

    const releasesResponse = await makeGitHubRequest(`${GITHUB_API_BASE}/repos/${REPO_OWNER}/${REPO_NAME}/releases`);
    const releasesData: GitHubRelease[] = await releasesResponse.json();

    const totalDownloads = releasesData.reduce((total, release) => {
      return total + release.assets.reduce((assetTotal, asset) => {
        return assetTotal + asset.download_count;
      }, 0);
    }, 0);

    const stats: GitHubStats = {
      stars: repoData.stargazers_count || 0,
      forks: repoData.forks_count || 0,
      releases: releasesData.length || 0,
      downloads: totalDownloads,
      lastUpdated: new Date().toISOString(),
    };


    serverCache = stats;
    serverCacheTimestamp = now;

    return stats;
  } catch (error) {
    console.error('Error fetching GitHub stats:', error);

    if (serverCache) {
      console.log('Returning cached GitHub stats due to API error');
      return serverCache;
    }

    return {
      stars: 61,
      forks: 22,
      releases: 4,
      downloads: 0,
      lastUpdated: new Date().toISOString(),
    };
  }
}

let clientCache: GitHubStats | null = null;
let clientCacheTimestamp: number = 0;
const CLIENT_CACHE_DURATION = 5 * 60 * 1000;

export async function getCachedGitHubStats(): Promise<GitHubStats> {
  const now = Date.now();

  if (clientCache && (now - clientCacheTimestamp) < CLIENT_CACHE_DURATION) {
    return clientCache;
  }

  const stats = await fetchGitHubStats();

  clientCache = stats;
  clientCacheTimestamp = now;
  
  return stats;
}

export function clearGitHubCache(): void {
  serverCache = null;
  serverCacheTimestamp = 0;
  clientCache = null;
  clientCacheTimestamp = 0;
}

export function getCacheStatus(): {
  serverCacheAge: number;
  clientCacheAge: number;
  hasServerCache: boolean;
  hasClientCache: boolean;
} {
  const now = Date.now();
  return {
    serverCacheAge: serverCache ? now - serverCacheTimestamp : 0,
    clientCacheAge: clientCache ? now - clientCacheTimestamp : 0,
    hasServerCache: !!serverCache,
    hasClientCache: !!clientCache,
  };
} 
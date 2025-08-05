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

export async function fetchGitHubStats(): Promise<GitHubStats> {
    try {
        const repoResponse = await fetch(`${GITHUB_API_BASE}/repos/${REPO_OWNER}/${REPO_NAME}`);
        const repoData = await repoResponse.json();

        const releasesResponse = await fetch(`${GITHUB_API_BASE}/repos/${REPO_OWNER}/${REPO_NAME}/releases`);
        const releasesData: GitHubRelease[] = await releasesResponse.json();

        const totalDownloads = releasesData.reduce((total, release) => {
            return total + release.assets.reduce((assetTotal, asset) => {
                return assetTotal + asset.download_count;
            }, 0);
        }, 0);

        return {
            stars: repoData.stargazers_count || 0,
            forks: repoData.forks_count || 0,
            releases: releasesData.length || 0,
            downloads: totalDownloads,
            lastUpdated: new Date().toISOString(),
        };
    } catch (error) {
        console.error('Error fetching GitHub stats:', error);
        return {
            stars: 61,
            forks: 22,
            releases: 4,
            downloads: 0,
            lastUpdated: new Date().toISOString(),
        };
    }
}

let cachedStats: GitHubStats | null = null;
let cacheTimestamp: number = 0;
const CACHE_DURATION = 5 * 60 * 1000;

export async function getCachedGitHubStats(): Promise<GitHubStats> {
    const now = Date.now();

    if (cachedStats && (now - cacheTimestamp) < CACHE_DURATION) {
        return cachedStats;
    }

    const stats = await fetchGitHubStats();
    cachedStats = stats;
    cacheTimestamp = now;

    return stats;
}
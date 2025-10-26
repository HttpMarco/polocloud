export interface GitHubStats {
    stars: number;
    forks: number;
    releases: number;
    downloads: number;
    commits: number;
    lastUpdated: string;
}

export interface GitHubContributor {
    login: string;
    id: number;
    avatar_url: string;
    contributions: number;
    commits: number;
    html_url: string;
    type: string;
}

export interface GitHubRelease {
    id: number;
    tag_name: string;
    name: string;
    published_at: string;
    html_url: string;
    draft: boolean;
    prerelease: boolean;
    assets: Array<{
        name: string;
        size: number;
        download_count: number;
        browser_download_url: string;
    }>;
}

const CACHE_STRATEGIES = {
    stats: { 
        duration: 10 * 60 * 1000,
        refreshThreshold: 0.8
    },
    contributors: { 
        duration: 60 * 60 * 1000,
        refreshThreshold: 0.9
    },
    partners: { 
        duration: 30 * 60 * 1000,
        refreshThreshold: 0.7
    },
    platforms: { 
        duration: 30 * 60 * 1000,
        refreshThreshold: 0.7
    }
};


const REFRESH_OFFSETS = {
    stats: 0,
    contributors: 30000,
    partners: 60000,
    platforms: 90000
};

const GITHUB_API_BASE = 'https://api.github.com';
const REPO_OWNER = 'HttpMarco';
const REPO_NAME = 'polocloud';
const GITHUB_TOKEN = process.env.GITHUB_TOKEN;

let serverCache: GitHubStats | null = null;
let serverCacheTimestamp: number = 0;
const SERVER_CACHE_DURATION = 10 * 60 * 1000;

let lastRequestTime: number = 0;
const MIN_REQUEST_INTERVAL = 1000;

function shouldRefreshCache(cacheType: keyof typeof CACHE_STRATEGIES, timestamp: number): boolean {
    const now = Date.now();
    const strategy = CACHE_STRATEGIES[cacheType];
    const cacheAge = now - timestamp;
    const refreshThreshold = strategy.duration * strategy.refreshThreshold;
    
    return cacheAge > refreshThreshold;
}

function getStaggeredRefreshTime(cacheType: keyof typeof REFRESH_OFFSETS): number {
    return Date.now() + REFRESH_OFFSETS[cacheType];
}

function isValidCache<T>(cache: T | null, timestamp: number, maxAge: number): boolean {
    if (!cache || !timestamp) return false;
    
    const now = Date.now();
    const cacheAge = now - timestamp;
    
    return cacheAge < maxAge;
}

async function refreshStatsInBackground(): Promise<void> {
    try {

        setTimeout(async () => {
            try {
                await fetchGitHubStatsInternal();
            } catch (error) {
                console.log('Background refresh failed, using existing cache');
            }
        }, 0);
    } catch (error) {

        console.log('Background refresh setup failed');
    }
}

async function fetchGitHubStatsInternal(): Promise<GitHubStats> {
    const repoResponse = await makeGitHubRequest(`${GITHUB_API_BASE}/repos/${REPO_OWNER}/${REPO_NAME}`);
    const repoData = await repoResponse.json();

    const releasesResponse = await makeGitHubRequest(`${GITHUB_API_BASE}/repos/${REPO_OWNER}/${REPO_NAME}/releases`);
    const releasesData: GitHubRelease[] = await releasesResponse.json();

    const commitsResponse = await makeGitHubRequest(`${GITHUB_API_BASE}/repos/${REPO_OWNER}/${REPO_NAME}/commits?per_page=1`);
    const linkHeader = commitsResponse.headers.get('link');
    let totalCommits = 0;

    if (linkHeader) {
        const lastLinkMatch = linkHeader.match(/<[^>]*page=(\d+)[^>]*>;\s*rel="last"/);
        if (lastLinkMatch) {
            totalCommits = parseInt(lastLinkMatch[1]);
        }
    }

    if (totalCommits === 0) {
        const commitsData = await commitsResponse.json();
        totalCommits = commitsData.length;
    }

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
        commits: totalCommits,
        lastUpdated: new Date().toISOString(),
    };

    serverCache = stats;
    serverCacheTimestamp = Date.now();

    return stats;
}

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
        

        if (serverCache && isValidCache(serverCache, serverCacheTimestamp, SERVER_CACHE_DURATION)) {

            if (shouldRefreshCache('stats', serverCacheTimestamp)) {

                refreshStatsInBackground();
            }
            return serverCache;
        }

        return fetchGitHubStatsInternal();
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
            commits: 3206,
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

let contributorsCache: GitHubContributor[] | null = null;
let contributorsCacheTimestamp: number = 0;
const CONTRIBUTORS_CACHE_DURATION = 60 * 60 * 1000;

export async function fetchGitHubContributors(): Promise<GitHubContributor[]> {
    try {
        const response = await makeGitHubRequest(`${GITHUB_API_BASE}/repos/${REPO_OWNER}/${REPO_NAME}/contributors?per_page=100`);
        const contributors: GitHubContributor[] = await response.json();

        const filteredContributors = contributors
            .filter(contributor => contributor.type === 'User')
            .sort((a, b) => b.contributions - a.contributions);

        const contributorsWithCommits = await Promise.all(
            filteredContributors.map(async (contributor) => {
                try {
                    const commitsResponse = await makeGitHubRequest(
                        `${GITHUB_API_BASE}/repos/${REPO_OWNER}/${REPO_NAME}/commits?author=${contributor.login}&per_page=1`
                    );

                    const linkHeader = commitsResponse.headers.get('link');
                    let commitCount = 0;

                    if (linkHeader) {
                        const lastLinkMatch = linkHeader.match(/<[^>]*page=(\d+)[^>]*>;\s*rel="last"/);
                        if (lastLinkMatch) {
                            commitCount = parseInt(lastLinkMatch[1]);
                        }
                    }

                    if (commitCount === 0) {
                        const commitsData = await commitsResponse.json();
                        commitCount = commitsData.length;
                    }

                    return {
                        ...contributor,
                        commits: commitCount
                    };
                } catch (error) {
                    console.error(`Error fetching commits for ${contributor.login}:`, error);
                    return {
                        ...contributor,
                        commits: contributor.contributions
                    };
                }
            })
        );

        return contributorsWithCommits;
    } catch (error) {
        console.error('Error fetching GitHub contributors:', error);

        if (contributorsCache) {
            console.log('Returning cached GitHub contributors due to API error');
            return contributorsCache;
        }

        return [
            {
                login: 'HttpMarco',
                id: 1,
                avatar_url: 'https://github.com/HttpMarco.png',
                contributions: 156,
                commits: 156,
                html_url: 'https://github.com/HttpMarco',
                type: 'User'
            }
        ];
    }
}

export async function getCachedGitHubContributors(): Promise<GitHubContributor[]> {
    const now = Date.now();

    if (contributorsCache && (now - contributorsCacheTimestamp) < CONTRIBUTORS_CACHE_DURATION) {
        return contributorsCache;
    }

    const contributors = await fetchGitHubContributors();

    contributorsCache = contributors;
    contributorsCacheTimestamp = now;

    return contributors;
}

export function clearGitHubCache(): void {
    serverCache = null;
    serverCacheTimestamp = 0;
    clientCache = null;
    clientCacheTimestamp = 0;
    contributorsCache = null;
    contributorsCacheTimestamp = 0;
    
    console.log('All GitHub caches cleared');
}

export function clearSpecificCache(cacheType: 'stats' | 'contributors' | 'partners' | 'platforms'): void {
    switch (cacheType) {
        case 'stats':
            serverCache = null;
            serverCacheTimestamp = 0;
            clientCache = null;
            clientCacheTimestamp = 0;
            break;
        case 'contributors':
            contributorsCache = null;
            contributorsCacheTimestamp = 0;
            break;
        default:
            console.log(`Cache type '${cacheType}' not implemented yet`);
    }
    
    console.log(`${cacheType} cache cleared`);
}

export function getCacheStatus(): {
    serverCacheAge: number;
    clientCacheAge: number;
    contributorsCacheAge: number;
    hasServerCache: boolean;
    hasClientCache: boolean;
    hasContributorsCache: boolean;
    cacheHealth: 'healthy' | 'warning' | 'expired';
    nextRefreshIn: number;
} {
    const now = Date.now();
    const serverAge = serverCache ? now - serverCacheTimestamp : 0;
    const clientAge = clientCache ? now - clientCacheTimestamp : 0;
    const contributorsAge = contributorsCache ? now - contributorsCacheTimestamp : 0;

    let cacheHealth: 'healthy' | 'warning' | 'expired' = 'healthy';
    if (serverAge > SERVER_CACHE_DURATION || clientAge > CACHE_STRATEGIES.stats.duration) {
        cacheHealth = 'expired';
    } else if (shouldRefreshCache('stats', serverCacheTimestamp)) {
        cacheHealth = 'warning';
    }

    const nextRefreshIn = Math.max(
        SERVER_CACHE_DURATION - serverAge,
        CACHE_STRATEGIES.stats.duration - clientAge,
        0
    );
    
    return {
        serverCacheAge: serverAge,
        clientCacheAge: clientAge,
        contributorsCacheAge: contributorsAge,
        hasServerCache: !!serverCache,
        hasClientCache: !!clientCache,
        hasContributorsCache: !!contributorsCache,
        cacheHealth,
        nextRefreshIn
    };
}



import { Octokit } from '@octokit/rest';
import matter from 'gray-matter';

export const blogOctokit = new Octokit({
  auth: GITHUB_TOKEN || undefined,
});

if (!GITHUB_TOKEN) {
  console.warn('GITHUB_TOKEN is not set. GitHub operations will fail.');
} else {
  console.log('GITHUB_TOKEN is available, length:', GITHUB_TOKEN.length);
}

console.log('GitHub Config:', {
  owner: process.env.GITHUB_REPO_OWNER || 'HttpMarco',
  repo: process.env.GITHUB_REPO_NAME || 'polocloud',
  branch: process.env.GITHUB_BRANCH || 'master',
  token: GITHUB_TOKEN ? 'SET' : 'NOT SET'
});

export const GITHUB_REPO_CONFIG = {
  owner: process.env.GITHUB_REPO_OWNER || 'HttpMarco',
  repo: process.env.GITHUB_REPO_NAME || 'polocloud',
  branch: process.env.GITHUB_BRANCH || 'master',
  blogPath: 'website/content/blog',
  changelogPath: 'website/content/changelog',
  metaFile: 'website/content/blog/meta.json',
  changelogMetaFile: 'website/content/changelog/meta.json',
};

export interface BlogPostMetadata {
  title: string;
  description: string;
  date: string;
  author: string;
  tags: string[];
  pinned: boolean;
  slug: string;
  content?: string;
}

export interface ChangelogMetadata {
  version: string;
  title: string;
  description: string;
  type: 'major' | 'minor' | 'patch' | 'hotfix';
  releaseDate: string;
  author: string;
  slug: string;
  content?: string;
}

export interface BlogMeta {
  pages: Array<{
    title: string;
    pages: Array<{
      title: string;
      url: string;
    }>;
  }>;
}

export interface ChangelogMeta {
  pages: Array<{
    title: string;
    pages: Array<{
      title: string;
      url: string;
    }>;
  }>;
}

export async function createOrUpdateBlogFile(
  path: string,
  content: string,
  message: string,
  sha?: string
): Promise<void> {
  try {
    const params: {
      owner: string;
      repo: string;
      path: string;
      message: string;
      content: string;
      branch: string;
      sha?: string;
    } = {
      owner: GITHUB_REPO_CONFIG.owner,
      repo: GITHUB_REPO_CONFIG.repo,
      path,
      message,
      content: Buffer.from(content).toString('base64'),
      branch: GITHUB_REPO_CONFIG.branch,
    };

    if (sha) {
      params.sha = sha;
    }

    await blogOctokit.rest.repos.createOrUpdateFileContents(params);
  } catch (error) {
    console.error('Error creating/updating blog file:', error);
    throw error;
  }
}

export async function getFileFromGitHub(path: string): Promise<{ content: string; sha: string } | null> {
  try {
    const response = await blogOctokit.rest.repos.getContent({
      owner: GITHUB_REPO_CONFIG.owner,
      repo: GITHUB_REPO_CONFIG.repo,
      path,
      ref: GITHUB_REPO_CONFIG.branch,
    });

    if ('content' in response.data) {
      return {
        content: Buffer.from(response.data.content, 'base64').toString('utf8'),
        sha: response.data.sha,
      };
    }
    return null;
  } catch (error) {
    if ((error as { status?: number }).status === 404) {
      return null;
    }
    throw error;
  }
}

export async function updateBlogMeta(newPost: { title: string; slug: string }): Promise<void> {
  try {
            const metaFile = await getFileFromGitHub(GITHUB_REPO_CONFIG.metaFile);

    let meta: BlogMeta;
    let sha: string | undefined;

    if (metaFile) {
      meta = JSON.parse(metaFile.content);
      sha = metaFile.sha;
    } else {
      meta = {
        pages: [
          {
            title: "Blog",
            pages: []
          }
        ]
      };
    }

    const blogSection = meta.pages.find(p => p.title === "Blog");
    if (blogSection) {
      const existingPost = blogSection.pages.find(p => p.url === `/blog/${newPost.slug}`);

      if (!existingPost) {

        blogSection.pages.unshift({
          title: newPost.title,
          url: `/blog/${newPost.slug}`
        });
      } else {

        existingPost.title = newPost.title;
      }
    }

    const updatedContent = JSON.stringify(meta, null, 2);
    await createOrUpdateBlogFile(
      GITHUB_REPO_CONFIG.metaFile,
      updatedContent,
      `Update blog meta for: ${newPost.title}`,
      sha
    );
  } catch (error) {
    console.error('Error updating blog meta:', error);
    throw error;
  }
}

export function createSlug(title: string): string {
  return title
    .toLowerCase()
    .replace(/[^a-z0-9\s-]/g, '')
    .replace(/\s+/g, '-')
    .replace(/-+/g, '-')
    .trim();
}

export function generateMDXContent(metadata: BlogPostMetadata, content: string): string {
  const frontmatter = `---
title: "${metadata.title}"
description: "${metadata.description}"
date: "${metadata.date}"
author: "${metadata.author}"
tags: [${metadata.tags.map(tag => `"${tag}"`).join(', ')}]
pinned: ${metadata.pinned}
---

${content}`;

  return frontmatter;
}


export interface FeedbackData {
  id: string;
  userId: string;
  username: string;
  avatar: string;
  rating: number;
  description: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  createdAt: string;
  approvedBy?: string;
  approvedAt?: string;
  rejectedBy?: string;
  rejectedAt?: string;
}

export async function getFeedbackFromGitHub(): Promise<FeedbackData[]> {
  try {
    const feedbackFile = await getFileFromGitHub('website/data/feedback.json');

    if (feedbackFile) {
      const feedbackData = JSON.parse(feedbackFile.content);
      return Array.isArray(feedbackData) ? feedbackData : [];
    }

    return [];
  } catch (error) {
    console.error('Error getting feedback from GitHub:', error);
    return [];
  }
}

export async function saveFeedbackToGitHub(feedbackData: FeedbackData[], commitMessage: string): Promise<void> {
  try {
    const feedbackFile = await getFileFromGitHub('website/data/feedback.json');
    const content = JSON.stringify(feedbackData, null, 2);

    await createOrUpdateBlogFile(
      'website/data/feedback.json',
      content,
      commitMessage,
      feedbackFile?.sha
    );
  } catch (error) {
    console.error('Error saving feedback to GitHub:', error);
    throw error;
  }
}

export async function addFeedbackToGitHub(newFeedback: Omit<FeedbackData, 'id' | 'createdAt' | 'status'>): Promise<FeedbackData> {
  const feedbackData = await getFeedbackFromGitHub();

  const feedback: FeedbackData = {
    ...newFeedback,
    id: Date.now().toString(),
    createdAt: new Date().toISOString(),
    status: 'PENDING'
  };

  feedbackData.push(feedback);

  await saveFeedbackToGitHub(
    feedbackData,
    `Add new feedback from ${newFeedback.username} (${newFeedback.rating} stars)`
  );

  return feedback;
}

export async function updateFeedbackStatusOnGitHub(
  feedbackId: string,
  status: 'APPROVED' | 'REJECTED',
  adminUser: string
): Promise<void> {
  const feedbackData = await getFeedbackFromGitHub();

  const feedbackIndex = feedbackData.findIndex(f => f.id === feedbackId);
  if (feedbackIndex === -1) {
    throw new Error('Feedback not found');
  }

  const feedback = feedbackData[feedbackIndex];
  feedback.status = status;

  if (status === 'APPROVED') {
    feedback.approvedBy = adminUser;
    feedback.approvedAt = new Date().toISOString();

    delete feedback.rejectedBy;
    delete feedback.rejectedAt;
  } else {
    feedback.rejectedBy = adminUser;
    feedback.rejectedAt = new Date().toISOString();

    delete feedback.approvedBy;
    delete feedback.approvedAt;
  }

  feedbackData[feedbackIndex] = feedback;

  await saveFeedbackToGitHub(
    feedbackData,
    `${status.toLowerCase()} feedback from ${feedback.username} by ${adminUser}`
  );
}

export async function getUserFeedbackFromGitHub(userId: string): Promise<FeedbackData | null> {
  const feedbackData = await getFeedbackFromGitHub();
  return feedbackData.find(f => f.userId === userId) || null;
}

export async function getPartnersFromGitHub() {
  try {
    console.log('Fetching partners from GitHub...');
    const partnersFile = await getFileFromGitHub('website/data/partners.json');
    
    if (partnersFile) {
      const partners = JSON.parse(partnersFile.content);
      console.log('Partners loaded from GitHub:', partners.length, 'partners');

      try {
        const fs = await import('fs/promises');
        const path = await import('path');
        const localPath = path.join(process.cwd(), 'data', 'partners.json');
        await fs.writeFile(localPath, partnersFile.content);
        console.log('Partners also saved locally as backup');
      } catch (localError) {
        console.warn('Could not save partners locally:', localError);
      }
      
      return partners;
    }

    return [];
  } catch (error) {
    console.error('Error fetching partners from GitHub:', error);

    try {
      const fs = await import('fs/promises');
      const path = await import('path');
      const localPath = path.join(process.cwd(), 'data', 'partners.json');
      const localContent = await fs.readFile(localPath, 'utf8');
      const localPartners = JSON.parse(localContent);
      console.log('Partners loaded from local file:', localPartners.length, 'partners');
      return localPartners;
    } catch {
      console.log('No local partners file found, returning empty array');
      return [];
    }
  }
}

export async function savePartnersToGitHub(partners: Array<{
  id: string;
  name: string;
  logo: string;
  website?: string;
  description?: string;
  addedAt: string;
  addedBy: string;
}>, commitMessage?: string): Promise<void> {
  try {
    const content = JSON.stringify(partners, null, 2);
    const partnersFile = await getFileFromGitHub('website/data/partners.json');
    
    const message = commitMessage || `Update partners list - ${new Date().toISOString()}`;
    
    await createOrUpdateBlogFile(
      'website/data/partners.json',
      content,
      message,
      partnersFile?.sha
    );

    console.log('Partners saved to GitHub successfully');

    try {
      const fs = await import('fs/promises');
      const path = await import('path');
      const localPath = path.join(process.cwd(), 'data', 'partners.json');
      await fs.writeFile(localPath, content);
      console.log('💾 Partners also saved locally as backup');
    } catch (localError) {
      console.warn('Could not save partners locally:', localError);
    }
    
  } catch (error) {
    console.error('Error saving partners to GitHub:', error);

    try {
      const fs = await import('fs/promises');
      const path = await import('path');
      const localPath = path.join(process.cwd(), 'data', 'partners.json');
      await fs.writeFile(localPath, JSON.stringify(partners, null, 2));
      console.log('💾 Partners saved locally as fallback');
    } catch (localError) {
      console.error('Could not save partners locally either:', localError);
    }
    
    throw error;
  }
}

export async function getPlatformsFromGitHub() {
  try {
    console.log('Fetching platforms from GitHub...');
    const platformsFile = await getFileFromGitHub('website/data/platforms.json');
    
    if (platformsFile) {
      const platforms = JSON.parse(platformsFile.content);
      console.log('Platforms loaded from GitHub:', platforms.length, 'platforms');

      try {
        const fs = await import('fs/promises');
        const path = await import('path');
        const localPath = path.join(process.cwd(), 'data', 'platforms.json');
        await fs.writeFile(localPath, platformsFile.content);
        console.log('💾 Platforms also saved locally as backup');
      } catch (localError) {
        console.warn('Could not save platforms locally:', localError);
      }
      
      return platforms;
    }

    return [];
  } catch (error: unknown) {
    console.error('Error fetching platforms from GitHub:', error);

    try {
      const fs = await import('fs/promises');
      const path = await import('path');
      const localPath = path.join(process.cwd(), 'data', 'platforms.json');
      const localContent = await fs.readFile(localPath, 'utf8');
      const localPlatforms = JSON.parse(localContent);
      console.log('Platforms loaded from local file:', localPlatforms.length, 'platforms');
      return localPlatforms;
    } catch {
      console.log('No local platforms file found, creating empty file');
      await savePlatformsToGitHub([]);
      return [];
    }
  }
}

export async function savePlatformsToGitHub(platforms: Array<{
  id: string;
  name: string;
  icon: string;
  website?: string;
  description?: string;
  addedAt: string;
  addedBy: string;
}>, commitMessage?: string): Promise<void> {
  try {
    const content = JSON.stringify(platforms, null, 2);
    const platformsFile = await getFileFromGitHub('website/data/platforms.json');
    
    const message = commitMessage || `Update platforms list - ${new Date().toISOString()}`;
    
    await createOrUpdateBlogFile(
      'website/data/platforms.json',
      content,
      message,
      platformsFile?.sha
    );

    console.log('Platforms saved to GitHub successfully');

    try {
      const fs = await import('fs/promises');
      const path = await import('path');
      const localPath = path.join(process.cwd(), 'data', 'platforms.json');
      await fs.writeFile(localPath, content);
      console.log('💾 Platforms also saved locally as backup');
    } catch (localError) {
      console.warn('Could not save platforms locally:', localError);
    }
    
  } catch (error) {
    console.error('Error saving platforms to GitHub:', error);

    try {
      const fs = await import('fs/promises');
      const path = await import('path');
      const localPath = path.join(process.cwd(), 'data', 'platforms.json');
      await fs.writeFile(localPath, JSON.stringify(platforms, null, 2));
      console.log('💾 Platforms saved locally as fallback');
    } catch (localError) {
      console.error('Could not save platforms locally either:', localError);
    }
    
    throw error;
  }
}

export function testCacheSystem(): void {
  console.log('🧪 Testing new cache system...');
  
  const status = getCacheStatus();
  console.log('📊 Cache Status:', {
    health: status.cacheHealth,
    nextRefresh: `${Math.round(status.nextRefreshIn / 1000)}s`,
    serverAge: `${Math.round(status.serverCacheAge / 1000)}s`,
    clientAge: `${Math.round(status.clientCacheAge / 1000)}s`
  });
  
  console.log('⚙️ Cache Strategies:', CACHE_STRATEGIES);
  console.log('🕐 Refresh Offsets:', REFRESH_OFFSETS);
  
  console.log('Cache system test completed');
}

export async function createOrUpdateChangelogFile(
  path: string,
  content: string,
  message: string,
  sha?: string
): Promise<void> {
  try {
    const params: {
      owner: string;
      repo: string;
      path: string;
      message: string;
      content: string;
      branch: string;
      sha?: string;
    } = {
      owner: GITHUB_REPO_CONFIG.owner,
      repo: GITHUB_REPO_CONFIG.repo,
      path,
      message,
      content: Buffer.from(content).toString('base64'),
      branch: GITHUB_REPO_CONFIG.branch,
    };

    if (sha) {
      params.sha = sha;
    }

    await blogOctokit.rest.repos.createOrUpdateFileContents(params);
  } catch (error) {
    console.error('Error creating/updating changelog file:', error);
    throw error;
  }
}

export async function updateChangelogMeta(newEntry: { title: string; slug: string }): Promise<void> {
  try {
    const metaFile = await getFileFromGitHub(GITHUB_REPO_CONFIG.changelogMetaFile);

    let meta: ChangelogMeta;
    let sha: string | undefined;

    if (metaFile) {
      meta = JSON.parse(metaFile.content);
      sha = metaFile.sha;
    } else {
      meta = {
        pages: [
          {
            title: "Changelog",
            pages: []
          }
        ]
      };
    }

    const changelogSection = meta.pages.find(p => p.title === "Changelog");
    if (changelogSection) {
      const existingEntry = changelogSection.pages.find(p => p.url === `/changelog/${newEntry.slug}`);

      if (!existingEntry) {
        changelogSection.pages.unshift({
          title: newEntry.title,
          url: `/changelog/${newEntry.slug}`
        });
      } else {
        existingEntry.title = newEntry.title;
      }
    }

    const updatedContent = JSON.stringify(meta, null, 2);
    await createOrUpdateBlogFile(
      GITHUB_REPO_CONFIG.changelogMetaFile,
      updatedContent,
      `Update changelog meta for: ${newEntry.title}`,
      sha
    );
  } catch (error) {
    console.error('Error updating changelog meta:', error);
    throw error;
  }
}

export function generateChangelogMDXContent(metadata: ChangelogMetadata, content: string): string {
  const frontmatter = `---
version: "${metadata.version}"
title: "${metadata.title}"
description: "${metadata.description}"
type: "${metadata.type}"
releaseDate: "${metadata.releaseDate}"
author: "${metadata.author}"
---

${content}`;

  return frontmatter;
}

export async function getChangelogFromGitHub(): Promise<ChangelogMetadata[]> {
  try {
    const metaFile = await getFileFromGitHub(GITHUB_REPO_CONFIG.changelogMetaFile);

    if (!metaFile) {
      return [];
    }

    const meta: ChangelogMeta = JSON.parse(metaFile.content);
    const changelogSection = meta.pages.find(p => p.title === "Changelog");

    if (!changelogSection) {
      return [];
    }

    const changelogEntries = await Promise.all(
      changelogSection.pages.map(async (page) => {
        try {
          const slug = page.url.replace('/changelog/', '');
          const filePath = `${GITHUB_REPO_CONFIG.changelogPath}/${slug}.mdx`;

          const file = await getFileFromGitHub(filePath);
          if (!file) return null;

          let frontmatter: Record<string, unknown>;

          try {
            const parsed = matter(file.content);
            frontmatter = parsed.data;
          } catch (yamlError) {
            return {
              slug,
              version: 'Unknown',
              title: `${slug} (YAML Error)`,
              description: 'This changelog entry has invalid YAML frontmatter',
              type: 'patch' as const,
              releaseDate: new Date().toISOString().split('T')[0],
              author: 'System'
            };
          }

          return {
            slug,
            version: (frontmatter.version as string) || 'Unknown',
            title: (frontmatter.title as string) || page.title,
            description: (frontmatter.description as string) || '',
            type: (frontmatter.type as 'major' | 'minor' | 'patch' | 'hotfix') || 'patch',
            releaseDate: (frontmatter.releaseDate as string) || '',
            author: (frontmatter.author as string) || '',
            content: file.content || ''
          };
        } catch (error) {
          return null;
        }
      })
    );

    const validEntries = changelogEntries.filter((entry): entry is NonNullable<typeof entry> => entry !== null)
      .sort((a, b) => {
        const dateA = a.releaseDate ? new Date(a.releaseDate).getTime() : 0;
        const dateB = b.releaseDate ? new Date(b.releaseDate).getTime() : 0;
        return dateB - dateA;
      });

    return validEntries;
  } catch (error) {
    console.error('Error getting changelog from GitHub:', error);
    return [];
  }
}

export async function saveChangelogToGitHub(changelogData: ChangelogMetadata[], commitMessage: string): Promise<void> {
  try {
    const changelogFile = await getFileFromGitHub('website/data/changelog.json');
    const content = JSON.stringify(changelogData, null, 2);

    await createOrUpdateBlogFile(
      'website/data/changelog.json',
      content,
      commitMessage,
      changelogFile?.sha
    );
  } catch (error) {
    console.error('Error saving changelog to GitHub:', error);
    throw error;
  }
}

export async function addChangelogToGitHub(newChangelog: Omit<ChangelogMetadata, 'slug'>): Promise<ChangelogMetadata> {
  const slug = createSlug(newChangelog.title);
  
  const changelogData: ChangelogMetadata = {
    ...newChangelog,
    slug
  };

  const mdxContent = generateChangelogMDXContent(changelogData, newChangelog.content || '');

  const filePath = `${GITHUB_REPO_CONFIG.changelogPath}/${slug}.mdx`;

  await createOrUpdateChangelogFile(
    filePath,
    mdxContent,
    `Add changelog entry: ${newChangelog.version} - ${newChangelog.title}`
  );

  await updateChangelogMeta({ title: newChangelog.title, slug });

  return changelogData;
}

export async function updateChangelogOnGitHub(
  changelogId: string,
  updatedChangelog: Omit<ChangelogMetadata, 'slug'>,
  adminUser: string
): Promise<void> {
  const slug = createSlug(updatedChangelog.title);
  
  const changelogData: ChangelogMetadata = {
    ...updatedChangelog,
    slug
  };

  const mdxContent = generateChangelogMDXContent(changelogData, updatedChangelog.content || '');

  const filePath = `${GITHUB_REPO_CONFIG.changelogPath}/${slug}.mdx`;

  const oldFile = await getFileFromGitHub(filePath);
  
  await createOrUpdateChangelogFile(
    filePath,
    mdxContent,
    `Update changelog entry: ${updatedChangelog.version} - ${updatedChangelog.title} by ${adminUser}`,
    oldFile?.sha
  );

  await updateChangelogMeta({ title: updatedChangelog.title, slug });
}

export async function deleteChangelogFromGitHub(changelogId: string, adminUser: string): Promise<void> {
  try {
    const metaFile = await getFileFromGitHub(GITHUB_REPO_CONFIG.changelogMetaFile);
    
    if (metaFile) {
      const meta: ChangelogMeta = JSON.parse(metaFile.content);
      const changelogSection = meta.pages.find(p => p.title === "Changelog");
      
      if (changelogSection) {
        const entryToDelete = changelogSection.pages.find(p => p.url === `/changelog/${changelogId}`);
        
        if (entryToDelete) {
          changelogSection.pages = changelogSection.pages.filter(p => p.url !== `/changelog/${changelogId}`);
          
          const updatedContent = JSON.stringify(meta, null, 2);
          await createOrUpdateBlogFile(
            GITHUB_REPO_CONFIG.changelogMetaFile,
            updatedContent,
            `Remove changelog entry by ${adminUser}`,
            metaFile.sha
          );
          
          const mdxFilePath = `${GITHUB_REPO_CONFIG.changelogPath}/${changelogId}.mdx`;
          try {
            const mdxFile = await getFileFromGitHub(mdxFilePath);
            if (mdxFile?.sha) {
              await blogOctokit.rest.repos.deleteFile({
                owner: GITHUB_REPO_CONFIG.owner,
                repo: GITHUB_REPO_CONFIG.repo,
                path: mdxFilePath,
                message: `Delete changelog MDX file: ${changelogId} by ${adminUser}`,
                branch: GITHUB_REPO_CONFIG.branch,
                sha: mdxFile.sha
              });
            } else {
              console.warn(`Could not get SHA for MDX file ${mdxFilePath}, skipping deletion`);
            }
          } catch (deleteError) {
            console.warn(`Could not delete MDX file ${mdxFilePath}:`, deleteError);
          }
        }
      }
    }
  } catch (error) {
    console.error('Error deleting changelog entry:', error);
    throw error;
  }
}

export async function getAllChangelogFiles(): Promise<ChangelogMetadata[]> {
  try {
    console.log('Getting changelog files from GitHub...');
    console.log('Config:', {
      owner: GITHUB_REPO_CONFIG.owner,
      repo: GITHUB_REPO_CONFIG.repo,
      path: GITHUB_REPO_CONFIG.changelogPath,
      branch: GITHUB_REPO_CONFIG.branch
    });

    const changelogDir = GITHUB_REPO_CONFIG.changelogPath;

    const response = await blogOctokit.rest.repos.getContent({
      owner: GITHUB_REPO_CONFIG.owner,
      repo: GITHUB_REPO_CONFIG.repo,
      path: changelogDir,
      ref: GITHUB_REPO_CONFIG.branch,
    });

    console.log('GitHub API response:', response.status, response.data);

    if (!Array.isArray(response.data)) {
      console.log('Response is not an array, trying alternative approach...');
      return [];
    }

    const mdxFiles = response.data.filter(item => 
      item.type === 'file' && item.name.endsWith('.mdx')
    );

    console.log('Found MDX files:', mdxFiles.map(f => f.name));

    const changelogEntries = await Promise.all(
      mdxFiles.map(async (file) => {
        try {
          const filePath = `${changelogDir}/${file.name}`;
          console.log('Reading file:', filePath);
          
          const fileContent = await getFileFromGitHub(filePath);
          
          if (!fileContent) {
            console.log('No content for file:', filePath);
            return null;
          }

          let frontmatter: Record<string, unknown>;
          let parsedContent: string = '';

          try {
            const parsed = matter(fileContent.content);
            frontmatter = parsed.data;
            parsedContent = parsed.content;
            console.log('Parsed frontmatter for', file.name, ':', frontmatter);
            console.log('Author from frontmatter:', frontmatter.author);
            console.log('Version from frontmatter:', frontmatter.version);
            console.log('Title from frontmatter:', frontmatter.title);
          } catch (yamlError) {
            console.error('YAML parsing error for', file.name, ':', yamlError);
            return null;
          }

          const slug = file.name.replace('.mdx', '');
          const author = (frontmatter.author as string) || 'Unknown Author';

          console.log('Final author for', file.name, ':', author);

          return {
            slug,
            version: (frontmatter.version as string) || 'Unknown',
            title: (frontmatter.title as string) || slug,
            description: (frontmatter.description as string) || '',
            type: (frontmatter.type as 'major' | 'minor' | 'patch' | 'hotfix') || 'patch',
            releaseDate: (frontmatter.releaseDate as string) || '',
            author: author,
            content: parsedContent || ''
          };
        } catch (error) {
          console.error(`Error processing file ${file.name}:`, error);
          return null;
        }
      })
    );

    const validEntries = changelogEntries.filter((entry): entry is NonNullable<typeof entry> => entry !== null)
      .sort((a, b) => {
        const dateA = a.releaseDate ? new Date(a.releaseDate).getTime() : 0;
        const dateB = b.releaseDate ? new Date(b.releaseDate).getTime() : 0;
        return dateB - dateA;
      });

    console.log('Final valid entries:', validEntries.length);
    return validEntries;
  } catch (error) {
    console.error('Error getting all changelog files:', error);
    if (error instanceof Error) {
      console.error('Error details:', {
        message: error.message,
        stack: error.stack,
        name: error.name
      });
    }
    return [];
  }
}

export async function getAllBlogFiles(): Promise<BlogPostMetadata[]> {
  try {
    console.log('Getting blog files from GitHub...');
    console.log('Config:', {
      owner: GITHUB_REPO_CONFIG.owner,
      repo: GITHUB_REPO_CONFIG.repo,
      path: GITHUB_REPO_CONFIG.blogPath,
      branch: GITHUB_REPO_CONFIG.branch
    });

    const blogDir = GITHUB_REPO_CONFIG.blogPath;

    const response = await blogOctokit.rest.repos.getContent({
      owner: GITHUB_REPO_CONFIG.owner,
      repo: GITHUB_REPO_CONFIG.repo,
      path: blogDir,
      ref: GITHUB_REPO_CONFIG.branch,
    });

    console.log('GitHub API response:', response.status, response.data);

    if (!Array.isArray(response.data)) {
      console.log('Response is not an array, trying alternative approach...');
      return [];
    }

    const mdxFiles = response.data.filter(item => 
      item.type === 'file' && item.name.endsWith('.mdx')
    );

    console.log('Found MDX files:', mdxFiles.map(f => f.name));

    const blogEntries = await Promise.all(
      mdxFiles.map(async (file) => {
        try {
          const filePath = `${blogDir}/${file.name}`;
          console.log('Reading file:', filePath);
          
          const fileContent = await getFileFromGitHub(filePath);
          
          if (!fileContent) {
            console.log('No content for file:', filePath);
            return null;
          }

          let frontmatter: Record<string, unknown>;
          let parsedContent: string = '';

          try {
            const parsed = matter(fileContent.content);
            frontmatter = parsed.data;
            parsedContent = parsed.content;
            console.log('Parsed frontmatter for', file.name, ':', frontmatter);
            console.log('Author from frontmatter:', frontmatter.author);
            console.log('Title from frontmatter:', frontmatter.title);
            console.log('Date from frontmatter:', frontmatter.date);
          } catch (yamlError) {
            console.error('YAML parsing error for', file.name, ':', yamlError);
            return null;
          }

          const slug = file.name.replace('.mdx', '');
          const author = (frontmatter.author as string) || 'Unknown Author';

          console.log('Final author for', file.name, ':', author);

          return {
            slug,
            title: (frontmatter.title as string) || slug,
            description: (frontmatter.description as string) || '',
            date: (frontmatter.date as string) || '',
            author: author,
            tags: (frontmatter.tags as string[]) || [],
            pinned: (frontmatter.pinned as boolean) || false,
            content: parsedContent || ''
          };
        } catch (error) {
          console.error(`Error processing file ${file.name}:`, error);
          return null;
        }
      })
    );

    const validEntries = blogEntries.filter((entry): entry is NonNullable<typeof entry> => entry !== null)
      .sort((a, b) => {
        const dateA = a.date ? new Date(a.date).getTime() : 0;
        const dateB = b.date ? new Date(b.date).getTime() : 0;
        return dateB - dateA;
      });

    console.log('Final valid blog entries:', validEntries.length);
    return validEntries;
  } catch (error) {
    console.error('Error getting all blog files:', error);
    if (error instanceof Error) {
      console.error('Error details:', {
        message: error.message,
        stack: error.stack,
        name: error.name
      });
    }
    return [];
  }
}

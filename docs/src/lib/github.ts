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
}

export function getCacheStatus(): {
    serverCacheAge: number;
    clientCacheAge: number;
    contributorsCacheAge: number;
    hasServerCache: boolean;
    hasClientCache: boolean;
    hasContributorsCache: boolean;
} {
    const now = Date.now();
    return {
        serverCacheAge: serverCache ? now - serverCacheTimestamp : 0,
        clientCacheAge: clientCache ? now - clientCacheTimestamp : 0,
        contributorsCacheAge: contributorsCache ? now - contributorsCacheTimestamp : 0,
        hasServerCache: !!serverCache,
        hasClientCache: !!clientCache,
        hasContributorsCache: !!contributorsCache,
    };
}



import { Octokit } from '@octokit/rest';

export const blogOctokit = new Octokit({
  auth: GITHUB_TOKEN,
});

export const BLOG_REPO_CONFIG = {
  owner: process.env.GITHUB_REPO_OWNER || 'jakubbbdev',
  repo: process.env.GITHUB_REPO_NAME || 'polocloud',
  branch: process.env.GITHUB_BRANCH || 'improve-web',
  blogPath: 'docs/content/blog',
  metaFile: 'docs/content/blog/meta.json',
};

export interface BlogPostMetadata {
  title: string;
  description: string;
  date: string;
  author: string;
  tags: string[];
  pinned: boolean;
  slug: string;
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
      owner: BLOG_REPO_CONFIG.owner,
      repo: BLOG_REPO_CONFIG.repo,
      path,
      message,
      content: Buffer.from(content).toString('base64'),
      branch: BLOG_REPO_CONFIG.branch,
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

export async function getBlogFileFromGitHub(path: string): Promise<{ content: string; sha: string } | null> {
  try {
    const response = await blogOctokit.rest.repos.getContent({
      owner: BLOG_REPO_CONFIG.owner,
      repo: BLOG_REPO_CONFIG.repo,
      path,
      ref: BLOG_REPO_CONFIG.branch,
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
    const metaFile = await getBlogFileFromGitHub(BLOG_REPO_CONFIG.metaFile);

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
      BLOG_REPO_CONFIG.metaFile,
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
    const feedbackFile = await getBlogFileFromGitHub('docs/data/feedback.json');

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
    const feedbackFile = await getBlogFileFromGitHub('docs/data/feedback.json');
    const content = JSON.stringify(feedbackData, null, 2);

    await createOrUpdateBlogFile(
      'docs/data/feedback.json',
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
    console.log('🔍 Fetching partners from GitHub...');
    const partnersFile = await getBlogFileFromGitHub('docs/data/partners.json');
    
    if (partnersFile) {
      const partners = JSON.parse(partnersFile.content);
      console.log('✅ Partners loaded from GitHub:', partners.length, 'partners');

      try {
        const fs = await import('fs/promises');
        const path = await import('path');
        const localPath = path.join(process.cwd(), 'data', 'partners.json');
        await fs.writeFile(localPath, partnersFile.content);
        console.log('💾 Partners also saved locally as backup');
      } catch (localError) {
        console.warn('⚠️ Could not save partners locally:', localError);
      }
      
      return partners;
    }

    return [];
  } catch (error) {
    console.error('❌ Error fetching partners from GitHub:', error);

    try {
      const fs = await import('fs/promises');
      const path = await import('path');
      const localPath = path.join(process.cwd(), 'data', 'partners.json');
      const localContent = await fs.readFile(localPath, 'utf8');
      const localPartners = JSON.parse(localContent);
      console.log('✅ Partners loaded from local file:', localPartners.length, 'partners');
      return localPartners;
    } catch {
      console.log('📁 No local partners file found, returning empty array');
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
    const partnersFile = await getBlogFileFromGitHub('docs/data/partners.json');
    
    const message = commitMessage || `Update partners list - ${new Date().toISOString()}`;
    
    await createOrUpdateBlogFile(
      'docs/data/partners.json',
      content,
      message,
      partnersFile?.sha
    );

    console.log('✅ Partners saved to GitHub successfully');

    try {
      const fs = await import('fs/promises');
      const path = await import('path');
      const localPath = path.join(process.cwd(), 'data', 'partners.json');
      await fs.writeFile(localPath, content);
      console.log('💾 Partners also saved locally as backup');
    } catch (localError) {
      console.warn('⚠️ Could not save partners locally:', localError);
    }
    
  } catch (error) {
    console.error('❌ Error saving partners to GitHub:', error);

    try {
      const fs = await import('fs/promises');
      const path = await import('path');
      const localPath = path.join(process.cwd(), 'data', 'partners.json');
      await fs.writeFile(localPath, JSON.stringify(partners, null, 2));
      console.log('💾 Partners saved locally as fallback');
    } catch (localError) {
      console.error('❌ Could not save partners locally either:', localError);
    }
    
    throw error;
  }
}

export async function getPlatformsFromGitHub() {
  try {
    console.log('🔍 Fetching platforms from GitHub...');
    const platformsFile = await getBlogFileFromGitHub('docs/data/platforms.json');
    
    if (platformsFile) {
      const platforms = JSON.parse(platformsFile.content);
      console.log('✅ Platforms loaded from GitHub:', platforms.length, 'platforms');

      try {
        const fs = await import('fs/promises');
        const path = await import('path');
        const localPath = path.join(process.cwd(), 'data', 'platforms.json');
        await fs.writeFile(localPath, platformsFile.content);
        console.log('💾 Platforms also saved locally as backup');
      } catch (localError) {
        console.warn('⚠️ Could not save platforms locally:', localError);
      }
      
      return platforms;
    }

    return [];
  } catch (error: unknown) {
    console.error('❌ Error fetching platforms from GitHub:', error);

    try {
      const fs = await import('fs/promises');
      const path = await import('path');
      const localPath = path.join(process.cwd(), 'data', 'platforms.json');
      const localContent = await fs.readFile(localPath, 'utf8');
      const localPlatforms = JSON.parse(localContent);
      console.log('✅ Platforms loaded from local file:', localPlatforms.length, 'platforms');
      return localPlatforms;
    } catch {
      console.log('📁 No local platforms file found, creating empty file');
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
    const platformsFile = await getBlogFileFromGitHub('docs/data/platforms.json');
    
    const message = commitMessage || `Update platforms list - ${new Date().toISOString()}`;
    
    await createOrUpdateBlogFile(
      'docs/data/platforms.json',
      content,
      message,
      platformsFile?.sha
    );

    console.log('✅ Platforms saved to GitHub successfully');

    try {
      const fs = await import('fs/promises');
      const path = await import('path');
      const localPath = path.join(process.cwd(), 'data', 'platforms.json');
      await fs.writeFile(localPath, content);
      console.log('💾 Platforms also saved locally as backup');
    } catch (localError) {
      console.warn('⚠️ Could not save platforms locally:', localError);
    }
    
  } catch (error) {
    console.error('❌ Error saving platforms to GitHub:', error);

    try {
      const fs = await import('fs/promises');
      const path = await import('path');
      const localPath = path.join(process.cwd(), 'data', 'platforms.json');
      await fs.writeFile(localPath, JSON.stringify(platforms, null, 2));
      console.log('💾 Platforms saved locally as fallback');
    } catch (localError) {
      console.error('❌ Could not save platforms locally either:', localError);
    }
    
    throw error;
  }
}
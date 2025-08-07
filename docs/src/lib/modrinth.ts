export interface ModrinthProject {
    project_id: string;
    project_type: string;
    slug: string;
    title: string;
    description: string;
    author: string;
    display_categories: string[];
    download_count: number;
    follower_count: number;
    date_created: string;
    date_modified: string;
    latest_version: string;
    gallery: Array<{
        url: string;
        featured: boolean;
    }>;
    icon_url?: string;
    color?: number;
    client_side: string;
    server_side: string;
    body_url?: string;
    source_url?: string;
    wiki_url?: string;
    discord_url?: string;
    donation_urls: Array<{
        id: string;
        platform: string;
        url: string;
    }>;
    issues_url?: string;
    license: {
        id: string;
        name: string;
        url?: string;
    };
    team: string;
    body?: string;
    moderator_message?: string;
    published: string;
    updated: string;
    approved?: string;
    queued?: string;
    followers: number;
    additional_categories: string[];
    loaders: string[];
    game_versions: string[];
    license_url?: string;
    versions: string[];
    follows: number;
    rating: {
        count: number;
        average: number;
    };
    status: string;
}

export interface ModrinthOrganization {
    id: string;
    name: string;
    description: string;
    icon_url?: string;
    color?: number;
    projects: ModrinthProject[];
    members: Array<{
        user: {
            username: string;
            avatar_url?: string;
        };
        role: string;
    }>;
    created: string;
    updated: string;
}

const MODRINTH_API_BASE = 'https://api.modrinth.com/v2';
const ORGANIZATION_ID = 'polocloud';

let organizationCache: ModrinthOrganization | null = null;
let organizationCacheTimestamp: number = 0;
const ORGANIZATION_CACHE_DURATION = 30 * 60 * 1000;

let lastRequestTime: number = 0;
const MIN_REQUEST_INTERVAL = 500;

async function makeModrinthRequest(url: string): Promise<Response> {
    const now = Date.now();
    const timeSinceLastRequest = now - lastRequestTime;

    if (timeSinceLastRequest < MIN_REQUEST_INTERVAL) {
        await new Promise(resolve => setTimeout(resolve, MIN_REQUEST_INTERVAL - timeSinceLastRequest));
    }

    lastRequestTime = Date.now();

    const headers: HeadersInit = {
        'Accept': 'application/json',
        'User-Agent': 'PoloCloud-Docs'
    };

    const response = await fetch(url, { headers });

    if (response.status === 429) {
        throw new Error('Modrinth API rate limit exceeded');
    }

    if (!response.ok) {
        throw new Error(`Modrinth API error: ${response.status} ${response.statusText}`);
    }

    return response;
}

export async function fetchModrinthOrganization(): Promise<ModrinthOrganization> {
    try {
        const now = Date.now();
        if (organizationCache && (now - organizationCacheTimestamp) < ORGANIZATION_CACHE_DURATION) {
            return organizationCache;
        }

        const orgResponse = await makeModrinthRequest(`${MODRINTH_API_BASE}/organization/${ORGANIZATION_ID}`);
        const orgData = await orgResponse.json();

        const projectsResponse = await makeModrinthRequest(`${MODRINTH_API_BASE}/organization/${ORGANIZATION_ID}/projects`);
        const projectsData: ModrinthProject[] = await projectsResponse.json();

        const detailedProjects = await Promise.all(
            projectsData.map(async (project) => {
                try {
                    const projectResponse = await makeModrinthRequest(`${MODRINTH_API_BASE}/project/${project.project_id}`);
                    const detailedProject = await projectResponse.json();
                    return detailedProject;
                } catch (error) {
                    console.error(`Error fetching project ${project.project_id}:`, error);
                    return project;
                }
            })
        );

        const organization: ModrinthOrganization = {
            ...orgData,
            projects: detailedProjects
        };

        organizationCache = organization;
        organizationCacheTimestamp = now;

        return organization;
    } catch (error) {
        console.error('Error fetching Modrinth organization:', error);

        if (organizationCache) {
            console.log('Returning cached Modrinth organization due to API error');
            return organizationCache;
        }

        return {
            id: 'polocloud',
            name: 'Polocloud',
            description: 'Simple and easy minecraft cloud',
            projects: [
                {
                    project_id: 'polocloud-hub',
                    project_type: 'plugin',
                    slug: 'polocloud-hub',
                    title: 'polocloud-hub',
                    description: 'This plugin is an addon for PoloCloud that adds a /hub command, allowing players to quickly return to the fallback server.',
                    author: 'HttpMarco',
                    display_categories: ['utility'],
                    download_count: 3,
                    follower_count: 5,
                    date_created: '2024-01-01T00:00:00Z',
                    date_modified: '2024-01-01T00:00:00Z',
                    latest_version: '1.0.0',
                    gallery: [],
                    client_side: 'unsupported',
                    server_side: 'required',
                    donation_urls: [],
                    license: {
                        id: 'MIT',
                        name: 'MIT License'
                    },
                    team: 'polocloud',
                    published: '2024-01-01T00:00:00Z',
                    updated: '2024-01-01T00:00:00Z',
                    followers: 5,
                    additional_categories: [],
                    loaders: ['bungeecord', 'velocity'],
                    game_versions: ['1.20'],
                    versions: ['1.0.0'],
                    follows: 5,
                    rating: {
                        count: 0,
                        average: 0
                    },
                    status: 'approved'
                }
            ],
            members: [
                {
                    user: {
                        username: 'HttpMarco',
                        avatar_url: 'https://github.com/HttpMarco.png'
                    },
                    role: 'Member'
                },
                {
                    user: {
                        username: 'RECHERGG',
                        avatar_url: undefined
                    },
                    role: 'Member'
                }
            ],
            created: '2024-01-01T00:00:00Z',
            updated: '2024-01-01T00:00:00Z'
        };
    }
}

export async function getCachedModrinthOrganization(): Promise<ModrinthOrganization> {
    const now = Date.now();

    if (organizationCache && (now - organizationCacheTimestamp) < ORGANIZATION_CACHE_DURATION) {
        return organizationCache;
    }

    const organization = await fetchModrinthOrganization();

    organizationCache = organization;
    organizationCacheTimestamp = now;

    return organization;
}

export function clearModrinthCache(): void {
    organizationCache = null;
    organizationCacheTimestamp = 0;
}

export function getModrinthCacheStatus(): {
    organizationCacheAge: number;
    hasOrganizationCache: boolean;
} {
    const now = Date.now();
    return {
        organizationCacheAge: organizationCache ? now - organizationCacheTimestamp : 0,
        hasOrganizationCache: !!organizationCache,
    };
}
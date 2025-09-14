export interface ModrinthProject {
    id: string;
    slug: string;
    project_types: string[];
    games: string[];
    team_id: string;
    organization: string;
    name: string;
    summary: string;
    description: string;
    published: string;
    updated: string;
    approved?: string;
    queued?: string;
    status: string;
    requested_status: string;
    moderator_message?: string;
    license: {
        id: string;
        name: string;
        url?: string;
    };
    downloads: number;
    followers: number;
    categories: string[];
    additional_categories: string[];
    loaders: string[];
    versions: string[];
    icon_url?: string;
    link_urls?: {
        discord?: string[];
        wiki?: string[];
        github?: string[];
        issues?: string[];
        source?: string[];
    };
    gallery: Array<{
        url: string;
        featured: boolean;
    }>;
    color?: number;
    thread_id?: string;
    monetization_status?: string;
    side_types_migration_review_status?: string;
    game_versions: string[];
}

export interface ModrinthOrganization {
    id: string;
    slug: string;
    name: string;
    team_id: string;
    description: string;
    icon_url?: string;
    color?: number;
    projects: ModrinthProject[];
    members: Array<{
        team_id: string;
        user: {
            username: string;
            avatar_url?: string;
        };
        role: string;
        is_owner: boolean;
        permissions: unknown;
        organization_permissions: unknown;
        accepted: boolean;
        payouts_split: unknown;
        ordering: number;
    }>;
}

const MODRINTH_API_BASE = 'https://api.modrinth.com/v3';
const ORGANIZATION_ID = 'tMaLb2wR';

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

        const organization: ModrinthOrganization = {
            ...orgData,
            projects: projectsData
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
            id: 'tMaLb2wR',
            slug: 'polocloud',
            name: 'Polocloud',
            team_id: 'lDBLJKIJ',
            description: 'Simple and easy minecraft cloud',
            projects: [
                {
                    id: '6xoY6tfS',
                    slug: 'polocloud-hub',
                    project_types: ['plugin'],
                    games: ['minecraft-java'],
                    team_id: 'sllGRGdp',
                    organization: 'tMaLb2wR',
                    name: 'polocloud-hub',
                    summary: 'This plugin is an addon for PoloCloud that adds a /hub command, allowing players to quickly return to the fallback server.',
                    description: 'This plugin is an addon for PoloCloud that adds a /hub command, allowing players to quickly return to the fallback server.',
                    published: '2024-01-01T00:00:00Z',
                    updated: '2024-01-01T00:00:00Z',
                    status: 'approved',
                    requested_status: 'approved',
                    license: {
                        id: 'MIT',
                        name: 'MIT License'
                    },
                    downloads: 3,
                    followers: 5,
                    categories: ['utility'],
                    additional_categories: [],
                    loaders: ['bungeecord', 'velocity'],
                    versions: ['1.0.0'],
                    game_versions: ['1.20'],
                    gallery: []
                }
            ],
            members: [
                {
                    team_id: 'lDBLJKIJ',
                    user: {
                        username: 'HttpMarco',
                        avatar_url: 'https://github.com/HttpMarco.png'
                    },
                    role: 'Member',
                    is_owner: true,
                    permissions: null,
                    organization_permissions: null,
                    accepted: true,
                    payouts_split: null,
                    ordering: 0
                },
                {
                    team_id: 'lDBLJKIJ',
                    user: {
                        username: 'RECHERGG',
                        avatar_url: undefined
                    },
                    role: 'Member',
                    is_owner: false,
                    permissions: null,
                    organization_permissions: null,
                    accepted: true,
                    payouts_split: null,
                    ordering: 1
                }
            ]
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
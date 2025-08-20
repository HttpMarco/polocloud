import { NextRequest, NextResponse } from 'next/server';

interface GitHubProjectItem {
    id: string;
    title: string;
    status: string;
    labels: string[];
    assignees: string[];
    createdAt: string;
    updatedAt: string;
    number?: number;
    url?: string;
}

interface RoadmapItem {
    id: string;
    title: string;
    description: string;
    category: 'ui' | 'platforms' | 'bot' | 'addons';
    estimatedDate?: string;
    tags?: string[];
    assignees?: Array<{
        login: string;
        avatarUrl?: string;
    }>;
}

interface RoadmapColumn {
    id: string;
    title: string;
    color: string;
    items: RoadmapItem[];
}

interface GitHubFieldValue {
    field?: {
        name: string;
    };
    name?: string;
    text?: string;
}

interface GitHubLabel {
    name: string;
}

interface GitHubContent {
    id: string;
    title: string;
    number?: number;
    url?: string;
    labels?: {
        nodes: GitHubLabel[];
    };
    assignees?: {
        nodes: Array<{ login: string; avatarUrl?: string }>;
    };
    createdAt: string;
    updatedAt: string;
}

interface GitHubProjectNode {
    id: string;
    content: GitHubContent | null;
    fieldValues?: {
        nodes: GitHubFieldValue[];
    };
}

const CACHE_DURATION = 10 * 60 * 1000;
let cachedData: RoadmapColumn[] | null = null;
let lastFetch = 0;

async function fetchGitHubProjects(): Promise<RoadmapColumn[]> {
    const GITHUB_TOKEN = process.env.GITHUB_TOKEN;
    const PROJECT_ID = "7";
    const OWNER = "HttpMarco";

    if (!GITHUB_TOKEN) {
        throw new Error('GitHub token not configured');
    }

    const query = `
    query($owner: String!, $projectNumber: Int!) {
      user(login: $owner) {
        projectV2(number: $projectNumber) {
          items(first: 100) {
            nodes {
              id
              content {
                ... on Issue {
                  id
                  title
                  number
                  url
                  labels(first: 10) {
                    nodes {
                      name
                    }
                  }
                  assignees(first: 10) {
                    nodes {
                      login
                      avatarUrl
                    }
                  }
                  createdAt
                  updatedAt
                }
                ... on PullRequest {
                  id
                  title
                  number
                  url
                  labels(first: 10) {
                    nodes {
                      name
                    }
                  }
                  assignees(first: 10) {
                    nodes {
                      login
                      avatarUrl
                    }
                  }
                  createdAt
                  updatedAt
                }
              }
              fieldValues(first: 20) {
                nodes {
                  ... on ProjectV2ItemFieldTextValue {
                    text
                    field {
                      ... on ProjectV2Field {
                        name
                      }
                    }
                  }
                  ... on ProjectV2ItemFieldSingleSelectValue {
                    name
                    field {
                      ... on ProjectV2SingleSelectField {
                        name
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  `;

    const response = await fetch('https://api.github.com/graphql', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${GITHUB_TOKEN}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            query,
            variables: {
                owner: OWNER,
                projectNumber: parseInt(PROJECT_ID),
            },
        }),
    });

    if (!response.ok) {
        throw new Error(`GitHub API error: ${response.status}`);
    }

    const data = await response.json();

    if (data.errors) {
        throw new Error(`GraphQL errors: ${JSON.stringify(data.errors)}`);
    }

    return transformGitHubData(data.data.user.projectV2.items.nodes);
}

function transformGitHubData(items: GitHubProjectNode[]): RoadmapColumn[] {
    const columns: RoadmapColumn[] = [
        {
            id: 'no-status',
            title: 'No Status',
            color: 'border-orange-500',
            items: []
        },
        {
            id: 'todo',
            title: 'Todo',
            color: 'border-sky-500',
            items: []
        },
        {
            id: 'in-progress',
            title: 'In Progress',
            color: 'border-purple-500',
            items: []
        },
        {
            id: 'quality-testing',
            title: 'Quality-Testing',
            color: 'border-pink-500',
            items: []
        },
        {
            id: 'done',
            title: 'Done',
            color: 'border-green-500',
            items: []
        }
    ];

    items.forEach((item) => {
        if (!item.content) return;

        const content = item.content;
        const status = getStatusFromFieldValues(item.fieldValues?.nodes || []);
        const tags = extractTags(content.labels?.nodes || []);
        const assignees = extractAssignees(content.assignees?.nodes || []);

        const roadmapItem: RoadmapItem = {
            id: content.id,
            title: content.title,
            description: '',
            category: determineCategory(content.title, tags),
            tags: tags,
            assignees: assignees,
        };

        const column = columns.find(col =>
            col.title.toLowerCase() === status.toLowerCase()
        );

        if (column) {
            column.items.push(roadmapItem);
        }
    });

    return columns;
}

function getStatusFromFieldValues(fieldValues: GitHubFieldValue[]): string {
    for (const fieldValue of fieldValues) {
        if (fieldValue?.field?.name === 'Status' && fieldValue.name) {
            return fieldValue.name;
        }
    }
    return 'No Status';
}

function extractTags(labels: GitHubLabel[]): string[] {
    const tags: string[] = [];

    console.log('GitHub Labels received:', labels);

    labels.forEach(label => {
        const labelName = label.name.toLowerCase();
        const originalLabelName = label.name;

        console.log(`Processing label: "${originalLabelName}" (lowercase: "${labelName}")`);

        tags.push(originalLabelName);
        
        if (labelName.includes('improvement') && !tags.includes('improvement')) {
            console.log(`Adding normalized tag: "improvement" for label "${originalLabelName}"`);
            tags.push('improvement');
        }
        if (labelName.includes('bug') && !tags.includes('bug')) {
            console.log(`Adding normalized tag: "bug" for label "${originalLabelName}"`);
            tags.push('bug');
        }
        if (labelName.includes('feature') && !tags.includes('feature')) {
            console.log(`Adding normalized tag: "feature" for label "${originalLabelName}"`);
            tags.push('feature');
        }
        if (labelName.includes('prototype-6') && !tags.includes('prototype-6')) {
            console.log(`Adding normalized tag: "prototype-6" for label "${originalLabelName}"`);
            tags.push('prototype-6');
        }
        if (labelName.includes('prototype-5') && !tags.includes('prototype-5')) {
            console.log(`Adding normalized tag: "prototype-5" for label "${originalLabelName}"`);
            tags.push('prototype-5');
        }
        if ((labelName.includes('new') || labelName.includes('requirement')) && !tags.includes('new requirement')) {
            console.log(`Adding normalized tag: "new requirement" for label "${originalLabelName}"`);
            tags.push('new requirement');
        }
    });

    console.log('Final tags extracted:', tags);

    return Array.from(new Set(tags));
}

function extractAssignees(assignees: Array<{ login: string; avatarUrl?: string }> | undefined): Array<{ login: string; avatarUrl?: string }> | undefined {
    if (!assignees || assignees.length === 0) {
        return undefined;
    }

    return assignees.map(assignee => ({
        login: assignee.login,
        avatarUrl: assignee.avatarUrl || `https://github.com/${assignee.login}.png`
    }));
}

function determineCategory(title: string, tags: string[]): 'ui' | 'platforms' | 'bot' | 'addons' {
    const titleLower = title.toLowerCase();

    if (titleLower.includes('ui') || titleLower.includes('interface') || titleLower.includes('terminal')) {
        return 'ui';
    }
    if (titleLower.includes('server') || titleLower.includes('fabric') || titleLower.includes('forge')) {
        return 'platforms';
    }
    if (titleLower.includes('bot') || titleLower.includes('discord')) {
        return 'bot';
    }

    return 'addons';
}

export async function GET(request: NextRequest) {
    try {
        const { searchParams } = new URL(request.url);
        const forceRefresh = searchParams.get('refresh') === 'true';

        if (!forceRefresh && cachedData && (Date.now() - lastFetch) < CACHE_DURATION) {
            return NextResponse.json(cachedData);
        }

        const data = await fetchGitHubProjects();

        cachedData = data;
        lastFetch = Date.now();

        return NextResponse.json(data);
    } catch (error) {
        console.error('GitHub API Error:', error);
        return NextResponse.json(
            { error: 'Failed to fetch GitHub projects' },
            { status: 500 }
        );
    }
}
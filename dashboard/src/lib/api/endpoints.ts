export const API_ENDPOINTS = {
    AUTH: {
        LOGIN: '/api/auth/login',
        LOGOUT: '/api/auth/logout',
        STATUS: '/api/auth/status',
        ME: '/api/auth/me',
    },

    USER: {
        LIST: '/api/users',
        CREATE: '/api/users',
        EDIT: (uuid: string) => `/api/users/${uuid}`,
        DELETE: (uuid: string) => `/api/users/${uuid}`,
        SELF: '/api/user/self',
        CHANGE_PASSWORD: '/api/user/change-password',
        TOKENS: '/api/user/tokens',
        TOKEN_DELETE: (tokenValue: string) => `/api/user/token/${tokenValue}`,
    },

    ROLE: {
        LIST: '/api/roles',
        CREATE: '/api/roles',
        EDIT: (id: number) => `/api/roles/${id}`,
        DELETE: (id: number) => `/api/roles/${id}`,
        GET: (id: number) => `/api/role/${id}`,
    },

    GROUPS: {
        LIST: '/api/groups/list',
        CREATE: '/api/groups/create',
        COUNT: '/api/groups/count',
        COUNT_WITH_RANGE: (from: number, to: number) => `/api/groups/count?from=${from}&to=${to}`,
        EDIT: (groupName: string) => `/api/groups/${groupName}`,
        DELETE: (groupName: string) => `/api/groups/${groupName}`,
    },

    SERVICES: {
        LIST: '/api/services/list',
        COUNT: '/api/services/count',
        COUNT_WITH_RANGE: (from: number, to: number) => `/api/services/count?from=${from}&to=${to}`,
        RESTART: (serviceName: string) => `/api/services/${serviceName}/restart`,
        COMMAND: (serviceName: string) => `/api/services/${serviceName}/command`,
    },

    SYSTEM: {
        INFORMATION: '/api/system/information',
        VERSION: '/api/system/version',
        AVERAGE: '/api/system/average',
        AVERAGE_WITH_RANGE: (from: number, to: number) => `/api/system/average?from=${from}&to=${to}`,
        MINUTES: '/api/system/minutes',
        HOURS: '/api/system/hours',
        DAYS: '/api/system/days',
        DATA_WITH_RANGE: (endpoint: string, from: number, to: number) => `/api/system/${endpoint}?from=${from}&to=${to}`,
    },

    TERMINAL: {
        COMMAND: '/api/terminal/command',
    },

    TEMPLATES: {
        LIST: '/api/templates/list',
        CREATE: '/api/template/create',
        EDIT: (templateName: string) => `/api/template/${templateName}`,
        DELETE: (templateName: string) => `/api/template/${templateName}`,
    },

    PLATFORMS: {
        LIST: '/api/platforms/list',
    },

    PLAYERS: {
        LIST: '/api/players/list',
        GET: (playerName: string) => `/api/players/${playerName}`,
        LIST_WITH_PAGINATION: (page: number, size: number) => `/api/players/list?page=${page}&size=${size}`,
    },

    ONBOARDING: {
        CREATE_USER: '/api/create-user',
        TEST_CONNECTION: '/api/test-connection',
    },
} as const

export const createRangeQuery = (from: number, to: number) => `?from=${from}&to=${to}`
export const createPaginationQuery = (page: number, size: number) => `?page=${page}&size=${size}`



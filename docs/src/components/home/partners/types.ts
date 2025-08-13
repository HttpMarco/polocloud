export interface Partner {
    id: string;
    name: string;
    logo: string;
    website?: string;
    description?: string;
}

export interface Benefit {
    icon: React.ComponentType<{ className?: string }>;
    title: string;
    description: string;
    color: string;
    iconColor: string;
}

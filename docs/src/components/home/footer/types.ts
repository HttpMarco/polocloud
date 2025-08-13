export interface FooterLink {
    href: string;
    label: string;
    icon: React.ComponentType<{ className?: string }>;
    external?: boolean;
}

export interface FooterSection {
    title: string;
    titleIcon: React.ComponentType<{ className?: string }>;
    links: FooterLink[];
}

export interface TechStack {
    name: string;
    href: string;
    icon: string;
    title: string;
}

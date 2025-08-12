export interface FooterLink {
    href: string;
    label: string;
    icon: any;
    external?: boolean;
}

export interface FooterSection {
    title: string;
    titleIcon: any;
    links: FooterLink[];
}

export interface TechStack {
    name: string;
    href: string;
    icon: string;
    title: string;
}

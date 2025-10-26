export interface ContributorStats {
    totalContributors: number;
    projectStars: number;
    projectForks: number;
    projectReleases: number;
}

export interface StatData {
    label: string;
    value: string;
    icon: React.ComponentType<{ className?: string }>;
}

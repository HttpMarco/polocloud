'use client';

import Link from 'next/link';
import { Scale, FileText, Shield, Code, BookOpen, ExternalLink, Info, Newspaper, Map, Mail } from 'lucide-react';

export function FooterLinks() {
    const legalLinks = [
        { href: '/imprint', label: 'Imprint', icon: FileText },
        { href: '/privacy', label: 'Privacy Policy', icon: Shield },
        { href: '/terms', label: 'Terms of Service', icon: FileText }
    ];

    const documentationLinks = [
        { href: '/download', label: 'Download', icon: BookOpen },
        { href: 'https://github.com/HttpMarco/polocloud', label: 'GitHub Repository', icon: ExternalLink, external: true },
        { href: '/roadmap', label: 'Roadmap', icon: Map },
        { href: '/changelog', label: 'Changelog', icon: FileText }
    ];

    const moreLinks = [
        { href: '/blog', label: 'Blog', icon: Newspaper },
        { href: '/changelog', label: 'Changelog', icon: FileText },
        { href: '/roadmap', label: 'Roadmap', icon: Map },
        { href: 'mailto:contact@polocloud.de', label: 'Contact', icon: Mail, external: true }
    ];

    const renderLink = (link: { href: string; label: string; icon: React.ComponentType<{ className?: string }>; external?: boolean }) => {
        const LinkComponent = link.external ? 'a' : Link;
        const props = link.external ? { href: link.href, target: '_blank', rel: 'noopener noreferrer' } : { href: link.href };
        
        return (
            <LinkComponent 
                key={link.href}
                {...props}
                className="text-sm text-muted-foreground dark:text-white/60 hover:text-foreground dark:hover:text-white transition-all duration-300 hover:scale-105 font-minecraft flex items-center justify-center sm:justify-start gap-2"
            >
                <link.icon className="w-4 h-4" />
                {link.label}
            </LinkComponent>
        );
    };

    return (
        <>
            <div className="flex flex-col gap-4 text-center sm:text-left">
                <h3 className="font-minecraft font-bold text-foreground dark:text-white text-lg flex items-center justify-center sm:justify-start gap-2">
                    <Scale className="w-5 h-5 text-primary" />
                    Legal
                </h3>
                <div className="flex flex-col gap-2">
                    {legalLinks.map(renderLink)}
                </div>
            </div>

            <div className="flex flex-col gap-4 text-center sm:text-left">
                <h3 className="font-minecraft font-bold text-foreground dark:text-white text-lg flex items-center justify-center sm:justify-start gap-2">
                    <Code className="w-5 h-5 text-primary" />
                    Documentation
                </h3>
                <div className="flex flex-col gap-2">
                    {documentationLinks.map(renderLink)}
                </div>
            </div>

            <div className="flex flex-col gap-4 text-center sm:text-left">
                <h3 className="font-minecraft font-bold text-foreground dark:text-white text-lg flex items-center justify-center sm:justify-start gap-2">
                    <Info className="w-5 h-5 text-primary" />
                    More
                </h3>
                <div className="flex flex-col gap-2">
                    {moreLinks.map(renderLink)}
                </div>
            </div>
        </>
    );
}

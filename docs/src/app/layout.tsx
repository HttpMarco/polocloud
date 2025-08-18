import '@/app/global.css';
import {RootProvider} from 'fumadocs-ui/provider';
import {Inter} from 'next/font/google';
import type {ReactNode} from 'react';

const inter = Inter({
    subsets: ['latin'],
});

export const viewport = {
    width: 'device-width',
    initialScale: 1,
    maximumScale: 5,
    themeColor: [
        { media: '(prefers-color-scheme: light)', color: '#0078ff' },
        { media: '(prefers-color-scheme: dark)', color: '#1a1a1a' }
    ],
    colorScheme: 'light dark',
};

export const metadata = {
    title: 'PoloCloud | Simplest and easiest Cloud for Minecraft',
    description: 'Simplest and easiest Cloud for Minecraft',
    keywords: 'Minecraft, Cloud, Server Management, Hosting, PoloCloud, Gaming',
    authors: [{ name: 'PoloCloud Team' }],
    creator: 'PoloCloud',
    publisher: 'PoloCloud',
    openGraph: {
        title: 'PoloCloud | Simplest and easiest Cloud for Minecraft',
        url: 'https://polocloud.de/',
        siteName: 'PoloCloud',
        description: 'Simplest and easiest Cloud for Minecraft. Manage your servers with ease.',
        images: [
            {
                url: 'https://polocloud.de/logo.png',
                width: 512,
                height: 512,
                alt: 'PoloCloud Logo - Minecraft Cloud Management',
                type: 'image/png',
            },
            {
                url: 'https://polocloud.de/og-image.png',
                width: 1200,
                height: 630,
                alt: 'PoloCloud - Minecraft Cloud Management Platform',
                type: 'image/png',
            },
        ],
        locale: 'en_US',
        type: 'website',
        countryName: 'Germany',
    },
    twitter: {
        card: 'summary_large_image',
        title: 'PoloCloud | Simplest and easiest Cloud for Minecraft',
        description: 'Simplest and easiest Cloud for Minecraft. Manage your servers with ease.',
        images: [
            {
                url: 'https://polocloud.de/og-image.png',
                alt: 'PoloCloud - Minecraft Cloud Management Platform',
            }
        ],
        creator: '@polocloud',
        site: '@polocloud',
    },
    robots: {
        index: true,
        follow: true,
        googleBot: {
            index: true,
            follow: true,
            'max-video-preview': -1,
            'max-image-preview': 'large',
            'max-snippet': -1,
        },
    },
    alternates: {
        canonical: 'https://polocloud.de/',
        languages: {
            'en-US': 'https://polocloud.de/',
        },
    },
    icons: {
        icon: [
            { url: '/logo.png', sizes: '32x32', type: 'image/png' },
            { url: '/logo.png', sizes: '16x16', type: 'image/png' },
        ],
        shortcut: '/logo.png',
        apple: '/logo.png',
        other: [
            {
                rel: 'mask-icon',
                url: '/logo.png',
                color: '#0078ff',
            },
        ],
    },
    manifest: '/manifest.json',
    other: {
        'mobile-web-app-capable': 'yes',
        'apple-mobile-web-app-capable': 'yes',
        'apple-mobile-web-app-status-bar-style': 'default',
        'apple-mobile-web-app-title': 'PoloCloud',
        'msapplication-TileColor': '#0078ff',
        'msapplication-config': '/browserconfig.xml',
        'application-name': 'PoloCloud',
        'msapplication-tooltip': 'Minecraft Cloud Management',
        'msapplication-starturl': '/',
    },
};

export default function Layout({children}: { children: ReactNode }) {
    return (
        <html lang="en" className={inter.className} suppressHydrationWarning>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=5, user-scalable=yes" />
            <meta name="theme-color" content="#0078ff" media="(prefers-color-scheme: light)" />
            <meta name="theme-color" content="#1a1a1a" media="(prefers-color-scheme: dark)" />
            <meta name="apple-mobile-web-app-capable" content="yes" />
            <meta name="apple-mobile-web-app-status-bar-style" content="default" />
            <meta name="apple-mobile-web-app-title" content="PoloCloud" />
            <meta name="msapplication-TileColor" content="#0078ff" />
            <meta name="msapplication-config" content="/browserconfig.xml" />

            <meta property="og:image:width" content="1200" />
            <meta property="og:image:height" content="630" />
            <meta property="og:image:type" content="image/png" />
            <meta property="og:image:alt" content="PoloCloud - Minecraft Cloud Management Platform" />

            <meta name="discord:invite" content="discord.polocloud.de" />
            <meta name="discord:server" content="PoloCloud Community" />

            <meta name="keywords" content="Minecraft, Cloud, Server Management, Hosting, PoloCloud, Gaming, Server Hosting, Minecraft Server" />
            <meta name="author" content="PoloCloud Team" />
            <meta name="robots" content="index, follow" />
            <meta name="language" content="English" />
            <meta name="revisit-after" content="7 days" />
            <meta name="distribution" content="global" />
            <meta name="rating" content="general" />
            <meta name="coverage" content="Worldwide" />
            <meta name="target" content="all" />
            <meta name="HandheldFriendly" content="true" />
            <meta name="format-detection" content="telephone=no" />
        </head>
        <body className="flex flex-col min-h-screen scroll-optimized">
        <RootProvider>{children}</RootProvider>
        </body>
        </html>
    );
}

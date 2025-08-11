import '@/app/global.css';
import {RootProvider} from 'fumadocs-ui/provider';
import {Inter} from 'next/font/google';
import type {ReactNode} from 'react';

const inter = Inter({
    subsets: ['latin'],
});

export const metadata = {
    title: 'PoloCloud | Simplest and easiest Cloud for Minecraft',
    description: 'Simplest and easiest Cloud for Minecraft',
    viewport: 'width=device-width, initial-scale=1, maximum-scale=5',
    themeColor: '#0078ff',
    colorScheme: 'light dark',
    openGraph: {
        title: 'PoloCloud',
        url: 'https://polocloud.de/',
        siteName: 'PoloCloud',
        description: 'Simplest and easiest Cloud for Minecraft',
        images: [
            {
                url: '/logo.png',
                width: 64,
                height: 64,
                alt: 'PoloCloud Logo',
            },
        ],
        locale: 'en_US',
        type: 'website',
    },
    twitter: {
        card: 'summary_large_image',
        title: 'PoloCloud',
        description: 'Simplest and easiest Cloud for Minecraft',
        images: ['/logo.png'],
    },
    icons: {
        icon: '/logo.png',
        shortcut: '/logo.png',
        apple: '/logo.png',
    },
    manifest: '/manifest.json',
    other: {
        'mobile-web-app-capable': 'yes',
        'apple-mobile-web-app-capable': 'yes',
        'apple-mobile-web-app-status-bar-style': 'default',
        'apple-mobile-web-app-title': 'PoloCloud',
        'msapplication-TileColor': '#0078ff',
        'msapplication-config': '/browserconfig.xml',
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
        </head>
        <body className="flex flex-col min-h-screen">
        <RootProvider>{children}</RootProvider>
        </body>
        </html>
    );
}

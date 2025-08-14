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
    },
    icons: {
        icon: '/logo.png',
    },
};

export default function Layout({children}: { children: ReactNode }) {
    return (
        <html lang="en" className={inter.className} suppressHydrationWarning>
        <body className="flex flex-col min-h-screen">
        <RootProvider>{children}</RootProvider>
        </body>
        </html>
    );
}

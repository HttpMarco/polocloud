import '@/app/global.css';
import {RootProvider} from 'fumadocs-ui/provider';
import {Inter} from 'next/font/google';
import type {ReactNode} from 'react';
import {Banner} from 'fumadocs-ui/components/banner';

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
        <Banner
            variant="rainbow"
            rainbowColors={[
                'rgba(0, 120, 255, 0.5)',
                'rgba(0, 120, 255, 0.5)',
                'transparent',
                'rgba(0, 120, 255, 0.5)',
                'transparent',
                'rgba(0, 120, 255, 0.5)',
                'transparent',
            ]}
            changeLayout={true}
            id="pre-4-notify"
        >
            PoloCloud 🧪 v3.0.0-pre-4 has released.⠀
            <a href="https://github.com/HttpMarco/polocloud/releases/tag/3.0.0-pre.4-SNAPSHOT" className="underline">
                Check it out now.
            </a>
        </Banner>
        <RootProvider>{children}</RootProvider>
        </body>
        </html>
    );
}

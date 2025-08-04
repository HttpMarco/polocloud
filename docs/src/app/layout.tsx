import '@/app/global.css';
import {RootProvider} from 'fumadocs-ui/provider';
import {Inter} from 'next/font/google';
import type {ReactNode} from 'react';
import {Banner} from 'fumadocs-ui/components/banner';
import image from '../../../../../public/logo.png';

const inter = Inter({
    subsets: ['latin'],
});

export default function Layout({children}: { children: ReactNode }) {
    return (
        <html lang="en" className={inter.className} suppressHydrationWarning>
        <head>
            <title>PoloCloud | Simplest and easiest Cloud for Minecraft</title>
            <link rel="icon" href={image}/>

            <meta property="og:site_name" content="PoloCloud"/>
            <meta property="og:title" content="PoloCloud"/>
            <meta property="og:url" content="https://polocloud.de/"/>
            <meta property="og:description" content="Simplest and easiest Cloud for Minecraft"/>
            <meta property="og:image" content={image}/>
            <meta property="og:image:url" content={image}/>
        </head>
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

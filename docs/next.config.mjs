import { createMDX } from 'fumadocs-mdx/next';

const withMDX = createMDX();

/** @type {import('next').NextConfig} */
const config = {
    reactStrictMode: true,
    output: 'standalone',
    trailingSlash: false,
    basePath: '',
    assetPrefix: '',
    images: {
        remotePatterns: [
            {
                protocol: 'https',
                hostname: 'cdn.discordapp.com',
                port: '',
                pathname: '/avatars/**',
            },
            {
                protocol: 'https',
                hostname: '*.vercel-storage.com',
                port: '',
                pathname: '/**',
            },
        ],
        unoptimized: true,
    },
};

export default withMDX(config);

import { createMDX } from 'fumadocs-mdx/next';

const withMDX = createMDX();
const isGithubPages = process.env.GITHUB_PAGES === 'true'

/** @type {import('next').NextConfig} */
const config = {
  reactStrictMode: true,
  basePath: isGithubPages ? '/polocloud' : '',
    assetPrefix: isGithubPages ? '/polocloud/' : '',
};

export default withMDX(config);

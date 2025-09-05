/** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    domains: ['localhost', 'mineskin.eu'],
    unoptimized: false,
  },
  
  webpack: (config, { isServer }) => {
    if (!isServer) {
      config.resolve.fallback = {
        ...config.resolve.fallback,
        fs: false,
        net: false,
        tls: false,
      };
    }
    return config;
  },
  
  typescript: {
    ignoreBuildErrors: false,
  },

  eslint: {
    ignoreDuringBuilds: false,
  },

  trailingSlash: false,
  poweredByHeader: false,
  
  generateBuildId: async () => {
    return 'build-' + Date.now();
  },
  
  async rewrites() {
    return [
      {
        source: '/login',
        destination: '/login',
        has: [
          {
            type: 'header',
            key: 'x-vercel-deployment',
          },
        ],
      },
      {
        source: '/onboarding',
        destination: '/onboarding',
        has: [
          {
            type: 'header',
            key: 'x-vercel-deployment',
          },
        ],
      },
    ];
  },
};

export default nextConfig;

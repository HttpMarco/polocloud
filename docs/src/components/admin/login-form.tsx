"use client";

import { LogIn, Github, Shield } from 'lucide-react';

export function LoginForm() {
  const handleGitHubLogin = () => {
    const clientId = process.env.NEXT_PUBLIC_GITHUB_CLIENT_ID;
    const redirectUri = `${window.location.origin}/api/auth/github-admin`;
    const githubAuthUrl = `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${encodeURIComponent(redirectUri)}&scope=read:user`;
    
    window.location.href = githubAuthUrl;
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900">
      <div className="max-w-md w-full mx-4">
        <div className="text-center mb-8">
          <div className="w-20 h-20 mx-auto mb-4 bg-gradient-to-br from-blue-500 to-purple-600 rounded-2xl flex items-center justify-center">
            <Shield className="w-10 h-10 text-white" />
          </div>
          <h1 className="text-3xl font-bold text-white mb-2">Admin Dashboard</h1>
          <p className="text-slate-300">Sign in to access the admin panel</p>
        </div>

        <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-8 border border-white/20">
          <div className="space-y-6">
            <div className="text-center">
              <p className="text-slate-300 mb-4">Access restricted to authorized GitHub users only</p>
            </div>

            <button
              onClick={handleGitHubLogin}
              className="w-full flex items-center justify-center gap-3 px-6 py-3 bg-slate-800 hover:bg-slate-700 text-white rounded-xl font-medium transition-all duration-300 border border-slate-600 hover:border-slate-500"
            >
              <Github className="w-5 h-5" />
              Continue with GitHub
            </button>

            <div className="text-center">
              <p className="text-xs text-slate-400">
                By continuing, you agree to our terms of service and privacy policy
              </p>
            </div>
          </div>
        </div>

        <div className="text-center mt-6">
          <p className="text-slate-400 text-sm">
            Need help? Contact your system administrator
          </p>
        </div>
      </div>
    </div>
  );
}

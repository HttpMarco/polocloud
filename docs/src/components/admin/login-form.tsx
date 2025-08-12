"use client";

import { LogIn, Github, Shield, ArrowRight, Lock, UserCheck } from 'lucide-react';
import { showToast, ToastContainer } from '@/components/ui/toast';

export function LoginForm() {
  const handleGitHubLogin = () => {
    const clientId = process.env.NEXT_PUBLIC_GITHUB_CLIENT_ID;

    
    const redirectUri = `${window.location.origin}/api/auth/github-admin`;
    const githubAuthUrl = `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${encodeURIComponent(redirectUri)}&scope=read:user`;

    window.location.href = githubAuthUrl;
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-background via-muted/5 to-background relative overflow-hidden">

      <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]" />
      
      <div className="absolute top-20 left-10 w-32 h-32 bg-primary/5 rounded-full blur-3xl" />
      <div className="absolute bottom-20 right-10 w-40 h-40 bg-primary/5 rounded-full blur-3xl" />
      <div className="absolute top-1/3 right-1/4 w-24 h-24 bg-primary/3 rounded-full blur-2xl" />

      <div className="relative z-10 max-w-md w-full mx-4">

        <div className="text-center mb-8">
          <div className="w-20 h-20 mx-auto mb-6 bg-gradient-to-br from-primary/20 to-primary/10 rounded-2xl flex items-center justify-center border border-primary/30">
            <Shield className="w-10 h-10 text-primary" />
          </div>
          
          <h1 className="text-3xl md:text-4xl font-black text-foreground dark:text-white mb-3 bg-gradient-to-r from-foreground via-primary to-foreground bg-clip-text text-transparent tracking-tight">
            Admin Dashboard
          </h1>
          <p className="text-muted-foreground dark:text-white/70 text-lg">
            Sign in to access the admin panel
          </p>
        </div>

        <div className="bg-card/30 backdrop-blur-sm border border-border/50 rounded-2xl p-8 shadow-xl">
          <div className="space-y-6">

            <div className="text-center space-y-3">
              <div className="flex items-center justify-center gap-2 text-muted-foreground dark:text-white/60">
                <Lock className="w-4 h-4" />
                <span className="text-sm font-medium">Access restricted to authorized users only</span>
              </div>
              <div className="flex items-center justify-center gap-2 text-muted-foreground dark:text-white/60">
                <UserCheck className="w-4 h-4" />
                <span className="text-sm">GitHub authentication required</span>
              </div>
            </div>

            <button
              onClick={handleGitHubLogin}
              className="w-full group relative flex items-center justify-center gap-3 px-6 py-4 bg-gradient-to-r from-primary to-primary/80 hover:from-primary/90 hover:to-primary/70 text-white rounded-xl font-semibold text-lg transition-all duration-300 shadow-lg hover:shadow-xl hover:scale-[1.02] border border-primary/20"
            >
              <Github className="w-6 h-6 group-hover:scale-110 transition-transform duration-300" />
              <span>Continue with GitHub</span>
              <ArrowRight className="w-5 h-5 group-hover:translate-x-1 transition-transform duration-300" />
            </button>

            <div className="text-center">
              <p className="text-xs text-muted-foreground/70 dark:text-white/40">
                By continuing, you agree to our{' '}
                <a href="/terms" className="text-primary hover:text-primary/80 underline underline-offset-2">
                  terms of service
                </a>{' '}
                and{' '}
                <a href="/privacy" className="text-primary hover:text-primary/80 underline underline-offset-2">
                  privacy policy
                </a>
              </p>
            </div>
          </div>
        </div>


        <div className="text-center mt-6">
          <a 
            href="/"
            className="inline-flex items-center gap-2 text-muted-foreground dark:text-white/60 hover:text-foreground dark:hover:text-white transition-all duration-300 text-sm"
          >
            <ArrowRight className="w-4 h-4 rotate-180" />
            Back to Homepage
          </a>
        </div>
      </div>
      
      <ToastContainer />
    </div>
  );
}

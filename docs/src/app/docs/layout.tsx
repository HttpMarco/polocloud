import { ReactNode } from 'react';
import { Logo } from '@/components/layout/header/logo';
import { Footer } from '../(home)/components/footer';

export default async function Layout({
  children,
  params,
}: {
  children: ReactNode;
  params: Promise<{ slug?: string[] }>;
}) {
  try {
    const resolvedParams = await params;

    if (!resolvedParams.slug || resolvedParams.slug.length === 0) {
      return <>{children}</>;
    }

    return (
      <div className="relative min-h-screen">
        <header className="sticky top-0 z-50 w-full border-b border-border/40 bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60 shadow-sm">
          <div className="container flex h-16 max-w-screen-2xl items-center px-4">
            <div className="flex items-center">
              <Logo />
              <span className="ml-4 text-lg font-semibold">Documentation</span>
            </div>
          </div>
        </header>

        <div className="flex min-h-[calc(100vh-4rem)]">
          <div className="w-64 border-r border-border/40 bg-muted/20 p-4">
            <div className="text-sm text-muted-foreground">
              Navigation coming soon...
            </div>
          </div>

          <div className="flex-1 p-6">
            {children}
          </div>
        </div>

        <Footer />
      </div>
    );
  } catch (error) {
    console.error('Error in docs layout:', error);
    return (
      <div className="min-h-screen bg-background">
        <div className="container mx-auto py-8">
          <div className="text-center">
            <h1 className="text-2xl font-bold mb-4">Documentation Error</h1>
            <p className="text-muted-foreground">
              There was an error loading the documentation. Please try again later.
            </p>
          </div>
          {children}
        </div>
      </div>
    );
  }
}

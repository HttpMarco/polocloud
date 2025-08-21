import { source } from '@/lib/source';
import { notFound } from 'next/navigation';
import { BookOpen, Construction, Clock, Sparkles } from 'lucide-react';

export default async function Page(props: {
  params: Promise<{ slug?: string[] }>;
}) {
  const params = await props.params;
  
  if (!params.slug || params.slug.length === 0) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-background via-background to-background">
        <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)] pointer-events-none" />
        
        <div className="relative z-10">
          <div className="text-center pt-12 pb-16 px-4">
            <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-primary/10 border border-primary/20 text-primary text-sm font-medium mb-6">
              <BookOpen className="w-4 h-4" />
              <span>Documentation</span>
            </div>
            
            <h1 className="text-4xl md:text-5xl font-bold bg-gradient-to-r from-foreground via-primary to-foreground bg-clip-text text-transparent tracking-tight mb-6">
              Documentation Under Construction
            </h1>
            
            <p className="text-xl text-muted-foreground max-w-3xl mx-auto leading-relaxed">
              We&apos;re completely redesigning and restructuring our documentation to provide you with a better experience.
            </p>
          </div>

          <div className="max-w-6xl mx-auto px-4 pb-20">
            <div className="grid md:grid-cols-3 gap-6">
              <div className="relative overflow-hidden rounded-2xl border border-border/50 bg-gradient-to-br from-background/50 to-background/80 p-8">
                <div className="absolute inset-0 bg-gradient-to-br from-orange-500/5 via-transparent to-orange-500/5 opacity-40" />
                <div className="relative">
                  <div className="flex items-center justify-center mb-6">
                    <div className="flex h-16 w-16 items-center justify-center rounded-2xl bg-orange-500/20 ring-2 ring-orange-500/30">
                      <Construction className="h-8 w-8 text-orange-500" />
                    </div>
                  </div>
                  
                  <h3 className="text-2xl font-bold text-foreground mb-4 text-center">Under Construction</h3>
                  <p className="text-muted-foreground text-center leading-relaxed">
                    Our documentation is being completely rebuilt from the ground up with a modern, intuitive design.
                  </p>
                </div>
              </div>

              <div className="relative overflow-hidden rounded-2xl border border-border/50 bg-gradient-to-br from-background/50 to-background/80 p-8">
                <div className="absolute inset-0 bg-gradient-to-br from-blue-500/5 via-transparent to-blue-500/5 opacity-40" />
                <div className="relative">
                  <div className="flex items-center justify-center mb-6">
                    <div className="flex h-16 w-16 items-center justify-center rounded-2xl bg-blue-500/20 ring-2 ring-blue-500/30">
                      <Clock className="h-8 w-8 text-blue-500" />
                    </div>
                  </div>
                  
                  <h3 className="text-2xl font-bold text-foreground mb-4 text-center">Coming Soon</h3>
                  <p className="text-muted-foreground text-center leading-relaxed">
                    We&apos;re working hard to bring you comprehensive, easy-to-follow documentation that covers everything you need.
                  </p>
                </div>
              </div>

              <div className="relative overflow-hidden rounded-2xl border border-border/50 bg-gradient-to-br from-background/50 to-background/80 p-8">
                <div className="absolute inset-0 bg-gradient-to-br from-green-500/5 via-transparent to-green-500/5 opacity-40" />
                <div className="relative">
                  <div className="flex items-center justify-center mb-6">
                    <div className="flex h-16 w-16 items-center justify-center rounded-2xl bg-green-500/20 ring-2 ring-green-500/30">
                      <Sparkles className="h-8 w-8 text-green-500" />
                    </div>
                  </div>
                  
                  <h3 className="text-2xl font-bold text-foreground mb-4 text-center">New Features</h3>
                  <p className="text-muted-foreground text-center leading-relaxed">
                    Interactive examples, better search, and improved navigation are just some of the improvements coming.
                  </p>
                </div>
              </div>
            </div>

            <div className="mt-12">
              <div className="relative overflow-hidden rounded-2xl border border-border/50 bg-gradient-to-br from-primary/5 via-primary/10 to-primary/5 p-8">
                <div className="absolute inset-0 bg-gradient-to-br from-primary/5 via-transparent to-primary/5 opacity-40" />
                <div className="relative text-center">
                  <div className="flex items-center justify-center mb-6">
                    <div className="flex h-16 w-16 items-center justify-center rounded-2xl bg-primary/20 ring-2 ring-primary/30">
                      <BookOpen className="h-8 w-8 text-primary" />
                    </div>
                  </div>
                  
                  <h3 className="text-2xl font-bold text-foreground mb-4">Stay Tuned!</h3>
                  <p className="text-muted-foreground leading-relaxed max-w-2xl mx-auto">
                    We&apos;re working hard to bring you the best documentation experience possible. 
                    Follow our progress and get notified when the new docs are ready!
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  try {
    const page = source.getPage(params.slug);
    if (!page) notFound();

    // Simple content rendering without DocsPage components
    return (
      <div className="prose prose-gray dark:prose-invert max-w-none">
        <div className="mb-8">
          <h1 className="text-4xl font-bold mb-4">{page.data.title}</h1>
          {page.data.description && (
            <p className="text-xl text-muted-foreground">{page.data.description}</p>
          )}
        </div>
        
        <div className="border-t border-border/40 pt-8">
          <div className="text-sm text-muted-foreground mb-4">
            Last updated: {new Date().toISOString().split('T')[0]}
          </div>
          
          {/* Simple content rendering */}
          <div className="mt-6">
            <div className="bg-muted/20 rounded-lg p-4 border border-border/40">
              <p className="text-muted-foreground">
                Content rendering is temporarily simplified while we fix the documentation system.
                The full content will be available soon.
              </p>
            </div>
          </div>
        </div>
      </div>
    );
  } catch (error) {
    console.error('Error rendering docs page:', error);
    notFound();
  }
}

export async function generateStaticParams() {
  try {
    return source.generateParams();
  } catch (error) {
    console.error('Error generating static params:', error);
    return [];
  }
}

export async function generateMetadata(props: {
  params: Promise<{ slug?: string[] }>;
}) {
  const params = await props.params;

  if (!params.slug || params.slug.length === 0) {
    return {
      title: 'Documentation Under Construction - PoloCloud',
      description: 'We\'re completely redesigning and restructuring our documentation to provide you with a better experience.',
    };
  }
  
  try {
    const page = source.getPage(params.slug);
    if (!page) notFound();

    return {
      title: page.data.title,
      description: page.data.description,
    };
  } catch (error) {
    console.error('Error generating metadata:', error);
    return {
      title: 'Documentation - PoloCloud',
      description: 'PoloCloud documentation',
    };
  }
}

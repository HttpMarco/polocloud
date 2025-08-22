import { source } from '@/lib/source';
import { notFound } from 'next/navigation';
import { BookOpen, Construction } from 'lucide-react';

export default async function Page(props: {
  params: Promise<{ slug?: string[] }>;
}) {
  const params = await props.params;
  
  if (!params.slug || params.slug.length === 0) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-background via-background to-background">
        <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)] pointer-events-none" />
        
        <div className="relative z-10 flex items-center justify-center min-h-screen">
          <div className="max-w-2xl mx-auto px-6 text-center">
            <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-primary/10 border border-primary/20 text-primary text-sm font-medium mb-8">
              <BookOpen className="w-4 h-4" />
              <span>Documentation</span>
            </div>
            
            <h1 className="text-4xl md:text-5xl font-bold bg-gradient-to-r from-foreground via-primary to-foreground bg-clip-text text-transparent tracking-tight mb-8">
              Documentation Under Construction
            </h1>
            
            <div className="relative overflow-hidden rounded-2xl border border-border/50 bg-gradient-to-br from-background/50 to-background/80 p-12">
              <div className="absolute inset-0 bg-gradient-to-br from-orange-500/5 via-transparent to-orange-500/5 opacity-40" />
              <div className="relative">
                <div className="flex items-center justify-center mb-8">
                  <div className="flex h-20 w-20 items-center justify-center rounded-2xl bg-orange-500/20 ring-2 ring-orange-500/30">
                    <Construction className="h-10 w-10 text-orange-500" />
                  </div>
                </div>
                
                <h3 className="text-3xl font-bold text-foreground mb-6 text-center">Under Construction</h3>
                <p className="text-lg text-muted-foreground text-center leading-relaxed max-w-xl mx-auto">
                  Our documentation is being completely rebuilt from the ground up with a modern, intuitive design.
                </p>
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

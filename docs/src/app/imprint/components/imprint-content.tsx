'use client';
import { useEffect, useState, useRef } from 'react';
import Link from 'next/link';
import { ArrowLeft } from 'lucide-react';

export function ImprintContent() {
  const [isVisible, setIsVisible] = useState(false);
  const contentRef = useRef<HTMLElement>(null);

  useEffect(() => {
    const observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) {
          setIsVisible(true);
        }
      },
      { threshold: 0.1, rootMargin: '0px 0px -100px 0px' }
    );

    if (contentRef.current) {
      observer.observe(contentRef.current);
    }

    return () => observer.disconnect();
  }, []);

  return (
    <section ref={contentRef} className="relative py-32 overflow-hidden">
      <div className="absolute inset-0 bg-gradient-to-b from-background via-muted/5 to-muted/5" />

      <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]" />

      <div className="relative container mx-auto px-6">
        <div className={`mb-8 transition-all duration-1000 ease-out ${
          isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
        }`}>
          <Link 
            href="/"
            className="inline-flex items-center gap-2 px-4 py-2 bg-card/50 hover:bg-card border border-border/50 rounded-lg text-sm font-medium transition-all duration-300 hover:scale-105 backdrop-blur-sm"
          >
            <ArrowLeft className="w-4 h-4" />
            Back to Home
          </Link>
        </div>

        <div className={`text-center mb-16 transition-all duration-1000 ease-out ${
          isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
        }`}>
          <h1 className={`text-4xl md:text-5xl font-bold mb-6 bg-gradient-to-r from-foreground to-muted-foreground bg-clip-text text-transparent transition-all duration-1000 delay-200 ${
            isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
          }`}>
            Imprint
          </h1>
          <p className={`text-xl text-muted-foreground max-w-3xl mx-auto leading-relaxed transition-all duration-1000 delay-400 ${
            isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
          }`}>
            Legal information and contact details for PoloCloud
          </p>
        </div>

        <div className={`max-w-4xl mx-auto transition-all duration-1000 delay-600 ${
          isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
        }`}>
          <div className="bg-card/30 backdrop-blur-sm border border-border/30 rounded-2xl p-8 shadow-lg">
            <div className="space-y-8">
              <div>
                <h2 className="text-2xl font-bold text-foreground dark:text-white mb-4">
                  Company Information
                </h2>
                <div className="space-y-3 text-muted-foreground dark:text-white/60">
                  <p><strong className="text-foreground dark:text-white">Company:</strong> Lorem Ipsum GmbH</p>
                  <p><strong className="text-foreground dark:text-white">Developer:</strong> John Doe</p>
                  <p><strong className="text-foreground dark:text-white">Email:</strong> contact@loremipsum.de</p>
                  <p><strong className="text-foreground dark:text-white">Discord:</strong> discord.loremipsum.de</p>
                </div>
              </div>

              <div>
                <h2 className="text-2xl font-bold text-foreground dark:text-white mb-4">
                  Legal Notice
                </h2>
                <div className="space-y-4 text-muted-foreground dark:text-white/60 leading-relaxed">
                  <p>
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor 
                    incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis 
                    nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
                  </p>
                  <p>
                    Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore 
                    eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt 
                    in culpa qui officia deserunt mollit anim id est laborum.
                  </p>
                  <p>
                    Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium 
                    doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore 
                    veritatis et quasi architecto beatae vitae dicta sunt explicabo.
                  </p>
                </div>
              </div>

              <div>
                <h2 className="text-2xl font-bold text-foreground dark:text-white mb-4">
                  Disclaimer
                </h2>
                <div className="space-y-4 text-muted-foreground dark:text-white/60 leading-relaxed">
                  <p>
                    <strong className="text-foreground dark:text-white">IMPORTANT NOTICE:</strong> 
                    This is a sample disclaimer for demonstration purposes only.
                  </p>
                  <p>
                    Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, 
                    sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. 
                    Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet.
                  </p>
                </div>
              </div>

              <div>
                <h2 className="text-2xl font-bold text-foreground dark:text-white mb-4">
                  Contact Information
                </h2>
                <div className="space-y-3 text-muted-foreground dark:text-white/60">
                  <p><strong className="text-foreground dark:text-white">Email:</strong> contact@loremipsum.de</p>
                  <p><strong className="text-foreground dark:text-white">Discord:</strong> discord.loremipsum.de</p>
                  <p><strong className="text-foreground dark:text-white">GitHub:</strong> github.com/loremipsum</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
} 
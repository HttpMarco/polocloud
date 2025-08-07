'use client';
import { Clock, Sparkles, ArrowRight, Puzzle, Users, BarChart3, Code, ExternalLink, MessageCircle } from 'lucide-react';
import Link from 'next/link';
import { useEffect, useState, useRef } from 'react';

export function ComingSoonSection() {
  const [isVisible, setIsVisible] = useState(false);
  const [isBuilding, setIsBuilding] = useState(false);
  const sectionRef = useRef<HTMLElement>(null);

  useEffect(() => {
    const observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) {
          setIsVisible(true);
          setTimeout(() => {
            setIsBuilding(true);
          }, 500);
        }
      },
      { threshold: 0.1, rootMargin: '0px 0px -100px 0px' }
    );

    if (sectionRef.current) {
      observer.observe(sectionRef.current);
    }

    return () => observer.disconnect();
  }, []);

  const features = [
    {
      icon: <Puzzle className="w-6 h-6" />,
      title: 'Plugin Marketplace',
      description: 'Browse and install plugins directly from our marketplace.',
      color: 'text-blue-500',
      delay: 0
    },
    {
      icon: <Users className="w-6 h-6" />,
      title: 'Team Management',
      description: 'Manage your team with advanced permissions and roles.',
      color: 'text-green-500',
      delay: 1
    },
    {
      icon: <BarChart3 className="w-6 h-6" />,
      title: 'Advanced Analytics',
      description: 'Get detailed insights into your server performance.',
      color: 'text-purple-500',
      delay: 2
    }
  ];

  return (
    <section ref={sectionRef} className="py-16 md:py-24 relative overflow-hidden">
      <div className={`absolute top-0 left-0 right-0 h-48 bg-gradient-to-b from-background via-background/95 via-background/80 to-transparent transition-all duration-1500 ${
        isVisible ? 'opacity-100' : 'opacity-0'
      }`} />
      
      <div className={`absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)] transition-all duration-1500 delay-300 ${
        isVisible ? 'opacity-100' : 'opacity-0'
      }`} />

      <div className={`absolute top-20 left-10 w-32 h-32 bg-primary/5 rounded-full blur-3xl animate-pulse transition-all duration-1500 delay-600 ${
        isVisible ? 'opacity-100 scale-100 translate-y-0' : 'opacity-0 scale-0 translate-y-20'
      }`} />
      <div className={`absolute bottom-20 right-10 w-40 h-40 bg-primary/5 rounded-full blur-3xl animate-pulse delay-1000 transition-all duration-1500 delay-800 ${
        isVisible ? 'opacity-100 scale-100 translate-y-0' : 'opacity-0 scale-0 translate-y-20'
      }`} />
      
      <div className="container mx-auto px-6 max-w-6xl relative z-10">
        <div className={`text-center mb-16 transition-all duration-1500 ease-out ${
          isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-12'
        }`}>
          <div className={`flex items-center justify-center gap-3 mb-6 transition-all duration-1500 delay-400 ${
            isVisible ? 'opacity-100 scale-100 rotate-0' : 'opacity-0 scale-50 rotate-180'
          }`}>
            <div className={`flex items-center justify-center w-12 h-12 bg-primary/10 text-primary rounded-full border border-primary/20 transition-all duration-1500 delay-600 ${
              isVisible ? 'opacity-100 scale-100' : 'opacity-0 scale-0'
            }`}>
              <Sparkles className="w-6 h-6" />
            </div>
          </div>
          <h2 className={`text-3xl md:text-4xl lg:text-5xl font-black bg-gradient-to-r from-foreground via-primary to-foreground bg-clip-text text-transparent mb-6 leading-tight transition-all duration-1500 delay-600 ${
            isVisible ? 'opacity-100 translate-y-0 scale-100' : 'opacity-0 translate-y-8 scale-95'
          }`}>
            Coming Soon
          </h2>
          <p className={`text-base md:text-lg text-muted-foreground max-w-3xl mx-auto leading-relaxed transition-all duration-1500 delay-800 ${
            isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
          }`}>
            We&apos;re working hard to bring you amazing new features. Stay tuned for exciting updates!
          </p>
        </div>

        <div className={`grid gap-8 md:grid-cols-2 lg:grid-cols-3 mb-16 transition-all duration-1500 delay-1000 ${
          isBuilding ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-16'
        }`}>
          {features.map((feature, index) => (
            <div 
              key={feature.title}
              className={`group bg-card/50 hover:bg-card border border-border/50 hover:border-border rounded-xl p-6 transition-all duration-500 hover:shadow-lg backdrop-blur-sm hover:scale-[1.02] transform relative overflow-hidden ${
                isBuilding ? 'opacity-100 translate-y-0 scale-100' : 'opacity-0 translate-y-20 scale-90'
              }`}
              style={{ 
                transitionDelay: `${(index + 1) * 200}ms`,
                animation: isBuilding ? `bounceIn 0.8s ease-out ${(index + 1) * 200}ms both` : 'none'
              }}
            >
              <div className={`absolute inset-0 bg-gradient-to-br from-primary/5 via-transparent to-primary/5 opacity-0 group-hover:opacity-100 transition-opacity duration-300 ${
                isBuilding ? 'opacity-0' : 'opacity-0'
              }`} />
              
              <div className="relative z-10">
                <div className={`w-12 h-12 bg-card/60 rounded-lg flex items-center justify-center mb-4 group-hover:scale-110 transition-transform duration-300 ${feature.color} ${
                  isBuilding ? 'opacity-100 scale-100' : 'opacity-0 scale-50'
                }`}
                style={{ transitionDelay: `${(index + 1) * 200 + 300}ms` }}
                >
                  {feature.icon}
                </div>
                <h3 className={`text-lg font-bold text-foreground mb-2 transition-all duration-500 ${
                  isBuilding ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'
                }`}
                style={{ transitionDelay: `${(index + 1) * 200 + 400}ms` }}
                >
                  {feature.title}
                </h3>
                <p className={`text-sm text-muted-foreground transition-all duration-500 ${
                  isBuilding ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'
                }`}
                style={{ transitionDelay: `${(index + 1) * 200 + 500}ms` }}
                >
                  {feature.description}
                </p>
              </div>
            </div>
          ))}
        </div>

        <div className={`text-center transition-all duration-1500 delay-1200 ${
          isBuilding ? 'opacity-100 translate-y-0 scale-100' : 'opacity-0 translate-y-12 scale-95'
        }`}>
          <div className={`bg-gradient-to-r from-primary/10 to-primary/5 border border-primary/20 rounded-2xl p-8 max-w-2xl mx-auto transition-all duration-1000 delay-1400 ${
            isBuilding ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
          }`}>
            <h3 className={`text-2xl font-bold text-foreground dark:text-white mb-4 transition-all duration-500 delay-1600 ${
              isBuilding ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'
            }`}>
              Join Our Community
            </h3>
            <p className={`text-muted-foreground dark:text-white/70 mb-6 transition-all duration-500 delay-1800 ${
              isBuilding ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'
            }`}>
              We&apos;re building a community of developers and users who are excited about the future of Polocloud. Join us on Discord to stay updated and get involved!
            </p>
            <a
              href="https://discord.gg/polocloud"
              target="_blank"
              rel="noopener noreferrer"
              className={`inline-flex items-center gap-3 px-8 py-4 bg-gradient-to-r from-primary to-primary/90 hover:from-primary/90 hover:to-primary text-primary-foreground rounded-xl font-semibold text-lg transition-all duration-300 shadow-[0_0_20px_rgba(0,120,255,0.3)] hover:shadow-[0_0_30px_rgba(0,120,255,0.5)] hover:scale-105 group transition-all duration-500 delay-2000 ${
                isBuilding ? 'opacity-100 translate-y-0 scale-100' : 'opacity-0 translate-y-4 scale-95'
              }`}
            >
              <MessageCircle className="w-5 h-5 group-hover:rotate-12 transition-transform" />
              Join Discord
              <ExternalLink className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
            </a>
          </div>
        </div>
      </div>

      <div className={`absolute bottom-0 left-0 right-0 h-48 bg-gradient-to-t from-background via-background/95 via-background/80 to-transparent transition-all duration-1500 delay-1600 ${
        isVisible ? 'opacity-100' : 'opacity-0'
      }`} />

      <style jsx>{`
        @keyframes bounceIn {
          0% {
            opacity: 0;
            transform: translateY(30px) scale(0.8);
          }
          50% {
            opacity: 1;
            transform: translateY(-10px) scale(1.05);
          }
          100% {
            opacity: 1;
            transform: translateY(0) scale(1);
          }
        }
      `}</style>
    </section>
  );
} 
'use client';
import { useEffect, useState, useRef } from 'react';
import { Server, Zap, Shield, Users, Cloud, Code } from 'lucide-react';

interface FeatureCard {
  icon: React.ReactNode;
  title: string;
  description: string;
  color: string;
}

const features: FeatureCard[] = [
  {
    icon: <Server className="w-6 h-6" />,
    title: 'Multi Platform',
    description: 'Set up your server with different platforms, from Spigot to Pumpkin, everything is included.',
    color: 'text-blue-400'
  },
  {
    icon: <Zap className="w-6 h-6" />,
    title: 'Easy Setup',
    description: 'Get your server running in minutes with our intuitive setup process. No complex configuration required.',
    color: 'text-yellow-400'
  },
  {
    icon: <Shield className="w-6 h-6" />,
    title: 'API Documentation',
    description: 'We have a very clear and powerful API that you can view in the documentation.',
    color: 'text-green-400'
  },
  {
    icon: <Users className="w-6 h-6" />,
    title: 'Fast Support',
    description: 'If you have any questions about the cloud or need help with the cloud, we are here to assist you.',
    color: 'text-purple-400'
  },
  {
    icon: <Cloud className="w-6 h-6" />,
    title: 'Developer Friendly',
    description: 'Easy to work with us and easy cloud API. Comprehensive developer tools and documentation to integrate with your workflow.',
    color: 'text-indigo-400'
  },
  {
    icon: <Code className="w-6 h-6" />,
    title: 'Multilingual',
    description: 'We have Crowdin and support many languages. Our platform is available in multiple languages for global accessibility.',
    color: 'text-orange-400'
  }
];

export function AboutSection() {
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
          }, 300);
        }
      },
      { threshold: 0.1, rootMargin: '0px 0px -100px 0px' }
    );

    if (sectionRef.current) {
      observer.observe(sectionRef.current);
    }

    return () => observer.disconnect();
  }, []);

  return (
    <section ref={sectionRef} className="relative py-32 overflow-hidden">
      <div className={`absolute top-0 left-0 right-0 h-48 bg-gradient-to-b from-background via-background/95 via-background/80 to-transparent transition-all duration-1000 ${
        isVisible ? 'opacity-100' : 'opacity-0'
      }`} />
      <div className={`absolute inset-0 bg-gradient-to-b from-background via-muted/5 to-muted/5 transition-all duration-1000 delay-200 ${
        isVisible ? 'opacity-100' : 'opacity-0'
      }`} />

      <div className={`absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)] transition-all duration-1000 delay-400 ${
        isVisible ? 'opacity-100' : 'opacity-0'
      }`} />

      <div className={`absolute top-20 left-10 w-32 h-32 bg-primary/5 rounded-full blur-3xl animate-pulse transition-all duration-1000 delay-600 ${
        isVisible ? 'opacity-100 scale-100' : 'opacity-0 scale-50'
      }`} />
      <div className={`absolute bottom-20 right-10 w-40 h-40 bg-primary/5 rounded-full blur-3xl animate-pulse delay-1000 transition-all duration-1000 delay-800 ${
        isVisible ? 'opacity-100 scale-100' : 'opacity-0 scale-50'
      }`} />

      <div className="relative container mx-auto px-6">
        <div className={`text-center mb-20 transition-all duration-1000 ease-out ${
          isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
        }`}>
          <h2 className={`text-3xl md:text-4xl lg:text-5xl font-black mb-8 bg-gradient-to-r from-foreground via-primary to-foreground bg-clip-text text-transparent transition-all duration-1000 delay-200 tracking-tight leading-tight ${
            isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
          }`}>
            About PoloCloud
          </h2>
          <p className={`text-base md:text-lg text-muted-foreground max-w-4xl mx-auto leading-relaxed transition-all duration-1000 delay-400 ${
            isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
          }`}>
            The ultimate cloud platform for Minecraft server hosting. 
            <span className="block mt-2 text-sm md:text-base font-normal">
              Built for performance, designed for simplicity, and trusted by thousands of server owners worldwide.
            </span>
          </p>
        </div>

        <div className={`grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8 transition-all duration-1000 delay-600 ${
          isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
        }`}>
          {features.map((feature, index) => (
            <div
              key={feature.title}
              className={`bg-card/30 backdrop-blur-sm border border-border/50 rounded-xl p-6 hover:bg-card/40 hover:border-border/70 transition-all duration-300 hover:shadow-lg hover:scale-[1.02] group relative overflow-hidden ${
                isBuilding ? 'opacity-100 translate-y-0 scale-100' : 'opacity-0 translate-y-8 scale-95'
              }`}
              style={{ 
                transitionDelay: `${(index + 1) * 150}ms`,
                transform: isBuilding ? 'translateY(0) scale(1)' : 'translateY(20px) scale(0.95)'
              }}
            >
              <div className={`absolute inset-0 bg-gradient-to-br from-primary/5 via-transparent to-primary/5 opacity-0 group-hover:opacity-100 transition-opacity duration-300 ${
                isBuilding ? 'opacity-0' : 'opacity-0'
              }`} />
              
              <div className="relative z-10">
                <div className={`w-12 h-12 bg-card/60 rounded-lg flex items-center justify-center mb-4 group-hover:scale-110 transition-transform duration-300 ${feature.color} ${
                  isBuilding ? 'opacity-100 scale-100' : 'opacity-0 scale-50'
                }`}
                style={{ transitionDelay: `${(index + 1) * 150 + 200}ms` }}
                >
                  {feature.icon}
                </div>
                <h3 className={`font-black text-foreground dark:text-white mb-3 text-lg md:text-xl leading-tight transition-all duration-500 ${
                  isBuilding ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'
                }`}
                style={{ transitionDelay: `${(index + 1) * 150 + 300}ms` }}
                >
                  {feature.title}
                </h3>
                <p className={`text-sm md:text-base text-muted-foreground dark:text-white/70 leading-relaxed font-light transition-all duration-500 ${
                  isBuilding ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'
                }`}
                style={{ transitionDelay: `${(index + 1) * 150 + 400}ms` }}
                >
                  {feature.description}
                </p>
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className={`absolute bottom-0 left-0 right-0 h-48 bg-gradient-to-t from-background via-background/95 via-background/80 to-transparent transition-all duration-1000 delay-1000 ${
        isVisible ? 'opacity-100' : 'opacity-0'
      }`} />
    </section>
  );
} 
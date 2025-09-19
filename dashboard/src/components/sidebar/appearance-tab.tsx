'use client'

import { useState, useEffect } from "react";
import { Moon, Sparkles, Globe, Palette, TreePine, Flame, Monitor, Zap, Heart, Coffee, Mountain, Star } from "lucide-react";

export function AppearanceTab() {
  const [selectedTheme, setSelectedTheme] = useState('dark');

  const themes = [
    { 
      id: 'dark', 
      name: 'Dark', 
      icon: Moon,
      description: 'Standard dark theme',
      colors: ['#0f0f0f', '#1f1f1f', '#ffffff']
    },
    { 
      id: 'darker', 
      name: 'Darker', 
      icon: Sparkles,
      description: 'Ultra dark theme',
      colors: ['#000000', '#080808', '#ffffff']
    },
    { 
      id: 'ocean', 
      name: 'Ocean', 
      icon: Globe,
      description: 'Dark blue ocean theme',
      colors: ['#0a1a2a', '#1a2a3a', '#00d4ff']
    },
    { 
      id: 'purple', 
      name: 'Purple', 
      icon: Palette,
      description: 'Elegant purple theme',
      colors: ['#1a0a2a', '#2a1a3a', '#a855f7']
    },
    { 
      id: 'forest', 
      name: 'Forest', 
      icon: TreePine,
      description: 'Dark green nature theme',
      colors: ['#0a2a0a', '#1a3a1a', '#22c55e']
    },
    { 
      id: 'sunset', 
      name: 'Sunset', 
      icon: Flame,
      description: 'Warm orange-red theme',
      colors: ['#2a0a0a', '#3a1a1a', '#f97316']
    },
    { 
      id: 'neon', 
      name: 'Neon', 
      icon: Zap,
      description: 'Electric neon cyberpunk theme',
      colors: ['#0a0a0a', '#1a0a1a', '#00ff88']
    },
    { 
      id: 'rose', 
      name: 'Rose', 
      icon: Heart,
      description: 'Elegant rose gold theme',
      colors: ['#2a0a1a', '#3a1a2a', '#f43f5e']
    },
    { 
      id: 'coffee', 
      name: 'Coffee', 
      icon: Coffee,
      description: 'Warm coffee brown theme',
      colors: ['#1a0f0a', '#2a1f1a', '#d2691e']
    },
    { 
      id: 'mountain', 
      name: 'Mountain', 
      icon: Mountain,
      description: 'Cool mountain blue theme',
      colors: ['#0a1a2a', '#1a2a3a', '#3b82f6']
    },
    { 
      id: 'cosmic', 
      name: 'Cosmic', 
      icon: Star,
      description: 'Deep space cosmic theme',
      colors: ['#0a0a2a', '#1a1a3a', '#8b5cf6']
    }
  ];

  const applyTheme = (themeId: string) => {
    const allThemes = ['dark', 'darker', 'ocean', 'purple', 'forest', 'sunset', 'neon', 'rose', 'coffee', 'mountain', 'cosmic', 'light'];
    document.documentElement.classList.remove(...allThemes);

    if (themeId !== 'light' && themeId !== 'custom') {
      document.documentElement.classList.add(themeId);
    }

    localStorage.setItem('theme', themeId);
    setSelectedTheme(themeId);
    window.dispatchEvent(new CustomEvent('theme-changed', { detail: { theme: themeId } }));
  };

  useEffect(() => {
    const savedTheme = localStorage.getItem('theme') || 'dark';
    applyTheme(savedTheme);
  }, []);

  return (
    <div className="space-y-6">
      <div>
        <h3 className="text-2xl font-bold mb-2">Theme Selection</h3>
        <p className="text-muted-foreground">
          Choose your preferred theme. Dark and Darker themes are available.
        </p>
      </div>

      <div className="grid grid-cols-2 gap-4">
        {themes.map((theme) => (
          <div
            key={theme.id}
            className={`relative p-4 rounded-xl border-2 transition-all duration-300 cursor-pointer group ${
              selectedTheme === theme.id
                ? 'border-primary bg-primary/10 shadow-lg shadow-primary/20'
                : 'border-border bg-muted/20 hover:border-primary/50 hover:bg-primary/5 hover:shadow-md'
            }`}
            onClick={() => {
              if (theme.id !== 'light' && theme.id !== 'custom') {
                applyTheme(theme.id);
              }
            }}
          >
            <div className="flex h-2 mb-3 rounded-full overflow-hidden">
              {theme.colors.map((color, index) => (
                <div 
                  key={index} 
                  className="flex-1" 
                  style={{ backgroundColor: color }}
                />
              ))}
            </div>

            <div className="flex items-center gap-3">
              <div className={`p-3 rounded-lg transition-all duration-200 ${
                selectedTheme === theme.id
                  ? 'bg-primary/20 border border-primary/30'
                  : 'bg-muted/30 border border-border/30 group-hover:bg-primary/10 group-hover:border-primary/20'
              }`}>
                <theme.icon className={`w-5 h-5 transition-colors duration-200 ${
                  selectedTheme === theme.id
                    ? 'text-primary'
                    : 'text-muted-foreground group-hover:text-primary'
                }`} />
              </div>
              
              <div className="flex-1 min-w-0">
                <h4 className={`font-semibold transition-colors duration-200 ${
                  selectedTheme === theme.id
                    ? 'text-primary'
                    : 'text-foreground group-hover:text-primary'
                }`}>
                  {theme.name}
                </h4>
                <p className="text-sm text-muted-foreground truncate">
                  {theme.description}
                </p>
              </div>

              {selectedTheme === theme.id && (
                <div className="w-3 h-3 bg-primary rounded-full animate-pulse" />
              )}
            </div>

            <div className={`absolute inset-0 rounded-xl transition-all duration-300 ${
              selectedTheme === theme.id
                ? 'ring-2 ring-primary/20'
                : 'group-hover:ring-2 group-hover:ring-primary/10'
            }`} />
          </div>
        ))}
      </div>

      <div className="p-4 rounded-lg border border-border bg-muted/20">
        <h4 className="text-lg font-semibold mb-2 flex items-center gap-2">
          <Monitor className="h-5 w-5" />
          Current Theme: {selectedTheme === 'dark' ? 'Dark' : selectedTheme === 'darker' ? 'Darker' : selectedTheme === 'ocean' ? 'Ocean' : selectedTheme === 'purple' ? 'Purple' : selectedTheme === 'forest' ? 'Forest' : selectedTheme === 'sunset' ? 'Sunset' : 'Dark'}
        </h4>
        <p className="text-sm text-muted-foreground">
          {selectedTheme === 'dark' 
            ? 'The Dark theme is currently active.' 
            : selectedTheme === 'darker'
            ? 'The Darker theme is currently active.'
            : selectedTheme === 'ocean'
            ? 'The Ocean theme is currently active.'
            : selectedTheme === 'purple'
            ? 'The Purple theme is currently active.'
            : selectedTheme === 'forest'
            ? 'The Forest theme is currently active.'
            : selectedTheme === 'sunset'
            ? 'The Sunset theme is currently active.'
            : 'The Dark theme is currently active.'
          } Other themes will be available in future updates.
        </p>
      </div>
    </div>
  );
}

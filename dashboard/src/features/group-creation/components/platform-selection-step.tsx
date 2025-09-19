'use client';

import { motion } from 'framer-motion';
import Image from 'next/image';
import { Settings } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { getPlatformIcon } from '@/lib/platform-icons';
import { PlatformSelectionStepProps } from '../types/group-creation.types';

export function PlatformSelectionStep({ platforms, selectedPlatform, onPlatformSelect }: PlatformSelectionStepProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6, delay: 0.2 }}
    >
      <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm">
        <CardHeader>
          <CardTitle className="flex items-center gap-2 text-foreground">
            <Settings className="w-5 h-5 text-[oklch(75.54% 0.1534 231.639)]" />
            Choose Platform
          </CardTitle>
        </CardHeader>
        
        <CardContent className="space-y-6">
          
          {}
          <div className="flex items-center gap-2 mb-3">
            <div className="w-2 h-2 rounded-full bg-[oklch(75.54% 0.1534 231.639)]"></div>
            <span className="text-sm font-medium text-muted-foreground uppercase tracking-wide">
              Platform selection
            </span>
          </div>
          
          {}
          <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
            {platforms.map((platform) => (
              <motion.div
                key={platform.name}
                whileHover={{ scale: 1.02, y: -2 }}
                whileTap={{ scale: 0.98 }}
                transition={{ duration: 0.15 }}
              >
                <Card
                  className={`cursor-pointer transition-all duration-200 border-2 ${
                    selectedPlatform === platform.name
                      ? 'border-[oklch(0.7554_0.1534_231.639)] bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] shadow-[0_0_25px_rgba(117,84,231,0.6)] ring-2 ring-[oklch(0.7554_0.1534_231.639,0.3)]'
                      : 'border-border/50 hover:border-[oklch(75.54%_0.1534_231.639,0.5)] hover:bg-gradient-to-r hover:from-[oklch(75.54%_0.1534_231.639,0.05)] hover:to-[oklch(75.54%_0.1534_231.639,0.02)]'
                  }`}
                  onClick={() => onPlatformSelect(platform.name)}
                >
                  <CardContent className="p-3 text-center space-y-2">
                    <div className="flex justify-center">
                      <Image
                        src={getPlatformIcon(platform.name)}
                        alt={platform.name}
                        width={40}
                        height={40}
                        className="w-10 h-10 rounded-lg"
                        onError={(e) => {
                          e.currentTarget.src = '/placeholder.png';
                        }}
                      />
                    </div>
                    <div>
                      <p className="font-medium text-foreground text-sm capitalize truncate">{platform.name}</p>
                      <p className="text-xs text-muted-foreground">{platform.versions.length} versions</p>
                    </div>
                  </CardContent>
                </Card>
              </motion.div>
            ))}
          </div>
          
          {}
          <div className="text-center">
            <p className="text-xs text-muted-foreground italic">
              Select a platform to continue. Each platform has different versions and capabilities.
            </p>
          </div>
        </CardContent>
      </Card>
    </motion.div>
  );
}

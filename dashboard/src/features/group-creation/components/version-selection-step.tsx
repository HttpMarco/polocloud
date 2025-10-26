'use client';

import { motion } from 'framer-motion';
import { Play } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { VersionSelectionStepProps } from '../types/group-creation.types';

export function VersionSelectionStep({ selectedPlatform, selectedVersion, onVersionSelect }: VersionSelectionStepProps) {
  if (!selectedPlatform) {
    return null;
  }

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6, delay: 0.2 }}
    >
      <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm">
        <CardHeader>
          <CardTitle className="flex items-center gap-2 text-foreground">
            <Play className="w-5 h-5 text-[oklch(75.54% 0.1534 231.639)]" />
            Version Selection
          </CardTitle>
        </CardHeader>
        
        <CardContent className="space-y-6">
          
          {}
          <div className="flex items-center gap-2 mb-3">
            <div className="w-2 h-2 rounded-full bg-[oklch(75.54% 0.1534 231.639)]"></div>
            <span className="text-sm font-medium text-muted-foreground uppercase tracking-wide">
              Available versions for {selectedPlatform.name}
            </span>
          </div>
          
          {}
          <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
            {selectedPlatform.versions.map((version) => (
              <motion.div
                key={version.version}
                whileHover={{ scale: 1.02, y: -2 }}
                whileTap={{ scale: 0.98 }}
                transition={{ duration: 0.15 }}
              >
                <Card
                  className={`cursor-pointer transition-all duration-200 border-2 ${
                    selectedVersion === version.version
                      ? 'border-[oklch(0.7554_0.1534_231.639)] bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] shadow-[0_0_25px_rgba(117,84,231,0.6)] ring-2 ring-[oklch(0.7554_0.1534_231.639,0.3)]'
                      : 'border-border/50 hover:border-[oklch(75.54%_0.1534_231.639,0.5)] hover:bg-gradient-to-r hover:from-[oklch(75.54%_0.1534_231.639,0.05)] hover:to-[oklch(75.54%_0.1534_231.639,0.02)]'
                  }`}
                  onClick={() => onVersionSelect(version.version)}
                >
                  <CardContent className="p-3 text-center space-y-2">
                                         <div className="w-8 h-8 mx-auto rounded-lg bg-gradient-to-br from-[oklch(75.54%_0.1534_231.639,0.2)] to-[oklch(75.54%_0.1534_231.639,0.1)] flex items-center justify-center border border-[oklch(75.54%_0.1534_231.639,0.3)]">
                       <span className="text-xs font-bold text-[oklch(75.54% 0.1534 231.639)]">v</span>
                     </div>
                    <p className="font-medium text-foreground text-sm">{version.version}</p>
                  </CardContent>
                </Card>
              </motion.div>
            ))}
          </div>
          
          {}
          <div className="text-center">
            <p className="text-xs text-muted-foreground italic">
              Select a version to continue. Different versions may have different features and compatibility.
            </p>
          </div>
        </CardContent>
      </Card>
    </motion.div>
  );
}

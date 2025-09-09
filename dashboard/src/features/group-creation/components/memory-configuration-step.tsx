'use client';

import { motion } from 'framer-motion';
import { Server } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { MemoryConfigurationStepProps } from '../types/group-creation.types';

export function MemoryConfigurationStep({ formData, onMemoryChange }: MemoryConfigurationStepProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6, delay: 0.2 }}
    >
      <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm">
        <CardHeader>
          <CardTitle className="flex items-center gap-2 text-foreground">
            <Server className="w-5 h-5 text-[oklch(75.54% 0.1534 231.639)]" />
            Memory Configuration
          </CardTitle>
        </CardHeader>
        
        <CardContent className="space-y-6">
          
          {}
          <div className="space-y-6">
            {}
            <div className="space-y-4">
              <div className="flex items-center gap-2 mb-3">
                <div className="w-2 h-2 rounded-full bg-[oklch(75.54% 0.1534 231.639)]"></div>
                <span className="text-sm font-medium text-muted-foreground uppercase tracking-wide">Minimum Memory</span>
              </div>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
                {['512MB', '1024MB', '2048MB', '3072MB'].map((memory) => (
                  <motion.div
                    key={memory}
                    whileHover={{ scale: 1.02, y: -2 }}
                    whileTap={{ scale: 0.98 }}
                    transition={{ duration: 0.15 }}
                  >
                    <Card
                      className={`cursor-pointer transition-all duration-200 border-2 ${
                        formData.memory.min === memory
                          ? 'border-[oklch(0.7554_0.1534_231.639)] bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] shadow-[0_0_25px_rgba(117,84,231,0.6)] ring-2 ring-[oklch(0.7554_0.1534_231.639,0.3)]'
                          : 'border-border/50 hover:border-[oklch(75.54%_0.1534_231.639,0.5)] hover:bg-gradient-to-r hover:from-[oklch(75.54%_0.1534_231.639,0.05)] hover:to-[oklch(75.54%_0.1534_231.639,0.02)]'
                      }`}
                      onClick={() => onMemoryChange('min', memory)}
                    >
                      <CardContent className="p-3 text-center space-y-2">
                        <div className="w-8 h-8 mx-auto rounded-lg bg-gradient-to-br from-[oklch(75.54%_0.1534_231.639,0.2)] to-[oklch(75.54%_0.1534_231.639,0.1)] flex items-center justify-center border border-[oklch(75.54%_0.1534_231.639,0.3)]">
                          <span className="text-xs font-bold text-[oklch(75.54% 0.1534 231.639)]">MB</span>
                        </div>
                        <p className="font-medium text-foreground text-sm">{memory}</p>
                      </CardContent>
                    </Card>
                  </motion.div>
                ))}
              </div>
            </div>

            {}
            <div className="space-y-4">
              <div className="flex items-center gap-2 mb-3">
                <div className="w-2 h-2 rounded-full bg-[oklch(75.54% 0.1534 231.639)]"></div>
                <span className="text-sm font-medium text-muted-foreground uppercase tracking-wide">Maximum Memory</span>
              </div>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
                {['512MB', '1024MB', '2048MB', '3072MB'].map((memory) => (
                  <motion.div
                    key={memory}
                    whileHover={{ scale: 1.02, y: -2 }}
                    whileTap={{ scale: 0.98 }}
                    transition={{ duration: 0.15 }}
                  >
                    <Card
                      className={`cursor-pointer transition-all duration-200 border-2 ${
                        formData.memory.max === memory
                          ? 'border-[oklch(0.7554_0.1534_231.639)] bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] shadow-[0_0_25px_rgba(117,84,231,0.6)] ring-2 ring-[oklch(0.7554_0.1534_231.639,0.3)]'
                          : 'border-border/50 hover:border-[oklch(75.54%_0.1534_231.639,0.5)] hover:bg-gradient-to-r hover:from-[oklch(75.54%_0.1534_231.639,0.05)] hover:to-[oklch(75.54%_0.1534_231.639,0.02)]'
                      }`}
                      onClick={() => onMemoryChange('max', memory)}
                    >
                      <CardContent className="p-3 text-center space-y-2">
                        <div className="w-8 h-8 mx-auto rounded-lg bg-gradient-to-br from-[oklch(75.54%_0.1534_231.639,0.2)] to-[oklch(75.54%_0.1534_231.639,0.1)] flex items-center justify-center border border-[oklch(75.54%_0.1534_231.639,0.3)]">
                          <span className="text-xs font-bold text-[oklch(75.54% 0.1534 231.639)]">MB</span>
                        </div>
                        <p className="font-medium text-foreground text-sm">{memory}</p>
                      </CardContent>
                    </Card>
                  </motion.div>
                ))}
              </div>
            </div>
          </div>

          {}
          <div className="space-y-4 pt-4 border-t border-border/30">
            <div className="flex items-center gap-2 mb-3">
              <div className="w-2 h-2 rounded-full bg-[oklch(75.54% 0.1534 231.639)]"></div>
              <span className="text-sm font-medium text-muted-foreground uppercase tracking-wide">Custom Memory</span>
            </div>
            
            {}
            {formData.memory.min && formData.memory.max && 
             parseInt(formData.memory.min.replace(/\D/g, '')) > parseInt(formData.memory.max.replace(/\D/g, '')) && (
              <div className="bg-gradient-to-r from-red-500/10 to-red-500/5 border border-red-500/20 rounded-lg px-4 py-3">
                <div className="flex items-center gap-2">
                  <div className="w-2 h-2 rounded-full bg-red-500"></div>
                  <span className="text-sm text-red-600 font-medium">
                    Min memory cannot be greater than max memory
                  </span>
                </div>
              </div>
            )}
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="space-y-3">
                <Label htmlFor="minMemory" className="text-sm font-medium text-muted-foreground">Minimum Memory</Label>
                                 <Input
                   id="minMemory"
                   placeholder="e.g., 512 (auto-adds MB) or 1GB, 2GB..."
                   value={formData.memory.min}
                   onChange={(e) => {
                     let value = e.target.value;
                     if (/^\d+$/.test(value)) {
                       value = value + 'MB';
                     }
                     onMemoryChange('min', value);
                   }}
                   className="h-10 border-2 focus:border-[oklch(75.54% 0.1534 231.639)] transition-all duration-200 bg-background/50"
                 />
              </div>
              <div className="space-y-3">
                <Label htmlFor="maxMemory" className="text-sm font-medium text-muted-foreground">Maximum Memory</Label>
                                 <Input
                   id="maxMemory"
                   placeholder="e.g., 1024 (auto-adds MB) or 1GB, 2GB..."
                   value={formData.memory.max}
                   onChange={(e) => {
                     let value = e.target.value;
                     if (/^\d+$/.test(value)) {
                       value = value + 'MB';
                     }
                     onMemoryChange('max', value);
                   }}
                   className="h-10 border-2 focus:border-[oklch(75.54% 0.1534 231.639)] transition-all duration-200 bg-background/50"
                 />
              </div>
            </div>
            
            <p className="text-xs text-muted-foreground italic">
              Enter memory values in MB or GB format. Numbers without units will automatically get &quot;MB&quot; added (e.g., 512 → 512MB, 1GB, 2GB)
            </p>
          </div>
        </CardContent>
      </Card>
    </motion.div>
  );
}





'use client';

import { motion } from 'framer-motion';
import { Shield } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Switch } from '@/components/ui/switch';
import { AdvancedSettingsStepProps } from '../types/group-creation.types';

export function AdvancedSettingsStep({ formData, onAdvancedChange }: AdvancedSettingsStepProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6, delay: 0.2 }}
    >
      <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm">
        <CardHeader>
          <CardTitle className="flex items-center gap-2 text-foreground">
            <Shield className="w-5 h-5 text-[oklch(75.54% 0.1534 231.639)]" />
            Advanced Settings
          </CardTitle>
        </CardHeader>
        
        <CardContent className="space-y-6">
          
          {}
          <div className="space-y-4">
            <div className="flex items-center gap-2 mb-3">
              <div className="w-2 h-2 rounded-full bg-[oklch(75.54% 0.1534 231.639)]"></div>
              <span className="text-sm font-medium text-muted-foreground uppercase tracking-wide">Service Configuration</span>
            </div>
            
                         <div className="space-y-3">
               <Label htmlFor="percentageNewService" className="text-sm font-medium text-muted-foreground">Percentage New Service</Label>
               <Input
                 id="percentageNewService"
                 placeholder="e.g., 80, 90, 100..."
                 value={formData.advanced.percentageNewService}
                 onChange={(e) => onAdvancedChange('percentageNewService', e.target.value)}
                 className="h-10 border-2 focus:border-[oklch(75.54% 0.1534 231.639)] transition-all duration-200 bg-background/50"
               />
               <p className="text-xs text-muted-foreground italic">
                 Percentage of services that should be new instances
               </p>
             </div>
             
             <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
               <div className="space-y-3">
                 <Label htmlFor="minServicesOnline" className="text-sm font-medium text-muted-foreground">Min Services Online</Label>
                 <Input
                   id="minServicesOnline"
                   placeholder="e.g., 1, 2, 3..."
                   value={formData.advanced.minServicesOnline}
                   onChange={(e) => onAdvancedChange('minServicesOnline', e.target.value)}
                   className="h-10 border-2 focus:border-[oklch(75.54% 0.1534 231.639)] transition-all duration-200 bg-background/50"
                 />
                 <p className="text-xs text-muted-foreground italic">
                   Minimum number of services that should be online
                 </p>
               </div>
               
               <div className="space-y-3">
                 <Label htmlFor="maxServicesOnline" className="text-sm font-medium text-muted-foreground">Max Services Online</Label>
                 <Input
                   id="maxServicesOnline"
                   placeholder="e.g., 5, 10, 20..."
                   value={formData.advanced.maxServicesOnline}
                   onChange={(e) => onAdvancedChange('maxServicesOnline', e.target.value)}
                   className="h-10 border-2 focus:border-[oklch(75.54% 0.1534 231.639)] transition-all duration-200 bg-background/50"
                 />
                 <p className="text-xs text-muted-foreground italic">
                   Maximum number of services that can be online
                 </p>
               </div>
             </div>
            
            {}
            {formData.advanced.minServicesOnline && formData.advanced.maxServicesOnline && 
             parseInt(formData.advanced.minServicesOnline) > parseInt(formData.advanced.maxServicesOnline) && (
              <div className="bg-gradient-to-r from-red-500/10 to-red-500/5 border border-red-500/20 rounded-lg px-4 py-3">
                <div className="flex items-center gap-3">
                  <div className="w-2 h-2 rounded-full bg-red-500"></div>
                  <span className="text-sm text-red-600 font-medium">
                    Min services cannot be greater than max services
                  </span>
                </div>
              </div>
            )}
          </div>

          {}
          <div className="space-y-4 pt-4 border-t border-border/30">
            <div className="flex items-center gap-2 mb-3">
              <div className="w-2 h-2 rounded-full bg-[oklch(75.54% 0.1534 231.639)]"></div>
              <span className="text-sm font-medium text-muted-foreground uppercase tracking-wide">Feature Toggles</span>
            </div>
            
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <Label htmlFor="fallback" className="text-sm font-medium text-foreground">Fallback Mode</Label>
                  <p className="text-xs text-muted-foreground">
                    Enable fallback to offline services when needed
                  </p>
                </div>
                                 <Switch
                   id="fallback"
                   checked={formData.advanced.fallback}
                   onCheckedChange={(checked) => onAdvancedChange('fallback', checked)}
                   className="data-[state=checked]:bg-[oklch(0.7554_0.1534_231.639)] data-[state=unchecked]:bg-muted"
                 />
              </div>
              
              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <Label htmlFor="static" className="text-sm font-medium text-foreground">Static Mode</Label>
                  <p className="text-xs text-muted-foreground">
                    Keep services running without auto-scaling
                  </p>
                </div>
                                 <Switch
                   id="static"
                   checked={formData.advanced.static}
                   onCheckedChange={(checked) => onAdvancedChange('static', checked)}
                   className="data-[state=checked]:bg-[oklch(0.7554_0.1534_231.639)] data-[state=unchecked]:bg-muted"
                 />
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
    </motion.div>
  );
}

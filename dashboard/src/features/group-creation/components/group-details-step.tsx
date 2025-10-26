'use client';

import { motion } from 'framer-motion';
import { Server } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { GroupDetailsStepProps } from '../types/group-creation.types';

export function GroupDetailsStep({ formData, onNameChange }: GroupDetailsStepProps) {
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
            Group Configuration
          </CardTitle>
        </CardHeader>
        
        <CardContent className="space-y-6">
          
          {}
          <div className="flex items-center gap-2 mb-3">
            <div className="w-2 h-2 rounded-full bg-[oklch(75.54% 0.1534 231.639)]"></div>
            <span className="text-sm font-medium text-muted-foreground uppercase tracking-wide">
              Group details
            </span>
          </div>
          
          {}
          <div className="space-y-3">
            <Label htmlFor="groupName" className="text-sm font-medium text-foreground">Group Name</Label>
            <Input
              id="groupName"
              placeholder="e.g., lobby, proxy"
              value={formData.name}
              onChange={(e) => onNameChange(e.target.value)}
              className="h-10 border-2 focus:border-[oklch(75.54% 0.1534 231.639)] transition-all duration-200 bg-background/50"
            />
            <p className="text-xs text-muted-foreground italic">
              Choose a unique, descriptive name for your service group
            </p>
          </div>
        </CardContent>
      </Card>
    </motion.div>
  );
}





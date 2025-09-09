'use client';

import { motion } from 'framer-motion';
import Image from 'next/image';
import { Server } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Label } from '@/components/ui/label';
import { Badge } from '@/components/ui/badge';
import { getPlatformIcon } from '@/lib/platform-icons';
import { GroupPreviewProps } from '../types/group-creation.types';

export function GroupPreview({ formData, currentStep }: GroupPreviewProps) {
  return (
    <div className="lg:col-span-1">
      <div className="sticky top-8">
        <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm">
          <CardHeader className="pb-4">
            <CardTitle className="flex items-center space-x-2 text-lg font-semibold">
              <div className="w-8 h-8 rounded-full bg-gradient-to-br from-[oklch(75.54% 0.1534 231.639,0.2)] to-[oklch(75.54% 0.1534 231.639,0.1)] flex items-center justify-center border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-lg">
                <Server className="w-4 h-4 text-[oklch(75.54% 0.1534 231.639)]" />
              </div>
              <span className="bg-gradient-to-r from-foreground via-[oklch(75.54% 0.1534 231.639)] to-foreground bg-clip-text text-transparent">
                Group Preview
              </span>
            </CardTitle>
          </CardHeader>
          
          <CardContent className="space-y-3">
            {}
            <div className="space-y-2">
              <Label className="text-sm font-medium text-muted-foreground">Group Name</Label>
              <div className="p-2.5 bg-gradient-to-r from-[oklch(75.54% 0.1534 231.639,0.1)] to-[oklch(75.54% 0.1534 231.639,0.05)] rounded-lg border border-[oklch(75.54% 0.1534 231.639,0.3)] text-sm shadow-sm">
                {formData.name ? (
                  <p className="font-semibold text-foreground">{formData.name}</p>
                ) : (
                  <p className="text-muted-foreground italic">Not set</p>
                )}
              </div>
            </div>

            {}
            <div className="space-y-2">
              <Label className="text-sm font-medium text-muted-foreground">Platform</Label>
              <div className="p-2.5 bg-gradient-to-r from-[oklch(0.7554 0.1534 231.639,0.1)] to-[oklch(0.7554 0.1534 231.639,0.05)] rounded-lg border border-[oklch(0.7554 0.1534 231.639,0.3)] text-sm shadow-sm">
                {formData.platform.name ? (
                  <div className="flex items-center space-x-2">
                    <Image
                      src={getPlatformIcon(formData.platform.name)}
                      alt={formData.platform.name}
                      width={20}
                      height={20}
                      className="w-5 h-5 rounded"
                      onError={(e) => {
                        e.currentTarget.src = '/placeholder.png';
                      }}
                    />
                    <span className="font-semibold capitalize text-truncate overflow-hidden">
                      {formData.platform.name}
                    </span>
                  </div>
                ) : (
                  <p className="text-muted-foreground italic">Not selected</p>
                )}
              </div>
            </div>

            {}
            <div className="space-y-2">
              <Label className="text-sm font-medium text-muted-foreground">Version</Label>
              <div className="p-2.5 bg-gradient-to-r from-[oklch(0.7554 0.1534 231.639,0.1)] to-[oklch(0.7554 0.1534 231.639,0.05)] rounded-lg border border-[oklch(0.7554 0.1534 231.639,0.3)] text-sm shadow-sm">
                {formData.platform.version ? (
                  <Badge 
                    variant="outline" 
                    className="font-semibold text-sm px-2 py-1 border-[oklch(75.54% 0.1534 231.639,0.5)] text-[oklch(75.54% 0.1534 231.639)]"
                  >
                    {formData.platform.version}
                  </Badge>
                ) : (
                  <p className="text-muted-foreground italic">Not selected</p>
                )}
              </div>
            </div>

            {}
            <div className="space-y-2">
              <Label className="text-sm font-medium text-muted-foreground">Memory</Label>
              <div className="p-2.5 bg-gradient-to-r from-[oklch(0.7554 0.1534 231.639,0.1)] to-[oklch(0.7554 0.1534 231.639,0.05)] rounded-lg border border-[oklch(0.7554 0.1534 231.639,0.3)] text-sm shadow-sm">
                {formData.memory.min && formData.memory.max ? (
                  <div className="space-y-1">
                    <div className="flex justify-between items-center">
                      <span className="text-muted-foreground">Min:</span>
                      <span className="font-semibold text-foreground">{formData.memory.min}</span>
                    </div>
                    <div className="flex justify-between items-center">
                      <span className="text-muted-foreground">Max:</span>
                      <span className="font-semibold text-foreground">{formData.memory.max}</span>
                    </div>
                  </div>
                ) : (
                  <p className="text-muted-foreground italic">Not set</p>
                )}
              </div>
            </div>

            {}
            <div className="space-y-2">
              <Label className="text-sm font-medium text-muted-foreground">Templates</Label>
              <div className="p-2.5 bg-gradient-to-r from-[oklch(0.7554 0.1534 231.639,0.1)] to-[oklch(0.7554 0.1534 231.639,0.05)] rounded-lg border border-[oklch(0.7554 0.1534 231.639,0.3)] text-sm shadow-sm">
                {formData.templates.length > 0 ? (
                  <div className="space-y-1">
                    {formData.templates.map((template, index) => (
                      <div key={index} className="flex items-center space-x-2">
                        {template === 'EVERY' && (
                          <Badge 
                            variant="secondary" 
                            className="text-xs bg-green-500/20 text-green-600 border-green-500/30 px-1.5 py-0.5"
                          >
                            {template}
                          </Badge>
                        )}
                        {template === 'EVERY_PROXY' && (
                          <Badge 
                            variant="secondary" 
                            className="text-xs bg-blue-500/20 text-blue-600 border-blue-500/30 px-1.5 py-0.5"
                          >
                            {template}
                          </Badge>
                        )}
                        {template === 'EVERY_SERVER' && (
                          <Badge 
                            variant="secondary" 
                            className="text-xs bg-purple-500/20 text-purple-600 border-purple-500/30 px-1.5 py-0.5"
                          >
                            {template}
                          </Badge>
                        )}
                        {template !== 'EVERY' && template !== 'EVERY_PROXY' && template !== 'EVERY_SERVER' && (
                          <Badge 
                            variant="outline" 
                            className="text-xs px-1.5 py-0.5 border-[oklch(75.54% 0.1534 231.639,0.5)] text-[oklch(75.54% 0.1534 231.639)]"
                          >
                            {template}
                          </Badge>
                        )}
                      </div>
                    ))}
                  </div>
                ) : (
                  <p className="text-muted-foreground italic">No templates</p>
                )}
              </div>
            </div>

            {}
            <div className="space-y-2">
              <Label className="text-sm font-medium text-muted-foreground">Advanced Settings</Label>
              <div className="p-2.5 bg-gradient-to-r from-[oklch(75.54% 0.1534 231.639,0.1)] to-[oklch(75.54% 0.1534 231.639,0.05)] rounded-lg border border-[oklch(75.54% 0.1534 231.639,0.3)] text-sm shadow-sm">
                <div className="space-y-2">
                  <div className="flex justify-between items-center">
                    <span className="text-muted-foreground">Percentage:</span>
                    <span className="font-semibold text-foreground">
                      {formData.advanced.percentageNewService || 'Not set'}%
                    </span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-muted-foreground">Services:</span>
                    <span className="font-semibold text-foreground">
                      {formData.advanced.minServicesOnline || 'Not set'} - {formData.advanced.maxServicesOnline || 'Not set'}
                    </span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-muted-foreground">Fallback:</span>
                    <span className="font-semibold text-foreground">
                      {formData.advanced.fallback ? 'Yes' : 'No'}
                    </span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-muted-foreground">Static:</span>
                    <span className="font-semibold text-foreground">
                      {formData.advanced.static ? 'Yes' : 'No'}
                    </span>
                  </div>
                </div>
              </div>
            </div>

            {}
            <div className="pt-3 border-t border-border">
              <div className="flex items-center justify-between text-sm font-medium text-muted-foreground mb-2">
                <span className="text-[oklch(75.54% 0.1534 231.639)]">Setup Progress</span>
                <span className="font-semibold text-[oklch(75.54% 0.1534 231.639)]">{currentStep}/6</span>
              </div>
              <div className="w-full bg-muted/30 rounded-full h-2.5 border border-[oklch(75.54% 0.1534 231.639,0.3)]">
                <motion.div
                  className="bg-[oklch(75.54% 0.1534 231.639)] h-2.5 rounded-full transition-all duration-700 ease-out shadow-lg"
                  initial={{ width: 0 }}
                  animate={{ width: `${(currentStep / 6) * 100}%` }}
                  style={{ backgroundColor: 'oklch(75.54% 0.1534 231.639)' }}
                />
              </div>
              <div className="mt-2 text-xs text-muted-foreground text-center">
                {currentStep === 1 && "Group Name"}
                {currentStep === 2 && "Platform Selection"}
                {currentStep === 3 && "Version Selection"}
                {currentStep === 4 && "Memory Settings"}
                {currentStep === 5 && "Template Selection"}
                {currentStep === 6 && "Advanced Configuration"}
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}





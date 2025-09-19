'use client';

import { motion } from 'framer-motion';
import { FileText, Trash2 } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { TemplateSelectionStepProps } from '../types/group-creation.types';

export function TemplateSelectionStep({ formData, onTemplateAdd, onTemplateRemove }: TemplateSelectionStepProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6, delay: 0.2 }}
    >
      <Card className="bg-gradient-to-br from-card/90 via-card/70 to-card/90 border border-[oklch(75.54% 0.1534 231.639,0.3)] shadow-2xl backdrop-blur-sm">
        <CardHeader>
          <CardTitle className="flex items-center gap-2 text-foreground">
            <FileText className="w-5 h-5 text-[oklch(75.54% 0.1534 231.639)]" />
            Template Selection
          </CardTitle>
        </CardHeader>
        
        <CardContent className="space-y-6">
          
          {}
          <div className="space-y-4">
            <div className="flex items-center gap-2 mb-3">
              <div className="w-2 h-2 rounded-full bg-[oklch(75.54% 0.1534 231.639)]"></div>
              <span className="text-sm font-medium text-muted-foreground uppercase tracking-wide">Standard Templates</span>
            </div>
            
            <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
              {formData.templates.includes('EVERY') && (
                <div className="bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] rounded-lg px-4 py-3 border border-[oklch(75.54%_0.1534_231.639,0.3)] hover:bg-gradient-to-r hover:from-[oklch(75.54%_0.1534_231.639,0.15)] hover:to-[oklch(75.54%_0.1534_231.639,0.08)] transition-all duration-200">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <div className="w-2 h-2 rounded-full bg-green-500"></div>
                      <span className="text-sm font-medium text-foreground">EVERY</span>
                    </div>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => onTemplateRemove('EVERY')}
                      className="h-7 w-7 p-0 text-red-500 hover:text-red-600 hover:bg-red-500/10 border-red-500/30 hover:border-red-500/50 transition-all duration-200"
                    >
                      <Trash2 className="w-3 h-3" />
                    </Button>
                  </div>
                </div>
              )}
              
              {formData.templates.includes('EVERY_PROXY') && (
                <div className="bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] rounded-lg px-4 py-3 border border-[oklch(75.54%_0.1534_231.639,0.3)] hover:bg-gradient-to-r hover:from-[oklch(75.54%_0.1534_231.639,0.15)] hover:to-[oklch(75.54%_0.1534_231.639,0.08)] transition-all duration-200">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <div className="w-2 h-2 rounded-full bg-blue-500"></div>
                      <span className="text-sm font-medium text-foreground">EVERY_PROXY</span>
                    </div>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => onTemplateRemove('EVERY_PROXY')}
                      className="h-7 w-7 p-0 text-red-500 hover:text-red-600 hover:bg-red-500/10 border-red-500/30 hover:border-red-500/50 transition-all duration-200"
                    >
                      <Trash2 className="w-3 h-3" />
                    </Button>
                  </div>
                </div>
              )}
              
              {formData.templates.includes('EVERY_SERVER') && (
                <div className="bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] rounded-lg px-4 py-3 border border-[oklch(75.54%_0.1534_231.639,0.3)] hover:bg-gradient-to-r hover:from-[oklch(75.54%_0.1534_231.639,0.15)] hover:to-[oklch(75.54%_0.1534_231.639,0.08)] transition-all duration-200">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <div className="w-2 h-2 rounded-full bg-purple-500"></div>
                      <span className="text-sm font-medium text-foreground">EVERY_SERVER</span>
                    </div>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => onTemplateRemove('EVERY_SERVER')}
                      className="h-7 w-7 p-0 text-red-500 hover:text-red-600 hover:bg-red-500/10 border-red-500/30 hover:border-red-500/50 transition-all duration-200"
                    >
                      <Trash2 className="w-3 h-3" />
                    </Button>
                  </div>
                </div>
              )}
            </div>
            
            <p className="text-xs text-muted-foreground italic">
              Automatically added based on your platform selection
            </p>
          </div>

          {}
          <div className="space-y-4 pt-4 border-t border-border/30">
            <div className="flex items-center gap-2 mb-3">
              <div className="w-2 h-2 rounded-full bg-[oklch(75.54% 0.1534 231.639)]"></div>
              <span className="text-sm font-medium text-muted-foreground uppercase tracking-wide">Custom Templates</span>
            </div>
            
            <div className="space-y-3">
              <div className="flex gap-3">
                <Input
                  id="newTemplate"
                  placeholder="Enter template name..."
                  className="flex-1 h-10 border-2 focus:border-[oklch(75.54% 0.1534 231.639)] transition-all duration-200 bg-background/50"
                  onKeyPress={(e) => {
                    if (e.key === 'Enter') {
                      const input = e.target as HTMLInputElement;
                      if (input.value.trim()) {
                        onTemplateAdd(input.value);
                        input.value = '';
                      }
                    }
                  }}
                />
                <Button
                  onClick={() => {
                    const input = document.getElementById('newTemplate') as HTMLInputElement;
                    if (input && input.value.trim()) {
                      onTemplateAdd(input.value);
                      input.value = '';
                    }
                  }}
                  className="h-10 px-4 text-sm font-medium hover:opacity-90 transition-all duration-200 shadow-lg shadow-[0_0_20px_rgba(75.54%,15.34%,231.639,0.3)] hover:shadow-[0_0_30px_rgba(75.54%,15.34%,231.639,0.4)]"
                  style={{ backgroundColor: 'oklch(75.54% .1534 231.639)' }}
                >
                  Add Template
                </Button>
              </div>
              
              {}
              <div className="space-y-2">
                {formData.templates
                  .filter(template => template !== 'EVERY' && template !== 'EVERY_PROXY' && template !== 'EVERY_SERVER')
                  .map((template, index) => (
                    <div key={index} className="flex items-center justify-between bg-gradient-to-r from-[oklch(75.54%_0.1534_231.639,0.1)] to-[oklch(75.54%_0.1534_231.639,0.05)] rounded-lg px-4 py-3 border border-[oklch(75.54%_0.1534_231.639,0.3)] hover:bg-gradient-to-r hover:from-[oklch(75.54%_0.1534_231.639,0.15)] hover:to-[oklch(75.54%_0.1534_231.639,0.08)] transition-all duration-200">
                      <div className="flex items-center gap-3">
                        <div className="w-2 h-2 rounded-full bg-[oklch(75.54% 0.1534 231.639)]"></div>
                        <span className="text-sm font-medium text-foreground">{template}</span>
                      </div>
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => onTemplateRemove(template)}
                        className="h-7 px-3 text-xs border-red-500/30 text-red-600 hover:bg-red-500/10 hover:border-red-500/50 transition-all duration-200"
                      >
                        Remove
                      </Button>
                    </div>
                  ))}
                
                {formData.templates.filter(template => template !== 'EVERY' && template !== 'EVERY_PROXY' && template !== 'EVERY_SERVER').length === 0 && (
                  <div className="text-center py-8">
                    <FileText className="w-16 h-16 text-muted-foreground mx-auto opacity-50 mb-4" />
                    <p className="text-muted-foreground">No custom templates added yet</p>
                    <p className="text-xs text-muted-foreground mt-2">Add templates to customize your service group</p>
                  </div>
                )}
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
    </motion.div>
  );
}





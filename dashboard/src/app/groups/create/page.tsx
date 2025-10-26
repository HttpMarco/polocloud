'use client';

import { useState, useEffect, useCallback } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { useRouter } from 'next/navigation';
import { Button } from '@/components/ui/button';
import GlobalNavbar from '@/components/global-navbar';

import { ChevronLeft, ChevronRight, Check } from 'lucide-react';
import { GroupPreview, GroupDetailsStep, PlatformSelectionStep, VersionSelectionStep, MemoryConfigurationStep, TemplateSelectionStep, AdvancedSettingsStep, GroupCreatedAnimation, Platform, GroupCreateData } from '@/features/group-creation';
import { toast } from 'sonner';

export default function CreateGroupPage() {
  const router = useRouter();
  const [currentStep, setCurrentStep] = useState(1);
  const [platforms, setPlatforms] = useState<Platform[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showSuccess, setShowSuccess] = useState(false);
  
  const [formData, setFormData] = useState<GroupCreateData>({
    name: '',
    platform: {
      name: '',
      version: ''
    },
    memory: {
      min: '',
      max: ''
    },
    templates: ['EVERY'],
    advanced: {
      percentageNewService: '',
      minServicesOnline: '',
      maxServicesOnline: '',
      fallback: false,
      static: false
    }
  });

  const withLoading = async (fn: () => Promise<void>) => {
    try {
      setIsLoading(true);
      setError(null);
      await fn();
    } catch {
      setError('Operation failed');
    } finally {
      setIsLoading(false);
    }
  };

  const loadPlatforms = useCallback(async () => {
    await withLoading(async () => {
      const response = await fetch('/api/platforms/list');
      if (response.ok) {
        const data = await response.json();

        if (Array.isArray(data)) {
          const processedPlatforms: Platform[] = data.map((item: unknown) => {
            if (typeof item === 'object' && item !== null && 'name' in item && 'type' in item) {
              const platform = item as { name: string; type: string; versions?: unknown[] };
              return {
                name: platform.name,
                type: platform.type,
                versions: Array.isArray(platform.versions) ? platform.versions : []
              };
            }
            return null;
          }).filter(Boolean) as Platform[];

          setPlatforms(processedPlatforms);
        } else {
          setError('Invalid response format from server');
        }
      } else {
        const errorData = await response.json();
        setError(errorData.error || 'Failed to load platforms');
      }
    });
  }, []);

  useEffect(() => {
    loadPlatforms();
  }, [loadPlatforms]);

  const handleNext = () => {
    if (currentStep < 6) {
      setCurrentStep(currentStep + 1);
    }
  };

  const handlePrevious = () => {
    if (currentStep > 1) {
      setCurrentStep(currentStep - 1);
    }
  };

  const handleNameChange = (name: string) => {
    setFormData(prev => {

      const templatesWithoutOldName = prev.templates.filter(template => template !== prev.name);

      const newTemplates = [...templatesWithoutOldName];
      if (name.trim() && !newTemplates.includes(name.trim())) {
        newTemplates.push(name.trim());
      }
      
      return { 
        ...prev, 
        name,
        templates: newTemplates
      };
    });
  };

  const handlePlatformSelect = (platformName: string) => {
    setFormData(prev => {
      const selectedPlatform = platforms.find(p => p.name === platformName);

      const newTemplates = ['EVERY'];
      
      if (prev.name.trim()) {
        newTemplates.push(prev.name.trim());
      }
      
      if (selectedPlatform) {
        const platformType = selectedPlatform.type.toLowerCase();

        if (platformType === 'proxy') {
          newTemplates.push('EVERY_PROXY');
        } else if (platformType === 'server') {
          newTemplates.push('EVERY_SERVER');
        }
      }
      
      return { 
        ...prev, 
        platform: { name: platformName, version: '' },
        templates: newTemplates
      };
    });
  };

  const handleVersionSelect = (version: string) => {
    setFormData(prev => ({ 
      ...prev, 
      platform: { ...prev.platform, version }
    }));
  };

  const handleMemoryChange = (type: 'min' | 'max', value: string) => {
    setFormData(prev => ({
      ...prev,
      memory: { ...prev.memory, [type]: value }
    }));
  };

  const handleAdvancedChange = (field: keyof GroupCreateData['advanced'], value: string | boolean) => {
    setFormData(prev => ({
      ...prev,
      advanced: { ...prev.advanced, [field]: value }
    }));
  };

  const handleTemplateAdd = (template: string) => {
    if (template.trim() && !formData.templates.includes(template.trim())) {
      setFormData(prev => ({
        ...prev,
        templates: [...prev.templates, template.trim()]
      }));
    }
  };

  const handleTemplateRemove = (template: string) => {

    setFormData(prev => {
      const newTemplates = prev.templates.filter(t => t !== template);
      
      return {
        ...prev,
        templates: newTemplates
      };
    });
  };

  const createGroup = async () => {
    try {

      const minMemoryMB = parseInt(formData.memory.min.replace(/\D/g, ''));
      const maxMemoryMB = parseInt(formData.memory.max.replace(/\D/g, ''));

      const percentage = parseFloat(formData.advanced.percentageNewService);

      const minServices = parseInt(formData.advanced.minServicesOnline);
      const maxServices = parseInt(formData.advanced.maxServicesOnline);
      
      const requestBody = {
        name: formData.name,
        minMemory: minMemoryMB,
        maxMemory: maxMemoryMB,
        minOnlineService: minServices,
        maxOnlineService: maxServices,
        platform: {
          name: formData.platform.name,
          version: formData.platform.version
        },
        percentageToStartNewService: percentage,
        createdAt: Date.now(),
        templates: formData.templates,
        properties: {
          fallback: formData.advanced.fallback,
          static: formData.advanced.static
        }
      };
      const response = await fetch('/api/groups/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody),
      });




      if (!response.ok) {
        const errorData = await response.json();

        throw new Error(errorData.error || 'Failed to create group');
      }
      
      await response.json();

      setShowSuccess(true);

      setTimeout(() => {
        window.location.href = '/groups';
      }, 3000);
      
    } catch (error) {

      toast.error(`Failed to create group: ${error instanceof Error ? error.message : 'Unknown error'}`);
    }
  };

  const canProceedToNext = () => {
    switch (currentStep) {
      case 1:
        return formData.name.trim().length > 0;
      case 2:
        return formData.platform.name.length > 0;
      case 3:
        return formData.platform.version.length > 0;
      case 4:
        return formData.memory.min.length > 0 && formData.memory.max.length > 0 &&
               parseInt(formData.memory.min.replace(/\D/g, '')) <= parseInt(formData.memory.max.replace(/\D/g, ''));
      case 5:
        return true;
      case 6:
        return formData.advanced.percentageNewService.length > 0 && 
               formData.advanced.minServicesOnline.length > 0 && 
               formData.advanced.maxServicesOnline.length > 0 &&
               parseInt(formData.advanced.minServicesOnline) <= parseInt(formData.advanced.maxServicesOnline);
      default:
        return false;
    }
  };

  const selectedPlatform = platforms.find(p => p.name === formData.platform.name);

  if (isLoading) {
    return (
      <div className="w-full h-full flex items-center justify-center">
        <div className="w-8 h-8 border-2 border-primary border-t-transparent rounded-full animate-spin"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="w-full h-full flex items-center justify-center">
        <div className="text-center">
          <p className="text-red-500 mb-4">{error}</p>
          <Button onClick={loadPlatforms}>Retry</Button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20 ultra-smooth-scroll">
      <GlobalNavbar />
      
      {}
      <div className="flex-1 p-6 overflow-auto">
        <div className="max-w-6xl mx-auto">
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            {}
            <GroupPreview formData={formData} currentStep={currentStep} />

            {}
            <div className="lg:col-span-2">
              <AnimatePresence mode="wait">
                <motion.div
                  key={currentStep}
                  initial={{ opacity: 0, y: 10 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, y: -10 }}
                  transition={{ duration: 0.25, ease: "easeOut" }}
                >
                  {}
                  {currentStep === 1 && (
                    <GroupDetailsStep 
                      formData={formData} 
                      onNameChange={handleNameChange} 
                    />
                  )}

                  {}
                  {currentStep === 2 && (
                    <PlatformSelectionStep
                      platforms={platforms}
                      selectedPlatform={formData.platform.name}
                      onPlatformSelect={handlePlatformSelect}
                    />
                  )}

                  {}
                  {currentStep === 3 && selectedPlatform && (
                    <VersionSelectionStep
                      selectedPlatform={selectedPlatform}
                      selectedVersion={formData.platform.version}
                      onVersionSelect={handleVersionSelect}
                    />
                  )}

                  {}
                  {currentStep === 4 && (
                    <MemoryConfigurationStep
                      formData={formData}
                      onMemoryChange={handleMemoryChange}
                    />
                  )}

                  {}
                  {currentStep === 5 && (
                    <TemplateSelectionStep
                      formData={formData}
                      onTemplateAdd={handleTemplateAdd}
                      onTemplateRemove={handleTemplateRemove}
                    />
                  )}

                  {}
                  {currentStep === 6 && (
                    <AdvancedSettingsStep
                      formData={formData}
                      onAdvancedChange={handleAdvancedChange}
                    />
                  )}
                </motion.div>
              </AnimatePresence>

              {}
              <GroupCreatedAnimation
                isVisible={showSuccess}
                onAnimationComplete={() => {
                  setShowSuccess(false);
                  router.push('/groups');
                }}
              />

              {}
              <div className="flex justify-between mt-8">
                <motion.div
                  initial={{ opacity: 0, x: -20 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ duration: 0.3, delay: 0.2 }}
                >
                  <motion.div
                    whileHover={{ scale: 1.02, x: -2 }}
                    whileTap={{ scale: 0.98 }}
                    transition={{ duration: 0.2 }}
                  >
                    <Button
                      variant="outline"
                      onClick={handlePrevious}
                      disabled={currentStep === 1}
                      className="flex items-center space-x-2 px-4 py-2 h-9 text-sm font-medium border-2 hover:bg-muted/50 transition-all duration-200"
                    >
                      <ChevronLeft className="w-3 h-3" />
                      <span>Previous</span>
                    </Button>
                  </motion.div>
                </motion.div>

                <motion.div 
                  className="flex space-x-3"
                  initial={{ opacity: 0, x: 20 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ duration: 0.3, delay: 0.3 }}
                >
                  {currentStep < 6 ? (
                    <motion.div
                      whileHover={{ scale: 1.02, x: 2 }}
                      whileTap={{ scale: 0.98 }}
                      transition={{ duration: 0.2 }}
                    >
                      <Button
                        onClick={handleNext}
                        disabled={!canProceedToNext()}
                        className="flex items-center space-x-2 px-6 py-2 h-9 text-sm font-medium shadow-lg hover:shadow-xl transition-all duration-200 text-white"
                        style={{
                          backgroundColor: 'oklch(75.54% 0.1534 231.639)'
                        }}
                        onMouseEnter={(e) => {
                          e.currentTarget.style.backgroundColor = 'oklch(70% 0.1534 231.639)';
                        }}
                        onMouseLeave={(e) => {
                          e.currentTarget.style.backgroundColor = 'oklch(75.54% 0.1534 231.639)';
                        }}
                      >
                        <span>Continue</span>
                        <ChevronRight className="w-3 h-3" />
                      </Button>
                    </motion.div>
                  ) : (
                    <motion.div
                      whileHover={{ scale: 1.02, y: -2 }}
                      whileTap={{ scale: 0.98 }}
                      transition={{ duration: 0.2 }}
                    >
                      <Button
                        onClick={createGroup}
                        disabled={!canProceedToNext()}
                        className="flex items-center space-x-2 px-6 py-2 h-9 text-sm font-medium shadow-lg hover:shadow-xl transition-all duration-200 text-white"
                        style={{
                          backgroundColor: 'oklch(75.54% 0.1534 231.639)'
                        }}
                        onMouseEnter={(e) => {
                          e.currentTarget.style.backgroundColor = 'oklch(70% 0.1534 231.639)';
                        }}
                        onMouseLeave={(e) => {
                          e.currentTarget.style.backgroundColor = 'oklch(75.54% 0.1534 231.639)';
                        }}
                      >
                        <span>Create Group</span>
                        <Check className="w-3 h-3" />
                      </Button>
                    </motion.div>
                  )}
                </motion.div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

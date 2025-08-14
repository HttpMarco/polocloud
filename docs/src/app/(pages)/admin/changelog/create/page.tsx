'use client';

import { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Badge } from '@/components/ui/badge';
import {
  ArrowLeft,
  ArrowRight,
  Check,
  GitBranch,
  Settings,
  Edit3,
  Sparkles,
  Calendar,
  User,
  Tag,
  Plus,
  X
} from 'lucide-react';
import { useRouter } from 'next/navigation';

interface ChangelogFormData {
  version: string;
  title: string;
  description: string;
  type: 'major' | 'minor' | 'patch' | 'hotfix';
  releaseDate: string;
  changes: string[];
}

export default function CreateChangelogPage() {
  const router = useRouter();
  const [creating, setCreating] = useState(false);
  const [currentStep, setCurrentStep] = useState(1);

  const [formData, setFormData] = useState<ChangelogFormData>({
    version: '',
    title: '',
    description: '',
    type: 'patch',
    releaseDate: new Date().toISOString().split('T')[0],
    changes: [''],
  });

  const steps = [
    { id: 1, title: 'Version Info', icon: GitBranch, description: 'Version number and type' },
    { id: 2, title: 'Details', icon: Settings, description: 'Title and description' },
    { id: 3, title: 'Changes', icon: Edit3, description: 'List of changes' },
    { id: 4, title: 'Review', icon: Check, description: 'Review and publish' },
  ];

  const nextStep = () => {
    if (currentStep < steps.length) {
      setCurrentStep(currentStep + 1);
    }
  };

  const prevStep = () => {
    if (currentStep > 1) {
      setCurrentStep(currentStep - 1);
    }
  };

  const canProceed = () => {
    switch (currentStep) {
      case 1:
        return formData.version.trim().length > 0;
      case 2:
        return formData.title.trim().length > 0 && formData.description.trim().length > 0;
      case 3:
        return formData.changes.some(change => change.trim().length > 0);
      case 4:
        return true;
      default:
        return false;
    }
  };

  const addChange = () => {
    setFormData(prev => ({
      ...prev,
      changes: [...prev.changes, '']
    }));
  };

  const removeChange = (index: number) => {
    if (formData.changes.length > 1) {
      setFormData(prev => ({
        ...prev,
        changes: prev.changes.filter((_, i) => i !== index)
      }));
    }
  };

  const updateChange = (index: number, value: string) => {
    setFormData(prev => ({
      ...prev,
      changes: prev.changes.map((change, i) => i === index ? value : change)
    }));
  };

  const handleSubmit = async () => {
    if (!formData.version.trim() || !formData.title.trim() || !formData.description.trim()) return;

    setCreating(true);

    try {
      const response = await fetch('/api/admin/changelog/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({
          version: formData.version.trim(),
          title: formData.title.trim(),
          description: formData.description.trim(),
          type: formData.type,
          releaseDate: formData.releaseDate,
          changes: formData.changes.map(change => change.trim()).filter(change => change.length > 0),
        }),
      });

      if (response.ok) {
        router.push('/admin');
      } else {
        const errorData = await response.json();
        alert('Failed to create changelog entry: ' + errorData.error);
      }
    } catch (error) {
      console.error('Error creating changelog entry:', error);
      alert('Failed to create changelog entry');
    } finally {
      setCreating(false);
    }
  };

  const getTypeColor = (type: string) => {
    switch (type) {
      case 'major': return 'bg-red-100 text-red-800 dark:bg-red-900/20 dark:text-red-400 border-red-200 dark:border-red-800';
      case 'minor': return 'bg-blue-100 text-blue-800 dark:bg-blue-900/20 dark:text-blue-400 border-blue-200 dark:border-blue-800';
      case 'patch': return 'bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-400 border-green-200 dark:border-green-800';
      case 'hotfix': return 'bg-orange-100 text-orange-800 dark:bg-orange-900/20 dark:text-orange-400 border-orange-200 dark:border-orange-800';
      default: return 'bg-gray-100 text-gray-800 dark:bg-gray-900/20 dark:text-gray-400 border-gray-200 dark:border-gray-800';
    }
  };

  const renderStepContent = () => {
    const stepVariants = {
      initial: { opacity: 0, y: 20 },
      animate: { opacity: 1, y: 0 },
      exit: { opacity: 0, y: -20 }
    };

    switch (currentStep) {
      case 1:
        return (
          <motion.div
            key="step1"
            variants={stepVariants}
            initial="initial"
            animate="animate"
            exit="exit"
            transition={{ duration: 0.3 }}
            className="space-y-8"
          >
            <div className="text-center mb-8">
              <motion.div
                initial={{ scale: 0 }}
                animate={{ scale: 1 }}
                transition={{ delay: 0.2, type: "spring" }}
              >
                <GitBranch className="w-20 h-20 mx-auto mb-6 text-primary" />
              </motion.div>
              <h3 className="text-3xl font-bold mb-3">Version Information</h3>
              <p className="text-muted-foreground text-lg">What version are you releasing?</p>
            </div>

            <div className="max-w-lg mx-auto space-y-6">
              <div>
                <label className="block text-sm font-medium mb-3">Version Number *</label>
                <Input
                  value={formData.version}
                  onChange={(e) => setFormData({ ...formData, version: e.target.value })}
                  placeholder="e.g., 1.2.3"
                  className="text-lg py-3 px-4"
                  autoFocus
                />
              </div>

              <div>
                <label className="block text-sm font-medium mb-3">Release Type *</label>
                <div className="grid grid-cols-2 gap-3">
                  {(['major', 'minor', 'patch', 'hotfix'] as const).map((type) => (
                    <motion.button
                      key={type}
                      type="button"
                      onClick={() => setFormData({ ...formData, type })}
                      className={`p-4 rounded-lg border-2 transition-all duration-200 ${
                        formData.type === type
                          ? 'border-primary bg-primary/10 scale-105'
                          : 'border-border hover:border-primary/50 hover:bg-muted/50'
                      }`}
                      whileHover={{ scale: 1.02 }}
                      whileTap={{ scale: 0.98 }}
                    >
                      <div className="text-center">
                        <div className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-medium mb-2 ${getTypeColor(type)}`}>
                          {type.toUpperCase()}
                        </div>
                        <p className="text-sm font-medium capitalize">{type}</p>
                      </div>
                    </motion.button>
                  ))}
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium mb-3">Release Date *</label>
                <Input
                  type="date"
                  value={formData.releaseDate}
                  onChange={(e) => setFormData({ ...formData, releaseDate: e.target.value })}
                  className="py-3"
                />
              </div>
            </div>
          </motion.div>
        );

      case 2:
        return (
          <motion.div
            key="step2"
            variants={stepVariants}
            initial="initial"
            animate="animate"
            exit="exit"
            transition={{ duration: 0.3 }}
            className="space-y-8"
          >
            <div className="text-center mb-8">
              <motion.div
                initial={{ scale: 0 }}
                animate={{ scale: 1 }}
                transition={{ delay: 0.2, type: "spring" }}
              >
                <Settings className="w-20 h-20 mx-auto mb-6 text-primary" />
              </motion.div>
              <h3 className="text-3xl font-bold mb-3">Release Details</h3>
              <p className="text-muted-foreground text-lg">Title and description of the release</p>
            </div>

            <div className="max-w-lg mx-auto space-y-6">
              <div>
                <label className="block text-sm font-medium mb-3">Release Title *</label>
                <Input
                  value={formData.title}
                  onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                  placeholder="e.g., Performance Improvements & Bug Fixes"
                  className="text-lg py-3 px-4"
                />
              </div>

              <div>
                <label className="block text-sm font-medium mb-3">Description *</label>
                <textarea
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  placeholder="Brief description of what this release includes..."
                  rows={4}
                  className="w-full px-4 py-3 border border-border rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent resize-none"
                />
              </div>
            </div>
          </motion.div>
        );

      case 3:
        return (
          <motion.div
            key="step3"
            variants={stepVariants}
            initial="initial"
            animate="animate"
            exit="exit"
            transition={{ duration: 0.3 }}
            className="space-y-8"
          >
            <div className="text-center mb-8">
              <motion.div
                initial={{ scale: 0 }}
                animate={{ scale: 1 }}
                transition={{ delay: 0.2, type: "spring" }}
              >
                <Edit3 className="w-20 h-20 mx-auto mb-6 text-primary" />
              </motion.div>
              <h3 className="text-3xl font-bold mb-3">List Changes</h3>
              <p className="text-muted-foreground text-lg">What changes are included in this release?</p>
            </div>

            <div className="max-w-2xl mx-auto space-y-4">
              {formData.changes.map((change, index) => (
                <motion.div
                  key={index}
                  initial={{ opacity: 0, x: -20 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ delay: index * 0.1 }}
                  className="flex items-center gap-3"
                >
                  <div className="flex-shrink-0 w-6 h-6 bg-primary/20 text-primary rounded-full flex items-center justify-center text-sm font-medium">
                    {index + 1}
                  </div>
                  <Input
                    value={change}
                    onChange={(e) => updateChange(index, e.target.value)}
                    placeholder={`Change ${index + 1}...`}
                    className="flex-1 py-3"
                  />
                  {formData.changes.length > 1 && (
                    <Button
                      type="button"
                      variant="outline"
                      size="sm"
                      onClick={() => removeChange(index)}
                      className="flex-shrink-0 w-10 h-10 p-0"
                    >
                      <X className="w-4 h-4" />
                    </Button>
                  )}
                </motion.div>
              ))}

              <motion.button
                type="button"
                onClick={addChange}
                className="w-full py-3 border-2 border-dashed border-border rounded-lg text-muted-foreground hover:border-primary/50 hover:text-primary transition-colors"
                whileHover={{ scale: 1.02 }}
                whileTap={{ scale: 0.98 }}
              >
                <Plus className="w-5 h-5 mx-auto mb-2" />
                Add Another Change
              </motion.button>
            </div>
          </motion.div>
        );

      case 4:
        return (
          <motion.div
            key="step4"
            variants={stepVariants}
            initial="initial"
            animate="animate"
            exit="exit"
            transition={{ duration: 0.3 }}
            className="space-y-8"
          >
            <div className="text-center mb-8">
              <motion.div
                initial={{ scale: 0 }}
                animate={{ scale: 1 }}
                transition={{ delay: 0.2, type: "spring" }}
              >
                <Sparkles className="w-20 h-20 mx-auto mb-6 text-primary" />
              </motion.div>
              <h3 className="text-3xl font-bold mb-3">Ready to Publish</h3>
              <p className="text-muted-foreground text-lg">Review your changelog entry and publish it</p>
            </div>

            <div className="max-w-2xl mx-auto">
              <motion.div
                className="bg-gradient-to-br from-muted/50 to-muted/20 rounded-xl p-8 space-y-6 border shadow-lg"
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.3 }}
              >
                <div className="flex items-center gap-3 mb-4">
                  <Badge className={`px-3 py-1 text-sm font-medium ${getTypeColor(formData.type)}`}>
                    {formData.type.toUpperCase()}
                  </Badge>
                  <span className="text-2xl font-bold">{formData.version}</span>
                </div>

                <div>
                  <h4 className="font-bold text-2xl mb-2">{formData.title}</h4>
                  {formData.description && (
                    <p className="text-muted-foreground text-lg">{formData.description}</p>
                  )}
                </div>

                <div className="space-y-3">
                  <h5 className="font-semibold text-lg">Changes:</h5>
                  <div className="space-y-2">
                    {formData.changes.filter(change => change.trim().length > 0).map((change, index) => (
                      <div key={index} className="flex items-start gap-2">
                        <span className="text-primary mt-1">•</span>
                        <span>{change}</span>
                      </div>
                    ))}
                  </div>
                </div>

                <div className="text-sm text-muted-foreground border-t pt-4">
                  <div className="flex items-center gap-4">
                    <span className="flex items-center gap-1">
                      <Calendar className="w-4 h-4" />
                      {new Date(formData.releaseDate).toLocaleDateString()}
                    </span>
                    <span className="flex items-center gap-1">
                      <Tag className="w-4 h-4" />
                      {formData.changes.filter(change => change.trim().length > 0).length} changes
                    </span>
                  </div>
                </div>
              </motion.div>
            </div>
          </motion.div>
        );

      default:
        return null;
    }
  };

  return (
    <div className="relative min-h-screen">
      <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]" />

      <div className="container mx-auto px-6 py-12 max-w-6xl relative z-10">

        <div className="text-center mb-16">
          <div className="flex items-center justify-center gap-3 mb-6">
            <div className="flex items-center justify-center w-10 h-10 bg-primary/10 text-primary rounded-full border border-primary/20">
              <GitBranch className="w-5 h-5" />
            </div>
          </div>
          <h1 className="text-3xl md:text-4xl lg:text-5xl font-black bg-gradient-to-r from-foreground via-primary to-foreground bg-clip-text text-transparent mb-6 leading-tight">
            Create New Changelog Entry
          </h1>
          <p className="text-base md:text-lg text-muted-foreground max-w-3xl mx-auto leading-relaxed">
            Follow the steps to create and publish a new changelog entry for PoloCloud
          </p>
        </div>

        <div className="bg-card/50 backdrop-blur-sm border border-border/50 rounded-xl p-8 mb-8 shadow-lg">
          <div className="relative mb-8">
            <div className="absolute top-6 left-6 right-6 h-0.5 bg-border/40"></div>

            <motion.div
              className="absolute top-6 left-6 h-0.5 bg-primary"
              initial={{ width: 0 }}
              animate={{
                width: ((currentStep - 1) / (steps.length - 1)) * 100 + '%'
              }}
              transition={{ duration: 0.5, ease: "easeInOut" }}
            />

            <div className="flex items-center justify-between relative">
              {steps.map((step, index) => (
                <div key={step.id} className="flex flex-col items-center">
                  <motion.div
                    className={'w-12 h-12 rounded-full flex items-center justify-center border-2 transition-all duration-300 z-10 ' +
                      (currentStep >= step.id
                        ? 'bg-primary border-primary text-primary-foreground shadow-lg'
                        : currentStep === step.id
                        ? 'bg-background border-primary text-primary shadow-lg'
                        : 'bg-background border-border text-muted-foreground')}
                    whileHover={{ scale: 1.1 }}
                    animate={{
                      scale: currentStep === step.id ? 1.1 : 1,
                    }}
                  >
                    {currentStep > step.id ? (
                      <motion.div
                        initial={{ scale: 0 }}
                        animate={{ scale: 1 }}
                        transition={{ type: "spring", delay: 0.1 }}
                      >
                        <Check className="w-5 h-5" />
                      </motion.div>
                    ) : (
                      <step.icon className="w-5 h-5" />
                    )}
                  </motion.div>

                  <div className="text-center mt-3">
                    <p className={'text-sm font-medium transition-colors ' +
                      (currentStep >= step.id ? 'text-foreground' : 'text-muted-foreground')}>
                      {step.title}
                    </p>
                  </div>
                </div>
              ))}
            </div>
          </div>

          <div className="text-center">
            <p className="font-semibold text-lg">{steps[currentStep - 1]?.title}</p>
            <p className="text-sm text-muted-foreground">{steps[currentStep - 1]?.description}</p>
          </div>
        </div>

        <div className="bg-card/50 backdrop-blur-sm border border-border/50 rounded-xl p-8 mb-8 shadow-lg">
          <div className="min-h-[500px] flex items-center justify-center">
            <div className="w-full">
              <AnimatePresence mode="wait">
                {renderStepContent()}
              </AnimatePresence>
            </div>
          </div>
        </div>

        <div className="flex justify-between">
          <Button
            variant="outline"
            onClick={() => router.push('/admin')}
            className="px-6"
          >
            <ArrowLeft className="w-4 h-4 mr-2" />
            Cancel
          </Button>

          <div className="flex gap-4">
            {currentStep > 1 && (
              <Button
                variant="outline"
                onClick={prevStep}
                className="px-6"
              >
                <ArrowLeft className="w-4 h-4 mr-2" />
                Back
              </Button>
            )}

            {currentStep < steps.length ? (
              <Button
                onClick={nextStep}
                disabled={!canProceed()}
                className="px-6"
              >
                Next
                <ArrowRight className="w-4 h-4 ml-2" />
              </Button>
            ) : (
              <motion.div
                whileHover={{ scale: 1.05 }}
                whileTap={{ scale: 0.95 }}
              >
                <Button
                  onClick={handleSubmit}
                  disabled={creating || !canProceed()}
                  variant="secondary"
                  className="px-8"
                >
                  {creating ? (
                    <>
                      <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                      Creating...
                    </>
                  ) : (
                    <>
                      Publish Changelog
                      <Sparkles className="w-4 h-4 ml-2" />
                    </>
                  )}
                </Button>
              </motion.div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

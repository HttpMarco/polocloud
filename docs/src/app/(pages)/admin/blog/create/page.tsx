'use client';

import { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Badge } from '@/components/ui/badge';
import { MDXEditor } from '@/components/ui/mdx-editor';
import {
  ArrowLeft,
  ArrowRight,
  Check,
  FileText,
  Settings,
  Edit3,
  Sparkles,
  Calendar,
  User,
  Tag,
  Plus,
  X,
  Save
} from 'lucide-react';
import { useRouter } from 'next/navigation';

interface BlogFormData {
  title: string;
  description: string;
  author: string;
  tags: string;
  pinned: boolean;
  content: string;
}

const STORAGE_KEY = 'blog-draft';

export default function CreateBlogPage() {
  const router = useRouter();
  const [creating, setCreating] = useState(false);
  const [currentStep, setCurrentStep] = useState(1);
  const [lastSaved, setLastSaved] = useState<Date | null>(null);
  const [autoSaveStatus, setAutoSaveStatus] = useState<'saved' | 'saving' | 'error'>('saved');

  const loadSavedData = (): BlogFormData => {
    if (typeof window === 'undefined') {
      return {
        title: '',
        description: '',
        author: '',
        tags: '',
        pinned: false,
        content: '# Blog Post Title\n\nWrite your content here...\n\n## Features\n\n- **Bold text** and *italic text*\n- `inline code` and code blocks\n- [Links](https://example.com)\n- > Beautiful quotes\n- Tables and lists\n\n:heart: :rocket: :sparkles:',
      };
    }

    try {
      const saved = localStorage.getItem(STORAGE_KEY);
      if (saved) {
        const parsed = JSON.parse(saved);
        return {
          title: parsed.title || '',
          description: parsed.description || '',
          author: parsed.author || '',
          tags: parsed.tags || '',
          pinned: parsed.pinned || false,
          content: parsed.content || '# Blog Post Title\n\nWrite your content here...\n\n## Features\n\n- **Bold text** and *italic text*\n- `inline code` and code blocks\n- [Links](https://example.com)\n- > Beautiful quotes\n- Tables and lists\n\n:heart: :rocket: :sparkles:',
        };
      }
    } catch (error) {
      console.error('Error loading saved data:', error);
    }

    return {
      title: '',
      description: '',
      author: '',
      tags: '',
      pinned: false,
      content: '# Blog Post Title\n\nWrite your content here...\n\n## Features\n\n- **Bold text** and *italic text*\n- `inline code` and code blocks\n- [Links](https://example.com)\n- > Beautiful quotes\n- Tables and lists\n\n:heart: :rocket: :sparkles:',
    };
  };

  const [formData, setFormData] = useState<BlogFormData>(loadSavedData);

  const saveToStorage = (data: BlogFormData) => {
    if (typeof window === 'undefined') return;

    setAutoSaveStatus('saving');
    try {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(data));
      setLastSaved(new Date());
      setAutoSaveStatus('saved');
    } catch (error) {
      console.error('Error saving to localStorage:', error);
      setAutoSaveStatus('error');
    }
  };

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      saveToStorage(formData);
    }, 1000);

    return () => clearTimeout(timeoutId);
  }, [formData]);

  useEffect(() => {
    const savedStep = localStorage.getItem(`${STORAGE_KEY}-step`);
    if (savedStep) {
      setCurrentStep(parseInt(savedStep));
    }
  }, []);

  const steps = [
    { id: 1, title: 'Basic Info', icon: FileText, description: 'Title and basic information' },
    { id: 2, title: 'Details', icon: Settings, description: 'Description and metadata' },
    { id: 3, title: 'Content', icon: Edit3, description: 'Write your blog content' },
    { id: 4, title: 'Review', icon: Check, description: 'Review and publish' },
  ];

  const nextStep = () => {
    if (currentStep < steps.length) {
      const newStep = currentStep + 1;
      setCurrentStep(newStep);
      if (typeof window !== 'undefined') {
        localStorage.setItem(`${STORAGE_KEY}-step`, newStep.toString());
      }
    }
  };

  const prevStep = () => {
    if (currentStep > 1) {
      const newStep = currentStep - 1;
      setCurrentStep(newStep);
      if (typeof window !== 'undefined') {
        localStorage.setItem(`${STORAGE_KEY}-step`, newStep.toString());
      }
    }
  };

  const manualSave = () => {
    saveToStorage(formData);
    if (typeof window !== 'undefined') {
      localStorage.setItem(`${STORAGE_KEY}-step`, currentStep.toString());
    }
  };

  const clearSavedData = () => {
    if (typeof window !== 'undefined') {
      localStorage.removeItem(STORAGE_KEY);
      localStorage.removeItem(`${STORAGE_KEY}-step`);
    }
  };

  const canProceed = () => {
    switch (currentStep) {
      case 1:
        return formData.title.trim().length > 0;
      case 2:
        return formData.description.trim().length > 0;
      case 3:
        return formData.content.trim().length > 0;
      case 4:
        return true;
      default:
        return false;
    }
  };

  const handleSubmit = async () => {
    if (!formData.title.trim() || !formData.content.trim()) return;

    setCreating(true);

    try {
      const response = await fetch('/api/admin/blog/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({
          title: formData.title.trim(),
          description: formData.description.trim(),
          author: formData.author.trim() || 'PoloCloud Team',
          tags: formData.tags.split(',').map(tag => tag.trim()).filter(tag => tag.length > 0),
          pinned: formData.pinned,
          content: formData.content.trim(),
        }),
      });

      if (response.ok) {
        clearSavedData();
        router.push('/admin');
      } else {
        const errorData = await response.json();
        alert('Failed to create blog post: ' + errorData.error);
      }
    } catch (error) {
      console.error('Error creating blog post:', error);
      alert('Failed to create blog post');
    } finally {
      setCreating(false);
    }
  };

  const renderStepContent = () => {
    const stepVariants = {
      initial: { opacity: 0, y: 20 },
      animate: { opacity: 1, y: 0 },
      exit: { opacity: 0, y: -20 },
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
                <FileText className="w-20 h-20 mx-auto mb-6 text-primary" />
              </motion.div>
              <h3 className="text-3xl font-bold mb-3">Blog Title</h3>
              <p className="text-muted-foreground text-lg">What should your new blog post be called?</p>
            </div>

            <div className="max-w-2xl mx-auto space-y-6">
              <div>
                <label className="block text-sm font-medium mb-3">Blog Title *</label>
                <Input
                  type="text"
                  placeholder="e.g., PoloCloud 2.0 Release"
                  value={formData.title}
                  onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                  className="text-lg"
                  autoFocus
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
              <h3 className="text-3xl font-bold mb-3">Blog Details</h3>
              <p className="text-muted-foreground text-lg">Provide description and additional information</p>
            </div>

            <div className="max-w-2xl mx-auto space-y-6">
              <div>
                <label className="block text-sm font-medium mb-3">Description *</label>
                <textarea
                  placeholder="Brief description of the blog post..."
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  className="w-full p-3 border rounded-lg resize-none focus:ring-2 focus:ring-primary focus:border-transparent text-lg"
                  rows={4}
                />
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium mb-3">Author</label>
                  <Input
                    type="text"
                    placeholder="PoloCloud Team"
                    value={formData.author}
                    onChange={(e) => setFormData({ ...formData, author: e.target.value })}
                    className="text-lg"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium mb-3">Tags</label>
                  <Input
                    type="text"
                    placeholder="tag1, tag2, tag3"
                    value={formData.tags}
                    onChange={(e) => setFormData({ ...formData, tags: e.target.value })}
                    className="text-lg"
                  />
                </div>
              </div>

              <div className="flex items-center space-x-3 p-4 bg-muted/50 rounded-lg">
                <input
                  type="checkbox"
                  id="pinned"
                  checked={formData.pinned}
                  onChange={(e) => setFormData({ ...formData, pinned: e.target.checked })}
                  className="rounded border-border w-5 h-5"
                />
                <label htmlFor="pinned" className="text-sm font-medium cursor-pointer">
                  Pin this post (appears at the top of the list)
                </label>
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
              <h3 className="text-3xl font-bold mb-3">Create Content</h3>
              <p className="text-muted-foreground text-lg">Write the content of your blog post</p>
            </div>

            <div className="max-w-5xl mx-auto">
              <label className="block text-sm font-medium mb-3">Blog Content *</label>
              <MDXEditor
                value={formData.content}
                onChange={(val) => setFormData({ ...formData, content: val })}
                height={500}
                placeholder="Write your blog content here...\n\nUse markdown formatting:\n# Headings\n## Subheadings\n- Lists\n**Bold text**\n*Italic text*\n`code snippets`"
              />
            </div>
          </motion.div>
        );

      case 4:
        const wordCount = formData.content.split(/\s+/).filter(word => word.length > 0).length;
        const tagsArray = formData.tags.split(',').map(tag => tag.trim()).filter(tag => tag.length > 0);
        
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
                <Check className="w-20 h-20 mx-auto mb-6 text-green-500" />
              </motion.div>
              <h3 className="text-3xl font-bold mb-3">Review & Publish</h3>
              <p className="text-muted-foreground text-lg">Review your blog post before publishing</p>
            </div>

            <div className="max-w-4xl mx-auto">
              <div className="grid md:grid-cols-2 gap-8">
                <div className="space-y-4">
                  <h4 className="text-xl font-semibold">Summary</h4>
                  <div className="space-y-3">
                    <div className="flex items-center justify-between p-3 bg-muted/50 rounded-lg">
                      <span className="font-medium">Title:</span>
                      <span className="font-semibold">{formData.title}</span>
                    </div>
                    <div className="flex items-center justify-between p-3 bg-muted/50 rounded-lg">
                      <span className="font-medium">Author:</span>
                      <Badge variant="outline">{formData.author || 'PoloCloud Team'}</Badge>
                    </div>
                    <div className="flex items-center justify-between p-3 bg-muted/50 rounded-lg">
                      <span className="font-medium">Pinned:</span>
                      <Badge variant={formData.pinned ? "secondary" : "outline"}>
                        {formData.pinned ? "Yes" : "No"}
                      </Badge>
                    </div>
                    <div className="flex items-center justify-between p-3 bg-muted/50 rounded-lg">
                      <span className="font-medium">Words:</span>
                      <span>{wordCount}</span>
                    </div>
                    {tagsArray.length > 0 && (
                      <div className="flex items-center justify-between p-3 bg-muted/50 rounded-lg">
                        <span className="font-medium">Tags:</span>
                        <div className="flex gap-1">
                          {tagsArray.map((tag) => (
                            <Badge key={tag} variant="outline" className="text-xs">
                              {tag}
                            </Badge>
                          ))}
                        </div>
                      </div>
                    )}
                  </div>
                </div>

                <div className="space-y-4">
                  <h4 className="text-xl font-semibold">Content Preview</h4>
                  <div className="p-4 border rounded-lg bg-muted/20 max-h-64 overflow-y-auto">
                    <h3 className="text-lg font-semibold mb-2">{formData.title}</h3>
                    <p className="text-muted-foreground mb-3">{formData.description}</p>
                    <div className="text-sm">
                      {formData.content.length > 200 
                        ? `${formData.content.substring(0, 200)}...`
                        : formData.content
                      }
                    </div>
                  </div>
                </div>
              </div>

              <div className="mt-8 p-4 bg-primary/5 border border-primary/20 rounded-lg">
                <h4 className="font-semibold text-primary mb-2">Ready to publish?</h4>
                <p className="text-sm text-muted-foreground">
                  This blog post will be published to GitHub and become visible to all users.
                </p>
              </div>
            </div>
          </motion.div>
        );

      default:
        return null;
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background">
      <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)] pointer-events-none" />
      
      <div className="relative z-10">
        <div className="pt-6 px-4">
          <div className="flex items-center justify-between">
            <Button
              variant="outline"
              onClick={() => router.back()}
              className="inline-flex items-center gap-2"
            >
              <ArrowLeft className="w-4 h-4" />
              <span>Back</span>
            </Button>

            <div className="flex items-center gap-3">
              <Button
                variant="ghost"
                size="sm"
                onClick={manualSave}
                className="inline-flex items-center gap-2"
              >
                <Save className="w-4 h-4" />
                <span>Save</span>
              </Button>
              
              <div className="flex items-center gap-2 text-sm">
                {autoSaveStatus === 'saving' && (
                  <div className="flex items-center gap-2 text-yellow-600">
                    <div className="w-3 h-3 border-2 border-yellow-600 border-t-transparent rounded-full animate-spin" />
                    <span>Saving...</span>
                  </div>
                )}
                {autoSaveStatus === 'saved' && lastSaved && (
                  <div className="flex items-center gap-2 text-green-600">
                    <Check className="w-3 h-3" />
                    <span>Saved {lastSaved.toLocaleTimeString()}</span>
                  </div>
                )}
                {autoSaveStatus === 'error' && (
                  <div className="flex items-center gap-2 text-red-600">
                    <X className="w-3 h-3" />
                    <span>Save failed</span>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>

        <div className="max-w-7xl mx-auto px-4 py-8">
          <div className="mb-12">
            <div className="flex items-center justify-center space-x-4">
              {steps.map((step, index) => (
                <div key={step.id} className="flex items-center">
                  <div
                    className={`flex items-center justify-center w-12 h-12 rounded-full border-2 transition-all duration-300 ${
                      currentStep >= step.id
                        ? 'border-primary bg-primary text-primary-foreground'
                        : 'border-border text-muted-foreground'
                    }`}
                  >
                    {currentStep > step.id ? (
                      <Check className="w-6 h-6" />
                    ) : (
                      <step.icon className="w-6 h-6" />
                    )}
                  </div>
                  {index < steps.length - 1 && (
                    <div
                      className={`w-16 h-0.5 transition-all duration-300 ${
                        currentStep > step.id ? 'bg-primary' : 'bg-border'
                      }`}
                    />
                  )}
                </div>
              ))}
            </div>
            
            <div className="text-center mt-4">
              <h2 className="text-2xl font-bold">
                {steps[currentStep - 1].title}
              </h2>
              <p className="text-muted-foreground">
                {steps[currentStep - 1].description}
              </p>
            </div>
          </div>

          <AnimatePresence mode="wait">
            {renderStepContent()}
          </AnimatePresence>

          <div className="flex items-center justify-between mt-12">
            <Button
              variant="outline"
              onClick={prevStep}
              disabled={currentStep === 1}
              className="flex items-center gap-2"
            >
              <ArrowLeft className="w-4 h-4" />
              <span>Previous</span>
            </Button>

            {currentStep < steps.length ? (
              <Button
                onClick={nextStep}
                disabled={!canProceed()}
                className="flex items-center gap-2"
              >
                <span>Next</span>
                <ArrowRight className="w-4 h-4" />
              </Button>
            ) : (
              <Button
                onClick={handleSubmit}
                disabled={creating}
                className="flex items-center gap-2 bg-green-600 hover:bg-green-700"
              >
                {creating ? (
                  <>
                    <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
                    <span>Publishing...</span>
                  </>
                ) : (
                  <>
                    <Check className="w-4 h-4" />
                    <span>Publish Blog Post</span>
                  </>
                )}
              </Button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

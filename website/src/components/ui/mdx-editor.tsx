'use client';

import { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { Eye, Code, Copy, Check, Link, List, Table, Quote, Hash, Bold, Italic, Code2, Minus, Plus } from 'lucide-react';
import { Button } from './button';
import { Badge } from './badge';

interface MDXEditorProps {
  value: string;
  onChange: (value: string) => void;
  height?: number;
  placeholder?: string;
}

export function MDXEditor({ value, onChange, height = 500, placeholder = 'Write your MDX content here...' }: MDXEditorProps) {
  const [isPreview, setIsPreview] = useState(false);
  const [copied, setCopied] = useState(false);
  const [showToolbar, setShowToolbar] = useState(false);

  const copyToClipboard = async () => {
    try {
      await navigator.clipboard.writeText(value);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    } catch (err) {
      console.error('Failed to copy text: ', err);
    }
  };

  const insertText = (before: string, after: string = '') => {
    const textarea = document.querySelector('textarea');
    if (!textarea) return;

    const start = textarea.selectionStart;
    const end = textarea.selectionEnd;
    const selectedText = value.substring(start, end);
    const newText = before + selectedText + after;
    
    const newValue = value.substring(0, start) + newText + value.substring(end);
    onChange(newValue);

    setTimeout(() => {
      textarea.focus();
      textarea.setSelectionRange(start + before.length, start + before.length + selectedText.length);
    }, 0);
  };

  const insertAtCursor = (text: string) => {
    const textarea = document.querySelector('textarea');
    if (!textarea) return;

    const start = textarea.selectionStart;
    const newValue = value.substring(0, start) + text + value.substring(start);
    onChange(newValue);
    
    setTimeout(() => {
      textarea.focus();
      textarea.setSelectionRange(start + text.length, start + text.length);
    }, 0);
  };

  const renderPreview = () => {
    let processedContent = value;

    processedContent = processedContent.replace(/```(\w+)?\n([\s\S]*?)```/g, (match, lang, code) => {
      const language = lang || 'text';
      return `<pre class="bg-muted p-4 rounded-lg overflow-x-auto my-4 border"><div class="flex items-center justify-between mb-2"><span class="text-xs font-mono text-muted-foreground">${language}</span></div><code class="text-sm font-mono">${code}</code></pre>`;
    });

    processedContent = processedContent.replace(/`([^`]+)`/g, '<code class="bg-muted px-1.5 py-0.5 rounded text-sm font-mono border">$1</code>');

    processedContent = processedContent.replace(/^###### (.*$)/gim, '<h6 class="text-base font-semibold mt-4 mb-2 text-muted-foreground">$1</h6>');
    processedContent = processedContent.replace(/^##### (.*$)/gim, '<h5 class="text-lg font-semibold mt-4 mb-2">$1</h5>');
    processedContent = processedContent.replace(/^#### (.*$)/gim, '<h4 class="text-xl font-semibold mt-4 mb-2">$1</h4>');
    processedContent = processedContent.replace(/^### (.*$)/gim, '<h3 class="text-2xl font-semibold mt-6 mb-3">$1</h3>');
    processedContent = processedContent.replace(/^## (.*$)/gim, '<h2 class="text-3xl font-bold mt-8 mb-4">$1</h2>');
    processedContent = processedContent.replace(/^# (.*$)/gim, '<h1 class="text-4xl font-bold mt-10 mb-6">$1</h1>');

    processedContent = processedContent.replace(/^\|(.+)\|$/gm, (match, content) => {
      const cells = content.split('|').map((cell: string) => cell.trim()).filter((cell: string) => cell);
      if (cells.length === 0) return match;
      
      const headerCells = cells.map((cell: string) => `<th class="border border-border px-3 py-2 text-left font-semibold">${cell}</th>`).join('');
      return `<tr>${headerCells}</tr>`;
    });

    processedContent = processedContent.replace(/^\|(.+)\|$/gm, (match, content) => {
      const cells = content.split('|').map((cell: string) => cell.trim()).filter((cell: string) => cell);
      if (cells.length === 0) return match;
      
      const dataCells = cells.map((cell: string) => `<td class="border border-border px-3 py-2">${cell}</td>`).join('');
      return `<tr>${dataCells}</tr>`;
    });

    processedContent = processedContent.replace(/(<tr>.*<\/tr>)/gs, '<table class="border-collapse border border-border my-4 w-full">$1</table>');

    processedContent = processedContent.replace(/^> (.*$)/gim, '<blockquote class="border-l-4 border-primary/30 pl-4 py-2 my-4 bg-muted/30 italic">$1</blockquote>');

    processedContent = processedContent.replace(/^- (.*$)/gim, '<li class="ml-4 mb-1 flex items-start"><span class="text-primary mr-2 mt-1">•</span>$1</li>');
    processedContent = processedContent.replace(/^\d+\. (.*$)/gim, '<li class="ml-4 mb-1 flex items-start"><span class="text-primary mr-2 mt-1">1.</span>$1</li>');

    processedContent = processedContent.replace(/(<li.*<\/li>)/gs, '<ul class="my-4">$1</ul>');

    processedContent = processedContent.replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2" class="text-primary hover:underline" target="_blank" rel="noopener noreferrer">$1</a>');

    processedContent = processedContent.replace(/!\[([^\]]*)\]\(([^)]+)\)/g, '<img src="$2" alt="$1" class="max-w-full h-auto rounded-lg border my-4" />');

    processedContent = processedContent.replace(/^---$/gm, '<hr class="border-t border-border my-6" />');

    processedContent = processedContent.replace(/\*\*\*(.*?)\*\*\*/g, '<strong class="font-bold"><em class="italic">$1</em></strong>');
    processedContent = processedContent.replace(/\*\*(.*?)\*\*/g, '<strong class="font-bold">$1</strong>');
    processedContent = processedContent.replace(/\*(.*?)\*/g, '<em class="italic">$1</em>');

    processedContent = processedContent.replace(/~~(.*?)~~/g, '<del class="line-through text-muted-foreground">$1</del>');

    processedContent = processedContent.replace(/:([\w+-]+):/g, (match, emoji) => {
      const emojiMap: { [key: string]: string } = {
        'smile': '😊', 'heart': '❤️', 'thumbsup': '👍', 'fire': '🔥', 'rocket': '🚀',
        'star': '⭐', 'check': '✅', 'warning': '⚠️', 'bug': '🐛', 'sparkles': '✨'
      };
      return emojiMap[emoji] || match;
    });

    processedContent = processedContent.replace(/\n\n/g, '</p><p class="mb-3 leading-relaxed">');
    processedContent = processedContent.replace(/^(.+)$/gm, '<p class="mb-3 leading-relaxed">$1</p>');

    processedContent = processedContent.replace(/<p class="mb-3 leading-relaxed"><\/p>/g, '');

    return (
      <div 
        className="prose prose-sm max-w-none dark:prose-invert prose-headings:text-foreground prose-p:text-foreground prose-strong:text-foreground prose-em:text-foreground prose-code:text-foreground prose-pre:bg-muted prose-pre:border prose-pre:border-border"
        dangerouslySetInnerHTML={{ __html: processedContent }}
      />
    );
  };

  return (
    <div className="border rounded-lg overflow-hidden shadow-lg bg-background">
      <div className="flex items-center justify-between p-3 border-b bg-muted/50">
        <div className="flex items-center space-x-2">
          <Badge variant={isPreview ? "secondary" : "default"}>
            <Code className="w-3 h-3 mr-1" />
            Editor
          </Badge>
          <Badge variant={isPreview ? "default" : "secondary"}>
            <Eye className="w-3 h-3 mr-1" />
            Preview
          </Badge>
        </div>
        
        <div className="flex items-center space-x-2">
          <div className="flex items-center space-x-1 border-r pr-2 mr-2">
            <Button
              variant="ghost"
              size="sm"
              onClick={() => insertText('**', '**')}
              className="h-8 w-8 p-0 hover:bg-muted"
              title="Bold (Ctrl+B)"
            >
              <Bold className="w-3 h-3" />
            </Button>
            <Button
              variant="ghost"
              size="sm"
              onClick={() => insertText('*', '*')}
              className="h-8 w-8 p-0 hover:bg-muted"
              title="Italic (Ctrl+I)"
            >
              <Italic className="w-3 h-3" />
            </Button>
            <Button
              variant="ghost"
              size="sm"
              onClick={() => insertText('`', '`')}
              className="h-8 w-8 p-0 hover:bg-muted"
              title="Inline Code"
            >
              <Code2 className="w-3 h-3" />
            </Button>
            <Button
              variant="ghost"
              size="sm"
              onClick={() => insertText('~~', '~~')}
              className="h-8 w-8 p-0 hover:bg-muted"
              title="Strikethrough"
            >
              <Minus className="w-3 h-3" />
            </Button>
          </div>

          <div className="flex items-center space-x-1 border-r pr-2 mr-2">
            <Button
              variant="ghost"
              size="sm"
              onClick={() => insertAtCursor('# ')}
              className="h-8 w-8 p-0 hover:bg-muted"
              title="Heading 1"
            >
              <Hash className="w-3 h-3" />
            </Button>
            <Button
              variant="ghost"
              size="sm"
              onClick={() => insertAtCursor('- ')}
              className="h-8 w-8 p-0 hover:bg-muted"
              title="Unordered List"
            >
              <List className="w-3 h-3" />
            </Button>
            <Button
              variant="ghost"
              size="sm"
              onClick={() => insertAtCursor('> ')}
              className="h-8 w-8 p-0 hover:bg-muted"
              title="Quote"
            >
              <Quote className="w-3 h-3" />
            </Button>
            <Button
              variant="ghost"
              size="sm"
              onClick={() => insertAtCursor('| Column 1 | Column 2 |\n|----------|----------|\n| Data 1   | Data 2   |')}
              className="h-8 w-8 p-0 hover:bg-muted"
              title="Table"
            >
              <Table className="w-3 h-3" />
            </Button>
          </div>

          <div className="flex items-center space-x-1 border-r pr-2 mr-2">
            <Button
              variant="ghost"
              size="sm"
              onClick={() => insertText('[', '](url)')}
              className="h-8 w-8 p-0 hover:bg-muted"
              title="Link"
            >
              <Link className="w-3 h-3" />
            </Button>
            <Button
              variant="ghost"
              size="sm"
              onClick={() => insertText('```\n', '\n```')}
              className="h-8 w-8 p-0 hover:bg-muted"
              title="Code Block"
            >
              <Code className="w-3 h-3" />
            </Button>
          </div>

          <Button
            variant="outline"
            size="sm"
            onClick={() => setIsPreview(!isPreview)}
            className="flex items-center space-x-2"
          >
            {isPreview ? (
              <>
                <Code className="w-4 h-4" />
                <span>Editor</span>
              </>
            ) : (
              <>
                <Eye className="w-4 h-4" />
                <span>Preview</span>
              </>
            )}
          </Button>
          
          <Button
            variant="outline"
            size="sm"
            onClick={copyToClipboard}
            className="flex items-center space-x-2"
          >
            {copied ? (
              <>
                <Check className="w-4 h-4 text-green-600" />
                <span>Copied!</span>
              </>
            ) : (
              <>
                <Copy className="w-4 h-4" />
                <span>Copy</span>
              </>
            )}
          </Button>
        </div>
      </div>

      <div className="flex" style={{ height }}>
        <motion.div
          className={`${isPreview ? 'w-1/2' : 'w-full'} transition-all duration-300`}
          initial={false}
          animate={{ width: isPreview ? '50%' : '100%' }}
        >
          <textarea
            value={value}
            onChange={(e) => onChange(e.target.value)}
            placeholder={placeholder}
            className="w-full h-full p-4 resize-none border-0 focus:ring-0 focus:outline-none bg-background text-foreground font-mono text-sm leading-relaxed"
            style={{ height: height - 60 }}
            onKeyDown={(e) => {
              if (e.ctrlKey || e.metaKey) {
                switch (e.key) {
                  case 'b':
                    e.preventDefault();
                    insertText('**', '**');
                    break;
                  case 'i':
                    e.preventDefault();
                    insertText('*', '*');
                    break;
                  case 'k':
                    e.preventDefault();
                    insertText('[', '](url)');
                    break;
                }
              }

              if (e.key === 'Tab') {
                e.preventDefault();
                const start = e.currentTarget.selectionStart;
                const end = e.currentTarget.selectionEnd;
                const newValue = value.substring(0, start) + '  ' + value.substring(end);
                onChange(newValue);
                setTimeout(() => {
                  e.currentTarget.setSelectionRange(start + 2, start + 2);
                }, 0);
              }
            }}
          />
        </motion.div>

        <motion.div
          className={`${isPreview ? 'w-1/2' : 'w-0'} overflow-hidden transition-all duration-300`}
          initial={false}
          animate={{ width: isPreview ? '50%' : '0%' }}
        >
          <div className="h-full p-4 overflow-y-auto border-l bg-muted/20">
            {value.trim() ? (
              renderPreview()
            ) : (
              <div className="text-center text-muted-foreground mt-20">
                <Eye className="w-12 h-12 mx-auto mb-4 opacity-50" />
                <p>Start typing to see the preview</p>
                <p className="text-xs mt-2">Use the toolbar above for quick formatting</p>
              </div>
            )}
          </div>
        </motion.div>
      </div>

      <div className="flex items-center justify-between p-3 border-t bg-muted/30 text-xs text-muted-foreground">
        <div className="flex items-center space-x-4">
          <span>Words: {value.split(/\s+/).filter(word => word.length > 0).length}</span>
          <span>Characters: {value.length}</span>
          <span>Lines: {value.split('\n').length}</span>
        </div>
        <div className="flex items-center space-x-2">
          <span>MDX Editor</span>
          <span>•</span>
          <span>Ctrl+B: Bold</span>
          <span>•</span>
          <span>Ctrl+I: Italic</span>
          <span>•</span>
          <span>Ctrl+K: Link</span>
        </div>
      </div>
    </div>
  );
}

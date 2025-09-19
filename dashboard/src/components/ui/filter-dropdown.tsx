'use client';

import { useState, useRef, useEffect } from 'react';
import { ChevronDown, Check } from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';

interface FilterDropdownProps {
    value: string;
    onChange: (value: string) => void;
    options: { value: string; label: string }[];
    placeholder: string;
    className?: string;
}

export function FilterDropdown({
    value,
    onChange,
    options,
    placeholder,
    className = ""
}: FilterDropdownProps) {
    const [isOpen, setIsOpen] = useState(false);
    const dropdownRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
                setIsOpen(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    const selectedOption = options.find(option => option.value === value);

    return (
        <div className={`relative ${className}`} ref={dropdownRef}>
            <button
                type="button"
                onClick={() => setIsOpen(!isOpen)}
                className="group flex items-center justify-between w-full px-3 py-2 h-9 text-sm bg-background/50 border border-border/50 rounded-lg text-foreground hover:bg-background/70 hover:border-border/70 transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-[oklch(75.54%_0.1534_231.639,0.2)] focus:border-[oklch(75.54%_0.1534_231.639)]"
            >
                <span className={`transition-colors duration-200 ${selectedOption ? 'text-foreground' : 'text-muted-foreground'}`}>
                    {selectedOption ? selectedOption.label : placeholder}
                </span>
                <motion.div
                    animate={{ rotate: isOpen ? 180 : 0 }}
                    transition={{ duration: 0.2, ease: "easeOut" }}
                    className="transition-colors duration-200 group-hover:text-foreground"
                >
                    <ChevronDown className="w-4 h-4 text-muted-foreground" />
                </motion.div>
            </button>

            <AnimatePresence>
                {isOpen && (
                    <motion.div
                        initial={{ opacity: 0, y: -4 }}
                        animate={{ opacity: 1, y: 0 }}
                        exit={{ opacity: 0, y: -4 }}
                        transition={{ duration: 0.15, ease: "easeOut" }}
                        className="absolute top-full left-0 right-0 mt-1 z-50 bg-card/90 border border-border/30 rounded-lg shadow-lg backdrop-blur-sm"
                    >
                        <div className="max-h-48 overflow-y-auto">
                            {options.map((option) => (
                                <button
                                    key={option.value}
                                    type="button"
                                    onClick={() => {
                                        onChange(option.value);
                                        setIsOpen(false);
                                    }}
                                    className={`w-full px-3 py-2 text-sm text-left transition-all duration-150 flex items-center justify-between hover:bg-[oklch(75.54%_0.1534_231.639,0.06)] ${
                                        option.value === value
                                            ? 'bg-[oklch(75.54%_0.1534_231.639,0.1)] text-[oklch(75.54%_0.1534_231.639)]'
                                            : 'text-foreground'
                                    }`}
                                >
                                    <span>{option.label}</span>
                                    {option.value === value && (
                                        <Check className="w-4 h-4 text-[oklch(75.54%_0.1534_231.639)]" />
                                    )}
                                </button>
                            ))}
                        </div>
                    </motion.div>
                )}
            </AnimatePresence>
        </div>
    );
}

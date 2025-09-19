'use client';

import { motion } from 'framer-motion';
import { Button } from '@/components/ui/button';
import { Search } from 'lucide-react';

interface GroupEmptyStateProps {
    searchTerm: string;
    selectedPlatform: string;
    onClearFilters: () => void;
}

export function GroupEmptyState({ searchTerm, selectedPlatform, onClearFilters }: GroupEmptyStateProps) {
    const hasActiveFilters = searchTerm || selectedPlatform !== 'all';

    return (
        <motion.div 
            className="text-center py-12"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
        >
            <div className="mx-auto w-16 h-16 bg-muted/20 rounded-full flex items-center justify-center mb-4">
                <Search className="w-8 h-8 text-muted-foreground" />
            </div>
            
            <h3 className="text-lg font-semibold text-foreground mb-2">
                No groups found
            </h3>
            
            <p className="text-muted-foreground mb-6 max-w-md mx-auto">
                {hasActiveFilters 
                    ? "No groups match your current filters. Try adjusting your search criteria or clearing the filters."
                    : "Get started by creating your first group to organize your services."
                }
            </p>

            {hasActiveFilters && (
                <Button
                    variant="outline"
                    onClick={onClearFilters}
                    className="border-border/50 text-foreground hover:bg-background/50 hover:border-border transition-all duration-200"
                >
                    Clear Filters
                </Button>
            )}
        </motion.div>
    );
}

'use client';

import { Button } from '@/components/ui/button';
import { Server } from 'lucide-react';

interface ServiceEmptyStateProps {
    searchTerm: string;
    selectedGroup: string;
    selectedType: string;
    onClearFilters: () => void;
}

export function ServiceEmptyState({
    searchTerm,
    selectedGroup,
    selectedType,
    onClearFilters
}: ServiceEmptyStateProps) {
    const hasActiveFilters = searchTerm || selectedGroup !== 'all' || selectedType !== 'all';

    return (
        <div className="text-center py-12">
            <div className="space-y-4">
                <Server className="w-16 h-16 text-muted-foreground mx-auto opacity-50" />
                <div>
                    <h3 className="text-lg font-semibold text-foreground">
                        No services found
                    </h3>
                    <p className="text-muted-foreground mt-1">
                        {hasActiveFilters
                            ? 'Try adjusting your search or filters'
                            : 'No services are currently running'
                        }
                    </p>
                </div>
                
                {hasActiveFilters && (
                    <Button
                        onClick={onClearFilters}
                        variant="outline"
                    >
                        Clear Filters
                    </Button>
                )}
            </div>
        </div>
    );
}

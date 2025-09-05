'use client';

import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { FilterDropdown } from '@/components/ui/filter-dropdown';
import { motion } from 'framer-motion';
import { Search, Plus } from 'lucide-react';
import { usePermissions } from '@/hooks/usePermissions';

interface GroupFiltersProps {
    searchTerm: string;
    setSearchTerm: (value: string) => void;
    selectedPlatform: string;
    setSelectedPlatform: (value: string) => void;
    availablePlatforms: string[];
    onCreateClick: () => void;
}

export function GroupFilters({
    searchTerm,
    setSearchTerm,
    selectedPlatform,
    setSelectedPlatform,
    availablePlatforms,
    onCreateClick
}: GroupFiltersProps) {
    const { hasPermission } = usePermissions();

    const canCreateGroup = hasPermission('polocloud.group.create');
    
    return (
        <motion.div 
            className="flex flex-col sm:flex-row gap-4"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.3 }}
        >
            <div className="flex-1">
                <div className="relative">
                    <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                    <Input
                        placeholder="Search groups by name or platform..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="pl-10 h-9 text-sm border-border/50 focus:border-[oklch(75.54% 0.1534 231.639)] focus:ring-2 focus:ring-[oklch(75.54% 0.1534 231.639,0.2)] transition-all duration-200"
                    />
                </div>
            </div>

            <div className="flex gap-3">
                <FilterDropdown
                    value={selectedPlatform}
                    onChange={setSelectedPlatform}
                    options={[
                        { value: 'all', label: 'All Platforms' },
                        ...availablePlatforms.map(platform => ({ value: platform, label: platform }))
                    ]}
                    placeholder="All Platforms"
                    className="w-40"
                />

                <Button
                    onClick={onCreateClick}
                    disabled={!canCreateGroup}
                    className={`h-9 px-4 text-sm font-medium transition-all duration-200 ${
                        canCreateGroup 
                            ? 'hover:opacity-90 shadow-lg shadow-[0_0_20px_rgba(75.54%,15.34%,231.639,0.3)] hover:shadow-[0_0_30px_rgba(75.54%,15.34%,231.639,0.4)]' 
                            : 'opacity-40 cursor-not-allowed bg-muted text-muted-foreground border border-border/30'
                    }`}
                    style={{ 
                        backgroundColor: canCreateGroup ? 'oklch(75.54% .1534 231.639)' : undefined
                    }}
                >
                    <Plus className="w-4 h-4 mr-2" />
                    Create Group
                </Button>
            </div>
        </motion.div>
    );
}

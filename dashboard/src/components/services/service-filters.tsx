'use client';

import { Input } from '@/components/ui/input';
import { FilterDropdown } from '@/components/ui/filter-dropdown';
import { motion } from 'framer-motion';
import { Search } from 'lucide-react';

interface ServiceFiltersProps {
    searchTerm: string;
    setSearchTerm: (term: string) => void;
    selectedGroup: string;
    setSelectedGroup: (group: string) => void;
    selectedType: string;
    setSelectedType: (type: string) => void;
    availableGroups: string[];
    availableTypes: string[];
}

export function ServiceFilters({
    searchTerm,
    setSearchTerm,
    selectedGroup,
    setSelectedGroup,
    selectedType,
    setSelectedType,
    availableGroups,
    availableTypes
}: ServiceFiltersProps) {
    return (
        <motion.div 
            className="flex flex-col sm:flex-row gap-4"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.3 }}
        >
            {}
            <div className="flex-1">
                <div className="relative">
                    <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                    <Input
                        placeholder="Search services by name or group..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="pl-10 h-9 text-sm border-border/50 focus:border-[oklch(75.54% 0.1534 231.639)] focus:ring-2 focus:ring-[oklch(75.54% 0.1534 231.639,0.2)] transition-all duration-200"
                    />
                </div>
            </div>

            {}
            <div className="flex gap-3">
                <FilterDropdown
                    value={selectedGroup}
                    onChange={(value) => setSelectedGroup(value)}
                    options={[
                        { value: 'all', label: 'All Groups' },
                        ...availableGroups.map(group => ({ value: group, label: group }))
                    ]}
                    placeholder="All Groups"
                    className="w-40"
                />

                <FilterDropdown
                    value={selectedType}
                    onChange={(value) => setSelectedType(value)}
                    options={[
                        { value: 'all', label: 'All Types' },
                        ...availableTypes.map(type => ({ value: type, label: type }))
                    ]}
                    placeholder="All Types"
                    className="w-40"
                />
            </div>
        </motion.div>
    );
}

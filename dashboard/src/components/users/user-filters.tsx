'use client';

import { Input } from '@/components/ui/input';
import { motion } from 'framer-motion';
import { Search } from 'lucide-react';
import { AddUserModal } from '@/components/add-user-modal';

interface UserFiltersProps {
    searchTerm: string;
    setSearchTerm: (value: string) => void;
    onUserAdded: (username: string, password: string) => void;
}

export function UserFilters({
    searchTerm,
    setSearchTerm,
    onUserAdded
}: UserFiltersProps) {
    return (
        <motion.div 
            className="flex flex-col sm:flex-row gap-4"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.2 }}
        >
            <div className="flex-1">
                <div className="relative">
                    <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                    <Input
                        placeholder="Search users..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="pl-10 h-9 text-sm border-border/50 focus:border-[oklch(75.54% 0.1534 231.639)] focus:ring-2 focus:ring-[oklch(75.54% 0.1534 231.639,0.2)] transition-all duration-200"
                    />
                </div>
            </div>

            <div className="flex gap-3">
                <AddUserModal onUserAdded={onUserAdded} />
            </div>
        </motion.div>
    );
}

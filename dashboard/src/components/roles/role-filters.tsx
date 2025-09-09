'use client';

import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { motion } from 'framer-motion';
import { Search, Plus } from 'lucide-react';
import { CreateRoleModal } from '@/components/create-role-modal';
import { usePermissions } from '@/hooks/usePermissions';

interface RoleFiltersProps {
    searchTerm: string;
    setSearchTerm: (value: string) => void;
    onRoleCreated?: () => void;
}

export function RoleFilters({
    searchTerm,
    setSearchTerm,
    onRoleCreated
}: RoleFiltersProps) {
    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

    const { hasPermission } = usePermissions();
    const canCreateRole = hasPermission('polocloud.role.create');

    const handleCreateClick = () => {
        setIsCreateModalOpen(true);
    };

    return (
        <>
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
                        placeholder="Search roles..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="pl-10 h-9 text-sm border-border/50 focus:border-[oklch(75.54% 0.1534 231.639)] focus:ring-2 focus:ring-[oklch(75.54% 0.1534 231.639,0.2)] transition-all duration-200"
                    />
                </div>
            </div>

            <div className="flex gap-3">
                <Button
                    onClick={handleCreateClick}
                    disabled={!canCreateRole}
                    className={`h-9 px-4 text-sm font-medium transition-all duration-200 ${
                        canCreateRole 
                            ? 'hover:opacity-90 shadow-lg shadow-[0_0_20px_rgba(75.54%,15.34%,231.639,0.3)] hover:shadow-[0_0_30px_rgba(75.54%,15.34%,231.639,0.4)]' 
                            : 'opacity-40 cursor-not-allowed bg-muted text-muted-foreground border border-border/30'
                    }`}
                    style={{ 
                        backgroundColor: canCreateRole ? 'oklch(75.54% .1534 231.639)' : undefined
                    }}
                >
                    <Plus className="w-4 h-4 mr-2" />
                    Create Role
                </Button>
            </div>
        </motion.div>

        {}
        <CreateRoleModal 
            isOpen={isCreateModalOpen}
            onOpenChange={setIsCreateModalOpen}
            onRoleAdded={() => {
                if (onRoleCreated) {
                    onRoleCreated();
                }
                setIsCreateModalOpen(false);
            }}
        />
        </>
    );
}

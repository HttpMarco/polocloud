'use client';

import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { motion } from 'framer-motion';
import { Trash2, Loader2 } from 'lucide-react';
import { API_ENDPOINTS } from '@/lib/api';
import { toast } from 'sonner';
import { usePermissions } from '@/hooks/usePermissions';

interface Role {
    id: string;
    label: string;
    hexColor: string;
    default: boolean;
    userCount: number;
    permissions?: string[];
}

interface RoleDeleteButtonProps {
    role: Role;
    onRoleDeleted: (id: string) => void;
}

export function RoleDeleteButton({ role, onRoleDeleted }: RoleDeleteButtonProps) {
    const [isDeleting, setIsDeleting] = useState(false);

    const { hasPermission } = usePermissions();
    const canDeleteRole = hasPermission('polocloud.role.delete');

    const handleDelete = async () => {
        if (role.default) {
            toast.error('Default roles cannot be deleted!');
            return;
        }
        
        setIsDeleting(true);
        try {
            const response = await fetch(API_ENDPOINTS.ROLE.DELETE(parseInt(role.id)), { 
                method: 'DELETE' 
            });
            if (response.ok) {
                onRoleDeleted(role.id);
            } else {

            }
        } catch {
        } finally {
            setIsDeleting(false);
        }
    };

    return (
        <motion.div 
            whileHover={{ scale: (canDeleteRole && !role.default) ? 1.05 : 1 }} 
            whileTap={{ scale: (canDeleteRole && !role.default) ? 0.95 : 1 }}
        >
            <Button 
                variant="ghost" 
                size="sm" 
                onClick={handleDelete} 
                disabled={!canDeleteRole || isDeleting || role.default} 
                className={`h-10 w-10 ${
                    !canDeleteRole 
                        ? 'opacity-40 cursor-not-allowed text-muted-foreground' 
                        : 'text-red-600 hover:text-red-700 hover:bg-red-50'
                }`}
            >
                {isDeleting ? (
                    <Loader2 className="w-5 h-5 animate-spin" />
                ) : (
                    <Trash2 className="w-5 h-5" />
                )}
            </Button>
        </motion.div>
    );
}


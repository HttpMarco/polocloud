'use client';

import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { motion } from 'framer-motion';
import { Trash2, Loader2 } from 'lucide-react';
import { API_ENDPOINTS } from '@/lib/api';

interface User {
    uuid: string;
    username: string;
    createdAt: number;
    role: number;
}

interface UserDeleteButtonProps {
    user: User;
    onUserDeleted: (uuid: string) => void;
    disabled?: boolean;
}

export function UserDeleteButton({ user, onUserDeleted, disabled = false }: UserDeleteButtonProps) {
    const [isDeleting, setIsDeleting] = useState(false);

    const handleDelete = async () => {
        setIsDeleting(true);
        
        try {
            const response = await fetch(`${API_ENDPOINTS.USER.DELETE(user.uuid)}`, {
                method: 'DELETE',
            });

            if (response.ok) {

                onUserDeleted(user.uuid);
            } else {

            }
        } catch {
        } finally {
            setIsDeleting(false);
        }
    };

    return (
        <motion.div 
            whileHover={{ scale: (disabled || isDeleting) ? 1 : 1.05 }} 
            whileTap={{ scale: (disabled || isDeleting) ? 1 : 0.95 }}
        >
            <Button 
                variant="ghost" 
                size="sm" 
                onClick={handleDelete} 
                disabled={disabled || isDeleting} 
                className={`h-10 w-10 ${
                    disabled 
                        ? 'opacity-40 cursor-not-allowed text-muted-foreground' 
                        : 'text-red-600 hover:text-red-700 hover:bg-red-50'
                }`}
            >
                {isDeleting ? (
                    <Loader2 className="w-4 h-4 animate-spin" />
                ) : (
                    <Trash2 className="w-5 h-5" />
                )}
            </Button>
        </motion.div>
    );
}

'use client';

import { EditUserModal } from '@/components/edit-user-modal';
import { UserDeleteButton } from '@/components/users/user-delete-button';
import { usePermissions } from '@/hooks/usePermissions';

interface User {
    uuid: string;
    username: string;
    createdAt: number;
    role: number;
}

interface UserActionsProps {
    user: User;
    onUserEdited: () => void;
    onUserDeleted: (uuid: string) => void;
}

export function UserActions({ user, onUserEdited, onUserDeleted }: UserActionsProps) {
    const { hasPermission } = usePermissions();
    const canEditUser = hasPermission('polocloud.user.edit');
    const canDeleteUser = hasPermission('polocloud.user.delete');
    
    return (
        <div className="flex items-center space-x-2">
            <EditUserModal 
                user={user} 
                onUserEdited={onUserEdited}
                disabled={!canEditUser}
            />
            <UserDeleteButton 
                user={user} 
                onUserDeleted={onUserDeleted}
                disabled={!canDeleteUser}
            />
        </div>
    );
}

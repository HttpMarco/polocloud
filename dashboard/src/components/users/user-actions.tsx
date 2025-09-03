'use client';

import { EditUserModal } from '@/components/edit-user-modal';
import { UserDeleteButton } from '@/components/users/user-delete-button';

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
    return (
        <div className="flex items-center space-x-2">
            <EditUserModal user={user} onUserEdited={onUserEdited} />
            <UserDeleteButton user={user} onUserDeleted={onUserDeleted} />
        </div>
    );
}

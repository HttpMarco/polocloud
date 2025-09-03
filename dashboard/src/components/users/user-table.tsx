'use client';

import { motion } from 'framer-motion';
import { DataTable } from '@/components/data-table';
import { ColumnDef } from '@tanstack/react-table';

interface User {
    uuid: string;
    username: string;
    createdAt: number;
    role: number;
}

interface UserTableProps {
    users: User[];
    columnsWithCallbacks: ColumnDef<User>[];
}

export function UserTable({ users, columnsWithCallbacks }: UserTableProps) {
    return (
        <div className="flex-1 px-6 pb-6 overflow-auto modern-scrollbar">
            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6, delay: 0.3 }}
            >
                <DataTable 
                    columns={columnsWithCallbacks} 
                    data={users}
                    showToolbar={false}
                />
            </motion.div>
        </div>
    );
}

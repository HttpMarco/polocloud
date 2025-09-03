'use client';

import { motion } from 'framer-motion';
import { DataTable } from '@/components/data-table';
import { ColumnDef } from '@tanstack/react-table';

interface Role {
    id: string;
    label: string;
    hexColor: string;
    default: boolean;
    userCount: number;
    permissions?: string[];
}

interface RoleTableProps {
    roles: Role[];
    columnsWithCallbacks: ColumnDef<Role>[];
}

export function RoleTable({ roles, columnsWithCallbacks }: RoleTableProps) {
    return (
        <div className="px-6 pb-6">
            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6, delay: 0.3 }}
            >
                <DataTable 
                    columns={columnsWithCallbacks} 
                    data={roles}
                    showToolbar={false}
                />
            </motion.div>
        </div>
    );
}


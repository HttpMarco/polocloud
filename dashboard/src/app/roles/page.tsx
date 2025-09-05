"use client"
import { useState, useEffect } from 'react'

import { columns } from './columns'





import { API_ENDPOINTS } from '@/lib/api'
import GlobalNavbar from '@/components/global-navbar'
import { RoleHeader } from '@/components/roles/role-header'
import { RoleFilters } from '@/components/roles/role-filters'
import { RoleTable } from '@/components/roles/role-table'
import { RoleDeleteButton } from '@/components/roles/role-delete-button'



interface Role { 
  id: string; 
  label: string; 
  hexColor: string; 
  default: boolean; 
  userCount: number;
  permissions?: string[];
}



export default function RolesPage() {
  const [roles, setRoles] = useState<Role[]>([])
  const [searchTerm, setSearchTerm] = useState('')


  const fetchRoles = async () => {
    try {
      const response = await fetch(API_ENDPOINTS.ROLE.LIST)
      if (response.ok) {
        const data = await response.json()
        setRoles(data)
      } else {
      }
    } catch {}
  }

  useEffect(() => {
    fetchRoles()
  }, [])

  const handleRoleDeleted = (id: string) => {
    setRoles(prev => prev.filter(role => role.id !== id))
  }







  const columnsWithCallbacks = columns.map(col =>
    col.id === 'actions'
      ? { 
          ...col, 
          cell: ({ row }: { row: { original: Role } }) => {
            const role = row.original
            return <RoleDeleteButton role={role} onRoleDeleted={handleRoleDeleted} />
          }
        }
      : col
  )

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20 ultra-smooth-scroll">
      {}
      <GlobalNavbar />
      
      {}
      <div className="h-2"></div>
      

      
      {}
      
      
      {}
      <RoleHeader />
      
      {}
      <div className="px-6 pb-8">
        <RoleFilters
          searchTerm={searchTerm}
          setSearchTerm={setSearchTerm}
          onRoleCreated={fetchRoles}
        />
      </div>
      
      {}
      <RoleTable 
        roles={roles}
        columnsWithCallbacks={columnsWithCallbacks}
      />


    </div>
  )
}

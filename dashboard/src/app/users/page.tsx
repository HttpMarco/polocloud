"use client"

import { useState, useEffect } from 'react'

import { columns } from './columns'
import { API_ENDPOINTS } from '@/lib/api'
import GlobalNavbar from '@/components/global-navbar'
import { UserHeader } from '@/components/users/user-header'
import { UserFilters } from '@/components/users/user-filters'
import { UserTable } from '@/components/users/user-table'

import { UserActions } from '@/components/users/user-actions'


interface User { 
  uuid: string; 
  username: string; 
  createdAt: number; 
  role: number 
}

export default function UsersPage() {
  const [users, setUsers] = useState<User[]>([])
  const [searchTerm, setSearchTerm] = useState('')

  const fetchUsers = async () => {
    try {
      const response = await fetch(API_ENDPOINTS.USER.LIST)
      if (response.ok) {
          const data = await response.json()
          setUsers(data)
      }
    } catch {}
  }

  useEffect(() => {
    fetchUsers()
  }, [])

  const handleUserAdded = async () => {

    await fetchUsers()
  }

  const handleUserDeleted = (uuid: string) => {
    setUsers(prev => prev.filter(user => user.uuid !== uuid))
  }

  const handleUserEdited = () => {

    fetchUsers()
  }

  const columnsWithCallbacks = columns.map(col =>
    col.id === 'actions'
      ? { 
          ...col, 
          cell: ({ row }: { row: { original: User } }) => {
            const user = row.original
            return (
              <UserActions 
                user={user} 
                onUserEdited={handleUserEdited} 
                onUserDeleted={handleUserDeleted} 
              />
            )
          }
        }
      : col
  )

      return (
        <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20 ultra-smooth-scroll">
            {}
            <GlobalNavbar />
            
            <div className="h-2"></div>
            
            {}
            <UserHeader />
      
      {}
      <div className="px-6 pb-8">
        <UserFilters
          searchTerm={searchTerm}
          setSearchTerm={setSearchTerm}
          onUserAdded={handleUserAdded}
        />
      </div>
      
      {}
      <UserTable 
        users={users}
        columnsWithCallbacks={columnsWithCallbacks}
      />
    </div>
  )
}



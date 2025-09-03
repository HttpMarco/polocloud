"use client"

import { ColumnDef } from "@tanstack/react-table"
import { Button } from "@/components/ui/button"
import { Trash2 } from "lucide-react"
import { useState, useEffect } from "react"
import Image from "next/image"

interface User { 
  uuid: string; 
  username: string; 
  createdAt: number; 
  role: number 
}

interface Role { 
  id: number; 
  label: string; 
  hexColor: string 
}

function RoleDisplay({ roleId }: { roleId: number }) {
  const [role, setRole] = useState<Role | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    const fetchRole = async () => {
      try {
        const response = await fetch(`/api/role/${roleId}`)
        if (response.ok) {
          const roleData = await response.json()
          setRole(roleData)
        }
      } catch {
      } finally {
        setIsLoading(false)
      }
    }

    fetchRole()
  }, [roleId])

  if (isLoading) {
    return <div className="px-2 py-1 text-xs rounded-full inline-block font-medium bg-gray-100 text-gray-600">Loading...</div>
  }

  if (!role) {
    return <div className="px-2 py-1 text-xs rounded-full inline-block font-medium bg-gray-100 text-gray-600">Unknown</div>
  }

  return (
    <div 
      className="px-2 py-1 text-xs rounded-full inline-block font-medium" 
      style={{ 
        backgroundColor: `${role.hexColor}20`, 
        color: role.hexColor 
      }}
    >
      {role.label}
    </div>
  )
}

export const columns: ColumnDef<User>[] = [
  {
    accessorKey: "username", 
    header: "Username", 
    size: 250,
    cell: ({ row }) => {
      const username = row.getValue("username") as string
      return (
        <div className="flex items-center space-x-3">
          <div className="w-8 h-8 rounded-lg overflow-hidden border border-border">
            <Image 
              src={`https://mineskin.eu/helm/${username}/64`} 
              alt={`${username} Minecraft Head`} 
              width={32}
              height={32}
              className="w-8 h-8"
              onError={(e) => {
                e.currentTarget.src = 'https://mineskin.eu/helm/MHF_Question/64'
              }}
            />
          </div>
          <div className="font-medium text-sm">{username}</div>
        </div>
      )
    },
  },
  { 
    accessorKey: "uuid", 
    header: "UUID", 
    size: 300, 
    cell: ({ row }) => (
      <div className="font-mono text-xs text-muted-foreground">
        {row.getValue("uuid")}
      </div>
    ), 
  },
  { 
    accessorKey: "role", 
    header: "Role", 
    size: 150, 
    cell: ({ row }) => {
      const roleId = row.getValue("role") as number
      return <RoleDisplay roleId={roleId} />
    }, 
  },
  { 
    accessorKey: "createdAt", 
    header: "Created", 
    size: 200, 
    cell: ({ row }) => {
      const timestamp = row.getValue("createdAt") as number
      const date = new Date(timestamp)
      return (
        <div className="text-xs text-muted-foreground">
          {date.toLocaleDateString('de-DE')} {date.toLocaleTimeString('de-DE', { hour: '2-digit', minute: '2-digit' })}
        </div>
      )
    }, 
  },
  {
    id: "actions", 
    header: "Actions", 
    size: 100,
    cell: () => {
      return (
        <Button variant="ghost" size="sm" className="text-red-600 hover:text-red-700 hover:bg-red-50 h-10 w-10">
          <Trash2 className="w-5 h-5" />
        </Button>
      )
    },
  },
]

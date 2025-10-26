"use client"
import { ColumnDef } from "@tanstack/react-table"
import { Button } from "@/components/ui/button"
import { Trash2 } from "lucide-react"

interface Role { 
  id: string; 
  label: string; 
  hexColor: string; 
  default: boolean; 
  userCount: number;
  permissions?: string[];
}

export const columns: ColumnDef<Role>[] = [
  {
    accessorKey: "label",
    header: "Role Name",
    size: 250,
    cell: ({ row }) => {
      const role = row.original
      return (
        <div className="flex items-center space-x-3">
          <div 
            className="w-4 h-4 rounded-full border border-border"
            style={{ backgroundColor: role.hexColor }}
          />
          <div className="font-medium">{role.label}</div>
        </div>
      )
    },
  },
  {
    accessorKey: "id",
    header: "ID",
    size: 100,
    cell: ({ row }) => (
      <div className="font-mono text-sm text-muted-foreground">
        {row.getValue("id")}
      </div>
    ),
  },
  {
    accessorKey: "hexColor",
    header: "Color",
    size: 150,
    cell: ({ row }) => {
      const hexColor = row.getValue("hexColor") as string
      return (
        <div className="flex items-center space-x-2">
          <div 
            className="w-6 h-6 rounded border border-border"
            style={{ backgroundColor: hexColor }}
          />
          <span className="text-sm font-mono">{hexColor}</span>
        </div>
      )
    },
  },
  {
    accessorKey: "userCount",
    header: "Users",
    size: 120,
    cell: ({ row }) => (
      <div className="text-sm text-muted-foreground">
        {row.getValue("userCount")} users
      </div>
    ),
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

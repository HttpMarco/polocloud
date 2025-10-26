"use client"
import { useState, useEffect, useRef } from 'react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Checkbox } from '@/components/ui/checkbox'
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Shield, Palette } from 'lucide-react'
import { motion } from 'framer-motion'
import { HexColorPicker } from 'react-colorful'

interface CreateRoleModalProps {
  onRoleAdded: (role: { label: string; hexColor: string; permissions: string[] }) => void
  isOpen?: boolean
  onOpenChange?: (open: boolean) => void
}

const availablePermissions = [

  { id: "polocloud.system.version", label: "System Version", category: "System" },
  { id: "polocloud.system.information", label: "System Information", category: "System" },
  { id: "polocloud.user.list", label: "User List", category: "Users" },
  { id: "polocloud.user.get", label: "User Get", category: "Users" },
  { id: "polocloud.service.count", label: "Service Count", category: "Services" },
  { id: "polocloud.service.list", label: "Service List", category: "Services" },
  { id: "polocloud.role.list", label: "Role List", category: "Roles" },
  { id: "polocloud.role.get", label: "Role Get", category: "Roles" },
  { id: "polocloud.player.get", label: "Player Get", category: "Players" },
  { id: "polocloud.players.list", label: "Players List", category: "Players" },
  { id: "polocloud.platform.list", label: "Platform List", category: "Platform" },
  { id: "polocloud.group.count", label: "Group Count", category: "Groups" },
  { id: "polocloud.group.list", label: "Group List", category: "Groups" },
  { id: "polocloud.group.get", label: "Group Get", category: "Groups" },
  { id: "polocloud.templates.list", label: "Templates List", category: "Templates" },

  { id: "polocloud.ws.alive", label: "WebSocket Alive", category: "WebSocket" },
  { id: "polocloud.ws.logs", label: "WebSocket Logs", category: "WebSocket" },
  { id: "polocloud.service.screen", label: "Service Screen", category: "WebSocket" },

  { id: "polocloud.user.self.edit", label: "Edit Self", category: "Self" },
  { id: "polocloud.user.self.change-password", label: "Change Password", category: "Self" },
  { id: "polocloud.user.self.tokens", label: "View Tokens", category: "Self" },
  { id: "polocloud.user.self.token.delete", label: "Delete Tokens", category: "Self" },

  { id: "polocloud.user.create", label: "Create Users", category: "Admin" },
  { id: "polocloud.user.edit", label: "Edit Users", category: "Admin" },
  { id: "polocloud.user.delete", label: "Delete Users", category: "Admin" },
  { id: "polocloud.role.create", label: "Create Roles", category: "Admin" },
  { id: "polocloud.role.edit", label: "Edit Roles", category: "Admin" },
  { id: "polocloud.role.delete", label: "Delete Roles", category: "Admin" },
  { id: "polocloud.group.create", label: "Create Groups", category: "Admin" },
  { id: "polocloud.group.edit", label: "Edit Groups", category: "Admin" },
  { id: "polocloud.group.delete", label: "Delete Groups", category: "Admin" },
  { id: "polocloud.service.create", label: "Create Services", category: "Admin" },
  { id: "polocloud.service.edit", label: "Edit Services", category: "Admin" },
  { id: "polocloud.service.delete", label: "Delete Services", category: "Admin" },
  { id: "polocloud.template.create", label: "Create Templates", category: "Admin" },
  { id: "polocloud.template.edit", label: "Edit Templates", category: "Admin" },
  { id: "polocloud.template.delete", label: "Delete Templates", category: "Admin" },
  { id: "polocloud.templates.create", label: "Create Templates", category: "Admin" },
  { id: "polocloud.templates.edit", label: "Edit Templates", category: "Admin" },
  { id: "polocloud.templates.delete", label: "Delete Templates", category: "Admin" },
  { id: "polocloud.terminal.command", label: "Terminal Commands", category: "Terminal" },
  { id: "polocloud.service.restart", label: "Restart Services", category: "Services" },
  { id: "polocloud.service.start", label: "Start Services", category: "Services" },
  { id: "polocloud.service.stop", label: "Stop Services", category: "Services" },
  { id: "polocloud.group.restart", label: "Restart Groups", category: "Groups" },
  { id: "polocloud.group.start", label: "Start Groups", category: "Groups" },
  { id: "polocloud.group.stop", label: "Stop Groups", category: "Groups" },
]

export function CreateRoleModal({ onRoleAdded, isOpen: externalIsOpen, onOpenChange }: CreateRoleModalProps) {
  const [label, setLabel] = useState('')
  const [hexColor, setHexColor] = useState('#3B82F6')
  const [selectedPermissions, setSelectedPermissions] = useState<string[]>([])
  const [internalIsOpen, setInternalIsOpen] = useState(false)
  const [isLoading, setIsLoading] = useState(false)

  const isOpen = externalIsOpen !== undefined ? externalIsOpen : internalIsOpen
  const setIsOpen = onOpenChange || setInternalIsOpen
  const [error, setError] = useState('')
  const [showColorPicker, setShowColorPicker] = useState(false)
  const colorPickerRef = useRef<HTMLDivElement>(null)

  const withLoading = async (fn: () => Promise<void>) => {
    try {
      setIsLoading(true);
      setError('');
      await fn();
    } catch (error) {
      setError(error instanceof Error ? error.message : 'Ein Fehler ist aufgetreten');
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (colorPickerRef.current && !colorPickerRef.current.contains(event.target as Node)) {
        setShowColorPicker(false)
      }
    }

    if (showColorPicker) {
      document.addEventListener('mousedown', handleClickOutside)
    }

    return () => {
      document.removeEventListener('mousedown', handleClickOutside)
    }
  }, [showColorPicker])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!label.trim() || !hexColor) return

    await withLoading(async () => {
      const backendIp = localStorage.getItem('backendIp')
      if (!backendIp) {
        throw new Error('Backend IP nicht gefunden. Bitte verbinde dich zuerst mit dem Backend.')
      }

      const response = await fetch('/api/roles', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ 
          label: label.trim(), 
          hexColor, 
          permissions: selectedPermissions,
          backendIp 
        }),
      })

      if (!response.ok) {
        const errorData = await response.json()
        throw new Error(errorData.message || `HTTP ${response.status}: Fehler beim Erstellen der Rolle`)
      }

      onRoleAdded({ label: label.trim(), hexColor, permissions: selectedPermissions })
      setLabel('')
      setHexColor('#3B82F6')
      setSelectedPermissions([])
      setIsOpen(false)
    })
  }

  const handlePermissionChange = (permissionId: string, checked: boolean) => {
    if (checked) {
      setSelectedPermissions(prev => [...prev, permissionId])
    } else {
      setSelectedPermissions(prev => prev.filter(id => id !== permissionId))
    }
  }

  const groupedPermissions = availablePermissions.reduce((acc, permission) => {
    if (!acc[permission.category]) {
      acc[permission.category] = []
    }
    acc[permission.category].push(permission)
    return acc
  }, {} as Record<string, typeof availablePermissions>)

  return (
    <Dialog open={isOpen} onOpenChange={setIsOpen}>

      <DialogContent className="sm:max-w-[800px] max-h-[90vh]">
        <DialogHeader>
          <DialogTitle className="text-xl font-bold">Create New Role</DialogTitle>
          <DialogDescription>
            Enter the role name, select a color, and choose permissions for the new role.
          </DialogDescription>
        </DialogHeader>
        
        {error && (
          <motion.div 
            initial={{ opacity: 0, y: -10 }} 
            animate={{ opacity: 1, y: 0 }} 
            className="bg-red-50 border border-red-200 rounded-lg p-3 mb-4"
          >
            <p className="text-red-700 text-sm">{error}</p>
          </motion.div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="space-y-6 py-4">
            <div className="space-y-3">
              <Label htmlFor="label" className="text-sm font-medium">
                Role Name
              </Label>
              <Input
                id="label"
                value={label}
                onChange={(e) => setLabel(e.target.value)}
                placeholder="Enter role name"
                className="flex-1"
                disabled={isLoading}
              />
            </div>

            <div className="space-y-3">
              <Label htmlFor="hexColor" className="text-sm font-medium flex items-center gap-2">
                <Palette className="w-4 h-4" />
                Role Color
              </Label>
              
              {}
              <div className="flex items-center space-x-3">
                <div 
                  className="w-16 h-16 rounded-lg border-2 border-border shadow-lg"
                  style={{ backgroundColor: hexColor }}
                />
                <div className="flex items-center space-x-2">
                  <Input
                    id="hexColor"
                    value={hexColor}
                    onChange={(e) => setHexColor(e.target.value)}
                    placeholder="#000000"
                    className="font-mono text-sm w-32"
                    disabled={isLoading}
                  />
                  
                  {}
                  <div className="relative" ref={colorPickerRef}>
                    <motion.button
                      type="button"
                      onClick={() => setShowColorPicker(!showColorPicker)}
                      className="h-10 w-10 rounded-lg border border-border bg-muted/20 hover:bg-muted/40 transition-colors duration-200 flex items-center justify-center"
                      whileHover={{ scale: 1.05 }}
                      whileTap={{ scale: 0.95 }}
                    >
                      <Palette className="w-4 h-4 text-muted-foreground" />
                    </motion.button>
                    
                    {}
                    {showColorPicker && (
                      <motion.div
                        initial={{ opacity: 0, scale: 0.9, y: 10 }}
                        animate={{ opacity: 1, scale: 1, y: 0 }}
                        exit={{ opacity: 0, scale: 0.9, y: 10 }}
                        className="absolute top-full left-0 mt-2 z-50"
                      >
                        <div className="border border-border rounded-lg p-3 bg-background shadow-xl">
                          <HexColorPicker 
                            color={hexColor} 
                            onChange={setHexColor}
                            className="w-[200px]"
                          />
                        </div>
                      </motion.div>
                    )}
                  </div>
                </div>
              </div>
            </div>

            <div className="space-y-3">
              <Label className="text-sm font-medium flex items-center gap-2">
                <Shield className="w-4 h-4" />
                Permissions
              </Label>
              <div className="space-y-4 max-h-60 overflow-y-auto border rounded-lg p-4 custom-scrollbar">
                {Object.entries(groupedPermissions).map(([category, permissions]) => (
                  <div key={category} className="space-y-2">
                    <h4 className="text-sm font-semibold text-muted-foreground uppercase tracking-wider">
                      {category}
                    </h4>
                    <div className="grid grid-cols-1 gap-2">
                      {permissions.map((permission) => (
                        <div key={permission.id} className="flex items-center space-x-2">
                          <Checkbox
                            id={permission.id}
                            checked={selectedPermissions.includes(permission.id)}
                            onCheckedChange={(checked: boolean | 'indeterminate') => 
                              handlePermissionChange(permission.id, checked === true)
                            }
                            disabled={isLoading}
                          />
                          <Label 
                            htmlFor={permission.id} 
                            className="text-sm cursor-pointer"
                          >
                            {permission.label}
                          </Label>
                        </div>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
              <p className="text-xs text-muted-foreground">
                Selected: {selectedPermissions.length} permissions
              </p>
            </div>
          </div>

          <DialogFooter>
            <Button 
              type="button" 
              variant="outline" 
              onClick={() => setIsOpen(false)} 
              disabled={isLoading}
            >
              Cancel
            </Button>
            <Button 
              type="submit" 
              disabled={!label.trim() || isLoading} 
              style={{ backgroundColor: 'oklch(75.54% .1534 231.639)' }} 
              className="hover:opacity-90"
            >
              {isLoading ? 'Creating...' : 'Create Role'}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}

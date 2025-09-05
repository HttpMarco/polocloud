"use client"
import { useState, useEffect, useCallback } from 'react'
import Image from 'next/image'
import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog'
import { Edit2, Loader2, AlertTriangle } from 'lucide-react'
import { motion } from 'framer-motion'

interface EditUserModalProps {
  user: { uuid: string; username: string; role: number }
  onUserEdited: () => void
  disabled?: boolean
}

interface Role {
  id: string
  label: string
  hexColor: string
}

interface CurrentUser {
  role: number
  username: string
}

export function EditUserModal({ user, onUserEdited, disabled = false }: EditUserModalProps) {
  const [isOpen, setIsOpen] = useState(false)
  const [isLoading, setIsLoading] = useState(false)
  const [roles, setRoles] = useState<Role[]>([])
  const [selectedRoleId, setSelectedRoleId] = useState<string>(user.role.toString())
  const [error, setError] = useState('')
  const [currentUser, setCurrentUser] = useState<CurrentUser | null>(null)

  const fetchRoles = useCallback(async () => {
    try {
      const response = await fetch('/api/roles')
      if (response.ok) {
        const data = await response.json()
        setRoles(data)
      }
    } catch {}
  }, [])

  const fetchCurrentUser = useCallback(async () => {
    try {
      const response = await fetch('/api/auth/me')
      if (response.ok) {
        const data = await response.json()
        if (data.authenticated) {
          setCurrentUser({ role: data.role, username: data.username })
        }
      }
    } catch {}
  }, [])

  useEffect(() => {
    if (isOpen) {
      fetchRoles()
      fetchCurrentUser()
    }
  }, [isOpen, fetchRoles, fetchCurrentUser])

  const isSelfDemotion = currentUser && 
    currentUser.username === user.username && 
    parseInt(selectedRoleId) > currentUser.role

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (selectedRoleId === user.role.toString()) return

    if (isSelfDemotion) {
      setError('Du kannst dir selbst keine niedrigere Rolle geben!')
      return
    }

    setIsLoading(true)
    setError('')
    
    try {
      const response = await fetch(`/api/users/${user.uuid}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ roleId: parseInt(selectedRoleId) }),
      })

      if (!response.ok) {
        const errorData = await response.json()
        throw new Error(errorData.message || 'Fehler beim Bearbeiten des Users')
      }

      onUserEdited()
      setIsOpen(false)
    } catch (error) {
      setError(error instanceof Error ? error.message : 'Unbekannter Fehler')
    } finally {
      setIsLoading(false)
    }
  }

  const selectedRole = roles.find(role => role.id === selectedRoleId)

  return (
    <Dialog open={isOpen} onOpenChange={disabled ? undefined : setIsOpen}>
      <DialogTrigger asChild disabled={disabled}>
        <motion.div 
          whileHover={{ scale: disabled ? 1 : 1.05 }} 
          whileTap={{ scale: disabled ? 1 : 0.95 }}
        >
          <Button 
            variant="ghost" 
            size="sm" 
            disabled={disabled}
            className={`h-10 w-10 ${
              disabled 
                ? 'opacity-40 cursor-not-allowed text-muted-foreground' 
                : 'text-blue-600 hover:text-blue-700 hover:bg-blue-50'
            }`}
          >
            <Edit2 className="w-5 h-5" />
          </Button>
        </motion.div>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle className="text-xl font-bold">Edit User Role</DialogTitle>
          <DialogDescription>
            Change the role for user &quot;{user.username}&quot;
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
              <Label htmlFor="username" className="text-sm font-medium">
                Username
              </Label>
              <div className="flex items-center space-x-3">
                <div className="w-12 h-12 rounded-lg overflow-hidden border border-border">
                  <Image 
                    src={`https://mineskin.eu/helm/${user.username}/64`} 
                    alt={`${user.username} Minecraft Head`} 
                    width={48}
                    height={48}
                    className="w-12 h-12"
                    onError={(e) => {
                      e.currentTarget.src = 'https://mineskin.eu/helm/MHF_Question/64'
                    }}
                  />
                </div>
                <div className="text-lg font-medium">{user.username}</div>
              </div>
            </div>

            <div className="space-y-3">
              <Label htmlFor="role" className="text-sm font-medium">
                Role
              </Label>
              <Select value={selectedRoleId} onValueChange={setSelectedRoleId} disabled={isLoading}>
                <SelectTrigger>
                  <SelectValue placeholder="Select a role" />
                </SelectTrigger>
                <SelectContent>
                  {roles.map((role) => (
                    <SelectItem key={role.id} value={role.id}>
                      <div className="flex items-center space-x-2">
                        <div 
                          className="w-3 h-3 rounded-full border border-border"
                          style={{ backgroundColor: role.hexColor }}
                        />
                        <span>{role.label}</span>
                      </div>
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              
              {selectedRole && (
                <div className="flex items-center space-x-2 text-sm text-muted-foreground">
                  <div 
                    className="w-4 h-4 rounded-full border border-border"
                    style={{ backgroundColor: selectedRole.hexColor }}
                  />
                  <span>Current selection: {selectedRole.label}</span>
                </div>
              )}

              {}
              {isSelfDemotion && (
                <motion.div 
                  initial={{ opacity: 0, y: -10 }} 
                  animate={{ opacity: 1, y: 0 }} 
                  className="bg-amber-50 border border-amber-200 rounded-lg p-3 flex items-center space-x-2"
                >
                  <AlertTriangle className="w-4 h-4 text-amber-600" />
                  <p className="text-amber-700 text-sm">
                    <strong>Warning:</strong> You are trying to assign yourself a lower role. 
                    This could restrict your access rights!
                  </p>
                  <p className="text-amber-600 text-xs mt-1">
                    Your current role: {currentUser?.role}, Selected role: {selectedRoleId}
                  </p>
                </motion.div>
              )}

              {}
              {currentUser && currentUser.username === user.username && (
                <div className="text-xs text-muted-foreground bg-muted/20 p-2 rounded">
                  <p>Debug: Your role: {currentUser.role}, User role: {user.role}, Selected: {selectedRoleId}</p>
                  <p>Self-demotion: {isSelfDemotion ? 'YES' : 'NO'}</p>
                </div>
              )}
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
              disabled={selectedRoleId === user.role.toString() || isLoading || (isSelfDemotion || false)} 
              className="bg-blue-600 hover:bg-blue-700"
            >
              {isLoading ? (
                <>
                  <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                  Updating...
                </>
              ) : (
                'Update Role'
              )}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}



"use client"
import { useState, useEffect, useCallback, useRef } from 'react'
import Image from 'next/image'
import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'
import { ChevronDown, Check } from 'lucide-react'
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
  const [isDropdownOpen, setIsDropdownOpen] = useState(false)
  const dropdownRef = useRef<HTMLDivElement>(null)

  const fetchRoles = useCallback(async () => {
    try {
      const response = await fetch('/api/roles')
      if (response.ok) {
        const data = await response.json()
        setRoles(data)
      }
    } catch (error) {
        console.error('Error in edit-user-modal:', error);
      }}, [])

  const fetchCurrentUser = useCallback(async () => {
    try {
      const response = await fetch('/api/auth/me')
      if (response.ok) {
        const data = await response.json()
        if (data.authenticated) {
          setCurrentUser({ role: data.role, username: data.username })
        }
      }
    } catch (error) {
        console.error('Error in edit-user-modal:', error);
      }}, [])

  useEffect(() => {
    if (isOpen) {
      fetchRoles()
      fetchCurrentUser()
    }
  }, [isOpen, fetchRoles, fetchCurrentUser])

  useEffect(() => {
    setSelectedRoleId(user.role.toString())
  }, [user.role])

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsDropdownOpen(false)
      }
    }

    if (isDropdownOpen) {
      document.addEventListener('mousedown', handleClickOutside)
    }

    return () => {
      document.removeEventListener('mousedown', handleClickOutside)
    }
  }, [isDropdownOpen])


  const isSelfDemotion = currentUser && 
    currentUser.username === user.username && 
    parseInt(selectedRoleId) > currentUser.role

  const handleRoleSelect = (roleId: string) => {
    setSelectedRoleId(roleId)
    setIsDropdownOpen(false)
  }

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
              
              <div className="relative" ref={dropdownRef}>
                <button
                  type="button"
                  onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                  disabled={isLoading}
                  className="w-full h-11 px-4 py-2 text-left border-2 border-border/50 hover:border-blue-500/50 focus:border-blue-500 rounded-md bg-background transition-all duration-200 flex items-center justify-between disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  <div className="flex items-center space-x-3">
                    {selectedRole && (
                      <div 
                        className="w-4 h-4 rounded-full border-2 border-border shadow-sm"
                        style={{ backgroundColor: selectedRole.hexColor }}
                      />
                    )}
                    <span className="font-medium">
                      {selectedRole ? selectedRole.label : (roles.length > 0 ? 'Select a role' : 'Loading roles...')}
                    </span>
                  </div>
                  <ChevronDown className={`w-4 h-4 transition-transform duration-200 ${isDropdownOpen ? 'rotate-180' : ''}`} />
                </button>

                {isDropdownOpen && (
                  <motion.div
                    initial={{ opacity: 0, y: -10 }}
                    animate={{ opacity: 1, y: 0 }}
                    exit={{ opacity: 0, y: -10 }}
                    className="absolute z-50 w-full mt-1 bg-background border border-border rounded-md shadow-lg max-h-60 overflow-y-auto"
                  >
                    {roles.map((role) => {
                      const isCurrentRole = role.id === user.role.toString()
                      const isSelected = selectedRoleId === role.id
                      
                      return (
                        <button
                          key={role.id}
                          type="button"
                          onClick={() => !isCurrentRole && handleRoleSelect(role.id)}
                          disabled={isCurrentRole}
                          className={`w-full px-4 py-3 text-left transition-colors duration-150 flex items-center space-x-3 ${
                            isCurrentRole 
                              ? 'opacity-50 cursor-not-allowed bg-muted/20' 
                              : 'hover:bg-muted/50'
                          }`}
                        >
                          <div 
                            className="w-4 h-4 rounded-full border-2 border-border shadow-sm"
                            style={{ backgroundColor: role.hexColor }}
                          />
                          <span className="font-medium flex-1">{role.label}</span>
                          {isCurrentRole && (
                            <span className="text-xs text-muted-foreground">(Current)</span>
                          )}
                          {isSelected && !isCurrentRole && (
                            <Check className="w-4 h-4 text-blue-500" />
                          )}
                        </button>
                      )
                    })}
                  </motion.div>
                )}
              </div>
              

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

          <DialogFooter className="gap-3">
            <Button 
              type="button" 
              variant="outline" 
              onClick={() => setIsOpen(false)} 
              disabled={isLoading}
              className="px-6"
            >
              Cancel
            </Button>
            <Button 
              type="submit" 
              disabled={selectedRoleId === user.role.toString() || isLoading || (isSelfDemotion || false)} 
              className="px-6 font-medium transition-all duration-200 shadow-lg hover:shadow-xl"
              style={{ 
                backgroundColor: 'oklch(75.54% .1534 231.639)',
                borderColor: 'oklch(75.54% .1534 231.639)'
              }}
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



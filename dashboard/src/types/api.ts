

export interface User {
  uuid: string
  username: string
  hasChangedPassword: boolean
  createdAt: number
  role: number
}
export interface Role {
  id: number
  label: string
  hexColor: string
  default: boolean
  permissions: string[]
  userCount?: number
}

export interface RoleCreateModel {
  label: string
  hexColor: string
  permissions: string[]
}

export interface RoleEditModel {
  label: string
  hexColor: string
  permissions: string[]
}

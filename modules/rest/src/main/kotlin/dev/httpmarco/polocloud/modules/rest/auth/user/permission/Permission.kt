package dev.httpmarco.polocloud.modules.rest.auth.user.permission

open class Permission {

    val permissions: MutableList<String> = mutableListOf()

    fun addPermission(permission: String) {
        if (permission.isNotBlank() && !this.permissions.contains(permission)) {
            this.permissions.add(permission)
        }
    }

    fun addPermissions(vararg permissions: String) {
        for (permission in permissions) {
            addPermission(permission)
        }
    }

    fun removePermission(permission: String) {
        this.permissions.remove(permission)
    }

    fun hasPermission(permission: String): Boolean {
        return "*" in this.permissions || permission in this.permissions
    }
}
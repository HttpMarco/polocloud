package dev.httpmarco.polocloud.modules.rest.auth.user.permission

open class Permission {

    private val permissions: MutableList<String> = mutableListOf()

    val all: List<String>
        get() = this.permissions.toList()

    fun addPermission(permission: String) {
        if (permission.isNotBlank() && !this.permissions.contains(permission)) {
            this.permissions.add(permission)
        }
    }

    fun removePermission(permission: String) {
        this.permissions.remove(permission)
    }

    fun hasPermission(permission: String): Boolean {
        return "*" in this.permissions || permission in this.permissions
    }
}
package dev.httpmarco.polocloud.modules.rest.auth.user.permission;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class Permission {

    private final List<String> permissions;

    public Permission() {
        this.permissions = new ArrayList<>();
    }

    public void addPermission(String permission) {
        this.permissions.add(permission);
    }

    public void removePermission(String permission) {
        this.permissions.remove(permission);
    }

    public boolean hasPermission(String permission) {
        if (this.permissions.contains("*")) return true;
        return this.permissions.contains(permission);
    }
}

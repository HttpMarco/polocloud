package dev.httpmarco.polocloud.modules.rest.auth.user;

import dev.httpmarco.polocloud.modules.rest.auth.user.permission.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@Accessors(fluent = true)
@AllArgsConstructor
@ToString
public class User extends Permission {

    private final UUID uuid;
    private String username;
    private String passwordHash;
}

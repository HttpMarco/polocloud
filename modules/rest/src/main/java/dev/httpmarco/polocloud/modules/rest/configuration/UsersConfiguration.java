package dev.httpmarco.polocloud.modules.rest.configuration;

import dev.httpmarco.polocloud.modules.rest.auth.user.User;
import dev.httpmarco.polocloud.node.util.StringUtils;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class UsersConfiguration {

    private final String secret;
    private final List<User> users;

    public UsersConfiguration() {
        this.secret = StringUtils.randomString(170);
        this.users = new ArrayList<>();
    }
}

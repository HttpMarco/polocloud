package dev.httpmarco.polocloud.modules.rest.auth.user;

import dev.httpmarco.polocloud.modules.rest.RestModule;
import dev.httpmarco.polocloud.modules.rest.auth.TokenInformation;
import dev.httpmarco.polocloud.modules.rest.configuration.Config;
import dev.httpmarco.polocloud.modules.rest.util.EncryptionUtil;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;


//TODO write better
@AllArgsConstructor
public class UserService {

    private final RestModule restModule;

    public String login(String username, String password, String ip, String userAgent) {
        var user = users().stream().filter(u -> u.username().equals(username)).findFirst().orElse(null);
        if (user == null) {
            return null;
        }

        if (!EncryptionUtil.verify(user.passwordHash(), password)) {
            return null;
        }

        return this.restModule.jwtProvider().provider().generateToken(new TokenInformation(user.uuid(), ip, userAgent)); //TODO token vailed endpoint
    }

    public String create(User user, String ip, String userAgent) {
        if (users().stream().anyMatch(u -> u.username().equals(user.username()))) {
            return null;
        }

        var token = this.restModule.jwtProvider().provider().generateToken(new TokenInformation(user.uuid(), ip, userAgent));

        if (users().isEmpty()) {
            user.addPermission("*");
        }

        users().add(user);
        save();

        return token;
    }

    public void delete(User user) {
        users().remove(user);
        save();
    }

    public void update(User user) {
        var oldUser = users().stream().filter(u -> u.uuid().equals(user.uuid())).findFirst().orElse(null);
        if (oldUser == null) {
            return;
        }

        users().remove(oldUser);
        users().add(user);

        save();
    }

    public boolean has(User user) {
        return users().contains(user);
    }

    public User has(UUID uuid) {
        return users().stream().filter(user -> user.uuid().equals(uuid)).findFirst().orElse(null);
    }

    public User userByUUID(UUID uuid) {
        return users().stream().filter(user -> user.uuid().equals(uuid)).findFirst().orElse(null);
    }

    private List<User> users() {
        return this.restModule.config().usersConfiguration().users();
    }

    private void save() {
        Config.save(this.restModule.config().usersConfiguration(), Config.USERS_PATH);
    }

}

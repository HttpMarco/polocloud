package dev.httpmarco.polocloud.modules.rest.controller.impl.v1.controller;

import dev.httpmarco.polocloud.modules.rest.RestModule;
import dev.httpmarco.polocloud.modules.rest.auth.user.User;
import dev.httpmarco.polocloud.modules.rest.controller.Controller;
import dev.httpmarco.polocloud.modules.rest.controller.impl.v1.model.UserModel;
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request;
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType;
import dev.httpmarco.polocloud.modules.rest.util.EncryptionUtil;
import io.javalin.http.Context;
import io.javalin.http.Cookie;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class UserController extends Controller {

    public UserController(RestModule restModule) {
        super("/user", restModule);
    }

    @Request(requestType = RequestType.POST, path = "/")
    public void createUser(Context context) {
        UserModel userModel;
        try {
            userModel = context.bodyAsClass(UserModel.class);
        } catch (Exception e) {
            context.status(400).json(message("Invalid body"));
            return;
        }

        if (userModel.username() == null || userModel.password() == null) {
            context.status(400).json(message("Invalid body: Missing fields"));
            return;
        }

        var user = new User(UUID.randomUUID(), userModel.username(), null);
        var token = restModule().userService().create(user, context.ip(), context.userAgent());

        if (token == null) {
            context.status(400).json(message("User already exists"));
            return;
        }

        var cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(7));
        cookie.setSecure(true);

        context.status(201).cookie(cookie);

        CompletableFuture.runAsync(() -> {
            user.passwordHash(EncryptionUtil.encrypt(userModel.password()));
            restModule().userService().update(user);
        });
    }
}

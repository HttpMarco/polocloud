package dev.httpmarco.polocloud.modules.rest.controller.impl.v1.controller;

import dev.httpmarco.polocloud.modules.rest.RestModule;
import dev.httpmarco.polocloud.modules.rest.controller.Controller;
import dev.httpmarco.polocloud.modules.rest.controller.impl.v1.model.AuthModel;
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request;
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType;
import io.javalin.http.Context;
import io.javalin.http.Cookie;

import java.util.concurrent.TimeUnit;

public class AuthController extends Controller {

    public AuthController(RestModule restModule) {
        super("/auth", restModule);
    }

    @Request(requestType = RequestType.POST, path = "/login")
    public void login(Context context) {
        AuthModel authModel;
        try {
            authModel = context.bodyAsClass(AuthModel.class);
        } catch (Exception e) {
            context.status(400).result(failMessage("Invalid body"));
            return;
        }

        if (authModel.username() == null || authModel.password() == null) {
            context.status(400).result(failMessage("Invalid body: Missing fields"));
            return;
        }

        var token = restModule().userService().login(authModel.username(), authModel.password(), context.ip(), context.userAgent());
        if (token == null) {
            context.status(401).result(failMessage("Invalid credentials"));
            return;
        }

        var cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(7));
        cookie.setSecure(true);

        context.status(200).cookie(cookie);
    }

    // Verify the token
    @Request(requestType = RequestType.GET, path = "/token")
    public void checkToken(Context context) {
        context.status(200); // if the user gets through the auth, the token is valid
    }
}

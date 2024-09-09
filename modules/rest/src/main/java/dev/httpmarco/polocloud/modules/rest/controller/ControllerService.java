package dev.httpmarco.polocloud.modules.rest.controller;

import dev.httpmarco.polocloud.modules.rest.RestModule;
import dev.httpmarco.polocloud.modules.rest.auth.AuthService;
import dev.httpmarco.polocloud.modules.rest.controller.impl.v1.endpoint.AuthController;
import dev.httpmarco.polocloud.modules.rest.controller.impl.v1.endpoint.UserController;
import dev.httpmarco.polocloud.modules.rest.controller.impl.v1.endpoint.groups.GroupController;
import dev.httpmarco.polocloud.modules.rest.controller.methods.*;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class ControllerService {

    public static final String API_PATH = "/polocloud/api/v1";
    private final RestModule restModule;
    private final List<Controller> controllers;

    public ControllerService(RestModule restModule) {
        this.restModule = restModule;
        this.controllers = new ArrayList<>();

        invoke();
    }

    private void registerControllers(Controller... controller) {
        this.controllers.addAll(List.of(controller));
    }

    public void invoke() {
        registerControllers(
                new UserController(this.restModule),
                new AuthController(this.restModule),
                new GroupController(this.restModule)
        );

        for (var controller : this.controllers) {
            handle(controller);
        }
    }

    private void handle(Controller controller) {
        var methods = controller.getClass().getMethods();
        var basePath = API_PATH + controller.path();

        var requestMethods = Arrays.stream(methods).filter(it -> it.isAnnotationPresent(Request.class));
        requestMethods.forEach(request -> registerRoute(request, controller, basePath));
    }

    private void registerRoute(Method method, Controller controller, String basePath) {
        var annotation = method.getAnnotation(Request.class);
        var type = HandlerType.valueOf(annotation.requestType().name());
        var path = basePath + annotation.path();
        var permission = annotation.permission();

        this.restModule.app().addHttpHandler(type, path, context -> new AuthService(this.restModule, new RequestMethodData(method, controller, permission)).handle(context));
    }

    public void processRequest(Method method, Controller controller, Context context) {
        try {
            if (context.result() == null) {
                method.invoke(controller, context);
            }
        } catch (Exception e) {
            context.status(500).result("Internal Server Error");
            e.printStackTrace();
        }
    }
}
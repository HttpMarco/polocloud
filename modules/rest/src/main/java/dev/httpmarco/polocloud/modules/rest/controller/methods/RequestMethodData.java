package dev.httpmarco.polocloud.modules.rest.controller.methods;

import dev.httpmarco.polocloud.modules.rest.controller.Controller;

import java.lang.reflect.Method;

public record RequestMethodData(Method method, Controller controller, String permission) {

}

package dev.httpmarco.polocloud.modules.rest.controller;

import com.google.gson.JsonObject;
import dev.httpmarco.polocloud.modules.rest.RestModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public abstract class Controller {

    private final String path;
    private RestModule restModule;

    public String failMessage(String message) {
        var response = new JsonObject();
        response.addProperty("message", message);
        return response.toString();
    }
}

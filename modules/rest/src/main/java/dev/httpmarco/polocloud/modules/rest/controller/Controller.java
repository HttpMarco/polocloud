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

    public String message(String message) {
        var response = new JsonObject();
        response.addProperty("message", message);
        return response.toString();
    }

    public String errorMessage(String errorCode, String message){
        var response = new JsonObject();
        response.addProperty("errorCode", errorCode);
        response.addProperty("message", message);
        return response.toString();
    }

    public boolean isNumber(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

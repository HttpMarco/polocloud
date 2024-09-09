package dev.httpmarco.polocloud.modules.rest.controller.methods;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Request {

    RequestType requestType();
    String path() default "";
    String permission() default "";
}

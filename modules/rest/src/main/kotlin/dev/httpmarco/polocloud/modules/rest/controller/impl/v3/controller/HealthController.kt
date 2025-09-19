package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller

import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.defaultResponse
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import io.javalin.http.Context

class HealthController : Controller("/health") {

    @Request(requestType = RequestType.GET, path = "/")
    fun health(context: Context) {
        context.defaultResponse(200, "Healthy")
    }

    //TODO implement health system
}
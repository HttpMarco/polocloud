package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller

import dev.httpmarco.polocloud.modules.rest.auth.user.User
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import io.javalin.http.Context

class AliveController : Controller("/alive") {

    @Request(requestType = RequestType.GET, path = "/")
    fun alive(context: Context, user: User?) {
        context.status(200).result(message("OK"))
    }
}
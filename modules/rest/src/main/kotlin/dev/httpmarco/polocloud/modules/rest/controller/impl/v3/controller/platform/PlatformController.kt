package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.platform

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.defaultResponse
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import dev.httpmarco.polocloud.shared.platform.toJson
import dev.httpmarco.polocloud.shared.polocloudShared
import io.javalin.http.Context

class PlatformController : Controller("/platform") {

    @Request(requestType = RequestType.GET, path = "s/list", permission = "polocloud.platform.list")
    fun list(context: Context) {
        val platforms = polocloudShared.platformProvider().findAll()
        val data = JsonArray().apply {
            platforms.map { platform ->
                add(platform.toJson())
            }
        }

        context.defaultResponse(200, data = data)
    }
}
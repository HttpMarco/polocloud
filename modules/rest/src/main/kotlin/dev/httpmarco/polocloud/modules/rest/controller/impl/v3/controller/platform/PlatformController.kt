package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.platform

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import dev.httpmarco.polocloud.shared.polocloudShared
import io.javalin.http.Context

class PlatformController : Controller("/platform") {

    @Request(requestType = RequestType.GET, path = "s/list", permission = "polocloud.platform.list")
    fun list(context: Context) {
        context.status(200).json(
            JsonArray().apply {
                polocloudShared.platformProvider().findAll().map { platform ->
                    add(
                        JsonObject().apply {
                            addProperty("name", platform.name)
                            addProperty("type", platform.type.name)
                            add(
                                JsonArray().apply {
                                    platform.versions.forEach { version ->
                                        add(
                                            JsonObject().apply {
                                                addProperty("version", version.version)
                                            }
                                        )
                                    }
                                }
                            )
                        }
                    )
                }
            }.toString()
        )
    }
}
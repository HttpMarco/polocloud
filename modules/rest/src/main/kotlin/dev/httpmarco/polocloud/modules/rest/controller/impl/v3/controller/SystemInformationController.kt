package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller

import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import dev.httpmarco.polocloud.shared.polocloudShared
import io.javalin.http.Context

class SystemInformationController : Controller("/system") {

    @Request(requestType = RequestType.GET, path = "/information", permission = "polocloud.system.information")
    fun information(context: Context) {
        val stats = polocloudShared.statsProvider().get()

        context.status(200).json(
            JsonObject().apply {
                addProperty("memoryUsage", stats.usedMemory)
                addProperty("cpuUsage", stats.cpuUsage)
                addProperty("runtime", stats.runtime)
                addProperty("uptime", System.currentTimeMillis() - stats.started)
            }.toString()
        )
    }
}
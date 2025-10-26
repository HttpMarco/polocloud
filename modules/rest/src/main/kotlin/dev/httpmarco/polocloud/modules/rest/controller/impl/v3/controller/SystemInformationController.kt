package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.httpmarco.polocloud.common.version.polocloudVersion
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.defaultResponse
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import dev.httpmarco.polocloud.shared.polocloudShared
import io.javalin.http.Context

class SystemInformationController : Controller("/system") {

    @Request(requestType = RequestType.GET, path = "/version", permission = "polocloud.system.version")
    fun version(context: Context) {
        context.defaultResponse(200, data = JsonObject().apply { addProperty("version", polocloudVersion()) })
    }

    @Request(requestType = RequestType.GET, path = "/information", permission = "polocloud.system.information")
    fun information(context: Context) {
        val information = polocloudShared.cloudInformationProvider().find()
        val services = polocloudShared.serviceProvider().findAll()

        val polocloudCpu = information.cpuUsage
        val polocloudRam = information.usedMemory

        val servicesCpu = services.sumOf { it.cpuUsage }
        val servicesRam = services.sumOf { it.memoryUsage }

        val totalCpuUsage = polocloudCpu + servicesCpu
        val totalMemoryUsage = polocloudRam + servicesRam

        val data = JsonObject().apply {
            add("cpuUsage", JsonObject().apply {
                addProperty("polocloud", polocloudCpu)
                addProperty("services", servicesCpu)
                addProperty("total", totalCpuUsage)
            })
            add("memoryUsage", JsonObject().apply {
                addProperty("polocloud", polocloudRam)
                addProperty("services", servicesRam)
                addProperty("total", totalMemoryUsage)
            })
            addProperty("runtime", information.runtime)
            addProperty("uptime", System.currentTimeMillis() - information.started)
        }

        context.defaultResponse(200, data = data)
    }

    @Request(requestType = RequestType.GET, path = "/information/average", permission = "polocloud.system.information")
    fun average(context: Context) {
        val from = context.queryParam("from")?.toLongOrNull() ?: 0L
        val to = context.queryParam("to")?.toLongOrNull() ?: System.currentTimeMillis()

        val cloudAvg = polocloudShared.cloudInformationProvider().findAverage(from, to)
        val services = polocloudShared.serviceProvider().findAll()

        val serviceCpuAvg = services.map { it.cpuUsage }.average() //TODO implement average from and to
        val serviceRamAvg = services.map { it.memoryUsage }.average() //TODO implement average from and to

        val totalAvgCpu = cloudAvg.avgCpu + serviceCpuAvg
        val totalAvgRam = cloudAvg.avgRam + serviceRamAvg

        val data = JsonObject().apply {
            add("avgCpu", JsonObject().apply {
                    addProperty("polocloud", cloudAvg.avgCpu)
                    addProperty("services", serviceCpuAvg)
                addProperty("total", totalAvgCpu)
            })
            add("avgRam", JsonObject().apply {
                addProperty("polocloud", cloudAvg.avgRam)
                addProperty("services", serviceRamAvg)
                addProperty("total", totalAvgRam)
            })
            addProperty("from", from)
            addProperty("to", to)
        }

        context.defaultResponse(200, data = data)
    }

    @Request(requestType = RequestType.GET, path = "/information/minutes", permission = "polocloud.system.information")
    fun minutes(context: Context) {
        val from = context.queryParam("from")?.toLongOrNull() ?: 0L
        val to = context.queryParam("to")?.toLongOrNull() ?: System.currentTimeMillis()
        val data = polocloudShared.cloudInformationProvider().findMinutes(from, to)

        //TODO services average integration (ServiceInformationStorage or something like CloudInformationStorage)

        val jsonArray = JsonArray()
        data.forEach {
            val obj = JsonObject()
            obj.addProperty("timestamp", it.timestamp)
            obj.addProperty("avgCpu", it.avgCpu)
            obj.addProperty("avgRam", it.avgRam)
            jsonArray.add(obj)
        }

        context.defaultResponse(200, data = jsonArray)
    }

    @Request(requestType = RequestType.GET, path = "/information/hours", permission = "polocloud.system.information")
    fun hours(context: Context) {
        val from = context.queryParam("from")?.toLongOrNull() ?: 0L
        val to = context.queryParam("to")?.toLongOrNull() ?: System.currentTimeMillis()
        val data = polocloudShared.cloudInformationProvider().findHours(from, to)

        //TODO services average integration (ServiceInformationStorage or something like CloudInformationStorage)

        val jsonArray = JsonArray()
        data.forEach {
            val obj = JsonObject()
            obj.addProperty("timestamp", it.timestamp)
            obj.addProperty("avgCpu", it.avgCpu)
            obj.addProperty("avgRam", it.avgRam)
            jsonArray.add(obj)
        }

        context.defaultResponse(200, data = jsonArray)
    }

    @Request(requestType = RequestType.GET, path = "/information/days", permission = "polocloud.system.information")
    fun days(context: Context) {
        val from = context.queryParam("from")?.toLongOrNull() ?: 0L
        val to = context.queryParam("to")?.toLongOrNull() ?: System.currentTimeMillis()
        val data = polocloudShared.cloudInformationProvider().findDays(from, to)

        //TODO services average integration (ServiceInformationStorage or something like CloudInformationStorage)

        val jsonArray = JsonArray()
        data.forEach {
            val obj = JsonObject()
            obj.addProperty("timestamp", it.timestamp)
            obj.addProperty("avgCpu", it.avgCpu)
            obj.addProperty("avgRam", it.avgRam)
            jsonArray.add(obj)
        }

        context.defaultResponse(200,data = jsonArray)
    }
}
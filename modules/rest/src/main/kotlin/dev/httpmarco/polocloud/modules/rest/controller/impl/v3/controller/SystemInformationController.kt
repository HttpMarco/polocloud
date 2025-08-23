package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import dev.httpmarco.polocloud.shared.polocloudShared
import io.javalin.http.Context

class SystemInformationController : Controller("/system") {

    @Request(requestType = RequestType.GET, path = "/version", permission = "polocloud.system.version")
    fun version(context: Context) {
        context.status(200).json(
            JsonObject().apply {
                addProperty("version", System.getenv("polocloud-version") ?: "unknown")
            }.toString()
        )
    }

    @Request(requestType = RequestType.GET, path = "/information", permission = "polocloud.system.information")
    fun information(context: Context) {
        val information = polocloudShared.cloudInformationProvider().find()
        val services = polocloudShared.serviceProvider().findAll()

        val totalCpuUsage = services.sumOf { it.cpuUsage } + information.cpuUsage
        val totalMemoryUsage = services.sumOf { it.memoryUsage } + information.usedMemory

        context.status(200).json(
            JsonObject().apply {
                addProperty("memoryUsage", totalMemoryUsage)
                addProperty("cpuUsage", totalCpuUsage)
                addProperty("runtime", information.runtime)
                addProperty("uptime", System.currentTimeMillis() - information.started)
            }.toString()
        )
    }

    @Request(requestType = RequestType.GET, path = "/information/average", permission = "polocloud.system.information")
    fun average(context: Context) {
        val from = context.queryParam("from")?.toLongOrNull() ?: 0L
        val to = context.queryParam("to")?.toLongOrNull() ?: System.currentTimeMillis()
        val avg = polocloudShared.cloudInformationProvider().findAverage(from, to)

        //TODO with services

        context.status(200).json(
            JsonObject().apply {
                addProperty("avgCpu", avg.avgCpu)
                addProperty("avgRam", avg.avgRam)
                addProperty("from", from)
                addProperty("to", to)
            }.toString()
        )
    }

    @Request(requestType = RequestType.GET, path = "/information/minutes", permission = "polocloud.system.information")
    fun minutes(context: Context) {
        val from = context.queryParam("from")?.toLongOrNull() ?: 0L
        val to = context.queryParam("to")?.toLongOrNull() ?: System.currentTimeMillis()
        val data = polocloudShared.cloudInformationProvider().findMinutes(from, to)

        val jsonArray = JsonArray()
        data.forEach {
            val obj = JsonObject()
            obj.addProperty("timestamp", it.timestamp)
            obj.addProperty("avgCpu", it.avgCpu)
            obj.addProperty("avgRam", it.avgRam)
            jsonArray.add(obj)
        }

        context.status(200).json(jsonArray.toString())
    }

    @Request(requestType = RequestType.GET, path = "/information/hours", permission = "polocloud.system.information")
    fun hours(context: Context) {
        val from = context.queryParam("from")?.toLongOrNull() ?: 0L
        val to = context.queryParam("to")?.toLongOrNull() ?: System.currentTimeMillis()
        val data = polocloudShared.cloudInformationProvider().findHours(from, to)

        val jsonArray = JsonArray()
        data.forEach {
            val obj = JsonObject()
            obj.addProperty("timestamp", it.timestamp)
            obj.addProperty("avgCpu", it.avgCpu)
            obj.addProperty("avgRam", it.avgRam)
            jsonArray.add(obj)
        }

        context.status(200).json(jsonArray.toString())
    }

    @Request(requestType = RequestType.GET, path = "/information/days", permission = "polocloud.system.information")
    fun days(context: Context) {
        val from = context.queryParam("from")?.toLongOrNull() ?: 0L
        val to = context.queryParam("to")?.toLongOrNull() ?: System.currentTimeMillis()
        val data = polocloudShared.cloudInformationProvider().findDays(from, to)

        val jsonArray = JsonArray()
        data.forEach {
            val obj = JsonObject()
            obj.addProperty("timestamp", it.timestamp)
            obj.addProperty("avgCpu", it.avgCpu)
            obj.addProperty("avgRam", it.avgRam)
            jsonArray.add(obj)
        }

        context.status(200).json(jsonArray.toString())
    }
}
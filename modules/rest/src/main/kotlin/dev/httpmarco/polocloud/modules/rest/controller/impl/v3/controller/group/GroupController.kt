package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.group

import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.group.GroupCreateModel
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import dev.httpmarco.polocloud.shared.groups.Group
import dev.httpmarco.polocloud.shared.groups.GroupInformation
import dev.httpmarco.polocloud.shared.groups.SharedGroupProvider
import dev.httpmarco.polocloud.shared.platform.PlatformIndex
import dev.httpmarco.polocloud.shared.polocloudShared
import io.javalin.http.Context

class GroupController : Controller("/group") {

    @Request(requestType = RequestType.GET, path = "s/count", permission = "polocloud.group.count")
    fun groupCount(context: Context) {
        val from = context.queryParam("from")?.toLongOrNull() ?: 0L
        val to = context.queryParam("to")?.toLongOrNull() ?: 0L

        val groups = polocloudShared.groupProvider().findAll()
        val totalCount = groups.count()

        if (from == 0L || to == 0L) {
            context.status(200).json(
                JsonObject().apply {
                    addProperty("groupCount", totalCount)
                }.toString()
            )
            return
        }

        if (from < 0 || to < 0 || from > to) {
            context.status(400).json(message("Invalid range"))
            return
        }

        val current = groups.count { it.information.createdAt in from..to }
        val previous = groups.count { it.information.createdAt < from }

        val percentage = when {
            previous > 0 -> (current * 100.0 / previous)
            current > 0 && previous == 0 -> current * 100.0
            else         -> 0.0
        }

        context.status(200).json(
            JsonObject().apply {
                addProperty("groupCount", current)
                addProperty("percentage", percentage)
            }.toString()
        )
    }

    @Request(requestType = RequestType.POST, path = "/create", permission = "polocloud.group.create")
    fun createGroup(context: Context) {
        val groupCreateModel = try {
            context.bodyAsClass(GroupCreateModel::class.java)
        } catch (e: Exception) {
            context.status(400).json(message("Invalid body"))
            return
        }

        if (groupCreateModel.name.isBlank() ||
            groupCreateModel.minMemory < 0 ||
            groupCreateModel.maxMemory < 0 ||
            groupCreateModel.platform.name.isBlank() ||
            groupCreateModel.platform.version.isBlank() ||
            groupCreateModel.percentageToStartNewService < 0.0 ||
            groupCreateModel.percentageToStartNewService > 100.0) {
            context.status(400).json(message("Invalid group data"))
            return
        }

        val platform = polocloudShared.platformProvider().find(groupCreateModel.name)
        if (platform == null) {
            context.status(404).json(message("Platform not found"))
            return
        }

        val platformVersion = platform.versions.find { it.version == groupCreateModel.platform.version }
        if (platformVersion == null) {
            context.status(404).json(message("Platform version not found"))
            return
        }

        val platformIndex = PlatformIndex(platform.name, platformVersion.version)
        val groupInformation = GroupInformation(groupCreateModel.information.createdAt)

        val group = Group(
            groupCreateModel.name,
            groupCreateModel.minMemory,
            groupCreateModel.maxMemory,
            groupCreateModel.minOnlineService,
            groupCreateModel.maxOnlineService,
            platformIndex,
            groupCreateModel.percentageToStartNewService,
            groupInformation,
            groupCreateModel.templates,
            groupCreateModel.properties
        )

        (polocloudShared.groupProvider() as SharedGroupProvider<Group>).create(group)
        context.status(201).json(
            JsonObject().apply {
                addProperty("message", "Group created successfully")
            }.toString()
        )
    }
}
package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.group

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.defaultResponse
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.group.GroupCreateModel
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.group.GroupEditModel
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import dev.httpmarco.polocloud.shared.groups.Group
import dev.httpmarco.polocloud.shared.groups.SharedGroupProvider
import dev.httpmarco.polocloud.shared.groups.toJson
import dev.httpmarco.polocloud.shared.platform.PlatformIndex
import dev.httpmarco.polocloud.shared.polocloudShared
import dev.httpmarco.polocloud.shared.properties.PropertyHolder
import dev.httpmarco.polocloud.shared.template.Template
import io.javalin.http.Context

class GroupController : Controller("/group") {

    @Request(requestType = RequestType.GET, path = "s/count", permission = "polocloud.group.count")
    fun groupCount(context: Context) {
        val from = context.queryParam("from")?.toLongOrNull() ?: 0L
        val to = context.queryParam("to")?.toLongOrNull() ?: 0L

        val groups = polocloudShared.groupProvider().findAll()
        val totalCount = groups.count()

        if (from == 0L || to == 0L) {
            val data = JsonObject().apply {
                addProperty("groupCount", totalCount)
            }

            context.defaultResponse(200, data = data)
            return
        }

        if (from < 0 || to < 0 || from > to) {
            context.defaultResponse(400,"Invalid range")
            return
        }

        val current = groups.count { it.createdAt in from..to }
        val previous = groups.count { it.createdAt < from }

        val percentage = when {
            previous > 0 -> (current * 100.0 / previous)
            current > 0 && previous == 0 -> current * 100.0
            else         -> 0.0
        }

        val data = JsonObject().apply {
            addProperty("groupCount", current)
            addProperty("percentage", percentage)
        }

        context.defaultResponse(200, data = data)
    }

    @Request(requestType = RequestType.POST, path = "/create", permission = "polocloud.group.create")
    fun createGroup(context: Context) {
        val model = context.parseBodyOrBadRequest<GroupCreateModel>() ?: return
        if (!context.validate(model.name.isNotBlank(), "Group name is required")) return
        if (!context.validate(model.minMemory >= 0, "Group minMemory must be >= 0")) return
        if (!context.validate(model.maxMemory >= 0, "Group maxMemory must be >= 0")) return
        if (!context.validate(model.platform.name.isNotBlank(), "Group platform name is required")) return
        if (!context.validate(model.platform.version.isNotBlank(), "Group platform version is required")) return
        if (!context.validate(model.percentageToStartNewService in 0.0..100.0, "Group percentage must be between 0 and 100")) return

        if (polocloudShared.groupProvider().find(model.name) != null) {
            context.defaultResponse(409,"Group with this name already exists")
            return
        }

        if (!context.validate(model.minMemory <= model.maxMemory, "Minimum memory cannot be greater than maximum memory")) return
        if (!context.validate(model.minOnlineService >= 0, "Minimum online services cannot be negative")) return
        if (!context.validate(model.maxOnlineService >= 0, "Maximum online services cannot be negative")) return
        if (!context.validate(model.minOnlineService <= model.maxOnlineService, "Minimum online services cannot be greater than maximum online services")) return

        val platform = polocloudShared.platformProvider().find(model.platform.name)
        if (platform == null) {
            context.defaultResponse(404, "Platform not found")
            return
        }

        val platformVersion = platform.versions.find { it.version == model.platform.version }
        if (platformVersion == null) {
            context.defaultResponse(404, "Platform version not found")
            return
        }

        val platformIndex = PlatformIndex(platform.name, platformVersion.version)
        val templates = model.templates.map { Template(it) }

        val group = AbstractGroup(
            model.name,
            model.minMemory,
            model.maxMemory,
            model.minOnlineService,
            model.maxOnlineService,
            model.percentageToStartNewService,
            platformIndex,
            model.createdAt,
            templates,
            PropertyHolder(model.properties)
        )

        (Agent.groupProvider() as SharedGroupProvider<Group>).create(group)
        context.defaultResponse(201,"Group created successfully")
    }

    @Request(requestType = RequestType.GET, path = "s/list", permission = "polocloud.group.list")
    fun listGroups(context: Context) {
        val groups = polocloudShared.groupProvider().findAll()
        if (groups.isEmpty()) {
            context.defaultResponse(400, "No groups found")
            return
        }

        val data = JsonArray().apply {
            groups.map { group ->
                add(group.toJson())
            }
        }

        context.defaultResponse(200, data = data)
    }

    @Request(requestType = RequestType.GET, path = "/{name}", permission = "polocloud.group.get")
    fun getGroup(context: Context) {
        val name = context.pathParam("name")
        if (name.isBlank()) {
            context.defaultResponse(400, "Invalid group name")
            return
        }

        val group = polocloudShared.groupProvider().find(name)
        if (group == null) {
            context.defaultResponse(404, "Group not found")
            return
        }

        context.defaultResponse(200, data = group.toJson())
    }

    @Request(requestType = RequestType.DELETE, path = "/{name}", permission = "polocloud.group.delete")
    fun deleteGroup(context: Context) {
        val name = context.pathParam("name")
        if (name.isBlank()) {
            context.defaultResponse(400,"Invalid group name")
            return
        }

        var group = polocloudShared.groupProvider().find(name)
        if (group == null) {
            context.defaultResponse(400,"Group not found")
            return
        }

        group = group as AbstractGroup

        group.shutdownAll()
        Agent.runtime.groupStorage().delete(group)

        context.defaultResponse(204)
    }

    @Request(requestType = RequestType.PATCH, path = "/{name}", permission = "polocloud.group.edit")
    fun editGroup(context: Context) {
        val name = context.pathParam("name")
        var group = polocloudShared.groupProvider().find(name)

        if (group == null) {
            context.defaultResponse(400,"Group cloud not be found")
            return
        }

        group = group as AbstractGroup
        val model = context.parseBodyOrBadRequest<GroupEditModel>() ?: return
        if (!context.validate(model.minMemory >= 0, "Group minMemory must be >= 0")) return
        if (!context.validate(model.maxMemory >= 0, "Group maxMemory must be >= 0")) return

        if (!context.validate(model.percentageToStartNewService in 0.0..100.0,
                "Group percentage to start new service must be between 0 and 100")) return

        if (!context.validate(
                model.minMemory <= model.maxMemory,
                "Group minimum memory cannot be greater than maximum memory")) return

        if (!context.validate(model.minOnlineService >= 0, "Group minimum online services cannot be negative")) return
        if (!context.validate(model.maxOnlineService >= 0, "Group maximum online services cannot be negative")) return

        if (!context.validate(
                model.minOnlineService <= model.maxOnlineService,
                "Minimum online services cannot be greater than maximum online services")) return

        group.updateMinMemory(model.minMemory)
        group.updateMaxMemory(model.maxMemory)
        group.updateMinOnlineServices(model.minOnlineService)
        group.updateMaxOnlineServices(model.maxOnlineService)
        group.updatePercentageToStartNewService(model.percentageToStartNewService)

        group.update()
        context.defaultResponse(201,"Group edited successfully")
    }
}
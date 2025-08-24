package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.group

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.group.GroupCreateModel
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.group.GroupEditModel
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

        if (polocloudShared.groupProvider().find(groupCreateModel.name) != null) {
            context.status(409).json(message("Group with this name already exists"))
            return
        }

        if (groupCreateModel.minMemory > groupCreateModel.maxMemory) {
            context.status(400).json(message("Minimum memory cannot be greater than maximum memory"))
            return
        }

        if (groupCreateModel.minOnlineService < 0 || groupCreateModel.maxOnlineService < 0) {
            context.status(400).json(message("Minimum and maximum online services cannot be negative"))
            return
        }

        if (groupCreateModel.minOnlineService > groupCreateModel.maxOnlineService) {
            context.status(400).json(message("Minimum online services cannot be greater than maximum online services"))
            return
        }

        val platform = polocloudShared.platformProvider().find(groupCreateModel.platform.name)
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

        val group = AbstractGroup(
            groupCreateModel.name,
            groupCreateModel.minMemory,
            groupCreateModel.maxMemory,
            groupCreateModel.minOnlineService,
            groupCreateModel.maxOnlineService,
            groupCreateModel.percentageToStartNewService,
            platformIndex,
            groupInformation,
            groupCreateModel.templates,
            groupCreateModel.properties
        )

        (Agent.groupProvider() as SharedGroupProvider<Group>).create(group)
        context.status(201).json(
            JsonObject().apply {
                addProperty("message", "Group created successfully")
            }.toString()
        )
    }

    @Request(requestType = RequestType.GET, path = "s/list", permission = "polocloud.group.list")
    fun listGroups(context: Context) {
        val services = polocloudShared.groupProvider().findAll()
        if (services.isEmpty()) {
            context.status(200).json(message("No groups found"))
            return
        }


        context.status(200).json(
            JsonArray().apply {
                services.map { group ->
                    add(
                        JsonObject().apply {
                            addProperty("name", group.name)
                            addProperty("minMemory", group.minMemory)
                            addProperty("maxMemory", group.maxMemory)
                            addProperty("minOnlineService", group.minOnlineService)
                            addProperty("maxOnlineService", group.maxOnlineService)
                            add("platform", JsonObject().apply {
                                addProperty("name", group.platform.name)
                                addProperty("version", group.platform.version)
                            })
                            addProperty("percentageToStartNewService", group.percentageToStartNewService)
                            add("information", JsonObject().apply {
                                addProperty("createdAt", group.information.createdAt)
                            })
                            add("templates", JsonArray().apply {
                                group.templates.forEach { template ->
                                    add(template)
                                }
                            })
                            add("properties", JsonObject().apply {
                                group.properties.forEach { (key, value) ->
                                    add(key, value)
                                }
                            })
                        }
                    )
                }
            }.toString()
        )
    }

    @Request(requestType = RequestType.GET, path = "/{name}", permission = "polocloud.group.get")
    fun getGroup(context: Context) {
        val name = context.pathParam("name")
        if (name.isBlank()) {
            context.status(400).json(message("Invalid group name"))
            return
        }

        val group = polocloudShared.groupProvider().find(name)
        if (group == null) {
            context.status(404).json(message("Group not found"))
            return
        }

        context.status(200).json(
            JsonObject().apply {
                addProperty("name", group.name)
                addProperty("minMemory", group.minMemory)
                addProperty("maxMemory", group.maxMemory)
                addProperty("minOnlineService", group.minOnlineService)
                addProperty("maxOnlineService", group.maxOnlineService)
                add("platform", JsonObject().apply {
                    addProperty("name", group.platform.name)
                    addProperty("version", group.platform.version)
                })
                addProperty("percentageToStartNewService", group.percentageToStartNewService)
                add("information", JsonObject().apply {
                    addProperty("createdAt", group.information.createdAt)
                })
                add("templates", JsonArray().apply {
                    group.templates.forEach { template ->
                        add(template)
                    }
                })
                add("properties", JsonObject().apply {
                    group.properties.forEach { (key, value) ->
                        add(key, value)
                    }
                })
            }.toString()
        )
    }

    @Request(requestType = RequestType.DELETE, path = "/{name}", permission = "polocloud.group.delete")
    fun deleteGroup(context: Context) {
        val name = context.pathParam("name")
        if (name.isBlank()) {
            context.status(400).json(message("Invalid group name"))
            return
        }

        var group = polocloudShared.groupProvider().find(name)
        if (group == null) {
            context.status(400).json(message("Group not found"))
            return
        }

        group = group as AbstractGroup

        Agent.runtime.groupStorage().destroy(group)
        group.shutdownAll()

        context.status(204).json(message("Group deleted successfully"))
    }

    @Request(requestType = RequestType.PATCH, path = "/{name}", permission = "polocloud.group.edit")
    fun editGroup(context: Context) {
        val name = context.pathParam("name")
        var group = polocloudShared.groupProvider().find(name)

        if (group == null) {
            context.status(400).json(message("Group cloud not be found"))
            return
        }

        group = group as AbstractGroup

        val groupEditModel = try {
            context.bodyAsClass(GroupEditModel::class.java)
        } catch (e: Exception) {
            context.status(400).json(message("Invalid body"))
            return
        }

        if (groupEditModel.minMemory < 0 ||
            groupEditModel.maxMemory < 0 ||
            groupEditModel.percentageToStartNewService < 0.0 ||
            groupEditModel.percentageToStartNewService > 100.0) {
            context.status(400).json(message("Invalid group data"))
            return
        }

        if (groupEditModel.minMemory > groupEditModel.maxMemory) {
            context.status(400).json(message("Minimum memory cannot be greater than maximum memory"))
            return
        }

        if (groupEditModel.minOnlineService < 0 || groupEditModel.maxOnlineService < 0) {
            context.status(400).json(message("Minimum and maximum online services cannot be negative"))
            return
        }

        if (groupEditModel.minOnlineService > groupEditModel.maxOnlineService) {
            context.status(400).json(message("Minimum online services cannot be greater than maximum online services"))
            return
        }

        group.updateMinMemory(groupEditModel.minMemory)
        group.updateMaxMemory(groupEditModel.maxMemory)
        group.updateMinOnlineServices(groupEditModel.minOnlineService)
        group.updateMaxOnlineServices(groupEditModel.maxOnlineService)
        group.updatePercentageToStartNewService(groupEditModel.percentageToStartNewService)

        group.update()
        context.status(201).json(
            JsonObject().apply {
                addProperty("message", "Group edited successfully")
            }.toString()
        )
    }
}
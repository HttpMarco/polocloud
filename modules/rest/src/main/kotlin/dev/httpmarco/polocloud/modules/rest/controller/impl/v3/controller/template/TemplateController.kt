package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.template

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.services.AbstractService
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.template.CreateTemplateModel
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.template.EditTemplateModel
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import dev.httpmarco.polocloud.shared.template.Template
import io.javalin.http.Context

class TemplateController : Controller("/template") {

    @Request(requestType = RequestType.GET, path = "s/list", permission = "polocloud.templates.list")
    fun listTemplates(context: Context) {
        val services = Agent.serviceProvider().findAll()

        val templates = mutableSetOf<Template>()
        services.forEach { service ->
            templates += Agent.runtime.templates().templates(service as AbstractService)
        }

        val sortedTemplates = templates.toList().sortedBy { it.name }

        context.status(200).json(
            JsonArray().apply {
                templates.forEach { template ->
                    add(
                        JsonObject().apply {
                            addProperty("name", template.name)
                            addProperty("size", template.size)
                        }
                    )
                }
            }.toString()
        )
    }

    @Request(requestType = RequestType.POST, path = "/", permission = "polocloud.templates.create")
    fun createTemplate(context: Context) {
        val createTemplateModel = try {
            context.bodyAsClass(CreateTemplateModel::class.java)
        } catch (e: Exception) {
            context.status(400).json(message("Invalid body"))
            return
        }

        if (createTemplateModel.name.isBlank()) {
            context.status(400).json(message("Invalid body: name cannot be empty"))
            return
        }

        Agent.runtime.templates().create(createTemplateModel.name)
        context.status(202).json(message("Creating template"))
    }

    @Request(requestType = RequestType.DELETE, path = "/{templateName}", permission = "polocloud.templates.delete")
    fun deleteTemplate(context: Context) {
        val template = context.pathParam("templateName")

        Agent.runtime.templates().delete(template)
        context.status(202).json(message("Deleted template"))
    }

    @Request(requestType = RequestType.PATCH, path = "/{templateName}", permission = "polocloud.templates.edit")
    fun editTemplate(context: Context) {
        val template = context.pathParam("templateName")

        val editTemplateModel = try {
            context.bodyAsClass(EditTemplateModel::class.java)
        } catch (e: Exception) {
            context.status(400).json(message("Invalid body"))
            return
        }

        if (editTemplateModel.name.isBlank()) {
            context.status(400).json(message("Invalid body: name cannot be empty"))
            return
        }


        Agent.runtime.templates().update(template, editTemplateModel.name)
        context.status(202).json(message("Template edited"))
    }
}
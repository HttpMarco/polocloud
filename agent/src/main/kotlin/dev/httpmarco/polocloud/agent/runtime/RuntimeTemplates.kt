package dev.httpmarco.polocloud.agent.runtime

import dev.httpmarco.polocloud.agent.services.AbstractService
import dev.httpmarco.polocloud.shared.template.Template

interface RuntimeTemplates<out S : AbstractService> {

    fun bindTemplate(service: @UnsafeVariance S)

    fun saveTemplate(template : String, service: @UnsafeVariance S)

    fun templates(service: @UnsafeVariance S): List<Template>

    fun create(name: String)

    fun delete(name: String)

    fun update(oldName: String, newName: String)
}
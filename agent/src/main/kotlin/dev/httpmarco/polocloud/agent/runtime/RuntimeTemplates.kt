package dev.httpmarco.polocloud.agent.runtime

import dev.httpmarco.polocloud.agent.services.AbstractService

interface RuntimeTemplates<out S : AbstractService> {

    fun bindTemplate(service: @UnsafeVariance S)

    fun saveTemplate(template : String, service: @UnsafeVariance S)

}
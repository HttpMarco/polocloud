package dev.httpmarco.polocloud.agent.runtime.k8s

import dev.httpmarco.polocloud.agent.runtime.RuntimeTemplates
import dev.httpmarco.polocloud.shared.template.Template

class KubernetesRuntimeTemplates : RuntimeTemplates<KubernetesService> {

    override fun bindTemplate(service: KubernetesService) {
        TODO("Not yet implemented")
    }

    override fun saveTemplate(
        template: String,
        service: KubernetesService
    ) {
        TODO("Not yet implemented")
    }

    override fun templates(service: KubernetesService): List<Template> {
        TODO("Not yet implemented")
    }

    override fun create(name: String) {
        TODO("Not yet implemented")
    }

    override fun delete(name: String) {
        TODO("Not yet implemented")
    }

    override fun update(oldName: String, newName: String) {
        TODO("Not yet implemented")
    }
}
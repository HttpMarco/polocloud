package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import dev.httpmarco.polocloud.shared.template.Template

class DockerImage(val client: DockerClient, name: String) : Template(name) {

    override fun size(): String {
        val info = client.inspectImageCmd(name).exec()
        val sizeBytes = info.size ?: 0L

        return humanReadableSize(sizeBytes)
    }
}
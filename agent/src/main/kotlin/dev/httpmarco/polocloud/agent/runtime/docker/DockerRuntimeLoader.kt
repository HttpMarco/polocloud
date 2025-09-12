package dev.httpmarco.polocloud.agent.runtime.docker

import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.runtime.Runtime
import dev.httpmarco.polocloud.agent.runtime.RuntimeLoader
import java.nio.file.Files
import java.nio.file.Paths

class DockerRuntimeLoader : RuntimeLoader {

    override fun runnable(): Boolean {
        return try {
            return Files.exists(Paths.get("/.dockerenv")) || Files.exists(Paths.get("/run/.containerenv"))
        } catch (e: Exception) {
            i18n.debug("agent.runtime.docker.connection.failed", e.javaClass.simpleName, e.message)
            false
        }
    }

    override fun instance(): Runtime {
        return DockerRuntime()
    }
}
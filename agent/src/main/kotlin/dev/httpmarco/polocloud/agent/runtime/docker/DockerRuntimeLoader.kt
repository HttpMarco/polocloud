package dev.httpmarco.polocloud.agent.runtime.docker

import dev.httpmarco.polocloud.agent.runtime.Runtime
import dev.httpmarco.polocloud.agent.runtime.RuntimeLoader
import java.nio.file.Files
import java.nio.file.Paths

class DockerRuntimeLoader : RuntimeLoader {

    override fun runnable(): Boolean {
        return try {
            val dockerEnv = Files.exists(Paths.get("/.dockerenv"))
            val cgroupDocker = try {
                Files.readAllLines(Paths.get("/proc/1/cgroup")).any { it.contains("docker") }
            } catch (_: Exception) { false }
            dockerEnv && cgroupDocker
        } catch (_: Exception) {
            false
        }
    }

    override fun instance(): Runtime {
        return DockerRuntime()
    }
}
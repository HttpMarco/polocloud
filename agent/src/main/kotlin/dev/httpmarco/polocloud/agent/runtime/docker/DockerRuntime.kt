package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientBuilder
import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.runtime.Runtime
import dev.httpmarco.polocloud.agent.runtime.RuntimeConfigHolder
import dev.httpmarco.polocloud.agent.runtime.RuntimeTemplateStorage
import dev.httpmarco.polocloud.agent.services.AbstractService
import java.nio.file.Files
import java.nio.file.Paths


class DockerRuntime : Runtime() {

    private val dockerClient = createLocalDockerClient()
    private val serviceStorage = DockerRuntimeServiceStorage()
    private val groupStorage = DockerRuntimeGroupStorage()
    private val expender = DockerExpender()
    private val runtimeFactory = DockerFactory(dockerClient)
    private val templateStorage = DockerTemplateStorage(dockerClient)
    private val dockerConfigHolder = DockerConfigHolder()

    fun createLocalDockerClient(): DockerClient {
        val config = DefaultDockerClientConfig.createDefaultConfigBuilder()
            .withDockerHost("unix:///var/run/docker.sock")
            .build()

        return DockerClientBuilder.getInstance(config).build()
    }

    override fun serviceStorage() = serviceStorage

    override fun groupStorage() = groupStorage

    override fun factory() = runtimeFactory

    override fun expender() = expender

    override fun templateStorage() = templateStorage

    override fun configHolder() = dockerConfigHolder

    override fun sendCommand(command: String) {
        TODO("Not yet implemented")
    }
}
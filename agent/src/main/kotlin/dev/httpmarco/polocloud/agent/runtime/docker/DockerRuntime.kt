package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.model.Event
import com.github.dockerjava.api.model.Frame
import com.github.dockerjava.api.model.Mount
import com.github.dockerjava.api.model.MountType
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.*
import dev.httpmarco.polocloud.common.image.pngToBase64DataUrl
import dev.httpmarco.polocloud.common.version.polocloudVersion
import dev.httpmarco.polocloud.platforms.PlatformParameters
import dev.httpmarco.polocloud.shared.service.ServiceInformation
import dev.httpmarco.polocloud.v1.GroupType
import dev.httpmarco.polocloud.v1.services.ServiceState
import kotlin.io.path.Path
import kotlin.io.path.name


class DockerRuntime : Runtime() {

    private val client = createLocalDockerClient()
    private val serviceStorage = DockerRuntimeServiceStorage(client)
    private val groupStorage = DockerRuntimeGroupStorage()
    private val expender = DockerExpender(client)
    private val runtimeFactory = DockerRuntimeFactory(client)
    private val templateStorage = DockerTemplateStorage(client)
    private val dockerConfigHolder = DockerConfigHolder()
    private val informationThread = DockerCloudInformationThread()
    private val queue = DockerThreadedRuntimeQueue()

    fun createLocalDockerClient(): DockerClient {
        val config = DefaultDockerClientConfig.createDefaultConfigBuilder().build()

        val httpClient = ApacheDockerHttpClient.Builder()
            .dockerHost(config.dockerHost)
            .sslConfig(config.sslConfig)
            .build()

        return DockerClientImpl.getInstance(config, httpClient)
    }

    override fun boot() {
        informationThread.start()
      //  queue.start()
    }

    override fun serviceStorage() = serviceStorage

    override fun groupStorage() = groupStorage

    override fun factory() = runtimeFactory

    override fun expender() = expender

    override fun templateStorage() = templateStorage

    override fun configHolder() = dockerConfigHolder

    override fun sendCommand(command: String) {
        //todo DELETE HERE
    }
}
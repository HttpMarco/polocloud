package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import dev.httpmarco.polocloud.agent.runtime.Runtime
import dev.httpmarco.polocloud.agent.runtime.abstract.AbstractThreadedRuntimeQueue
import dev.httpmarco.polocloud.agent.runtime.local.LocalCloudInformationThread
import dev.httpmarco.polocloud.agent.runtime.local.LocalCpuDetectionThread


class DockerRuntime : Runtime() {

    private val client = createLocalDockerClient()
    private val serviceStorage = DockerRuntimeServiceStorage(client)
    private val groupStorage = DockerRuntimeGroupStorage(client)
    private val expender = DockerExpender(client)
    private val runtimeFactory = DockerRuntimeFactory(client)
    private val templateStorage = DockerTemplateStorage(client)
    private val dockerConfigHolder = DockerConfigHolder()
    private val runtimeQueue = AbstractThreadedRuntimeQueue()


    // TODO REMOVE
    private val runtimeCpuDetectionThread = LocalCpuDetectionThread()
    private val runtimeCloudInformationThread = LocalCloudInformationThread()

    fun createLocalDockerClient(): DockerClient {
        val config = DefaultDockerClientConfig.createDefaultConfigBuilder().build()

        val httpClient = ApacheDockerHttpClient.Builder()
            .dockerHost(config.dockerHost)
            .sslConfig(config.sslConfig)
            .build()

        return DockerClientImpl.getInstance(config, httpClient)
    }

    override fun boot() {
        runtimeQueue.start()

        this.runtimeCpuDetectionThread.start()
        this.runtimeCloudInformationThread.start()
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
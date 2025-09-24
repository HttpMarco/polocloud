package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import dev.httpmarco.polocloud.agent.runtime.*
import dev.httpmarco.polocloud.agent.runtime.abstract.AbstractServiceStatsThread
import java.net.Inet4Address
import java.net.NetworkInterface


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
        queue.start()
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

    override fun detectLocalAddress(): String {
        val interfaces = NetworkInterface.getNetworkInterfaces()
        for (iface in interfaces) {
            if (!iface.isLoopback && iface.isUp) {
                for (addr in iface.inetAddresses) {
                    if (addr is Inet4Address && !addr.isLoopbackAddress) {
                        return addr.hostAddress
                    }
                }
            }
        }
        return "null";
    }

    override fun serviceStatsThread(): AbstractServiceStatsThread<*> {
        return DockerServiceStatsThread(this.client)
    }
}
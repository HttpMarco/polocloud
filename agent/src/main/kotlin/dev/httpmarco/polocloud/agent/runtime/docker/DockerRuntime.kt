package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.model.Event
import com.github.dockerjava.api.model.Mount
import com.github.dockerjava.api.model.MountType
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.*
import dev.httpmarco.polocloud.v1.services.ServiceState


class DockerRuntime : Runtime() {

    private val client = createLocalDockerClient()
    private val serviceStorage = DockerRuntimeServiceStorage(client)
    private val groupStorage = DockerRuntimeGroupStorage(client)
    private val expender = DockerExpender(client)
    private val runtimeFactory = DockerRuntimeFactory(client)
    private val templateStorage = DockerTemplateStorage(client)
    private val dockerConfigHolder = DockerConfigHolder()

    fun createLocalDockerClient(): DockerClient {
        val config = DefaultDockerClientConfig.createDefaultConfigBuilder().build()

        val httpClient = ApacheDockerHttpClient.Builder()
            .dockerHost(config.dockerHost)
            .sslConfig(config.sslConfig)
            .build()

        return DockerClientImpl.getInstance(config, httpClient)
    }

    override fun boot() {
        client.eventsCmd()
            .withEventTypeFilter("container")
            .exec(object : ResultCallback.Adapter<Event>() {
                override fun onNext(event: Event) {
                    val action = event.action
                    val containerId = event.id
                    val serviceName = event.actor?.attributes?.get("com.docker.swarm.service.name")

                    if (action == "start" && serviceName != null) {

                        val task = client.listTasksCmd()
                            .withServiceFilter(serviceName)
                            .exec()
                            .firstOrNull { it.status?.containerStatus?.containerID == containerId }

                        val taskSlot = task?.slot

                        // TODO handle all service labels


                        val service = client.inspectServiceCmd(serviceName).exec()
                        val version = service.version?.index
                        val spec = service.spec!!

                        val updatedSpec = spec.withTaskTemplate(
                            spec.taskTemplate!!.withContainerSpec(
                                spec.taskTemplate!!.containerSpec!!.withMounts(
                                    listOf(
                                        Mount()
                                            .withType(MountType.BIND)
                                            .withSource("C:\\Users\\nervi\\Desktop\\123\\temp\\${serviceName.split("-").last()}-$taskSlot")
                                            .withTarget("/app")
                                    )
                                )
                            )
                        )
                        client.updateServiceCmd(service.id, updatedSpec)
                            .withVersion(version!!)
                            .exec()
                    }
                }
            })

        logger.info("Already find running services&8: &f${serviceStorage().findAll().joinToString { it.name() }}")
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
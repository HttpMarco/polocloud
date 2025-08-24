package dev.httpmarco.polocloud.agent.runtime.k8s

import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.runtime.Runtime
import dev.httpmarco.polocloud.agent.runtime.RuntimeConfigHolder
import io.fabric8.kubernetes.client.KubernetesClientBuilder
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class KubernetesRuntime : Runtime {

    private val kubernetesClient = KubernetesClientBuilder().build()
    private val groupStorage = KubernetesRuntimeGroupStorage(kubernetesClient)
    private val configHolder = KubernetesRuntimeConfigHolder(kubernetesClient)
    private val serviceStorage = KubernetesRuntimeServiceStorage()
    private val factory = KubernetesFactory()
    private val expender = KubernetesExpender()
    private val templates = KubernetesRuntimeTemplates()
    private val started = System.currentTimeMillis()

    override fun runnable(): Boolean {
        return try {
            val future = CompletableFuture.supplyAsync {
                kubernetesClient.kubernetesVersion != null
            }
            future.get(1, TimeUnit.SECONDS)
        } catch (e: Exception) {
            i18n.debug("agent.runtime.k8s.connection.failed", e.javaClass.simpleName, e.message)
            false
        }
    }

    override fun serviceStorage() = serviceStorage

    override fun groupStorage() = groupStorage

    override fun factory() = factory

    override fun expender() = expender

    override fun templates() = templates

    override fun configHolder() = configHolder

    override fun started() = started

    override fun sendCommand(command: String) {
        TODO("Not yet implemented")
    }

}
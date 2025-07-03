package dev.httpmarco.polocloud.agent.runtime.k8s

import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.Runtime
import dev.httpmarco.polocloud.agent.runtime.RuntimeExpender
import dev.httpmarco.polocloud.agent.runtime.RuntimeFactory
import io.fabric8.kubernetes.client.KubernetesClientBuilder
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class KubernetesRuntime : Runtime {

    private val kubernetesClient = KubernetesClientBuilder().build()
    private val groupStorage = KubernetesRuntimeGroupStorage(kubernetesClient)
    private val serviceStorage = KubernetesRuntimeServiceStorage()
    private val factory = KubernetesFactory()
    private val expender = KubernetesExpender()

    override fun runnable(): Boolean {
        return try {
            val future = CompletableFuture.supplyAsync {
                kubernetesClient.kubernetesVersion != null
            }
            future.get(1, TimeUnit.SECONDS)
        } catch (e: Exception) {
            logger.debug("Failed to connect to Kubernetes API: ${e.javaClass.simpleName} - ${e.message}")
            false
        }
    }

    override fun serviceStorage() = serviceStorage

    override fun groupStorage() = groupStorage

    override fun factory() = factory;

    override fun expender() = expender

}
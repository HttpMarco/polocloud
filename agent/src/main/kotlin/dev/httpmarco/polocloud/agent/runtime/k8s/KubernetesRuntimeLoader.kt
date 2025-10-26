package dev.httpmarco.polocloud.agent.runtime.k8s

import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.runtime.Runtime
import dev.httpmarco.polocloud.agent.runtime.RuntimeLoader
import io.fabric8.kubernetes.client.KubernetesClientBuilder
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class KubernetesRuntimeLoader : RuntimeLoader {

    private val kubernetesClient = KubernetesClientBuilder().build()

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

    override fun instance(): Runtime {
        return KubernetesRuntime(kubernetesClient)
    }
}
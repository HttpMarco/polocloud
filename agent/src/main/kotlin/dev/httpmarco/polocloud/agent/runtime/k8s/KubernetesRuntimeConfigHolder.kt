package dev.httpmarco.polocloud.agent.runtime.k8s

import dev.httpmarco.polocloud.agent.configuration.Config
import dev.httpmarco.polocloud.agent.runtime.RuntimeConfigHolder
import dev.httpmarco.polocloud.common.json.GSON
import io.fabric8.kubernetes.api.model.ConfigMap
import io.fabric8.kubernetes.api.model.ObjectMeta
import io.fabric8.kubernetes.client.KubernetesClient

class KubernetesRuntimeConfigHolder(val client : KubernetesClient) : RuntimeConfigHolder {
    override fun <T : Config> read(fileName: String, defaultValue: T): T {
        val configMap = client.configMaps()
            .inNamespace(KUBERNETES_NAMESPACE)
            .withName(fileName)
            .get() ?: return defaultValue

        val raw = configMap.data?.get("config") ?: return defaultValue

        return try {
            GSON.fromJson(raw, defaultValue::class.java)
        } catch (_: Exception) {
            defaultValue
        }
    }

    override fun <T : Config> write(fileName: String, value: T) {
        val cmResource = client.configMaps()
            .inNamespace(KUBERNETES_NAMESPACE)
            .withName(fileName)

        val json = GSON.toJson(value)

        val configMap: ConfigMap = cmResource.get() ?: ConfigMap().apply {
            metadata = ObjectMeta().apply {
                name = fileName
                this.namespace = namespace
            }
            data = mutableMapOf()
        }

        val newData = (configMap.data ?: mutableMapOf()).toMutableMap()
        newData["config"] = json
        configMap.data = newData

        if (cmResource.get() == null) {
            client.configMaps().inNamespace(KUBERNETES_NAMESPACE).create(configMap)
        } else {
            cmResource.replace(configMap)
        }
    }
}
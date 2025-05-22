package dev.httpmarco.polocloud.runtime.kubernetes.groups

import dev.httpmarco.polocloud.agent.groups.GroupStorage
import dev.httpmarco.polocloud.runtime.kubernetes.KubernetesRuntime
import org.koin.core.component.inject

class KubernetesGroupStorage : GroupStorage {

    val runtime : KubernetesRuntime by inject()

}
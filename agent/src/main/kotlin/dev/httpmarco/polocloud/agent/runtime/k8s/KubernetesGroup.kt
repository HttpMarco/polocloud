package dev.httpmarco.polocloud.agent.runtime.k8s

import dev.httpmarco.polocloud.shared.service.Service
import io.fabric8.kubernetes.api.model.Namespaced
import io.fabric8.kubernetes.client.CustomResource
import io.fabric8.kubernetes.model.annotation.Group
import io.fabric8.kubernetes.model.annotation.Kind
import io.fabric8.kubernetes.model.annotation.Version

@Kind("Group")
@Version("v1")
@Group("polocloud.de")
class KubernetesGroup(val name: String) : CustomResource<Service, KubernetesGroupStatus>(), Namespaced {

}
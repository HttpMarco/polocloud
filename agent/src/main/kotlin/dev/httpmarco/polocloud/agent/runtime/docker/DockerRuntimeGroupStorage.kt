package dev.httpmarco.polocloud.agent.runtime.docker

import dev.httpmarco.polocloud.agent.runtime.abstract.AbstractGroupStorage
import kotlin.io.path.Path

open class DockerRuntimeGroupStorage() : AbstractGroupStorage(Path(("local/groups"))) {


}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "polocloud"
include("agent")
include("runtime")
include("runtime:local")
include("runtime:kubernetes")
include("runtime:docker")

include("agent:api")
include("agent:impl")
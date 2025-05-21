plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "polocloud"
include("proto")
include("metadata")
include("launcher")
include("agent")
include("runtime")
include("runtime:local")
include("runtime:kubernetes")
include("runtime:docker")
include("cli")
include("cli:terminal")
include("cli:installer")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "polocloud"
include("proto")
include("metadata")
include("launcher")
include("agent")
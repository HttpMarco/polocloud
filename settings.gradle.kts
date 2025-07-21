plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "polocloudv3"
include("sdk")
include("sdk:java")
include("proto")
include("agent")
include("platforms")
include("launcher")
include("common")
include("bridges")
include("bridges:java-bridge")
include("updater")
include("bridges:bridge-api")
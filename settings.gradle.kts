plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "polocloud"
include("polocloud-gradle-structure")
include("polocloud-common")
include("polocloud-api")
include("polocloud-daemon")
include("polocloud-node")
include("polocloud-launcher")
include("polocloud-installer")

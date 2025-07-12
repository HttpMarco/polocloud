plugins {
    kotlin("jvm") version "2.2.0"
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(libs.bundles.proto)
    implementation(libs.grpc.netty)
    compileOnly(libs.gson)
    implementation(project(":proto"))
    implementation(project(":shared"))
}

sourceSets["main"].java.srcDirs("../proto/build/generated/sources/proto","../shared/src/main/kotlin")

tasks.named("compileJava") {
    dependsOn(":proto:classes")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    archiveFileName.set("polocloud-java-sdk-$version.jar")
}

kotlin {
    jvmToolchain(21)
}
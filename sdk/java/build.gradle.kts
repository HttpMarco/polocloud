plugins {
    kotlin("jvm") version "2.2.0"
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(libs.bundles.proto)
    implementation(libs.grpc.netty)
    compileOnly(libs.gson)

    api(project(":proto"))
    implementation(project(":proto"))

    api(project(":shared"))
    implementation(project(":shared"))

    compileOnly("javax.annotation:javax.annotation-api:1.3.2")
}

sourceSets["main"].java.srcDirs("../../proto/build/generated/sources/proto")

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
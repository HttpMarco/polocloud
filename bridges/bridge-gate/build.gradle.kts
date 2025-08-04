repositories {
    maven {
        name = "buf"
        url = uri("https://buf.build/gen/maven")
    }
}

dependencies {
    implementation("build.buf.gen:minekube_gate_grpc_kotlin:1.4.3.1.20250516132630.2a0c7768e191")

    implementation(projects.sdk.sdkJava)
    implementation(projects.bridges.bridgeApi)
}
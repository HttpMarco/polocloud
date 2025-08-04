repositories {
    maven {
        name = "buf"
        url = uri("https://buf.build/gen/maven")
    }
}

dependencies {
    implementation("build.buf.gen:minekube_gate_connectrpc_kotlin:31.1.0.1.20250516132630.2a0c7768e191")
    implementation("com.connectrpc:connect-kotlin:0.7.4")

    implementation(projects.sdk.sdkJava)
    implementation(projects.bridges.bridgeApi)
}
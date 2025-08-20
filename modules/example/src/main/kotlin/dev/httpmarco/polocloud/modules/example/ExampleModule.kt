package dev.httpmarco.polocloud.modules.example

import dev.httpmarco.polocloud.shared.module.PolocloudModule
import dev.httpmarco.polocloud.shared.polocloudShared

class ExampleModule : PolocloudModule {
    override fun onEnable() {
        // Here you can add your module's initialization logic
        polocloudShared.logger().info("Example module enabled.")
    }

    override fun onDisable() {
        // Here you can add your module's cleanup logic
        polocloudShared.logger().info("Example module disabled.")
    }
}
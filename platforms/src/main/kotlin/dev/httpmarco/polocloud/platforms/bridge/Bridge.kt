package dev.httpmarco.polocloud.platforms.bridge

import java.nio.file.Path

data class Bridge(val name: String, val type: BridgeType, val bridgeClass: String? = null) {

    @Transient
    lateinit var path: Path

}
package dev.httpmarco.polocloud.agent.bridges

import kotlin.io.path.Path

class BridgeProvider {

    private val bridges = scanForBridges(Path("local/libs").toFile())

}
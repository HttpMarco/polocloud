package dev.httpmarco.polocloud.addons.api.location

import java.util.Objects

data class Position(val world: String, val x: Double, val y: Double, val z: Double) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Position) return false

        if (world != other.world) return false
        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(world, x, y, z)
    }
}
package dev.httpmarco.polocloud.bridges.fabric.v1_21_5

import net.fabricmc.loader.api.FabricLoader
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class VersionCheckPlugin : IMixinConfigPlugin {
    override fun onLoad(p0: String?) {
        // We don't need to do anything here
    }

    override fun getRefMapperConfig(): String? {
        return "v1_21_5-refmap.json"
    }

    override fun shouldApplyMixin(p0: String?, p1: String?): Boolean {
        val mcVersion: String = FabricLoader.getInstance()
            .getModContainer("minecraft")
            .get().metadata.version.friendlyString
        return mcVersion.startsWith("1.21.5")
    }

    override fun acceptTargets(
        p0: Set<String?>?,
        p1: Set<String?>?
    ) {
        // We don't need to do anything here
    }

    override fun getMixins(): List<String?>? {
        return null // We don't need to do anything here
    }

    override fun preApply(
        p0: String?,
        p1: ClassNode?,
        p2: String?,
        p3: IMixinInfo?
    ) {
        // We don't need to do anything here
    }

    override fun postApply(
        p0: String?,
        p1: ClassNode?,
        p2: String?,
        p3: IMixinInfo?
    ) {
        // We don't need to do anything here
    }
}
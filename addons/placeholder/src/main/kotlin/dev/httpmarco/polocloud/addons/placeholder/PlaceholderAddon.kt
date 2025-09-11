package dev.httpmarco.polocloud.addons.placeholder

import dev.httpmarco.polocloud.sdk.java.Polocloud
import org.bukkit.plugin.java.JavaPlugin

class PlaceholderAddon : JavaPlugin() {
    
    private var polocloud: Polocloud? = null
    private lateinit var placeholderExpansion: PlaceholderExpansion
    
    override fun onEnable() {
        polocloud = Polocloud.instance()

        placeholderExpansion = PlaceholderExpansion { polocloud }

        if (placeholderExpansion.register()) {
            logger.info("PlaceholderAPI expansion registered successfully!")
        } else {
            logger.warning("Failed to register PlaceholderAPI expansion!")
        }

        logger.info("PoloCloud Placeholder Addon enabled!")
    }
    
    override fun onDisable() {
        if (::placeholderExpansion.isInitialized) {
            placeholderExpansion.unregister()
        }
        
        logger.info("PoloCloud Placeholder Addon disabled!")
    }
}

package dev.httpmarco.polocloud.addons.hub

class HubConfigAccessor(val config: HubConfiguration): HubConfig {

    override fun messages(key: String): String {
        return this.config.messages[key] ?: "Message $key not found"
    }

    override fun aliases(): List<String> {
        return this.config.aliases
    }
}
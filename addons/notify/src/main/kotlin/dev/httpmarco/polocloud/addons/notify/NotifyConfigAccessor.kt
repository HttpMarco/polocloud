package dev.httpmarco.polocloud.addons.notify

class NotifyConfigAccessor(val config: NotifyConfiguration): NotifyConfig {
    override fun prefix(): String {
        return this.config.prefix
    }

    override fun messages(key: String): String {
        return this.config.messages[key] ?: "Message $key not found"
    }
}
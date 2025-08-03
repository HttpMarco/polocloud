package dev.httpmarco.polocloud.addons.api

object MiniMessageFormatter : MessageFormatter {
    override fun formatPrefix(): String =
        "<gradient:#00fdee:#118bd1><bold>PoloCloud</bold></gradient> <dark_gray>» <gray>"

    override fun format(message: String): String {
        return message
            .replace("§c", "<red>")
            .replace("§a", "<green>")
            .replace("§e", "<yellow>")
            .replace("§7", "<gray>")
            .replace("§8", "<dark_gray>")
            .replace("§b", "<aqua>")
            .replace("§l", "<bold>")
            .replace("§r", "</>")
            .replace("§", "")
    }
}
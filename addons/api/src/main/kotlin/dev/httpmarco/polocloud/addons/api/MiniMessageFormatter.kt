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

    override fun formatTablistHeader(): String =
        "\n          <gradient:#00fdee:#118bd1><bold>PoloCloud</bold></gradient> <dark_gray>- <gray>Simplest and easiest CloudSystem          \n<gray>Current Server: <aqua>%server% <dark_gray>| <gray>Players: <aqua>%online_players%/%max_players%</aqua>\n"

    override fun formatTablistFooter(): String
        =   "\n<gray>Version: <aqua>%polocloud_version%</aqua>\n" +
            "<dark_gray>» <gray>Powered by <gradient:#00fdee:#118bd1><bold>PoloCloud</bold></gradient> <dark_gray>«\n"
}
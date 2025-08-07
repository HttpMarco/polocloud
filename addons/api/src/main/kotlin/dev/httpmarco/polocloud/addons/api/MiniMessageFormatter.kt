package dev.httpmarco.polocloud.addons.api

object MiniMessageFormatter : MessageFormatter {
    override fun formatPrefix(): String =
        "<gradient:#00fdee:#118bd1><bold>PoloCloud</bold></gradient> <dark_gray>» <gray>"

    override fun format(message: String): String {
        return message
            .replace("§0", "<black>")
            .replace("§1", "<dark_blue>")
            .replace("§2", "<dark_green>")
            .replace("§3", "<dark_aqua>")
            .replace("§4", "<dark_red>")
            .replace("§5", "<dark_purple>")
            .replace("§6", "<gold>")
            .replace("§7", "<gray>")
            .replace("§8", "<dark_gray>")
            .replace("§9", "<blue>")
            .replace("§a", "<green>")
            .replace("§b", "<aqua>")
            .replace("§c", "<red>")
            .replace("§d", "<light_purple>")
            .replace("§e", "<yellow>")
            .replace("§f", "<white>")
            .replace("§k", "<obfuscated>")
            .replace("§l", "<bold>")
            .replace("§m", "<strikethrough>")
            .replace("§n", "<underline>")
            .replace("§o", "<italic>")
            .replace("§r", "</>")
            .replace("§", "")
    }

    override fun formatTablistHeader(): String =
        "\n          <gradient:#00fdee:#118bd1><bold>PoloCloud</bold></gradient> <dark_gray>- <gray>Simplest and easiest CloudSystem          \n<gray>Current Server: <aqua>%server% <dark_gray>| <gray>Players: <aqua>%online_players%/%max_players%</aqua>\n"

    override fun formatTablistFooter(): String
        =   "\n<gray>Version: <aqua>%polocloud_version%</aqua>\n" +
            "<dark_gray>» <gray>Powered by <gradient:#00fdee:#118bd1><bold>PoloCloud</bold></gradient> <dark_gray>«\n"
}
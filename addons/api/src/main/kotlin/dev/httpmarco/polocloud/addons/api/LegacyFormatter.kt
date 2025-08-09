package dev.httpmarco.polocloud.addons.api

object LegacyFormatter : MessageFormatter {
    override fun formatPrefix(): String = "§b§lPoloCloud §8» §7"

    override fun format(message: String): String {
        return message
    }

    override fun tablistEnabled(): Boolean = true

    override fun formatTablistHeader(): String =
        "\n          §b§lPoloCloud §8- §7Simplest and easiest CloudSystem          \n§7Current Server: §b%server% $8| §7Players: §b%online_players%/%max_players%\n"

    override fun formatTablistFooter(): String =
        "\n§7Version: §b%polocloud_version%\n" +
        "§8» §7Powered by §b§lPoloCloud §8«\n"

    override fun motdEnabled(): Boolean = true

    override fun formatMotdLineOne(): String =
        "§b§lPoloCloud §8» §7Simple and lightweight §8- §f%version%"

    override fun formatMotdLineTwo(): String =
        "§f§8» §7GitHub§8: §n§fgithub.polocloud.de"
}
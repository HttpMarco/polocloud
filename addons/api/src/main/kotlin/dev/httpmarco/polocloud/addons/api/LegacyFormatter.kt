package dev.httpmarco.polocloud.addons.api

object LegacyFormatter : MessageFormatter {
    override fun formatPrefix(): String = "§b§lPoloCloud §8» §7"

    override fun format(message: String): String {
        return message
    }

    override fun formatTablistHeader(): String =
        "\n          §b§lPoloCloud §8- §7Simplest and easiest CloudSystem          \n§7Current Server: §b%server% $8| §7Players: §b%online_players%/%max_players%\n"

    override fun formatTablistFooter(): String =
        "\n§7Version: §b%polocloud_version%\n" +
        "§8» §7Powered by §b§lPoloCloud §8«\n"
}
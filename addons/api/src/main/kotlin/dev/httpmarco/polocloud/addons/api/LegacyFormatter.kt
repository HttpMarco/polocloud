package dev.httpmarco.polocloud.addons.api

object LegacyFormatter : MessageFormatter {
    override fun formatPrefix(): String = "§b§lPoloCloud §8» §7"

    override fun format(message: String): String {
        return message
    }

    override fun formatTablistHeader(): String =
        "\n          §b§lPoloCloud §8- §7Simplest and easiest CloudSystem          \n§7Current Server: §b%server% §8| §7Players: §b%online_players%/%max_players%\n"

    override fun formatTablistFooter(): String =
        "\n§7Version: §b%polocloud_version%\n" +
        "§8» §7Powered by §b§lPoloCloud §8«\n"

    override fun formatMotdLineOne(): String =
        "§b§lPoloCloud §8» §7github.polocloud.de"

    override fun formatMotdLineTwo(): String =
        "§7§lVersion §8§l» §f§l%version%"

    override fun formatMaintenanceMotdLineOne(): String =
        "§c§lPoloCloud §8» §7github.polocloud.de"

    override fun formatMaintenanceMotdLineTwo(): String =
        "§7§lMaintenance - Please try again later"

    override fun formatMaintenancePingMessage(): String =
        "§c§lMaintenance"
}
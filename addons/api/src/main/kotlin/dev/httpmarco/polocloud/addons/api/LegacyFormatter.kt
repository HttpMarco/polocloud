package dev.httpmarco.polocloud.addons.api

object LegacyFormatter : MessageFormatter {
    override fun formatPrefix(): String = "§b§lPoloCloud §8» §7"

    override fun format(message: String): String {
        return message
    }
}
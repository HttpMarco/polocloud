package dev.httpmarco.polocloud.platforms

enum class PlatformLanguage(private var fileSuffix: String) {

    JAVA("jar"),
    GO("exe");

    fun suffix(): String {
        return ".$fileSuffix"
    }
}
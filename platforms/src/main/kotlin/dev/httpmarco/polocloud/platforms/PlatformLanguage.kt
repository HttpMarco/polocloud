package dev.httpmarco.polocloud.platforms

enum class PlatformLanguage(private var fileSuffix: String?) {

    JAVA("jar"),
    GO("exe"),
    RUST(null);

    fun suffix(): String {
        return if (fileSuffix != null) ".$fileSuffix" else ""
    }
}
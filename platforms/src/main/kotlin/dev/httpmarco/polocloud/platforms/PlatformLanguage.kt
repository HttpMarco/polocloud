package dev.httpmarco.polocloud.platforms

import dev.httpmarco.polocloud.common.os.currentOS


enum class PlatformLanguage(private var fileSuffix: String?, val nativeExecutable: Boolean) {

    JAVA("jar", false),
    GO(null, true),
    RUST(null, true);

    fun suffix(): String {
        val suffix = if (nativeExecutable) currentOS.nativeExecutableSuffix else fileSuffix

        return if (suffix != null) ".$suffix" else ""
    }
}
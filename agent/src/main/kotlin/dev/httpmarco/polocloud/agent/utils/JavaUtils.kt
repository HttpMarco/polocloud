package dev.httpmarco.polocloud.agent.utils

import java.io.File

class JavaUtils {

    fun isValidJavaPath(javaPath: String): Boolean {
        val javaExecutable = if (System.getProperty("os.name").startsWith("Windows")) {
            File(javaPath, "bin/java.exe")
        } else {
            File(javaPath, "bin/java")
        }

        return javaExecutable.exists() && javaExecutable.canExecute()
    }

}
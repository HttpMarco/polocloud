package dev.httpmarco.polocloud.platforms.tasks.actions

import dev.httpmarco.polocloud.common.os.OS
import dev.httpmarco.polocloud.common.os.currentOS
import dev.httpmarco.polocloud.platforms.PlatformParameters
import dev.httpmarco.polocloud.platforms.tasks.PlatformTaskStep
import java.nio.file.Path

class PlatformExecuteCommandAction(val command: String) : PlatformAction() {
    override fun run(
        file: Path,
        step: PlatformTaskStep,
        environment: PlatformParameters
    ) {
        val builder = ProcessBuilder()

        val prefix = if (currentOS == OS.WIN) arrayOf("cmd", "/c") else arrayOf("sh", "-c")

        builder.command(*prefix, environment.modifyValueWithEnvironment(command))
        builder.directory(file.toFile())
        val process = builder.start()

        process.waitFor()
    }
}
package dev.httpmarco.polocloud.agent.runtime.local.terminal

import dev.httpmarco.polocloud.agent.exitPolocloud
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.CommandService
import org.jline.jansi.Ansi
import org.jline.reader.LineReader
import org.jline.reader.UserInterruptException

class JLine3Reading(
    private var terminal: Jline3Terminal,
    private val lineReader: LineReader,
    private val commandService: CommandService
) : Thread() {

    override fun run() {
        this.terminal.resetPrompt()

        while (!isInterrupted) {
            try {
                val line = lineReader.readLine(this.terminal.prompt).trim()
                if (line.isBlank()) {
                    // we reset the terminal prompt as message -> we have a clean console
                    println(Ansi.ansi().cursorUpLine().eraseLine().toString() + Ansi.ansi().cursorUp(1).toString())
                    continue
                }

                val splat: Array<String> = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val commandName = splat[0]
                val args = splat.copyOfRange(1, splat.size)
                val screenService = terminal.screenService

                if (!screenService.isRecoding()) {
                    commandService.call(commandName, args)
                } else {
                    if (line == "exit") {
                        screenService.stopCurrentRecording()
                        continue
                    }
                    screenService.redirectCommand(line)
                }

            } catch (_: UserInterruptException) {
                // pressing Ctrl+C or similar to interrupt reading
                exitPolocloud(cleanShutdown = false)
                break
            } catch (e: Throwable) {
                logger.throwable(e)
            }
        }
    }
}
package dev.httpmarco.polocloud.agent.runtime.local.terminal

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.CommandService
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.impl.ClearCommand
import dev.httpmarco.polocloud.agent.runtime.local.terminal.screen.ServiceScreenController
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.impl.LineReaderImpl
import org.jline.terminal.TerminalBuilder
import org.jline.utils.InfoCmp
import java.nio.charset.StandardCharsets

class JLine3Terminal {

    val commandService = CommandService()
    val screenService = ServiceScreenController(this)

    var prompt: String? = null

    private val terminal = TerminalBuilder.builder()
        .system(true)
        .encoding(StandardCharsets.UTF_8)
        .dumb(true)
        .jansi(true)
        .build()
    private val lineReader: LineReaderImpl = LineReaderBuilder.builder()
        .terminal(this.terminal)
        .completer(JLine3Completer(commandService))
        .option(LineReader.Option.AUTO_MENU_LIST, true)
        .variable(LineReader.COMPLETION_STYLE_LIST_SELECTION, "fg:cyan")
        .variable(LineReader.COMPLETION_STYLE_LIST_BACKGROUND, "fg:default")
        .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
        .option(LineReader.Option.AUTO_PARAM_SLASH, false)
        .variable(LineReader.BELL_STYLE, "none")
        .build() as LineReaderImpl

    val jLine3Reading = JLine3Reading(this, this.lineReader, this.commandService)

    init {
        this.commandService.registerCommand(ClearCommand(this))
    }

    fun clearScreen() {
        println("\u001b[H\u001b[2J")
    }

    fun available(): Boolean {
        return this.terminal != null && this.lineReader != null
    }

    fun display(message: String) {
        this.terminal.puts(InfoCmp.Capability.carriage_return)
        this.terminal.writer().println(message)
        this.update()
    }

    fun update() {
        if (this.lineReader.isReading) {
            this.lineReader.callWidget(LineReader.REDRAW_LINE)
            this.lineReader.callWidget(LineReader.REDISPLAY)
        }
    }

    fun updatePrompt(prompt: String) {
        this.prompt = LoggingColor.translate(prompt)
        this.lineReader.setPrompt(this.prompt)
        this.update()
    }

    fun resetPrompt() {
        this.updatePrompt("&bpolocloud&8@&7" + Agent.instance.version() + " &8» &7")
    }

    fun shutdown() {
        this.terminal.close()
        this.jLine3Reading.interrupt()
    }
}
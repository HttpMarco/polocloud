package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.runtime.Runtime
import dev.httpmarco.polocloud.agent.runtime.RuntimeConfigHolder
import dev.httpmarco.polocloud.agent.runtime.local.terminal.JLine3Terminal
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.impl.GroupCommand
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.impl.PlatformCommand
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.impl.ServiceCommand
class LocalRuntime : Runtime {

    private val runtimeGroupStorage = LocalRuntimeGroupStorage()
    private val runtimeServiceStorage = LocalRuntimeServiceStorage()
    private val runtimeFactory = LocalRuntimeFactory(this)
    private val runtimeQueue = LocalRuntimeQueue()
    private val runtimeExpender = LocalRuntimeExpender()
    private val templates = LocalRuntimeTemplates()
    private val configHolder = LocalRuntimeConfigHolder()

    lateinit var terminal: JLine3Terminal

    override fun boot() {
        terminal = JLine3Terminal()

        this.terminal.jLine3Reading.start()
        this.runtimeQueue.start()

        terminal.commandService.registerCommand(GroupCommand(runtimeGroupStorage, terminal))
        terminal.commandService.registerCommand(ServiceCommand(runtimeServiceStorage, terminal))
        terminal.commandService.registerCommand(PlatformCommand())
    }

    override fun runnable(): Boolean {
        return true // LocalRuntime is always runnable
    }

    override fun serviceStorage() = runtimeServiceStorage

    override fun groupStorage() = runtimeGroupStorage

    override fun factory() = runtimeFactory

    override fun expender() = runtimeExpender

    override fun templates() = templates

    override fun configHolder() = configHolder

    override fun shutdown() {
        this.terminal.shutdown()

        this.runtimeQueue.interrupt()
    }
}
package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.runtime.RuntimeLoader

class LocalRuntimeLoader : RuntimeLoader {

    override fun runnable(): Boolean {
        return true
    }

    override fun instance(): LocalRuntime {
       return LocalRuntime()
    }
}
package dev.httpmarco.polocloud.modules.rest.config

interface Config {

    fun save(path: String) {
        ConfigProvider().write(path, this)
    }
}
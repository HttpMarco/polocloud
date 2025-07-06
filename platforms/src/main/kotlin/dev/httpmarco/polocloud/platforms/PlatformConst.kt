package dev.httpmarco.polocloud.platforms

import dev.httpmarco.polocloud.platforms.tasks.actions.PlatformAction
import dev.httpmarco.polocloud.platforms.tasks.actions.PlatformFilePropertyUpdateAction
import dev.httpmarco.polocloud.platforms.tasks.actions.PlatformFileReplacementAction
import dev.httpmarco.polocloud.platforms.tasks.actions.PlatformFileWriteAction
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.io.path.Path

val PLATFORM_PATH = Path("local/metadata")
const val PLATFORM_METADATA_URL = "https://raw.githubusercontent.com/HttpMarco/polocloud/refs/heads/master/metadata/"

val actionModule = SerializersModule {
    polymorphic(PlatformAction::class) {
        subclass(PlatformFileReplacementAction::class)
        subclass(PlatformFileWriteAction::class)
        subclass(PlatformFilePropertyUpdateAction::class)
    }
}

val JSON = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
    encodeDefaults = true
    classDiscriminator = "type"
    serializersModule = actionModule
}
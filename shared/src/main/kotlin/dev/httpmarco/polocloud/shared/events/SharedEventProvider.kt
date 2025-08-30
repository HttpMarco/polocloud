package dev.httpmarco.polocloud.shared.events

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.httpmarco.polocloud.shared.player.PlayerSerializer
import dev.httpmarco.polocloud.shared.player.PolocloudPlayer
import dev.httpmarco.polocloud.shared.service.Service
import dev.httpmarco.polocloud.shared.service.ServiceSerializer
import dev.httpmarco.polocloud.shared.template.Template
import dev.httpmarco.polocloud.shared.template.TemplateSerializer

abstract class SharedEventProvider {

    val gsonSerializer: Gson =
        GsonBuilder()
            .registerTypeHierarchyAdapter(Service::class.java, ServiceSerializer())
            .registerTypeHierarchyAdapter(Template::class.java, TemplateSerializer())
            .registerTypeHierarchyAdapter(PolocloudPlayer::class.java, PlayerSerializer())
            .create()

    abstract fun call(event: Event)

    abstract fun <T : Event> subscribe(eventType: Class<T>, result: (T) -> Any)
}
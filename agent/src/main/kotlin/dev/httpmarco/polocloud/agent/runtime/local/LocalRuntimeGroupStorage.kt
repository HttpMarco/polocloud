package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.groups.Group
import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.RuntimeGroupStorage
import dev.httpmarco.polocloud.common.json.PRETTY_JSON
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.listDirectoryEntries

private val STORAGE_PATH = Path("local/groups")

class LocalRuntimeGroupStorage : RuntimeGroupStorage {

    private lateinit var cachedGroups: ArrayList<Group>

    init {
        this.initialize()
    }

    private fun initialize() {
        STORAGE_PATH.createDirectories()

        // load all groups from the storage path
        cachedGroups = ArrayList(STORAGE_PATH.listDirectoryEntries("*.json").stream().map {
            return@map Group(Json.decodeFromString(Files.readString(it)))
        }.toList())
    }

    /**
     * Return all cached Items
     */
    override fun items(): List<Group> {
        return this.cachedGroups
    }

    /**
     * Return the cached Item with the given identifier
     */
    override fun item(identifier: String): Group? {
        return this.cachedGroups.stream().filter { it.data.name == identifier }.findFirst().orElse(null)
    }

    override fun publish(group: Group) {
        Files.writeString(groupPath(group), PRETTY_JSON.encodeToString(group.data))
        this.cachedGroups.add(group)
    }

    override fun destroy(group: Group) {
        this.cachedGroups.remove(group)
        this.groupPath(group).deleteIfExists()
    }

    override fun present(identifier: String): Boolean {
        return this.cachedGroups.any { it.data.name == identifier }
    }

    override fun update(group: Group) {
        // overwrite the existing group file with the new data
        Files.writeString(groupPath(group), PRETTY_JSON.encodeToString(group.data))
    }

    override fun reload() {
        i18n.info("agent.local-runtime.group-storage.reload")
        this.initialize()
        i18n.info("agent.local-runtime.group-storage.collect", this.cachedGroups.size)
    }

    private fun groupPath(group: Group): Path {
        return STORAGE_PATH.resolve(group.data.name + ".json")
    }
}
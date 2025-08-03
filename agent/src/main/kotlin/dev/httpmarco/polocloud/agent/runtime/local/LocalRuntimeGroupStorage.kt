package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.runtime.RuntimeGroupStorage
import dev.httpmarco.polocloud.common.json.GSON
import dev.httpmarco.polocloud.common.json.PRETTY_GSON
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.listDirectoryEntries

private val STORAGE_PATH = Path("local/groups")

class LocalRuntimeGroupStorage : RuntimeGroupStorage {

    private lateinit var cachedAbstractGroups: ArrayList<AbstractGroup>

    init {
        this.initialize()
    }

    private fun initialize() {
        STORAGE_PATH.createDirectories()

        // load all groups from the storage path
        cachedAbstractGroups = ArrayList(STORAGE_PATH.listDirectoryEntries("*.json").stream().map {
            return@map GSON.fromJson(Files.readString(it), AbstractGroup::class.java)
        }.toList())
    }

    override fun publish(abstractGroup: AbstractGroup) {
        Files.writeString(groupPath(abstractGroup), PRETTY_GSON.toJson(abstractGroup))
        this.cachedAbstractGroups.add(abstractGroup)
    }

    override fun destroy(abstractGroup: AbstractGroup) {
        this.cachedAbstractGroups.remove(abstractGroup)
        this.groupPath(abstractGroup).deleteIfExists()
    }

    override fun update(group: AbstractGroup) {
        // overwrite the existing group file with the new data
        Files.writeString(groupPath(group), PRETTY_GSON.toJson(group))
    }

    override fun reload() {
        i18n.info("agent.local-runtime.group-storage.reload")
        this.initialize()
        i18n.info("agent.local-runtime.group-storage.collect", this.cachedAbstractGroups.size)
    }

    private fun groupPath(abstractGroup: AbstractGroup): Path {
        return STORAGE_PATH.resolve(abstractGroup.name + ".json")
    }

    override fun findAll(): List<AbstractGroup> {
        return this.cachedAbstractGroups
    }

    override fun findAllAsync() = CompletableFuture.completedFuture<List<AbstractGroup>>(findAll())

    override fun find(name: String): AbstractGroup? {
        return this.cachedAbstractGroups.stream().filter { it.name == name }.findFirst().orElse(null)
    }

    override fun findAsync(name: String) = CompletableFuture.completedFuture<AbstractGroup?>(find(name))
}
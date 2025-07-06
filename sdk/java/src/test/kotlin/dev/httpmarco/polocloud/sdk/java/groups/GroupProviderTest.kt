package dev.httpmarco.polocloud.sdk.java.groups

import dev.httpmarco.polocloud.sdk.java.Polocloud
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test

class GroupProviderTest {

    @Test
    @DisplayName("findGroups should return all groups")
    fun findGroups() {
        val result = Polocloud.instance().groupProvider().find()

        assert(result.isNotEmpty())

        println("Found groups: ${result.joinToString(", ") { it.name() }}")
    }

    @Test
    @DisplayName("findGroupByName should return a group by its name")
    fun findGroupByName() {
        val result = Polocloud.instance().groupProvider().find("lobby")

        assert(result != null)

        println("Found group: ${result!!.name()}")

    }
}
package dev.httpmarco.polocloud.sdk.java.groups

import dev.httpmarco.polocloud.sdk.java.Polocloud
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test

class GroupProviderTest {

    @Test
    @DisplayName("findGroups should return all groups")
    fun findGroups() {
        val groupProvider = Polocloud.instance().groupProvider()


    }

    @Test
    @DisplayName("findGroupByName should return a group by its name")
    fun findGroupByName() {
        val groupProvider = Polocloud.instance().groupProvider()

    }
}
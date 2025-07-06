package dev.httpmarco.polocloud.platforms

import dev.httpmarco.polocloud.platforms.metadata.MetadataReader
import dev.httpmarco.polocloud.platforms.metadata.MetadataTranslator
import dev.httpmarco.polocloud.platforms.tasks.PlatformTaskPool
import org.junit.jupiter.api.Test

class PlatformTest {

    @Test
    fun loadPool() {
        PlatformPool


        assert(PlatformTaskPool.size() != 0)
    }

}
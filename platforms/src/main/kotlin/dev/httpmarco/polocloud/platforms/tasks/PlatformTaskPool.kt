package dev.httpmarco.polocloud.platforms.tasks

object PlatformTaskPool {

    private val tasks = ArrayList<PlatformTask>()

    fun attach(task: PlatformTask) {
        tasks.add(task)
    }

    fun find(id: String): PlatformTask? {
        return tasks.firstOrNull { it.name == id }
    }

    fun size(): Int {
        return tasks.size
    }
}
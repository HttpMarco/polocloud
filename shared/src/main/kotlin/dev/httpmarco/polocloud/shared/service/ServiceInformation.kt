package dev.httpmarco.polocloud.shared.service

import com.google.gson.Gson
import dev.httpmarco.polocloud.v1.services.ServiceInformationSnapshot

open class ServiceInformation(val createdAt: Long) {

    companion object {
        private val gson = Gson()
        fun bindSnapshot(snapshot: ServiceInformationSnapshot): ServiceInformation{
            return ServiceInformation(
                snapshot.createdAt
            )
        }

        fun bindString(string: String): ServiceInformation {
            return gson.fromJson(string, ServiceInformation::class.java)
        }
    }

    fun toSnapshot(): ServiceInformationSnapshot {
        return ServiceInformationSnapshot.newBuilder()
            .setCreatedAt(this.createdAt)
            .build()
    }

    override fun toString(): String {
        return gson.toJson(this)
    }

}
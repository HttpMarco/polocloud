package dev.httpmarco.polocloud.agent.information

import dev.httpmarco.polocloud.shared.information.CloudInformation
import dev.httpmarco.polocloud.shared.information.SharedCloudInformationProvider

interface CloudInformationStorage : SharedCloudInformationProvider<CloudInformation> {

    fun addCloudInformation(cloudInformation: CloudInformation)

    fun removeCloudInformation(cloudInformation: CloudInformation)

    fun saveCurrentCloudInformation()

}
package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.polocloud.api.protocoll.CloudResultInfo;

public interface ClusterGroupBuilder {

    /**
     * Set min memory property of a service of this group
     * @param memory the minimum memory amount
     * @return the current group builder
     */
    ClusterGroupBuilder withMinMemory(int memory);

    /**
     * Set max memory property of a service of this group
     * @param memory the maximum memory amount
     * @return the current group builder
     */
    ClusterGroupBuilder withMaxMemory(int memory);

    /**
     * Delete an existing group
     * @param group id
     * Return the result of delete processing
     */
    CloudResultInfo delete(String group);

    /**
     * Finally create the group with the given properties
     * @return the new group
     */
    ClusterGroup create();

}

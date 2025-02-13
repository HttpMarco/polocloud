package dev.httpmarco.polocloud.api.groups;

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
     * Finally create the group with the given properties
     * @return the new group
     */
    ClusterGroup create();

}

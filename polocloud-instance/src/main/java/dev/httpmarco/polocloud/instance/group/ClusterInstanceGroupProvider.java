package dev.httpmarco.polocloud.instance.group;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.ClusterGroupBuilder;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.utils.Future;
import dev.httpmarco.polocloud.explanation.group.ClusterGroupServiceGrpc;
import dev.httpmarco.polocloud.explanation.group.ClusterGroupServiceOuterClass;
import io.grpc.Channel;

import java.util.Collection;

public class ClusterInstanceGroupProvider implements ClusterGroupProvider {

    private final ClusterGroupServiceGrpc.ClusterGroupServiceBlockingStub stub;

    public ClusterInstanceGroupProvider(Channel channel) {
        this.stub = ClusterGroupServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public Future<Collection<ClusterGroup>> findAllAsync() {
        return null;
    }

    @Override
    public Future<ClusterGroup> findAsync(String groupId) {
        var response = stub.findGroup(ClusterGroupServiceOuterClass.GroupFindRequest.newBuilder().setName(groupId).build());
        return Future.completedFuture(new ClusterInstanceGroup(
                response.getName(),
                response.getMinMemory(),
                response.getMaxMemory(),
                response.getMinOnlineServices(),
                response.getMaxOnlineServices(),
                response.getPercentageToStartNewService(),
                response.getTemplatesList().stream().toList()));
    }

    @Override
    public void delete(String group) {

    }

    @Override
    public ClusterGroupBuilder create(String group) {
        return null;
    }
}

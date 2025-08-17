package dev.httpmarco.polocloud.sdk.java.stats;

import dev.httpmarco.polocloud.shared.stats.SharedStatsProvider;
import dev.httpmarco.polocloud.shared.stats.Stats;
import dev.httpmarco.polocloud.v1.groups.GetStatsRequest;
import dev.httpmarco.polocloud.v1.groups.StatsControllerGrpc;
import io.grpc.ManagedChannel;
import org.jetbrains.annotations.NotNull;

public final class StatsProvider implements SharedStatsProvider<Stats> {

    private final StatsControllerGrpc.StatsControllerFutureStub futureStub;
    private final StatsControllerGrpc.StatsControllerBlockingStub blockingStub;

    public StatsProvider(ManagedChannel channel) {
        this.blockingStub = StatsControllerGrpc.newBlockingStub(channel);
        this.futureStub = StatsControllerGrpc.newFutureStub(channel);
    }

    @Override
    public @NotNull Stats get() {
        return Stats.Companion.bindSnapshot(blockingStub.get(GetStatsRequest.getDefaultInstance()).getStats());
    }
}

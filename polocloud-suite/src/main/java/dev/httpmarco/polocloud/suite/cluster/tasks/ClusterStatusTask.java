package dev.httpmarco.polocloud.suite.cluster.tasks;

import dev.httpmarco.polocloud.grpc.ClusterService;
import dev.httpmarco.polocloud.suite.cluster.global.GlobalCluster;
import dev.httpmarco.polocloud.suite.utils.tasks.TickTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClusterStatusTask extends TickTask {

    private static final Logger log = LogManager.getLogger(ClusterStatusTask.class);
    private final GlobalCluster cluster;

    public ClusterStatusTask(GlobalCluster cluster) {
        // every 5 seconds
        super(5);

        this.cluster = cluster;
    }

    @Override
    public void start() {
        this.requestState();
        super.start();
    }

    @Override
    public void onTick() {
        this.requestState();
    }

    public void requestState() {
        for (var suite : this.cluster.suites()) {

            var currentState = suite.state();
            var state = suite.available() ? suite.clusterStub().requestState(ClusterService.EmptyCall.newBuilder().build()).getState() : ClusterService.State.OFFLINE;

            if (state != currentState) {
                // we must update the new state

                if ((currentState == ClusterService.State.OFFLINE || currentState == ClusterService.State.INITIALIZING) && state == ClusterService.State.AVAILABLE) {
                    log.info("The suite {} is now online and bound to the cluster!", suite.id());
                }

                if((currentState == ClusterService.State.AVAILABLE || currentState == ClusterService.State.INITIALIZING) && state == ClusterService.State.OFFLINE) {
                    log.info("The suite {} is now offline and unbound from the cluster!", suite.id());
                }

                suite.state(state);
            }
        }
    }
}

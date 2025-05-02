package dev.httpmarco.polocloud.suite.groups.setup;

import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.api.platform.PlatformType;
import dev.httpmarco.polocloud.api.platform.SharedPlatform;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.groups.ClusterGroupImpl;
import dev.httpmarco.polocloud.suite.terminal.setup.Setup;

import java.util.Map;

public final class GroupSetup extends Setup {


    public GroupSetup() {
        super("Create your Group");

        question("name", "What is the name about the group?");
        question("minMemory", "What is the minimum memory available for this group's service? (mb)");
        question("maxMemory", "What is the maximum memory available for this group's service? (mb)");
        question("minOnlineService", "How many services must be minimal online?");
        question("maxOnlineService", "What is the maximum online services amount?");
    }

    @Override
    public void complete(Map<String, String> context) {
        // todo check if local cluster -> no running suite -> else set running node
        ClusterGroupImpl clusterGroup = new ClusterGroupImpl(
                context.get("name"),
                new SharedPlatform("velocity", Version.parse("3.4.0-SNAPSHOT"), PlatformType.PROXY),
                Integer.parseInt(context.get("minMemory")),
                Integer.parseInt(context.get("maxMemory")),
                Integer.parseInt(context.get("minOnlineService")),
                Integer.parseInt(context.get("maxOnlineService")),
                80.0
        );

        PolocloudSuite.instance().groupProvider().registerGroup(clusterGroup);
    }
}

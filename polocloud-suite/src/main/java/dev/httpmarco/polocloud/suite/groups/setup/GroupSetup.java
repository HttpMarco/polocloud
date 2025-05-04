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

        var translation = PolocloudSuite.instance().translation();

        question("name", translation.get("suite.setup.group.name"));
        question("minMemory", translation.get("suite.setup.group.minMemory"));
        question("maxMemory", translation.get("suite.setup.group.maxMemory"));
        question("minOnlineService", translation.get("suite.setup.group.minOnline"));
        question("maxOnlineService", translation.get("suite.setup.group.maxOnline"));
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

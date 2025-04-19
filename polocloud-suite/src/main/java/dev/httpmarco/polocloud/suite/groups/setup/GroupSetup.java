package dev.httpmarco.polocloud.suite.groups.setup;

import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.api.platform.PlatformType;
import dev.httpmarco.polocloud.api.platform.SharedPlatform;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.groups.ClusterGroupImpl;
import dev.httpmarco.polocloud.suite.terminal.setup.Setup;

import java.util.Map;

public class GroupSetup extends Setup {


    public GroupSetup() {
        super("Create your Group");
    }

    @Override
    public void complete(Map<String, String> context) {

        // todo check if local cluster -> no running suite -> else set running node
        ClusterGroupImpl clusterGroup = new ClusterGroupImpl(
                "lobby",
                new SharedPlatform("velocity", Version.parse("3.4.0-SNAPSHOT"), PlatformType.PROXY),
                512,
                1024,
                1,
                1,
                80.0
        );

        PolocloudSuite.instance().groupProvider().registerGroup(clusterGroup);
    }
}

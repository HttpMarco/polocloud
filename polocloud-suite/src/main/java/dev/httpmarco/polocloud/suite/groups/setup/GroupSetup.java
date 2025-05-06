package dev.httpmarco.polocloud.suite.groups.setup;

import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.api.platform.SharedPlatform;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.groups.ClusterGroupImpl;
import dev.httpmarco.polocloud.suite.platforms.Platform;
import dev.httpmarco.polocloud.suite.platforms.PlatformVersion;
import dev.httpmarco.polocloud.suite.terminal.setup.Setup;

import java.util.Map;

public final class GroupSetup extends Setup {


    public GroupSetup() {
        super("Create your Group");

        var translation = PolocloudSuite.instance().translation();

        question("name", translation.get("suite.setup.group.name"));

        question("platform", translation.get("suite.setup.group.platform"),
                cache -> PolocloudSuite.instance().platformProvider().platforms().stream().map(Platform::name).toList(),
                it -> {

                    var result = PolocloudSuite.instance().platformProvider().findPlatform(it.left());

                    if (result == null) {
                        return false;
                    }

                    // only if a valid platform is found, we can run the next question
                    question("platformVersion", translation.get("suite.setup.group.platformVersion"),
                            cache -> result.versions().stream().map(PlatformVersion::version).toList(),
                            cache -> result.versions().stream().anyMatch(pv -> pv.version().equals(cache.left())));

                    question("minMemory", translation.get("suite.setup.group.minMemory"));
                    question("maxMemory", translation.get("suite.setup.group.maxMemory"));
                    question("minOnlineService", translation.get("suite.setup.group.minOnline"));
                    question("maxOnlineService", translation.get("suite.setup.group.maxOnline"));

                    return true;
                });
    }

    @Override
    public void complete(Map<String, String> context) {
        // todo check if local cluster -> no running suite -> else set running node

        var platform = PolocloudSuite.instance().platformProvider().findPlatform(context.get("platform"));
        var platformVersion = platform.versions().stream().filter(it -> it.version().equals(context.get("platformVersion"))).findFirst().orElseThrow();

        ClusterGroupImpl clusterGroup = new ClusterGroupImpl(
                context.get("name"),
                new SharedPlatform(context.get("platform"), Version.parse(platformVersion.version()), platform.type()),
                Integer.parseInt(context.get("minMemory")),
                Integer.parseInt(context.get("maxMemory")),
                Integer.parseInt(context.get("minOnlineService")),
                Integer.parseInt(context.get("maxOnlineService")),
                80.0
        );
        PolocloudSuite.instance().groupProvider().registerGroup(clusterGroup);
    }
}
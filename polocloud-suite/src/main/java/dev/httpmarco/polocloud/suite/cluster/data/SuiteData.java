package dev.httpmarco.polocloud.suite.cluster.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class SuiteData {

    private final String id;
    private final String host;
    private final int port;

}

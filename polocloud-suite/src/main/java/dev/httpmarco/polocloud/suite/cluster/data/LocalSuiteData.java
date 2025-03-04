package dev.httpmarco.polocloud.suite.cluster.data;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public class LocalSuiteData extends SuiteData {

    private final String privateKey;

    public LocalSuiteData(String id, String host, int port) {
        super(id, host, port);

        this.privateKey = UUID.randomUUID().toString().substring(0, 8);
    }
}

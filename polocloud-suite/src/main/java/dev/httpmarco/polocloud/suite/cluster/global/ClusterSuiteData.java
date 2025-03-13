package dev.httpmarco.polocloud.suite.cluster.global;

public record ClusterSuiteData(String id, String hostname, String privateKey, int port) {

    public String address() {
        return hostname() + "@" + port();
    }

}

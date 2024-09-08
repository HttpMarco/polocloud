package dev.httpmarco.polocloud.plugin;

import java.util.List;

public interface ProxyServerHandler {

    void registerServer(String name, String hostname, int port);

    void unregisterServer(String name);

    List<String> services();

}

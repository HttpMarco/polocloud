package dev.httpmarco.polocloud.plugin.waterdog;

import dev.httpmarco.polocloud.plugin.ProxyServerHandler;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.network.serverinfo.BedrockServerInfo;
import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import lombok.AllArgsConstructor;

import java.net.InetSocketAddress;
import java.util.List;

@AllArgsConstructor
public final class WaterdogServerHandler implements ProxyServerHandler {

    private ProxyServer server;

    @Override
    public void registerServer(String name, String hostname, int port) {
        server.registerServerInfo(new BedrockServerInfo(name, new InetSocketAddress(hostname, port), new InetSocketAddress(hostname, port)));
    }

    @Override
    public void unregisterServer(String name) {
        server.removeServerInfo(name);
    }

    @Override
    public List<String> services() {
        return server.getServers().stream().map(ServerInfo::getServerName).toList();
    }
}

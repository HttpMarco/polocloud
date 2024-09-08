package dev.httpmarco.polocloud.plugin.bungeecord;

import dev.httpmarco.polocloud.plugin.ProxyServerHandler;
import net.md_5.bungee.api.ProxyServer;

import java.net.InetSocketAddress;
import java.util.List;

public class BungeeCordPlatformServerHandler implements ProxyServerHandler {

    @Override
    public void registerServer(String name, String hostname, int port) {
        ProxyServer.getInstance().getServers().put(name, ProxyServer.getInstance().constructServerInfo(name, new InetSocketAddress(hostname, port), "PoloCloud Service", false));
    }

    @Override
    public void unregisterServer(String name) {
        ProxyServer.getInstance().getServers().remove(name);
    }

    @Override
    public List<String> services() {
        return ProxyServer.getInstance().getServers().keySet().stream().toList();
    }
}

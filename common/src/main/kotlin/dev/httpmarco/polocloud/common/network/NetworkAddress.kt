package dev.httpmarco.polocloud.common.network

import java.net.Inet4Address
import java.net.NetworkInterface
import kotlin.collections.iterator

fun localAddress(): String {
    val interfaces = NetworkInterface.getNetworkInterfaces()
    for (iface in interfaces) {
        if (!iface.isLoopback && iface.isUp) {
            for (addr in iface.inetAddresses) {
                if (addr is Inet4Address && !addr.isLoopbackAddress) {
                    return addr.hostAddress
                }
            }
        }
    }
    return "null"
}
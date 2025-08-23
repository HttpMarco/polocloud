package dev.httpmarco.polocloud.agent.detector

import com.google.gson.JsonObject
import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.common.json.GSON
import dev.httpmarco.polocloud.shared.events.definitions.ServiceOnlineEvent
import dev.httpmarco.polocloud.shared.service.Service
import dev.httpmarco.polocloud.v1.services.ServiceState
import java.io.ByteArrayOutputStream
import java.io.EOFException
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.charset.StandardCharsets

class OnlineStateDetector : Detector {

    override fun tick() {
        val services = Agent.runtime.serviceStorage().findAll()

        services.forEach { service ->
            val host = "127.0.0.1"
            val port = service.port

            if (service.isBedrockLike()) {
                val result = pingBedrock(host, port)
                if (result != null) {
                    val (motd, online, max) = result
                    service.updateMotd(motd ?: "Unknown")
                    service.updatePlayerCount(online)
                    service.updateMaxPlayerCount(max)
                    callOnline(service)
                }
                return@forEach
            }

            try {
                Socket().use { socket ->
                    socket.connect(InetSocketAddress(host, port), 500)

                    val out = socket.getOutputStream()
                    val input = socket.getInputStream()

                    val handshake = ByteArrayOutputStream().apply {
                        write(0x00) // Packet ID
                        write(0x00) // Protocol version
                        writeVarInt(this, host.length)
                        write(host.toByteArray(StandardCharsets.UTF_8))
                        write((port shr 8) and 0xFF)
                        write(port and 0xFF)
                        write(0x01) // Next state: status
                    }

                    writeVarInt(out, handshake.size())
                    out.write(handshake.toByteArray())
                    out.write(0x01)
                    out.write(0x00)

                    try {
                        readVarInt(input) // packet length
                        readVarInt(input) // packet ID
                    } catch (_: Throwable) {
                        // if the packet length or ID cannot be read, the service is not online
                        return@forEach
                    }


                    val jsonLength = readVarInt(input)
                    val jsonData = ByteArray(jsonLength)
                    input.readFully(jsonData)

                    val json: JsonObject = GSON.fromJson(String(jsonData), JsonObject::class.java)
                    val description = json["description"]

                    val motd = when {
                        description?.isJsonPrimitive == true -> description.asString
                        description?.isJsonObject == true -> {
                            val descriptionObj = description.asJsonObject
                            if (descriptionObj.has("extra")) {
                                descriptionObj["extra"].asJsonArray.joinToString("") {
                                    it.asJsonObject["text"]?.asString ?: ""
                                }
                            } else {
                                descriptionObj["text"]?.asString
                            }
                        }
                        else -> null
                    }

                    val players = json["players"]?.asJsonObject
                    val playerCount = players?.get("online")?.asJsonPrimitive?.asInt ?: -1

                    service.updateMotd(motd!!)
                    service.updatePlayerCount(playerCount)
                    service.updateMaxPlayerCount(players?.get("max")?.asJsonPrimitive?.asInt ?: -1)

                    this.callOnline(service)
                }
            } catch (_: Throwable) {
                // ignore connection errors, the service is not online yet
            }
        }
    }

    private fun pingBedrock(host: String, port: Int): Triple<String?, Int, Int>? {
        return try {
            DatagramSocket().use { socket ->
                socket.soTimeout = 2000
                val address = InetAddress.getByName(host)

                val timestamp = System.currentTimeMillis()
                val magic = byteArrayOf(
                    0x00, 0xff.toByte(), 0xff.toByte(), 0x00,
                    0xfe.toByte(), 0xfe.toByte(), 0xfe.toByte(), 0xfe.toByte(),
                    0xfd.toByte(), 0xfd.toByte(), 0xfd.toByte(), 0xfd.toByte(),
                    0x12, 0x34, 0x56, 0x78
                )

                val packetData = ByteArrayOutputStream().apply {
                    write(0x01) // Unconnected Ping
                    writeLong(timestamp)
                    write(magic)
                    writeLong(2) // client GUID
                }.toByteArray()

                val request = DatagramPacket(packetData, packetData.size, address, port)
                socket.send(request)

                val buffer = ByteArray(4096)
                val response = DatagramPacket(buffer, buffer.size)
                socket.receive(response)

                val data = response.data
                if (data[0] != 0x1C.toByte()) {
                    return null
                }

                val strLen = ((data[33].toInt() and 0xFF) shl 8) or (data[34].toInt() and 0xFF)
                val serverIdStr = String(data.copyOfRange(35, 35 + strLen), StandardCharsets.UTF_8)

                val parts = serverIdStr.split(";")
                if (parts.size < 6) {
                    return null
                }

                val motd = parts[1]
                val online = parts[4].toIntOrNull() ?: -1
                val max = parts[5].toIntOrNull() ?: -1

                Triple(motd, online, max)
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun ByteArrayOutputStream.writeLong(value: Long) {
        for (i in 7 downTo 0) {
            this.write(((value ushr (i * 8)) and 0xFF).toInt())
        }
    }

    private fun Service.isBedrockLike(): Boolean {
        val find = Agent.groupProvider().find(this.groupName) ?: return false
        val platform = find.platform.name.lowercase()
        return platform.contains("nukkit") || platform.contains("waterdog")
    }

    private fun callOnline(service: Service) {
        if (service.state == ServiceState.STARTING) {
            service.state = ServiceState.ONLINE
            Agent.eventService.call(ServiceOnlineEvent(service))
            i18n.info("agent.detector.service.online", service.name())
        }
    }

    override fun cycleLife(): Long {
        return 2000
    }

    fun writeVarInt(out: OutputStream, value: Int) {
        var v = value
        while ((v and -128) != 0) {
            out.write((v and 127) or 128)
            v = v ushr 7
        }
        out.write(v)
    }

    fun readVarInt(input: InputStream): Int {
        var numRead = 0
        var result = 0
        var read: Int
        do {
            read = input.read()
            if (read == -1) throw EOFException("End of stream")
            val value = read and 0b01111111
            result = result or (value shl (7 * numRead))
            numRead++
            if (numRead > 5) throw IOException("VarInt zu lang")
        } while ((read and 0b10000000) != 0)
        return result
    }

    fun InputStream.readFully(buffer: ByteArray) {
        var bytesRead = 0
        while (bytesRead < buffer.size) {
            val result = read(buffer, bytesRead, buffer.size - bytesRead)
            if (result == -1) throw EOFException("Not enough bytes")
            bytesRead += result
        }
    }
}
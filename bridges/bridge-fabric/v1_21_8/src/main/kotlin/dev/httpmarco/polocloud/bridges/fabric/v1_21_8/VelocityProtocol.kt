package dev.httpmarco.polocloud.bridges.fabric.v1_21_8

import com.google.common.net.InetAddresses
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import io.netty.buffer.Unpooled
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import java.net.InetAddress
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.security.MessageDigest
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.Arrays

object VelocityProtocol {

    @JvmField
    val PLAYER_INFO_CHANNEL: Identifier = Identifier.of("velocity", "player_info")

    const val FORWARDING_V1 = 1
    const val LAZY_SESSION = 4

    @JvmField
    val PLAYER_INFO_PACKET: PacketByteBuf = PacketByteBuf(
        Unpooled.wrappedBuffer(byteArrayOf(LAZY_SESSION.toByte())).asReadOnly()
    )

    private val SUPPORTED_VERSIONS = intArrayOf(FORWARDING_V1, LAZY_SESSION)

    /**
     * Verifies the integrity of the packet using HMAC-SHA256 with the provided secret.
     * Returns true if signature matches, false otherwise.
     */
    fun verifyPacketIntegrity(buf: PacketByteBuf, secret: String): Boolean {
        val signature = ByteArray(32).also { buf.readBytes(it) }
        val data = ByteArray(buf.readableBytes()).also { buf.getBytes(buf.readerIndex(), it) }

        return try {
            val mac = Mac.getInstance("HmacSHA256").apply {
                init(SecretKeySpec(secret.toByteArray(Charsets.UTF_8), "HmacSHA256"))
            }
            val calculatedSignature = mac.doFinal(data)
            MessageDigest.isEqual(signature, calculatedSignature)
        } catch (e: InvalidKeyException) {
            throw AssertionError("Invalid key for HMAC-SHA256", e)
        } catch (e: NoSuchAlgorithmException) {
            throw AssertionError("HMAC-SHA256 algorithm not found", e)
        }
    }

    /**
     * Reads and validates the forwarding version from the buffer.
     * Throws IllegalStateException if unsupported.
     */
    fun validateForwardingVersion(buf: PacketByteBuf) {
        val version = buf.readVarInt()
        if (Arrays.binarySearch(SUPPORTED_VERSIONS, version) < 0) {
            throw IllegalStateException(
                "Unsupported forwarding version $version, supported versions: ${SUPPORTED_VERSIONS.contentToString()}"
            )
        }
    }

    /**
     * Reads an InetAddress from the buffer.
     */
    fun readInetAddress(buf: PacketByteBuf): InetAddress =
        InetAddresses.forString(buf.readString(Short.MAX_VALUE.toInt()))

    /**
     * Reads a GameProfile including all properties from the buffer.
     */
    fun readGameProfile(buf: PacketByteBuf): GameProfile {
        val uuid = buf.readUuid()
        val name = buf.readString(16)
        val profile = GameProfile(uuid, name)
        readProperties(buf, profile)
        return profile
    }

    private fun readProperties(buf: PacketByteBuf, profile: GameProfile) {
        val count = buf.readVarInt()
        repeat(count) {
            val name = buf.readString(Short.MAX_VALUE.toInt())
            val value = buf.readString(Short.MAX_VALUE.toInt())
            val signature = if (buf.readBoolean()) buf.readString(Short.MAX_VALUE.toInt()) else null
            profile.properties.put(name, Property(name, value, signature))
        }
    }
}
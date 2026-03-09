package io.escaper.escaperapp.data.networking

import kotlin.random.Random
import okio.Buffer

internal fun generateFakeTlsClientHello(
    sni: String = "www.google.com"
): ByteArray {

    val sniBytes = sni.encodeToByteArray()

    // ---- Build SNI extension ----
    val sniExtension = Buffer().apply {
        // Extension type: server_name (0x0000)
        writeShort(0x0000)

        // Extension data length
        writeShort(5 + sniBytes.size)

        // Server Name List Length
        writeShort(3 + sniBytes.size)

        // Name type: host_name (0)
        writeByte(0x00)

        // Host length
        writeShort(sniBytes.size)

        // Host bytes
        write(sniBytes)
    }

    // ---- Build ClientHello body ----
    val clientHelloBody = Buffer().apply {

        // TLS version 1.2 (0x0303)
        writeShort(0x0303)

        // Random (32 bytes)
        repeat(32) {
            writeByte(Random.nextInt(0, 256))
        }

        // Session ID length: 0
        writeByte(0x00)

        // Cipher suites
        writeShort(4)              // length: 2 suites (4 bytes)
        writeShort(0x1301)         // TLS_AES_128_GCM_SHA256
        writeShort(0x1302)         // TLS_AES_256_GCM_SHA384

        // Compression methods
        writeByte(0x01)            // 1 method
        writeByte(0x00)            // null

        // Extensions length
        writeShort(sniExtension.size.toInt())

        // Extensions data
        writeAll(sniExtension)
    }

    // ---- Handshake layer ----
    val handshake = Buffer().apply {

        writeByte(0x01) // Handshake type: ClientHello

        // 3-byte length (uint24)
        val bodyLength = clientHelloBody.size.toInt()
        writeByte((bodyLength shr 16) and 0xFF)
        writeByte((bodyLength shr 8) and 0xFF)
        writeByte(bodyLength and 0xFF)

        writeAll(clientHelloBody)
    }

    // ---- TLS record layer ----
    val record = Buffer().apply {

        writeByte(0x16)            // ContentType: Handshake
        writeShort(0x0301)         // TLS 1.0 record version
        writeShort(handshake.size.toInt())

        writeAll(handshake)
    }

    return record.readByteArray()
}

package io.escaper.escaperapp.data.networking

import kotlin.random.Random
import okio.Buffer

internal fun generateFakeQuicInitial(): ByteArray {
    val buffer = Buffer()

    // Flags: Long Header, Initial packet type (0xC3)
    buffer.writeByte(0xC3)

    // Version: QUIC v1 (0x00000001)
    buffer.writeInt(0x00000001)

    // DCID length (8)
    buffer.writeByte(0x08)

    // DCID (8 random bytes)
    repeat(8) {
        buffer.writeByte(Random.nextInt(0, 256))
    }

    // SCID length (0)
    buffer.writeByte(0x00)

    // Token length (0)
    buffer.writeByte(0x00)

    // Compute remaining length:
    // We want total packet size = 256 bytes
    val headerSizeSoFar = buffer.size
    val remaining = 256 - headerSizeSoFar - 2 // minus 2 for length field

    // Length field (2 bytes, high bit set like original 0x4000 | remaining)
    buffer.writeShort(0x4000 or remaining.toInt())

    // Packet number (4 bytes)
    buffer.writeInt(0x00000001)

    // Fill rest with random payload
    while (buffer.size < 256) {
        buffer.writeByte(Random.nextInt(0, 256))
    }

    return buffer.readByteArray()
}

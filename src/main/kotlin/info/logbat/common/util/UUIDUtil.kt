package info.logbat.common.util

import java.nio.ByteBuffer
import java.util.*

object UUIDUtil {
    private val UUID_BYTE_LENGTH = 16
    fun uuidStringToBytes(uuidStr: String):ByteArray{
        val uuid = UUID.fromString(uuidStr)
        val byteBuffer = ByteBuffer.wrap(ByteArray(UUID_BYTE_LENGTH))
        byteBuffer.putLong(uuid.mostSignificantBits)
        byteBuffer.putLong(uuid.leastSignificantBits)
        return byteBuffer.array();
    }
    fun  bytesToUuidString(bytes: ByteArray) :String{
        val byteBuffer = ByteBuffer.wrap(bytes);
        return UUID(byteBuffer.getLong(), byteBuffer.getLong()).toString()
    }
}

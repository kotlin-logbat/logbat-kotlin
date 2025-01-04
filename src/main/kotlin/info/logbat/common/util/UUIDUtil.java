package info.logbat.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UUIDUtil {

  private static final int UUID_BYTE_LENGTH = 16;

  public static byte[] uuidStringToBytes(String uuidStr) {
    UUID uuid = UUID.fromString(uuidStr);
    ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[UUID_BYTE_LENGTH]);
    byteBuffer.putLong(uuid.getMostSignificantBits());
    byteBuffer.putLong(uuid.getLeastSignificantBits());
    return byteBuffer.array();
  }

  public static String bytesToUuidString(byte[] bytes) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
    long high = byteBuffer.getLong();
    long low = byteBuffer.getLong();
    UUID uuid = new UUID(high, low);
    return uuid.toString();
  }
}

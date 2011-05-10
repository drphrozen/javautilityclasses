package dk.iha.and.message;

import java.nio.ByteBuffer;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class Configuration {

  public static byte[] createUpdatedDatetimeConfiguration() {
    return createConfiguration(null, true);
  }

  public static byte[] createConfiguration(String name, boolean updateTime) {
    byte[] data = new byte[91];
    ByteBuffer bb = ByteBuffer.wrap(data);
    if (name != null) {
      int i;
      for (i = 0; i < 13 && i < name.length(); i++) {
        data[i + 1] = (byte) name.charAt(i);
      }
      data[i + 1] = '\0';
    }
    if (updateTime) {
      DateTime dt = new DateTime();
      DateTime dtUTC = dt.withZone(DateTimeZone.UTC);
      bb.position(62);
      bb.put((byte) 1);
      bb.putShort((short) dtUTC.getYear());
      bb.put((byte) dtUTC.getMonthOfYear());
      bb.put((byte) dtUTC.getDayOfMonth());
      bb.put((byte) dtUTC.getHourOfDay());
      bb.put((byte) dtUTC.getMinuteOfHour());
      bb.put((byte) dtUTC.getSecondOfMinute());
    }
    return data;
  }
}

package dk.iha.and.message;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import dk.iha.and.utils.MacEncoder;

public class Message {
  private final static int PACKET_TYPE        = 2;
  private final static int HEADER_LENGTH      = 60;
  private final static int PAYLOAD_MAX_LENGTH = 14;

  private final byte[]     mHeader            = new byte[256];
  private final ByteBuffer mByteBuffer        = ByteBuffer.wrap(mHeader);
  private final ByteBuffer mPayloadByteBuffer = ByteBuffer.wrap(mHeader, HEADER_LENGTH, PAYLOAD_MAX_LENGTH).asReadOnlyBuffer();
  private final Charset    mAscii             = Charset.forName("US-ASCII");
  private DateTime         mTransmissionDate;
  private DateTime         mMeasurementDate;
  private DeviceType       mDeviceType;
  private int              mPayloadLength;

  public Message() {
    mByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
  }

  public void readData(InputStream in) throws IOException {
    int length = in.read(mHeader, 0, mHeader.length);
    if (length != 74 && length != 70)
      throw new IOException("Incoming data has an invalid length: " + length);
    if (mByteBuffer.getShort(0) != PACKET_TYPE)
      throw new IOException("Invalid packet type.");
    try {
      mDeviceType = DeviceType.fromShort(mByteBuffer.getShort(6));
    } catch (DeviceTypeFormatException e) {
      throw new IOException(e);
    }
    mPayloadLength = mByteBuffer.getInt(2);
    switch (mDeviceType) {
    case UA767PBT:
      if (mPayloadLength != 10)
        throw new IOException("Invalid packet length.");
      break;
    case UC321PBT:
      if (mPayloadLength != 14)
        throw new IOException("Invalid packet length.");
      break;
    default:
      throw new IOException("Wrong device type.");
    }
    mMeasurementDate = new DateTime(
        mByteBuffer.getShort(9),
        mByteBuffer.get(11),
        mByteBuffer.get(12),
        mByteBuffer.get(13),
        mByteBuffer.get(14),
        mByteBuffer.get(15),
        0,
        DateTimeZone.UTC);
    mTransmissionDate = new DateTime(
        mByteBuffer.getShort(16),
        mByteBuffer.get(18),
        mByteBuffer.get(19),
        mByteBuffer.get(20),
        mByteBuffer.get(21),
        mByteBuffer.get(22),
        0,
        DateTimeZone.UTC);
  }

  public short getPacketType() {
    return PACKET_TYPE;
  }

  public int getPayloadLength() {
    return mPayloadLength;
  }

  public DeviceType getDeviceType() {
    return mDeviceType;
  }

  public byte getFlag() {
    return mByteBuffer.get(8);
  }

  public Date getMeasurementDate() {
    return mMeasurementDate.toDate();
  }

  public Date getTransmissionDate() {
    return mTransmissionDate.toDate();
  }

  public String getRemoteBluetoothId(MacEncoder encoder) {
    return encoder.encode(mHeader, 23, 6);
  }

  public String getAccessPointBluetoothId(MacEncoder encoder) {
    return encoder.encode(mHeader, 29, 6);
  }

  /**
   * 
   * @return
   * @throws IndexOutOfBoundsException
   */
  public String getSerialNumber() {
    return new String(mHeader, 35, 12, mAscii);
  }

  public float getBatteryStatus() {
    int b = mByteBuffer.get(57) & 0xFF;
    switch (mDeviceType) {
    case UA767PBT:
      return b * 0.03f + 2.3f;
    case UC321PBT:
      return b * 0.02f + 1.9f;
    }
    return Float.NaN;
  }

  public int getFirmwareRevision() {
    return (mByteBuffer.get(59) & 0xF8) >> 3;
  }

  public int getHardwareRevision() {
    return mByteBuffer.get(59) & 0x07;
  }

  public ByteBuffer getPayload() {
    mPayloadByteBuffer.position(HEADER_LENGTH);
    mPayloadByteBuffer.mark();
    return mPayloadByteBuffer;
  }
}
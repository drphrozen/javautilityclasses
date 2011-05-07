package dk.iha.and;

import java.io.DataInput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.GregorianCalendar;

public class PbtReader {
  private final static int mPacketType = 2;
  private final byte[] mHeader = new byte[60];
  private final byte[] mPayload = new byte[14];
  private final ByteBuffer mByteBuffer = ByteBuffer.wrap(mHeader);
  private final Charset mAscii = Charset.forName("US-ASCII");
  private final GregorianCalendar mCalendar = new GregorianCalendar();
  private Date mTransmissionDate;
  private Date mMeasurementDate;
  private DeviceType mDeviceType;
  private int mPacketLength;
  
  public PbtReader() {
    mByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
  }
  
  public void readData(DataInput in) throws IOException {
    in.readFully(mHeader, 0, 60);
    if(mByteBuffer.getShort(0) != mPacketType)
      throw new IOException("Invalid packet type.");    
    try {
      mDeviceType = DeviceType.fromShort(mByteBuffer.getShort(6));
    } catch (DeviceTypeFormatException e) {
      throw new IOException(e);
    }
    mPacketLength = mByteBuffer.getInt(2);
    switch (mDeviceType) {
    case UA767PBT:
      if(mPacketLength != 10)
        throw new IOException("Invalid packet length.");
      break;
    case UC321PBT:
      if(mPacketLength != 14)
        throw new IOException("Invalid packet length.");
      break;
    default:
      throw new IOException("Wrong device type.");
    }
    in.readFully(mPayload, 0, mPacketLength);
    mCalendar.set(
        mByteBuffer.getShort(9),
        mByteBuffer.get(11),
        mByteBuffer.get(12),
        mByteBuffer.get(13),
        mByteBuffer.get(14),
        mByteBuffer.get(15));
    mMeasurementDate = mCalendar.getTime();
    mCalendar.set(
      mByteBuffer.getShort(16),
      mByteBuffer.get(18),
      mByteBuffer.get(19),
      mByteBuffer.get(20),
      mByteBuffer.get(21),
      mByteBuffer.get(22));
    mTransmissionDate = mCalendar.getTime();
  }
  
  public short getPacketType() {
    return mPacketType;
  }

  public int getPacketLength() {
    return mPacketLength;
  }

  public DeviceType getDeviceType() {
    return mDeviceType;
  }

  public byte getFlag() {
    return mByteBuffer.get(8);
  }

  public Date getMeasurementDate() {
    return mMeasurementDate;
  }
  public Date getTransmissionDate() {
    return mTransmissionDate;
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
    switch(mDeviceType) {
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
}
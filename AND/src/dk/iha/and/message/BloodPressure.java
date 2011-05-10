package dk.iha.and.message;

import java.nio.ByteBuffer;
import java.text.ParseException;

import dk.iha.and.utils.HexDecoder;

public class BloodPressure implements Data {

  private final byte[]     mData        = new byte[10];
  private byte[]           mDecodedData = new byte[mData.length / 2];
  private final HexDecoder mHexDecoder  = new HexDecoder();

  /**
   * @return { Systolic [mmHg], Diastolic [mmHg], Pulse [BPM], Mean Arterial
   *         Pressure [mmHg] }
   */
  @Override public float[] getValues() {
    return new float[] { mDecodedData[1] & 0xFF, mDecodedData[2] & 0xFF, mDecodedData[3] & 0xFF, mDecodedData[4] & 0xFF };
  }

  @Override public void setData(ByteBuffer bb) throws ParseException {
    bb.get(mData);
    mHexDecoder.decode(mData, 0, mData.length, mDecodedData, 0);
    if ((mDecodedData[0] & 0xFF) != 0x80)
      throw new ParseException("Invalid measurement.", 0);
    mDecodedData[1] = (byte) ((mDecodedData[1] & 0xFF) + (mDecodedData[2] & 0xFF));
  }

  @Override public String toString() {
    return String.format("%d/%d, %d BPM, %d MAP", mDecodedData[1], mDecodedData[2], mDecodedData[3], mDecodedData[4]);
  }
}

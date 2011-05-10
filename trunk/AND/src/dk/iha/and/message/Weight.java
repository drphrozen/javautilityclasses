package dk.iha.and.message;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Weight implements Data {

  private static final float          POUND_IN_KG = 0.45359237f;
  private final static CharsetDecoder mAscii      = Charset.forName("US-ASCII").newDecoder();
  private final Pattern               pattern     = Pattern.compile("ST,([\\+\\-][\\d\\.]{6})(kg|lb)\\r\\n");
  private final byte[]                mData       = new byte[14];
  private final CharBuffer            mCharBuffer = CharBuffer.allocate(mData.length);
  private float                       mValue;

  @Override public void setData(ByteBuffer bb) throws ParseException {
    mAscii.decode(bb, mCharBuffer, true);
    mCharBuffer.rewind();
    Matcher m = pattern.matcher(mCharBuffer);
    if (m.matches() == false)
      throw new ParseException("Could not parse weight data.", 0);
    try {
      mValue = Float.valueOf(m.group(1));
      if (m.group(2).equals("lb")) {
        mValue *= POUND_IN_KG;
      }
    } catch (NumberFormatException e) {
      throw new ParseException("Could not parse weight value.", 4);
    }
  }

  @Override public float[] getValues() {
    return new float[] { mValue };
  }

  @Override public String toString() {
    return String.format("%fkg", mValue);
  }

}

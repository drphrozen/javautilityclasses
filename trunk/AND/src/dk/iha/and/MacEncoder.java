package dk.iha.and;


public class MacEncoder {
  private static final String HEXES = "0123456789ABCDEF";
  private final char mSeparator;
  private final StringBuilder mStringBuilder = new StringBuilder(6);

  //TODO: replace with optimized reverse function
  private final boolean mReverse;

  public MacEncoder(char separator, boolean reverse) {
    mSeparator = separator;
    mReverse = reverse;
  }
  
  public String encode(byte[] buffer, int offset, int length) {
    if (buffer == null)
      return null;
    if (buffer.length == 0)
      return "";
    for (int i = offset; i < length - 1; i++) {
      byte b = buffer[(mReverse ? (length-1) - i : i)];
      mStringBuilder.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt(b & 0x0F)).append(mSeparator);
    }
    int i = offset+length-1;
    byte b = buffer[(mReverse ? (length-1) - i : i)];
    
    mStringBuilder.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt(b & 0x0F));

    return mStringBuilder.toString();
  }
}

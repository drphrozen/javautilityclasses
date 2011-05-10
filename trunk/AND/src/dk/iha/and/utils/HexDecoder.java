package dk.iha.and.utils;

import java.text.ParseException;

public class HexDecoder {
  private static final byte[] HEX_TABLE_TEMPLATE = new byte[] { 0, '0', 1, '1', 2, '2', 3, '3', 4, '4', 5, '5', 6, '6', 7, '7', 8, '8', 9, '9', 0, '0', 10, 'a', 11, 'b', 12, 'c', 13, 'd', 14, 'e', 15, 'f', 10, 'A', 11, 'B', 12, 'C', 13, 'D', 14, 'E', 15, 'F' };
  private static final byte[] HEX_TABLE;
  
  static {
    HEX_TABLE = new byte[256];
    for (int i = 0; i < 256; i++) {
      HEX_TABLE[i] = -1;
    }
    for (int i = 0; i < HEX_TABLE_TEMPLATE.length; i += 2) {
      HEX_TABLE[HEX_TABLE_TEMPLATE[i + 1]] = HEX_TABLE_TEMPLATE[i];
    }
  }

  public void decode(byte[] in, int offset, int length, byte[] out, int outOffset) throws ParseException {
    if (length % 2 != 0)
      throw new ParseException("Data length must be even.", 0);
    length /= 2;
    for (int i = 0; i < length; i++) {
      int inIndex = i * 2 + offset;
      out[i + outOffset] = (byte) ((lookup(in, inIndex) << 4) | lookup(in, inIndex + 1));
    }
  }

  private static byte lookup(byte[] in, int index) throws ParseException {
    byte b = HEX_TABLE[in[index]];
    if (b == -1)
      throw new ParseException("Invalid hex character!", index);
    return b;
  }
}
package dk.iha.nonin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NoninConnector {
  private final InputStream mInputStream;
  private final Format8     mBuffer = new Format8();

  public NoninConnector(InputStream in, OutputStream out) throws IOException {
    this.mInputStream = in;
    out.write(new byte[] { 'D', 'D' });
  }

  public Format8 read() throws IOException {
    int b;
    while (hasMSB(b = mInputStream.read()) == false) {}
    mBuffer.data[0] = (byte) b;
    mBuffer.data[1] = (byte) mInputStream.read();
    mBuffer.data[2] = (byte) mInputStream.read();
    mBuffer.data[3] = (byte) mInputStream.read();
    return mBuffer;
  }

  private boolean hasMSB(int b) {
    return (b & 0x80) == 0x80;
  }
}

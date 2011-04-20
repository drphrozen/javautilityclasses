package dk.iha.nonin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NoninConnector {
	private final InputStream mInputStream;
	private final Format8 mBuffer = new Format8();

	public NoninConnector(InputStream in, OutputStream out) throws IOException {
		this.mInputStream = in;
		out.write(new byte[] {'D', 'D'});
	}
	
	public Format8 read() throws IOException {
		mInputStream.read(mBuffer.data);
		if(hasMSB(mBuffer.data[0]) == false)
			throw new IOException("MSB not set on first byte!");
		return mBuffer;
	}
	
	private boolean hasMSB(byte b) {
		return (b & 0x80) != 0x80;
	}
}

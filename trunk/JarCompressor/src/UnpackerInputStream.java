import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UnpackerInputStream extends FilterInputStream {
	
	private DataInputStream mDataInputStream;
	
	public UnpackerInputStream(InputStream in) {
		super(in);
		mDataInputStream = new DataInputStream(this);
	}
	
	/**
	 * Writes the header for the next entry stream.
	 * @param info Some info, should be some sort of serialization information.
	 * @param size The size of the stream.
	 * @throws IOException
	 */
	public EntryInfo nextEntry() throws IOException
	{
		EntryInfo info = new EntryInfo(); 
		info.setInfo(mDataInputStream.readUTF());
		info.setSize(mDataInputStream.readLong());
		return info;
	}
}


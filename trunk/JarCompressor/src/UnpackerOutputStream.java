import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
public class UnpackerOutputStream extends FilterOutputStream {

	private DataOutputStream mDataOutputStream;
	
	public UnpackerOutputStream(OutputStream out) {
		super(out);
		mDataOutputStream = new DataOutputStream(this);
	}
	
	/**
	 * Writes the header for the next entry stream.
	 * @param info Some info, should be some sort of serialization information.
	 * @param size The size of the stream.
	 * @throws IOException
	 */
	public void nextEntry(EntryInfo info) throws IOException
	{
		mDataOutputStream.writeUTF(info.getInfo());
		mDataOutputStream.writeLong(info.getSize());
	}
}

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.thoughtworks.xstream.XStream;


public class Recompressor {

	private XStream xstream = new XStream();

	public Recompressor() {
		// You require xstream-[version].jar and xpp3-[version].jar in the classpath. XPP3 is a very fast XML pull-parser implementation. If you do not want to include this dependency, you can use a standard JAXP DOM parser instead:
		//XStream xstream = new XStream(new DomDriver()); // does not require XPP3 library
		// Note: This class is a simple facade designed for common operations. For more flexibility you may choose to create your own facade that behaves differently.
		// Now, to make the XML outputted by XStream more concise, you can create aliases for your custom class names to XML element names. This is the only type of mapping required to use XStream and even this is optional.

		xstream.alias("zipEntry", ZipEntry.class);
	}
	
	/**
	 * Unpack a file, suffix ".unpacked".
	 * @param fileIn
	 * @return A File describing the newly created file.
	 * @throws IOException
	 */
	public File UnpackJar(File fileIn) throws IOException
	{
		File fileOut = new File(fileIn.getAbsolutePath() + ".unpacked");
		UnpackJar(fileIn, fileOut);
		return fileOut;
	}

	/**
	 * Unpack a file, optionally replace the original file.
	 * @param fileIn
	 * @param replace If true, then replace the new file with the old one, otherwise it works like UnpackJar(File). 
	 * @throws IOException
	 */
	public void UnpackJar(File fileIn, boolean replace) throws IOException
	{
		if(replace == false)
		{
			UnpackJar(fileIn);
		}
		else
		{
			File fileOut = UnpackJar(fileIn);
			fileIn.delete();
			fileOut.renameTo(fileIn);
		}
	}
	
	/**
	 * Unpack a file.
	 * @param fileIn The file to unpack.
	 * @param fileOut The name of the new file.
	 * @throws IOException
	 */
	public void UnpackJar(File fileIn, File fileOut) throws IOException
	{
		ByteArrayOutputStream intermediate = new ByteArrayOutputStream();

		long originalSize = fileIn.length();
		long internalSize = 0;
		
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(fileIn));
		UnpackerOutputStream output = new UnpackerOutputStream(new BufferedOutputStream(new FileOutputStream(fileOut)));
		
		
		ZipInputStream zip = new ZipInputStream(input);
		ZipEntry entry;
		while((entry = zip.getNextEntry()) != null)
		{			
			String xml = xstream.toXML(entry);
			long zipSize = entry.getSize();
			if(zipSize == -1)
			{
				int value;
				while((value = zip.read()) != -1)
				{
					intermediate.write(value);
				}
				internalSize += intermediate.size();
				output.nextEntry(new EntryInfo(xml, intermediate.size()));
				intermediate.writeTo(output);
				intermediate.reset();
			}
			else
			{
				internalSize += zipSize;
				output.nextEntry(new EntryInfo(xml, zipSize));
				int value;
				while((value = zip.read()) != -1)
				{
					output.write(value);
				}
			}
		}
		input.close();
		output.close();
		
		long newSize = fileOut.length();
		System.out.printf("Original:          %19d\n", originalSize);
		System.out.printf("Unpacked size:     %19d\n", internalSize);
		System.out.printf("Recompressed size: %19d\n", newSize);
	}
}

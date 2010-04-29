import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class DirectoryOutputStream extends FilterOutputStream {
	
	private ArrayDeque<File> files = new ArrayDeque<File>();
	private ZipOutputStream dataOutput;
	private final String root;
	
	public DirectoryOutputStream(OutputStream output, File root)
	{
		super(output);
		dataOutput = new ZipOutputStream(output);
		dataOutput.setLevel(ZipOutputStream.STORED);
		this.root = (root.getAbsolutePath().endsWith(File.separator) ?
			root.getAbsolutePath():
			root.getAbsolutePath() + File.separatorChar);
		files.addAll(Arrays.asList(root.listFiles()));
	}
	
	public File nextFile() throws IOException
	{
		File file;
		while((file = files.pollLast()) != null)
		{
			String relativePath = file.getAbsolutePath().substring(root.length()).replace(File.separatorChar, '/');
			relativePath = (file.isDirectory() ? relativePath + '/' : relativePath);
			ZipEntry newEntry = new ZipEntry(relativePath);
			long modified = file.lastModified();
			if(modified != 0L)
				newEntry.setTime(modified);
			dataOutput.putNextEntry(newEntry);
			if(file.isDirectory())
			{
				files.addAll(Arrays.asList(file.listFiles()));
			}
			else
			{
				return file;
			}
		}
		return null;
	}
	
}

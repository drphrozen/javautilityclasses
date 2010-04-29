import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;


public class Program {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CompressFolder("C:\\eclipse\\");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void CompressFolder(String path) throws IOException
	{
		Recompressor recompressor = new Recompressor();
		File root = new File(path);
		ArrayDeque<File> files = new ArrayDeque<File>();
		files.addAll(Arrays.asList(root.listFiles()));

		long oldSize = 0;
		long newSize = 0;
		
		File file;
		while((file = files.pollLast()) != null)
		{
			long fileSize = file.length(); 
			oldSize += fileSize;
			
			System.out.println(file.getAbsolutePath());
			if(file.isDirectory())
			{
				files.addAll(Arrays.asList(file.listFiles()));
				newSize += fileSize;
			}
			else
			{
				if(file.getName().endsWith(".jar") || file.getName().endsWith(".zip"))
				{
					File newFile = recompressor.UnpackJar(file);
					newSize += newFile.length();
					file.delete();
				}
				else
				{
					newSize += file.length();
				}
			}
		}
		System.out.println("Old size: " + oldSize);
		System.out.println("New size: " + newSize);
	}
}

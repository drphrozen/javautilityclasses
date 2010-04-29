import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Program().run();
	}

	private void run() {
		try {
			File file = new File("data/names.txt");
			InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
			char[] buffer = new char[(int)file.length()];
			int readLength = 0; 
			for(int i = 0; i<buffer.length; i += readLength) {
				int length = reader.read(buffer, i, buffer.length);
				if(readLength == -1)
					break;
				readLength += length;
			}
			
			String str = new String(buffer, 0, readLength);
			str = str.replaceAll("\"", "");
			String[] names = str.split(",");
			Arrays.sort(names);

			long sum = 0;
			for (int i = 0; i < names.length; i++) {
				String name = names[i];
				sum += nameScore(name, i+1);
			}
			System.out.println(sum);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private final int A = ((int)'A') - 1;
	
	private int nameScore(String name, int index) {
		final int length = name.length();
		int sum = 0;
		for (int i = 0; i < length; i++) {
			int c = (int)name.charAt(i)-A;
			sum += c;
		}
		return sum * index;
	}
}

import java.io.FileInputStream;
import java.io.IOException;

public class Program {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		CRC8Table crc8Table = new CRC8Table(0x8C);
		int[] tableData = crc8Table.getTable();
		for (int i = 0; i < tableData.length; i++) {
			int b = tableData[i];
			System.out.printf("%02X ", b);
			if(i%8 == 7)
				System.out.println();
		}
		
		byte[] buffer = new byte[3 + 0x37 + 2];
		FileInputStream stream = new FileInputStream("HxM.log");
		while(stream.read(buffer) != -1) {
//			byte[] payload = Arrays.copyOfRange(buffer, 3, 3 + 0x37);
			int b = 0;
			System.gc();
			long start = System.nanoTime();
			for(int i = 0; i<1000000; ++i) {
				b = crc8Table.calc(buffer, 3, 3+0x37);
//				b = CRC8.crc8PushBlock(buffer, 3, 0x37, 0);
			}
			long stop = System.nanoTime();
			System.out.println((stop - start)/1000000000.0);
			
			System.out.printf("%02X crc(%02X)\n", buffer[3 + 0x37], b);
		}
	}
}

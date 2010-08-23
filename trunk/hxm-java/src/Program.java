import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import dk.znz.znk.HxMEvent;
import dk.znz.znk.HxMEventListener;
import dk.znz.znk.HxMReader;

public class Program implements HxMEventListener {


	@Override
	public void handle(HxMEvent event) {
		System.out.printf("%s\n%s\n%03d%%\n%d\n%d\n\n", event.firmwareID(), event.hardwareID(), event.batteryCharge(), event.heartRate(), event.heartBeatNumber());
	}
	
	private void start() throws NoSuchPortException, UnsupportedCommOperationException, PortInUseException {
		HxMReader reader = new HxMReader(this, "COM30");
		Thread thread = new Thread(reader);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Program program = new Program();
		System.out.println("Program starts");
		
		try {
			program.start();
		} catch (NoSuchPortException e) {
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
		} catch (PortInUseException e) {
			e.printStackTrace();
		}
		System.out.println("Program ended..");
		
		
//		CRC8Table crc8Table = new CRC8Table(0x8C);
//		int[] tableData = crc8Table.getTable();
//		for (int i = 0; i < tableData.length; i++) {
//			int b = tableData[i];
//			System.out.printf("%02X ", b);
//			if(i%8 == 7)
//				System.out.println();
//		}
//		
//		byte[] buffer = new byte[3 + 0x37 + 2];
//		FileInputStream stream = new FileInputStream("HxM.log");
//		while(stream.read(buffer) != -1) {
////			byte[] payload = Arrays.copyOfRange(buffer, 3, 3 + 0x37);
//			int b = 0;
//			System.gc();
//			long start = System.nanoTime();
//			for(int i = 0; i<10000000; ++i) {
//				b = crc8Table.calc(buffer, 3, 3+0x37);
////				b = CRC8.crc8PushBlock(buffer, 3, 0x37, 0);
//			}
//			long stop = System.nanoTime();
//			System.out.println((stop - start)/1000000000.0);
//			
//			System.out.printf("%02X crc(%02X)\n", buffer[3 + 0x37], b);
//		}
	}
}

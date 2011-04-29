import java.util.concurrent.CountDownLatch;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Program().run(args);
	}

	public void run(String[] args) {
		final CountDownLatch latch = new CountDownLatch(1);
		BluetoothFindService bluetoothFindService = null;
		try {
			bluetoothFindService = new BluetoothFindService(
					new BluetoothListener() {

						@Override
						public void onLocated(RemoteDevice remoteDevice) {
							System.out.println("Located");
						}

						@Override
						public void onFinished() {
							System.out.println("Finished");
							latch.countDown();
						}

						@Override
						public void onLocated(RemoteDevice remoteDevice, ServiceRecord... serviceRecords) {
							System.out.println("Located services");
						}
					});
			bluetoothFindService.find();
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(bluetoothFindService != null)
			bluetoothFindService.shutdown();
	}
}
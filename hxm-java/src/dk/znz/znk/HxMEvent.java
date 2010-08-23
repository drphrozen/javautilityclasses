package dk.znz.znk;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class HxMEvent {
	private final ByteBuffer bb;

	public HxMEvent(byte[] data) {
		bb = ByteBuffer.allocate(data.length);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(data);
	}

	private String idVersion(int index) {
		byte[] chars = new byte[] {
			bb.get(2 + index),
			bb.get(3 + index)
		};
		return String.format("9500.%04d.V%s", bb.getShort(0 + index), new String(chars, Charset.forName("US-ASCII")));
	}

	/**
	 * Zephyr Firmware release. 
	 * @return 9500.xxxx.Vyz where xxxx = 0000-9999, y = 1-9, z = A-Z or a-z
	 */
	public String firmwareID() {
		return idVersion(0);
	}

	/**
	 * Zephyr Hardware release. 
	 * @return 9500.xxxx.Vyz where xxxx = 0000-9999, y = 1-9, z = A-Z or a-z
	 */
	public String hardwareID() {
		return idVersion(4);
	}
	
	/**
	 * Battery charge indicator.
	 * @return 0-100 (%)
	 */
	public byte batteryCharge() {
		return bb.get(8);
	}

	/**
	 * The current heart rate.
	 * @return valid range 30bpm - 240bpm
	 */
	public short heartRate() {
		return (short)(bb.get(9) & 0xFF);
	}

	/**
	 * Counts the number of heart beats.
	 * @return returns the heart beat number, 0-255 rolls around 
	 */
	public short heartBeatNumber() {
		return (short)(bb.get(10) & 0xFF);
	}
	
	/**
	 * The last 15 heart beat timestamps, each is represented as 0-65535ms.
	 * @return an array of the last 15 timestamps
	 */
	public int[] heartBeatTimestamp() {
		int[] timestamps = new int[15];
		for (int i = 0; i < timestamps.length; i++) {
			timestamps[i] = bb.getShort(11+(i*2)) & 0xFFFF;
		}
		return timestamps;
	}
}

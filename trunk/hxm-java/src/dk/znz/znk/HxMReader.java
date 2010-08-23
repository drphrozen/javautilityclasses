package dk.znz.znk;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;

public class HxMReader implements Runnable {
	
	private final HxMEventListener listener;
	private final String commPort;
	private final SerialPort serialPort;
	private volatile boolean isRunning;
	
	public HxMReader(HxMEventListener listener, String commPort) throws NoSuchPortException, UnsupportedCommOperationException, PortInUseException {
		this.listener = listener;
		this.commPort = commPort;
		CommPortIdentifier commPortIdentifier;
		commPortIdentifier = CommPortIdentifier.getPortIdentifier(commPort);
		serialPort = (SerialPort) commPortIdentifier.open(Thread.currentThread().getName(), 2000);
		serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
	}
	
	public void stop() {
		isRunning = false;
	}
	
	public void run() {
		try {
			InputStream input = serialPort.getInputStream();
			isRunning = true;
			while (isRunning) {
				int b = input.read();
				if (b != 0x02) continue; // Start byte
				b = input.read();
				if (b != 0x26) continue; // Message ID (HxM)
				b = input.read();
				if (b != 0x37) continue; // Payload length
				byte[] payload = new byte[0x37];
				input.read(payload);
				b = input.read();
				CRC8Table crc8Table = new CRC8Table(0x8C);
				int crc = crc8Table.calc(payload);
				if (b != crc) {
					System.err.printf("CRC Error. Payload: %02X CRC-8 byte: %02X", crc, b);
					continue;
				}
				b = input.read();
				if (b != 0x03) continue;
				listener.handle(new HxMEvent(payload));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getCommPort() {
		return commPort;
	}
}

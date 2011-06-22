package dk.znz.znk.terminal;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

public class SerialConnection {
  private final CommPortIdentifier mCommPortIdentifier;
  private final SerialPort mSerialPort;
  private final String mFileDescriptor = "SimpleSerial" + Integer.toHexString(SerialConnection.this.hashCode());

  public SerialConnection(String portName) throws NoSuchPortException, PortInUseException {
    mCommPortIdentifier = CommPortIdentifier.getPortIdentifier(portName);
    mSerialPort = (SerialPort) mCommPortIdentifier.open(mFileDescriptor, 2000);
  }
  
  public void setSettings(SerialSettings settings) throws UnsupportedCommOperationException {
    mSerialPort.setSerialPortParams(settings.getSpeed(), settings.getDataBits(), settings.getStopBits(), settings.getParity());
    mSerialPort.setFlowControlMode(settings.getFlowControl());
  }
  
  public SerialSettings getSettings() {
    return new SerialSettings(mSerialPort.getBaudRate(), mSerialPort.getDataBits(), mSerialPort.getStopBits(), mSerialPort.getParity(), mSerialPort.getFlowControlMode());
  }
  
  public InputStream getInputStream() throws IOException {
    return mSerialPort.getInputStream();
  }

  public OutputStream getOutputStream() throws IOException {
    return mSerialPort.getOutputStream();
  }
  
  public void addSerialPortEventListener(SerialPortEventListener serialPortEventListener) throws TooManyListenersException {
    mSerialPort.addEventListener(serialPortEventListener);
  }
}

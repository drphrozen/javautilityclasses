package gnu.io;

import com.sun.jna.Platform;

public class SerialPortInfoFactory {
  
  private final CreateSerialPortInfo mCreateSerialPortInfo;
  
  protected SerialPortInfoFactory() {
    switch(Platform.getOSType()) {
    case Platform.WINDOWS:
      mCreateSerialPortInfo = new CreateSerialPortInfo() {
        @Override public SerialPortInfo createSerialPortInfo(String name) {
          return new SerialPortInfoWin32(name);
        }
      };
      break;
    default:
      mCreateSerialPortInfo = new CreateSerialPortInfo() {
        @Override public SerialPortInfo createSerialPortInfo(String name) {
          return new SerialPortInfoDefault(name);
        }
      };
      break;
    }
  }
  
  // singleton begin
  private static class SingletonHolder { 
    public static final SerialPortInfoFactory INSTANCE = new SerialPortInfoFactory();
  }

  public static SerialPortInfoFactory getInstance() {
    return SingletonHolder.INSTANCE;
  }
  // singleton end
  
  public SerialPortInfo createSerialPortInfo(String name) {
    return mCreateSerialPortInfo.createSerialPortInfo(name);
  }
  private interface CreateSerialPortInfo {
    SerialPortInfo createSerialPortInfo(String name);
  }
}

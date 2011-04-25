package gnu.io;

public class SerialPortInfoDefault extends SerialPortInfo {

  public SerialPortInfoDefault(String name) {
    super(name);
  }

  @Override public String getDescription() {
    return "";
  }
  
  @Override public String toString() {
    return mName;
  }
}

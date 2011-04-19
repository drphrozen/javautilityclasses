package gnu.io;

public enum DataBits {
  DATABITS_8(SerialPort.DATABITS_8),
  DATABITS_7(SerialPort.DATABITS_7),
  DATABITS_6(SerialPort.DATABITS_6),
  DATABITS_5(SerialPort.DATABITS_5);
  
  private final int mValue;
  
  DataBits(int value) {
    mValue = value;
  }
  
  public int getValue() {
    return mValue;
  }
  
  @Override public String toString() {
    switch(this) {
    case DATABITS_5:
      return "5 bits";
    case DATABITS_6:
      return "6 bits";
    case DATABITS_7:
      return "7 bits";
    case DATABITS_8:
      return "8 bits";
    }
    return super.toString();
  }
}

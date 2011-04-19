package gnu.io;

public enum StopBits {
  STOPBITS_1(SerialPort.STOPBITS_1),
  STOPBITS_1_5(SerialPort.STOPBITS_1_5),
  STOPBITS_2(SerialPort.STOPBITS_2);
  
  private final int mValue;
  
  StopBits(int value) {
    mValue = value;
  }
  
  public int getValue() {
    return mValue;
  }
  
  @Override public String toString() {
    switch(this) {
    case STOPBITS_1:
      return "1 bit";
    case STOPBITS_1_5:
      return "1.5 bits";
    case STOPBITS_2:
      return "2 bits";
    }
    return super.toString();
  }
}

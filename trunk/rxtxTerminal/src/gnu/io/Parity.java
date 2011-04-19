package gnu.io;

public enum Parity {
  PARITY_NONE(SerialPort.PARITY_NONE),
  PARITY_ODD(SerialPort.PARITY_ODD),
  PARITY_EVEN(SerialPort.PARITY_EVEN);
  
  private final int mValue;
  
  Parity(int value) {
    mValue = value;
  }
  
  public int getValue() {
    return mValue;
  }
  
  @Override public String toString() {
    switch(this) {
    case PARITY_EVEN:
      return "Even";
    case PARITY_NONE:
      return "None";
    case PARITY_ODD:
      return "Odd";
    }
    return super.toString();
  }
}

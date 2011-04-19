package gnu.io;

public enum FlowControl {
  FLOWCONTROL_NONE(SerialPort.FLOWCONTROL_NONE),
  FLOWCONTROL_RTSCTS(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT),
  FLOWCONTROL_XONXOFF(SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT);
  
  private final int mValue;
  
  FlowControl(int value) {
    mValue = value;
  }
  
  public int getValue() {
    return mValue;
  }
  
  @Override public String toString() {
    switch(this) {
    case FLOWCONTROL_NONE:
      return "None";
    case FLOWCONTROL_RTSCTS:
      return "RTS/CTS";
    case FLOWCONTROL_XONXOFF:
      return "XON/XOFF";
    }
    return super.toString();
  }
}

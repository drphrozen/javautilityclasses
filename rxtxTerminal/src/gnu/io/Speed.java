package gnu.io;

public enum Speed {
  SPEED_110(110),
  SPEED_330(330),
  SPEED_600(600),
  SPEED_1200(1200),
  SPEED_2400(2400),
  SPEED_4800(4800),
  SPEED_9600(9600),
  SPEED_14400(14400),
  SPEED_19200(19200),
  SPEED_38400(38400),
  SPEED_57600(57600),
  SPEED_115200(115200),
  SPEED_128000(128000),
  SPEED_256000(256000),
  SPEED_CUSTOM(-1);
  
  private final int mValue;
  
  Speed(int value) {
    mValue = value;
  }
  
  public int getValue() {
    return mValue;
  }
  
  @Override public String toString() {
    switch(this) {
    case SPEED_CUSTOM:
      return "Custom";
    default:
      return Integer.toString(mValue);
    }
  }
}

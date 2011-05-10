package dk.iha.and.message;

public enum DeviceType {
  UA767PBT(0x02FF),
  UC321PBT(0x0141);

  public final int mValue;

  private DeviceType(int s) {
    mValue = s;
  }

  public int getValue() {
    return mValue;
  }

  /**
   * 
   * @param s
   * @return
   * @throws DeviceTypeFormatException
   */
  public static DeviceType fromShort(short s) {
    if (s == UA767PBT.getValue())
      return UA767PBT;
    if (s == UC321PBT.getValue())
      return UC321PBT;
    throw new DeviceTypeFormatException();
  }
}
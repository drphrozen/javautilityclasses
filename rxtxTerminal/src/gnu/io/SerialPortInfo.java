package gnu.io;

public abstract class SerialPortInfo
{
  protected final String mName;

  public SerialPortInfo(String name)
  {
    mName = name;
  }
  
  public String getName() {
    return mName;
  }
  
  /**
   * @return The description of the port if available, otherwise an empty string.
   */
  public abstract String getDescription();
  
  @Override public String toString() {
    String info = getDescription();
    if(info == "") {
      return mName;
    } else {
      return mName + " (" + getDescription() + ")";
    }
  }
}
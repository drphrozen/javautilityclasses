package dk.iha.and.message;

public class DeviceTypeFormatException extends IllegalArgumentException {

  private static final long serialVersionUID = 8687269628746065903L;

  public DeviceTypeFormatException() {
    super();
  }

  public DeviceTypeFormatException(String message, Throwable cause) {
    super(message, cause);
  }

  public DeviceTypeFormatException(String s) {
    super(s);
  }

  public DeviceTypeFormatException(Throwable cause) {
    super(cause);
  }
}

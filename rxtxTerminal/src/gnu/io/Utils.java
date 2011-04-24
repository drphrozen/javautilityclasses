package gnu.io;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Utils {

  public static List<CommPortIdentifier> getPorts(PortType type) {
    @SuppressWarnings("unchecked") Enumeration<CommPortIdentifier> commPortIdentifiers = CommPortIdentifier.getPortIdentifiers();
    ArrayList<CommPortIdentifier> serialPorts = new ArrayList<CommPortIdentifier>();
    while (commPortIdentifiers.hasMoreElements()) {
      CommPortIdentifier commPortIdentifier = commPortIdentifiers.nextElement();
      if (commPortIdentifier.getPortType() == type.getValue())
        serialPorts.add(commPortIdentifier);
    }
    return serialPorts;
  }

  public static List<CommPortIdentifier> getPorts() {
    @SuppressWarnings("unchecked") Enumeration<CommPortIdentifier> commPortIdentifiers = CommPortIdentifier.getPortIdentifiers();
    ArrayList<CommPortIdentifier> serialPorts = new ArrayList<CommPortIdentifier>();
    while (commPortIdentifiers.hasMoreElements()) {
      serialPorts.add(commPortIdentifiers.nextElement());
    }
    return serialPorts;
  }

  public enum PortType {
    PORT_SERIAL(1),
    PORT_PARALLEL(2),
    PORT_I2C(3),
    PORT_RS485(4),
    PORT_RAW(5);

    private final int mType;

    private PortType(int type) {
      mType = type;
    }

    public int getValue() {
      return mType;
    }
  }
}

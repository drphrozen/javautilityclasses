package gnu.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

public class Utils {

  public static SerialPortInfo[] getSerialPorts() {
    SerialPortInfoFactory factory = SerialPortInfoFactory.getInstance();
    List<CommPortIdentifier> identifiers = Utils.getPorts(PortType.PORT_SERIAL);
    ArrayList<SerialPortInfo> serialPortInfos = new ArrayList<SerialPortInfo>();
    for (CommPortIdentifier identifier : identifiers) {
      serialPortInfos.add(factory.createSerialPortInfo(identifier.getName()));
    }
    return serialPortInfos.toArray(new SerialPortInfo[0]);
  }

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

  public final static List<String> DefaultSpeeds = Collections.unmodifiableList(Arrays.asList(
                                                     "110",
                                                     "330",
                                                     "600",
                                                     "1200",
                                                     "2400",
                                                     "4800",
                                                     "9600",
                                                     "14400",
                                                     "19200",
                                                     "38400",
                                                     "57600",
                                                     "115200",
                                                     "128000",
                                                     "256000"));

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

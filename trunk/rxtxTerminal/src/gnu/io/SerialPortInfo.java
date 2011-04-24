package gnu.io;

import gnu.io.Utils.PortType;

import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public class SerialPortInfo
{
  private String mName;
  private String mInfo;

  public SerialPortInfo(String name)
  {
    mName = name;
    // TODO: should be string, not char[] hacks
    String[] str = queryDosDevice(name);
    mInfo = (str.length != 0 ? str[0].toString().replace("\\Device\\", "") : "N/A " + Kernel32.INSTANCE.GetLastError());
  }
  
  @Override public String toString() {
    return mName + " (" + mInfo + ")";
  }

  public static SerialPortInfo[] GetSerialPorts()
  {
    List<CommPortIdentifier> identifiers = Utils.getPorts(PortType.PORT_SERIAL);
    ArrayList<SerialPortInfo> serialPortInfos = new ArrayList<SerialPortInfo>();
    for (CommPortIdentifier identifier : identifiers) {
      serialPortInfos.add(new SerialPortInfo(identifier.getName()));
    }
    return serialPortInfos.toArray(new SerialPortInfo[0]);
  }
  
  public String[] queryDosDevice(String name) { 
    char[] buffer = new char[65535];
    int result = SerialPortInfo.Kernel32.INSTANCE.QueryDosDeviceW((name + "\0").toCharArray(), buffer, buffer.length);
    ArrayList<String> list = new ArrayList<String>();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < result; i++) {
      char c = buffer[i];
      if(c != 0) {
        sb.append(c);
      } else {
        list.add(sb.toString());
        sb.setLength(0);
      }
    }
    return list.toArray(new String[0]);
//    if(result == 0) {
//      System.out.println("ERROR: " + SerialPortInfo.Kernel32.INSTANCE.GetLastError());
//    }
  }
  
  //kernel32.dll uses the __stdcall calling convention (check the function
  //declaration for "WINAPI" or "PASCAL"), so extend StdCallLibrary
  //Most C libraries will just extend com.sun.jna.Library,
  public interface Kernel32 extends StdCallLibrary {
    Kernel32 INSTANCE      = (Kernel32)Native.loadLibrary("kernel32", Kernel32.class);
    // Optional: wraps every call to the native library in a
    // synchronized block, limiting native calls to one at a time
    Kernel32 SYNC_INSTANCE = (Kernel32)Native.synchronizedLibrary(INSTANCE);
    
    int GetLastError();
    int QueryDosDeviceW(char[] lpDeviceName, char[] lpTargetPath, int ucchMax);
  }

}
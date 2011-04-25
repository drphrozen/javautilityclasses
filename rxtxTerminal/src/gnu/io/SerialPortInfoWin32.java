package gnu.io;

import java.util.ArrayList;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public class SerialPortInfoWin32 extends SerialPortInfo {

  protected final String mInfo;
  
  public SerialPortInfoWin32(String name) {
    super(name);
    // TODO: should be string, not char[] hacks
    String[] str = queryDosDevice(name);
    mInfo = (str.length != 0 ? str[0].toString().replace("\\Device\\", "") : "N/A " + Kernel32.INSTANCE.GetLastError());
  }
  
  protected String[] queryDosDevice(String name) {
    // TODO: should handle GetLastError
    char[] buffer = new char[65535];
    int result = Kernel32.INSTANCE.QueryDosDeviceW((name + "\0").toCharArray(), buffer, buffer.length);
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

  @Override public String getDescription() {
    return mInfo;
  }
}

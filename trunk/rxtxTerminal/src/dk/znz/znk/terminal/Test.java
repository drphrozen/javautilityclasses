package dk.znz.znk.terminal;

import java.util.ArrayList;

import gnu.io.SerialPortInfo;



public class Test {

  /**
   * @param args
   */
  public static void main(String[] args) {
    char[] buffer = new char[65535];
    int result = SerialPortInfo.Kernel32.INSTANCE.QueryDosDeviceW("COM1\0".toCharArray(), buffer, buffer.length);
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
    for (String string : list) {
      System.out.println(string);
    }
    if(result == 0) {
      System.out.println("ERROR: " + SerialPortInfo.Kernel32.INSTANCE.GetLastError());
    }
  }
}

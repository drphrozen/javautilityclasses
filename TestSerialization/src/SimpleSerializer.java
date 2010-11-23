import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SimpleSerializer {
  public static void write(OutputStream output, SimpleSerializable root) throws IOException {
    DataOutputStream dataOutput = new DataOutputStream(output);
    root.write(dataOutput);
    dataOutput.flush();
  }

  public static void read(InputStream input, SimpleSerializable root) throws IOException {
    DataInputStream dataInput = new DataInputStream(input);
    root.read(dataInput);
  }
}

package dk.znz.serialization;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SimpleSerializer {
  public static void serialize(OutputStream output, SimpleSerializable root) throws IOException {
    DataOutputStream dataOutput = new DataOutputStream(output);
    root.serialize(dataOutput);
  }

  public static void deserialize(InputStream input, SimpleSerializable root) throws IOException {
    DataInputStream dataInput = new DataInputStream(input);
    root.deserialize(dataInput);
  }
}

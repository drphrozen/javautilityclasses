import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface SimpleSerializable {
  void write(DataOutputStream output) throws IOException;
  void read(DataInputStream input) throws IOException;
}

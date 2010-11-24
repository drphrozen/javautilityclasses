package dk.znz.serialization;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface SimpleSerializable {
  void serialize(DataOutputStream output) throws IOException;
  void deserialize(DataInputStream input) throws IOException;
}

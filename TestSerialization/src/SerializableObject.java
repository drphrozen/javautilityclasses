import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import dk.znz.serialization.SimpleSerializable;

public class SerializableObject implements SimpleSerializable {

  private String mString = "";
  private int    mInteger = 0;

  public String getString() {
    return mString;
  }

  public void setString(String string) {
    mString = string;
  }

  public int getInteger() {
    return mInteger;
  }

  public void setInteger(int integer) {
    mInteger = integer;
  }

  @Override
  public void deserialize(DataInputStream input) throws IOException {
    mString = input.readUTF();
  }

  @Override
  public void serialize(DataOutputStream output) throws IOException {
    output.writeUTF(mString);
  }
}

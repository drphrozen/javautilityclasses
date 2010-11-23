import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
  public void read(DataInputStream input) throws IOException {
    mString = input.readUTF();
  }

  @Override
  public void write(DataOutputStream output) throws IOException {
    output.writeUTF(mString);
  }
}

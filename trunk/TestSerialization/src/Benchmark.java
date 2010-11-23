import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.DeflaterOutputStream;

public class Benchmark {

  public static void main(String[] args) throws IOException {
    writeObject(new SimpleObject());
    writeObject(new SimpleObject[] { new SimpleObject() });
    writeObject(new SimpleObject[] { new SimpleObject(), new SimpleObject() });
    writeObject(new SimpleObject[] { new SimpleObject(), new SimpleObject(), new SimpleObject() });
    writeObject(new SpecialObject());
    writeObject(new SpecialObject[] { new SpecialObject() });
    writeObject(new SpecialObject[] { new SpecialObject(), new SpecialObject() });
    writeObject(new SpecialObject[] { new SpecialObject(), new SpecialObject(), new SpecialObject() });
    writeObject(new int[] { 1 });
    writeObject(new int[] { 1, 2 });
    writeObject(new int[] { 1, 2, 3 });
    writeSimpleSerializable(new SerializableObject());
  }

  private static void writeObject(Object o) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream output = new ObjectOutputStream(byteArrayOutputStream);
    output.writeObject(o);
    output.flush();
    System.out.println(o.getClass().getSimpleName() + byteArrayOutputStream.toByteArray().length);
  }

  private static void writeSimpleSerializable(SimpleSerializable o) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DeflaterOutputStream deflateOutput = new DeflaterOutputStream(byteArrayOutputStream);
    
    SimpleSerializer.write(deflateOutput, o);
    deflateOutput.finish();
    System.out.println(o.getClass().getSimpleName() + byteArrayOutputStream.toByteArray().length);
  }
}

class SimpleObject implements Serializable {

  private static final long serialVersionUID = -3989239984559388644L;

  private int               mIntenger;                               // 4
  private float             mFloat;                                  // 4
  private byte              mByte;                                   // 1

  public int getIntenger() {
    return mIntenger;
  }

  public void setIntenger(int intenger) {
    mIntenger = intenger;
  }

  public float getFloat() {
    return mFloat;
  }

  public void setFloat(float f) {
    mFloat = f;
  }

  public byte getByte() {
    return mByte;
  }

  public void setByte(byte b) {
    mByte = b;
  }
}

class SpecialObject implements Serializable {

  private static final long serialVersionUID = 5675999350876668400L;

  private void writeObject(java.io.ObjectOutputStream out) throws IOException {
    // out.write(0);
  }

  private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.read();
  }

}

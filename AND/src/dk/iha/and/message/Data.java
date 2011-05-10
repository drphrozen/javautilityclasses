package dk.iha.and.message;

import java.nio.ByteBuffer;
import java.text.ParseException;

public interface Data {
  void setData(ByteBuffer bb) throws ParseException;

  float[] getValues();
}

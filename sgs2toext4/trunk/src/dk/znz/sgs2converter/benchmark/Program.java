package dk.znz.sgs2converter.benchmark;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import dk.znz.sgs2converter.StopWatch;


public class Program {

  /**
   * @param args
   * @throws IOException 
   * @throws FileNotFoundException 
   */
  public static void main(String[] args) throws FileNotFoundException, IOException {
    for(int i = 1; i<32; i++) {
      System.out.println(i + ": " + benchmark(i*1024*1024));
    }
  }
  
  private static StopWatch benchmark(int size) throws IOException {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    RandomAccessFile in = new RandomAccessFile("C:\\Users\\drphrozen\\Downloads\\Odin3-v1.85\\I9100XWKE2_I9100XXKDJ_I9100OXXKE2\\factoryfs.img", "r");
    FileChannel out = new RandomAccessFile("C:\\Users\\drphrozen\\Downloads\\Odin3-v1.85\\I9100XWKE2_I9100XXKDJ_I9100OXXKE2\\factoryfs.img.out", "rw").getChannel();
    MappedByteBuffer buffer = in.getChannel().map(MapMode.READ_ONLY, 0, in.length());
    while(buffer.hasRemaining()) {
      ByteBuffer slice = buffer.slice();
      int newLimit = slice.position() + size;
      if(newLimit > slice.capacity()) {
        buffer.position(buffer.position() + slice.remaining());
        newLimit = slice.capacity();
      } else {
        buffer.position(buffer.position() + size);
      }
      slice.limit(newLimit);
      out.write(slice);
    }
    out.close();
    stopWatch.stop();
    return stopWatch;
  }

}

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Program {

  static final long UINT32_MASK = 0xffffffffl;

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    String inputFilename = args[0];
    String outputFilename = args[1];
    CountInputStream countInputStream = new CountInputStream(new BufferedInputStream(new FileInputStream(inputFilename)));
    DataInputStream in = new DataInputStream(countInputStream);
    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFilename)));
    for (int i = 0; i < 7; i++) {
      in.readInt();
    }
    int phase = 0;

    int b1, b2, b3 = 0;

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    while (true) {
      switch (phase) {
      case 0:
        try {
          b1 = in.readInt();
        } catch (EOFException e) {
          stopWatch.stop();
          System.out.println("Done in " + stopWatch);
          return;
        }
        if (b1 == 0xC1CA0000) {
          b2 = in.readInt();
          b3 = in.readInt();
          phase = 1;
        } else if (b1 == 0xC3CA0000) {
          b2 = in.readInt();
          b3 = in.readInt();
          if (b2 != 0) {
            long k = (Integer.reverseBytes(b2) & UINT32_MASK) * 1024 - 1;
            System.out.format("Write %10s zeros.. %s%n", Long.toString(k * 4), countInputStream.getSpeed());
            b3 = 0;
            for (int i = 0; i < k; i++) {
              out.writeInt(0);
            }
          }
        } else {
          System.out.println("Error header.");
          return;
        }
        break;
      case 1:
        int b = Integer.reverseBytes(b3);
        long k = b & UINT32_MASK;
        k = ((k - 12) >> 2);
        System.out.format("Copy  %10s bytes.. %s%n", Long.toString(k*4), countInputStream.getSpeed());
        for (int i = 0; i < k; i++) {
          b1 = in.readInt();
          out.writeInt(b1);
        }
        phase = 0;
        break;
      }
    }
  }
}

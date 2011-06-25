import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

class CountInputStream extends FilterInputStream {
  private long            mPosition;
  private long            mLastPosition;
  private final StopWatch mStopWatch = new StopWatch();

  public CountInputStream(InputStream in) {
    super(in);
    mStopWatch.start();
  }

  public String getSpeed() {
    mStopWatch.restart();
    long elapsed = mStopWatch.getElapsedTime();
    String out = elapsed == 0 ? "N/A" : humanReadableByteCount(((mPosition - mLastPosition) * 1000) / elapsed, false);
    mLastPosition = mPosition;
    return out;
  }

  @Override public int read() throws IOException {
    mPosition++;
    return super.read();
  }

  public long getPosition() {
    return mPosition;
  }
  
  private String humanReadableByteCount(long bytes, boolean si) {
    int unit = si ? 1000 : 1024;
    if (bytes < unit) return bytes + " B";
    int exp = (int) (Math.log(bytes) / Math.log(unit));
    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
    return String.format("%.1f %sB/s", bytes / Math.pow(unit, exp), pre);
  }
}
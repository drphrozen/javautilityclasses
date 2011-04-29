package dk.znz.util.concurrent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * One shot counting latch
 * @author drphrozen
 *
 */
public class CountingLatch {
  private final AtomicInteger mValue;
  private final int mTargetValue;
  private final CountDownLatch mCountDownLatch = new CountDownLatch(1);
  
  public CountingLatch(int initialValue, int targetValue) {
    mValue = new AtomicInteger(initialValue);
    mTargetValue = targetValue;
  }
  
  public void await() throws InterruptedException {
    mCountDownLatch.await();
  }
  
  public void increment() {
    if(mValue.incrementAndGet() == mTargetValue)
      mCountDownLatch.countDown();
  }

  public void decrement() {
    if(mValue.decrementAndGet() == mTargetValue)
      mCountDownLatch.countDown();
  }
}

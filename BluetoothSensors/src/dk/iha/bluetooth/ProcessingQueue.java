package dk.iha.bluetooth;

import java.util.concurrent.ArrayBlockingQueue;

public class ProcessingQueue<Input, Output> {
  private final ArrayBlockingQueue<Output> free;
  private final ArrayBlockingQueue<Output> queued;
  private final Convert<Input, Output> convert;
  private final Thread thread;
  private volatile boolean isRunning;

  public ProcessingQueue(Convert<Input, Output> convert, final Process<Output> process, Output... values) {
    this.convert = convert;
    this.thread = new Thread(new Runnable() {
      @Override public void run() {
        isRunning = true;
        while(isRunning) {
          try {
            Output out = ProcessingQueue.this.queued.take();
            try {
              process.process(out);
            } finally {
              try {
                free.put(out);
              } catch (InterruptedException e) {
                throw new RuntimeException("Internal error, contact admin!", e);
              }
            }
          } catch (InterruptedException e) {
            isRunning = false;
          }
        }
      }
    });
    thread.start();
    free = new ArrayBlockingQueue<Output>(values.length);
    queued = new ArrayBlockingQueue<Output>(values.length);
    for (Output t : values) {
      free.add(t);
    }
  }
  
  public boolean process(Input in) {
    Output out = free.poll();
    if(out == null) return false;
    try {
      convert.convert(in, out);
    } finally {
      try {
        queued.put(out);
      } catch (InterruptedException e) {
        throw new RuntimeException("Internal error, contact admin!", e);
      }
    }
    return true;
  }
  
  public void close(long millis) {
    if(thread.isAlive() == false) return;
    isRunning = false;
    try {
      thread.join(millis);
    } catch (InterruptedException e) {
    }
    if(thread.isAlive())
      thread.interrupt();
  }
  
  public interface Convert<Input, Output> {
    void convert(Input in, Output out);
  }

  public interface Process<Output> {
    void process(Output out);
  }
}

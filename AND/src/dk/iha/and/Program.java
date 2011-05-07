package dk.iha.and;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;


public class Program {
  public static void main(String[] args) throws IOException {
  }

  
  public class SimpleServer {

    private volatile boolean mIsRunning; 
    private volatile Thread mServerThread;

    public synchronized void start() throws IOException {
      if(mIsRunning)
        return;
      LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);
      final SerialConnection serialConnection = new SerialConnection();
      final ExecutorService executorService = Executors.newFixedThreadPool(10);
      mIsRunning = true;
      
      mServerThread = new Thread(new Runnable() {
        @Override public void run() {
          try {
            while(mIsRunning) {
              executorService.execute(createClientHandler(serialConnection.acceptAndOpen()));
            }
          } catch (IOException e) {
            e.printStackTrace();
          } finally {
            try {
              serialConnection.close();
            } catch (IOException e) {
              e.printStackTrace();
            } finally {
              mIsRunning = false;              
            }
          }
        }

        private Runnable createClientHandler(final StreamConnection clientConnection) {
          return new Runnable() {
            @Override public void run() {
              try {
                DataInputStream in = clientConnection.openDataInputStream();
                DataOutputStream out = clientConnection.openDataOutputStream();
                PbtReader pbtReader = new PbtReader();
                while(mIsRunning) {
                  pbtReader.readData(in);
                  
                }
              } catch (IOException e) {
                e.printStackTrace();
              } finally {
                try {
                  clientConnection.close();
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            }
          };
        }
      });
    }
    
    public synchronized void stop() {
      if(mIsRunning == false)
        return;
      mIsRunning = false;
      mServerThread.interrupt();
      mServerThread = null;
    }
        
    private class SerialConnection implements Connection, StreamConnectionNotifier {
      private final String serviceURL = "btspp://localhost:6b901b2b3c5e432094b007f61089052f;name=PWAccessP";
      private final Connection mConnection;
      private final StreamConnectionNotifier mNotifier;
      
      public SerialConnection() throws IOException {
        Object o = Connector.open(serviceURL);
        mConnection = (Connection)o;
        mNotifier = (StreamConnectionNotifier)o;        
      }
      
      @Override public StreamConnection acceptAndOpen() throws IOException {
        return mNotifier.acceptAndOpen();
      }

      @Override public void close() throws IOException {
        mConnection.close();
      }
      
    }
  }
}

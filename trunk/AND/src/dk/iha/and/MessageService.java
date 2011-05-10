package dk.iha.and;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import dk.iha.and.message.Message;

public class MessageService {
  private static final String     PASSWORD            = "39121440";
  private static final String     SERIAL_PORT_PROFILE = new UUID("1101", true).toString();

  private volatile boolean        mIsRunning;
  private volatile Thread         mServerThread;
  private final OnMessageListener mOnPbtDataListener;

  public MessageService(OnMessageListener onDataListener) {
    if (onDataListener == null)
      throw new NullPointerException();
    mOnPbtDataListener = onDataListener;
  }

  public synchronized void start() throws IOException {
    if (mIsRunning)
      return;
    LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);
    final SerialConnection serialConnection = new SerialConnection();
    final ExecutorService executorService = Executors.newFixedThreadPool(10);
    mIsRunning = true;

    mServerThread = new Thread(new Runnable() {
      @Override public void run() {
        System.out.println("Server running, waiting for clients..");
        try {
          while (mIsRunning) {
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
        System.out.println("Server shutdown..");
      }

      private Runnable createClientHandler(final StreamConnection clientConnection) {
        return new Runnable() {

          private final byte[] DATA_REFUSED                = new byte[] { 'P', 'W', 'A', '0' };
          private final byte[] DATA_ACCEPTED               = new byte[] { 'P', 'W', 'A', '1' };
          private final byte[] DATA_ACCEPTED_AND_CONFIGURE = new byte[] { 'P', 'W', 'A', '2' };

          @Override public void run() {
            System.out.println("Client connected..");
            try {
              InputStream in = clientConnection.openInputStream();
              OutputStream out = clientConnection.openOutputStream();
              Message pbtReader = new Message();
              pbtReader.readData(in);
              long start = System.currentTimeMillis();
              boolean result;
              try {
                result = mOnPbtDataListener.onMessage(pbtReader);
              } catch (Exception ex) {
                result = false;
                ex.printStackTrace();
              }
              long sleepFor = 250 - (System.currentTimeMillis() - start);
              if (sleepFor > 0)
                Thread.sleep(sleepFor);
              out.write(result ? DATA_ACCEPTED : DATA_REFUSED);
//              byte[] tmp = new byte[256];
//              in.read(tmp);
//              if (tmp[0] == 'P' && tmp[1] == 'W' && tmp[2] == 'R' && tmp[3] == 'Q' && tmp[4] == 'C' && tmp[5] == 'F') {
//                out.write(Configuration.createConfiguration("PWAccessP", true));
//                in.read(tmp);
//                if(tmp[3] != '1')
//                  throw new IOException("Configuration failed");
//              }
            } catch (IOException e) {
              e.printStackTrace();
            } catch (InterruptedException e) {
              e.printStackTrace();
            } finally {
              try {
                clientConnection.close();
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
            System.out.println("Client disconnected..");
          }
        };
      }
    });
    mServerThread.start();
  }

  public synchronized void stop() {
    if (mIsRunning == false)
      return;
    mIsRunning = false;
    mServerThread.interrupt();
    mServerThread = null;
  }

  private class SerialConnection implements Connection, StreamConnectionNotifier {
    private final String                   serviceURL = "btspp://localhost:" + SERIAL_PORT_PROFILE + ";name=PWAccessP";
    private final Connection               mConnection;
    private final StreamConnectionNotifier mNotifier;

    public SerialConnection() throws IOException {
      Object o = Connector.open(serviceURL);
      mConnection = (Connection) o;
      mNotifier = (StreamConnectionNotifier) o;
    }

    @Override public StreamConnection acceptAndOpen() throws IOException {
      return mNotifier.acceptAndOpen();
    }

    @Override public void close() throws IOException {
      mConnection.close();
    }
  }
}
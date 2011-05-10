package dk.iha.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public interface Adapter {
  
  void setDiscoverable(boolean discoverable);
  
  public interface Socket {
    InputStream getInputStream();
    OutputStream getOutputStream();
  }
  
  public interface Service {
    void start(SocketListener listener, UUID uuid, String name) throws IOException;
    void stop() throws IOException;
  }
  
  public interface SocketListener {
    void onSocket(Socket client);
  }  
}
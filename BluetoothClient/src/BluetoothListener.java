import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;


public interface BluetoothListener {
    void onLocated(RemoteDevice remoteDevice);
    void onLocated(RemoteDevice remoteDevice, ServiceRecord... serviceRecords);
    void onFinished();
  }
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;


public class BluetoothFindService implements DiscoveryListener {

    private final LocalDevice mLocalDevice;
    private final UUID mServiceUUID = new UUID("27c5492aaf3344b7ab3a25916827c1d1", false);
    private final UUID[] mServiceUUIDs = new UUID[] { mServiceUUID }; 
    private final DiscoveryAgent mDiscoveryAgent;
    private final BluetoothListener mBluetoothListener;
    private final AtomicInteger mCounter = new AtomicInteger(-1);
	private final ConcurrentHashMap<Integer, RemoteDevice> mRemoteDevices = new ConcurrentHashMap<Integer, RemoteDevice>(); 

    public BluetoothFindService(BluetoothListener onLocated) throws BluetoothStateException {
      mLocalDevice = LocalDevice.getLocalDevice();
      mDiscoveryAgent = mLocalDevice.getDiscoveryAgent();
      mBluetoothListener = onLocated;
    }

    public void find() throws BluetoothStateException {
        mCounter.set(0);
        mRemoteDevices.clear();
		mDiscoveryAgent.startInquiry(DiscoveryAgent.GIAC, this);
    }

    
    @Override public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
    	startServiceSearch(btDevice);
    }
    
    private void startServiceSearch(RemoteDevice btDevice) {
        increment();
        mBluetoothListener.onLocated(btDevice);
        try {
          int transID = mDiscoveryAgent.searchServices(null, mServiceUUIDs, btDevice, this);
          mRemoteDevices.put(transID, btDevice);
        } catch (BluetoothStateException e) {
          e.printStackTrace();
        }
    }

    @Override public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
    	mBluetoothListener.onLocated(mRemoteDevices.get(transID), servRecord);
    }

    @Override public void serviceSearchCompleted(int transID, int respCode) {
      decrement();
    }

    @Override public void inquiryCompleted(int discType) {
      decrement();
      System.out.println("inquiryCompleted");
    }
    private void increment() {
      mCounter.incrementAndGet();
    }
    
    private void decrement() {
      if(mCounter.decrementAndGet() == -1)
        mBluetoothListener.onFinished();
    }

	public void shutdown() {
		mLocalDevice.getDiscoveryAgent().cancelInquiry(this);
	}
  }
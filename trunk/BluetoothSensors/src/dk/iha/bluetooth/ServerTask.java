package dk.iha.bluetooth;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import dk.iha.bluetooth.ConnectionTask.OnStopListener;

public class ServerTask extends AsyncTask<Void, String, IOException> {

  public static final UUID SERVICE_UUID = UUID.fromString("27c5492a-af33-44b7-ab3a-25916827c1d1");

  private final SensorManager mSensorManager;
  
  private BluetoothAdapter mAdapter;
  private BluetoothServerSocket mServerSocket;
  private IOException mResult = null;
  private final Set<ConnectionTask> mConnectionTasks = Collections.synchronizedSet(new HashSet<ConnectionTask>(10));
  
  public ServerTask(SensorManager sensorManager) {
    mSensorManager = sensorManager;
  }
  
  protected void onPreExecute() {
    mAdapter = BluetoothAdapter.getDefaultAdapter();
    try {
        mServerSocket = mAdapter.listenUsingRfcommWithServiceRecord("AndroidSensors", SERVICE_UUID);
    } catch (IOException e) {
      mResult = e;
      e.printStackTrace();
    }
  };
  
  @Override protected IOException doInBackground(Void... params) {
    while(isCancelled() == false || mResult != null) {
      try {
        BluetoothSocket clientSocket =  mServerSocket.accept();
        ConnectionTask connectionTask = new ConnectionTask(clientSocket, mSensorManager, new OnStopListener() {
          @Override public void onStop() {
            mConnectionTasks.remove(mConnectionTasks);
          }
        });
        mConnectionTasks.add(connectionTask);
        connectionTask.execute();
      } catch (IOException e) {
        e.printStackTrace();
        return e;
      }
    }
    return mResult;
  }
  
  public void onStop() {
    try {
      if(mServerSocket != null)
        mServerSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (ConnectionTask connectionTask : mConnectionTasks) {
      connectionTask.cancel(true);
    }
  }
  
  @Override protected void onCancelled() {
    onStop();
  }
  
  @Override protected void onPostExecute(IOException result) {
    onStop();
  }  
}

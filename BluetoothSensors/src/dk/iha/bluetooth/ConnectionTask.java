package dk.iha.bluetooth;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;

import android.bluetooth.BluetoothSocket;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;

public class ConnectionTask extends AsyncTask<Void, Void, IOException> implements SensorEventListener {
  private final BluetoothSocket mClientSocket;
  private final SensorManager mSensorManager;
  private final int QUEUE_SIZE = 10;
  private final ArrayBlockingQueue<PDU> mUnusedQueue = new ArrayBlockingQueue<PDU>(QUEUE_SIZE);
  private final ArrayBlockingQueue<PDU> mOutgoingQueue = new ArrayBlockingQueue<PDU>(QUEUE_SIZE);
  private final OnStopListener mOnStopListener;
  
  public ConnectionTask(BluetoothSocket clientSocket, SensorManager sensorManager, OnStopListener onStopListener) {
    mClientSocket = clientSocket;
    mSensorManager = sensorManager;
    mOnStopListener = onStopListener;
    for (int i = 0; i < QUEUE_SIZE; i++) {
      mUnusedQueue.add(new PDU());
    }
  }
  
  @Override protected void onPreExecute() {
    Sensor mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
  }
  
  @Override protected void onPostExecute(IOException result) {
    super.onPostExecute(result);
    onStop();
  }
  
  @Override protected void onCancelled() {
    super.onCancelled();
    onStop();
  }
  
  private void onStop() {
    mSensorManager.unregisterListener(this);
    if(mOnStopListener != null)
      mOnStopListener.onStop();
  }

  @Override protected IOException doInBackground(Void... params) {
    try {
      OutputStream out = mClientSocket.getOutputStream();
      while(isCancelled() == false) {
        PDU pdu = mOutgoingQueue.take();
        out.write(pdu.data);
        mUnusedQueue.put(pdu);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
      return e;
    }
    return null;
  }

  @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}

  @Override public void onSensorChanged(SensorEvent event) {
    PDU pdu = mUnusedQueue.poll();
    if(pdu == null) return;
    pdu.setData(event);
    try {
      mOutgoingQueue.put(pdu);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static class PDU {
    public final byte[] data = new byte[8 + 4 + 1 + 1 + 4*4];
    private final ByteBuffer mByteBuffer = ByteBuffer.wrap(data);
    
    public void setData(SensorEvent event) {
      mByteBuffer.putLong(0, event.timestamp);
      mByteBuffer.putInt(8, event.sensor.getType());
      mByteBuffer.put(12, (byte)event.accuracy);
      mByteBuffer.put(13, (byte)event.values.length);
      for (int i = 0; i < event.values.length; i++) {
        mByteBuffer.putFloat(14 + i, event.values[i]);
      }
    }
  }
  
  public interface OnStopListener {
    void onStop();
  }
}

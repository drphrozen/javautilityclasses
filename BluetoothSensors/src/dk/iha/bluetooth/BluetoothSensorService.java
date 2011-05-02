package dk.iha.bluetooth;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BluetoothSensorService extends Service {

  private static final String REQUEST_CODE = "REQUEST_CODE";
  private static final int REQUEST_CODE_SHUTDOWN = 0;
  private static final int NOTIFICATION_ID = 0;
  private static Notification mNotification;
  private BluetoothBroadcastReceiver mBluetoothBroadcastReceiver;
  private NotificationManager mNotificationManager;
  private BluetoothAdapter mBluetoothAdapter;
  

  @Override public void onCreate() {
    super.onCreate();
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if (mBluetoothAdapter == null) {
      Toast.makeText(this, "This program requires a Bluetooth adapter to work.", Toast.LENGTH_LONG).show();
      stopSelf();
      return;
    }
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d("BSS", "onStartCommand");
    int requestCode = intent.getIntExtra(REQUEST_CODE, -1);
    switch (requestCode) {
    case REQUEST_CODE_SHUTDOWN:
      stopSelf();
      return START_NOT_STICKY;
    default:
      mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);    
      mNotification = new Notification(R.drawable.icon, "Bluetooth Service started", System.currentTimeMillis());
      Intent contentIntent = new Intent(this, BluetoothSensorService.class);
      contentIntent.putExtra(REQUEST_CODE, REQUEST_CODE_SHUTDOWN);
      startForeground(NOTIFICATION_ID, mNotification);

      mBluetoothBroadcastReceiver = new BluetoothBroadcastReceiver();
      mBluetoothBroadcastReceiver.start();

      if (mBluetoothAdapter.isEnabled() == false) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), Intent.FLAG_ACTIVITY_NEW_TASK);
        mNotification.setLatestEventInfo(this, "Bluetooth is disabled", "Click to enable bluetooth.", pendingIntent);
      } else {
        startListening();
      }
      break;
    }
    return START_STICKY;
  }

  @Override public IBinder onBind(Intent intent) {
    Log.d("BSS", "onBind");
    return null;
  }
  
  @Override public void onDestroy() {
    super.onDestroy();
    Log.d("BSS", "onDestroy");
  }

  private void startListening() {
    Intent intent = new Intent(this, BluetoothSensorService.class);
    intent.putExtra(REQUEST_CODE, REQUEST_CODE_SHUTDOWN);
    PendingIntent pendingIntent = PendingIntent.getService(this, 0, new Intent(this, BluetoothSensorService.class), 0);
    mNotification.setLatestEventInfo(this, "Running", "Waiting for and handling connections.", pendingIntent);
  }
  
  private void shutdown() {
    mNotificationManager.cancel(NOTIFICATION_ID);
  }

  private class BluetoothBroadcastReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
      if (intent.getAction() != BluetoothAdapter.ACTION_STATE_CHANGED)
        return;
      Bundle b = intent.getExtras();
      switch (b.getInt(BluetoothAdapter.EXTRA_STATE)) {
      case BluetoothAdapter.STATE_TURNING_OFF:
        shutdown();
        break;
      case BluetoothAdapter.STATE_ON:
        startListening();
        break;
      }
    }

    public void start() {
      IntentFilter actionStateChanged = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
      registerReceiver(this, actionStateChanged);
    }

    public void stop() {
      unregisterReceiver(this);
    }
  }
}

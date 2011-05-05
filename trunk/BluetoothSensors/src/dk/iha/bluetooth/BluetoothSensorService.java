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

  public static final String         REQUEST_CODE          = "REQUEST_CODE";
  public static final int            REQUEST_CODE_START    = 0;
  public static final int            REQUEST_CODE_SHUTDOWN = 1;
  public static final int            REQUEST_CODE_ACTIVITY = 2;

  private enum States {
    START,
    WAIT_FOR_BLUETOOTH,
    RUNNING,
    SHUTDOWN,
    NO_BLUETOOTH
  }
  
  private enum Events {
    BLUETOOTH_STARTED,
    BLUETOOTH_STOPPING,
    SHUTDOWN_REQUESTED,
    START_REQUESTED
  }
  
  private States mCurrentState;
  
  private static final int           NOTIFICATION_ID       = 0;
  private static Notification        mNotification;

  private BluetoothBroadcastReceiver mBluetoothBroadcastReceiver;
  private NotificationManager        mNotificationManager;
  private BluetoothAdapter           mBluetoothAdapter;

  @Override public void onCreate() {
    super.onCreate();
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if (mBluetoothAdapter == null) {
      enterState(States.NO_BLUETOOTH);
      return;
    }
    
    mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    mNotification = new Notification(R.drawable.notification_icon, "Bluetooth Service started", System.currentTimeMillis());
    Intent contentIntent = new Intent(this, BluetoothSensorService.class);
    contentIntent.putExtra(REQUEST_CODE, REQUEST_CODE_ACTIVITY);
    mNotification.setLatestEventInfo(this, "Bluetooth Sensor Service", "Started", PendingIntent.getService(this, 0, contentIntent, 0));
    mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    
    mCurrentState = States.SHUTDOWN;
  }

  @Override public IBinder onBind(Intent intent) {
    Log.d("BSS", "onBind");
    return null;
  }

  @Override public void onDestroy() {
    if(mCurrentState != States.SHUTDOWN)
      enterState(States.SHUTDOWN);
  }
  
  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    if(intent == null) return START_STICKY;
    int requestCode = intent.getIntExtra(REQUEST_CODE, -1);
    switch(requestCode) {
    case REQUEST_CODE_START:
      handleEvent(Events.START_REQUESTED);
      break;
    case REQUEST_CODE_SHUTDOWN:
      handleEvent(Events.SHUTDOWN_REQUESTED);
      break;
    }
    return START_STICKY;
  }
  
  private void handleEvent(Events event) {
    Log.w("CurrentEvent", event.toString() + ", " + mCurrentState.toString());
    switch(mCurrentState) {
    case RUNNING:
      if(event == Events.SHUTDOWN_REQUESTED || event == Events.BLUETOOTH_STOPPING)
        enterState(States.SHUTDOWN);
      break;
    case SHUTDOWN:
      if(event == Events.START_REQUESTED)
        enterState(States.START);
      break;
    case NO_BLUETOOTH:
      if(event == Events.START_REQUESTED) {
        Toast.makeText(this, "This program requires a Bluetooth adapter to work.", Toast.LENGTH_LONG).show();
        stopSelf();
      }
      break;
    case WAIT_FOR_BLUETOOTH:
      if(event == Events.BLUETOOTH_STARTED)
        enterState(States.RUNNING);
      break;
    }
  }
  
  private void enterState(States newState) {
    Log.w("CurrentState", String.format("%s -> %s", mCurrentState.toString(), newState.toString()));
    switch (newState) {
    case RUNNING:
      Intent intent = new Intent(this, BluetoothSensorService.class);
      intent.putExtra(REQUEST_CODE, REQUEST_CODE_ACTIVITY);
      PendingIntent pendingIntent = PendingIntent.getService(this, 0, new Intent(this, BluetoothSensorService.class), 0);
      mNotification.iconLevel = 1;
      mNotification.setLatestEventInfo(this, "Running", "Waiting for and handling connections.", pendingIntent);
      mNotificationManager.notify(NOTIFICATION_ID, mNotification);
      //TODO: Start service thread
      break;
      
    case SHUTDOWN:
      mNotificationManager.cancel(NOTIFICATION_ID);
      mBluetoothBroadcastReceiver.stop();
      stopSelf();
      break;
      
    case START:
      mBluetoothBroadcastReceiver = new BluetoothBroadcastReceiver();
      mBluetoothBroadcastReceiver.start();
      if (mBluetoothAdapter.isEnabled() == false) {
        PendingIntent pendingRequesrBluetoothIntent = PendingIntent.getActivity(this, 0, new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), Intent.FLAG_ACTIVITY_NEW_TASK);
        mNotification.setLatestEventInfo(this, "Bluetooth is disabled", "Click to enable bluetooth.", pendingRequesrBluetoothIntent);
        mNotification.iconLevel = 2;
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        newState = States.WAIT_FOR_BLUETOOTH;
      } else {
        enterState(States.RUNNING);
      }
      break;
    }
    mCurrentState = newState;
  }

  private class BluetoothBroadcastReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
      if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED) == false)
        return;
      Bundle b = intent.getExtras();
      switch (b.getInt(BluetoothAdapter.EXTRA_STATE)) {
      case BluetoothAdapter.STATE_TURNING_OFF:
        handleEvent(Events.BLUETOOTH_STOPPING);
        break;
      case BluetoothAdapter.STATE_ON:
        handleEvent(Events.BLUETOOTH_STARTED);
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

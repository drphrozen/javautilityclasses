package dk.iha.bluetooth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class BluetoothSensorActivity extends Activity {

  /** Called when the activity is first created. */
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    startService(new Intent(this, BluetoothSensorService.class));
    Log.i("BSS", "Start");
  }
}
package dk.au.perpos.tailing.agent;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import dk.au.perpos.android.PerPos;

public class TailingAgent extends Activity {
	private PerPos perpos = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		int camelID;
		if(savedInstanceState != null) {
			camelID = savedInstanceState.getInt(STATE_CAMELID);
			CharSequence port = savedInstanceState.getCharSequence(STATE_PORT);
			if(port != null)
				((EditText) findViewById(R.id.EditText01)).setText(port);
		} else {
      // Restore preferences
      SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
			camelID = settings.getInt(STATE_CAMELID, 0);
			CharSequence port = settings.getString(STATE_PORT, null);
			if(port != null)
				((EditText) findViewById(R.id.EditText01)).setText(port);
		}
		
		// SPINNER
		final Spinner s = (Spinner) findViewById(R.id.Spinner01);
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            this, R.array.camels, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    s.setAdapter(adapter);
    try {
    	s.setSelection(camelID);
    } catch(Exception ex) {
    	
    }
		
    // BUTTON
		findViewById(R.id.Button01).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				final String hostName;
				final int port;

				String portString = ((EditText) findViewById(R.id.EditText01)).getText().toString();
				hostName = (CharSequence)s.getSelectedItem() + ".cs.au.dk";
				int tmpPort = -1;
				try {
					tmpPort= Integer.parseInt(portString); 
				} catch (NumberFormatException e) {
					Toast.makeText(TailingAgent.this, "Port could not be parsed!", Toast.LENGTH_LONG);
					return;
				}
				port = tmpPort;

				perpos = new PerPos(TailingAgent.this, hostName, port);
//				SensingService service = perpos.getSensingService();
				
				perpos.startGPS();
				perpos.startRelay();
//				perpos.startWiFi();
				
//				MeasurementProducer<Measurement> producer = null;
//				Sensor sensor = perpos.getSensingService().findFirstSensor(
//						new Any());
//				if (sensor != null) {
//					producer = perpos.getSensingService().getProducer(sensor);
//					producer.addConsumer(new Consumer<Measurement>() {
//
//						public void consume(Measurement m) {
//							if (m instanceof WiFiMeasurement) {
//								WiFiMeasurement wm = (WiFiMeasurement) m;
//								toast("Received WiFi measurement [x: "
//										+ wm.getX() + ", y: " + wm.getY()
//										+ ", z: " + wm.getZ() + ", symID: "
//										+ wm.getSymbolicID() + ", time: "
//										+ wm.getTimeOfMeasurement() + "]");
//							} else if (m instanceof GPSMeasurement) {
//								GPSMeasurement gm = (GPSMeasurement) m;
//								toast("Received GPS measurement [lat: "
//										+ gm.getPosition().getLatitude()
//										+ ", lon: "
//										+ gm.getPosition().getLongitude()
//										+ ", time: "
//										+ gm.getTimeOfMeasurement() + "]");
//							} else {
//								toast("Unknown measurement "
//										+ m.getTimeOfMeasurement());
//							}
//						}
//					});
//				}
			}
		});
	}

	protected void toast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT);
	}
	
	
	private static final String PREFS_NAME = "TailingAgentPrefsFile";
	private static final String STATE_CAMELID = "camelID"; 
	private static final String STATE_PORT = "port"; 
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_CAMELID, ((Spinner) findViewById(R.id.Spinner01)).getSelectedItemPosition());
		outState.putCharSequence(STATE_PORT, ((EditText) findViewById(R.id.EditText01)).getText());
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		((Spinner) findViewById(R.id.Spinner01)).setSelection(savedInstanceState.getInt(STATE_CAMELID));
		((EditText) findViewById(R.id.EditText01)).setText(savedInstanceState.getCharSequence(STATE_PORT));
	}

	@Override
	protected void onStop() {
		super.onStop();
		
    // Save user preferences. We need an Editor object to
    // make changes. All objects are from android.context.Context
    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putInt(STATE_CAMELID, ((Spinner) findViewById(R.id.Spinner01)).getSelectedItemPosition());
    editor.putString(STATE_PORT, ((EditText) findViewById(R.id.EditText01)).getText().toString());
		
    // Don't forget to commit your edits!!!
    editor.commit();
	}
	
	@Override
	public void onBackPressed() {
		if(perpos != null)
			perpos.shutdown();
		finish();
	}
	
}
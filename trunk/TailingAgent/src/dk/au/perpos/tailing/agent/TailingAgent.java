package dk.au.perpos.tailing.agent;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import dk.au.perpos.android.PerPos;

public class TailingAgent extends Activity {
	
	private PerPos perpos = null;
	private WakeLock wl;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TailingAgent.class.getName() + "WakeLock");
		wl.acquire();
		
		int camelID;
		if(savedInstanceState != null) {
			camelID = savedInstanceState.getInt(STATE_CAMELID);
			CharSequence port = savedInstanceState.getCharSequence(STATE_PORT);
			if(port != null)
				((EditText) findViewById(R.id.EditTextPort)).setText(port);
		} else {
      // Restore preferences
      SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
			camelID = settings.getInt(STATE_CAMELID, 0);
			CharSequence port = settings.getString(STATE_PORT, null);
			if(port != null)
				((EditText) findViewById(R.id.EditTextPort)).setText(port);
		}
		
		// SPINNER
		final Spinner s = (Spinner) findViewById(R.id.SpinnerCamel);
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            this, R.array.camels, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    s.setAdapter(adapter);
    try {
    	s.setSelection(camelID);
    } catch(Exception ex) {
    	
    }
		
    
    findViewById(R.id.Button5).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				updateTargetSeen(5);
			}
		});
    
    findViewById(R.id.Button25).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				updateTargetSeen(25);
			}
		});
    
    findViewById(R.id.Button50).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				updateTargetSeen(50);
			}
		});
    
    findViewById(R.id.Button75).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				updateTargetSeen(75);
			}
		});
    
    findViewById(R.id.Button100).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				updateTargetSeen(100);
			}
		});
    
    findViewById(R.id.ButtonGone).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				updateTargetSeen(-1);
			}
		});
    
    // Connect BUTTON
		findViewById(R.id.ButtonConnect).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				final String hostName;
				final int port;

				String portString = ((EditText) findViewById(R.id.EditTextPort)).getText().toString();
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
				perpos.startGPS();
				perpos.startRelay();
			}
		});
	}
	
	private static void updateTargetSeen(int distance) {
		
	}

	protected void toast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT);
	}
	
	
	private static final String PREFS_NAME = "TailingAgentPrefsFile";
	private static final String STATE_CAMELID = "camelID"; 
	private static final String STATE_PORT = "port"; 
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_CAMELID, ((Spinner) findViewById(R.id.SpinnerCamel)).getSelectedItemPosition());
		outState.putCharSequence(STATE_PORT, ((EditText) findViewById(R.id.EditTextPort)).getText());
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		((Spinner) findViewById(R.id.SpinnerCamel)).setSelection(savedInstanceState.getInt(STATE_CAMELID));
		((EditText) findViewById(R.id.EditTextPort)).setText(savedInstanceState.getCharSequence(STATE_PORT));
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		wl.release();

		
    // Save user preferences. We need an Editor object to
    // make changes. All objects are from android.context.Context
    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putInt(STATE_CAMELID, ((Spinner) findViewById(R.id.SpinnerCamel)).getSelectedItemPosition());
    editor.putString(STATE_PORT, ((EditText) findViewById(R.id.EditTextPort)).getText().toString());
		
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
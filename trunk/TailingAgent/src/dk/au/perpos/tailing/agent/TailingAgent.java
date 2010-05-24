package dk.au.perpos.tailing.agent;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import dk.au.perpos.android.PerPos;
import dk.au.perpos.tailing.TailingAgent.ServerMessage;
import dk.au.perpos.tailing.TailingAgent.TargetSeen;

public class TailingAgent extends Activity implements LocationListener {
	
	private static final String PREFS_NAME = "TailingAgentPrefsFile";
	private static final String STATE_CAMELID = "camelID"; 
	private static final String STATE_PORT = "port"; 
	
	private PerPos perpos = null;
	private Socket socket = null;
	private WakeLock wl;
	private Spinner spinnerCamel;
	private ServerProtocol targetSeenSender = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		spinnerCamel = (Spinner) findViewById(R.id.SpinnerCamel);
		
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TailingAgent.class.getName() + "WakeLock");
		wl.acquire();
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
		
    // Restore preferences
    SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    int camelID = settings.getInt(STATE_CAMELID, 0);
		CharSequence portString = settings.getString(STATE_PORT, null);
		if(portString != null)
			((EditText) findViewById(R.id.EditTextPort)).setText(portString);
		
		// SPINNER
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            this, R.array.camels, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerCamel.setAdapter(adapter);
    try {
    	spinnerCamel.setSelection(camelID);
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
		final String hostName = getHostName();
		final int port = getPort();
		
		findViewById(R.id.ButtonConnect).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String imei = ((TelephonyManager) TailingAgent.this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
				try {
					targetSeenSender = new ServerProtocol(hostName, port, imei);
					new Thread(targetSeenSender).start();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
//				perpos = new PerPos(TailingAgent.this, hostName, port);				
//				perpos.startGPS();
//				perpos.startRelay();
			}
		});
	}
	
	private String getHostName() {
		return (CharSequence)spinnerCamel.getSelectedItem() + ".cs.au.dk";
	}
	
	private int getPort() {
		String portString = ((EditText) findViewById(R.id.EditTextPort)).getText().toString();
		int tmpPort = -1;
		try {
			tmpPort = Integer.parseInt(portString); 
		} catch (NumberFormatException e) {
			Toast.makeText(TailingAgent.this, "Port could not be parsed!", Toast.LENGTH_LONG);
		}
		return tmpPort;
	}
	
	private void updateTargetSeen(int distance) {
		if(socket == null) {
			toast("Update unsuccessful: Not connected!");
			return;
		}
		try {
			ServerMessage.newBuilder()
				.setTargetSeen(TargetSeen.newBuilder()
					.setDirection(1.0f)
					.setDistance(distance)
				.build())
			.build().writeDelimitedTo(socket.getOutputStream());
		} catch (IOException e) {
			socket = null;
			toast(e.getMessage());
			e.printStackTrace();
		}
	}

	protected void toast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT);
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
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.removeUpdates(this);
		finish();
	}

	public void onLocationChanged(Location location) {
		if(targetSeenSender != null)
			targetSeenSender.sendLocation(location);
	}

	public void onProviderDisabled(String provider) {
		
	}

	public void onProviderEnabled(String provider) {
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}
}
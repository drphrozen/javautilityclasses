package dk.iha.nonin;

import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class Monitor extends Activity {

	private TextView mLogTextView;
	private BluetoothAdapter mBluetoothAdapter;
	private AsyncTask<BluetoothSocket, Format8, Throwable> mTask;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mLogTextView = (TextView) findViewById(R.id.logTextView);

		if (mTask == null) {
			try {
				BluetoothSocket socket = openBluetoothSocket();
				mTask = createAsyncTask();
				mTask.execute(socket);
			} catch (IOException e) {
				e.printStackTrace();
				setError(e.getMessage());
			}
		}
	}

	private BluetoothSocket openBluetoothSocket() throws IOException {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			setError(R.string.error_no_bluetooth);
			return null;
		}

		BluetoothDevice device;
		if((device = findNonin()) == null) {
			setError(R.string.error_no_pairs);
			return null;
		}

		return device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
	}

	private BluetoothDevice findNonin() {
		for (BluetoothDevice device : mBluetoothAdapter.getBondedDevices()) {
			if (device.getName().startsWith("Nonin_Medical_Inc._"))
				return device;
		}
		return null;
	}
	
	private void setError(int id) {
		mLogTextView.setText(getResources().getText(id));
	}
	
	private void setError(String message) {
		mLogTextView.setText(message);
	}
		
	private AsyncTask<BluetoothSocket, Format8, Throwable> createAsyncTask() {
		return new AsyncTask<BluetoothSocket, Format8, Throwable>() {
			@Override
			protected Throwable doInBackground(BluetoothSocket... params) {
				NoninConnector nonin;
				try {
					nonin = new NoninConnector(params[0].getInputStream(), params[0].getOutputStream());
				} catch (IOException e) {
					return e;
				}
				while(isCancelled() == false) {
					try {
						publishProgress(nonin.read());
					} catch (IOException e) {
						return e;
					}
				}
				return null;
			}
		};
	}
}
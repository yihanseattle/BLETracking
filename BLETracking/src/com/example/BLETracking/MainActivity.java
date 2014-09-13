package com.example.BLETracking;

import java.util.ArrayList;

import com.example.seniordesignv3.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class MainActivity extends Activity {
	// Initialize instances of classes (objects)
	public LeDeviceListAdapter mLeDeviceListAdapter;
	private BluetoothAdapter mBluetoothAdapter;
	private boolean mScanning;
	public Handler mHandler;
	private Menu _menu = null;
	public static ArrayList<String> logArray = new ArrayList();
	public boolean notificationSent = false;

	public static ArrayList<String> getLogArray() {
		return logArray;
	}

	private boolean emptyItem[] = new boolean[] { true, true, true, true, true };

	// Declare constants
	private static final int REQUEST_ENABLE_BT = 1;
	// Stops scanning after 10 seconds.
	private static final long SCAN_PERIOD = 10000;

	private Button b1, b2, b3, b4, b5, b6;
	private Handler h;
	public static String[] s = new String[] { "0", "1", "2", "3", "4" };

	public BluetoothGatt mConnectedGatt1;
	public BluetoothGatt mConnectedGatt2;
	public BluetoothGatt mConnectedGatt3;
	public BluetoothGatt mConnectedGatt4;
	public BluetoothGatt mConnectedGatt5;

	private ListView lv;

	public void newActivity(View v) {
		Intent intent = new Intent(this, BlackActivity.class);
		startActivity(intent);
	}

	private Time today = new Time(Time.getCurrentTimezone());

	View.OnClickListener myOnlyhandler = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bRSSI1:
				// it was the first button
				if (mConnectedGatt1 != null) {
					mConnectedGatt1.disconnect();
					b1.setText("");
					emptyItem[0] = true;
				}
				break;
			case R.id.bRSSI2:
				// it was the second button
				if (mConnectedGatt2 != null) {
					mConnectedGatt2.disconnect();
					b2.setText("");
					emptyItem[1] = true;
				}
				break;
			case R.id.bRSSI3:
				// it was the second button
				if (mConnectedGatt3 != null) {
					mConnectedGatt3.disconnect();
					b3.setText("");
					emptyItem[2] = true;
				}
				break;
			case R.id.bRSSI4:
				// it was the second button
				if (mConnectedGatt4 != null) {
					mConnectedGatt4.disconnect();
					b4.setText("");
					emptyItem[3] = true;
				}
				break;
			case R.id.bRSSI5:
				// it was the second button
				if (mConnectedGatt5 != null) {
					mConnectedGatt5.disconnect();
					b5.setText("");
					emptyItem[4] = true;
				}
				break;
			}
		}
	};

	// Code that is executed when app starts
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("lifecycle", "onCreate was created");

		getActionBar().setTitle(R.string.title_devices); // Header title
		setContentView(R.layout.home_layout);
		mHandler = new Handler();
		mLeDeviceListAdapter = new LeDeviceListAdapter();

		h = new Handler();
		b1 = (Button) findViewById(R.id.bRSSI1);
		b2 = (Button) findViewById(R.id.bRSSI2);
		b3 = (Button) findViewById(R.id.bRSSI3);
		b4 = (Button) findViewById(R.id.bRSSI4);
		b5 = (Button) findViewById(R.id.bRSSI5);
		b6 = (Button) findViewById(R.id.bNewActivity);
		b6.setBackgroundColor(Color.BLACK);
		b1.setOnClickListener(myOnlyhandler);
		b2.setOnClickListener(myOnlyhandler);
		b3.setOnClickListener(myOnlyhandler);
		b4.setOnClickListener(myOnlyhandler);
		b5.setOnClickListener(myOnlyhandler);

		lv = (ListView) findViewById(R.id.list);
		lv.setAdapter(mLeDeviceListAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Log.d("lifecycle", "onListItemClick was created");
				final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
				for (int i = 0; i < emptyItem.length; i++) {
					if (emptyItem[i] == true && i == 0) {
						mConnectedGatt1 = device.connectGatt(getApplicationContext(), false, mGattCallback1);
						mHandler.post(firstSensorTagRunnable);
						emptyItem[i] = false;
						break;
					} else if (emptyItem[i] == true && i == 1) {
						mConnectedGatt2 = device.connectGatt(getApplicationContext(), false, mGattCallback2);
						mHandler.post(secondSensorTagRunnable);
						emptyItem[i] = false;
						break;
					} else if (emptyItem[i] == true && i == 2) {
						mConnectedGatt3 = device.connectGatt(getApplicationContext(), false, mGattCallback3);
						mHandler.post(thirdSensorTagRunnable);
						emptyItem[i] = false;
						break;
					} else if (emptyItem[i] == true && i == 3) {
						mConnectedGatt4 = device.connectGatt(getApplicationContext(), false, mGattCallback4);
						mHandler.post(fourthSensorTagRunnable);
						emptyItem[i] = false;
						break;
					} else if (emptyItem[i] == true && i == 4) {
						mConnectedGatt5 = device.connectGatt(getApplicationContext(), false, mGattCallback5);
						mHandler.post(fifthSensorTagRunnable);
						emptyItem[i] = false;
						break;
					}
				}
				// mLeConnectedDeviceListAdapter.addDevice(device);
				mBluetoothAdapter.stopLeScan(mLeScanCallback);
				_menu.findItem(R.id.menu_stop).setVisible(false);
				_menu.findItem(R.id.menu_scan).setVisible(true);
				// displays the item as its title
				_menu.findItem(R.id.menu_refresh).setActionView(null);
				mLeDeviceListAdapter.mLeDevices.remove(position);
				lv.setAdapter(mLeDeviceListAdapter);
			}

		});
		// Use this check to determine whether BLE is supported on the device.
		// Then you can
		// selectively disable BLE-related features.
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
			finish();
		}

		// Initializes a Bluetooth adapter. For API level 18 and above, get a
		// reference to
		// BluetoothAdapter through BluetoothManager.
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();

		// Checks if Bluetooth is supported on the device.
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
	}

	/**
	 * onCreateOptionsMenu called before onCreate finishes
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d("lifecycle", "onCreateOptionsMenu was created");
		getMenuInflater().inflate(R.menu.main, menu);
		_menu = menu;
		if (!mScanning) { // Where is value of mScanning initialized?
			menu.findItem(R.id.menu_stop).setVisible(false); // android:showAsAction="ifRoom|withText"
			menu.findItem(R.id.menu_scan).setVisible(true); // displays the item
															// as its title
			menu.findItem(R.id.menu_refresh).setActionView(null);
		} else {
			menu.findItem(R.id.menu_stop).setVisible(true);
			menu.findItem(R.id.menu_scan).setVisible(false);
			menu.findItem(R.id.menu_refresh).setActionView(R.layout.actionbar_indeterminate_progress);
		}
		return true;
	}

	public static String[] getStringArray() {
		return s;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("lifecycle", "onOptionsItemSelected was created");
		switch (item.getItemId()) {
		case R.id.menu_scan:
			mLeDeviceListAdapter.clear();
			lv = (ListView) findViewById(R.id.list);
			lv.setAdapter(mLeDeviceListAdapter);

			scanLeDevice(true);
			break;
		case R.id.menu_stop:
			scanLeDevice(false);
			break;
		}
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("lifecycle", "onResume was created");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("lifecycle", "onActivityResult was created");
		// User chose not to enable Bluetooth.
		if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
			finish();
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("lifecycle", "onPause was created");
		scanLeDevice(false);
		mLeDeviceListAdapter.clear();

	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d("lifecycle", "onStop was created");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d("lifecycle", "onRestart was created");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("lifecycle", "onDestroy was created");
	}

	private void firstSensorTagRSSI() {
		mConnectedGatt1.readRemoteRssi();
		mHandler.postDelayed(firstSensorTagRunnable, 2000);
	}

	private void secondSensorTagRSSI() {
		mConnectedGatt2.readRemoteRssi();
		mHandler.postDelayed(secondSensorTagRunnable, 2000);
	}

	private void thirdSensorTagRSSI() {
		mConnectedGatt3.readRemoteRssi();
		mHandler.postDelayed(thirdSensorTagRunnable, 2000);
	}

	private void fourthSensorTagRSSI() {
		mConnectedGatt4.readRemoteRssi();
		mHandler.postDelayed(fourthSensorTagRunnable, 2000);
	}

	private void fifthSensorTagRSSI() {
		mConnectedGatt5.readRemoteRssi();
		mHandler.postDelayed(fifthSensorTagRunnable, 2000);
	}

	private void updateProximity(int rssi, int devicenumber) {
		// show notification to the user
	
		if (rssi <= -70) {
			// double num = rssiD - 80;
			// int Green = (int) Math.round(num * 12.75);
			// mProximity.setText(String.format("%.0f", (float)rssi));
			// mProximity.setTextColor(Color.rgb(255, 0, 0));
	
			if (rssi <= -80 && !notificationSent) {
				showNotification();
				notificationSent = true;
				today.setToNow();
				logArray.add("Device " + devicenumber + " alert at " + today.format("%k:%M:%S") + " " + rssi);
			}
		}
	
		else if (rssi <= -50) {
			// double num = rssiD - 60;
			// int Red = (int) Math.round(num * 12.75);
			// mProximity.setText(String.format("%.0f", (float)rssi));
			// mProximity.setTextColor(Color.rgb(255, 205, 0));
			notificationSent = false;
		} else {
			// mProximity.setText(String.format("%.0f", (float)rssi));
			// mProximity.setTextColor(Color.rgb(0, 255, 0));
		}
	}

	private void showNotification() {
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrate for 1000 milliseconds
		v.vibrate(1000);
	
		MediaPlayer mp = MediaPlayer.create(this, R.raw.out_of_range_notification);
		mp.start();
	
	}

	static class ViewHolder {
		TextView deviceName;
		TextView deviceAddress;
	}
	// Adapter for holding devices found through scanning.
	public class LeDeviceListAdapter extends BaseAdapter {
	
		private ArrayList<BluetoothDevice> mLeDevices;
		private LayoutInflater mInflator;
	
		public LeDeviceListAdapter() {
			super();
			Log.d("lifecycle", "LeDeviceListAdapter was created");
			mLeDevices = new ArrayList<BluetoothDevice>();
			mInflator = MainActivity.this.getLayoutInflater();
		}
	
		public void addDevice(BluetoothDevice device) {
			if (!mLeDevices.contains(device)) {
				mLeDevices.add(device);
			}
		}
	
		public BluetoothDevice getDevice(int position) {
			return mLeDevices.get(position);
		}
	
		public void clear() {
			mLeDevices.clear();
		}
	
		@Override
		public int getCount() {
			return mLeDevices.size();
		}
	
		@Override
		public Object getItem(int i) {
			return mLeDevices.get(i);
		}
	
		@Override
		public long getItemId(int i) {
			return i;
		}
	
		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			ViewHolder viewHolder;
			Log.d("lifecycle", "getView was executed");
			// General ListView optimization code.
			if (view == null) {
				view = mInflator.inflate(R.layout.listitem_device, null);
				viewHolder = new ViewHolder();
				viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
				viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
	
			BluetoothDevice device = mLeDevices.get(i);
			final String deviceName = device.getName();
			if (deviceName != null && deviceName.length() > 0)
				viewHolder.deviceName.setText(deviceName);
			else
				viewHolder.deviceName.setText(R.string.unknown_device);
			viewHolder.deviceAddress.setText(device.getAddress());
	
			return view;
		}
	}
	private void scanLeDevice(final boolean enable) {
		Log.d("lifecycle", "scanLeDevice was created");
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
					invalidateOptionsMenu();
				}
			}, SCAN_PERIOD);
	
			mScanning = true;
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
		invalidateOptionsMenu();
	}

	// Device scan callback.
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
	
		@Override
		public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
			Log.d("lifecycle", "onLeScan was executed");
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mLeDeviceListAdapter.addDevice(device);
					mLeDeviceListAdapter.notifyDataSetChanged();
				}
			});
		}
	};
	private BluetoothGattCallback mGattCallback1 = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			// Connection established
			if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
				Log.d("lifecycle", "mGattCallback1 success & connected");
	
				// Discover services
				gatt.discoverServices();
	
			} else if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_DISCONNECTED) {
	
				// Handle a disconnect event
				Log.d("lifecycle", "mGattCallback1 success & disconnected");
				today.setToNow();
				logArray.add("Device 1 disconnected at " + today.format("%k:%M:%S"));
				notificationSent = false;
	
			}
		}
	
		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			super.onReadRemoteRssi(gatt, rssi, status);
			s[0] = rssi + "";
			h.post(renderingFirstTv);
			Log.d("RSSI_testing", "1st " + rssi);
			updateProximity(rssi, 1);
		}
	};
	private BluetoothGattCallback mGattCallback2 = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			// Connection established
			if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
				Log.d("lifecycle", "mGattCallback2 success & connected");
	
				// Discover services
				gatt.discoverServices();
	
			} else if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_DISCONNECTED) {
	
				// Handle a disconnect event
				Log.d("lifecycle", "mGattCallback2 success & disconnected");
				today.setToNow();
				logArray.add("Device 2 disconnected at " + today.format("%k:%M:%S"));
				notificationSent = false;
	
			}
		}
	
		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			super.onReadRemoteRssi(gatt, rssi, status);
			s[1] = rssi + "";
	
			h.post(renderingSecondTv);
			Log.d("RSSI", "2nd " + rssi);
			updateProximity(rssi, 2);
	
		}
	};
	private BluetoothGattCallback mGattCallback3 = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			// Connection established
			if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
				Log.d("lifecycle", "mGattCallback3 success & connected");
	
				// Discover services
				gatt.discoverServices();
	
			} else if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_DISCONNECTED) {
	
				// Handle a disconnect event
				Log.d("lifecycle", "mGattCallback3 success & disconnected");
				today.setToNow();
				logArray.add("Device 3 disconnected at " + today.format("%k:%M:%S"));
				notificationSent = false;
	
			}
		}
	
		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			super.onReadRemoteRssi(gatt, rssi, status);
			s[2] = rssi + "";
			h.post(renderingThirdTv);
			Log.d("RSSI", "3rd " + rssi);
			updateProximity(rssi, 3);
		}
	};
	private BluetoothGattCallback mGattCallback4 = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			// Connection established
			if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
				Log.d("lifecycle", "mGattCallback4 success & connected");
	
				// Discover services
				gatt.discoverServices();
	
			} else if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_DISCONNECTED) {
	
				// Handle a disconnect event
				Log.d("lifecycle", "mGattCallback4 success & disconnected");
				today.setToNow();
				logArray.add("Device 4 disconnected at " + today.format("%k:%M:%S"));
				notificationSent = false;
	
			}
		}
	
		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			super.onReadRemoteRssi(gatt, rssi, status);
			s[3] = rssi + "";
			h.post(renderingFourthTv);
			Log.d("RSSI", "4th " + rssi);
			updateProximity(rssi, 4);
		}
	};
	private BluetoothGattCallback mGattCallback5 = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			// Connection established
			if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
				Log.d("lifecycle", "mGattCallback5 success & connected");
	
				// Discover services
				gatt.discoverServices();
	
			} else if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_DISCONNECTED) {
	
				// Handle a disconnect event
				Log.d("lifecycle", "mGattCallback5 success & disconnected");
				today.setToNow();
				logArray.add("Device 5 disconnected at " + today.format("%k:%M:%S"));
	
			}
		}
	
		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			super.onReadRemoteRssi(gatt, rssi, status);
			s[4] = rssi + "";
			h.post(renderingFifthTv);
			Log.d("RSSI", "5th " + rssi);
			updateProximity(rssi, 5);
		}
	};
	Runnable firstSensorTagRunnable = new Runnable() {
		@Override
		public void run() {
			firstSensorTagRSSI();
	
			Log.d("RSSI", "Runnable 1 is executed");
		}
	};
	Runnable secondSensorTagRunnable = new Runnable() {
		@Override
		public void run() {
			secondSensorTagRSSI();
			Log.d("RSSI", "Runnable 2 is executed");
		}
	};

	Runnable thirdSensorTagRunnable = new Runnable() {
		@Override
		public void run() {
			thirdSensorTagRSSI();

			Log.d("RSSI", "Runnable 3 is executed");
		}
	};

	Runnable fourthSensorTagRunnable = new Runnable() {
		@Override
		public void run() {
			fourthSensorTagRSSI();

			Log.d("RSSI", "Runnable 4 is executed");
		}
	};

	Runnable fifthSensorTagRunnable = new Runnable() {
		@Override
		public void run() {
			fifthSensorTagRSSI();
	
			Log.d("RSSI", "Runnable 5 is executed");
		}
	};
	Runnable renderingFirstTv = new Runnable() {
	
		@Override
		public void run() {
			b1.setText(s[0] + "");
		}
	};
	Runnable renderingSecondTv = new Runnable() {
	
		@Override
		public void run() {
			b2.setText(s[1] + "");
		}
	};
	Runnable renderingThirdTv = new Runnable() {
	
		@Override
		public void run() {
			b3.setText(s[2] + "");
		}
	};
	Runnable renderingFourthTv = new Runnable() {
	
		@Override
		public void run() {
			b4.setText(s[3] + "");
		}
	};
	Runnable renderingFifthTv = new Runnable() {
	
		@Override
		public void run() {
			b5.setText(s[4] + "");
		}
	};

}
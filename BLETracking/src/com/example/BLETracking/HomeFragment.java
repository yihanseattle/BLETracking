package com.example.BLETracking;

import com.example.seniordesignv3.R;

import android.app.ListFragment;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class HomeFragment extends ListFragment {

	public static String[] s = new String[] { "0", "1", "2", "3", "4" };
	private Handler h;
	public BluetoothGatt mConnectedGatt1;

	public BluetoothGatt mConnectedGatt2;

	private TextView t1, t2, t3, t4, t5;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("lifecycle", "HomeFragment onCreateView was executed");
		return inflater.inflate(R.layout.home_layout, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d("lifecycle", "HomeFragment onActivityCreated was executed");
		h = new Handler();
		setListAdapter(((MainActivity) getActivity()).mLeDeviceListAdapter);
	}

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
	
			}
		}
	
		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			// TODO Auto-generated method stub
			super.onReadRemoteRssi(gatt, rssi, status);
			s[0] = rssi + "";
			h.post(renderingFirstTv);
			Log.d("RSSI", "1st " + rssi);
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
	
			}
		}
	
		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			super.onReadRemoteRssi(gatt, rssi, status);
			s[1] = rssi + "";
	
			h.post(renderingSecondTv);
			Log.d("RSSI", "2nd " + rssi);
		}
	};
	Runnable renderingFirstTv = new Runnable() {

		@Override
		public void run() {
			t1.setText(s[0] + "");
		}
	};
	Runnable renderingSecondTv = new Runnable() {

		@Override
		public void run() {
			t2.setText(s[1] + "");
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

	private void firstSensorTagRSSI() {
		mConnectedGatt1.readRemoteRssi();
		((MainActivity) getActivity()).mHandler.postDelayed(firstSensorTagRunnable, 2000);
	}

	private void secondSensorTagRSSI() {
		mConnectedGatt2.readRemoteRssi();
		((MainActivity) getActivity()).mHandler.postDelayed(secondSensorTagRunnable, 2000);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.d("lifecycle", "onListItemClick was created");
		final BluetoothDevice device = ((MainActivity) getActivity()).mLeDeviceListAdapter.getDevice(position);
		if (position == 0) {
			mConnectedGatt1 = device.connectGatt(getActivity(), false, mGattCallback1);
			((MainActivity) getActivity()).mHandler.post(firstSensorTagRunnable);

		} else if (position == 1) {
			mConnectedGatt2 = device.connectGatt(getActivity(), false, mGattCallback2);
			((MainActivity) getActivity()).mHandler.post(secondSensorTagRunnable);
		}
	}

	public static String[] getStringArray() {
		return s;
	}

}

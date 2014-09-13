package com.example.BLETracking;

import java.util.ArrayList;

import com.example.seniordesignv3.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BlackActivity extends Activity {

	private ListView lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setTitle(R.string.title_activity_black); // Header title
		setContentView(R.layout.fragment_black);
		lv=(ListView)findViewById(R.id.lvlog);
		ArrayList<String> temp = MainActivity.getLogArray();
		lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,temp));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.black, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

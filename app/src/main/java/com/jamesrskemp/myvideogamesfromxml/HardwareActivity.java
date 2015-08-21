package com.jamesrskemp.myvideogamesfromxml;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;


public class HardwareActivity extends ActionBarActivity {
	private final static String TAG = HardwareActivity.class.getName();

	private List<VideoGameHardware> videoGameHardware = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hardware);

		// TODO possibly add filtering later
		parseData();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_hardware, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void parseData() {
		String xmlFileName = "videogames.xml";
		String filePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath()  + "/" + xmlFileName;
		try {
			File file = new File(filePath);
			InputStream fis = null;
			fis = new BufferedInputStream(new FileInputStream(file));
			// TODO everything - includes creating xml parser, list adapter

		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
			Log.e(TAG, "Exception", ex);
		}
	}
}

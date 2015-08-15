package com.jamesrskemp.myvideogamesfromxml;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class DownloadsActivity extends ActionBarActivity {
	private final static String TAG = DownloadsActivity.class.getName();

	private static final String DL_ID = "downloadId";
	private SharedPreferences preferences;
	private DownloadManager downloadManager;

	private boolean isReceiverRegistered = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_downloads);

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_downloads, menu);
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

	@Override
	protected void onPause() {
		super.onPause();

		if (isReceiverRegistered) {
			unregisterReceiver(broadcastReceiver);
			isReceiverRegistered = false;
		}
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			queryDownloadStatus();
		}
	};

	private void queryDownloadStatus() {
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(preferences.getLong(DL_ID, 0));
		Cursor cursor = downloadManager.query(query);

		if (cursor.moveToFirst()) {
			int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
			Log.d(TAG, "Status Check: " + status);
			switch(status) {
				case DownloadManager.STATUS_PAUSED:
				case DownloadManager.STATUS_PENDING:
				case DownloadManager.STATUS_RUNNING:
					break;
				case DownloadManager.STATUS_SUCCESSFUL:
					Toast.makeText(DownloadsActivity.this, "File downloaded", Toast.LENGTH_SHORT).show();
					try {
						cleanupDownloadedFiles();
						Log.d(TAG, "Local file name: " + cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case DownloadManager.STATUS_FAILED:
					downloadManager.remove(preferences.getLong(DL_ID, 0));
					preferences.edit().clear().commit();
					break;
			}
		}
	}

	public void downloadHardwareXml(View view) {
		if (!isReceiverRegistered) {
			registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
			isReceiverRegistered = true;
		}

		Uri resource = Uri.parse("http://media.jamesrskemp.com/xml/videogames.xml");

		DownloadManager.Request request = new DownloadManager.Request(resource);
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
		request.setAllowedOverRoaming(false);
		request.setTitle("Video Game Hardware XML");
		request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "videogames.xml");

		long id = downloadManager.enqueue(request);
		preferences.edit().putLong(DL_ID, id).commit();
	}

	public void downloadGamesXml(View view) {
		if (!isReceiverRegistered) {
			registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
			isReceiverRegistered = true;
		}

		Uri resource = Uri.parse("http://media.jamesrskemp.com/xml/video_games.xml");

		DownloadManager.Request request = new DownloadManager.Request(resource);
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
		request.setAllowedOverRoaming(false);
		request.setTitle("Video Game Games XML");
		request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "video_games.xml");

		long id = downloadManager.enqueue(request);
		preferences.edit().putLong(DL_ID, id).commit();
	}

	private void cleanupDownloadedFiles() {
		try {
			File[] otherFiles = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).listFiles();

			ArrayList<File> gameFiles = new ArrayList<File>();
			ArrayList<File> hardwareFiles = new ArrayList<File>();

			for (File file : otherFiles) {
				if (file.getName().startsWith("video_games")) {
					gameFiles.add(file);
				} else if (file.getName().startsWith("videogames")) {
					hardwareFiles.add(file);
				}
			}

			if (gameFiles.size() > 1) {
				Collections.sort(gameFiles, new DownloadsComparator());
				File lastFile = gameFiles.get(gameFiles.size() - 1);

				Iterator<File> iterator = gameFiles.iterator();
				while (iterator.hasNext()) {
					File file = iterator.next();
					if (!file.getName().equals(lastFile.getName())) {
						file.delete();
					} else {
						Log.d(TAG, "Path: " + file.getPath());
						file.renameTo(new File(file.getPath().replace(file.getName(), "video_games.xml")));
					}
				}
			}

			if (hardwareFiles.size() > 1) {
				Collections.sort(hardwareFiles, new DownloadsComparator());
				File lastFile = hardwareFiles.get(hardwareFiles.size() - 1);

				Iterator<File> iterator = hardwareFiles.iterator();
				while (iterator.hasNext()) {
					File file = iterator.next();
					if (!file.getName().equals(lastFile.getName())) {
						file.delete();
					} else {
						Log.d(TAG, "Path: " + file.getPath());
						file.renameTo(new File(file.getPath().replace(file.getName(), "videogames.xml")));
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

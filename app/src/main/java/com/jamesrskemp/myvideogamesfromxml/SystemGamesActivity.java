package com.jamesrskemp.myvideogamesfromxml;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;


public class SystemGamesActivity extends ActionBarActivity {
	private List<VideoGame> videoGames = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_games);

		parseData();
	}

	public void parseData() {
		String xmlFileName = "video_games.xml";
		String filePath = Environment.getExternalStorageDirectory() + "/Download/" + xmlFileName;
		try {
			File file = new File(filePath);
			InputStream fis = new BufferedInputStream(new FileInputStream(file));
			VideoGameXmlParser parser = new VideoGameXmlParser();

			videoGames = parser.parse(fis);
			Toast.makeText(this, "Video games: " + videoGames.size(), Toast.LENGTH_SHORT).show();

			SystemGameComparer comparer = new SystemGameComparer();

			Collections.sort(videoGames, comparer);

			ArrayAdapter<VideoGame> gamesAdapter = new GameSelectionListAdapter(this, videoGames);

			ListView listView = (ListView)findViewById(R.id.expand_list_games);
			listView.setAdapter(gamesAdapter);
		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
			Log.e("GamesActivity", "Exception", ex);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_system_games, menu);
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
}

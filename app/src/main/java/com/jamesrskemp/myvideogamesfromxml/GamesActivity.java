package com.jamesrskemp.myvideogamesfromxml;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;


public class GamesActivity extends ActionBarActivity {

	private List<VideoGame> videoGames = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_games);

		EditText searchText = (EditText)findViewById(R.id.edit_text_games_search);

		searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				Toast.makeText(getApplicationContext(), "Button pushed: " + actionId, Toast.LENGTH_SHORT).show();
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					Toast.makeText(getApplicationContext(), "Search button pushed.", Toast.LENGTH_SHORT).show();
					filterData();
				}
				return true;
			}
		});

		/*searchText.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				Toast.makeText(getApplicationContext(), "Button pushed: " + keyCode, Toast.LENGTH_SHORT).show();
				if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
					Toast.makeText(getApplicationContext(), "Search button pushed.", Toast.LENGTH_SHORT).show();
					filterData();
				}
				return true;
			}
		});*/

		parseData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_games, menu);
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
		String xmlFileName = "video_games.xml";
		String filePath = Environment.getExternalStorageDirectory() + "/Download/" + xmlFileName;
		try {
			File file = new File(filePath);
			InputStream fis = null;
			fis = new BufferedInputStream(new FileInputStream(file));
			VideoGameXmlParser parser = new VideoGameXmlParser();

			videoGames = parser.parse(fis);
			Toast.makeText(this, "Video games: " + videoGames.size(), Toast.LENGTH_SHORT).show();

			VideoGameComparer comparer = new VideoGameComparer();

			Collections.sort(videoGames, comparer);

			ArrayAdapter<VideoGame> gamesAdapter = new GameSelectionListAdapter(this, videoGames);

			ListView listView = (ListView)findViewById(R.id.list_games);
			listView.setAdapter(gamesAdapter);
		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
			Log.e("GamesActivity", "Exception", ex);
		}
	}

	// TODO http://stackoverflow.com/questions/14663725/list-view-filter-android
	public void filterData() {
		Toast.makeText(getApplicationContext(), "Filtering data.", Toast.LENGTH_SHORT).show();

		CharSequence filter = ((TextView)findViewById(R.id.edit_text_games_search)).getText();

		VideoGameComparer comparer = new VideoGameComparer();
		Collections.sort(videoGames, comparer);
		ArrayAdapter<VideoGame> gamesAdapter = null;
		gamesAdapter = new GameSelectionListAdapter(this, videoGames);
		ListView listView = (ListView)findViewById(R.id.list_games);
		listView.setAdapter(gamesAdapter);

		//if (!filter.equals("")) {
			Log.d("GamesActivity", filter.toString());
			gamesAdapter.getFilter().filter(filter);

			Toast.makeText(getApplicationContext(), listView.getAdapter().getCount() + " results found", Toast.LENGTH_SHORT).show();
		//}
	}
}

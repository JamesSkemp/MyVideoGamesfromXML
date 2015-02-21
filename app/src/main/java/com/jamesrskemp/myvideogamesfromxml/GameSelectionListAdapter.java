package com.jamesrskemp.myvideogamesfromxml;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 1/24/2015.
 */
public class GameSelectionListAdapter extends ArrayAdapter<VideoGame> implements Filterable {
	private List<VideoGame> videoGames = null;
	private List<VideoGame> originalVideoGames = null;
	Context context;

	public GameSelectionListAdapter(Context context, List<VideoGame> viewModel) {
		super(context, 0, viewModel);
		originalVideoGames = viewModel;
		videoGames = viewModel;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		VideoGame item = getItem(position);

		// Check if an existing view is being reused, otherwise inflate the view.
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_game_selection, parent, false);
		}

		TextView title = (TextView) convertView.findViewById(R.id.list_game_selection_title);
		title.setText(item.title);

		TextView system = (TextView) convertView.findViewById(R.id.list_game_selection_system);
		system.setText(item.system());

		title.append(" (Own: " + item.own + ")");

		TextView purchase = (TextView) convertView.findViewById(R.id.list_game_selection_purchase);
		purchase.setText(item.purchaseDate + " " + item.purchasePrice + " " + item.purchasePlace);

		TextView notes = (TextView) convertView.findViewById(R.id.list_game_selection_notes);
		notes.setText(item.notes);

		return convertView;
	}

	@Override
	public int getCount() {
		return videoGames.size();
	}

	@Override
	public VideoGame getItem(int position) {
		return videoGames.get(position);
	}

	@Override
	public Filter getFilter() {
		return new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				String filter = constraint.toString().toLowerCase();

				FilterResults results = new FilterResults();
				final ArrayList<VideoGame> foundVideoGames = new ArrayList<VideoGame>(originalVideoGames.size());

				VideoGame videoGameChecking;
				for (int i = 0; i < originalVideoGames.size(); i++) {
					videoGameChecking = originalVideoGames.get(i);
					if (videoGameChecking.title.toLowerCase().contains(filter)) {
						foundVideoGames.add(videoGameChecking);
					}
				}

				Log.d("GamesActivity", String.valueOf(foundVideoGames.size()));

				results.values = foundVideoGames;
				results.count = foundVideoGames.size();

				return results;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				if (results.count == 0) {
					notifyDataSetInvalidated();
				} else {
					videoGames = (List<VideoGame>)results.values;
					notifyDataSetChanged();
				}
			}
		};
	}
}
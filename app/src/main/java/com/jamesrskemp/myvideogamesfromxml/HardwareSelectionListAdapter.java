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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 8/22/2015.
 */
public class HardwareSelectionListAdapter extends ArrayAdapter<VideoGameHardware> implements Filterable {
	private final static String TAG = HardwareSelectionListAdapter.class.getName();

	private List<VideoGameHardware> hardware = null;
	private List<VideoGameHardware> originalHardware = null;
	Context context;

	public HardwareSelectionListAdapter(Context context, List<VideoGameHardware> viewModel) {
		super(context, 0, viewModel);
		originalHardware = viewModel;
		hardware = viewModel;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		VideoGameHardware item = getItem(position);

		// Check if an existing view is being reused, otherwise inflate the view.
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_hardware_selection, parent, false);
		}

		TextView title = (TextView) convertView.findViewById(R.id.list_hardware_selection_name);
		title.setText(item.name);

		TextView system = (TextView) convertView.findViewById(R.id.list_hardware_selection_system);
		system.setText(item.system());
		if (system.getText().length() == 0) {
			//system.setVisibility(View.GONE);
		}

		title.append(" (Own: " + item.own + ")");

		TextView purchase = (TextView) convertView.findViewById(R.id.list_hardware_selection_purchase);
		purchase.setText(
				String.format("%s %s %s",
						item.purchaseDate == null ? "" : item.purchaseDate,
						item.purchasePrice == null ? "" : item.purchasePrice,
						item.purchasePlace == null ? "" : item.purchasePlace).trim());

		TextView notes = (TextView) convertView.findViewById(R.id.list_hardware_selection_notes);
		notes.setText(item.notes.trim());

		return convertView;
	}

	@Override
	public int getCount() {
		return hardware.size();
	}

	@Override
	public VideoGameHardware getItem(int position) {
		return hardware.get(position);
	}

	@Override
	public Filter getFilter() {
		return new Filter() {
			@Override
			protected Filter.FilterResults performFiltering(CharSequence constraint) {
				String filter = constraint.toString().toLowerCase();

				FilterResults results = new FilterResults();
				final ArrayList<VideoGameHardware> foundHardware = new ArrayList<VideoGameHardware>(originalHardware.size());

				VideoGameHardware hardwareChecking;
				for (int i = 0; i < originalHardware.size(); i++) {
					hardwareChecking = originalHardware.get(i);
					if (hardwareChecking.name.toLowerCase().contains(filter)) {
						foundHardware.add(hardwareChecking);
					}
				}

				Log.d(TAG, String.valueOf(foundHardware.size()));

				results.values = foundHardware;
				results.count = foundHardware.size();

				return results;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				if (results.count == 0) {
					notifyDataSetInvalidated();
				} else {
					hardware = (List<VideoGameHardware>)results.values;
					notifyDataSetChanged();
				}
			}
		};
	}
}

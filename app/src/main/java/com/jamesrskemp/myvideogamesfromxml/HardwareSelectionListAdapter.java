package com.jamesrskemp.myvideogamesfromxml;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;

/**
 * Created by James on 8/22/2015.
 */
public class HardwareSelectionListAdapter extends ArrayAdapter<VideoGameHardware> implements Filterable {
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

		title.append(" (Own: " + item.own + ")");

		TextView purchase = (TextView) convertView.findViewById(R.id.list_hardware_selection_purchase);
		purchase.setText(item.purchaseDate + " " + item.purchasePrice + " " + item.purchasePlace);

		TextView notes = (TextView) convertView.findViewById(R.id.list_hardware_selection_notes);
		notes.setText(item.notes);

		return convertView;
	}
}

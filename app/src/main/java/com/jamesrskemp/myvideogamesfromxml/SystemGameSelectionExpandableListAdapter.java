package com.jamesrskemp.myvideogamesfromxml;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by James on 1/24/2015.
 */
public class SystemGameSelectionExpandableListAdapter extends BaseExpandableListAdapter {
	private List<String> listHeaders;
	private HashMap<String, List<String>> videoGames;
	Context context;

	public SystemGameSelectionExpandableListAdapter(Context context, List<String> listHeaders, HashMap<String, List<String>> listData) {
		this.context = context;
		this.listHeaders = listHeaders;
		this.videoGames = listData;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.videoGames.get(this.listHeaders.get(groupPosition)).size();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return this.videoGames.get(this.listHeaders.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getGroupCount() {
		return this.listHeaders.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		VideoGame item = (VideoGame)getChild(groupPosition, childPosition);

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.list_game_selection, parent, false);
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
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		String headerTitle = (String)getGroup(groupPosition);

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.list_systems_group, parent, false);
		}

		TextView headingSystem = (TextView)convertView.findViewById(R.id.list_systems_group_system);
		headingSystem.setText(headerTitle);

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	/*
	public SystemGameSelectionExpandableListAdapter(Context context, List<VideoGame> viewModel) {
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
	*/
}
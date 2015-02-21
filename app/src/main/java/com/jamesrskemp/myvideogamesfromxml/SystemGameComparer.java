package com.jamesrskemp.myvideogamesfromxml;

import java.util.Comparator;

/**
 * Created by James on 2/21/2015.
 */
public class SystemGameComparer implements Comparator<VideoGame> {
	@Override
	public int compare(VideoGame x, VideoGame y) {
		int value = x.system().compareTo(y.system());
		if (value == 0) {
			value = x.title.compareTo(y.title);
			if (value == 0) {
				value = x.purchaseDate.compareTo(y.purchaseDate);
			}
		}
		return value;
	}
}

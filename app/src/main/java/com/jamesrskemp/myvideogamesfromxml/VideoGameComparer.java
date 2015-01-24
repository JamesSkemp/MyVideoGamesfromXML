package com.jamesrskemp.myvideogamesfromxml;

import java.util.Comparator;

/**
 * Created by James on 1/24/2015.
 */
public class VideoGameComparer implements Comparator<VideoGame> {
	@Override
	public int compare(VideoGame x, VideoGame y) {
		int value = x.title.compareTo(y.title);
		if (value == 0) {
			value = x.system.compareTo(y.system);
		}
		return value;
	}
}

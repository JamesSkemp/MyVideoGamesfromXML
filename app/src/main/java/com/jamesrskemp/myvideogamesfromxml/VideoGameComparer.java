package com.jamesrskemp.myvideogamesfromxml;

import java.util.Comparator;

/**
 * Allows sorting of video games by title and then system.
 */
public class VideoGameComparer implements Comparator<VideoGame> {
	@Override
	public int compare(VideoGame x, VideoGame y) {
		int value = x.title.toLowerCase().compareTo(y.title.toLowerCase());
		if (value == 0) {
			value = x.system().compareTo(y.system());
		}
		return value;
	}
}

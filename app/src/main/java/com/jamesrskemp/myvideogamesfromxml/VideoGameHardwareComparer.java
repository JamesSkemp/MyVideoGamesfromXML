package com.jamesrskemp.myvideogamesfromxml;

import java.util.Comparator;

/**
 * Allows sorting of video game hardware by title and then system.
 */
public class VideoGameHardwareComparer implements Comparator<VideoGameHardware> {
	@Override
	public int compare(VideoGameHardware lhs, VideoGameHardware rhs) {
		int value = lhs.name.compareTo(rhs.name);
		if (value == 0) {
			value = lhs.system().compareTo(rhs.system());
		}
		return value;
	}
}

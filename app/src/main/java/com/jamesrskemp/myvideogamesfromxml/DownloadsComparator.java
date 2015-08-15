package com.jamesrskemp.myvideogamesfromxml;

import java.io.File;
import java.util.Comparator;

/**
 * Created by James on 8/15/2015.
 */
public class DownloadsComparator implements Comparator<File> {
	@Override
	public int compare(File lhs, File rhs) {
		long difference =lhs.lastModified() - rhs.lastModified();

		if (difference < Integer.MIN_VALUE || difference > Integer.MAX_VALUE) {
			return 0;
		}

		return (int) difference;
	}
}

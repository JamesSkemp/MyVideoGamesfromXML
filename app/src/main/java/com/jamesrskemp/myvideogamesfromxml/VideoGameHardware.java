package com.jamesrskemp.myvideogamesfromxml;

/**
 * Contains information about a particular piece of video game hardware.
 */
public class VideoGameHardware {
	public long id;
	public String name;
	public String systemConsole;
	public String systemVersion;
	public String own;
	public String notes;
	public String purchaseDate;
	public String purchasePrice;
	public String purchasePlace;

	public VideoGameHardware() {
	}

	public String system() {
		return String.format("%s %s", systemConsole == null ? "" : systemConsole, systemVersion == null ? "" : systemVersion).trim();
	}
}

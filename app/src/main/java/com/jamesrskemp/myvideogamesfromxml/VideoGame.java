package com.jamesrskemp.myvideogamesfromxml;

/**
 * Created by James on 1/24/2015.
 */
public class VideoGame {
	public long id;
	public String title;
	public String systemConsole;
	public String systemVersion;
	public String notes;
	public String own;
	public String purchaseDate;
	public String purchasePrice;
	public String purchasePlace;
	public String sellDate;
	public String sellPrice;
	public String sellPlace;
	public String addOn;
	public String electronic;
	public Boolean beat;

	public VideoGame() { }

	public String system() {
		return (systemConsole + " " + systemVersion).trim();
	}
}

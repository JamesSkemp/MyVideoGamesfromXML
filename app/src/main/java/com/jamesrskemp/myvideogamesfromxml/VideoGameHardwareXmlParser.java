package com.jamesrskemp.myvideogamesfromxml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 8/22/2015.
 */
public class VideoGameHardwareXmlParser {
	private static final String ns = null;

	public List<VideoGameHardware> parse(InputStream in) throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readFeed(parser);
		} finally {
			in.close();
		}
	}

	private List<VideoGameHardware> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
		List<VideoGameHardware> hardware = new ArrayList<VideoGameHardware>();

		parser.require(XmlPullParser.START_TAG, ns, "videoGames");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("hardware")) {
				while (parser.next() != XmlPullParser.END_TAG) {
					if (parser.getEventType() != XmlPullParser.START_TAG) {
						continue;
					}
					String childName = parser.getName();
					if (childName.equals("item")) {
						hardware.add(readHardware(parser));
					} else {
						skip(parser);
					}
				}
			} else {
				skip(parser);
			}
		}
		return hardware;
	}

	private void readSystem(XmlPullParser parser, VideoGameHardware game) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "system");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();
			if (name.equals("console")) {
				game.systemConsole = readStringElement(parser, "console");
			} else if (name.equals("version")) {
				game.systemVersion = readStringElement(parser, "version");
			} else {
				skip(parser);
			}
		}
	}

	private void readPurchase(XmlPullParser parser, VideoGameHardware game) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "purchase");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();
			if (name.equals("date")) {
				game.purchaseDate = readStringElement(parser, "date");
			} else if (name.equals("price")) {
				game.purchasePrice = readStringElement(parser, "price");
			} else if (name.equals("place")) {
				game.purchasePlace = readStringElement(parser, "place");
			} else {
				skip(parser);
			}
		}
	}

	private VideoGameHardware readHardware(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "item");
		VideoGameHardware hardware = new VideoGameHardware();

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() == XmlPullParser.END_TAG && parser.getName() == "item") {
				continue;
			}
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("name")) {
				hardware.name = readStringElement(parser, "name");
			/*} else if (name.equals("id")) {
				game.id = Long.parseLong(readStringElement(parser, "id"));*/
			} else if (name.equals("system")) {
				readSystem(parser, hardware);
			} else if (name.equals("purchase")) {
				readPurchase(parser, hardware);
			} else if (name.equals("own")) {
				hardware.own = readStringElement(parser, "own");
			} else if (name.equals("notes")) {
				hardware.notes = readStringElement(parser, "notes");
			} else {
				skip(parser);
			}
		}
		return hardware;
	}

	private String readStringElement(XmlPullParser parser, String elementName) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, elementName);
		String elementValue = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, elementName);
		return elementValue;
	}

	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
				case XmlPullParser.END_TAG:
					depth--;
					break;
				case XmlPullParser.START_TAG:
					depth++;
					break;
			}
		}
	}
}

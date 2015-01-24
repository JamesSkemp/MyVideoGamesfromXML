package com.jamesrskemp.myvideogamesfromxml;

import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 1/24/2015.
 */
public class VideoGameXmlParser {
	private static final String ns = null;

	public List<VideoGame> parse(InputStream in) throws XmlPullParserException, IOException {
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

	private List<VideoGame> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
		List<VideoGame> games = new ArrayList<VideoGame>();

		parser.require(XmlPullParser.START_TAG, ns, "games");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("game")) {
				games.add(readGame(parser));
			} else {
				skip(parser);
			}
		}
		return games;
	}

	private void readSystem(XmlPullParser parser, VideoGame game) throws XmlPullParserException, IOException {
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

	private void readPurchase(XmlPullParser parser, VideoGame game) throws XmlPullParserException, IOException {
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

	private void readSell(XmlPullParser parser, VideoGame game) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "sell");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();
			if (name.equals("date")) {
				game.sellDate = readStringElement(parser, "date");
			} else if (name.equals("price")) {
				game.sellPrice = readStringElement(parser, "price");
			} else if (name.equals("place")) {
				game.sellPlace = readStringElement(parser, "place");
			} else {
				skip(parser);
			}
		}
	}

	private VideoGame readGame(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "game");
		VideoGame game = new VideoGame();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() == XmlPullParser.END_TAG && parser.getName() == "game") {
				continue;
			}
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("title")) {
				game.title = readStringElement(parser, "title");
			/*} else if (name.equals("id")) {
				game.id = Long.parseLong(readStringElement(parser, "id"));*/
			} else if (name.equals("system")) {
				readSystem(parser, game);
			} else if (name.equals("purchase")) {
				readPurchase(parser, game);
			} else if (name.equals("sell")) {
				readSell(parser, game);
			} else if (name.equals("own")) {
				game.own = readStringElement(parser, "own");
			} else if (name.equals("notes")) {
				game.notes = readStringElement(parser, "notes");
			} else {
				skip(parser);
			}
			/*
			game.title;
Long id;
Boolean addOn;
Boolean electronic;
Boolean beat;
Boolean used;
game.systemConsole;
game.systemVersion;
Date purchaseDate;
Currency purchasePrice;
game.purchasePlace;
Date sellDate;
Currency sellPrice;
Boolean own;
game.notes;
			 */
		}
		return game;
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

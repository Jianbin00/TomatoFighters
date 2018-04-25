package com.tomatofighter;


import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Jianbin Li
 * TimerXmlParser.java
 */

public class TimerXmlParser
{


    public ArrayList<TodoList> parse(InputStream in) throws XmlPullParserException, IOException
    {
        XmlPullParser parser = Xml.newPullParser();
        try
        {

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, "UTF-8");
            parser.nextTag();
            return readAllLists(parser);
        } finally
        {
            if (in != null)
            {
                in.close();
            }

        }
    }

    private ArrayList<TodoList> readAllLists(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        ArrayList<TodoList> allPlayList = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, null, "alllists");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("playlist"))
            {
                allPlayList.add(readPlaylist(parser));
            } else
            {
                skip(parser);
            }
        }
        return allPlayList;
    }

    private TodoList readPlaylist(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, null, "playlist");
        String name = parser.getAttributeValue(null, "name");
        ArrayList<TaskItem> tracks = new ArrayList<>();
        while (parser.next() != XmlPullParser.END_TAG)
        {

            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String label = parser.getName();
            if (label.equals("track"))
            {
                tracks.add(readTrack(parser));
            } else
            {
                skip(parser);
            }
        }
        return new TodoList(name, tracks);
    }


    private TaskItem readTrack(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, null, "track");
        String name = parser.getAttributeValue(null, "name");
        String time = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "track");
        return new TaskItem(name, time);
    }


    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT)
        {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        if (parser.getEventType() != XmlPullParser.START_TAG)
        {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0)
        {
            switch (parser.next())
            {
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

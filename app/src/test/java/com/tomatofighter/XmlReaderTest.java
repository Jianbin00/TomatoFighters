package com.tomatofighter;

import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


public class XmlReaderTest
{
    InputStream stream = null;
    ArrayList<TodoList> playlist = null;
    String name = null;
    ArrayList<TaskItem> items = null;
    String trackName = null;
    String time = null;


    @Test
    public void loadXML() throws Exception
    {
        TimerXmlParser parser = new TimerXmlParser();
        try
        {

            stream = this.getClass().getClassLoader().getResourceAsStream("testXML.xml");
            playlist = parser.parse(stream);
        } finally
        {
            if (stream != null)
            {
                stream.close();
            }
        }

        for (TodoList td : playlist)
        {
            System.out.println(td.getName());
            for (TaskItem item : td.getTasks())
            {
                System.out.println(item.getName() + "+" + item.getTime());
            }
        }

        assertNotNull(playlist);
    }

}
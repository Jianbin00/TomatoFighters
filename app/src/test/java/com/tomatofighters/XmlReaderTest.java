package com.tomatofighters;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.transform.TransformerException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


public class XmlReaderTest
{
    static InputStream stream = null;
    ArrayList<TodoList> playlist = null;
    String name = null;
    ArrayList<TaskItem> items = null;
    String trackName = null;
    String time = null;
    Document doc;
    File f;
    Node root;


    @Before
    public void init() throws Exception
    {
        try
        {
            //assertNotNull(getClass().getClassLoader().getResource("initplaylist.xml").toString());
            stream = getClass().getClassLoader().getResourceAsStream("initplaylist.xml");
            //assertNotNull(stream);
            //f=new File(this.getClass().getClassLoader().getResource("initplaylist.xml").getFile());
            doc = TimerXmlParser.getDocument(stream);
            root = TimerXmlParser.getRootNode(doc);
        } catch (Exception e)
        {

        }
    }

    @Test
    public void TestDocument()
    {
        assertNotNull(doc);
    }

    @Test
    public void TestGetRootNode()
    {

        assertNotNull(root);
    }

    @Test
    public void TestGetPlayListNode()
    {
        assertNotNull(TimerXmlParser.getPlayListNode(root, 0));
        assertNotNull(TimerXmlParser.getPlayListNode(root, 1));
    }

    @Test
    public void TestLoadPlayListName()
    {
        assertEquals(TimerXmlParser.getPlayListName(root, 0), "Task 1");
        assertEquals(TimerXmlParser.getPlayListName(root, 1), "Task 2");
    }

/*    @Test
    public void TestLoadTrackName()
    {
        assertEquals(TimerXmlParser.getTrackName(TimerXmlParser.getPlayListNode(root,0),0),"Step 1");
        assertEquals(TimerXmlParser.getTrackName(TimerXmlParser.getPlayListNode(root,1),3),"Step 3");
    }

    @Test
    public void TestLoadTrackTime()
    {
        System.out.println(TimerXmlParser.getTrackTime(TimerXmlParser.getPlayListNode(root,0),0));
        assertEquals(TimerXmlParser.getTrackTime(TimerXmlParser.getPlayListNode(root,0),0),"00:01:00");
        assertEquals(TimerXmlParser.getTrackTime(TimerXmlParser.getPlayListNode(root,1),3),"00:00:20");
    }*/

/*    @Test
    public void TestAddTrack()
    {
        TimerXmlParser.addNewTrack(doc,TimerXmlParser.getPlayListNode(doc,1),"step test","00:55:00");
        //update();
        //assertNotEquals(TimerXmlParser.getTrackName(TimerXmlParser.getPlayListNode(doc,1),3),"step test");
        //assertNotEquals(TimerXmlParser.getTrackTime(TimerXmlParser.getPlayListNode(doc,1),3),"00:55:00");
    }*/


    private void update()
    {
        try
        {
            TimerXmlParser.update(doc, f);
        } catch (TransformerException e)
        {
            e.printStackTrace();
        }
    }


}
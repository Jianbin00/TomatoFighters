package com.tomatofighters;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Jianbin Li
 * TimerXmlParser.java
 * Use the DOM parser to read the xml file.
 */

public class TimerXmlParser
{

    private static final String DEFAULT_TIME = "00:00:00";

    public static Document getDocument(InputStream inputStream)
    {

        Document doc = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder db = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(inputStream);
            doc = db.parse(inputSource);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return doc;
    }

    public static Node getRootNode(Document doc)
    {
        Node newn = doc.getElementsByTagName("alllists").item(0);
        for (int i = 0; i < newn.getChildNodes().getLength(); i++)
        {
            System.out.println(newn.getChildNodes().item(i).getNodeName());
            System.out.println(newn.getChildNodes().item(i).getNodeValue());
        }

        return newn;
    }

    public static Node getPlayListNode(Node root, int i)
    {
        return root.getChildNodes().item(i);
    }

    public static NodeList getPlayListNodes(Node root)
    {
        return root.getChildNodes();
    }

    public static String getPlayListName(Node root, int i)
    {


        return root.getAttributes().getLength() + "";
/*        Node child=getPlayListNode(root, i);
        if(child instanceof Element)
        {
            Element childElment=(Element)child;
            if(childElment.getTagName().equals("name"))
            {
                return child.getFirstChild().getTextContent();
            }
        }
        return "GetPlayListError";*/
    }

    public static void setPlayListName(Node root, int i, String newName)
    {
        Node child = getPlayListNode(root, i);
        if (child instanceof Element)
        {
            Element childElment = (Element) child;
            if (childElment.getTagName().equals("name"))
            {
                child.getFirstChild().setTextContent(newName);
            }
        }
    }

    public static NodeList getTrackNodes(Node playListNode)
    {
        return playListNode.getChildNodes();
    }

    public static Node getTrackNode(Node playListNode, int i)
    {
        return playListNode.getChildNodes().item(i);
    }

    public static String getTrackName(Node playListNode, int i)

    {
        Node child = getPlayListNode(playListNode, i);
        if (child instanceof Element)
        {
            Element childElment = (Element) child;
            if (childElment.getTagName().equals("name"))
            {
                return child.getFirstChild().getTextContent();
            }
        }
        return "GetTrackNameError";
        //return getTrackNode(playListNode, i).getAttributes().getNamedItem("name").getTextContent();
    }

    public static void setTrackName(Node playListNode, int i, String newName)
    {
        Node child = getPlayListNode(playListNode, i);
        if (child instanceof Element)
        {
            Element childElment = (Element) child;
            if (childElment.getTagName().equals("name"))
            {
                child.getFirstChild().setTextContent(newName);
            }
        }
        //getTrackNode(playListNode, i).getAttributes().getNamedItem("name").setTextContent(newName);
    }

    public static String getTrackTime(Node playListNode, int i)
    {
        Node child = getPlayListNode(playListNode, i);
        if (child instanceof Element)
        {
            Element childElment = (Element) child;
            return child.getFirstChild().getTextContent();
        }
/*            if(childElment.getTagName().equals("time"))
            {

        }*/
        return "wrong";
        //return getTrackNode(playListNode,i).getFirstChild().getTextContent();
    }

    public static void setTrackTime(Node playListNode, int i, String newTime)
    {
        getTrackNode(playListNode, i).setTextContent(newTime);
    }

    public static void addPlayList(Document doc)
    {
        Element playListElement = doc.createElement("playlist");
        for (int i = 0; i < 5; i++)
        {
            playListElement.appendChild(newTrack(doc, "Task " + i, DEFAULT_TIME));
        }
        playListElement.setAttribute("name", "New to do");
        playListElement.appendChild(getRootNode(doc));
    }

    public static Element newTrack(Document doc, String name, String time)
    {
        Element track = doc.createElement("track");
        track.setAttribute("name", name);
        track.setTextContent(time);
        return track;
    }

    public static void addNewTrack(Document doc, Node playlistNode, String name, String time)
    {
        playlistNode.appendChild(newTrack(doc, name, time));
    }


    public static void removeNode(Document doc, Node parentNode, Node node)
    {
        parentNode.removeChild(node);
    }


    public static void update(Document doc, File filePath) throws TransformerException
    {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(filePath);
        transformer.transform(source, result);
    }


}

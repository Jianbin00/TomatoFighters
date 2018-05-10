package com.tomatofighters;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Jianbin Li
 * The container that holds a list of tasks.
 * Date:4/18/2018
 */


public class PlayList extends RealmObject
{


    @PrimaryKey
    private int id;
    private String name;

    private RealmList<Track> tracks;

    public PlayList()
    {
    }


    public PlayList(String name)
    {
        this.name = name;
    }

    public PlayList(String name, RealmList<Track> tracks)
    {
        this.name = name;
        this.tracks = tracks;
    }

    public PlayList(int id, String name, RealmList<Track> tracks)
    {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public RealmList<Track> getTracks()
    {
        return tracks;
    }

    public void setTracks(RealmList<Track> tracks)
    {
        this.tracks = tracks;
    }


}

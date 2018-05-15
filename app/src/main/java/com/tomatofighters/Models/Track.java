package com.tomatofighters.Models;


import com.bigkoo.pickerview.model.IPickerViewData;

import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Jianbin Li
 * The container hold the name and time of a task.
 * Date:4/18/2018
 * add a boolean isLast to record the last data.
 * Date:4/21/2018
 */

public class Track extends RealmObject implements IPickerViewData
{
    public boolean isLast;

    private int id;
    @Index
    private int playListId;
    private String name;
    private String time;


    public Track()
    {

    }

    public Track(String name, int playListId)
    {
        this.name = name;
        this.time = "00:00:00";
    }

    public Track(int id, int playListId, String name)
    {
        this.id = id;
        this.playListId = playListId;
        this.name = name;
        this.time = "00:00:00";
    }

    public Track(int id, int playListId, String name, String time)
    {
        this.id = id;
        this.playListId = playListId;
        this.name = name;
        this.time = time;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getPlayListId()
    {
        return playListId;
    }

    public void setPlayListId(int playListId)
    {
        this.playListId = playListId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    @Override
    public String getPickerViewText()
    {
        return time;
    }

}


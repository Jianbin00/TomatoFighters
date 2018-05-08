package com.tomatofighters;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Jianbin Li
 * Assignment
 * ${FILE_NAME}.
 */

public class PlayListAllTracks
{

    @Embedded
    private PlayList playList;

    @Relation(parentColumn = "id", entityColumn = "playListId", entity = Track.class)
    private List<Track> tracks;

    /* Getters and setters. */

    public PlayList getPlayList()
    {
        return playList;
    }

    public void setPlayList(PlayList playList)
    {
        this.playList = playList;
    }

    public List<Track> getTracks()
    {
        return tracks;
    }

    public void setTracks(List<Track> tracks)
    {
        this.tracks = tracks;
    }


}

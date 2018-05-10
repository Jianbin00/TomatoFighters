package com.tomatofighters;


import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Jianbin Li
 * PlayListDBHelper.java
 * It contains functions that use Realm to read and save data.
 * TODO:when track id and playList id reach max int, new record will be not available.
 */

public class PlayListDBHelper
{
    private final String DEFAULT_PLAY_LIST_NAME = "TODO";
    private final String PRE_TRACK_NAME = "TASK";
    private final int DEFAULT_NUM_OF_TRACK_IN_PLAYLIST = 5;


    private Realm realm;

    public PlayListDBHelper()
    {
        realm = Realm.getDefaultInstance();
    }


    public List<PlayList> queryAllPlayLists()
    {

        RealmResults<PlayList> playLists = realm.where(PlayList.class).findAll();
/*        RealmList<PlayList> pList=new RealmList<>();
        pList.addAll(playLists);
        return pList;*/
        return realm.copyFromRealm(playLists);
        //return playLists;
    }

    public PlayList queryPlayListById(int playListId)
    {

        return realm.where(PlayList.class).equalTo("id", playListId).findFirst();
    }


    public List<Track> queryTracksByPlayListId(int playListId)
    {
/*        PlayList playList=realm.where(PlayList.class).equalTo("id",playListId).findFirst();
        if(playList!=null)
        {
            return playList.getTracks();
        }
        return null;*/
        RealmResults<Track> tracks = realm.where(Track.class).equalTo("playListId", playListId).findAll();
        return realm.copyFromRealm(tracks);
    }


    public PlayList insertNewPlayListAndTracks(int playListId)
    {
        RealmList<Track> tracks = new RealmList<>();
        int maxTrackId = findMaxTrackId();
        for (int i = 0; i < DEFAULT_NUM_OF_TRACK_IN_PLAYLIST; i++)
        {
            maxTrackId++;
            Track track = new Track(maxTrackId, playListId, PRE_TRACK_NAME + i);
            tracks.add(track);
        }
        PlayList playList = new PlayList(playListId, DEFAULT_PLAY_LIST_NAME, tracks);
        // Copy the object to Realm. Any further changes must happen on realm
        realm.beginTransaction();
        realm.copyToRealm(playList);
        realm.copyToRealm(tracks);
        realm.commitTransaction();
        return playList;
    }

    public void setPlayListName(int playListId, String name)
    {


        PlayList playList = queryPlayListById(playListId);
        if (playList != null)
        {
            realm.beginTransaction();
            playList.setName(name);
            realm.commitTransaction();
        }

    }

    public void deletePlayList(int playListId)
    {

        PlayList playList = queryPlayListById(playListId);
        if (playList != null)
        {
            realm.beginTransaction();
            playList.deleteFromRealm();
            realm.commitTransaction();
        }

    }


    public int findMaxPlayListId()
    {
        Number num = realm.where(PlayList.class).max("id");
        if (num != null)
        {
            return num.intValue();
        }
        return -1;
    }

    public Track queryTrackById(int trackId)
    {

        return realm.where(Track.class).equalTo("id", trackId).findFirst();
    }


    public void setTrackName(int trackId, String name)
    {
        setTrackAttr(trackId, TrackAttr.name, name);
    }

    public void setTrackTime(int trackId, String time)
    {
        setTrackAttr(trackId, TrackAttr.time, time);
    }


    private void setTrackAttr(int trackId, TrackAttr attr, String value)
    {


        Track track = queryTrackById(trackId);

        if (track != null)
        {
            realm.beginTransaction();
            switch (attr)
            {
                case name:
                    track.setName(value);
                    break;
                case time:
                    track.setTime(value);
                    break;
            }
            realm.commitTransaction();


        }

    }

    public int findMaxTrackId()
    {
        Number num = realm.where(Track.class).max("id");
        if (num != null)
        {
            return num.intValue();
        }
        return -1;
    }

    public Track insertNewTrack(int playListId)
    {
        Track track = new Track(findMaxTrackId() + 1, playListId, "NEW");
        realm.beginTransaction();
        realm.copyToRealm(track);
        realm.commitTransaction();
        return track;
    }

    public void deleteTrack(int id)
    {

        Track track = queryTrackById(id);
        if (track != null)
        {
            realm.beginTransaction();
            track.deleteFromRealm();
            realm.commitTransaction();
        }

    }

    public void close()
    {
        realm.close();
    }


    public enum TrackAttr
    {
        name, time
    }


}

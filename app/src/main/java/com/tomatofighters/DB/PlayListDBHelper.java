package com.tomatofighters.DB;


import com.tomatofighters.Models.PlayList;
import com.tomatofighters.Models.Track;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Jianbin Li
 * PlayListDBHelper.java
 * It contains functions that use Realm to read and save data.
 *
 */

public class PlayListDBHelper
{
    private final String DEFAULT_PLAY_LIST_NAME = "TODO";
    private final String PRE_TRACK_NAME = "TASK";
    private final int DEFAULT_NUM_OF_TRACK_IN_PLAYLIST = 5;
    private String[] colors = {"#D09AE7", "#FD9927", "#6ACA6B", "#6BCDFD", "#FD9BCB"};

    private Realm realm;

    public PlayListDBHelper()
    {
        realm = Realm.getDefaultInstance();
    }


    public List<PlayList> queryAllPlayLists()
    {
        RealmResults<PlayList> playLists = realm.where(PlayList.class).findAll();
        return realm.copyFromRealm(playLists);
    }


    public PlayList queryPlayListById(int playListId)
    {
        return realm.where(PlayList.class).equalTo("id", playListId).findFirst();
    }


    public List<Track> queryTracksByPlayListId(int playListId)
    {
        return realm.copyFromRealm(queryTracksByPlayListIdRaw(playListId));
    }

    private RealmResults<Track> queryTracksByPlayListIdRaw(int playListId)
    {
        return realm.where(Track.class).equalTo("playListId", playListId).sort("id").findAll();
    }


    public PlayList insertNewPlayListAndTracks(int playListId)
    {
        RealmList<Track> tracks = new RealmList<>();
        for (int i = 0; i < DEFAULT_NUM_OF_TRACK_IN_PLAYLIST; i++)
        {
            Track track = new Track(i, playListId, PRE_TRACK_NAME + i);
            tracks.add(track);
        }
        PlayList playList = new PlayList(playListId, DEFAULT_PLAY_LIST_NAME, colors[getPlayListsNum() % colors.length], tracks);
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
        RealmResults<Track> tracks = queryTracksByPlayListIdRaw(playListId);
        if (playList != null)
        {
            realm.beginTransaction();
            playList.deleteFromRealm();
            tracks.deleteAllFromRealm();
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

    public Track queryTrackById(int trackId, int playListId)
    {

        return realm.where(Track.class).equalTo("id", trackId).and().equalTo("playListId", playListId).findFirst();
    }


    public void setTrackName(int trackId, int playListId, String name)
    {
        setTrackAttr(trackId, playListId, TrackAttr.name, name);
    }

    public void setTrackTime(int trackId, int playListId, String time)
    {
        setTrackAttr(trackId, playListId, TrackAttr.time, time);
    }


    private void setTrackAttr(int trackId, int playListId, TrackAttr attr, String value)
    {


        Track track = queryTrackById(trackId, playListId);

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

    public int getPlayListsNum()
    {
        return (int) realm.where(PlayList.class).count();

    }


    public int getTracksNum(int playListId)
    {
        return (int) realm.where(Track.class).equalTo("playListId", playListId).count();

    }

    public Track insertNewTrack(int playListId)
    {
        Track track = new Track(getTracksNum(playListId), playListId, "NEW");
        realm.beginTransaction();
        realm.copyToRealm(track);
        realm.commitTransaction();
        return track;
    }

    public void deleteTrack(int id, int playListId)
    {

        Track track = queryTrackById(id, playListId);
        RealmResults<Track> tracks = realm.where(Track.class).equalTo("playListId", playListId).and().greaterThan("id", id).findAll();
        if (track != null)
        {
            realm.beginTransaction();
            track.deleteFromRealm();
            for (Track t : tracks)
            {
                t.setId(t.getId() - 1);
            }

            realm.commitTransaction();
        }

    }

    public void swapTrack(int playListId, int fromIndex, int toIndex)
    {
        RealmResults<Track> tracks = queryTracksByPlayListIdRaw(playListId);
        if (fromIndex >= tracks.size())
        {
            throw new IndexOutOfBoundsException("fromIndex is out of bound.");
        }
        if (toIndex >= tracks.size())
        {
            throw new IndexOutOfBoundsException("toIndex is out of bound.");
        }

        realm.beginTransaction();
        if (fromIndex < toIndex)
        {

            for (int i = fromIndex; i < toIndex; i++)
            {

                tracks.get(i + 1).setId(i);
            }
        } else
        {
            for (int i = fromIndex; i > toIndex; i--)
            {
                tracks.get(i - 1).setId(i);
            }

        }
        tracks.get(fromIndex).setId(toIndex);

        //realm.commitTransaction();

    }

    public void beginTransaction()
    {
        realm.beginTransaction();
    }

    public void commitTransaction()
    {
        realm.commitTransaction();
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

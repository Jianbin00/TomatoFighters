package com.tomatofighters;

import java.util.LinkedList;
import java.util.List;

/**
 * Jianbin Li
 * Assignment
 * ${FILE_NAME}.
 */

public class NewPlayListGenerator
{
    private static final String PLAYLIST_NAME = "TODO";
    private static final String PRE_TRACK_NAME = "TASK";
    private static final String INIT_TIME = "00:00:00";
    private static final int DEFAULT_TRACK_NUM = 5;


    public static List<PlayList> buildPlayLists()
    {
        List<PlayList> playLists = new LinkedList<>();
        PlayList playList = new PlayList();
        playList.setName(PLAYLIST_NAME);
        playLists.add(playList);
        return playLists;
    }

    public static List<Track> buildTracks(final PlayList playList)
    {
        List<Track> tracks = new LinkedList<>();

        for (int i = 1; i <= DEFAULT_TRACK_NUM; i++)
        {
            Track track = new Track();
            track.setName(PRE_TRACK_NAME + i);
            track.setTime(INIT_TIME);
            track.setPlayListId(playList.getId());
            tracks.add(track);
        }
        return tracks;
    }

}

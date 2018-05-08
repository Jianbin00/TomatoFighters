package com.tomatofighters;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Jianbin Li
 * AppDatabaseHelper.java
 * Adapt from the Room example in
 * https://proandroiddev.com/android-room-handling-relations-using-livedata-2d892e40bd53
 * <p>
 * 4/27/2018
 */

public class AppDatabaseHelper
{
    private PlayListDAO dao;


    public AppDatabaseHelper(AppDatabase db)
    {
        dao = db.PlayListDAO();
    }

    public LiveData<PlayList> getPlayList(int id)
    {
        LiveData<PlayList> playListLiveData = dao.loadPlayList(id);
        playListLiveData = Transformations.switchMap(playListLiveData, new Function<PlayList, LiveData<PlayList>>()
        {
            @Override
            public LiveData<PlayList> apply(final PlayList inputPlayList)
            {
                LiveData<List<Track>> tracksLiveData = dao.LoadTracks(inputPlayList.getId());
                LiveData<PlayList> outputLiveData = Transformations.map(tracksLiveData, new Function<List<Track>, PlayList>()
                {
                    @Override
                    public PlayList apply(List<Track> inputTracks)
                    {
                        inputPlayList.setTracks(inputTracks);
                        return inputPlayList;
                    }
                });
                return outputLiveData;
            }
        });
        return playListLiveData;
    }

    public LiveData<List<PlayList>> getPlayLists()
    {
        LiveData<List<PlayList>> playListsLiveData = dao.loadAllPlayLists();

        playListsLiveData = Transformations.switchMap(playListsLiveData, new Function<List<PlayList>, LiveData<List<PlayList>>>()
        {
            @Override
            public LiveData<List<PlayList>> apply(final List<PlayList> inputPlayList)
            {
                final MediatorLiveData<List<PlayList>> playListMediatorLiveData = new MediatorLiveData<>();
                for (final PlayList playList : inputPlayList)
                {
                    playListMediatorLiveData.addSource(dao.LoadTracks(playList.getId()), new Observer<List<Track>>()
                    {
                        @Override
                        public void onChanged(@Nullable List<Track> tracks)
                        {
                            playList.setTracks(tracks);
                            playListMediatorLiveData.postValue(inputPlayList);
                        }
                    });
                }
                return playListMediatorLiveData;
            }
        });

        return playListsLiveData;
    }

    public void savePlayList(PlayList playList)
    {
        dao.insertPlayList(playList);
        dao.insertTracks(playList.getTracks());
    }

    public void savePlayLists(List<PlayList> playLists)
    {
        dao.insertPlayLists(playLists);
        for (PlayList playList : playLists)
        {
            dao.insertTracks(playList.getTracks());
        }
    }


}

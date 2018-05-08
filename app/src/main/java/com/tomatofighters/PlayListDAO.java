package com.tomatofighters;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Jianbin Li
 * Assignment
 * ${FILE_NAME}.
 */
@Dao
public interface PlayListDAO
{
    @Query("SELECT * FROM PlayList")
    List<PlayList> getPlayLists();

    @Query("SELECT * FROM PlayList")
    LiveData<List<PlayList>> loadAllPlayLists();

    @Query("SELECT * FROM PlayList WHERE id=:id")
    PlayList getPlayList(int id);

    @Query("SELECT * FROM PlayList WHERE id=:id")
    LiveData<PlayList> loadPlayList(int id);

    @Query("SELECT * FROM Track WHERE playListId=:playListId")
    List<Track> getTracks(int playListId);

    @Query("SELECT * FROM Track WHERE playListId=:playListId")
    LiveData<List<Track>> LoadTracks(int playListId);

    @Query("SELECT * FROM Track WHERE id=(:id)")
    Track findTrackById(int id);

    @Insert(onConflict = REPLACE)
    long insertPlayList(PlayList playList);

    @Insert(onConflict = REPLACE)
    void insertPlayLists(List<PlayList> playLists);

    @Insert(onConflict = REPLACE)
    void insertTracks(List<Track> track);

    @Query("DELETE FROM PlayList WHERE id=:id")
    int deletePlayListById(int id);

    @Query("DELETE FROM Track WHERE playlistId=:playlistId")
    void deleteTracksByPlaylistId(int playlistId);

    @Query("DELETE FROM Track WHERE id=:id")
    int deleteTrackById(int id);


    @Delete
    void deletePlayList(PlayList playList);

    @Update
    void updatePlayListName(PlayList playList);

}

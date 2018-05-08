package com.tomatofighters;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

/**
 * Jianbin Li
 * The container that holds a list of tasks.
 * Date:4/18/2018
 */

@Entity
public class PlayList //implements Parcelable
{


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    private int id;
    @ColumnInfo
    private String name;
    @Ignore
    private List<Track> tracks;


    /*
        public static final Creator<PlayList> CREATOR = new Creator<PlayList>()
        {
            @Override
            public PlayList createFromParcel(Parcel source)
            {
                PlayList playList = new PlayList(source.readString());
                //playList.tracks = source.createTypedArrayList(Track.CREATOR);
                return playList;
            }

            @Override
            public PlayList[] newArray(int size)
            {
                return new PlayList[size];
            }
        };
    */
    @Ignore
    public PlayList()
    {
    }

    @Ignore
    public PlayList(String name)
    {
        this.name = name;
//        this.tracks = new ArrayList<>();
//        addNewTasks(tracks, 5);
    }

    public PlayList(final int id, String name)
    {
        this.id = id;
        this.name = name;
//        this.tracks = tracks;
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

    public List<Track> getTracks()
    {
        return tracks;
    }

    public void setTracks(List<Track> tracks)
    {
        this.tracks = tracks;
    }

//    public void addNewTasks(ArrayList<Track> tasks, int numberOfTasks)
//    {
//        Track newTask;
//        for (int i = 0; i < numberOfTasks; i++)
//        {
//            newTask = new Track("Task" + i);
//            tasks.add(newTask);
//        }
//    }
//
//    public void addNewTrack(ArrayList<Track> tracks)
//    {
//        tracks.add(new Track("NewTask"));
//    }

/*    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(name);
        //dest.writeTypedList(tracks);
    }*/


}

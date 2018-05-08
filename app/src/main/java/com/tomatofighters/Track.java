package com.tomatofighters;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Relation;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Jianbin Li
 * The container hold the name and time of a task.
 * Date:4/18/2018
 * add a boolean isLast to record the last data.
 * Date:4/21/2018
 */

@Entity(foreignKeys = @ForeignKey(entity = PlayList.class,
        parentColumns = "id",
        childColumns = "playListId",
        onDelete = CASCADE)
        , indices = {@Index(value = "playListId")}
)
public class Track implements IPickerViewData//, Parcelable
{


    @Ignore
    public boolean isLast;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    private int id;
    @ColumnInfo
    private int playListId;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private String time;


    /*public static final Creator<Track> CREATOR = new Creator<Track>()
    {
        @Override
        public Track createFromParcel(Parcel source)
        {
            //从Parcel容器中读取传递数据值，封装成Parcelable对象返回逻辑层。
            Track tracks = new Track(source.readString(), source.readString());
            return tracks;
        }

        @Override
        public Track[] newArray(int size)
        {
            //创建一个类型为T，长度为size的数组，仅一句话（return new T[size])即可。方法是供外部类反序列化本类数组使用。
            return new Track[size];
        }
    };*/
    @Ignore
    public Track()
    {

    }

    @Ignore
    public Track(String name)
    {
        this.name = name;
        this.time = "00:00:00";
    }


    public Track(final int id, String name, String time, final int playListId)
    {
        this.id = id;
        this.name = name;
        this.time = time;
        this.playListId = playListId;
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

    @Dao
    public interface PlayListTrackDao
    {
        @Query("SELECT id, name from PlayList")
        public List<PlayListNameAndAllTracks> loadPlayListAndTracks();
    }

    public class PlayListNameAndAllTracks
    {
        public int id;
        public String name;
        @Relation(parentColumn = "id", entityColumn = "playListId")
        public List<Track> tracks;

    }

/*    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(name);
        dest.writeString(time);
    }*/
}


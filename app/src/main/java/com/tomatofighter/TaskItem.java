package com.tomatofighter;

import android.os.Parcel;
import android.os.Parcelable;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Jianbin Li
 * The container hold the name and time of a task.
 * Date:4/18/2018
 */

public class TaskItem implements IPickerViewData, Parcelable
{
    public static final Creator<TaskItem> CREATOR = new Creator<TaskItem>()
    {
        @Override
        public TaskItem createFromParcel(Parcel source)
        {
            //从Parcel容器中读取传递数据值，封装成Parcelable对象返回逻辑层。
            TaskItem tasks = new TaskItem(source.readString(), source.readString());
            return tasks;
        }

        @Override
        public TaskItem[] newArray(int size)
        {
            //创建一个类型为T，长度为size的数组，仅一句话（return new T[size])即可。方法是供外部类反序列化本类数组使用。
            return new TaskItem[size];
        }
    };
    public String name;
    public String time;

    public TaskItem(String name)
    {
        this.name = name;
        this.time = "00:00:00";
    }

    public TaskItem(String name, String time)
    {
        this.name = name;
        this.time = time;
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

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(name);
        dest.writeString(time);
    }
}


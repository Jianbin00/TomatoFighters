package com.tomatofighter;

import com.bigkoo.pickerview.model.IPickerViewData;


/**
 * Created by Jianbin Li .
 * The container that holds task's name and the time.
 * Date: 4/18/2018
 */
public class TaskItem implements IPickerViewData
{
    public String name;
    public String time;

    public TaskItem(String name)
    {
        this.name = name;
        this.time= "00:00:00";
    }

    public TaskItem(String name,String time)
    {
        this.name = name;
        this.time= time;
    }

    public String getName()
    {
        return name;
    }

    public String getTime()
    {
        return time;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    @Override
    public String getPickerViewText() {
        return time;
    }
}

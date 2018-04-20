package com.tomatofighter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Jianbin Li
 * The container that holds a list of tasks.
 * Date:4/18/2018
 */

public class TodoList implements Parcelable
{

    public static final Creator<TodoList> CREATOR = new Creator<TodoList>()
    {
        @Override
        public TodoList createFromParcel(Parcel source)
        {
            TodoList todoList = new TodoList(source.readString());
            todoList.tasks = source.createTypedArrayList(TaskItem.CREATOR);
            return todoList;
        }

        @Override
        public TodoList[] newArray(int size)
        {
            //创建一个类型为T，长度为size的数组，仅一句话（return new T[size])即可。方法是供外部类反序列化本类数组使用。
            return new TodoList[size];
        }
    };
    private String name;
    private ArrayList<TaskItem> tasks;

    public TodoList(String name)
    {
        this.name = name;
        this.tasks = new ArrayList<>();
        addTasks(tasks, 5);
    }

    public TodoList(String name, ArrayList<TaskItem> tasks)
    {
        this.name = name;
        this.tasks = tasks;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ArrayList<TaskItem> getTasks()
    {
        return tasks;
    }

    public void setTasks(ArrayList<TaskItem> tasks)
    {
        this.tasks = tasks;
    }

    public void addTasks(ArrayList<TaskItem> tasks, int numberOfTasks)
    {
        TaskItem newTask;
        for (int i = 0; i < numberOfTasks; i++)
        {
            newTask = new TaskItem("Task" + i);
            tasks.add(newTask);
        }
    }

    public void addTask(ArrayList<TaskItem> tasks)
    {
        addTasks(tasks, 1);
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
        dest.writeTypedList(tasks);
    }

}

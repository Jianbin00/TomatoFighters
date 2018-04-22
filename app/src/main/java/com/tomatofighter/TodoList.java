package com.tomatofighter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

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
            return new TodoList[size];
        }
    };
    private String name;
    private List<TaskItem> tasks;

    public TodoList(String name)
    {
        this.name = name;
        this.tasks = new ArrayList<>();
        addNewTasks(tasks, 5);
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

    public List<TaskItem> getTasks()
    {
        return tasks;
    }

    public void setTasks(List<TaskItem> tasks)
    {
        this.tasks = tasks;
    }

    public void addNewTasks(List<TaskItem> tasks, int numberOfTasks)
    {
        TaskItem newTask;
        for (int i = 0; i < numberOfTasks; i++)
        {
            newTask = new TaskItem("Task" + i);
            tasks.add(newTask);
        }
    }

    public void addNewTask(List<TaskItem> tasks)
    {
        tasks.add(new TaskItem("NewTask"));
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

package com.tomatofighter;

import java.util.ArrayList;

/**
 * Jianbin Li
 * The container that holds a list of tasks.
 * Date:4/18/2018
 */

public class TodoList
{
    private String name;
    private ArrayList<TaskItem> tasks;

    public TodoList(String name)
    {
        this.name = name;
        this.tasks = new ArrayList<>();
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
}

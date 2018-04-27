package com.tomatofighters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Jianbin Li
 * PlayListStorageHelper.java
 */

public class PlayListStorageHelper extends SQLiteOpenHelper
{
    private String DEFAULT_DB_NAME = "";

    public PlayListStorageHelper(Context c)
    {
        super(c, c.getResources().getString(R.string.database_name), null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL("create table person(_id integer primary key autoincrement, playlist_name char(20), table_name char(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion)
    {
        System.out.println("Database updated.");
    }

}

package com.tomatofighters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Jianbin Li
 * Assignment
 * ${FILE_NAME}.
 */

@Database(entities = {PlayList.class, Track.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase
{
    private static final String DATABASE_NAME = "data.db";
    private static AppDatabase INSTANCE;
    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getDatabase(final Context context)
    {
        if (INSTANCE == null)
        {
            synchronized (AppDatabase.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                            .addCallback(new Callback()
                            {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db)
                                {
                                    super.onCreate(db);
                                    final List<PlayList> playLists = NewPlayListGenerator.buildPlayLists();
                                    final List<Track> tracks = NewPlayListGenerator.buildTracks(playLists.get(0));
                                    INSTANCE.runInTransaction(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            INSTANCE.PlayListDAO().insertPlayLists(playLists);
                                            INSTANCE.PlayListDAO().insertTracks(tracks);
                                            INSTANCE.setDatabaseCreated();
                                        }
                                    });

                                }
                            })
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    public static void onDestroy()
    {
        INSTANCE = null;
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context)
    {
        if (context.getDatabasePath(DATABASE_NAME).exists())
        {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated()
    {
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated()
    {
        return mIsDatabaseCreated;
    }

    public abstract PlayListDAO PlayListDAO();


}

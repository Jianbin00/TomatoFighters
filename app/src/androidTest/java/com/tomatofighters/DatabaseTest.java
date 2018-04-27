package com.tomatofighters;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest
{
    private PlayListDAO plDao;
    private TrackDAO tDAO;
    private AppDatabase db;

    @Before
    public void createDb()
    {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        plDao = db.PlayListDAO();
        tDAO = db.TrackDAO();
    }

    @After
    public void closeDb() throws IOException
    {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception
    {
        assertEquals(plDao.findById(0).getName(), "TODO1");
        assertEquals(tDAO.findById(0).getName(), "Task 1");
    }
}

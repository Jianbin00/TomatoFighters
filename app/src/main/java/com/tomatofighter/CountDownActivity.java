package com.tomatofighter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;


//import java.io.FileOutputStream;
//import java.io.IOException;

public class CountDownActivity extends Activity
{

    //FileOutputStream out;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);

        /*Button fab = findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.save_schedule)
        {

            return true;
        }
        else if(id ==R.id.load_schedule)
        {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    /*public void saveToFile(String filename, String data)
    {

        try
        {
            out = openFileOutput(filename, Context.MODE_PRIVATE);
            out.write(data.getBytes());
            out.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void loadFromFile(String filename)
    {

    }*/
}

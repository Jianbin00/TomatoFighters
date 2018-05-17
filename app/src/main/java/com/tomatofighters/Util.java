package com.tomatofighters;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Jianbin Li
 * Assignment
 * ${FILE_NAME}.
 */

public class Util
{

    public static void showSoftKeyboard(EditText editText)
    {
        Timer timer = new Timer();

        timer.schedule(new TimerTask()
        {

            @Override

            public void run()
            {

                InputMethodManager manager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                if (manager != null)
                {
                    manager.showSoftInput(editText, 0);
                }


            }

        }, 498);


    }
}

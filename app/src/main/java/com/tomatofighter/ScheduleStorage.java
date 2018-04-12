package com.tomatofighter;



/**
 * Jianbin Li
 * Not yet implement
 */

public class ScheduleStorage
{

    private final static int DETAIL_NUMBER=4;

    /*
    The format of data is:
    RemainTime isAnswerNeed LabelColor Name
    300 0 #000000 TODO1
    50 1 #333333 TODO2
    100 1 #999999 TODO3
    Note: The unit of RemainTime is seco
    nd.
          If isAnswerNeed is 0, you don't need to click the "continue" button to start the
           next count down.
          If isAnswerNeed is 1, the count down clock will stop when reach 0, only you click
           the "continue" button, you start the next count down.
          Items in Name may obtain spaces, so put it at last.
     */




    protected static String combineLine(String time, String isAnswerNeed, String color, String name)
    {
        return time+" "+isAnswerNeed+" "+color+" "+name;
    }

    protected static String[] devideLine(String scheduluLine)
    {
         return scheduluLine.split(" ",DETAIL_NUMBER);

    }
}

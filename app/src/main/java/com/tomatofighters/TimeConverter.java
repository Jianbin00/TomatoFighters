package com.tomatofighters;

/**
 * Jianbin Li
 * Assignment
 * ${FILE_NAME}.
 */

public class TimeConverter
{
    public static String timeToString(int hour, int min, int sec)
    {
        return String.format("%02d", hour) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec);
    }

    public static int[] timeStringToInts(String timeString)
    {
        String[] digits = timeStringDivider(timeString);

        return new int[]{Integer.parseInt(digits[0]), Integer.parseInt(digits[1]), Integer.parseInt(digits[2])};
    }

    public static long timeStringToLong(String timeString)
    {
        long time;
        String[] digits = timeStringDivider(timeString);
        time = Integer.parseInt(digits[0]) * 3600000 + Integer.parseInt(digits[1]) * 60000 + Integer.parseInt(digits[2]) * 1000;
        return time;
    }

    private static String[] timeStringDivider(String timeString)
    {
        if (timeString.matches("\\d\\d:\\d\\d:\\d\\d"))
        {
            return timeString.split(":");

        } else
        {
            throw new IllegalArgumentException(timeString);
        }
    }

    public static String millisToString(long millisUntilFinished)
    {
        //The format of remainTimeTV is "hh:mm:ss".
        int hour = (int) millisUntilFinished / 3600000;
        int min = (int) (millisUntilFinished % 3600000) / 60000;
        int sec = (int) (millisUntilFinished % 60000) / 1000;
        return timeToString(hour, min, sec);

    }


}

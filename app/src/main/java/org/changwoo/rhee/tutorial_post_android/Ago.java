package org.changwoo.rhee.tutorial_post_android;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Ago {
    public static CharSequence build(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        CharSequence ago = "";
        try {
            //"2020-03-04T21:59:32.305Z"
            long time = sdf.parse(date).getTime();
            long now = System.currentTimeMillis();
            ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ago;
    }
}

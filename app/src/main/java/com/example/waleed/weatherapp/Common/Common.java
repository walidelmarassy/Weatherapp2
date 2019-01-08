package com.example.waleed.weatherapp.Common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static String API_ID="a6ea89a144ef1677b5ee3b0fac146fe4";
    public static Location current_location=null;


    public static String convertUnixTODate(long dt) {
        Date date=new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm EEE MM YYYY");
        String formatted=simpleDateFormat.format(date);
        return formatted;
    }

    public static String convertUnixTOHour(long dt) {
        Date date=new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
        String formatted=simpleDateFormat.format(date);
        return formatted;


    }
}

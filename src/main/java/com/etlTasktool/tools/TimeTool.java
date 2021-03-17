package com.etlTasktool.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeTool {

    public final static String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public final static String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public  static String getTime(){
        Calendar calendar=Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_YYYY_MM_DD_HH_MM_SS);
        String time=simpleDateFormat.format(date);
        return time;
    }


    public static String getDateString(){
        Calendar calendar=Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time=simpleDateFormat.format(date);
        return time;
    }


    public static String standard(String time) {
        String[] temps = time.split("-");
        temps[1]=temps[1].length() == 1 ? "0" + temps[1] : temps[1];
        temps[2]=temps[2].length() == 1 ? "0" + temps[2] : temps[2];
        time = temps[0] + "-" + temps[1] + "-" + temps[2];
        return time;
    }

}

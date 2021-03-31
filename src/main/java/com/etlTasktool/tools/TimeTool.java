package com.etlTasktool.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
public class TimeTool {

    /**
     * 日期模板yyyy-MM-dd
     */
    public final static String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    /**
     * 日期模板yyyy-MM-dd HH:mm:ss
     */
    public final static String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取时间字符串1
     * @return
     */
    public  static String getTimeString(){
        Calendar calendar=Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_YYYY_MM_DD_HH_MM_SS);
        String time=simpleDateFormat.format(date);
        return time;
    }

    /**
     * 获取时间字符串2
     * @return
     */
    public static String getDateString(){
        Calendar calendar=Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_YYYY_MM_DD);
        String time=simpleDateFormat.format(date);
        return time;
    }


    /**
     * 标准化日期
     * <p>
     *     当time传入为2021-8-9时，会转化为2021-08-09
     * </p>
     * @param time
     * @return
     */
    public static String standard(String time) {
        String[] temps = time.split("-");
        temps[1]=temps[1].length() == 1 ? "0" + temps[1] : temps[1];
        temps[2]=temps[2].length() == 1 ? "0" + temps[2] : temps[2];
        time = temps[0] + "-" + temps[1] + "-" + temps[2];
        return time;
    }

}

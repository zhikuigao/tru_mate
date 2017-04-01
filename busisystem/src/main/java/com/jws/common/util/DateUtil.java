package com.jws.common.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @description
 *         时间转换工具类
 * @date   2016年3月9日 下午3:23:38
 * @author kevin
 * @email  kevin.zhu@jwis.cn
 */
public class DateUtil {

    /**
     * utilDate ---->string
     * 获得当前时间，将时间格式化
     * yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDateString(java.util.Date utilDate, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr, Locale.getDefault());
        return format.format(utilDate);
    }

    /**
     * utilDate ---->string
     * 获得当前时间，将时间格式化
     * yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDateString(java.util.Date utilDate) {
        return getDateString(utilDate, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * utilDate ---->string
     * 获得当前时间，将时间格式化
     * yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getMillDateString(java.util.Date utilDate) {
        return getDateString(utilDate, "yyyy-MM-dd HH:mm:ss:SSS");
    }

    /**
     * string---->utilDate
     * 将指定格式的字符串转为utilDate
     * @param time
     * @param formatStr
     * @return
     */
    public static java.util.Date getUtilDate(String time, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr, Locale.getDefault());
        java.util.Date utilDate = null;
        try {
            utilDate = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return utilDate;
    }

    /**
     * string---->utilDate
     * 将指定格式的字符串转为utilDate
     * time格式：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static java.util.Date getUtilDate(String time) {
        return getUtilDate(time, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 将utilDate转为sqlDate
     * @param utilDate
     */
    public static java.sql.Date getSQLDate(java.util.Date utilDate){
        if(utilDate == null){
            utilDate = new java.util.Date();
        }
        return new java.sql.Date(utilDate.getTime());
    }

    /**
     * 将sqlDate转为utilDate
     * @param sqlDate
     */
    public static java.util.Date getUtilDate(java.sql.Date sqlDate){
        java.util.Date utilDate;
        if(sqlDate == null){
            utilDate = new java.util.Date();
            return utilDate;
        }
        utilDate=new java.util.Date(sqlDate.getTime());
        return utilDate;
    }

    /**
     * 获取指定日期的月份简称
     * @param date
     * @return
     */
    public static String getShortMonth(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);
        return format.format(date).substring(5, 8);
    }

    /**
     * 获取指定日期的年
     * @param date
     * @return
     */
    public static String getDateOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR) + "";
    }

    /**
     * 给指定日期增加指定月份数
     * @param date
     * @param value
     * @return
     */
    public static Date addMonth(Date date, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, value);
        return calendar.getTime();
    }

    /** 比较两个时间大小
     * @param date1
     * @param date2
     * @return
     *      如果date1 > date2，return 1
     *      如果date1 = date2，return 0
     *      如果date1 < date2，return -1
     */
    public static int compareDateByMonth(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        int year1 = calendar1.get(Calendar.YEAR);
        int month1 = calendar1.get(Calendar.MONTH);
        int year2 = calendar2.get(Calendar.YEAR);
        int month2 = calendar2.get(Calendar.MONTH);

        if(year1 > year2) {
            return 1;
        } else if(year1 < year2) {
            return -1;
        } else {
            if(month1 > month2) {
                return 1;
            } else if(month1 < month2) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
     * 返回美国格式时间
     * @param str
     * @return
     */
    public static String getEDate(String str) {
        Date date = new Date(Long.parseLong(str));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String time = formatter.format(date);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(time, pos);
        String j = strtodate.toString();
        String[] k = j.split(" ");
        return k[2] + "-" + k[1] + "-" + k[5].substring(2, 4);
    }

    public static String toLocalTimeString(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(System.currentTimeMillis());
    }

    public static String dateToMessageTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        return sdf.format(new Date(time));
    }

    public static boolean needShowTime(long time1,long time2){
        Long temp = time1 - time2;
        Long time = temp / 60;
        if(time >= 1){
            return true;
        }else{
            return false;
        }
    }

    // 将字符串转为时间戳
    public static String getTime(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }
}

package com.wwl.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author wwl
 * @date 2022/7/26 13:44
 * @desc 转换工具类
 */
public class ConvertUtils {
    private static final String dateFormatStr = "yyyy-MM-dd HH:mm:ss";
    private static final String onlyDateFormatStr = "yyyy-MM-dd";
    private static final String onlyTimeFormatStr = "HH:mm:ss";

    private ConvertUtils(){

    }

    public static int random(int maxValue) {
        return (int)(Math.random() * maxValue);
    }

    public static double random(double maxValue) {
        return Math.random() * maxValue;
    }
    public static double random(double maxValue,int fixLength) {
        return round(Math.random() * maxValue,fixLength);
    }
    public static Double round(double value,int fixLength){
        double fix = 1.0;
        for (int i = 0; i < fixLength; i++) {
            fix = fix*10;
        }
        return Math.round(value*fix)/fix;
    }
    public static Double toDouble(String value){
        return isDouble(value)? NumberUtils.toDouble(value):0.0;
    }

    public static boolean isDouble(String value){
        return NumberUtils.isParsable(value);
    }

    public static int toInt(String value){
        return NumberUtils.isDigits(value)?NumberUtils.toInt(value):0;
    }

    public static long toLong(String value){
        return NumberUtils.isDigits(value)?NumberUtils.toLong(value):0L;
    }


    public static boolean isInt(String value){
        return NumberUtils.isDigits(value);
    }

    public static String format(Date date, String format){
        return new SimpleDateFormat(format).format(date);
    }
    public static String formatDatetime(Date date){
        return new SimpleDateFormat(dateFormatStr).format(date);
    }
    public static String formatDate(Date date){
        return new SimpleDateFormat(onlyDateFormatStr).format(date);
    }
    public static String formatTime(Date date){
        return new SimpleDateFormat(onlyTimeFormatStr).format(date);
    }
    public static String formatTime(LocalDateTime date){
        return DateTimeFormatter.ofPattern(onlyTimeFormatStr).format(date);
    }    public static String formatDatetime(LocalDateTime date){
        return DateTimeFormatter.ofPattern(dateFormatStr).format(date);
    }
    public static String formatDate(LocalDateTime date){
        return DateTimeFormatter.ofPattern(onlyDateFormatStr).format(date);
    }

    public static String timestampToString(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat(dateFormatStr);
        return df.format(timestamp);
    }

    public static Boolean isToday(Timestamp timestamp) {
        if(timestamp==null){
            return false;
        }
        String dayStr = format(new Date(timestamp.getTime()),"yyyy-MM-dd");
        return Objects.equals(dayStr,format(new Date(),"yyyy-MM-dd"));
    }

    public static List<String> toListString(String splitStr)
    {
        List<String> results= new ArrayList<>();
        if(StringUtils.isBlank(splitStr))
        {
            return results;
        }
        String[] arrays = splitStr.split(",");
        for (String array:arrays)
        {
            if(StringUtils.isNotBlank(array))
            {
                results.add(array);
            }
        }
        return results;
    }

    public static List<Integer> toListInt(String splitStr)
    {
        List<Integer> results= new ArrayList<>();
        if(StringUtils.isBlank(splitStr))
        {
            return results;
        }
        String[] arrays = splitStr.split(",");
        for (String array:arrays)
        {
            if(StringUtils.isNotBlank(array))
            {
                results.add(NumberUtils.toInt(array));
            }
        }
        return results;
    }

    public static List<Double> toListDouble(String splitStr)
    {
        List<Double> results= new ArrayList<>();
        if(StringUtils.isBlank(splitStr))
        {
            return results;
        }
        String[] arrays = splitStr.split(",");
        for (String array:arrays)
        {
            if(StringUtils.isNotBlank(array))
            {
                results.add(NumberUtils.toDouble(array));
            }
        }
        return results;
    }

    public static boolean toBoolean(String boolStr)
    {
        if(StringUtils.isBlank(boolStr))
        {
            return false;
        }
        if(NumberUtils.isDigits(boolStr))
        {
            return NumberUtils.toDouble(boolStr)>0;
        }
        return "true".equalsIgnoreCase(boolStr);
    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }
    /**
     * 日期格式字符串转换成时间戳
     * @param date_str 字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @param ms 是否精确到毫秒
     * @return
     */
    public static String date2TimeStamp(String date_str,String format,Boolean ms){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);

            if (ms){
                return String.valueOf(sdf.parse(date_str).getTime()/1000);
            }else {
                return String.valueOf(sdf.parse(date_str).getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static Date plusDay(Date originDate,int plus,String addType){
        long mills = originDate.getTime();
        switch ((addType+"").toLowerCase().trim()){
            case "day":
                mills += plus * 24*3600*1000;
                break;
            case "min":
            case "minute":
                mills += plus * 60 * 1000;
                break;
            case "hour":
                mills += plus * 3600 * 1000;
                break;
            default: mills += plus * 1000;
        }
        return new Date(mills);
    }

    public static Date toDate(String dateStr)
    {
        try {
            return DateUtils.parseDate(dateStr, Locale.CHINA,"yyyy","yyyy-MM","yyyy-MM-dd","yyyy","yyyy/MM","yyyy/MM/dd","YYYY","YYYY-MM","YYYY-MM-dd","dd/MM/YYYY","yyyy-MM-dd HH:mm:ss","YYYY","YYYY/MM","YYYY/MM/dd","dd/MM/YYYY HH:mm:SS");
        } catch (ParseException e) {
            return new Date();
        }
    }
    public static Date toTime(String dateStr)
    {
        try {
            return DateUtils.parseDate(dateStr,Locale.CHINA,"HH:mm","HH:mm:ss");
        } catch (ParseException e) {
            return new Date();
        }
    }
    public static Date toDateTime(String dateStr,Date defaultValue)
    {
        try {
            return DateUtils.parseDate(dateStr,Locale.CHINA
                    ,"yyyy","yyyy-MM","yyyy-MM-dd"
                    ,"yyyy","yyyy/MM","yyyy/MM/dd"
                    ,"yyyy-MM-dd HH","yyyy-MM-dd HH:mm","yyyy-MM-dd HH:mm:ss"
                    ,"yyyy/MM/dd HH","yyyy/MM/dd HH:mm","yyyy/MM/dd HH:mm:ss","dd/MM/YYYY HH:mm:SS"
            );
        } catch (ParseException e) {
            return defaultValue;
        }
    }
    public static Timestamp toTimeStamp(String dateStr,Date defaultValue)
    {
        return Timestamp.from(toDateTime(dateStr,defaultValue).toInstant());
    }

    public static <T> T[] listToArray(List<T> originDataList,Class<T> tClass){
        if(originDataList==null){
            return null;
        }
        T[] items = (T[]) Array.newInstance(tClass, originDataList.size());
        for (int i = 0; i < originDataList.size(); i++) {
            items[i] = originDataList.get(i);
        }
        return items;
    }

    public static <T> T[]  listToArray(List<T> originDataList,T[] results){
        if(originDataList==null){
            return null;
        }
        for (int i = 0; i < originDataList.size(); i++) {
            results[i] = originDataList.get(i);
        }
        return results;
    }



    /**
     * 如果是小数，保留两位，非小数，保留整数
     * @param number
     */
    public static String getDoubleString(double number) {
        String numberStr;
        if (((int) number * 1000) == (int) (number * 1000)) {
            //如果是一个整数
            numberStr = String.valueOf((int) number);
        } else {
            DecimalFormat df = new DecimalFormat("######0.00");
            numberStr = df.format(number);
        }
        return numberStr;
    }


    /**
     * 将每三个数字（或字符）加上逗号处理（通常使用金额方面的编辑）
     * 5000000.00 --> 5,000,000.00
     * 20000000 --> 20,000,000
     * @param str  无逗号的数字
     * @return 加上逗号的数字
     */
    public static String getNumberAddComma(String str) {
        if (str == null) {
            str = "";
        }
        String addCommaStr = ""; // 需要添加逗号的字符串（整数）
        String tmpCommaStr = ""; // 小数，等逗号添加完后，最后在末尾补上
        if (str.contains(".")) {
            addCommaStr = str.substring(0,str.indexOf("."));
            tmpCommaStr = str.substring(str.indexOf("."),str.length());
        }else{
            addCommaStr = str;
        }
        // 将传进数字反转
        String reverseStr = new StringBuilder(addCommaStr).reverse().toString();
        String strTemp = "";
        for (int i = 0; i < reverseStr.length(); i++) {
            if (i * 3 + 3 > reverseStr.length()) {
                strTemp += reverseStr.substring(i * 3, reverseStr.length());
                break;
            }
            strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";
        }
        // 将 "5,000,000," 中最后一个","去除
        if (strTemp.endsWith(",")) {
            strTemp = strTemp.substring(0, strTemp.length() - 1);
        }
        // 将数字重新反转,并将小数拼接到末尾
        String resultStr = new StringBuilder(strTemp).reverse().toString() + tmpCommaStr;
        return resultStr;
    }

    /**
     * 将加上逗号处理的数字（字符）的逗号去掉 （通常使用金额方面的编辑）
     * 5,000,000.00 --> 5000000.00
     * 20,000,000 --> 20000000
     * @param str  加上逗号的数字（字符）
     * @return 无逗号的数字（字符）
     */
    public static String getNumberRemoveComma(String str) {
        if (str == null) {
            str = "";
        }
        String resultStr = str.replaceAll(",","");

        return resultStr;
    }

    public static String getDistanceString(double distance){
        if(distance>1000.00){
            return  String.format("%.2f %s",distance/1000.00,"千米");
        }
        return String.format("%.2f %s",distance,"米");
    }
    public static String getAreaString(double distance){
        if(distance>1000000.00){
            return  String.format("%.2f %s",distance/1000000.00,"平方公里");
        }
        return String.format("%.2f %s",distance,"平方米");
    }
    public static String getString(Object value){
        if(value==null){
            return  "";
        }
        return value+"";
    }
}
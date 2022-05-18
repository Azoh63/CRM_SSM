package cn.edu.yibinu.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtils {
    public static String dateTimeFormat(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}

package cn.edu.yibinu.crm.commons.utils;

import java.util.UUID;

public class UUIDUtils {
    public static String getUUID(){
        return  UUID.randomUUID().toString().replaceAll("-","");
    }
}

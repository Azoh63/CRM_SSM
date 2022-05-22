package cn.edu.yibinu.crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;

public class HSSFUtils {
    /**
     * 获取每一行的市场活动对象
     * @param cell 列对象
     * @return  返回的是市场活动对象
     */
    public static String getCellValue(HSSFCell cell){
        String retStr = "";
        //类型没有列举完，因为本excel文件都只涉及到字符串类型
        //另外的一个小技巧，在获取到的对象后面加""就会变成一个字符串
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            retStr = String.valueOf(cell.getNumericCellValue());
        }else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){
            retStr = cell.getStringCellValue();
        }else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
            retStr = cell.getStringCellValue();
        }else if(cell.getCellType() == Cell.CELL_TYPE_ERROR){
            retStr = cell.getErrorCellValue() + "";
        }else{
            retStr = "";
        }
        return retStr;
    }
}

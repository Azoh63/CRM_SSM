package cn.edu.yibinu.crm.test;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileOutputStream;

public class ApachePoiTest {
    public static void main(String[] args) throws Exception{
        //创建输出流对象
        FileOutputStream outputStream = new FileOutputStream("D:\\dev\\JavaLearn\\CRM项目（SSM框架版本）\\serverDir\\firstExcel.xls");

        //创建工作文本对象
        HSSFWorkbook workbook = new HSSFWorkbook();

        //操作excel，创建了一个新的页
        HSSFSheet sheet =  workbook.createSheet("第一页,学生工作表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);

        //设置样式的对象
        HSSFCellStyle style = workbook.createCellStyle();
        //设置好样式
        style.setAlignment(HorizontalAlignment.CENTER);

        cell.setCellValue("学号");

        cell = row.createCell(1);
        cell.setCellValue("姓名");

        cell = row.createCell(2);
        cell.setCellValue("年龄");

        for (int i = 1; i < 64; i++) {
            //行
            row = sheet.createRow(i);
            //列
            cell = row.createCell(0);
            cell.setCellValue("1911010" + i);

            cell = row.createCell(1);
            cell.setCellValue("md" + i);
            //给名称的这一栏数据添加样式
            cell.setCellStyle(style);


            cell = row.createCell(2);
            cell.setCellValue("age" + i);

        }

        //将excel数据导出到文件中
        workbook.write(outputStream);

        //释放资源
        outputStream.close();
        workbook.close();
    }
}

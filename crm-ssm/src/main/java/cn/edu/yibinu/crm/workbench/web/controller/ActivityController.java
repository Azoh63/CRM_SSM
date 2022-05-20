package cn.edu.yibinu.crm.workbench.web.controller;

import cn.edu.yibinu.crm.commons.domain.ReturnObject;
import cn.edu.yibinu.crm.commons.utils.Contents;
import cn.edu.yibinu.crm.commons.utils.DateFormatUtils;
import cn.edu.yibinu.crm.commons.utils.HSSFUtils;
import cn.edu.yibinu.crm.commons.utils.UUIDUtils;
import cn.edu.yibinu.crm.settings.domain.User;
import cn.edu.yibinu.crm.settings.service.UserService;
import cn.edu.yibinu.crm.workbench.domain.Activity;
import cn.edu.yibinu.crm.workbench.domain.ActivityRemark;
import cn.edu.yibinu.crm.workbench.service.ActivityRemarkService;
import cn.edu.yibinu.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Controller
public class ActivityController {

    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request, Model model) {
        //去数据库查所有的用户信息
        List<User> userList = userService.queryAllUsers();
        //将用户信息封装到request域中
        //request.setAttribute("userList",userList);
        model.addAttribute("userList",userList);


        //验证是否取出用户信息，遍历list
        /*for (User user : userList){
            System.out.println(user.getName());
        }*/
        //跳转页面到index.html
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/create.do")
    @ResponseBody
    public ReturnObject createActivity(Activity activity, HttpSession session){
        int code;   //数据库返回的影响行数
        ReturnObject object = null; //json

        //前台传过来的数据并不完整
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateFormatUtils.dateTimeFormat(new Date()));
        User user =  (User)session.getAttribute(Contents.SESSION_USER);
        activity.setCreateBy(user.getId());

        //在使用其他层的方法时，可能发生异常
        try{
            code = activityService.saveActivity(activity);
            //ajax请求的数据封装成了一个对象
            object = new ReturnObject();

            if(code > 0){  //对数据库的影响行数有一行就说明插入成功
                object.setCode(Contents.RETURN_CODE_SUCCESS);
            }else{
                object.setCode(Contents.RETURN_CODE_FAIL);
                object.setMessage("插入数据失败");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return object;
    }

    @RequestMapping(value = "/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name, String owner, String startDate, String endDate, Integer pageNo, Integer pageSize){
        //查询一个用户信息列表
        /*List<User> userList = userService.queryAllUsers();
        session.setAttribute("userList",userList);*/

        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("pageNo",(pageNo - 1)*pageSize);
        map.put("pageSize",pageSize);

        //这个map是作为json格式返回的，spring会将实体类和map自动解析成json格式
        Map<String,Object> retMap = new HashMap<>();
        //查出来的列表
        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        //还需要查总记录条数，先就查列表测试一次

        //查出总记录条数
        int totalRows = activityService.queryActivityByConditionForTotal(map);

        //将两个数据封装成json对象
        retMap.put("activityList",activityList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }

    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(String[] id){
        ReturnObject ret = new ReturnObject();
        try {
            //调用service层去删除
            int code = activityService.deleteActivityByIds(id);
            //传回来的数据不是0就成功了
            if(code >= 0){
                ret.setCode(Contents.RETURN_CODE_SUCCESS);
            }else{
                ret.setCode(Contents.RETURN_CODE_FAIL);
                ret.setMessage("系统忙，请稍后...");
            }
        }catch (Exception e){
            e.printStackTrace();
            ret.setCode(Contents.RETURN_CODE_FAIL);
            ret.setMessage("系统忙，请稍后...");
        }
        return ret;
    }

    /**
     * 这个是根据选择需要修改的市场活动来获取该市场活动的所有信息显示在模态窗口上
     * @param id
     * @return 是一个json，返回实体类对象
     */
    @RequestMapping(value = "/workbench/activity/queryActivityById.do",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object queryActivityById(String id){
        Activity activity = activityService.queryActivityById(id);
        if(activity != null){   //查询到确实有该对象
            return activity;
        }else return null;
    }

    /**
     * 点击更新按钮，发出修改数据库的请求
     * @param activity 前端获取的模态窗口中各个文本框中的值
     * @return 是一个ReturnObject对象
     */
    @RequestMapping("/workbench/activity/editActivity.do")
    @ResponseBody
    public Object editActivity(Activity activity,HttpSession session){
        ReturnObject retObj = new ReturnObject();
        //修改人和修改时间需要后台生成传过去
        activity.setEditTime(DateFormatUtils.dateTimeFormat(new Date()));
        User user = (User) session.getAttribute(Contents.SESSION_USER);
        activity.setEditBy(user.getId());

        //前端已经完成了表单验证，直接调用数据库修改就行
        try{
            int code = activityService.editActivity(activity);
            //判断修改是否成功
            if(code == 1){
                retObj.setCode(Contents.RETURN_CODE_SUCCESS);
            }else{
                retObj.setCode(Contents.RETURN_CODE_FAIL);
                retObj.setMessage("系统忙，请稍后...");
            }
        }catch (Exception e){
            e.printStackTrace();
            retObj.setCode(Contents.RETURN_CODE_FAIL);
            retObj.setMessage("系统忙，请稍后...");
        }
        return retObj;
    }

    /**
     * 用来测试poi的一个测试方法
     * @param response
     * @throws Exception
     */
    @RequestMapping("/workbench/activity/fileDownload.do")
    public void fileDownload(HttpServletResponse response) throws Exception{
        //设置响应类型
        response.setContentType("application/octet-stream;charset=utf-8");

        //将硬盘上的文件拿到
        FileInputStream inputStream = new FileInputStream("D:/dev/JavaLearn/CRM项目（SSM框架版本）/serverDir/firstExcel.xls");
        //获取输出流，用来输出到浏览器上
        OutputStream outputStream = response.getOutputStream();

        //设置响应头信息，让浏览器默认不打开该类型的文件，但是需要提供下载文件的窗口
        response.addHeader("Content-Disposition","attachment;filename=myFirst.xls");

        //拿到之后需要读
        int len = 0;
        byte[] buff = new byte[256];
        while((len = inputStream.read(buff)) != -1){
            outputStream.write(buff,0,len);
        }

        //使用完后都需要释放流，但是有一个原则：由谁开的就由谁关闭
        //outputStream是由tomcat获取的response对象来拿到的，所以应该由tomcat关闭流
        inputStream.close();
        outputStream.flush();
    }

    /**
     * 控制器方法用来将xls文件返回给前台（导出）
     */
    @RequestMapping("/workbench/activity/activityFileDownloadForAll.do")
    public void activityFileDownloadForAll(HttpServletResponse response) {
        //设置响应类型
        response.setContentType("application/octet-stream;charset=utf-8");
        //设置响应头信息
        response.addHeader("Content-Disposition","attachment;filename=activityList2222.xls");

        //先准备数据
        List<Activity> activityList = activityService.queryActivityList();

        HSSFRow row = null;
        HSSFCell cell = null;
        //因为这个读写异常如果只是让服务器知道就没有意义，需要让前台也知道
        try{
            //将文件填充数据
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("市场活动列表");

            //制作表头
            row = sheet.createRow(0);
            cell = row.createCell(0);
            cell.setCellValue("所有者");
            cell = row.createCell(1);
            cell.setCellValue("名字");
            cell = row.createCell(2);
            cell.setCellValue("开始日期");
            cell = row.createCell(3);
            cell.setCellValue("结束日期");
            cell = row.createCell(4);
            cell.setCellValue("成本");
            cell = row.createCell(5);
            cell.setCellValue("描述");
            cell = row.createCell(6);
            cell.setCellValue("创建时间");
            cell = row.createCell(7);
            cell.setCellValue("创建人");
            cell = row.createCell(8);
            cell.setCellValue("修改人");
            cell = row.createCell(9);
            cell.setCellValue("修改时间");

            Activity activity = null;
            //excel表中的数据
            for (int i = 0; i < activityList.size(); i++) {
                row = sheet.createRow(i+1);
                //每一行都对应一个市场活动
                activity = activityList.get(i);
                for (int j = 0; j < 10; j++) {
                    //有10列，暂时写成固定值
                    cell = row.createCell(j);
                    if(j == 0){
                        cell.setCellValue(activity.getOwner());
                    }
                    if(j == 1){
                        cell.setCellValue(activity.getName());
                    }
                    if(j == 2){
                        cell.setCellValue(activity.getStartDate());
                    }
                    if(j == 3){
                        cell.setCellValue(activity.getEndDate());
                    }
                    if(j == 4){
                        cell.setCellValue(activity.getCost());
                    }
                    if(j == 5){
                        cell.setCellValue(activity.getDescription());
                    }
                    if(j == 6){
                        cell.setCellValue(activity.getCreateTime());
                    }
                    if(j == 7){
                        cell.setCellValue(activity.getCreateBy());
                    }
                    if(j == 8){
                        cell.setCellValue(activity.getEditBy());
                    }
                    if(j == 9){
                        cell.setCellValue(activity.getEditTime());
                    }
                }
            }
            OutputStream outputStream = response.getOutputStream();
            //行和列都写完了
            wb.write(outputStream);
            //获取输入流
            /*FileInputStream inputStream = new FileInputStream("D:\\dev\\JavaLearn\\CRM项目（SSM框架版本）\\serverDir\\activityList.xls");
            OutputStream activityOutput = response.getOutputStream();*/
            /*int len = 0;
            byte[] buffer = new byte[1024];
            while((len=inputStream.read(buffer)) != -1){
                activityOutput.write(buffer,0,len);
            }*/
           /* activityOutput.flush();
            outputStream.close();
            inputStream.close();*/

           outputStream.flush();
           wb.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 实现文件导入操作
     * @return 返回的是json数据
     */
    @RequestMapping("/workbench/activity/activityListFileUpload.do")
    @ResponseBody
    public Object activityListFileUpload(@RequestParam MultipartFile excelFile,HttpSession session){
        ReturnObject retObj = new ReturnObject();

        //判断文件名是不是以.xls结尾；如果是，就不需要再判断，直接返回json数据
        String name = excelFile.getOriginalFilename();

        //成功导入数据的条数
        int totalActivityNum = 0;

        //并且对文件类型的判断，xls可能是XLS、Xls、xLS等，需要将文件类型字符全部转换成小写字母
        String suffix = name.substring(name.lastIndexOf("."),name.length());
        suffix = suffix.toLowerCase();

        if(!suffix.equals(".xls")){
            retObj.setCode(Contents.RETURN_CODE_FAIL);
            retObj.setMessage("文件类型必须是.xls");
            return retObj;
        }

        //文件类型正确就可以开始解析文件了
        try{
            InputStream is = excelFile.getInputStream();
            HSSFWorkbook wb = new HSSFWorkbook(is);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row = null;
            HSSFCell cell = null;
            String str = "";

            List<Activity> activityList = new ArrayList<>();

            //从第二行开始读数据
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                //uuid应该在每一行生成，否则就只会生成一个id会有主键冲突
                Activity activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                User user = (User)session.getAttribute(Contents.SESSION_USER);
                activity.setCreateBy(user.getId());
                activity.setCreateTime(DateFormatUtils.dateTimeFormat(new Date()));

                //读到每一行的最后一列
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    str = HSSFUtils.getCellValue(cell);

                    //开始向activity实体类对象写数据并封装到list当中
                    if(j == 0){
                        activity.setOwner(str);
                    }
                    if(j == 1){
                        activity.setName(str);
                    }
                    if(j == 2){
                        activity.setStartDate(str);
                    }
                    if(j == 3){
                        activity.setEndDate(str);
                    }
                    if(j == 4){
                        activity.setCost(str);
                    }
                    if(j == 5){
                        activity.setDescription(str);
                        //准备好了一个市场活动就加入list中
                        activityList.add(activity);
                    }
                }
            }

            //已经将文件中的市场活动拿到了
            totalActivityNum = activityService.updateActivityListForFile(activityList);

        }catch (Exception e){
            e.printStackTrace();
            retObj.setCode(Contents.RETURN_CODE_FAIL);
            retObj.setMessage("系统忙，请稍后...");
            return retObj;
        }

        retObj.setCode(Contents.RETURN_CODE_SUCCESS);
        retObj.setRetData(totalActivityNum);
        return retObj;
    }

    @RequestMapping("/workbench/activity/activityFileDownloadForChosen.do")
    public void activityFileDownloadForChosen(String[] id,HttpServletResponse response){
        //设置响应类型
        response.setContentType("application/octet-stream;charset=utf-8");
        //设置响应头信息
        response.addHeader("Content-Disposition","attachment;filename=activity.xls");

        ReturnObject retObj = new ReturnObject();
        //接收到从前台传过来的id
        //用这些id去查市场活动列表
        HSSFRow row = null;
        HSSFCell cell = null;

        //因为这个读写异常如果只是让服务器知道就没有意义，需要让前台也知道
        try{
            List<Activity> activityList = activityService.queryActivityListByIds(id);
            //将文件填充数据
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("市场活动列表");

            //制作表头
            row = sheet.createRow(0);
            cell = row.createCell(0);
            cell.setCellValue("所有者");
            cell = row.createCell(1);
            cell.setCellValue("名字");
            cell = row.createCell(2);
            cell.setCellValue("开始日期");
            cell = row.createCell(3);
            cell.setCellValue("结束日期");
            cell = row.createCell(4);
            cell.setCellValue("成本");
            cell = row.createCell(5);
            cell.setCellValue("描述");
            cell = row.createCell(6);
            cell.setCellValue("创建时间");
            cell = row.createCell(7);
            cell.setCellValue("创建人");
            cell = row.createCell(8);
            cell.setCellValue("修改人");
            cell = row.createCell(9);
            cell.setCellValue("修改时间");

            Activity activity = null;
            //excel表中的数据
            for (int i = 0; i < activityList.size(); i++) {
                row = sheet.createRow(i+1);
                //每一行都对应一个市场活动
                activity = activityList.get(i);
                for (int j = 0; j < 10; j++) {
                    //有10列，暂时写成固定值
                    cell = row.createCell(j);
                    if(j == 0){
                        cell.setCellValue(activity.getOwner());
                    }
                    if(j == 1){
                        cell.setCellValue(activity.getName());
                    }
                    if(j == 2){
                        cell.setCellValue(activity.getStartDate());
                    }
                    if(j == 3){
                        cell.setCellValue(activity.getEndDate());
                    }
                    if(j == 4){
                        cell.setCellValue(activity.getCost());
                    }
                    if(j == 5){
                        cell.setCellValue(activity.getDescription());
                    }
                    if(j == 6){
                        cell.setCellValue(activity.getCreateTime());
                    }
                    if(j == 7){
                        cell.setCellValue(activity.getCreateBy());
                    }
                    if(j == 8){
                        cell.setCellValue(activity.getEditBy());
                    }
                    if(j == 9){
                        cell.setCellValue(activity.getEditTime());
                    }
                }
            }

            OutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);

            outputStream.flush();
            wb.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取市场活动详情页面
     * @return 是一个视图
     */
    @RequestMapping("/workbench/activity/getActivityDetail.do")
    public String getActivityDetail(String id,Model model) {
        //获取市场活动对象
        Activity activity = activityService.queryActivityByIdForDetail(id);

        //将获取到的市场活动封装到request
        model.addAttribute("activity",activity);

        //查询市场活动相关备注
        List<ActivityRemark> remarkList = activityRemarkService.queryActivityRemarkByActivityId(id);

        model.addAttribute("remarkList",remarkList);

        //返回给视图解析器
        return "workbench/activity/detail";
    }

    @RequestMapping("/workbench/activity/saveActivityRemark.do")
    @ResponseBody
    public Object saveActivityRemark(ActivityRemark remark,HttpSession session){
        ReturnObject retObject = new ReturnObject();
        //添加一条备注
        remark.setId(UUIDUtils.getUUID());
        remark.setCreateTime(DateFormatUtils.dateTimeFormat(new Date()));
        //创建人的数据是一个id
        User user = (User) session.getAttribute(Contents.SESSION_USER);
        remark.setCreateBy(user.getId());
        remark.setEditFlag("0");

        try{
            //插入数据
            int code = activityRemarkService.saveActivityRemark(remark);
            if(code == 1){
                //新增备注成功
                retObject.setCode(Contents.RETURN_CODE_SUCCESS);
                //把remark对象传回去方便动态刷新
                retObject.setRetData(remark);
            }else{
                retObject.setCode(Contents.RETURN_CODE_FAIL);
                retObject.setMessage("系统忙，请稍后...");
            }
        }catch (Exception e){
            e.printStackTrace();
            retObject.setCode(Contents.RETURN_CODE_FAIL);
            retObject.setMessage("系统忙，请稍后...");
        }
        return retObject;
    }

    @RequestMapping("/workbench/activity/deleteActivityRemarkById")
    @ResponseBody
    public Object deleteActivityRemarkById(String id){
        ReturnObject retObj = new ReturnObject();
        try{
            //通过备注id删除备注
            int ret = activityRemarkService.deleteActivityRemarkById(id);
            if(ret > 0){
                //删除成功
                retObj.setCode(Contents.RETURN_CODE_SUCCESS);
            }else{
                retObj.setCode(Contents.RETURN_CODE_SUCCESS);
                retObj.setMessage("系统忙，请稍后重试...");
            }
        }catch (Exception e){
            e.printStackTrace();
            retObj.setCode(Contents.RETURN_CODE_SUCCESS);
            retObj.setMessage("系统忙，请稍后重试...");
        }
        return retObj;
    }

    @RequestMapping("/workbench/activity/editActivityRemark.do")
    @ResponseBody
    public Object editActivityRemark(ActivityRemark remark,HttpSession session){
        ReturnObject retObj = new ReturnObject();
        //修改修改标志
        remark.setEditFlag(Contents.REMARK_EDIT_FLAG_YES);

        //拿到修改时间
        remark.setEditTime(DateFormatUtils.dateTimeFormat(new Date()));

        //修改人
        User user = (User) session.getAttribute(Contents.SESSION_USER);
        remark.setEditBy(user.getId());

        try{
            //传输数据到数据库
            int ret = activityRemarkService.editActivityRemark(remark);
            if(ret > 0){
                retObj.setCode(Contents.RETURN_CODE_SUCCESS);
                retObj.setRetData(remark);
            }else{
                retObj.setCode(Contents.RETURN_CODE_FAIL);
                retObj.setMessage("系统忙，请稍后重试...");
            }
        }catch (Exception e){
            e.printStackTrace();
            retObj.setCode(Contents.RETURN_CODE_FAIL);
            retObj.setMessage("系统忙，请稍后重试...");
        }
        return retObj;
    }
}

















































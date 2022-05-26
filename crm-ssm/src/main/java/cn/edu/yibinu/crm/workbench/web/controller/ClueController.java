package cn.edu.yibinu.crm.workbench.web.controller;

import cn.edu.yibinu.crm.commons.domain.ReturnObject;
import cn.edu.yibinu.crm.commons.utils.Contents;
import cn.edu.yibinu.crm.commons.utils.DateFormatUtils;
import cn.edu.yibinu.crm.commons.utils.UUIDUtils;
import cn.edu.yibinu.crm.settings.domain.DicValue;
import cn.edu.yibinu.crm.settings.domain.User;
import cn.edu.yibinu.crm.settings.service.UserService;
import cn.edu.yibinu.crm.workbench.domain.Activity;
import cn.edu.yibinu.crm.workbench.domain.Clue;
import cn.edu.yibinu.crm.workbench.domain.ClueActivityRelation;
import cn.edu.yibinu.crm.workbench.domain.ClueRemark;
import cn.edu.yibinu.crm.workbench.service.ClueActivityRelationService;
import cn.edu.yibinu.crm.workbench.service.ClueRemarkService;
import cn.edu.yibinu.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {
    @Autowired
    private ClueService clueService;

    @Autowired
    private UserService userService;

    @Autowired
    private ClueActivityRelationService clueActivityRelationService;

    @Autowired
    private ClueRemarkService clueRemarkService;

    /**
     * 跳转到首页的同时去请求数据字典值
     * @return
     */
    @RequestMapping("/workbench/clue/index.do")
    public String queryClueDicValueByDicType(HttpServletRequest request){
        //还要查所有者
        List<User> userList = userService.queryAllUsers();
        //从前面传过来了三个typeCode变量
        List<DicValue> appellationList  = clueService.queryClueDicValueByTypeCode("appellation");
        List<DicValue> clueStateList = clueService.queryClueDicValueByTypeCode("clueState");
        List<DicValue> sourceList = clueService.queryClueDicValueByTypeCode("source");

        request.setAttribute("appellationList",appellationList);
        request.setAttribute("clueStateList",clueStateList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("userList",userList);
        return "workbench/clue/index";
    }

    @RequestMapping("/workbench/clue/addClue.do")
    @ResponseBody
    public Object addClue(Clue clue, HttpSession session){
        ReturnObject retObj = new ReturnObject();
        //自动封装参数
        clue.setId(UUIDUtils.getUUID());
        User user = (User) session.getAttribute(Contents.SESSION_USER);
        clue.setCreateBy(user.getId());
        clue.setCreateTime(DateFormatUtils.dateTimeFormat(new Date()));

        try{
            //某些字段由后台生成后再进入后台新增数据
            int ret = clueService.addClue(clue);
            if(ret > 0){
                //成功
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

    @RequestMapping("/workbench/clue/queryClueByConditionForPage.do")
    @ResponseBody
    public Object queryClueByConditionForPage(Integer pageNo,Integer pageSize,String fullname,String company,String phone,String source,String owner,String mphone,String state){
        ReturnObject retObj = new ReturnObject();
        //封装参数成map
        Map<String,Object> map = new HashMap<>();
        pageNo = pageNo - 1;
        map.put("pageNo",pageNo);
        map.put("pageSize",pageSize);
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("state",state);

        //作为返回的map
        Map<String,Object> retMap = new HashMap<>();
        //开始根据条件查询线索
        List<Clue> clueList = clueService.queryClueByConditionForPage(map);
        int totalRows = clueService.queryClueByConditionForTotal();

        //准备返回的数据
        retMap.put("clueList",clueList);
        retMap.put("totalRows",totalRows);

        //发出数据给前台
        return retMap;
    }

    @RequestMapping("/workbench/clue/getClueForDetail.do")
    public String getClueForDetail(String id, HttpServletRequest request){
        //去后台查出线索的所有字段
        Clue clue = clueService.queryClueForDetail(id);
        if(clue != null){
            //封装结果到request
            request.setAttribute("clue",clue);
        }else{
            System.out.println("没有查询到相应线索");
        }
        return "workbench/clue/detail";
    }

    /**
     * 查询没有被联系的市场活动
     * @param clueId
     * @return
     */
    @RequestMapping("/workbench/clue/queryActivityForContactClue.do")
    @ResponseBody
    public Object queryActivityForContactClue(String clueId){
        ReturnObject retObj = new ReturnObject();
        try{
            //通过前端传过来的线索id查没有和线索联系过的市场活动
            List<Activity> activityList = clueService.queryActivityForContactClue(clueId);
            //如果所有的市场活动都和线索关联，那就是空；所以，判断没有查到的条件是发生了异常
            retObj.setCode(Contents.RETURN_CODE_SUCCESS);
            retObj.setRetData(activityList);
        }catch (Exception e){
            e.printStackTrace();
            retObj.setCode(Contents.RETURN_CODE_FAIL);
            retObj.setMessage("系统忙，轻稍后...");
        }
        return retObj;
    }

    @RequestMapping("/workbench/clue/addClueActivityRelation.do")
    @ResponseBody
    public Object addClueActivityRelation(String[] activityId,String clueId){
        //封装前台传过来的两个参数到ClueActivityRelation对象中，并把对象加到list去
        ReturnObject retObj = new ReturnObject();
        //这个参数在循环中去赋值
        ClueActivityRelation car = null;
        //存放多个对象
        List<ClueActivityRelation> carList = new ArrayList<>();
        try{
            for (String aId : activityId){
                car = new ClueActivityRelation();
                car.setId(UUIDUtils.getUUID());
                car.setClueId(clueId);
                car.setActivityId(aId);
                //每将一个对象封装好后就添加到list当中
                carList.add(car);
            }
            //循环结束，数据准备完毕
            int ret = clueActivityRelationService.addClueActivityRelation(carList);

            //判断影响记录条数
            if(ret > 0){
                retObj.setCode(Contents.RETURN_CODE_SUCCESS);
            }else{
                retObj.setCode(Contents.RETURN_CODE_FAIL);
                retObj.setMessage("系统忙，请稍后...");
            }
        }catch (Exception e){
            retObj.setCode(Contents.RETURN_CODE_FAIL);
            retObj.setMessage("系统忙，请稍后...");
            e.printStackTrace();
        }
        //返回json对象
        return retObj;
    }

    @RequestMapping("/workbench/clue/queryActivityByNameForRelation.do")
    @ResponseBody
    public Object queryActivityByNameForRelation(String clueId,String name){
        //模糊查询出没有被关联并且名字和传参的名字类似
        return clueActivityRelationService.queryActivityByNameForRelation(name,clueId);
    }

    /**
     * 插入一条线索备注
     * @param clueRemark 线索备注对象
     * @return
     */
    @RequestMapping("/workbench/clue/addClueRemark.do")
    @ResponseBody
    public Object addClueRemark(ClueRemark clueRemark,HttpSession session){
        ReturnObject retObj = new ReturnObject();
        //将参数的属性配置完整
        clueRemark.setId(UUIDUtils.getUUID());
        User user = (User) session.getAttribute(Contents.SESSION_USER);
        clueRemark.setCreateBy(user.getId());
        clueRemark.setCreateTime(DateFormatUtils.dateTimeFormat(new Date()));
        clueRemark.setEditFlag("0");    //没有经过修改就是0

        try{
            //插入数据
            int ret = clueRemarkService.addClueRemark(clueRemark);
            if(ret > 0){
                retObj.setCode(Contents.RETURN_CODE_SUCCESS);
                retObj.setRetData(clueRemark);
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

    @RequestMapping("/workbench/clue/queryClueRemarkList.do")
    @ResponseBody
    public Object queryClueRemarkList(String clueId){
        //通过线索id查询出所有相关的线索备注列表
        return clueRemarkService.queryClueRemarkList(clueId);
    }

    @RequestMapping("/workbench/clue/toConvert.do/{clueId}")
    public String toConvert(@PathVariable(value = "clueId") String clueId, HttpServletRequest request){
        String typeCode="stage";

        //通过clueId查线索
        Clue clue = clueService.queryClueByIdForConvert(clueId);
        //把线索添加到request域当中
        request.setAttribute("clue",clue);

        //查dicValue
        List<DicValue> dicValueList = clueService.queryDicValueForConvert(typeCode);
        request.setAttribute("dicValueList",dicValueList);

        //让页面使用request域来丰富页面
        return "workbench/clue/convert";
    }

    @RequestMapping("/workbench/clue/queryActivityList.do")
    @ResponseBody
    public Object queryActivityList(String activityName){
        //根据市场活动的名字模糊查询出市场活动列表
        return clueService.queryActivityByNameForConvert(activityName);
    }

    /**
     * 根据市场活动id查询一条市场活动
     * @param activityId
     * @return
     */
    @RequestMapping("/workbench/clue/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String activityId){
        return clueService.queryActivityById(activityId);
    }
}





























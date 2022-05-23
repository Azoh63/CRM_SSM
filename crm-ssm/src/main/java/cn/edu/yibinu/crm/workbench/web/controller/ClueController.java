package cn.edu.yibinu.crm.workbench.web.controller;

import cn.edu.yibinu.crm.commons.domain.ReturnObject;
import cn.edu.yibinu.crm.commons.utils.Contents;
import cn.edu.yibinu.crm.commons.utils.DateFormatUtils;
import cn.edu.yibinu.crm.commons.utils.UUIDUtils;
import cn.edu.yibinu.crm.settings.domain.DicValue;
import cn.edu.yibinu.crm.settings.domain.User;
import cn.edu.yibinu.crm.settings.service.UserService;
import cn.edu.yibinu.crm.workbench.domain.Clue;
import cn.edu.yibinu.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ClueController {
    @Autowired
    private ClueService clueService;

    @Autowired
    private UserService userService;

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
}





















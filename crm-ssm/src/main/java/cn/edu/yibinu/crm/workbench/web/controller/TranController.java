package cn.edu.yibinu.crm.workbench.web.controller;

import cn.edu.yibinu.crm.commons.domain.ReturnObject;
import cn.edu.yibinu.crm.commons.utils.Contents;
import cn.edu.yibinu.crm.commons.utils.DateFormatUtils;
import cn.edu.yibinu.crm.commons.utils.UUIDUtils;
import cn.edu.yibinu.crm.settings.domain.DicValue;
import cn.edu.yibinu.crm.settings.domain.User;
import cn.edu.yibinu.crm.workbench.domain.Customer;
import cn.edu.yibinu.crm.workbench.domain.Tran;
import cn.edu.yibinu.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class TranController {
    @Autowired
    private TranService tranService;
    @RequestMapping("/workbench/transaction/toTranIndex.do")
    public String toTranIndex(HttpServletRequest request){
        //去查三个数据字典值
        List<DicValue> stageList = tranService.queryDicValueForTran("stage");
        List<DicValue> typeList = tranService.queryDicValueForTran("transactionType");
        List<DicValue> sourceList = tranService.queryDicValueForTran("source");

        //添加到request域中
        request.setAttribute("stageList",stageList);
        request.setAttribute("typeList",typeList);
        request.setAttribute("sourceList",sourceList);

        return "workbench/transaction/index";
    }

    @RequestMapping("/workbench/transaction/toSaveTran.do")
    public String toSaveTran(HttpServletRequest request){
        //首先需要查出所有用户列表
        List<User> userList = tranService.queryAllUsers();
        request.setAttribute("userList",userList);

        //然后根据下拉列表框的typeCode来查出数据字典值
        List<DicValue> stageList = tranService.queryDicValueForTran("stage");
        List<DicValue> typeList = tranService.queryDicValueForTran("transactionType");
        List<DicValue> sourceList = tranService.queryDicValueForTran("source");

        //添加到request域当中
        request.setAttribute("stageList",stageList);
        request.setAttribute("typeList",typeList);
        request.setAttribute("sourceList",sourceList);

        return "workbench/transaction/save";
    }

    /**
     * 通过stage来从配置文件中取出possibility
     * @return
     */
    @RequestMapping("/workbench/transaction/getPossibility.do")
    @ResponseBody
    public String getPossibility(String stage){
        //拿到配置文件
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        //通过stage拿
        return bundle.getString(stage);
    }

    @RequestMapping("/workbench/transaction/addFullTran.do")
    @ResponseBody
    public Object addFullTran(Tran tran, HttpSession session){
        //定义返回的对象
        ReturnObject retObject = new ReturnObject();
        User user = (User) session.getAttribute(Contents.SESSION_USER);
        //封装参数
        tran.setId(UUIDUtils.getUUID());
        tran.setCreateTime(DateFormatUtils.dateTimeFormat(new Date()));
        tran.setCreateBy(user.getId());
        //其他参数前端传过来时会自动封装到tran对象当中,但是possibility属性需要在tran对象中添加属性
        //possibility属性也会自动封装

        //customerId字段需要通过用户名去查出来
        Customer customer = tranService.queryCustomerByName(tran.getCustomerId());
        if(customer != null){
            tran.setCustomerId(customer.getId());
        }else{
            customer = new Customer();
        }

        try {
            int ret = tranService.addFullTran(tran);
            if(ret > 0){
                retObject.setCode(Contents.RETURN_CODE_SUCCESS);
            }else{
                retObject.setCode(Contents.RETURN_CODE_FAIL);
                retObject.setMessage("系统忙，请稍后...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            retObject.setCode(Contents.RETURN_CODE_FAIL);
            retObject.setMessage("系统忙，请稍后...");
        }
        return retObject;
    }

    @RequestMapping("/workbench/transaction/queryCustomerByNameWithLike.do")
    @ResponseBody
    public Object queryCustomerByNameWithLike(String customerName){
        //通过前台拿到的名字获取出客户列表
        return tranService.queryCustomerByNameWithLike(customerName);
    }

    @RequestMapping("/workbench/transaction/queryTranListForPage.do")
    @ResponseBody
    public Object queryTranListForPage(Integer pageNo,Integer pageSize){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        //数据库中的分页是从0开始的
        pageNo = pageNo - 1;
        map.put("pageNo",pageNo);
        map.put("pageSize",pageSize);

        int totalRows = tranService.queryTotalRowsForPage();
        //查询出交易列表
        List<Tran> tranList = tranService.queryTranListForPage(map);
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("totalRows",totalRows);
        retMap.put("tranList",tranList);

        return retMap;
    }

    @RequestMapping("/workbench/transaction/toTranDetail.do")
    public String toTranDetail(String id,HttpServletRequest request){
        Tran tran = tranService.queryTranForDetail(id);

        //根据阶段获取possibility
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(tran.getStage());

        //获取所有的DicValue
        List<DicValue> dicValueList = tranService.queryAllDicValueByTypeCode("stage");
        //将查询到的放到request域当中，方便阶段图标使用
        request.setAttribute("dicValueList",dicValueList);

        //将tran的possibility属性添加进tran中
        tran.setPossibility(possibility);
        //将查到的这个对象放入request域当中
        request.setAttribute("tran",tran);
        return "workbench/transaction/detail";
    }
}











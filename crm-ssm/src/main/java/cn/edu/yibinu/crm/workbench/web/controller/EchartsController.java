package cn.edu.yibinu.crm.workbench.web.controller;

import cn.edu.yibinu.crm.commons.domain.EchartsVo;
import cn.edu.yibinu.crm.commons.utils.Contents;
import cn.edu.yibinu.crm.commons.utils.DateFormatUtils;
import cn.edu.yibinu.crm.commons.utils.UUIDUtils;
import cn.edu.yibinu.crm.settings.domain.DicValue;
import cn.edu.yibinu.crm.settings.domain.User;
import cn.edu.yibinu.crm.workbench.domain.Tran;
import cn.edu.yibinu.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class EchartsController {
    @Autowired
    private TranService tranService;

    @RequestMapping("/workbench/chart/transaction/index.do")
    public String toEchartsIndex(){
        return "workbench/chart/transaction/index";
    }

    @RequestMapping("/workbench/chart/transaction/getCharts.do")
    @ResponseBody
    public Object getCharts(){
        //返回的value是该阶段的数量，而不是stage对应的possibility
        EchartsVo vo =  null;
        List<EchartsVo> voList = new ArrayList<>();
        List<Tran> tranList = tranService.queryTranForEcharts();
        Integer value = 0;

        //前台需要的数据是name:60,value:资质审查，所以需要重新创建一个VO类来保存这两个属性
        //应该循环stageList
        List<DicValue> dicValueList = tranService.queryDicValueForTran("stage");
        for (int i = 0; i < dicValueList.size(); i++) {
            value = 0;
            vo = new EchartsVo();
            //去统计每一个stage的数量
            for (int j = 0; j < tranList.size(); j++) {
                if (tranList.get(j).getStage().equals(dicValueList.get(i).getId()) ){
                    //如果就是同一个，那么value的值+1
                    value++;
                    vo.setName(dicValueList.get(i).getValue());
                    vo.setValue(value);
                }
            }
            voList.add(vo);
        }
        return voList;
    }

    @RequestMapping("/workbench/chart/transaction/addLotsOfTrans.do")
    @ResponseBody
    public void addLotsOfTrans(HttpSession session){
        Tran tran = null;
        DicValue dicValue = null;
        String stageId = null;
        List<Tran> tranList = new ArrayList<>();
        //不需要获取参数，只需要生成对象就可以了
        User user = (User) session.getAttribute(Contents.SESSION_USER);
        //查出所有的dicValue
        List<DicValue> dicValueList = tranService.queryAllDicValueByTypeCode("stage");
        int j=0;  //用来循环多个dicValueList
        for (int i = 0; i < 1000; i++) {
            tran = new Tran();
            tran.setId(UUIDUtils.getUUID());
            tran.setOwner(user.getId());
            tran.setName("tran"+i);
            tran.setCreateTime(DateFormatUtils.dateTimeFormat(new Date()));
            if (j == dicValueList.size()){
                //就重新循环
                j = 0;
            }else{
                dicValue = dicValueList.get(j);
                stageId = dicValue.getId();
                j++;
            }
            tran.setStage(stageId);
            tranList.add(tran);
        }
        //最后再插入多条数据
        tranService.addLotsOfTrans(tranList);
    }
}








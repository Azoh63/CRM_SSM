package cn.edu.yibinu.crm.workbench.service;

import cn.edu.yibinu.crm.settings.domain.DicValue;
import cn.edu.yibinu.crm.settings.domain.User;
import cn.edu.yibinu.crm.workbench.domain.Customer;
import cn.edu.yibinu.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {
    List<DicValue> queryDicValueForTran(String typeCode);

    List<User> queryAllUsers();

    int addFullTran(Tran tran);

    List<String> queryCustomerByNameWithLike(String customerName);

    Customer queryCustomerByName(String customerName);

    List<Tran> queryTranListForPage(Map<String,Object> map);

    int queryTotalRowsForPage();

    Tran queryTranForDetail(String id);

    List<DicValue> queryAllDicValueByTypeCode(String typeCode);

    List<Tran> queryTranForEcharts();

    String queryStage(String id);

    int addLotsOfTrans(List<Tran> tranList);

}

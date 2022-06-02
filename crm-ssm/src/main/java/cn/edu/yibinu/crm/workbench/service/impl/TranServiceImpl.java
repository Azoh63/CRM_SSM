package cn.edu.yibinu.crm.workbench.service.impl;

import cn.edu.yibinu.crm.settings.domain.DicValue;
import cn.edu.yibinu.crm.settings.domain.User;
import cn.edu.yibinu.crm.settings.mapper.DicValueMapper;
import cn.edu.yibinu.crm.settings.mapper.UserMapper;
import cn.edu.yibinu.crm.workbench.domain.Customer;
import cn.edu.yibinu.crm.workbench.domain.Tran;
import cn.edu.yibinu.crm.workbench.mapper.CustomerMapper;
import cn.edu.yibinu.crm.workbench.mapper.TranMapper;
import cn.edu.yibinu.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("tranService")
public class TranServiceImpl implements TranService {
    @Autowired
    private DicValueMapper dicValueMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<DicValue> queryDicValueForTran(String typeCode) {
        return dicValueMapper.selectDicValueForTran(typeCode);
    }

    @Override
    public List<User> queryAllUsers() {
        return userMapper.selectAllUsers();
    }

    @Override
    public int addFullTran(Tran tran) {
        return tranMapper.insertFullTran(tran);
    }

    @Override
    public List<String> queryCustomerByNameWithLike(String customerName) {
        return customerMapper.selectCustomerByNameWithLike(customerName);
    }

    @Override
    public Customer queryCustomerByName(String customerName) {
        return customerMapper.selectCustomerByName(customerName);
    }

    @Override
    public List<Tran> queryTranListForPage(Map<String, Object> map) {
        return tranMapper.selectTranListForPage(map);
    }

    @Override
    public int queryTotalRowsForPage() {
        return tranMapper.selectTotalRowsForPage();
    }

    @Override
    public Tran queryTranForDetail(String id) {
        return tranMapper.selectTranForDetail(id);
    }

    @Override
    public List<DicValue> queryAllDicValueByTypeCode(String typeCode) {
        return dicValueMapper.selectAllDicValueByTypeCode(typeCode);
    }

    @Override
    public List<Tran> queryTranForEcharts() {
        return tranMapper.selectTranForEcharts();
    }

    @Override
    public String queryStage(String id) {
        return tranMapper.selectStage(id);
    }

    @Override
    public int addLotsOfTrans(List<Tran> tranList) {
        return tranMapper.insertLotsOfTrans(tranList);
    }

}
